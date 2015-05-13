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


import org.i3xx.step.due.service.model.InstPropertyService;
import org.i3xx.step.due.service.model.Session0Service;
import org.i3xx.step.due.service.model.SessionService;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.util.symbol.service.model.SymbolService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * For each new Mandator a '0' session will be created. If a mandator ends the sessions
 * still remain valid until they are closed or running out of time.
 * 
 * @author Stefan
 *
 */
public class InitializeMandatorTrackerImpl extends ServiceTracker<Mandator, Mandator> {

	static Logger logger = LoggerFactory.getLogger(InitializeMandatorTrackerImpl.class);
	
	/** The service */
	private SessionService sessionService;
	
	/** The service */
	private Session0Service session0Service;
	
	/** The service */
	private InstPropertyService instPropertyService;
	
	public InitializeMandatorTrackerImpl(BundleContext bundleContext,
			SessionService sessionService, Session0Service session0Service,
			InstPropertyService instPropertyService) {
		
		super(bundleContext, Mandator.class.getName(), null);
		
		this.sessionService = sessionService;
		this.session0Service = session0Service;
		this.instPropertyService = instPropertyService;
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#addingService(org.osgi.framework.ServiceReference)
	 */
	public Mandator addingService(ServiceReference<Mandator> reference) {
		
		Mandator mandator = context.getService(reference);
		if(mandator==null)
			logger.warn("The mandator service is not available (maybe down or a version conflict).");
		
		if(! mandator.isInitialized()){
			logger.warn("The mandator is not initialized (initialize mandator before use).");
			return null;
		}
		
		logger.debug("adding service, create one session0, initiated by mandator id:{}", mandator.getId());
		
		//
		// Gets the symbol service
		//
		ServiceReference<SymbolService> ssr = context.getServiceReference(SymbolService.class);
		SymbolService symbolService = context.getService(ssr);
		if(symbolService==null)
			logger.warn("The symbol service is not available (maybe down or a version conflict).");
		
		//
		// Gets the configured mandator and initialize the '0' session.
		//
		
		//
		// Search the service if it can be found (at blueprint start, the service is not in the registry)
		//
		ServiceReference<Session0Service> osr = context.getServiceReference(Session0Service.class);
		if(osr==null){
			logger.debug("The session '0' service is not available (3).");
		}else{
			Session0Service os = context.getService(osr);
			if(os==null){
				logger.debug("The session '0' service is not available (4).");
			}else{
				session0Service = os;
			}//fi
		}//fi
		
		//
		// Gets the session service
		//
		ServiceReference<SessionService> xsr = context.getServiceReference(SessionService.class);
		if(xsr==null){
			logger.debug("The session service is not available (5).");
		}else{
			SessionService xs = context.getService(xsr);
			if(xs==null) {
				logger.debug("The session service is not available (maybe down or a version conflict).");
			}else{
				sessionService = xs;
			}
		}//fi
		
		//
		// Create a new session if necessary.
		//
		session0Service.initialize(sessionService, symbolService, mandator.getId());
		
		//
		// Scan the properties of the mandator
		//
		PropertyScanner scan = new PropertyScanner();
		scan.setBundleContext(context);
		scan.setInstPropertyService(instPropertyService);
		scan.scan(mandator.getId());
		
		
		return mandator;
	}

}
