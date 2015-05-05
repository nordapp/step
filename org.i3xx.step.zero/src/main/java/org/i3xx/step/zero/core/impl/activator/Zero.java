package org.i3xx.step.zero.core.impl.activator;

/*
 * #%L
 * NordApp OfficeBase :: zero
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


import java.util.Dictionary;
import java.util.Hashtable;

import org.i3xx.step.zero.service.impl.cx.ContextProviderImpl;
import org.i3xx.step.zero.service.impl.mandator.MandatorServiceImpl;
import org.i3xx.step.zero.service.impl.mandator.PropertyServiceTrackerImpl;
import org.i3xx.step.zero.service.model.cx.ContextProvider;
import org.i3xx.step.zero.service.model.mandator.MandatorService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ManagedServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Zero implements BundleActivator {

	static Logger logger = LoggerFactory.getLogger(Zero.class);
	
	private PropertyServiceTrackerImpl propertyServiceTracker;
	
	public Zero() {
		propertyServiceTracker = null;
	}

	//@Override
	//Old fashioned activator
	public void start(BundleContext context) throws Exception {
		
		// ------------------------------------------------------------------
		// Mandator
		// ------------------------------------------------------------------
		
		logger.info("Initialize mandator.");
		
		//
		// Register the mandator service factory
		//
		
		//
		// Create a new mandator using the command config:
		// config:edit i3xx.step.zero.MandatorService-<Mandator-ID>
		//
		
		MandatorServiceImpl service = new MandatorServiceImpl();
		service.setBundleContext(context);
		
		String[] clazzes = new String[]{ManagedServiceFactory.class.getName(),
				MandatorService.class.getName()};
		
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(Constants.SERVICE_PID, MandatorServiceImpl.mandatorConfigName);
		
		context.registerService(clazzes, service, properties);
		
		//
		// Create the security context provider
		//
		
		ContextProviderImpl provider = new ContextProviderImpl();
		provider.setBundleContext(context);
		
		context.registerService(ContextProvider.class, provider, null);
		
		//
		//
		//
		propertyServiceTracker = new PropertyServiceTrackerImpl(context);
		propertyServiceTracker.open();
	}
	
	//@Override
	//Old fashioned activator
	public void stop(BundleContext context) throws Exception {
		propertyServiceTracker.close();
	}

}
