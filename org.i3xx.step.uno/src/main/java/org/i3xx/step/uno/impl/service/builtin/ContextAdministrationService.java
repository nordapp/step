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


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.i3xx.step.uno.impl.ContextImpl;
import org.i3xx.step.uno.impl.ContextImpl.ContextService;
import org.i3xx.step.uno.impl.util.GsonHashMapDeserializer;
import org.i3xx.step.uno.model.service.builtin.BuiltinService;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Use this service to administer the context
 * 
 * @author Stefan
 *
 */
public class ContextAdministrationService implements BuiltinService {
	
	private static Logger logger = LoggerFactory.getLogger(ContextAdministrationService.class);
	
	private ContextImpl.ContextService context;
	
	public ContextAdministrationService(ContextImpl.ContextService context) {
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
	
	/**
	 * Clears the values of the context.
	 */
	public void clearValues() {
		context.getValues().clear();
	}
	
	/**
	 * Writes the key value map to a JSON String
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toJSON() throws Exception {
		Gson gson = new Gson();
		StringBuffer buffer = new StringBuffer();
		
		Map<String, Object> values = context.getValues();
		
		if(logger.isDebugEnabled()) {
			Iterator<Map.Entry<String, Object>> tempI = context.getValues().entrySet().iterator();
			while(tempI.hasNext()){
				Map.Entry<String, Object> e = tempI.next();
				logger.debug("JSON object key:{}, value:{}, class:{}", e.getKey(), e.getValue(), 
						e.getValue()==null ? "null" : e.getValue().getClass());
			}
		}
		
		Context jscx = Context.getCurrentContext();
		boolean jsf = jscx!=null;
		if( ! jsf)
			jscx = Context.enter();
		
		Iterator<String> keys = values.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			
			buffer.append(',');
			buffer.append( gson.toJson(key) );
			buffer.append(':');
			
			Object val = values.get(key);
			
			if(val==null){
				buffer.append( gson.toJson(null) );
			}else if(val instanceof Number) {
				buffer.append( gson.toJson(val) );
			}else if(val instanceof String){
				buffer.append( gson.toJson(val) );
			}else if(val instanceof Scriptable) {
				Scriptable scope = context.getScope();
				Scriptable object = (Scriptable)val;
				String stmt = (String)NativeJSON.stringify(Context.getCurrentContext(), scope, object, null, null);
				
				buffer.append('{');
				
				buffer.append( gson.toJson("Scriptable") );
				buffer.append(':');
				
				buffer.append( gson.toJson(stmt) );
				
				buffer.append('}');
			}else if(val instanceof Serializable){
				
				buffer.append('{');
				
				buffer.append( gson.toJson("Object") );
				buffer.append(':');
				
				byte[] buf = readValue(val);
				if(buf==null)
					continue;
				String stmt = Base64.encodeBase64URLSafeString( buf );
				buffer.append( gson.toJson(stmt) );
				
				buffer.append('}');
			}else{
				//error
			}//fi
		}
		
		if(buffer.length()>0){
			buffer.setCharAt(0, '{');
		}else{
			buffer.append('{');
		}
		buffer.append('}');
		
		if( ! jsf)
			Context.exit();
		
		return buffer.toString();
	}
	
	/**
	 * Reads the key value pairs from a JSON String, clears the existing map
	 * and puts the pairs into the map.
	 * 
	 * @param json
	 * @throws Exception
	 */
	public void fromJSON(String json) throws Exception {
		
		Map<String, Object> values = context.getValues();
		values.clear();
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(LinkedHashMap.class, new GsonHashMapDeserializer());
		Gson gson = gsonBuilder.create();
		
		final Callable rev = new Callable(){
			public Object call(Context cx, Scriptable scope,
					Scriptable thisObj, Object[] args) {
				//
				// sh, 12.12.2014
				// not really understood what happens, but
				// @see http://stackoverflow.com/questions/10856154/rhino-return-json-from-within-java
				// tested some toJSON/fromJSON sequences with success.
				//
				return args[1];
			}};
		
		Context jscx = Context.getCurrentContext();
		boolean jsf = jscx!=null;
		if( ! jsf)
			jscx = Context.enter();
		
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
			}else if(val instanceof Map<?, ?>) {
				LinkedHashMap<?, ?> mo2 = (LinkedHashMap<?, ?>)val;
				if(mo2.containsKey("Object")){
					String stmt = (String)mo2.get("Object");
					if(stmt!=null){
						Object obj = writeValue( Base64.decodeBase64( stmt ) );
						if(obj!=null){
							values.put(key, obj);
						}//fi
					}//fi
				}else if(mo2.containsKey("Scriptable")){
					String stmt = (String)mo2.get("Scriptable");
					if(stmt!=null){
						Scriptable scope = context.getScope();
						Object obj = NativeJSON.parse(jscx, scope, stmt, rev);
						if(obj!=null){
							values.put(key, obj);
						}//fi
					}//fi
				}else{
					continue;
				}
			}//fi
		}//while
		
		if( ! jsf)
			Context.exit();
	}
	
	/**/
	private byte[] readValue(Object val) throws Exception {
		if(val==null)
			return null;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream( buffer );
		out.writeObject(val);
		out.close();
		
		return buffer.toByteArray();
	}
	
	/**/
	private Object writeValue(byte[] buffer) throws Exception {
		if(buffer==null)
			return null;
		
		ObjectInputStream in = new ObjectInputStream( new ByteArrayInputStream(buffer) );
		Object val = (Object)in.readObject();
		in.close();
		
		return val;
	}

}
