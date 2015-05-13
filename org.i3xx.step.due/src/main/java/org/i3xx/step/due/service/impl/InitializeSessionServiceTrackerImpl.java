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


import org.i3xx.step.due.service.model.Session0Service;
import org.i3xx.step.due.service.model.SessionService;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.step.zero.service.model.mandator.MandatorService;
import org.i3xx.util.symbol.service.model.SymbolService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starting the SessionService, a '0' session for every mandator will be created.
 * 
 * @author Stefan
 *
 */
public class InitializeSessionServiceTrackerImpl extends ServiceTracker<SessionService, SessionService> {
	
	static Logger logger = LoggerFactory.getLogger(InitializeSessionServiceTrackerImpl.class);
	
	/** The service */
	private Session0Service session0Service;
	
	public InitializeSessionServiceTrackerImpl(BundleContext bundleContext,
			Session0Service session0Service) {
		
		super(bundleContext, SessionService.class.getName(), null);
		
		this.session0Service = session0Service;
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
	 */
	public SessionService addingService(ServiceReference<SessionService> reference) {
		
		logger.debug("adding service, create every session0, initiated by session service.");
		
		SessionService sessionService = context.getService(reference);
		
		//
		// Gets the symbol service
		//
		ServiceReference<SymbolService> ssr = context.getServiceReference(SymbolService.class);
		SymbolService symbolService = context.getService(ssr);
		if(symbolService==null) {
			logger.warn("The symbol service is not available (maybe down or a version conflict).");
			return null;
		}
		
		//
		// Gets the configured mandator and initialize the '0' session.
		//
		
		//
		// Search the service if it can be found (at blueprint start, the service is not in the registry)
		//
		ServiceReference<Session0Service> osr = context.getServiceReference(Session0Service.class);
		if(osr==null){
			logger.debug("The session '0' service is not available (1).");
		}else{
			Session0Service os = context.getService(osr);
			if(os==null) {
				logger.debug("The session '0' service is not available (2).");
			}else{
				session0Service = os;
			}//fi
		}//fi
		
		//
		// Gets the configured mandator and initialize the '0' session.
		//
		ServiceReference<MandatorService> msr = context.getServiceReference(MandatorService.class);
		MandatorService ms = context.getService(msr);
		if(ms==null)
			logger.warn("The mandator service is not available (maybe down or a version conflict).");
		
		for(String mandatorId : ms.getMandatorList()){
			
			Mandator mandator = ms.getMandator(mandatorId);
			if(mandator==null)
				logger.warn("The mandator '"+mandatorId+"' is not available on this system.");
			else
				logger.debug("The mandator {} is available at the path '{}'.", mandatorId, mandator.getPath());
			
			//initialize the session
			session0Service.initialize(sessionService, symbolService, mandatorId);
		}//for
		
		return sessionService;
	}

	public void removedService(ServiceReference<SessionService> reference, SessionService service) {
		
		logger.debug("removing service, destroy all sessions, initiated by session service.");
		
		service.destroy(null, null, null);
		context.ungetService(reference);
	}
}
