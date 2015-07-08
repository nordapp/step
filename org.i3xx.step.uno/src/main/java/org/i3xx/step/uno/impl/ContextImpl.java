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


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.i3xx.step.uno.impl.service.builtin.AccessOSGiService;
import org.i3xx.step.uno.impl.service.builtin.ContextAdministrationService;
import org.i3xx.step.uno.impl.service.builtin.ContextPropertiesService;
import org.i3xx.step.uno.impl.service.builtin.KeyIndexService;
import org.i3xx.step.uno.impl.service.builtin.NotifyValueService;
import org.i3xx.step.uno.impl.service.builtin.ServiceFactoryService;
import org.i3xx.step.uno.impl.service.builtin.VerifyValueService;
import org.i3xx.step.uno.model.OSGiServiceBridge;
import org.i3xx.step.uno.model.ServiceFactory;
import org.i3xx.step.uno.model.StepContext;
import org.i3xx.step.uno.model.service.builtin.BuiltinService;
import org.mozilla.javascript.Scriptable;

public class ContextImpl implements StepContext {
	
	private Map<String, Object> values;
	private ServiceFactory factory;
	private Scriptable sharedScope;
	private OSGiServiceBridge serviceBridge;
	private Map<String, Object> localValues;
	
	private boolean _lock;
	
	private ContextService _context;
	private KeyIndexService keyIndex;
	private NotifyValueService notifyValue;
	private VerifyValueService verifyValue;
	private ServiceFactoryService serviceFactory;
	
	public ContextImpl() {
		values = new LinkedHashMap<String, Object>();
		factory = null;
		sharedScope = null;
		serviceBridge = null;
		localValues = new HashMap<String, Object>();
		
		_lock = false;
		/* private inner class of this */
		_context = new ContextServiceImpl();
		
		
		keyIndex = null;
		notifyValue = null;
		verifyValue = null;
		serviceFactory = null;
	}
	
	/**
	 * Initializes all builtin services
	 * 
	 * @throws Exception 
	 */
	public void initializeBuiltinServices() throws Exception {
		//
		// Initialize doesn't create new service objects if they are just there.
		//
		((BuiltinService)getService("org.i3xx.step.uno.KeyIndexService")).initialize();
		((BuiltinService)getService("org.i3xx.step.uno.NotifyValueService")).initialize();
		((BuiltinService)getService("org.i3xx.step.uno.VerifyValueService")).initialize();
		((BuiltinService)getService("org.i3xx.step.uno.ServiceFactoryService")).initialize();
		((BuiltinService)getService("org.i3xx.step.uno.AccessOSGiService")).initialize();
		
		//sets the factory to this.factory
		serviceFactory.setFactory();
	}
	
	/**
	 * Reinitializes all builtin services
	 * 
	 * @see org.i3xx.step.uno.model.service.builtin.BuiltinService for more details.
	 * @throws Exception
	 */
	public void reinitializeBuiltinServices() throws Exception {
		//
		// Reinitialize doesn't create new service objects if they are just there.
		//
		((BuiltinService)getService("org.i3xx.step.uno.KeyIndexService")).reinitialize(_context);
		((BuiltinService)getService("org.i3xx.step.uno.NotifyValueService")).reinitialize(_context);
		((BuiltinService)getService("org.i3xx.step.uno.VerifyValueService")).reinitialize(_context);
		((BuiltinService)getService("org.i3xx.step.uno.ServiceFactoryService")).reinitialize(_context);
		((BuiltinService)getService("org.i3xx.step.uno.AccessOSGiService")).reinitialize(_context);
		
		//
		// Note: This won't work if the context is locked and the factory is available.
		//       (You need a context exit or a new context to get really new services)
		//
		//sets the factory to this.factory
		if( (! _lock) && factory!=null) {
			serviceFactory.setFactory();
		}
	}
	
	/**
	 * @return The lock state
	 */
	public boolean isLocked() {
		return _lock;
	}
	
	/**
	 * Lock the context to avoid reinitializing the services.
	 */
	public void lock() {
		_lock = true;
	}
	
	/**
	 * Destroys the context
	 */
	public void exit() {
		
		values = new LinkedHashMap<String, Object>();
		factory = null;
		sharedScope = null;
		serviceBridge = null;
		localValues = new HashMap<String, Object>();
		
		_lock = false;
		/* private inner class of this */
		_context = new ContextServiceImpl();
		
		
		keyIndex = null;
		notifyValue = null;
		verifyValue = null;
		serviceFactory = null;
	}

