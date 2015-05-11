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


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.i3xx.step.due.service.model.Session;
import org.i3xx.step.due.service.model.VelocityService;
import org.i3xx.step.zero.service.impl.mandator.MandatorServiceImpl;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.util.basic.io.FilePath;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class VelocityServiceImpl implements VelocityService {
	
	static Logger logger = LoggerFactory.getLogger(VelocityServiceImpl.class);
			
	/** The osgi bundle context */
	private BundleContext bundleContext;
	
	public VelocityServiceImpl() {
		bundleContext = null;
	}
	
	/**
	 * The startup of the service
	 * @throws IOException 
	 */
	public void startUp() throws IOException {
		logger.info("VelocityService started");
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.due.service.model.ResourceService#getResource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.util.Map)
	 */
	public String getResource(String mandatorId, String groupId,
			String artifactId, String path, int type,
			Map<String, String> props) throws IOException {
		
		checkPermission("read", "resource");
		logger.debug("Resource for {} requested '{}'.", mandatorId, path);
		
		Mandator mandator = MandatorServiceImpl.getMandator(bundleContext, mandatorId);
		if(mandator==null)
			throw new IOException("Not found: The requested mandator '"+mandatorId+"' is not available at this system.");
		
		FilePath filePath = FilePath.append( mandator.getPath(), "data/page/file/bundle", groupId, artifactId, path );
		File file = filePath.toFile();
		
		if( !file.exists() ){
			logger.trace("The resource '{}' is not available.", file.getAbsolutePath());
			throw new IOException("Not found: The requested resource '"+file.getName()+"' is not available at this system.");
		}
		if( !file.isFile() ){
			logger.trace("The resource '{}' is no file.", file.getAbsolutePath());
			throw new IOException("Illegal: The requested resource '"+file.getName()+"' is not a file.");
		}
		
		//Get the '0' session
		Session session0 = SessionServiceImpl.getSession(bundleContext, null, mandatorId, "0");
		if(session0==null)
			throw new IOException("The session(0) service is not available for the mandator '"+mandatorId+"'.");
		
		VelocityEngine ve = (VelocityEngine)session0.getValue(Session.ENGINE_VELOCITY);
		if(ve==null)
			throw new IOException("The velocity engine is not available for the mandator '"+mandatorId+"'.");
		
		VelocityContext context = new VelocityContext();
		context.put("subject.mandator.id", mandatorId);
		context.put("subject.group.id", groupId);
		context.put("subject.artifact.id", artifactId);
		//put the properties to the context
		for(Map.Entry<String, String> e : props.entrySet())
			context.put(e.getKey(), e.getValue());
		
		FileReader in = new FileReader(file);
		StringWriter out = new StringWriter();
		try{
			boolean f = ve.evaluate(context, out, "VelocityServiceImpl", in);
			if(!f)
				logger.debug("Velocity for '{}' request fail at template '{}'.", mandatorId, file.getAbsolutePath());
		}finally{
			in.close();
			out.close();
		}
		
		return out.toString();
	}

	/* (non-Javadoc)
	 * @see org.i3xx.step.due.service.model.ResourceService#getResourceAsStream(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.util.Map)
	 */
	public InputStream getResourceAsStream(String mandatorId, String groupId,
			String artifactId, String path,	int type,
			Map<String, String> props) throws IOException {
		
		checkPermission("read", "resource");
		logger.debug("Resource for {} requested '{}'.", mandatorId, path);
		
		Mandator mandator = MandatorServiceImpl.getMandator(bundleContext, mandatorId);
		if(mandator==null)
			throw new IOException("Not found: The requested mandator '"+mandatorId+"' is not available at this system.");
		
		FilePath filePath = FilePath.append( mandator.getPath(), "data/page/file/bundle", groupId, artifactId, path );
		File file = filePath.toFile();
		
		if( !file.exists() ){
			logger.trace("The resource '{}' is not available.", file.getAbsolutePath());
			throw new IOException("Not found: The requested resource '"+file.getName()+"' is not available at this system.");
		}
		if( !file.isFile() ){
			logger.trace("The resource '{}' is no file.", file.getAbsolutePath());
			throw new IOException("Illegal: The requested resource '"+file.getName()+"' is not a file.");
		}
		
		
		//Get the '0' session
		Session session0 = SessionServiceImpl.getSession(bundleContext, null, mandatorId, "0");
		if(session0==null)
			throw new IOException("The session(0) service is not available for the mandator '"+mandatorId+"'.");
		
		VelocityEngine ve = (VelocityEngine)session0.getValue(Session.ENGINE_VELOCITY);
		if(ve==null)
			throw new IOException("The velocity engine is not available for the mandator '"+mandatorId+"'.");
		
		VelocityContext context = new VelocityContext();
		context.put("subject_mandator_id", mandatorId);
		context.put("subject_group_id", groupId);
		context.put("subject_artifact_id", artifactId);
		//put the properties to the context
		for(Map.Entry<String, String> e : props.entrySet())
			context.put(e.getKey(), e.getValue());
		
		FileReader in = new FileReader(file);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		OutputStreamWriter out = new OutputStreamWriter(bout);
		try{
			boolean f = ve.evaluate(context, out, "VelocityServiceImpl", in);
			if(!f)
				logger.debug("Velocity for '{}' request fail at template '{}'.", mandatorId, file.getAbsolutePath());
		}finally{
			in.close();
			out.close();
		}
		
		return new ByteArrayInputStream(bout.toByteArray());
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
	 * Checks the permission of the Service ( read )
	 * 
	 * @param action The action to be done
	 * @param target The target the permission is requested for
	 */
	private static void checkPermission(String action, String target) {
		
		//TODO Implement the permission to access the mandator configuration
	}
}
