package org.i3xx.step.zero.security;

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


import java.util.Properties;

import org.i3xx.step.zero.security.impl.shiro.NaFactoryImpl;
import org.i3xx.step.zero.security.model.NaSession;
import org.i3xx.step.zero.security.model.NaSubject;

public final class NaSecurityUtil {

	static final NaFactory naFactory = new NaFactoryImpl();
	
	/**
	 * Sets the session manager
	 * 
	 * @param props (currently null)
	 */
	public static void setSecurityManager(Properties props) {
		if(props==null) {
			//does nothing
		}
		naFactory.setSecurityManager(props);
	}
	
	/**
	 * @param sessionId The session id
	 * @return The subject
	 */
	public static NaSubject createSubject(String sessionId) {
		return naFactory.createSubject(sessionId);
	}
	
	/**
	 * @return The subject
	 */
	public static NaSubject getSubject() {
		return naFactory.getSubject();
	}
	
	/**
	 * @return The session
	 */
	public static NaSession getSession() {
		return naFactory.getSession();
	}
}
