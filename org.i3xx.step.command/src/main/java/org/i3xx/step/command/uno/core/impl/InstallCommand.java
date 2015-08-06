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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.i3xx.step.zero.service.impl.mandator.MandatorServiceImpl;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.util.basic.io.FilePath;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Command(scope = "ob", name = "uno-install", description="Installs the content of a script bundle from the bundle cache.")
public class InstallCommand extends OsgiCommandSupport {
	
	static Logger logger = LoggerFactory.getLogger(InstallCommand.class);
	
    @Argument(index = 0, name = "mandatorId", description = "The id of the mandator", required = true, multiValued = false)
    private String mandatorId = null;
	
    @Argument(index = 1, name = "bundleId", description = "The id of the bundle to install", required = true, multiValued = false)
    private String bundleId = null;

	/**
	 * 
	 */
	public InstallCommand() {
	}

	@Override
	protected Object doExecute() throws Exception {
		
        logger.info("Uno-Install mandator-id: '{}', bundleId: '{}'", mandatorId, bundleId);
        
        //gets the service
        BundleContext context = getBundleContext();
        
        //gets the bundle location
        long bid = Long.parseLong(bundleId);
        Bundle bundle = context.getBundle(bid);
        Enumeration<URL> en = bundle.findEntries("/", "*.*", true);
		
		//
		// Search the mandator
		//
		Mandator mandator = MandatorServiceImpl.getMandator(context, mandatorId);
		
		//
		// Run
		//
		if(mandator!=null) {
			// get the id of the mandator
			//String mandatorId = mandator.getId();
			
			FilePath resLoc = FilePath.get(mandator.getPath()).add("data");
			
			//ensureLocation( resLoc.add("page").add("/file/bundle").toFile() );
			//ensureLocation( resLoc.add("step").add("/bin/bundle").toFile() );
			
			while(en.hasMoreElements()) {
				URL url = en.nextElement();
				InputStream in = url.openConnection().getInputStream();
				
				String path = url.getPath();
				int p = 0;
				if(path.contains("site")) {
					logger.debug("Copy the resource '{}' from the bundle {} of the mandator {}", path, bundleId, mandatorId);
					p = path.indexOf("site");
					// p + 'site' + File.separatorChar
					path = path.substring(p+5);
				}else{
					logger.debug("Skip the resource '{}' from the bundle {} of the mandator {} (Reason 'site' is missing in the path)", path, bundleId, mandatorId);
					continue;
				}
				
				File file = resLoc.add(path).toFile();
				ensureLocation(file.getParentFile());
				
				OutputStream out = new FileOutputStream( file );
				
				try{
					int c = 0;
					byte[] buf = new byte[1024];
					
					while((c=in.read(buf))>-1){
						out.write(buf, 0, c);
					}
				}finally{
					try{
						in.close();
					}finally{
						out.close();
					}
				}
			}//while
		}//fi
		
		return null;
	}

	/*  */
	/**
	 * @param loc
	 */
	private void ensureLocation(File loc) {
		
		//ensure the location
		if( ! loc.exists() ){
			loc.mkdirs();
		}
	}

}
