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


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.i3xx.step.uno.model.service.DeployService;
import org.i3xx.util.basic.io.CURL;
import org.i3xx.util.basic.io.FilePath;
import org.i3xx.util.basic.util.text.QuotedStringTokenizer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Command(scope = "ob", name = "deploy-feature", description="Deploy by a feature file")
public class DeployFeatureCommand extends OsgiCommandSupport {
	
	static Pattern pattern = Pattern.compile("\\$(\\d)");
	
	static Logger logger = LoggerFactory.getLogger(DeployFeatureCommand.class);
	
    @Argument(index = 0, name = "file", description = "The feature file", required = false, multiValued = false)
    private String file = null;
	
    @Argument(index = 1, name = "arg0", description = "The argument 0", required = false, multiValued = false)
    private String arg0 = null;
	
    @Argument(index = 2, name = "arg1", description = "The argument 1", required = false, multiValued = false)
    private String arg1 = null;
	
    @Argument(index = 3, name = "arg2", description = "The argument 2", required = false, multiValued = false)
    private String arg2 = null;
	
    @Argument(index = 4, name = "arg3", description = "The argument 3", required = false, multiValued = false)
    private String arg3 = null;
	
    @Argument(index = 5, name = "arg4", description = "The argument 4", required = false, multiValued = false)
    private String arg4 = null;
	
	public DeployFeatureCommand() {
		super();
	}
    
	@Override
	protected Object doExecute() throws Exception {
        
        //get the service
        BundleContext context = getBundleContext();
        
		ServiceReference<DeployService> msr = context.getServiceReference(DeployService.class);
		DeployService deploy = context.getService(msr);
		
		FilePath path = FilePath.get(file);
		
		LineNumberReader in = new LineNumberReader(new FileReader(path.toFile()));
		try{
			for(;;) {
				String line = in.readLine();
				if(line==null)
					break;
				
				QuotedStringTokenizer tok = new QuotedStringTokenizer(line, " ");
				while( tok.hasMoreElements() ){
					String type = tok.nextElement();
					
					if(type.equalsIgnoreCase("default")){
						//mandator id
						String arg0 = testArgument( tok.nextElement() );
						//path
						String arg1 = testArgument( tok.nextElement() );
						//resource id
						String arg2 = testArgument( tok.nextElement() );
						//target
						String arg3 = testArgument( tok.nextElement() );
						
						deployDefault(deploy, arg2, arg0, arg1, arg3);
					}
					else if(type.equalsIgnoreCase("step")){
						//mandator id
						String arg0 = testArgument( tok.nextElement() );
						//group id
						String arg1 = testArgument( tok.nextElement() );
						//artifact id
						String arg2 = testArgument( tok.nextElement() );
						//resource id
						String arg3 = testArgument( tok.nextElement() );
						//target
						String arg4 = testArgument( tok.nextElement() );
						
						deployStep(deploy, arg3, arg0, arg1, arg2, arg4);
					}
					else if(type.equalsIgnoreCase("resources")){
						//mandator id
						String arg0 = testArgument( tok.nextElement() );
						//group id
						String arg1 = testArgument( tok.nextElement() );
						//artifact id
						String arg2 = testArgument( tok.nextElement() );
						//resource id
						String arg3 = testArgument( tok.nextElement() );
						//target
						String arg4 = testArgument( tok.nextElement() );
						
						deployResources(deploy, arg3, arg0, arg1, arg2, arg4);
					}
					else if(type.equalsIgnoreCase("properties")){
						//mandator id
						String arg0 = testArgument( tok.nextElement() );
						//group id
						String arg1 = testArgument( tok.nextElement() );
						//artifact id
						String arg2 = testArgument( tok.nextElement() );
						//resource id
						String arg3 = testArgument( tok.nextElement() );
						//target
						String arg4 = testArgument( tok.nextElement() );
						
						deployProperties(deploy, arg3, arg0, arg1, arg2, arg4);
					}
					else
						throw new IllegalStateException("The type '"+type+"' is not valid.");
				}//while
			}//for
		}finally{
			in.close();
		}
		
        return null;
	}
	
	/**
	 * @param deploy
	 * @param resourceId
	 * @param mandatorId
	 * @param groupId
	 * @param artifactId
	 * @param target
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	private void deployStep(DeployService deploy, String resourceId, String mandatorId, String groupId, String artifactId, String target) throws IOException, URISyntaxException {
		
		//file content
		if(resourceId.startsWith("file:///")) {
			File res = CURL.fileURLtoFile(resourceId) ;
			
			deploy.deployContent(mandatorId, groupId, artifactId, target, res);
		}
		
		//http content
		if(resourceId.startsWith("http://")) {
			URI uri = URI.create(resourceId);
			URL url = uri.toURL();
			InputStream is = url.openStream();
			
			try{
				deploy.deployContent(mandatorId, groupId, artifactId, target, is);
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
				deploy.deployContent(mandatorId, groupId, artifactId, target, is);
			}finally{
				is.close();
			}
		}
		
		//mvn content
		//TODO:
		
		//
	}
	
	/**
	 * @param deploy
	 * @param resourceId
	 * @param mandatorId
	 * @param path
	 * @param target
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	private void deployDefault(DeployService deploy, String resourceId, String mandatorId, String path, String target) throws IOException, URISyntaxException {
		
        
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
	}
	
	/**
	 * @param deploy
	 * @param resourceId
	 * @param mandatorId
	 * @param path
	 * @param target
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	private void deployResources(DeployService deploy, String resourceId, String mandatorId, String groupId, String artifactId, String target) throws IOException, URISyntaxException {
		
        
		//file content
		if(resourceId.startsWith("file:///")) {
			File res = CURL.fileURLtoFile(resourceId) ;
			
			deploy.deployResources(mandatorId, groupId, artifactId, target, res);
		}
		
		//http content
		if(resourceId.startsWith("http://")) {
			URI uri = URI.create(resourceId);
			URL url = uri.toURL();
			InputStream is = url.openStream();
			
			try{
				deploy.deployResources(mandatorId, groupId, artifactId, target, is);
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
				deploy.deployResources(mandatorId, groupId, artifactId, target, is);
			}finally{
				is.close();
			}
		}
		
		//mvn content
		//TODO:
		
		//
	}
	
	/**
	 * @param deploy
	 * @param resourceId
	 * @param mandatorId
	 * @param path
	 * @param target
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	private void deployProperties(DeployService deploy, String resourceId, String mandatorId, String groupId, String artifactId, String target) throws IOException, URISyntaxException {
		
        
		//file content
		if(resourceId.startsWith("file:///")) {
			File res = CURL.fileURLtoFile(resourceId) ;
			
			deploy.deployProperties(mandatorId, groupId, artifactId, target, res);
		}
		
		//http content
		if(resourceId.startsWith("http://")) {
			URI uri = URI.create(resourceId);
			URL url = uri.toURL();
			InputStream is = url.openStream();
			
			try{
				deploy.deployProperties(mandatorId, groupId, artifactId, target, is);
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
				deploy.deployProperties(mandatorId, groupId, artifactId, target, is);
			}finally{
				is.close();
			}
		}
		
		//mvn content
		//TODO:
		
		//
	}
	
	private String testArgument(String stmt) {
		
		Matcher m = pattern.matcher(stmt);
		if(m.matches()) {
			int n = Integer.parseInt( m.group(1) );
			switch(n){
			case 0:
				return arg0;
			case 1:
				return arg1;
			case 2:
				return arg2;
			case 3:
				return arg3;
			case 4:
				return arg4;
			default:
			}
		}
		
		return stmt;
	}
}
