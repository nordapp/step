package org.i3xx.step.zero.service.impl.mandator;

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


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Dictionary;
import java.util.Hashtable;

import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.step.zero.service.model.mandator.MandatorService;
import org.i3xx.step.zero.service.model.mandator.RootService;
import org.i3xx.util.basic.io.FilePath;
import org.i3xx.util.basic.platform.Platform;
import org.i3xx.util.general.setup.impl.Setup;
import org.i3xx.util.general.setup.model.SetupService;
import org.i3xx.util.platform.impl.AvailableKeys;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RootServiceImpl implements RootService {
	
	private Logger logger = LoggerFactory.getLogger(RootServiceImpl.class);
	
	/** The osgi bundle context */
	private BundleContext bundleContext;

	public RootServiceImpl() {
		bundleContext = null;
	}
	
	public void startUp() {
		//does nothing but logging
		logger.info("Root service start up.");
	}
	
	public void createMandator(String mandatorId) throws IOException, InvalidSyntaxException, ConfigurationException {
        
        ServiceReference<SetupService> srA = bundleContext.getServiceReference(SetupService.class);
        SetupService setupService = bundleContext.getService(srA);
        
        ServiceReference<MandatorService> srB = bundleContext.getServiceReference(MandatorService.class);
        MandatorService mandatorService = bundleContext.getService(srB);
        
        ServiceReference<ConfigurationAdmin> srC = bundleContext.getServiceReference(ConfigurationAdmin.class);
        ConfigurationAdmin configAdmin = bundleContext.getService(srC);
        
        ServiceReference<Platform> srD = bundleContext.getServiceReference(Platform.class);
        Platform platformService = bundleContext.getService(srD);
        
        Configuration config = configAdmin.createFactoryConfiguration(MandatorServiceImpl.mandatorConfigName);
        String pid = config.getPid();
        Dictionary<String, Object> props = config.getProperties();
        if(props==null){
        	props = new Hashtable<String, Object>();
            props.put(Constants.SERVICE_PID, pid);
        	props.put("mandator.id", mandatorId);
        	props.put("mandator.start", "true");
        	config.update(props);
        }
        
        //Delegate to mandator service because this is called by the ConfigurationAdmin
        //if the file is edit manually or by the configuration page.
    	((MandatorServiceImpl)mandatorService).updated(pid, props);
    	
    	Mandator m = mandatorService.getMandator(mandatorId);
        
        //initialize the mandator
        if( ! m.isInitialized()){
        	
        	Setup setup = setupService.getGeneralSetup();
    		
    		logger.info("Setup defaults title='{}' id='{}' root='{}'.", setup.getTitle(), setup.getId(), setup.getRoot());
        	
    		String _root = Setup.setCurrentId(setup.getRoot(), mandatorId);
    		String _id = Setup.setCurrentId(setup.getId(), mandatorId);
    		String _title = Setup.setCurrentId(setup.getTitle(), mandatorId);
    		
    		String path = _root.replace('/', File.separatorChar);
    		if(path.startsWith(".")){
    			String home = (String)platformService.getObject(AvailableKeys.SERVER_HOME);
    			path = home + File.separator + path.substring(1);
    		}
    		logger.info("Create mandator root {}.", path);
    		
    		//
    		File root = createRootDirectory(path);
    		String _path = root.getAbsolutePath();
    		
    		m.setId(_id);
    		m.setRoot(_root);
    		m.setPath(_path);
    		m.setTitle(_title);
    		m.setInitialized(true);
    		
    		Configuration configuration = configAdmin.getConfiguration(m.getServicePid());
    		Dictionary<String, Object> dict = configuration.getProperties();
    		
    		m.save(dict);
    		
    		configuration.update(dict);
    		
    		logger.info("Setup a new mandator title='{}' id='{}' path='{}'.", _title, _id, _path);
    		
    		// do some other stuff here
        }
	}

	public void startMandator(String mandatorId) throws IOException, InvalidSyntaxException, ConfigurationException {
		
		ServiceReference<ConfigurationAdmin> cref = bundleContext.getServiceReference(ConfigurationAdmin.class);
		ConfigurationAdmin configurationAdmin = bundleContext.getService(cref);
		Configuration configuration = null;
		
		Configuration[] c = configurationAdmin.listConfigurations(
				"(&(service.factoryPid="+MandatorServiceImpl.mandatorConfigName+")(mandator.id="+mandatorId+"))");
		
		configuration = c.length==0 ? null : c[0];
		if(configuration==null) {
			System.out.println("ERROR: The mandator '"+mandatorId+"' is not available.");
		}
		String servicePid = configuration.getPid();
		
		//String[] clazzes = new String[]{MandatorService.class.getName()};
        ServiceReference<MandatorService> mref = bundleContext.getServiceReference(MandatorService.class);
        MandatorService msrv = bundleContext.getService(mref);
        
        Mandator mandator = msrv.getMandator(mandatorId);
        if(mandator==null){
        	logger.info("The mandator '{}' is not available on this system.", mandatorId);
        	
        	//creates the mandator if the service pid is available
    		if(configuration!=null) {
    			Dictionary<String, Object> props = configuration.getProperties();
    			props.put("mandator.start", "true"); //update sets the flag
    			configuration.update(props);
    			
    			((ManagedServiceFactory)msrv).updated(servicePid, props);
    		}//fi
        }//fi
}

	public void stopMandator(String mandatorId) throws IOException, InvalidSyntaxException, ConfigurationException {
		
        //String[] clazzes = new String[]{MandatorService.class.getName()};
        ServiceReference<MandatorService> mref = bundleContext.getServiceReference(MandatorService.class);
        MandatorService msrv = bundleContext.getService(mref);
        
        Mandator mandator = msrv.getMandator(mandatorId);
        if(mandator==null){
        	logger.info("The mandator '{}' is not available on this system.", mandatorId);
        }
		
        mandator.setStarted(false);
        save(mandator);
        
        //doesn't delete the service reference - Fixed: use stop
        //boolean resl = bundleContext.ungetService( mandator.getServiceRef() );
        ((MandatorServiceImpl)msrv).stop(mandator.getId());
	}
	
	public void destroyMandator(String servicePid) throws IOException, InvalidSyntaxException, ConfigurationException {
		
		ServiceReference<ConfigurationAdmin> cref = bundleContext.getServiceReference(ConfigurationAdmin.class);
		ConfigurationAdmin configurationAdmin = bundleContext.getService(cref);
		Configuration configuration = configurationAdmin.getConfiguration(servicePid);
		
		if(configuration==null){
        	logger.info("The mandator '{}' is not available on this system (no config).", servicePid);
        	return;
		}
        Dictionary<String, Object> dict = configuration.getProperties();
		if(dict.get("mandator.id")==null){
        	logger.info("The mandator '{}' is not available on this system (no mandator.id).", servicePid);
        	return;
		}
		
        ServiceReference<SetupService> srA = bundleContext.getServiceReference(SetupService.class);
        SetupService setupService = bundleContext.getService(srA);
        
        ServiceReference<MandatorService> srB = bundleContext.getServiceReference(MandatorService.class);
        MandatorService mandatorService = bundleContext.getService(srB);
        
        ServiceReference<Platform> srD = bundleContext.getServiceReference(Platform.class);
        Platform platformService = bundleContext.getService(srD);
		
        String mandatorId = (String)dict.get("mandator.id");
    	Mandator mandator = mandatorService.getMandator(mandatorId);
    	if(mandator==null && dict!=null) {
    		mandator = new MandatorImpl();
    		((MandatorImpl)mandator).updated(dict);
    	}
		String path = mandator.getPath();
		//String mandatorId = mandator.getId();
		
		configuration.delete();
		
		if(path!=null) {
	    	Setup setup = setupService.getGeneralSetup();
			String root = Setup.setCurrentId(setup.getRoot(), mandatorId);
			
			FilePath fp = null;
			if(root.startsWith(".")) {
				String home = (String)platformService.getObject(AvailableKeys.SERVER_HOME);
				fp = FilePath.append(home, root.substring(1));
			}else{
				fp = FilePath.get(root);
			}
			
			FilePath mp = FilePath.get(path);
			if(mp.toString().contains(fp.toString())){
				cleanDir(mp.toFile());
			}else{
				logger.warn("The path '{}' doesn't match the path '{}'.", fp.toString(), mp.toString());
			}
		}else{
			logger.warn("The mandator '{}' was not configured (nothing cleared).", mandatorId);
		}
	}
	
	/**
	 * @param mandator
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 */
	private void save(Mandator mandator) throws IOException, InvalidSyntaxException {
		
		ServiceReference<ConfigurationAdmin> cref = bundleContext.getServiceReference(ConfigurationAdmin.class);
		ConfigurationAdmin configurationAdmin = bundleContext.getService(cref);
		Configuration configuration = null;
		
		Configuration[] c = configurationAdmin.listConfigurations(
				"(&(service.factoryPid="+MandatorServiceImpl.mandatorConfigName+")(mandator.id="+mandator.getId()+"))");
		
		configuration = c.length==0 ? null : c[0];
		if(configuration==null) {
			System.out.println("ERROR: The mandator '"+mandator.getId()+"' is not available.");
		}
    	
		if(configuration!=null) {
			Dictionary<String, Object> config = configuration.getProperties();
			mandator.save(config);
			
			configuration.update(config);
		}
	}

	public void listMandator() {
		
        //String[] clazzes = new String[]{MandatorService.class.getName()};
        ServiceReference<MandatorService> mref = bundleContext.getServiceReference(MandatorService.class);
        MandatorService msrv = bundleContext.getService(mref);
        
        for(String mandatorId : msrv.getMandatorList()) {
        	
        	Mandator mandator = msrv.getMandator(mandatorId); 
            
            StringBuffer buffer = new StringBuffer();
        	buffer.append("factory-pid: ");
            buffer.append( mandator.getFactoryPid() );
        	buffer.append(", service-pid: ");
            buffer.append( mandator.getServicePid() );
        	buffer.append(", id: ");
            buffer.append( mandator.getId() );
        	buffer.append(", title: ");
            buffer.append( mandator.getTitle() );
        	buffer.append(", path: ");
            buffer.append( mandator.getPath() );
        	buffer.append(", root: ");
            buffer.append( mandator.getRoot() );
            for(String key : mandator.getPropertyKeys()){
            	buffer.append(", ");
            	buffer.append(key);
            	buffer.append(":");
            	buffer.append( mandator.getProperty(key) );
            }//for
            
        	logger.info("The mandator '{}' has been started.", mandatorId);
        	logger.debug( buffer.toString() );
        	
        }//for
	}
	
	public void listConfiguration() throws IOException, InvalidSyntaxException {
        
        ServiceReference<ConfigurationAdmin> srC = bundleContext.getServiceReference(ConfigurationAdmin.class);
        ConfigurationAdmin configAdmin = bundleContext.getService(srC);
		
		Configuration[] c = configAdmin.listConfigurations(
				"(service.factoryPid="+MandatorServiceImpl.mandatorConfigName+")");
		
		if(c==null || c.length==0) {
			System.out.println("ERROR: There is no mandator available (configure one).");
		}else{
			
			for(int i=0;i<c.length;i++) {
				
				String servicePid = c[i].getPid();
				Dictionary<String, Object> props = c[i].getProperties();
				Object id = props==null ? "undefined" : props.get("mandator.id");
				
				logger.info("Mandator id:{} pid:{}", id, servicePid);
			}//for
		}//fi
		
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
	
	/* Creates the root directory */
	private File createRootDirectory(String path) throws IOException {
		
		File root = new File(path);
		if( ! root.exists())
			root.mkdirs();
		
		// The configuration directory
		File etc = new File(root, "etc");
		etc.mkdir();
		
		// The deploy and modules directory
		File dep = new File(root, "deploy");
		dep.mkdir();
		
		// The working directory
		File tmp = new File(root, "temp");
		tmp.mkdir();
		
		// The persistence directory
		File dat = new File(root, "data");
		dat.mkdir();
		
		return root;
	}
	
	/**
	 * Cleans the directory
	 * 
	 * @param dir
	 */
	private void cleanDir(File file) {
		
		//recursion
		if(file.isDirectory()){
			for(File f : file.listFiles()) {
				cleanDir(f);
			}//for
		}//fi
		
		//cleanup
		file.delete();
		
		//kill it - if possible
		if(file.exists()) {
			try{
				//set the length to o
				RandomAccessFile raf = new RandomAccessFile(file, "rws");
				try{
					raf.setLength(0);
				}finally{
					raf.close();
				}
			}catch(IOException e){}
			
			file.deleteOnExit();
		}//fi
	}

}
