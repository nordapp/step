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


import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.i3xx.step.uno.model.service.DeployService;
import org.i3xx.util.basic.io.CURL;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Command(scope = "ob", name = "deploy-default", description="Deploys a step script or a step manifest")
public class DeployDefaultCommand extends OsgiCommandSupport {
	
	static Logger logger = LoggerFactory.getLogger(DeployDefaultCommand.class);
	
    @Argument(index = 0, name = "mandatorId", description = "The id of the mandator", required = true, multiValued = false)
    private String mandatorId = null;
	
    @Argument(index = 1, name = "path", description = "The path of resource", required = true, multiValued = false)
    private String path = null;
	
    @Argument(index = 2, name = "resourceId", description = "The URI of the resource", required = true, multiValued = false)
    private String resourceId = null;
	
    @Argument(index = 3, name = "target", description = "The target (or destination) to write the resource to.", required = true, multiValued = false)
    private String target = null;
			
	public DeployDefaultCommand() {
		super();
	}
    
	@Override
	protected Object doExecute() throws Exception {
		
        logger.info("Deploy the resource '{}' to mandator-id: '{}', path: '{}'", resourceId, mandatorId, path);
        
        //get the service
        BundleContext context = getBundleContext();
        
		ServiceReference<DeployService> msr = context.getServiceReference(DeployService.class);
		DeployService deploy = context.getService(msr);
        
		//file content
		if(resourceId.startsWith("file:///")) {
			File res = CURL.fileURLtoFile(resourceId) ;
			
			deploy.deployDefault(mandatorId, path, target, res);
		}
		
		//http content
		if(resourceId.startsWith("http://")) {
			URI uri = URI.create(resourceId);
			URL url = uri.toURL();
			InputStream is = url.openStream();
			
			try{
				deploy.deployDefault(mandatorId, path, target, is);
			}finally{
				is.close();
			}
		}
		
		//ftp content
		if(resourceId.startsWith("ftp://")) {
			URI uri = URI.create(resourceId);
			URL url = uri.toURL();
			InputStream is = url.openStream();
			
			try{
				deploy.deployDefault(mandatorId, path, target, is);
			}finally{
				is.close();
			}
		}
		
		//mvn content
		//TODO:
		
		//
		
        return null;
	}
}
