package org.i3xx.step.mongo.core.model;

/*
 * #%L
 * NordApp OfficeBase :: mongo
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
import java.util.Set;

public interface DbObject {

	
	/**
	 * Indicates whether a field exists or not.
	 * 
	 * @param key
	 * @return
	 */
	boolean containsField(String key);
	
	/**
	 * Gets a value
	 * 
	 * @param key
	 * @return
	 */
	Object get(String key);
	
	/**
	 * Gets the key set
	 * 
	 * @return
	 */
	Set<String> keySet();
	
	/**
	 * Returns the result as a map
	 * 
	 * @return
	 */
	Map<String, Object> getMap();
	
	/**
	 * returns the result as a String
	 * 
	 * @return
	 */
	String toString();
	
}
