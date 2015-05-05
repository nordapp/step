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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.i3xx.step.uno.model.service.EngineBaseService;
import org.i3xx.step.zero.service.impl.mandator.MandatorServiceImpl;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.util.basic.io.FilePath;
import org.i3xx.util.symbol.service.model.SymbolService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Each time a mandator is added to the registry a corresponding
 * engine base is created.
 * 
 * @author Stefan
 *
 */
public class MandatorTrackerImpl extends ServiceTracker<Mandator, Mandator> {
	
	static Logger logger = LoggerFactory.getLogger(MandatorTrackerImpl.class);
	
	/**
	 * There is one service for each mandator defined
	 */
	private Map<String, List<ServiceRegistration<?>>> refList;
	
	/**
	 * The engineBaseService cannot be created because of a missing service.
	 */
	private List<String> manList;
	
	/** The private thread for the lookup */
	private Thread thread;
	
	public MandatorTrackerImpl(BundleContext bundleContext) {
		super(bundleContext, Mandator.class.getName(), null);
		
		refList = new HashMap<String, List<ServiceRegistration<?>>>();
		manList = new ArrayList<String>();
		thread = null;
	}
	
	public Mandator addingService(ServiceReference<Mandator> reference) {
		
		Mandator mandator = context.getService(reference);
		
		if( ! refList.containsKey(mandator.getId())) {
			refList.put(mandator.getId(), new ArrayList<ServiceRegistration<?>>());
		}
		
		try {
			if(mandator.isInitialized()) {
				createEngineBase(mandator);
			}//fi
		} catch (Exception e) {
			logger.warn("The mandator {} is not initialized properly.", mandator.getId(), e);
			e.printStackTrace();
		}
		
		return mandator;
	}
	
	/**
	 * @param mandator
	 * @throws Exception
	 */
	private void createEngineBase(Mandator mandator) throws Exception {
		
		logger.info("Initializes the EngineBase of title:{}, factory-pid:{}, service-pid:{}, "+
			"id:{}, path:{}, root:{}, initialized:{}",
				mandator.getTitle(), mandator.getFactoryPid(), mandator.getServicePid(),
				mandator.getId(), mandator.getPath(), mandator.getRoot(),
				mandator.isInitialized());
		
		FilePath resLoc = FilePath.get(mandator.getPath()).add("data");
		
		ensureLocation( resLoc.add("step").toFile() );
		ensureLocation( resLoc.add("step").add("/bin/main/js").toFile() );
		
		//
		// Ensure each mandator has it's own immutable base engineId
		//
		ServiceReference<?> symRef = context.getServiceReference(SymbolService.class.getName());
		if(symRef==null){
			logger.warn("The ServiceReference for '{}' cannot be created (missing reference to SymbolService)."
					, mandator.getId());
			
			manList.add(mandator.getId());
			lazyLoad();
			return;
		}
		SymbolService symbolService = (SymbolService)context.getService(symRef);
		if(symbolService==null){
			logger.warn("The SymbolService for '{}' cannot be created (SymbolService is not available)."
					, mandator.getId());
			return;
		}
		
		// get the id of the mandator
		String mandatorId = mandator.getId();
		// get the symbol from the symbol service
		int index = symbolService.getSymbol( mandatorId );
		
		BigInteger engineId = BigInteger.valueOf( index );
		
		//
		// Each engine runtime has it's own id
		//
		
		EngineBaseService base = new EngineBaseServiceImpl(resLoc);
		base.setMandatorId(mandatorId);
		base.setBundleContext(context);
		
		//TODO: generate hash and bind to shiro session
		BigInteger key = engineId;
		String sKey = key.toString();
		
		/*
		UUID ui = UUID.randomUUID();
		BigInteger id = BigInteger
				.valueOf(ui.getMostSignificantBits())
				.shiftLeft(64)
				.or(BigInteger.valueOf(ui.getLeastSignificantBits()));
		*/
		
		String[] clazzes = new String[]{EngineBaseService.class.getName()};
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(Mandator.MANDATORID, mandatorId);
		properties.put(EngineBaseService.ENGINE_BASE_ID, sKey);
		
		logger.debug("Register EngineBaseService '{}' of '{}'.", 
				sKey, mandatorId);
		
		List<ServiceRegistration<?>> reg = refList.get(mandator.getId());
		reg.add( context.registerService(clazzes, base, properties) );
	}
	
	
	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTracker#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	public void removedService(ServiceReference<Mandator> reference, Mandator mandator) {
		//Dictionary<String, Object> properties = new Hashtable<String, Object>();
		//properties.put(Mandator.MANDATORID, mandator.getId());
		//properties.put(EngineBaseService.ENGINE_BASE_ID, key.toString());
		
		try {
			ServiceReference<?>[] refs = context.getServiceReferences(EngineBaseService.class.getName(),
					"("+Mandator.MANDATORID+"="+mandator.getId()+")");
			
			if(refs!=null){
				for(ServiceReference<?> ref : refs){
					logger.debug("Unget EngineBaseService '{}' of '{}'.", 
							ref.getProperty(EngineBaseService.ENGINE_BASE_ID),
							ref.getProperty(Mandator.MANDATORID));
					
					//
					// Do not kill the engines because all data will be lost.
					// Use the cleanup command instead.
					//
					EngineBaseService srv = (EngineBaseService)context.getService(ref);
					try {
					//	srv.killEngine();
						BigInteger id = new BigInteger( (String)ref.getProperty(EngineBaseService.ENGINE_BASE_ID) );
						srv.dropEngine(id);
					} catch (Exception e) {
						//logger.debug("Exception while killing the engine.", e);
						logger.debug("Exception while stopping the engine.", e);
					}
					
					context.ungetService(ref);
				}//for
				
				List<ServiceRegistration<?>> list = refList.get(mandator.getId());
				for(ServiceRegistration<?> r : list){
					r.unregister();
				}
				list.clear();
			}
		} catch (InvalidSyntaxException e) {
			logger.error("Error ungetting service of mandator '"+mandator.getId()+"'.", e);
		}
	}

	/*  */
	private void ensureLocation(File loc) {
		
		//ensure the location
		if( ! loc.exists() ){
			loc.mkdirs();
		}
	}
	
	/*  */
	private void lazyLoad() {
		if(thread!=null || manList.size()==0){
			logger.info("Do not load mandator lazy (restart module).");
			return;
		}//
		
		thread = new Thread(new Runnable(){
			//
			public void run() {
				//Maximal wait for 300s
				for(int i=0;i<60 && manList.size()>0;i++){
					try{
						Thread.sleep(1000);
					}catch(InterruptedException e){}
					
					String id = manList.remove(0);
					try{
						Mandator m = MandatorServiceImpl.getMandator(context, id);
						createEngineBase(m);
					}catch(Exception e){
						logger.error("An error occurs loading the mandator lazy.", e);
					}
				}//for
				
				//remove me
				thread = null;
			}});
		
		thread.start();
	}

}
