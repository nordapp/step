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

import org.i3xx.step.due.service.model.DeployService;
import org.i3xx.step.zero.service.impl.mandator.MandatorServiceImpl;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeployServiceImpl implements DeployService {
	
	static Logger logger = LoggerFactory.getLogger(DeployServiceImpl.class);
	
	/** The osgi bundle context */
	private BundleContext bundleContext;

	public DeployServiceImpl() {
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
	 * @see org.i3xx.step.due.service.model.DeployService#createEmptyZip(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public File createEmptyZip(String processId, String mandatorId,
			String groupId, String artifactId) throws IOException {
		
		checkPermission("write", "resource");
		checkPermission("write", "property");
		checkPermission("write", "script");
		logger.debug("Archiv for {} requested process:{}, group:{}, artifact:{}.", processId, mandatorId, groupId, artifactId);
		
		DeployTool deploy = new DeployTool();
		
		deploy.checkArguments(processId, mandatorId, groupId, artifactId);
		
		Mandator mandator = MandatorServiceImpl.getMandator(bundleContext, mandatorId);
		if(mandator==null)
			throw new IOException("Not found: The requested mandator is not available at this system.");
		
		return deploy.createEmptyZipA(mandator.getPath(), processId, groupId, artifactId);
	}

	/* (non-Javadoc)
	 * @see org.i3xx.step.due.service.model.DeployService#zipFromData(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public File zipFromData(String processId, String mandatorId,
			String groupId, String artifactId) throws IOException {
		
		checkPermission("write", "resource");
		checkPermission("write", "property");
		checkPermission("write", "script");
		logger.debug("Archiv for {} requested process:{}, group:{}, artifact:{}.", processId, mandatorId, groupId, artifactId);
		
		DeployTool deploy = new DeployTool();
		
		deploy.checkArguments(processId, mandatorId, groupId, artifactId);
		
		Mandator mandator = MandatorServiceImpl.getMandator(bundleContext, mandatorId);
		if(mandator==null)
			throw new IOException("Not found: The requested mandator is not available at this system.");
		
		return deploy.zipFromDataA(mandator.getPath(), processId, groupId, artifactId);
	}

	/* (non-Javadoc)
	 * @see org.i3xx.step.due.service.model.DeployService#zipToData(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void zipToData(String processId, String mandatorId, String groupId,
			String artifactId, String zipFileName) throws IOException {
		
		checkPermission("write", "resource");
		checkPermission("write", "property");
		checkPermission("write", "script");
		logger.debug("Archiv for {} requested process:{}, group:{}, artifact:{}.", processId, mandatorId, groupId, artifactId);
		
		DeployTool deploy = new DeployTool();
		
		deploy.checkArguments(processId, mandatorId, groupId, artifactId);
		
		Mandator mandator = MandatorServiceImpl.getMandator(bundleContext, mandatorId);
		if(mandator==null)
			throw new IOException("Not found: The requested mandator is not available at this system.");
		
		deploy.zipToDataA(mandator.getPath(), processId, groupId, artifactId, zipFileName);
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
