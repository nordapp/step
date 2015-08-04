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


import java.util.NoSuchElementException;

import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.step.zero.service.model.mandator.MandatorService;
import org.i3xx.step.zero.service.model.mandator.PropertyService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The PropertyServiceTrackerImpl watches for a PropertyService to be added
 * to the ServiceRegistry. The service must be added using a property
 * Mandator.MANDATORID. Then the matching Mandator is fetched from the
 * MandatorService and the properties of that service are added to the
 * Mandator.
 * 
 * If the Mandator is created after registering the PropertyService the
 * WhiteboardPattern is used with a ServiceTracker to get the properties.
 * 
 * @see org.i3xx.step.zero.service.impl.mandator.MandatorServiceImpl
 * 
 * @author Stefan
 *
 */
public class PropertyServiceTrackerImpl extends ServiceTracker<PropertyService, PropertyService>  {
	
	static Logger logger = LoggerFactory.getLogger(PropertyServiceTrackerImpl.class);
	
	public PropertyServiceTrackerImpl(BundleContext bundleContext) {
		super(bundleContext, PropertyService.class.getName(), null);
	}
	
	@Override
	public PropertyService addingService(ServiceReference<PropertyService> reference) {
		PropertyService service = context.getService(reference);
		
		String mandatorId = (String)reference.getProperty(Mandator.MANDATORID);
		if(mandatorId==null)
			throw new java.lang.IllegalArgumentException("The property "+Mandator.MANDATORID+" is missing.");
		
		ServiceReference<MandatorService> msr = context.getServiceReference(MandatorService.class);
		MandatorService ms = context.getService(msr);
		
		Mandator mandator = ms.getMandator(mandatorId);
		if(mandator==null)
			throw new NoSuchElementException("The mandator '"+mandatorId+"' is not available.");
		
		logger.debug("Add and configure mandator service {}.", mandatorId);
		for(String name : service.getPropertyNames()){
			String value = service.getProperty(name);
			mandator.setProperty(name, value);
		}//for
		
		return service;
	}
	
	@Override
	public void removedService(ServiceReference<PropertyService> reference, PropertyService service) {
		context.ungetService(reference);
	}

}
