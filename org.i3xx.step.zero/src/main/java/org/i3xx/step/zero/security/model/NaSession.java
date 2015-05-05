package org.i3xx.step.zero.security.model;

/*
 * #%L
 * NordApp OfficeBase :: zero
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


import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public interface NaSession {
	
	/**
	 * Returns a session id
	 * 
	 * @return
	 */
	Serializable getId();
	
	/**
	 * The attribute of the session
	 * @param key The key of the attribute
	 * @return
	 */
	Object getAttribute(Object key);
	
	/**
	 * @return The list of keys
	 */
	Collection<Object> getAttributeKeys();
	
	/**
	 * @return The host
	 */
	String getHost();
	
	/**
	 * @return The time of the last access
	 */
	Date getLastAccessTime();
	
	/**
	 * @return The timeout
	 */
	long getTimeout();
	
	/**
	 * @return The timestamp of the session's start time
	 */
	Date getStartTimestamp();
	
	/**
	 * Removes the attribute
	 * 
	 * @param key The key
	 * @return
	 */
	Object removeAttribute(Object key);
	
	/**
	 * Sets the attribute
	 * 
	 * @param key The key of the attribute
	 * @param value The value of the attribute
	 */
	void setAttribute(Object key, Object value);
	
	/**
	 * Sets the timeout of the session
	 * 
	 * @param timeout The timeout
	 */
	void setTimeout(long timeout);
	
	/**
	 * Stops the session
	 */
	void stop();
}
