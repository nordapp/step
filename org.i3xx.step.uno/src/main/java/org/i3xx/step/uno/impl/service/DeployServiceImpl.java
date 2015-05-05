package org.i3xx.step.uno.impl.service;

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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.NoSuchElementException;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.i3xx.step.uno.model.service.DeployService;
import org.i3xx.step.uno.model.service.FileLocationService;
import org.i3xx.step.zero.service.impl.mandator.MandatorServiceImpl;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.util.basic.io.FilePath;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DeployServiceImpl implements DeployService {
	
	static Logger logger = LoggerFactory.getLogger(DeployService.class);
	
	/** The osgi bundle context */
	private BundleContext bundleContext;
	
	/** The file location service */
	private FileLocationService fileLoc;
	
	public DeployServiceImpl() {
		this.setBundleContext(null);
		this.setFileLocation(null);
	}


	public void deployContent(String mandatorId, String groupId, String artifactId, String target,
			File content) throws IOException {
		
		deployContentA(mandatorId, "data/step/bin/bundle/"+groupId+"/"+artifactId+"/js", target, content);
	}
	
	public void deployDefault(String mandatorId, String path, String target,
			File content) throws IOException {
		
		deployContentA(mandatorId, "data/step/bin/main/js/"+path, target, content);
	}
	
	public void deployResources(String mandatorId, String groupId, String artifactId, String target,
			File content) throws IOException {
		
		deployContentA(mandatorId, "data/page/file/bundle/"+groupId+"/"+artifactId+"/resources", target, content);
	}
	
	public void deployProperties(String mandatorId, String groupId, String artifactId, String target,
			File content) throws IOException {
		
		deployContentA(mandatorId, "data/page/file/bundle/"+groupId+"/"+artifactId+"/properties", target, content);
	}
	
	private void deployContentA(String mandatorId, String path, String target,
			File content) throws IOException {
		
		logger.info("Deploy the resource '{}' to mandator-id: '{}', path: '{}', target:'{}'", content, mandatorId, path, target);
		
		Mandator m = MandatorServiceImpl.getMandator(bundleContext, mandatorId);
		if(m==null)
			throw new NoSuchElementException("There is no mandator '"+mandatorId+"' available on theis system.");
		
		FilePath resLoc = FilePath.get(m.getPath());
		
		FilePath loc = resLoc.add(path);
		ensureLocation( loc.toFile() );
		
		File dest = getFile(loc.toFile(), target);
		OutputStream out = new FileOutputStream(dest);
		InputStream in = new FileInputStream(content);
		
		byte[] buf = new byte[1024];
		int c = 0;
		
		try{
			while((c=in.read(buf))>-1)
				out.write(buf, 0, c);
		}finally{
			try{
				in.close();
			}finally{
				out.close();
			}
		}
	}

	public void deployContent(String mandatorId, String groupId, String artifactId, String target,
			String content) throws IOException {
		
		deployContentA(mandatorId, "data/step/bin/bundle/"+groupId+"/"+artifactId+"/js", target, content);
	}
	
	public void deployDefault(String mandatorId, String path, String target,
			String content) throws IOException {
		
		deployContentA(mandatorId, "data/step/bin/main/js/"+path, target, content);
	}
	
	public void deployResources(String mandatorId, String groupId, String artifactId, String target,
			String content) throws IOException {
		
		deployContentA(mandatorId, "data/page/file/bundle/"+groupId+"/"+artifactId+"/resources", target, content);
	}
	
	public void deployProperties(String mandatorId, String groupId, String artifactId, String target,
			String content) throws IOException {
		
		deployContentA(mandatorId, "data/page/file/bundle/"+groupId+"/"+artifactId+"/properties", target, content);
	}
	
	private void deployContentA(String mandatorId, String path, String target,
			String content) throws IOException {
		
		logger.info("Deploy the resource (size) '{}' to mandator-id: '{}', path: '{}', target:'{}'", content.length(), mandatorId, path, target);

		Mandator m = MandatorServiceImpl.getMandator(bundleContext, mandatorId);
		if(m==null)
			throw new NoSuchElementException("There is no mandator '"+mandatorId+"' available on theis system.");
		
		FilePath resLoc = FilePath.get(m.getPath());
		
		FilePath loc = resLoc.add(path);
		ensureLocation( loc.toFile() );
		
		File dest = getFile(loc.toFile(), target);
		Writer out = new FileWriter(dest);
		Reader in = new StringReader(content);
		
		char[] cbuf = new char[1024];
		int c = 0;
		
		try{
			while((c=in.read(cbuf))>-1)
				out.write(cbuf, 0, c);
		}finally{
			try{
				in.close();
			}finally{
				out.close();
			}
		}
	}

	public void deployContent(String mandatorId, String groupId, String artifactId, String target,
			InputStream content) throws IOException {
		
		deployContentA(mandatorId, "data/step/bin/bundle/"+groupId+"/"+artifactId+"/js", target, content);
	}
	
	public void deployDefault(String mandatorId, String path, String target,
			InputStream content) throws IOException {
		
		deployContentA(mandatorId, "data/step/bin/main/js/"+path, target, content);
	}
	
	public void deployResources(String mandatorId, String groupId, String artifactId, String target,
			InputStream content) throws IOException {
		
		deployContentA(mandatorId, "data/page/file/bundle/"+groupId+"/"+artifactId+"/resources", target, content);
	}
	
	public void deployProperties(String mandatorId, String groupId, String artifactId, String target,
			InputStream content) throws IOException {
		
		deployContentA(mandatorId, "data/page/file/bundle/"+groupId+"/"+artifactId+"/properties", target, content);
	}
	
	private void deployContentA(String mandatorId, String path, String target,
			InputStream content) throws IOException {
		
		Mandator m = MandatorServiceImpl.getMandator(bundleContext, mandatorId);
		if(m==null)
			throw new NoSuchElementException("There is no mandator '"+mandatorId+"' available on theis system.");
		
		FilePath resLoc = FilePath.get(m.getPath());
		
		FilePath loc = resLoc.add(path);
		ensureLocation( loc.toFile() );
		
		File dest = getFile(loc.toFile(), target);
		OutputStream out = new FileOutputStream(dest);
		InputStream in = content;
		
		byte[] buf = new byte[1024];
		int c = 0;
		
		try{
			while((c=in.read(buf))>-1)
				out.write(buf, 0, c);
		}finally{
				out.close();
		}
	}
	
	/*  */
	private File getFile(File parent, String target) {
		
		while(target.contains("*")){
			String tmp = target;
			while(tmp.contains("*")) {
				int p = tmp.lastIndexOf('*');
				
				byte[] bytes = new byte[4];
				
				Random r = new Random();
				r.nextBytes(bytes);
				String s = Base64.encodeBase64URLSafeString( bytes );
				
				tmp = tmp.substring(0, p) + s + tmp.substring(p+1);
			}//while
			
			File f = new File(parent, tmp);
			if( ! f.exists() )
				return f; //exit
		}//while
		
		return new File(parent, target);
	}
	
	/*  */
	private void ensureLocation(File loc) {
		
		//ensure the location
		if( ! loc.exists() ){
			loc.mkdirs();
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
	 * @return the fileLoc
	 */
	public FileLocationService getFileLocation() {
		return fileLoc;
	}


	/**
	 * @param fileLoc the fileLoc to set
	 */
	public void setFileLocation(FileLocationService fileLoc) {
		this.fileLoc = fileLoc;
	}

}
