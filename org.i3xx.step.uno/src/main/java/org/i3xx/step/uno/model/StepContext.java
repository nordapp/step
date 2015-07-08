package org.i3xx.step.uno.model;

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


/**
 * This is the main adapter to handle every access to the world outside.
 * 
 * @author Stefan
 * @since step 0.1, 13.10.2014
 *
 */
public interface StepContext {

	/**
	 * The service getter. A service may be every java object.
	 * 
	 * @param fullname The full name of the service
	 * @return The service
	 */
	Object getService(String fullname) throws Exception;
	
	/**
	 * Get the names of the values in the context as a String array.
	 * 
	 * @return The String array containing names of the the values.
	 */
	String[] getNames();
	
	/**
	 * Gets a value from the context.
	 * 
	 * @param key The key of the value
	 * @return The value (only String and Object representation of a primitive type is allowed)
	 */
	Object getValue(String key);
	
	/**
	 * Sets a value to the context.
	 * 
	 * @param key The key of the value
	 * @param value The value (only String and Object representation of a primitive type is allowed)
	 */
	void setValue(String key, Object value);
}
