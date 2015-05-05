package org.i3xx.step.zero.core.impl.cmd;

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


import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.i3xx.step.zero.service.model.mandator.RootService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(scope = "ob", name = "mandator-destroy", description="Destroys a mandator")
public class DestroyMandatorCommand extends OsgiCommandSupport {

	static Logger logger = LoggerFactory.getLogger(DestroyMandatorCommand.class);
	
    @Argument(index = 0, name = "arg", description = "The mandator-pid to destroy", required = true, multiValued = false)
    String arg = null;

	@Override
	protected Object doExecute() throws Exception {
		String pid = arg;
		
		logger.info("Destroys the mandator '{}'.", pid);
        
        //get the service
        BundleContext bc = getBundleContext();
        
        ServiceReference<RootService> srA = bc.getServiceReference(RootService.class);
        RootService rootService = bc.getService(srA);
        
        rootService.destroyMandator(pid);
        
    	logger.info("The mandator '{}' has been destroyed.", pid);
        
		return null;
	}

}
