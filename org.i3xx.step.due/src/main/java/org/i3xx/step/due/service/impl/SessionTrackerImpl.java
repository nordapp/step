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


import org.i3xx.step.due.service.model.Session;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionTrackerImpl extends ServiceTracker<Session, Session> {
	
	static Logger logger = LoggerFactory.getLogger(SessionTrackerImpl.class);

	public SessionTrackerImpl(BundleContext bundleContext) {
		super(bundleContext, Session.class.getName(), null);
	}
	
	public Session addingService(ServiceReference<Session> reference) {
		
		Session session = context.getService(reference);
		
		//setup default values from session 0
		Session session0 = SessionServiceImpl.getSession(context, null, session.getMandator(), "0");
		if(session0==null) {
			logger.warn("The session:{}, mandator:{} is not setup properly.", session.getSessionId(), session.getMandator());
			return session;
		}
		logger.debug("The session:{}, mandator:{} is setup properly.", session.getSessionId(), session.getMandator());
		
		session.setValue(Session.ENGINE_BASE_INDEX, session0.getValue(Session.ENGINE_BASE_INDEX));
		
		return session;
	}

	public void removedService(ServiceReference<Session> reference, Session service) {
		//does nothing
		//context.ungetService(reference);
	}

}
