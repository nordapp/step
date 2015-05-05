package org.i3xx.step.uno.impl.service;

/*
 * #%L
 * NordApp OfficeBase :: uno
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


import java.io.IOException;

import org.i3xx.step.uno.model.service.MandatorTrackerService;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MandatorTrackerServiceImpl implements MandatorTrackerService {
	
	static Logger logger = LoggerFactory.getLogger(MandatorTrackerServiceImpl.class);
	
	/** The osgi bundle context */
	private BundleContext bundleContext;
	
	private MandatorTrackerImpl imServiceTracker;
	
	public MandatorTrackerServiceImpl() {
		this.setBundleContext(null);
		imServiceTracker = null;
	}
	
	/**
	 * The startup of the service
	 * @throws IOException 
	 */
	public void startUp() throws IOException {
		
		//
		// Each time a mandator is added to the registry a corresponding
		// engine base is created.
		//
		// @see /org.i3xx.step.uno/OSGI-INF/blueprint/initialize-mandator-service.xml
		//
		imServiceTracker = new MandatorTrackerImpl(bundleContext);
		imServiceTracker.open();
		
		logger.debug("The mandator tracker is opened.");
	}
	
	/**
	 * The startup of the service
	 * @throws IOException 
	 */
	public void cleanUp() throws IOException {
		imServiceTracker.close();
		
		logger.debug("The mandator tracker is closed.");
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

}
