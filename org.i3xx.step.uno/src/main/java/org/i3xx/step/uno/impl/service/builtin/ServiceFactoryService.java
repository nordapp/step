package org.i3xx.step.uno.impl.service.builtin;

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


import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.i3xx.step.uno.impl.ContextImpl;
import org.i3xx.step.uno.model.ServiceFactory;
import org.i3xx.step.uno.model.service.builtin.BuiltinService;

/**
 * The service provides a factory to create new service objects. It also provides
 * a way to register and unregister plain java objects as service objects.
 * 
 * @author Stefan
 *
 */
public class ServiceFactoryService implements BuiltinService {

	private ContextImpl.ContextService context;
	
	private final Map<String, RegistryEntry> registry;
	
	public ServiceFactoryService(ContextImpl.ContextService context) {
		this.context = context;
		
		registry = new HashMap<String, RegistryEntry>();
	}
	
	/**
	 * Initializes this service
	 */
	public void initialize() {
		context.setServiceFactory(this);
	}
	
	/**
	 * Deactivates this service
	 */
	public void deactivate() {
		context.removeServiceFactory();
	}
	
	/**
	 * Clears and reinitializes the service to a clean state.
	 * 
	 * Reinitialize is not thought to free the former used resources, but
	 * to start work in a clean state.
	 */
	public void reinitialize(ContextImpl.ContextService context) {
		this.context = context;
		
		registry.clear();
	}
	
	/**
	 * Registers a class to the factory
	 * 
	 * @param key The key
	 * @param object The object or class to get a new instance
	 */
	public void register(String key, Object object) {
		registry.put(key, new RegistryEntry(object));
	}
	
	/**
	 * Registers a classname to the factory
	 * 
	 * @param key The key
	 * @param fullname The name of the class
	 * @throws ClassNotFoundException 
	 */
	public void registerClass(String key, String fullname) throws ClassNotFoundException {
		//SECURITY: Each class needs to be known by the SandboxClassShutter
		Class<?> clazz = getClass().getClassLoader().loadClass(fullname);
		registry.put(key, new RegistryEntry(clazz));
	}
	
	/**
	 * Unregisters a class from the registry
	 * 
	 * @param key The key
	 */
	public void unregister(String key) {
		registry.remove(key);
	}
	
	/**
	 * 
	 */
	public void setFactory() {
		//
		// Reinitialize doesn't create a new factory.
		//
		context.setServiceFactory(new ServiceFactory(){

			public Object getInstance(String key) throws InstantiationException, IllegalAccessException  {
				RegistryEntry re = registry.get(key);
				if(re==null)
					throw new NoSuchElementException("The service '"+key+"' is not registered.");
				
				return re.getService();
			}});
	}
	
	/**
	 * @author Stefan
	 *
	 */
	private class RegistryEntry {
		
		private Class<?> clazz;
		private Object service;
		
		/**
		 * @param object
		 * @param clazz
		 */
		public RegistryEntry(Object object) {
			if(object instanceof Class<?>) {
				//SECURITY: Each class needs to be known by the SandboxClassShutter
				this.clazz = (Class<?>) object;
				this.service = null;
			}else{
				this.clazz = null;
				this.service = object;
			}
		}
		
		/**
		 * Returns a new instance of the class in the registry
		 * or the registered service or null if nothing is registered.
		 * 
		 * @return
		 * @throws InstantiationException
		 * @throws IllegalAccessException
		 */
		public Object getService() throws InstantiationException, IllegalAccessException {
			return this.clazz != null ? this.clazz.newInstance() : this.service != null ? this.service : null;
		}
		
	}/* CLASS */

}
