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


import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.i3xx.step.zero.service.model.mandator.RootService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(scope = "ob", name = "mandator-setup", description="Setup a mandator")
@Service
public class SetupMandatorCommand implements Action {
	
	static Logger logger = LoggerFactory.getLogger(SetupMandatorCommand.class);
	
    @Argument(index = 0, name = "arg", description = "The id of the mandator", required = true, multiValued = false)
    private String arg = null;
    
    //Do not inject a service in a command yet (01.11.2014). May work in near future.
    
    public SetupMandatorCommand() {
    	super();
    }
    
	@Override
	public Object execute() throws Exception {
		
        logger.info("Setup the mandator '{}'.", arg);
        
        //get the service
        BundleContext bc = FrameworkUtil.getBundle(SetupMandatorCommand.class).getBundleContext();
        
        ServiceReference<RootService> srA = bc.getServiceReference(RootService.class);
        RootService rootService = bc.getService(srA);
        
        rootService.createMandator(arg);
        
		return null;
	}
	
}
