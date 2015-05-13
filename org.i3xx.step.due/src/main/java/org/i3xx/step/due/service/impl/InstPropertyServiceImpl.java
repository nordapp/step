package org.i3xx.step.due.service.impl;

/*
 * #%L
 * NordApp OfficeBase :: due
 * %%
 * Copyright (C) 2014 - 2015 I.D.S. DialogSysteme GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.i3xx.step.due.service.model.InstPropertyService;
import org.i3xx.step.zero.service.impl.mandator.MandatorServiceImpl;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.util.basic.io.FilePath;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InstPropertyServiceImpl implements InstPropertyService {
	
	static Logger logger = LoggerFactory.getLogger(InstPropertyServiceImpl.class);
			
	/** The osgi bundle context */
	private BundleContext bundleContext;
	
	/**  */
	private Map<String, ServiceRegistration<?>> srvRegistration;

	public InstPropertyServiceImpl() {
		bundleContext = null;
		srvRegistration = new HashMap<String, ServiceRegistration<?>>();
	}
	
	/**
	 * The startup of the service
	 * @throws IOException 
	 */
	public void startUp() throws IOException {
		logger.info("PropertyService started");
	}

	/**
	 * Set the properties of the mandator
	 * 
	 * @param mandatorId The mandator to set
	 * @param path The property file
	 * @throws IOException 
	 */
	public void setProperties(String mandatorId, String groupId, String artifactId, String path) throws IOException {
		
		checkPermission("write", "property");
		logger.debug("Resource for mandator:{}, group:{} artifact:{} requested '{}'.",
				mandatorId, groupId, artifactId, path);
		
		//The registration key
		String regKey = mandatorId+"."+groupId+"."+artifactId+"="+path;
		
		Mandator mandator = MandatorServiceImpl.getMandator(bundleContext, mandatorId);
		if(mandator==null)
			throw new IOException("Not found: The requested mandator '"+mandatorId+
					"' is not available at this system.");
		
		FilePath filePath = FilePath.append( mandator.getPath(), "data/page/file/bundle", groupId, artifactId, path );
		File file = filePath.toFile();
		
		//
		// Read property file
		//
		if( !file.exists() )
			throw new IOException("Not found: The requested property '"+
					file.getAbsolutePath()+"' is not available at this system.");
		if( !file.isFile() )
			throw new IOException("Illegal: The requested property '"+
					file.getAbsolutePath()+"' is not a file.");
		
		//
		// Write the properties to the mandator
		//
		Map<String, String> props = new HashMap<String, String>();
		Properties p = readPropertyFile(file);
		for(Object k : p.keySet()){
			String key = (String)k;
			String val = p.getProperty(key);
			
			props.put(groupId+"."+artifactId+"."+key, val);
		}
		
		org.i3xx.step.zero.service.model.mandator.PropertyService srv =
				new org.i3xx.step.zero.service.impl.mandator.PropertyServiceImpl(props);
		
		//
		// You may insert the properties into the mandator,
		// but the ServiceTracker does this job for you and
		// what is about a new start of the MandatorService.
		//
		Dictionary<String, String> srvDict = new Hashtable<String, String>();
		srvDict.put(Mandator.MANDATORID, mandatorId);
		srvDict.put(Mandator.GROUPID, groupId);
		srvDict.put(Mandator.ARTIFACTID, artifactId);
		srvDict.put(InstPropertyService.PATH_ID, path);
		String cName = org.i3xx.step.zero.service.model.mandator.PropertyService.class.getName();
		
		// Unregister an old service
		try {
			String filter = "(&("+Mandator.MANDATORID+"="+mandatorId+")("+
					Mandator.GROUPID+"="+groupId+")("+
					Mandator.ARTIFACTID+"="+artifactId+")("+
					InstPropertyService.PATH_ID+"="+path+"))";
			ServiceReference<?>[] refs = bundleContext.getServiceReferences(cName, filter);
			if(refs!=null) {
				for(ServiceReference<?> ref : refs) {
					bundleContext.ungetService(ref);
					
					ServiceRegistration<?> sv = srvRegistration.remove(regKey);
					if(sv!=null)
						sv.unregister();
				}//for
			}//fi
		} catch (InvalidSyntaxException e) {
			logger.error("Error removing service. Proceed...", e);
		}
		// Register the new one
		ServiceRegistration<?> srg = bundleContext.registerService(cName, srv, srvDict);
		srvRegistration.put(regKey, srg);
	}
	
	/** Read a property file */
	private Properties readPropertyFile(File file) throws IOException {
		checkPermission("read", "property");
		
		Properties props = new Properties();
		FileInputStream istream = null;
		try {
			istream = new FileInputStream(file);
			props.load(istream);
			istream.close();
		}
		catch (Exception e) {
			if (e instanceof InterruptedIOException || e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
			logger.error("Could not read configuration file.", e);
			logger.error("Ignoring configuration file [{}].", file.getAbsoluteFile());
		} finally {
			if(istream != null) {
				try {
					istream.close();
				} catch(InterruptedIOException ignore) {
					Thread.currentThread().interrupt();
				} catch(Throwable ignore) {
				}
			}
		}
		return props;
	}

	/**
	 * @return the bundleContext
	 */
	public BundleContext getBundleContext() {
		return bundleContext;
	}

	/**
	 * @param bundleContext the bundleContext to set
	 */
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}
	
	/**
	 * Checks the permission of the Service ( read | write )
	 * 
	 * @param action The action to be done
	 * @param target The target the permission is requested for
	 */
	private static void checkPermission(String action, String target) {
		
		//TODO Implement the permission to access the mandator configuration
	}

}
