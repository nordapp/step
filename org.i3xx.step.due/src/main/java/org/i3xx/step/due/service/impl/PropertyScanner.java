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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.i3xx.step.due.service.model.InstPropertyService;
import org.i3xx.step.zero.service.impl.mandator.MandatorServiceImpl;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.step.zero.service.model.mandator.MandatorService;
import org.i3xx.util.basic.io.FilePath;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PropertyScanner {
	
	static Logger logger = LoggerFactory.getLogger(PropertyScanner.class);
			
	/** The osgi bundle context */
	private BundleContext bundleContext;
	
	/** The service */
	private InstPropertyService instPropertyService;

	public PropertyScanner() {
		bundleContext = null;
		instPropertyService = null;
	}
	
	/**
	 * 
	 */
	public void scanAll() {
		
		//
		// Gets the configured mandator and initialize the '0' session.
		//
		ServiceReference<MandatorService> msr = bundleContext.getServiceReference(MandatorService.class);
		MandatorService ms = bundleContext.getService(msr);
		if(ms==null)
			logger.warn("The mandator service is not available (maybe down or a version conflict).");
		
		for(String mandatorId : ms.getMandatorList()){
			
			Mandator mandator = ms.getMandator(mandatorId);
			if(mandator==null)
				logger.warn("The mandator '"+mandatorId+"' is not available on this system.");
			else
				logger.debug("The mandator {} is available at the path '{}'.", mandatorId, mandator.getPath());
			
			scan(mandator);
		}//for
		
	}
	
	/**
	 * @param mandatorId
	 */
	public void scan(String mandatorId) {
		Mandator mandator = MandatorServiceImpl.getMandator(bundleContext, mandatorId);
		if(mandator==null)
			logger.warn("The mandator '"+mandatorId+"' is not available on this system.");
		else
			logger.debug("The mandator {} is available at the path '{}'.", mandatorId, mandator.getPath());
		
		scan(mandator);
	}
	
	/**
	 * @param mandator
	 */
	private void scan(Mandator mandator) {
		
		logger.debug("The mandator is '"+mandator+"'");
		logger.debug("The mandator path is '"+mandator.getPath()+"'");
		FilePath filePath = FilePath.append( mandator.getPath(), "data/page/file/bundle" );
		File file = filePath.toFile();
		
		//
		// get the service
		//
		ServiceReference<InstPropertyService> psr = bundleContext.getServiceReference(InstPropertyService.class);
		if(psr==null) {
			logger.warn("The property service reference is not available.");
		}else{
			InstPropertyService ps = (InstPropertyService)bundleContext.getService(psr);
			if(ps==null){
				logger.warn("The property service is not available (maybe down or a version conflict).");
			}else{
				instPropertyService = ps;
			}//fi
		}//fi
		
		//
		// scan directories
		//
		
		// groups
		File[] gff = file.listFiles();
		if(gff!=null){
			for(File gf : gff) {
				logger.info("Scan mandator {} group {}", mandator.getId(), gf.getName());
				// artifacts
				File[] aff = gf.listFiles();
				if(aff!=null){
					for(File af : aff){
						logger.info("Scan mandator {} group {} artifact {}", mandator.getId(), gf.getName(), af.getName());
						// properties directory
						File d = new File(af, "properties");
						if(d.exists() && d.isDirectory()) {
							
							List<String> list = new ArrayList<String>();
							search(d, d.getName(), list);
							
							String mandatorId = mandator.getId();
							String groupId = gf.getName();
							String artifactId = af.getName();
							
							for(String pathId : list) {
								try {
									instPropertyService.setProperties(mandatorId, groupId, artifactId, pathId);
								} catch (IOException e) {
									logger.warn("", e);
								}
							}//for
						}//fi
					}//for
				}//fi
			}//for
		}//fi
	}
	
	/**
	 * Searches files and put the names into the list.
	 * 
	 * @param file The file to search from.
	 * @param parent The parent string ('' or file.getName() at begin)
	 * @param list The result list
	 */
	private void search(File file, String parent, List<String> list) {
		if(file.isFile()) {
			//Note: The parent contains the current filename
			list.add(parent);
			return;
		}
		
		File[] ff = file.listFiles();
		if(ff==null)
			return;
		
		for(File f:ff) {
			search(f, parent+"/"+f.getName(), list);
		}//for
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
	 * @return the instPropertyService
	 */
	public InstPropertyService getInstPropertyService() {
		return instPropertyService;
	}

	/**
	 * @param instPropertyService the instPropertyService to set
	 */
	public void setInstPropertyService(InstPropertyService instPropertyService) {
		this.instPropertyService = instPropertyService;
	}

}
