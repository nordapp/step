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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

import org.i3xx.step.due.service.model.ResourceService;
import org.i3xx.step.zero.service.impl.mandator.MandatorServiceImpl;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.util.basic.io.FilePath;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ResourceServiceImpl implements ResourceService {
	
	static Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);
			
	/** The osgi bundle context */
	private BundleContext bundleContext;
	
	public ResourceServiceImpl() {
		bundleContext = null;
	}
	
	/**
	 * The startup of the service
	 * @throws IOException 
	 */
	public void startUp() throws IOException {
		logger.info("ResourceService started");
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.due.service.model.ResourceService#getResource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.util.Map)
	 */
	public String getResource(String mandatorId, String groupId,
			String artifactId, String path, int type,
			Map<String, String> props) throws IOException {
		
		checkPermission("read", "resource");
		logger.debug("Resource for {} requested '{}'.", mandatorId, path);
		
		Mandator mandator = MandatorServiceImpl.getMandator(bundleContext, mandatorId);
		if(mandator==null)
			throw new IOException("Not found: The requested mandator is not available at this system.");
		
		FilePath filePath = FilePath.append( mandator.getPath(), "data/page/file/bundle", groupId, artifactId, path );
		File file = filePath.toFile();
		
		if( !file.exists() ){
			logger.trace("The resource '{}' is not available.", file.getAbsolutePath());
			throw new IOException("Not found: The requested resource is not available at this system.");
		}
		if( !file.isFile() ){
			logger.trace("The resource '{}' is no file.", file.getAbsolutePath());
			throw new IOException("Illegal: The requested resource is not a file.");
		}
		
		FileReader in = new FileReader(file);
		StringWriter out = new StringWriter();
		int c = 0;
		char[] cbuf = new char[512];
		
		try{
			while((c=in.read(cbuf))>-1)
				out.write(cbuf,0,c);
			
		}finally{
			in.close();
			out.close();
		}
		
		return out.toString();
	}

	/* (non-Javadoc)
	 * @see org.i3xx.step.due.service.model.ResourceService#getResourceAsStream(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.util.Map)
	 */
	public InputStream getResourceAsStream(String mandatorId, String groupId,
			String artifactId, String path,	int type,
			Map<String, String> props) throws IOException {
		
		checkPermission("read", "resource");
		logger.debug("Resource for {} requested '{}'.", mandatorId, path);
		
		Mandator mandator = MandatorServiceImpl.getMandator(bundleContext, mandatorId);
		if(mandator==null)
			throw new IOException("Not found: The requested mandator is not available at this system.");
		
		FilePath filePath = FilePath.append( mandator.getPath(), "data/page/file/bundle", groupId, artifactId, path );
		File file = filePath.toFile();
		
		if( !file.exists() ){
			logger.trace("The resource '{}' is not available.", file.getAbsolutePath());
			throw new IOException("Not found: The requested resource is not available at this system.");
		}
		if( !file.isFile() ){
			logger.trace("The resource '{}' is no file.", file.getAbsolutePath());
			throw new IOException("Illegal: The requested resource is not a file.");
		}
		
		return new FileInputStream(file);
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
	 * Checks the permission of the Service ( read )
	 * 
	 * @param action The action to be done
	 * @param target The target the permission is requested for
	 */
	private static void checkPermission(String action, String target) {
		
		//TODO Implement the permission to access the mandator configuration
	}

}
