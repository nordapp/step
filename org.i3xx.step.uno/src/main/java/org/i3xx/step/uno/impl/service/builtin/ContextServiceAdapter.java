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


import org.i3xx.step.uno.model.StepContext;

/**
 * The context interface as a service (adapter)
 * 
 * This service provides a setter and a getter to access the key-value-pair (=parameter) storage
 * of the context.
 * 
 * The service provides also a getService function to access further services of the context. The
 * getService function here does a call to the getService function of the context itself. 
 * 
 * @author Stefan
 *
 */
public class ContextServiceAdapter /* DO NOT IMPLEMENT BuiltinService */ {

	private StepContext context;
	
	public ContextServiceAdapter(StepContext context) {
		this.context = context;
	}
	
	public Object getService(String fullname) throws Exception {
		return this.context.getService(fullname);
	}

	/**
	 * Get the names of the values in the context as a String array.
	 * 
	 * @return The String array containing names of the the values.
	 */
	public String[] getNames() {
		return this.context.getNames();
	}
	
	/**
	 * Gets a parameter from the context
	 * 
	 * @param key The key of the parameter to get
	 * @return The value of the parameter
	 */
	public Object get(String key){ 
		return this.context.getValue(key);
	}
	
	/**
	 * Sets a parameter to the context
	 * 
	 * Note:
	 * It is not possible to remove a parameter from the context. But
	 * it is possible to clear a parameter by setting it's value to null.
	 * 
	 * The KeyIndexService provides a method to remove a key from the
	 * index. But it is set again the next time the value changes.
	 * 
	 * @param key The key of the parameter to set
	 * @param value The value of the parameter
	 */
	public void set(String key, Object value) {
		this.context.setValue(key, value);
	}
}
