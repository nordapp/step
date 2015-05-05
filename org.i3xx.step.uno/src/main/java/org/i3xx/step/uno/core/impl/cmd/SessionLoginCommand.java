package org.i3xx.step.uno.core.impl.cmd;

/*
 * #%L
 * NordApp OfficeBase :: uno
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


import java.math.BigInteger;
import java.util.UUID;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.i3xx.step.uno.impl.service.EngineBaseServiceImpl;
import org.i3xx.step.uno.model.daemon.Engine;
import org.i3xx.step.uno.model.service.EngineBaseService;
import org.i3xx.step.zero.service.impl.mandator.MandatorServiceImpl;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.step.zero.service.model.mandator.MandatorService;
import org.i3xx.util.symbol.service.model.SymbolService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(scope = "ob", name = "uno-session-login", description="Login to the mandator service")
public class SessionLoginCommand extends OsgiCommandSupport {
	
	static Logger logger = LoggerFactory.getLogger(SessionLoginCommand.class);
	
    @Argument(index = 0, name = "mandatorId", description = "The id of the mandator", required = true, multiValued = false)
    private String mandatorId = null;
	
    @Argument(index = 1, name = "userId", description = "The id of the user", required = true, multiValued = false)
    private String userId = null;
	
    @Argument(index = 2, name = "sessionId", description = "The id of an existing session", required = false, multiValued = false)
    private String sessionId = null;

	/**
	 * 
	 */
	public SessionLoginCommand() {
	}

	@Override
	protected Object doExecute() throws Exception {
		
        logger.info("Login mandator-id: '{}', user-id:'{}'", mandatorId, userId);
        
        //gets the service
        BundleContext context = getBundleContext();
        
		//
		// Gets a list of all configured mandator and initialize them.
		//
		ServiceReference<MandatorService> msr = context.getServiceReference(MandatorService.class);
		@SuppressWarnings("unused")
		MandatorService ms = context.getService(msr);
		
		//
		// Gets the symbol service
		//
		ServiceReference<SymbolService> ssr = context.getServiceReference(SymbolService.class);
		SymbolService symbolService = context.getService(ssr);
		
		//
		// Search the mandator
		//
		Mandator mandator = MandatorServiceImpl.getMandator(context, mandatorId);
		
		//
		// Run
		//
		if(mandator!=null) {
			// get the id of the mandator
			String mandatorId = mandator.getId();
			// get the symbol from the symbol service
			int index = symbolService.getSymbol( mandatorId );
			
			BigInteger engineId = BigInteger.valueOf( index );
			
			UUID ui = UUID.randomUUID();
			BigInteger id = BigInteger
					.valueOf(ui.getMostSignificantBits())
					.shiftLeft(64)
					.or(BigInteger.valueOf(ui.getLeastSignificantBits()));
			
			BigInteger session = sessionId==null ? id : new BigInteger(sessionId);
			EngineBaseService baseService = EngineBaseServiceImpl.getService(context, mandatorId, engineId.toString());
			
			System.out.println("Session:"+session);
			
			baseService.setupStore(session);
			Engine engine = baseService.getEngine(session);
			engine.setLogin(true);
			
			if( ! engine.hasNext()){
				engine.reinit();
			}
			
			//saves the content of the engine
			engine.save();
			
			//saves the state of the engine
			baseService.saveEngine(session);
			
		}//fi
		
		return null;
	}

}
