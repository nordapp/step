package org.i3xx.step.due.service.impl;

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


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.i3xx.step.due.service.model.Session;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionImpl implements Session {
	
	static Logger logger = LoggerFactory.getLogger(SessionImpl.class);
	
	/** The osgi bundle context */
	private BundleContext bundleContext;
	
	/** The session id */
	private final String sessionId;
	
	/** The mandator */
	private final String mandator;
	
	/** The create time of the session */
	private final long startTime;
	
	/** the timeout the session is valid */
	private final long timeout;
	
	/** the data relating to this session */
	private final Map<String, Object> data;
	
	/** The session is valid */
	private boolean alive;
	
	public SessionImpl(String sessionId, String mandator, long timeout) {
		this.bundleContext = null;
		this.sessionId = sessionId;
		this.mandator = mandator;
		this.startTime = System.currentTimeMillis();
		this.timeout = timeout;
		this.data = new LinkedHashMap<String, Object>();
		
		this.alive = true;
	}

	public String getSessionId() {
		checkPermission("read", "session-id");
		return sessionId;
	}

	public String getMandator() {
		checkPermission("read", "mandator-id");
		return mandator;
	}

	public Set<String> getKeys() {
		checkPermission("read", "key");
		return data.keySet();
	}

	public Object getValue(String key) {
		checkPermission("read", "value");
		return data.get(key);
	}

	public void setValue(String key, Object value) {
		checkPermission("write", "value");
		data.put(key, value);
	}
	
	public void clearData() {
		checkPermission("write", "cleanup");
		data.clear();
	}

	public long getStartTime() {
		checkPermission("read", "starttime");
		return startTime;
	}

	public long getTimeout() {
		checkPermission("read", "timeout");
		return timeout;
	}

	public void destroy() {
		checkPermission("exec", "destroy");
		this.alive = false;
		
		//
		//Cleanup and private data of the session here.
		//
		this.data.clear();
		
		//
		//Cleanup any resource used by the session by sending the event 'session-destroy'.
		//
		//The resource manager of that resource should receive the event and clean
		//the resource after than in it's handleEvent method.
		//
		ServiceReference<EventAdmin> eventAdmRef = bundleContext.getServiceReference(EventAdmin.class);
		if(eventAdmRef!=null){
			EventAdmin eventAdmin = bundleContext.getService(eventAdmRef);
			if(eventAdmin!=null){
				Map<String, Object> data = new HashMap<String, Object>();
				data.put(Mandator.MANDATORID, mandator);
				data.put(Session.SESSIONID, sessionId);
				Event event = new Event("session-destroy", data);
				eventAdmin.sendEvent(event);
			}//fi
		}else{
			logger.debug("The EventAdminService is not present.");
		}//fi
	}

	public boolean isValid() {
		checkPermission("read", "is-valid");
		return alive && ( (timeout < 0) ||
				((System.currentTimeMillis() - timeout) < startTime) );
	}
	
	public boolean isAlive() {
		checkPermission("read", "is-alive");
		return alive;
	}
	
	/**
	 * @return the bundleContext
	 */
	public BundleContext getBundleContext() {
		return bundleContext;
	}

	/**
	 * @param bundleContext the bundleContext to set
	 */
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}
	
	/**
	 * Checks the permission of the Session ( read | write )
	 * 
	 * @param action The action to be done
	 * @param target The target the permission is requested for
	 */
	private static void checkPermission(String action, String target) {
		
		//TODO Implement the permission to access the mandator configuration
	}

}
