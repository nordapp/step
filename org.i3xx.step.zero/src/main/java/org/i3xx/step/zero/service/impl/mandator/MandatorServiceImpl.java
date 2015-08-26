package org.i3xx.step.zero.service.impl.mandator;

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


import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.i3xx.step.zero.service.model.cx.ContextProvider;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.step.zero.service.model.mandator.MandatorService;
import org.i3xx.step.zero.service.model.mandator.PropertyService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MandatorServiceImpl implements ManagedServiceFactory, MandatorService {
	
	//the configuration base of the service
	public static final String mandatorConfigName = "i3xx.step.zero.MandatorService";
	
	private Logger logger = LoggerFactory.getLogger(MandatorServiceImpl.class);
	
	/** The osgi bundle context */
	private BundleContext bundleContext;
	
	/**
	 * There is one service for each mandator defined
	 */
	private Map<String, List<ServiceRegistration<?>>> regList;
	
	public MandatorServiceImpl() {
		bundleContext = null;
		
		regList = new HashMap<String, List<ServiceRegistration<?>>>();
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
	
	// ----------------------------------------------------------------------
	// ManagedServiceFactory
	// ----------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.osgi.service.cm.ManagedServiceFactory#deleted(java.lang.String)
	 */
	public void deleted(String pid) {
		Mandator mand = getMandatorByPid(pid);
		
		if(mand!=null) {
			
			mand.setInitialized(false);
			
			//Unregister the service
			ServiceReference<?> ref = mand.getServiceRef();
			if(ref!=null)
				bundleContext.ungetService(ref);
			
			List<ServiceRegistration<?>> list = regList.get(mand.getId());
			for(ServiceRegistration<?> r : list){
				r.unregister();
			}
			list.clear();
		}//fi
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.cm.ManagedServiceFactory#getName()
	 */
	public String getName() {
		return MandatorServiceImpl.mandatorConfigName;
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.service.cm.ManagedServiceFactory#updated(java.lang.String, java.util.Dictionary)
	 */
	public void updated(String pid, Dictionary<String, ?> config) throws ConfigurationException {
		
		Mandator mand = getMandatorByPid(pid);
		
		//
		// Creates a new mandator and register it as managed service
		//
		if(mand == null) {
			mand = new MandatorImpl();
			//bundle context is protected
			((MandatorImpl) mand).setBundleContext(bundleContext);
			
			//Updated from the interface ManagedService
			((ManagedService)mand).updated(config);
			
			//
			// Note: There are a few ways to update and start a mandator.
			//       (by configuration file, by console command, by service)
			//        
			//        If you configure a mandator by using the configuration
			//        file, the mandator is created, but not started until
			//        you call the createMandator function of the root service.
			//
			//        It is possible to set the start flag using the 
			//        configuration file, but than it is your job to ensure the
			//        mandator is configured and initialized well.
			//
			if( ! mand.isStarted() ) {
				logger.info("The mandator '{}' has been skipped.", mand.getId());
				return;
			}
			
			if( ! regList.containsKey(mand.getId())) {
				regList.put(mand.getId(), new ArrayList<ServiceRegistration<?>>());
			}
			
			//Register as managed service
			ServiceRegistration<?> reg = null;
			String[] clazzes = new String[]{
					ManagedService.class.getName(),
					Mandator.class.getName()};
			
			Dictionary<String, Object> properties = new Hashtable<String, Object>();
			properties.put(Constants.SERVICE_PID, pid);
			properties.put(Mandator.MANDATORID, mand.getId());
			reg = bundleContext.registerService(clazzes, mand, properties);
			regList.get(mand.getId()).add(reg);
			
			ServiceReference<?> ref = reg.getReference();
			if(ref!=null)
				mand.setServiceRef(ref);
			
			//
			// Get the properties for the mandator
			// (Whiteboard-Pattern)
			//
			Filter filter = null;
			try {
				filter = bundleContext.createFilter("(&("+Constants.OBJECTCLASS+"="+
						PropertyService.class.getName()+")("+Mandator.MANDATORID+"="+
						mand.getId()+"))");
			} catch (InvalidSyntaxException e) {
				logger.error("The syntax of the filter is not valid.", e);
			}
			
			ServiceTracker<PropertyService, PropertyService> listenerTracker = 
					new ServiceTracker<PropertyService, PropertyService>(bundleContext,	filter, null);
			
			listenerTracker.open();
			try{
				Object[] services = listenerTracker.getServices();
				if(services!=null) {
					for(Object service : services) {
						PropertyService propertyService = (PropertyService)service;
						String[] names = propertyService.getPropertyNames();
						for(String name : names){
							if(propertyService.hasProperty(name))
								mand.setProperty(name, propertyService.getProperty(name));
						}//for
					}//for
				}//fi
			}finally{
				listenerTracker.close();
			}
			
			logger.info("The mandator '{}' has been started.", mand.getId());
			
			//
			//
			//
		}else{
			
			//Updated from the interface ManagedService
			((ManagedService)mand).updated(config);
		}//fi
	}
	
	// ----------------------------------------------------------------------
	// Stop mandator
	// ----------------------------------------------------------------------
	
	public void stop(String id) {
		Mandator mand = getMandator(id);
		
		if(mand!=null) {
			logger.info("Remove the mandator '{}'.", id);
			
			//Unregister the service
			ServiceReference<?> ref = mand.getServiceRef();
			if(ref!=null)
				bundleContext.ungetService(ref);
			
			List<ServiceRegistration<?>> list = regList.get(mand.getId());
			for(ServiceRegistration<?> r : list){
				r.unregister();
			}
			list.clear();
		}//fi
		
		ServiceReference<ContextProvider> ref = bundleContext.getServiceReference(ContextProvider.class);
		
		if(ref!=null) {
			logger.info("Remove all contexts of mandator '{}'.", id);
			
			ContextProvider provider = (ContextProvider)bundleContext.getService(ref);
			
			HashMap<String, Object> properties = new HashMap<String, Object>();
			properties.put(Mandator.MANDATORID, id);
			provider.kill(properties);
		}//fi
		
	}
	
	
	// ----------------------------------------------------------------------
	// Get and save mandator
	// ----------------------------------------------------------------------
	
	/**
	 * Saves the data of the mandator with the id
	 * 
	 * @param id The id of the mandator
	 * @param config The configuration
	 * @return true if a value has been saved, false otherwise.
	 */
	/* (non-Javadoc)
	 * @see org.i3xx.step.zero.service.model.mandator.MandatorService#save(java.lang.String, java.util.Dictionary)
	 */
	public boolean save(String id, Dictionary<String, Object> config) {
		
		boolean f = false;
		
		Mandator m = getMandator(id);
		if(m != null && m.isDirty()){
			m.save(config);
			f = true;
		}//fi
		
		return f;
	}
	
	/**
	 * Get a list of all available mandator.
	 * 
	 * @return The mandator list as a String array
	 */
	public String[] getMandatorList() {
		Set<String> list = new HashSet<String>();
		
		ServiceReference<?>[] refs;
		try {
			refs = bundleContext.getServiceReferences(Mandator.class.getName(), null);
			if(refs==null)
				refs = new ServiceReference<?>[0];
		} catch (InvalidSyntaxException e) {
			throw new RuntimeException(e); /* The filter is null - this never occurs */
		}
		for(ServiceReference<?> ref : refs){
			String mandatorId = (String)ref.getProperty(Mandator.MANDATORID);
			if(mandatorId!=null) {
				list.add(mandatorId);
			}//fi
		}//for
		
		return list.toArray( new String[list.size()] );
	}
	
	/**
	 * Gets the mandator with the id
	 * 
	 * @param id The id of the mandator
	 * @return The mandator object
	 */
	public Mandator getMandator(String id) {
		Mandator mandator = null;
		
		ServiceReference<?>[] ref = null;
		try {
			ref = bundleContext.getServiceReferences(Mandator.class.getName(),
					"("+Mandator.MANDATORID+"="+id+")");
		} catch (InvalidSyntaxException e) {
			logger.warn("The filter syntax is not valid.", e);
		}
		
		if(ref!=null && ref.length>0) {
			logger.debug("Mandator: '{}' references: {}", id, ref.length);
			mandator = (Mandator)bundleContext.getService(ref[0]);
		}
		
		return mandator;
	}
	
	/**
	 * Gets the mandator with the pid
	 * 
	 * @param pid The pid of the mandator
	 * @return The mandator object
	 */
	protected Mandator getMandatorByPid(String pid) {
		Mandator mandator = null;
		
		ServiceReference<?>[] ref = null;
		try {
			ref = bundleContext.getServiceReferences(Mandator.class.getName(),
					"("+Constants.SERVICE_PID+"="+pid+")");
		} catch (InvalidSyntaxException e) {
			logger.warn("The filter syntax is not valid.", e);
		}
		
		if(ref!=null && ref.length>0)
			mandator = (Mandator)bundleContext.getService(ref[0]);
		
		return mandator;
	}
	
	/**
	 * Gets the mandator with the id
	 * 
	 * @param context The bundle context
	 * @param id The id of the mandator
	 * @return The mandator object
	 */
	public static Mandator getMandator(BundleContext context, String id) {
		Mandator mandator = null;
		
		ServiceReference<?>[] ref = null;
		try {
			ref = context.getServiceReferences(Mandator.class.getName(),
					"("+Mandator.MANDATORID+"="+id+")");
		} catch (InvalidSyntaxException e) {
			throw new RuntimeException(e);
		}
		
		if(ref!=null && ref.length>0)
			mandator = (Mandator)context.getService(ref[0]);
		
		return mandator;
	}
}
