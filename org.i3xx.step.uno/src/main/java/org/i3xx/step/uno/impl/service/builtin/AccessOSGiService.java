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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.i3xx.step.uno.impl.ContextImpl;
import org.i3xx.step.uno.impl.ContextImpl.ContextService;
import org.i3xx.step.uno.impl.util.GsonHashMapDeserializer;
import org.i3xx.step.uno.model.service.builtin.BuiltinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AccessOSGiService implements BuiltinService {
	
	private static Logger logger = LoggerFactory.getLogger(AccessOSGiService.class);
	
	private ContextImpl.ContextService context;

	public AccessOSGiService(ContextImpl.ContextService context) {
		this.context = context;
	}

	public void initialize() {
		//not implemented yet
	}

	public void deactivate() {
		//not implemented yet
	}
	
	public void reinitialize(ContextService context) {
		this.context = context;
	}
	
	public Object getService(String name, String properties) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		fromJSON(properties, map);
		
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
