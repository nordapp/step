package org.i3xx.step.due.service.model;

/*
 * #%L
 * NordApp OfficeBase :: due
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


import java.util.Set;

public interface Session {
	
	/**
	 * The id of the session
	 */
	public static final String SESSIONID = "sessionId";
	
	/**
	 * The index of the EngineBase from the mandator-id by the SymbolService
	 */
	public static final String ENGINE_BASE_INDEX = "engine.base.index";
	
	/** The key of the velocity engine */
	public static final String ENGINE_VELOCITY = "engine.velocity";
	
	/**
	 * Gets the session id
	 * 
	 * @return
	 */
	String getSessionId();
	
	/**
	 * Gets the mandator
	 * 
	 * @return
	 */
	String getMandator();
	
	/**
	 * Get a set of keys of the key-value-pairs.
	 * 
	 * @return
	 */
	Set<String> getKeys();
	
	/**
	 * Gets a value
	 * 
	 * @param key
	 * @return
	 */
	Object getValue(String key);
	
	/**
	 * Sets a value
	 * 
	 * @param key
	 * @param value
	 */
	void setValue(String key, Object value);
	
	/**
	 * Cleanup the data map
	 */
	void clearData();
	
	/**
	 * Gets the start time of the session
	 * 
	 * @return
	 */
	long getStartTime();
	
	/**
	 * Gets the timeout
	 * 
	 * @return
	 */
	long getTimeout();
	
	/**
	 * Returns true if the session is not destroyed and not running out of time.
	 * 
	 * @return
	 */
	boolean isValid();
	
	/**
	 * Returns true if the session is not destroyed.
	 * 
	 * @return
	 */
	boolean isAlive();
	
	/**
	 * Destroys the session
	 */
	void destroy();
}
