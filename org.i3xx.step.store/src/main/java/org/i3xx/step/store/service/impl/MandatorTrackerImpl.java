package org.i3xx.step.store.service.impl;

/*
 * #%L
 * NordApp OfficeBase :: store
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.i3xx.step.store.service.model.StoreService;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.step.zero.service.model.mandator.MandatorService;
import org.i3xx.util.basic.io.FilePath;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MandatorTrackerImpl extends ServiceTracker<Mandator, Mandator> {
	
	static Logger logger = LoggerFactory.getLogger(MandatorTrackerImpl.class);
	
	private Map<String, ServiceRegistration<?>> registry;
	
	public MandatorTrackerImpl(BundleContext bundleContext) {
		super(bundleContext, Mandator.class.getName(), null);
		
		registry = new HashMap<String, ServiceRegistration<?>>();
		
		//
		// Gets the configured mandator and initialize the '0' session.
		//
		ServiceReference<MandatorService> msr = context.getServiceReference(MandatorService.class);
		MandatorService ms = context.getService(msr);
		if(ms==null)
			logger.warn("The mandator service is not available (maybe down or a version conflict).");
		
		for(String mandatorId : ms.getMandatorList()){
			
			Mandator mandator = ms.getMandator(mandatorId);
			if(mandator==null)
				logger.warn("The mandator '"+mandatorId+"' is not available on this system.");
			else
				logger.debug("The mandator {} is available at the path '{}'.", mandatorId, mandator.getPath());
			
			//initialize the store by mandator - not necessary here
		}//for
		
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
	 */
	public Mandator addingService(ServiceReference<Mandator> reference) {
		
		Mandator mandator = context.getService(reference);
		if(mandator==null)
			logger.warn("The mandator service is not available (maybe down or a version conflict).");
		
		if(! mandator.isInitialized()){
			logger.warn("The mandator is not initialized (initialize mandator before use).");
			return null;
		}
		
		if(registry.containsKey(mandator.getId())) {
			logger.debug("removing db service and session, initiated by mandator id:{}", mandator.getId());
			registry.remove(mandator.getId()).unregister();
		}
		
		FilePath tmpLoc = FilePath.get(mandator.getPath()).add("temp/store");
		File repository = tmpLoc.toFile();
		if( ! repository.exists()){
			repository.mkdirs();
		}
		
		logger.debug("adding service, create one db session, initiated by mandator id:{}", mandator.getId());
		
		//initialize the db session
		StoreService sts = new StoreServiceImpl(tmpLoc.getPath());
		
		Hashtable<String, Object> properties = new Hashtable<String, Object>();
		properties.put(Mandator.MANDATORID, mandator.getId());
		properties.put(Mandator.USERACCESSID, Boolean.TRUE);
		
		registry.put(mandator.getId(), 
				context.registerService(StoreService.class, sts, properties) );
		
		return mandator;
	}

	public void removedService(ServiceReference<Mandator> reference, Mandator service) {
		
		String filter = "(&("+Constants.OBJECTCLASS+"="+
				StoreService.class.getName()+")("+Mandator.MANDATORID+"="+
				service.getId()+")("+Mandator.USERACCESSID+"=true))";
		
		//runs the garbage collector before removing the service.
		try {
			Collection<ServiceReference<StoreService>> col = context.getServiceReferences(StoreService.class, filter);
			for(ServiceReference<StoreService> ref : col) {
				StoreService sts = context.getService(ref);
				try {
					logger.debug("running the garbage collector, initiated by mandator id:{}", service.getId());
					sts.gc();
				} catch (IOException ee) {
					logger.warn("The store service of the mandator "+service.getId()+" cannot be GARBAGE COLLECTED.", ee);
				}
			}
		} catch (InvalidSyntaxException e) {
			logger.warn("The store service of the mandator "+service.getId()+" cannot be cleared.", e);
		}
		
		if(registry.containsKey(service.getId())) {
			logger.debug("removing db service and session, initiated by mandator id:{}", service.getId());
			registry.remove(service.getId()).unregister();
		}
	}
}
