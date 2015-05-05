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


import java.io.IOException;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>This is a blueprint driven activator. The startup is called if all
 * requested services are ready to use.</p>
 * 
 * <p>The services are implemented as dummy, they are not used here. The
 * point of interest is the availability only.</p>
 * 
 * @author Stefan
 *
 */
public class ActivatorBean {

	static Logger logger = LoggerFactory.getLogger(ActivatorBean.class);
	
	
	/** The osgi bundle context */
	private BundleContext bundleContext;
	
	private Object[] dummy;
	
	/**  */
	private InitializeMandatorTrackerImpl mandatorTracker;
	
	/**  */
	private InitializeSessionServiceTrackerImpl sessionServiceTracker;
	
	/**  */
	private SessionTrackerImpl sessionTracker;
	
	public ActivatorBean() {
		bundleContext = null;
		dummy = new Object[4];
		
		mandatorTracker = null;
		sessionServiceTracker = null;
		sessionTracker = null;
	}
	
	/**
	 * The startup of the service
	 * @throws IOException 
	 */
	public void startUp() throws IOException {
		logger.info("ActivatorBean started");
		
		//
		// Setup the mandator session
		//
		mandatorTracker = new InitializeMandatorTrackerImpl(bundleContext);
		mandatorTracker.open();
		
		//
		// The initialize session tracker notices the start of the
		// Session service and initializes the session by using
		// the mandator service.
		//
		// @see /org.i3xx.step.uno/OSGI-INF/blueprint/initialize-resource-service.xml
		//
		sessionServiceTracker = new InitializeSessionServiceTrackerImpl(bundleContext);
		sessionServiceTracker.open();
		
		//
		// Setup the session after it is created
		//
		sessionTracker = new SessionTrackerImpl(bundleContext);
		sessionTracker.open();
		
		//
		// Scan all properties at startup
		//
		PropertyScanner scan = new PropertyScanner();
		scan.setBundleContext(bundleContext);
		scan.scanAll();
	}
	
	/**
	 * The cleanup of the service
	 * @throws IOException 
	 */
	public void cleanUp() throws IOException {
		logger.info("ActivatorBean stopped");
		
		mandatorTracker.close();
		sessionTracker.close();
		sessionServiceTracker.close();
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
	
	/** A service dummy */
	public Object getServiceDummy1() { return dummy[0]; }
	
	/** A service dummy */
	public void setServiceDummy1(Object rc) { dummy[0] = rc; }
	
	/** A service dummy */
	public Object getServiceDummy2() { return dummy[1]; }
	
	/** A service dummy */
	public void setServiceDummy2(Object rc) { dummy[1] = rc; }
	
	/** A service dummy */
	public Object getServiceDummy3() { return dummy[2]; }
	
	/** A service dummy */
	public void setServiceDummy3(Object rc) { dummy[2] = rc; }
	
	/** A service dummy */
	public Object getServiceDummy4() { return dummy[3]; }
	
	/** A service dummy */
	public void setServiceDummy4(Object rc) { dummy[3] = rc; }

}
