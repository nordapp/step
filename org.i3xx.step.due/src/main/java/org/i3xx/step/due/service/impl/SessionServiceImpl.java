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


import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.i3xx.step.due.core.impl.SessionException;
import org.i3xx.step.due.service.model.Session;
import org.i3xx.step.due.service.model.SessionService;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionServiceImpl implements SessionService, EventHandler {
	
	static Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);
			
	/** The osgi bundle context */
	private BundleContext bundleContext;
	
	private Map<String, ServiceRegistration<?>> regList;
	
	public SessionServiceImpl() {
		bundleContext = null;
		regList = new HashMap<String, ServiceRegistration<?>>();
	}
	
	/**
	 * The startup of the service
	 * @throws IOException 
	 */
	public void startUp() throws IOException {
		logger.info("SessionService started");
	}
	
	public void handleEvent(Event event) {
		logger.trace("Event {} received", event.getTopic());
		
		if(event.getTopic()==null){
			//does nothing
		}else
		if( event.getTopic().equals("session-destroy") ) {
			String mandatorId = (String)event.getProperty(Mandator.MANDATORID);
			String sessionId = (String)event.getProperty(Session.SESSIONID);
			
			destroy(null, mandatorId, sessionId);
		}//fi
	}
	
	public Session createSession(String cert, String mandatorId, long timeout,
			String sessionId, Map<String, String> params)
			throws SessionException {
		
		checkPermission("create", "session");
		Session session = null;
		
		if(exists(mandatorId, sessionId)) {
			session = getSession(mandatorId, sessionId);
		}else{
			logger.debug("Create session:{} mandator:{} timeout:{}", sessionId, mandatorId, timeout);
			session = new SessionImpl(sessionId, mandatorId, timeout);
			((SessionImpl) session).setBundleContext(bundleContext);
			
			String[] clazzes = new String[]{Session.class.getName()};
			Hashtable<String, Object> properties = new Hashtable<String, Object>();
			properties.put(Mandator.MANDATORID, mandatorId);
			properties.put(Mandator.USERACCESSID, Boolean.TRUE);
			properties.put(Session.SESSIONID, sessionId);
			properties.putAll(params);
			
			regList.put( sessionId, bundleContext.registerService(clazzes, session, properties) );
		}//fi
		
		return session;
	}
	
	/**
	 * @param mandatorId
	 * @param sessionId
	 * @return The session service
	 */
	private Session getSession(String mandatorId, String sessionId) {
		
		checkPermission("read", "session");
		Session session = null;
		
		//
		// Gets the session
		//
		try {
			String clazz = Session.class.getName();
			String filter = "(&("+Constants.OBJECTCLASS+"="+
					Session.class.getName()+")("+Mandator.MANDATORID+"="+
					mandatorId+")("+Session.SESSIONID+"="+sessionId+"))";
					
			ServiceReference<?>[] srv = bundleContext.getServiceReferences(clazz, filter);
			logger.debug("Get session session:{} mandator:{} refs:{}", sessionId, mandatorId, (srv==null?-1:srv.length));
			if(srv!=null && srv.length>0)
				session = (Session)bundleContext.getService(srv[0]);
		} catch (InvalidSyntaxException e) {
			logger.error("The syntax of the filter is not valid.", e);
		} catch (Exception e) {
			logger.error("The session search results in an exception.", e);
		}
		
		return session;
	}
	
	/**
	 * Gets the session references of the matching sessions
	 * 
	 * @param mandatorId The id of the mandator (optional)
	 * @param sessionId The id of the session (optional)
	 * @return The session reference
	 */
	public ServiceReference<?>[] getSessionReference(String mandatorId, String sessionId) {
		
		checkPermission("read", "session");
		ServiceReference<?>[] refs = null;
		
		//
		// Gets the session
		//
		try {
			String clazz = Session.class.getName();
			String filter = "(&("+Constants.OBJECTCLASS+"="+
					Session.class.getName()+")";
			if(mandatorId!=null)
				filter += "("+Mandator.MANDATORID+"="+mandatorId+")";
			if(sessionId!=null)
				filter += "("+Session.SESSIONID+"="+sessionId+")";
			filter += ")";
					
			 refs = bundleContext.getServiceReferences(clazz, filter);
			logger.debug("Get session service reference session:{} mandator:{} refs:{}", sessionId, mandatorId, (refs==null?-1:refs.length));
		} catch (InvalidSyntaxException e) {
			logger.error("The syntax of the filter is not valid.", e);
		} catch (Exception e) {
			logger.error("The session search results in an exception.", e);
		}
		
		return refs;
	}
	
	public Session getSession(String cert, String mandatorId, String sessionId) {
		checkPermission("read", "session");
		return getSession(mandatorId, sessionId);
	}

	public boolean exists(String mandatorId, String sessionId) {
		checkPermission("read", "session");
		ServiceReference<?>[] refs = getSessionReference(mandatorId, sessionId);
		return ((refs!=null) && (refs.length>0));
	}
	
	public boolean destroy(String cert, String mandatorId, String sessionId) {
		//
		// Usually the destroy method of the session sends this event. The
		// destroyed session must be removed from the registry. This is to
		// be done here.
		//
		// Other bundles, especially the mandator can also send the event
		// to destroy sessions belonging to them. These events are handled
		// here and the sessions will be destroyed calling session.destroy()
		// within the for loop. The isAlive query avoids an infinite loop.
		//
		checkPermission("destroy", "session");
		if(sessionId==null)
			checkPermission("destroy", "sessions");
		if(sessionId==null && mandatorId==null)
			checkPermission("destroy", "all-sessions");
		
		ServiceReference<?>[] refs = getSessionReference(mandatorId, sessionId);
		if(refs==null || refs.length==0)
			return false;
		
		boolean flag = true;
		for(ServiceReference<?> r : refs) {
			Session session = (Session)bundleContext.getService(r);
			if(session==null){
				logger.debug("The session is not avaialble (null).");
				continue;
			}
			
			if(session.isAlive()){
				String mn = session.getMandator();
				String id = session.getSessionId();
				
				session.destroy();
				
				logger.debug("The session '{}' of '{}' is destroyed.", mn, id);
			}
			
			if( ! bundleContext.ungetService(r) ){
				flag = false;
				logger.warn("The session '{}' of the mandator '{}' is not removed properly.", mandatorId, sessionId);
			}
			
			ServiceRegistration<?> reg = regList.remove(session.getSessionId());
			if(reg!=null){
				reg.unregister();
			}//fi
		}
		
		return flag;
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
	 * Gets the session with the id
	 * 
	 * @param bundleContext The bundle context
	 * @param cert The certificate
	 * @param mandatorId The id of the mandator
	 * @param sessionId The id of the session
	 * @return The session
	 */
	public static Session getSession(BundleContext bundleContext, String cert, String mandatorId, String sessionId) {
		
		checkPermission("read", "session");
		Session session = null;
		
		//
		// Gets the session
		//
		try {
			String clazz = Session.class.getName();
			String filter = "(&("+Constants.OBJECTCLASS+"="+
					Session.class.getName()+")("+Mandator.MANDATORID+"="+
					mandatorId+")("+Session.SESSIONID+"="+sessionId+"))";
					
			
			ServiceReference<?>[] srv = bundleContext.getServiceReferences(clazz, filter);
			logger.trace("Search session service reference session:{} mandator:{} result:{}", sessionId, mandatorId,
					srv==null ? -1 : srv.length);
			
			if(srv!=null && srv.length>0)
				session = (Session)bundleContext.getService(srv[0]);
		} catch (InvalidSyntaxException e) {
			logger.error("The syntax of the filter is not valid.", e);
		} catch (Exception e) {
			logger.error("The session search results in an exception.", e);
		}
		
		return session;
	}
	
	/**
	 * Checks the permission of the Service ( read | create | destroy )
	 * 
	 * @param action The action to be done
	 * @param target The target the permission is requested for
	 */
	private static void checkPermission(String action, String target) {
		
		//TODO Implement the permission to access the mandator configuration
	}

}
