package org.i3xx.step.uno.impl.api;

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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.i3xx.step.uno.impl.ContextImpl;
import org.i3xx.step.uno.impl.util.GsonHashMapDeserializer;
import org.mozilla.javascript.ScriptableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Provides functionality to print out to the java context.
 * 
 * @author Stefan
 *
 */
public class Require extends ScriptableObject{
	
	private static Logger logger = LoggerFactory.getLogger(Require.class);
	
    private static final long serialVersionUID = 1L;
    private static String scriptClassName = "Undefined";
    
    private ContextImpl context;
    
	public Require(ContextImpl context) {
		this.context = context;
	}

	@Override
	public String getClassName() {
		return scriptClassName;
	}
	
	/**
	 * <p>Gets the OSGi service from the registration.</p>
	 * <p>
	 * Note: Only those services can be returned that are built for mandator
	 *       and script use. The access to the service registry is filtered
	 *       by the properties Mandator.MANDATORID and Mandator.USERACCESSID.</p>
	 * <pre><code>
	 *       String mandatorId = "<the-id-of-the-mandator>";
	 *       String filter = "(&("+Mandator.MANDATORID+"="+mandatorId+")"+
	 *           "("+Mandator.USERACCESSID+"=true)";
	 *       
	 *       To setup a service to be used for script put the properties
	 *       to the property dictionary when registering the service.
	 *       
	 *       BundleContext bundleContext = ...;
	 *       Object service = ...;
	 *       String[] clazzes = new String[]{...};
	 *       Dictionary<String, Object> props = new Hashtable<String, Object>();
	 *       props.put(Mandator.MANDATORID, mandatorId);
	 *       props.put(Mandator.USERACCESSID, Boolean.TRUE);
	 *       ... //other useful properties to describe the service
	 *       
	 *       context.registerService(clazzes, service, props);
	 *       ...
	 * </code></pre>
	 * 
	 * @param name The name of the service
	 * @param properties The properties to describe the service
	 * with the exception of Mandator.MANDATORID, Mandator.USERACCESSID
	 * and the name of the service, which are set automatically.
	 * @throws Exception 
	 */
	public Object require(String name, String properties) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		fromJSON(properties, map);
		
		//
		// get builtin service
		//
		if(name.startsWith("org.i3xx.step.uno.")) {
			//get the built in service
			Object srv = context.getService(name);
			if(srv != null)
				return srv;
		}
		
		return context.getServiceBridge().getService(name, map);
	}
	
	/**
	 * Reads the key value pairs from a JSON String, 
	 * and adds the pairs to the map.
	 * 
	 * @param json
	 * @throws Exception
	 */
	public void fromJSON(String json, Map<String, Object> values) throws Exception {
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(LinkedHashMap.class, new GsonHashMapDeserializer());
		Gson gson = gsonBuilder.create();
		
		LinkedHashMap<?, ?> map = gson.fromJson(json, LinkedHashMap.class);
		Iterator<?> iter = map.keySet().iterator();
		while(iter.hasNext()) {
			String key = (String)iter.next();
			Object val = map.get(key);
			
			if(val==null){
				//values.put(key, null);
			}else if(val instanceof Number) {
				values.put(key, val);
			}else if(val instanceof String) {
				values.put(key, val);
			}else {
				logger.warn("The Object key: {} can't be converted. val: {}", key, val);
				throw new IllegalArgumentException("The key: "+key+" has no valid value: "+val);
			}//fi
		}//while
	}

}
