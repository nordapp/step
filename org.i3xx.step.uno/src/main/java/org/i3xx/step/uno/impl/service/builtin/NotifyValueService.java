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


import java.util.LinkedHashMap;
import java.util.Map;

import org.i3xx.step.uno.impl.ContextImpl;
import org.i3xx.step.uno.model.service.builtin.BuiltinService;
import org.i3xx.step.uno.model.service.builtin.keyindex.KeyItem;

/**
 * This service provides a change notification that will be thrown if the watched
 * value is set. A change of the value is not significant an may be detected by the
 * compare of the old and new value.
 * 
 * Upon this service an event service may be built.
 * 
 * @author Stefan
 *
 */
public class NotifyValueService implements BuiltinService {

	private ContextImpl.ContextService context;
	private KeyIndexService index;
	private Map<KeyItem, Event> registry;
	
	public NotifyValueService(ContextImpl.ContextService context) {
		this.context = context;
		
		index = new KeyIndexService(null);
		registry = new LinkedHashMap<KeyItem, Event>();
	}
	
	/**
	 * Initializes this service
	 */
	public void initialize() {
		context.setNotifyValue(this);
	}
	
	/**
	 * Deactivates this service
	 */
	public void deactivate() {
		context.setNotifyValue(null);
	}
	
	/**
	 * Clears and reinitializes the service to a clean state.
	 * 
	 * Reinitialize is not thought to free the former used resources, but
	 * to start work in a clean state.
	 */
	public void reinitialize(ContextImpl.ContextService context) {
		this.context = context;
		
		index = new KeyIndexService(null);
		registry = new LinkedHashMap<KeyItem, Event>();
	}
	
	/**
	 * Registers an event to the registry
	 * 
	 * Note:
	 * To register more than one event to a value use the name of the
	 * value and append a '.' and a name for the event. This name must
	 * be unique for the value.
	 * 
	 * @param key The key of the event (and the value)
	 * @param event The event
	 */
	public void register(String key, Event event) {
		KeyItem k = index.getKey(key);
		if(k==null){
			index.addKey(key);
			k = index.getKey(key);
		}
		
		registry.put(k, event);
	}
	
	/**
	 * @param key The key of the event
	 */
	public void unregister(String key) {
		KeyItem k = index.getKey(key);
		if(k!=null)
			registry.remove(k);
	}
	
	/**
	 * Notifies the Listener
	 * 
	 * @param key The key of the value to watch
	 * @param oldValue The value before the change
	 * @param newValue The new value to be set
	 */
	public void notify(String key, Object oldValue, Object newValue) {
		KeyItem k = index.getKey(key);
		if(k==null)
			return;
		
		if(k.getList()!=null){
			//recursion
			for(KeyItem i:k.getList()){
				notify((key.length()==0 ? i.getKey() : key+".")+i.getKey(), oldValue, newValue);
			}//for
		}//fi
		
		Event e = registry.get(k);
		if(e==null)
			return;
		
		if(e instanceof Change){
			((Change) e).notify(key, oldValue, newValue);
		}
	}
	
	/** The event interface */
	public static interface Event {}
	
	/** The change interface */
	public static interface Change extends Event {
		
		/**
		 * The notify method
		 *  
		 * @param key The key of the value that has changed
		 * @param oldValue The old value
		 * @param newValue The new value
		 */
		void notify(String key, Object oldValue, Object newValue);
	}
}
