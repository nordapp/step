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


import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MandatorImpl implements Mandator, ManagedService {
	
	final Logger logger = LoggerFactory.getLogger(MandatorImpl.class);
	
	/** The osgi bundle context */
	private BundleContext bundleContext;
	
	//Administration parameter
	private boolean dirty;
	private boolean initialized;
	private boolean started;
	private String factoryPid;
	private String servicePid;
	private ServiceReference<?> serviceRef;
	
	//The title of the mandator
	private String title;
	//The id of the mandator (former company name)
	private String id;
	//The root directory of the mandator's data (former officebase root)
	private String root;
	//The abstract path of the mandator (available after 'ob:mandator-setup')
	private String path;
	
	//The property map for further settings
	private Map<String, String> properties;
	
	public MandatorImpl() {
		bundleContext = null;
		
		dirty = false;
		initialized = false;
		started = false;
		factoryPid = null;
		servicePid = null;
		serviceRef = null;
		
		title = null;
		id = null;
		root = null;
		
		path = null;
		properties = new HashMap<String, String>();
	}

	/**
	 * @return the bundleContext
	 */
	protected BundleContext getBundleContext() {
		return bundleContext;
	}

	/**
	 * @param bundleContext the bundleContext to set
	 */
	protected void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		checkPermission("update");
		this.title = title;
		this.dirty = true;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		checkPermission("update");
		this.id = id;
		this.dirty = true;
	}

	/**
	 * @return the root
	 */
	public String getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(String root) {
		checkPermission("update");
		this.root = root;
		this.dirty = true;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		checkPermission("update");
		this.path = path;
		this.dirty = true;
	}
	
	
	/**
	 * Gets a key array of all property names
	 * 
	 * @return The keys as an array
	 */
	public String[] getPropertyKeys() {
		return properties.keySet().toArray(new String[properties.size()]);
	}
	
	/**
	 * Gets a property
	 * 
	 * @param key The key of the property
	 * @return The property value
	 */
	public String getProperty(String key){
		return properties.get(key);
	}
	
	/**
	 * Sets a a property
	 * 
	 * @param key The key of the property
	 * @param value The value of the property
	 */
	public void setProperty(String key, String value){
		if(logger.isTraceEnabled()) {
			logger.trace("Set property {}='{}'.", key, value);
		}
		properties.put(key, value);
	}
	
	
	public boolean isInitialized() {
		return initialized;
	}
	
	public void setInitialized(boolean initialized) {
		checkPermission("admin");
		this.initialized = initialized;
	}
	
	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean isDirty() {
		return dirty;
	}
	
	/**
	 * @return the factoryPid
	 */
	public String getFactoryPid() {
		return factoryPid;
	}

	/**
	 * @param factoryPid the factoryPid to set
	 */
	public void setFactoryPid(String factoryPid) {
		checkPermission("admin");
		this.factoryPid = factoryPid;
	}

	/**
	 * @return the servicePid
	 */
	public String getServicePid() {
		return servicePid;
	}

	/**
	 * @param servicePid the servicePid to set
	 */
	public void setServicePid(String servicePid) {
		checkPermission("admin");
		this.servicePid = servicePid;
	}

	/**
	 * @return the serviceRef
	 */
	public ServiceReference<?> getServiceRef() {
		return serviceRef;
	}

	/**
	 * @param serviceRef the serviceRef to set
	 */
	public void setServiceRef(ServiceReference<?> serviceRef) {
		checkPermission("admin");
		this.serviceRef = serviceRef;
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.zero.service.model.mandator.Mandator#save(java.util.Dictionary)
	 */
	public void save(Dictionary<String, Object> config) {
		checkPermission("update");
		
		//save
		if(this.title!=null)
			config.put("mandator.title", this.title);
		if(this.id!=null)
			config.put("mandator.id", this.id);
		if(this.root!=null)
			config.put("mandator.root", this.root);
		if(this.path!=null)
			config.put("mandator.path", this.path);
		if(this.servicePid!=null)
			config.put("mandator.service-pid", this.servicePid);
		if(this.factoryPid!=null)
			config.put("mandator.factory-pid", this.factoryPid);
		
		config.put("mandator.init", this.initialized ? "true" : "false");
		config.put("mandator.start", this.started ? "true" : "false");
		
		dirty = false;
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
	 */
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		checkPermission("update");
		
		if(config == null) {
			return;
		}
		
		//
		String tmp = null;
		
		//The service pid
		tmp = (String)config.get(Constants.SERVICE_PID);
		if(tmp!=null)
			this.servicePid = tmp;
		
		//The factory pid
		tmp = (String)config.get("service.factorypid");
		if(tmp!=null)
			this.factoryPid = tmp;
		
		//The title
		tmp = (String)config.get("mandator.title");
		if(tmp!=null)
			this.title = tmp;
		
		//The title
		tmp = (String)config.get("mandator.id");
		if(tmp!=null)
			this.id = tmp;
		
		//The title
		tmp = (String)config.get("mandator.root");
		if(tmp!=null)
			this.root = tmp;
		
		//The title
		tmp = (String)config.get("mandator.path");
		if(tmp!=null)
			this.path = tmp;
		
		//The initalized flag
		tmp = (String)config.get("mandator.init");
		if(tmp!=null){
			this.initialized = Boolean.parseBoolean(tmp);
		}else{
			//need to set the flag to false if unavailable
			//to use the update path at the command set.
			this.initialized = false;
		}
		
		//The started flag
		tmp = (String)config.get("mandator.start");
		if(tmp!=null){
			this.started = Boolean.parseBoolean(tmp);
		}else{
			this.started = false;
		}
		
		dirty = false;
		
		logger.debug("Update id={} root={} title={} path={} init={} start={}", id, root, title, path,
				new Boolean(initialized), new Boolean(started));
	}
	
	/**
	 * Checks the permission of the Mandator ( update | admin )
	 * 
	 * @param action The action to be done
	 */
	private static void checkPermission(String action) {
		
		//TODO Implement the permission to access the mandator configuration
	}
}
