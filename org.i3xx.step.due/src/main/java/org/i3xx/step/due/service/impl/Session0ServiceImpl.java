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
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.i3xx.step.due.core.impl.SessionException;
import org.i3xx.step.due.core.impl.activator.DueLogChute;
import org.i3xx.step.due.service.model.Session;
import org.i3xx.step.due.service.model.Session0Service;
import org.i3xx.step.due.service.model.SessionService;
import org.i3xx.util.symbol.service.model.SymbolService;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Session0ServiceImpl implements Session0Service {
	
	static Logger logger = LoggerFactory.getLogger(Session0ServiceImpl.class);
			
	/** The osgi bundle context */
	private BundleContext bundleContext;

	public Session0ServiceImpl() {
		bundleContext = null;
	}
	
	/**
	 * The startup of the service
	 * @throws IOException 
	 */
	public void startUp() throws IOException {
		logger.info("Session0Service started");
	}

	public void initialize(SessionService sessionService, SymbolService symbolService, String mandatorId) {
		
		logger.info("Create the session '0' for the mandator {}", mandatorId);
		
		//
		// Create the '0' session
		//
		boolean created = false;
		Session session = null;
		String sessionId = "0";
		
		session = sessionService.getSession(null, mandatorId, sessionId);
		if(session==null){
			Map<String, String> params = new HashMap<String, String>();
			try {
				session = sessionService.createSession(null, mandatorId, -1, sessionId, params);
			} catch (SessionException e) {
				logger.warn("The session service is not initialized properly.", e);
				e.printStackTrace();
			}
			created = true;
		}
		
		//
		// Create Velocity instance
		//
		VelocityEngine ve = created ? null : (VelocityEngine)session.getValue(Session.ENGINE_VELOCITY);
		if(ve==null) {
			ve = new VelocityEngine();
			ve.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, new DueLogChute());
			ve.init();
			
			session.setValue(Session.ENGINE_VELOCITY, ve);
		}
		
		//
		// The EngineBase is created by the step.uno package and available as a service.
		//
		// @see EngineBaseServiceImpl.getService(BundleContext, String, String);
		if(session.getValue(Session.ENGINE_BASE_INDEX)==null){
			try{
				int index = symbolService.getSymbol(mandatorId);
				session.setValue(Session.ENGINE_BASE_INDEX, new Integer(index));
			}catch(IOException e){
				logger.warn("The 'engine.base.index' is not set properly.", e);
			}
		}
		
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
	 * Checks the permission of the Service ( read | write )
	 * 
	 * @param action The action to be done
	 * @param target The target the permission is requested for
	 */
	@SuppressWarnings("unused")
	private static void checkPermission(String action, String target) {
		
		//TODO Implement the permission to access the mandator configuration
	}

}
