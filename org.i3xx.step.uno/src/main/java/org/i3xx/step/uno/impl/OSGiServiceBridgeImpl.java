package org.i3xx.step.uno.impl;

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


import java.util.Map;

import org.i3xx.step.uno.model.OSGiServiceBridge;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OSGiServiceBridgeImpl implements OSGiServiceBridge {
	
	static Logger logger = LoggerFactory.getLogger(OSGiServiceBridgeImpl.class);
	
	/** The bundle context */
	private BundleContext bundleContext;
	
	/** The mandator id */
	private String mandatorId;

	public OSGiServiceBridgeImpl() {
		this.bundleContext = null;
		this.mandatorId = null;
	}

	/* (non-Javadoc)
	 * @see org.i3xx.step.uno.model.OSGiServiceBridge#getService(java.lang.String, java.util.Map)
	 */
	public Object getService(String name, Map<String, Object> properties) {
		
		String filter = "(&("+Mandator.MANDATORID+"="+mandatorId+")";
		filter += "("+Mandator.USERACCESSID+"=true)";
		for(Map.Entry<String, Object> e : properties.entrySet()) {
			filter += "("+e.getKey()+"="+String.valueOf(e.getValue())+")";
		}
		filter += ")";
		
		try {
			ServiceReference<?>[] ref = bundleContext.getServiceReferences(name, filter);
			if(ref.length==0){
				logger.info("The service '{}' of the mandator '{}' is not available by the filter {}.", name, mandatorId, filter);
				return null;
			}
			
			return bundleContext.getService(ref[0]);
			
		} catch (InvalidSyntaxException t) {
			logger.warn("The service '"+name+"' cannot be get.", t);
			return null;
		}
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
	 * @return the mandatorId
	 */
	public String getMandatorId() {
		return mandatorId;
	}

	/**
	 * @param mandatorId the mandatorId to set
	 */
	public void setMandatorId(String mandatorId) {
		this.mandatorId = mandatorId;
	}

}