	/* (non-Javadoc)
	 * @see org.i3xx.step.uno.model.StepContext#getService(java.lang.String)
	 */
	public Object getService(String fullname) throws Exception {
		//builtin services
		if(fullname.startsWith("org.i3xx.step.uno.")) {
			if(fullname.equals("org.i3xx.step.uno.ContextService")){
				//
				// - Caution: security hazard, wrap the context object properly - do not extend -
				// The access to the StepContext object used by the script itself.
				//
				return new org.i3xx.step.uno.impl.service.builtin.ContextServiceAdapter(this);
			}
			else if(fullname.equals("org.i3xx.step.uno.ContextAdministrationService")){
				return new ContextAdministrationService(_context);
			}
			else if(fullname.equals("org.i3xx.step.uno.ContextPropertiesService")){
				return new ContextPropertiesService(_context);
			}
			else if(fullname.equals("org.i3xx.step.uno.KeyIndexService")){
				//return the initialized service if available
				return (keyIndex==null) ? (new KeyIndexService(_context)) : keyIndex;
			}
			else if(fullname.equals("org.i3xx.step.uno.NotifyValueService")){
				//return the initialized service if available
				return (notifyValue==null) ? (new NotifyValueService(_context)) : notifyValue;
			}
			else if(fullname.equals("org.i3xx.step.uno.VerifyValueService")){
				//return the initialized service if available
				return (verifyValue==null) ? (new VerifyValueService(_context)) : verifyValue;
			}
			else if(fullname.equals("org.i3xx.step.uno.ServiceFactoryService")){
				//return the initialized service if available
				return (serviceFactory==null) ? (new ServiceFactoryService(_context)) : serviceFactory;
			}
			else if(fullname.equals("org.i3xx.step.uno.AccessOSGiService")){
				return new AccessOSGiService(_context);
			}
		}//fi
		
		//Create other services by the factory
		return factory==null ? null : factory.getInstance(fullname);
	}
	
	/**
	 * Gets the shared scope of the script engine
	 * 
	 * @return
	 */
	public Scriptable getScope() {
		return sharedScope;
	}
	
	/**
	 * Sets the shared scope of the script engine
	 * 
	 * @param sharedScope
	 */
	public void setScope(Scriptable sharedScope) {
		//The scope is set after lock
		//
		//if(this.sharedScope!=null)
		//	throw new IllegalStateException("Forbidden reset of the shared scope");
		
		this.sharedScope = sharedScope;
	}

	/**
	 * @return the serviceBridge
	 */
	public OSGiServiceBridge getServiceBridge() {
		return serviceBridge;
	}

	/**
	 * @param serviceBridge the serviceBridge to set
	 */
	public void setServiceBridge(OSGiServiceBridge serviceBridge) {
		if(_lock)
			throw new LockException("The context is locked.");
		
		this.serviceBridge = serviceBridge;
	}
	
	/**
	 * @return The localValues
	 */
	public Map<String, Object> getLocalValues(String key) {
		return localValues;
	}

	/**
	 * Sets The localValues
	 */
	public void setLocalValues(Map<String, Object> localValues) {
		this.localValues = localValues;
	}
	
	public String[] getNames() {
		Set<String> nms = new HashSet<String>();
		nms.addAll(values.keySet());
		nms.addAll(localValues.keySet());
		
		return nms.toArray(new String[values.size()]);
	}
	
	public Object getValue(String key) {
		if(localValues.containsKey(key))
			return localValues.get(key);
		
		return values.get(key);
	}

	public void setValue(String key, Object value) {
		
		if(localValues.containsKey(key)) {
			localValues.put(key, value);
		}else{
			
			Object oldValue = values.get(key);
			
			//Add the key to the index
			if(keyIndex!=null)
				keyIndex.addKey(key);
			
			//Only Numbers, Strings and serializable Objects are allowed.
			if(verifyValue==null) {
				values.put(key, value);
				
				if(notifyValue!=null)
					notifyValue.notify(key, oldValue, value);
			}else if(verifyValue.verify(value)){
				values.put(key, value);
				
				if(notifyValue!=null)
					notifyValue.notify(key, oldValue, value);
			}//fi
		}//fi
	}
	
	//
	// protected access to the inner vars
	//
	
	/**  */
	public static interface ContextService {
		
