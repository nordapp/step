package org.i3xx.step.command.uno.core.impl;

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

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.i3xx.step.uno.impl.service.EngineBaseServiceImpl;
import org.i3xx.step.uno.model.service.EngineBaseService;
import org.i3xx.step.zero.service.impl.mandator.MandatorServiceImpl;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.util.symbol.service.model.SymbolService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(scope = "ob", name = "uno-stop", description="Removes the uno script engine and cleanup the cache.")
public class StopCommand extends OsgiCommandSupport {
	
	static Logger logger = LoggerFactory.getLogger(StopCommand.class);
	
    @Argument(index = 0, name = "mandatorId", description = "The id of the mandator", required = true, multiValued = false)
    private String mandatorId = null;

	/**
	 * 
	 */
	public StopCommand() {
	}

	@Override
	protected Object doExecute() throws Exception {
		
        logger.info("Uno-Stop mandator-id: '{}'", mandatorId);
        
        //gets the service
        BundleContext context = getBundleContext();
        
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
			EngineBaseService baseService = EngineBaseServiceImpl.getService(context, mandatorId, engineId.toString());
			
			baseService.removeEngine(engineId);
		}//fi
		
		return null;
	}

}
