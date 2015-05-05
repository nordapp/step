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


import java.util.Map;

import org.i3xx.step.due.core.impl.SessionException;
import org.osgi.framework.ServiceReference;

public interface SessionService {

	/**
	 * Creates a session to handle resources
	 * 
	 * @param cert The string to authenticate
	 * @param mandator The mandator of the session
	 * @param timeout The timeout of the session
	 * @param sessionId The id of the session, if the sessionId is null a new id is created.
	 * @param params The parameter of the session to be put to the service registry (not the session)
	 * @return
	 * @throws SessionException If the session can't be created.
	 * @throws SessionAlreadyExistsException If the sessionId exists.
	 */
	Session createSession(String cert, String mandator, long timeout, String sessionId, Map<String, String> params)throws SessionException;
	
	/**
	 * Gets a session or null if the session doesn't exist.
	 * 
	 * @param cert The string to authenticate
	 * @param mandatorId The mandator of the session
	 * @param sessionId The id of the session
	 * @return
	 */
	Session getSession(String cert, String mandatorId, String sessionId);
	/**
	 * Gets the session references of the matching sessions
	 * 
	 * @param mandatorId The id of the mandator (optional)
	 * @param sessionId The id of the session (optional)
	 * @return
	 */
	ServiceReference<?>[] getSessionReference(String mandatorId, String sessionId);
	
	/**
	 * <p>Returns true if the session is available.</p>
	 * 
	 * <p><b>Note:</b> If the sessionId is <b>'0'</b> the mandator id is <b>not</b> optional.</p>
	 * 
	 * @param mandatorId The id of the mandator (optional)
	 * @param sessionId The id of the session
	 * @return
	 */
	boolean exists(String mandatorId, String sessionId);
	
	/**
	 * Destroys a session
	 * 
	 * @param cert The string to authenticate
	 * @param mandatorId The mandator of the session
	 * @param sessionId The id of the session
	 * @return
	 */
	boolean destroy(String cert, String mandator, String sessionId);
}