		/**
		 * @return the script scope
		 */
		Scriptable getScope();
		
		/**
		 * @return The OSGi service bridge
		 */
		OSGiServiceBridge getServiceBridge();
		
		/**
		 * @return the values
		 */
		Map<String, Object> getValues();

		/**
		 * @return the KeyIndexService
		 */
		KeyIndexService getKeyIndex();

		/**
		 * @param keyIndex sets the KeyIndexService to set
		 */
		void setKeyIndex(KeyIndexService keyIndex);

		/**
		 * @return the NotifyValueService
		 */
		NotifyValueService getNotifyValue();

		/**
		 * @param notifyValue sets the NotifyValueService to set
		 */
		void setNotifyValue(NotifyValueService notifyValue);

		/**
		 * @return the VerifyValueService
		 */
		VerifyValueService getVerifyValue();

		/**
		 * @param verifyValue sets the VerifyValueService to set
		 */
		void setVerifyValue(VerifyValueService verifyValue);
		
		/**
		 * @return the ServiceFactoryService
		 */
		ServiceFactoryService getServiceFactory();
		
		/**
		 * @param serviceFactory sets the ServiceFactoryService to set
		 */
		void setServiceFactory(ServiceFactoryService serviceFactory);
		
		/**
		 * @param factory sets the service factory
		 */
		void setServiceFactory(ServiceFactory factory);
		
		/**
		 * Removes the service factory and the ServiceFactoryService
		 */
		void removeServiceFactory();
		
	}/*INTERFACE*/
	
	/**
	 * The private class wraps any access to the StepContext by the service
	 * interface to avoid a cast to ContextImpl (this).
	 * 
	 * @author Administrator
	 *
	 */
	private class ContextServiceImpl implements ContextService {
		
		public ContextServiceImpl() {
		}
		
		/**
		 * @return the shared script scope
		 */
		public Scriptable getScope() {
			return ContextImpl.this.getScope();
		}
		
		/**
		 * @return the OSGi service bridge
		 */
		public OSGiServiceBridge getServiceBridge() {
			return ContextImpl.this.getServiceBridge();
		}
		
		/**
		 * @return the values
		 */
		public Map<String, Object> getValues() {
			return values;
		}

		/**
		 * @return the keyIndex
		 */
		public KeyIndexService getKeyIndex() {
			return keyIndex;
		}

		/**
		 * @param keyIndex the keyIndex to set
		 */
		public void setKeyIndex(KeyIndexService keyIndex) {
			if(_lock)
				throw new LockException("The context is locked.");
			
			ContextImpl.this.keyIndex = keyIndex;
		}

		/**
		 * @return the notifyValue
		 */
		public NotifyValueService getNotifyValue() {
			return notifyValue;
		}

		/**
		 * @param notifyValue the notifyValue to set
		 */
		public void setNotifyValue(NotifyValueService notifyValue) {
			if(_lock)
				throw new LockException("The context is locked.");
			
			ContextImpl.this.notifyValue = notifyValue;
		}

		/**
		 * @return the verifyValue
		 */
		public VerifyValueService getVerifyValue() {
			return verifyValue;
		}

		/**
		 * @param verifyValue the verifyValue to set
		 */
		public void setVerifyValue(VerifyValueService verifyValue) {
			if(_lock)
				throw new LockException("The context is locked.");
			
			ContextImpl.this.verifyValue = verifyValue;
		}

		/**
		 * @return the serviceFactory
		 */
		public ServiceFactoryService getServiceFactory() {
			return serviceFactory;
		}

		/**
		 * @param serviceFactory the serviceFactory to set
		 */
		public void setServiceFactory(ServiceFactoryService serviceFactory) {
			if(_lock)
				throw new LockException("The context is locked.");
			
			ContextImpl.this.serviceFactory = serviceFactory;
		}
		
		/**
		 * @param serviceFactory sets the service factory
		 */
		public void setServiceFactory(ServiceFactory factory) {
			if(_lock)
				throw new LockException("The context is locked.");
			
			ContextImpl.this.factory = factory;
		}
		
		/**
		 * Removes the service factory and the ServiceFactoryService
		 */
		public void removeServiceFactory() {
			if(_lock)
				throw new LockException("The context is locked.");
			
			ContextImpl.this.serviceFactory = null;
			ContextImpl.this.factory = null;
		}
		
	}/*CLASS*/
}
