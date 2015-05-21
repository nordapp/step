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
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.i3xx.step.uno.impl.OSGiServiceBridgeImpl;
import org.i3xx.step.uno.impl.daemon.EngineBase;
import org.i3xx.step.uno.model.OSGiServiceBridge;
import org.i3xx.step.uno.model.daemon.Engine;
import org.i3xx.step.uno.model.service.EngineBaseService;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.util.basic.io.FilePath;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Each mandator has one EngineBaseService available with it's main id. This
 * service contains a list of all available bundles (scripts) deployed to this
 * mandator. No script that is not deployed can be executed. This is a general
 * security mechanism.
 * 
 * To run a script or a workflow step it is necessary to get an Engine. The
 * EngineBase creates a store-transaction that contains the general and the
 * specific workflow steps for the particular engine.
 * 
 * The engine is built from this store and can run the workflow steps defined
 * at the store. It is possible to run a single command named by it's symbolic
 * name or the whole workflow. In the first case there is an engine for each
 * session, in the second case there is an engine for each workflow.
 * 
 * [Mandator] -(1:1)-> [EngineBaseService] -(1:1)-> [EngineBase]
 * [EngineBase] -(1:n)-> [Store-Transaction] -(1:n)-> [Engine]
 * 
 * 1. Case [Engine].call(SYMBOLIC-NAME)
 * 2. Case [Engine].start()
 * 
 * @author Stefan
 *
 */
public class EngineBaseServiceImpl implements EngineBaseService {
	
	static Logger logger = LoggerFactory.getLogger(EngineBaseService.class);
			
	/** the engine base of the mandator */
	private EngineBase base;
	
	/** The root path of the bundles */
	private FilePath bundleLoc;
	
	/** The list of available bundles */
	private Map<String, BigInteger> bundles;
	
	/** The map of engines */
	private Map<BigInteger, Engine> engines;
	
	/** The id of the mandator */
	private String mandatorId;
	
	/** The bundle context */
	private BundleContext bundleContext;
	
	public EngineBaseServiceImpl(FilePath resLoc) throws IOException {
		bundleLoc = resLoc.add("step").add("bin/bundle");
		if( bundleLoc.toFile().isFile() )
			throw new IllegalStateException("The location '"+bundleLoc+"' denotes a file.");
		if( ! bundleLoc.toFile().exists() )
			bundleLoc.toFile().mkdirs();
		
		logger.info("Start engine base path:='{}'", bundleLoc);
		mandatorId = null;
		bundleContext = null;
		
		base = new EngineBase( resLoc.add("step") );
		base.init();
		
		reloadBundles();
		
		engines = new LinkedHashMap<BigInteger, Engine>();
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	public void reloadBundles() throws IOException {
		
		//add the bundles to the bundle repository
		bundles = new HashMap<String, BigInteger>();
		//search groupId and artifactId
		for( File gf : bundleLoc.toFile().listFiles() ){
			if( ! gf.isDirectory())
				continue;
			
			for( File af : gf.listFiles() ){
				if( ! af.isDirectory())
					continue;
				
				BigInteger i = base.addBundle(gf.getName(), af.getName());
				bundles.put( gf.getName()+"/"+af.getName(), i );
				logger.info("Pick bundle id:={} group:='{}' artifact:='{}'", i, gf.getName(), af.getName());
			}//for
		}//for
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.uno.model.service.EngineBaseService#setupStore(java.math.BigInteger)
	 */
	public void setupStore(BigInteger key) throws IOException {
		
		//cleanup
		base.clean(key);
		
		//load defaults
		base.load(key);
		
		//load bundles
		for(String b : this.bundles.keySet()){
			BigInteger bundle = this.bundles.get(b);
			if(bundle==null)
				continue;
			
			base.load(key, bundle);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.uno.model.service.EngineBaseService#setupStore(java.math.BigInteger, java.util.List)
	 */
	public void setupStore(BigInteger key, List<String> bundles) throws IOException {
		
		//cleanup
		base.clean(key);
		
		//load defaults
		base.load(key);
		
		Set<BigInteger> list = new LinkedHashSet<BigInteger>();
		
		//load bundles
		for(String b : bundles){
			BigInteger bundle = this.bundles.get(b);
			if(bundle==null) {
				//starts-with
				if(b.endsWith("/*")){
					b = b.substring(0, b.length()-2);
					for(Map.Entry<String, BigInteger> e : this.bundles.entrySet()) {
						if(e.getKey().startsWith(b))
							list.add(e.getValue());
					}//for
				}
				//regexp-match
				else if(b.startsWith("/")) {
					b = b.substring(1);
					Pattern p = Pattern.compile(b);
					for(Map.Entry<String, BigInteger> e : this.bundles.entrySet()) {
						if(p.matcher(e.getKey()).matches())
							list.add(e.getValue());
					}//for
				}//fi
				//continue;
			}
			else{
				//base.load(key, bundle);
				list.add(bundle);
			}//fi
		}//for
		
		for(BigInteger bundle : list) {
			base.load(key, bundle);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.uno.model.service.EngineBaseService#getBundles()
	 */
	public List<String> getBundles() {
		List<String> list = new ArrayList<String>();
		list.addAll(bundles.keySet());
		
		return list;
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.uno.model.service.EngineBaseService#addEngine()
	 */
	public Engine addEngine() throws Exception {
		
		UUID ui = UUID.randomUUID();
		BigInteger id = BigInteger
				.valueOf(ui.getMostSignificantBits())
				.shiftLeft(64)
				.or(BigInteger.valueOf(ui.getLeastSignificantBits()));
		
		OSGiServiceBridge bridge = new OSGiServiceBridgeImpl();
		((OSGiServiceBridgeImpl) bridge).setBundleContext(bundleContext);
		((OSGiServiceBridgeImpl) bridge).setMandatorId(mandatorId);
		
		// Usage: loadExecutables(), init() OR load()
		//
		Engine impl = base.createEngine(id);
		impl.setServiceBridge(bridge);
		impl.init();
		
		engines.put(id, impl);
		
		return impl;
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.uno.model.service.EngineBaseService#getEngine(java.math.BigInteger)
	 */
	public Engine getEngine(BigInteger id) throws Exception {
		//returns existing engine
		if(engines.keySet().contains(id))
			return engines.get(id);
		
		OSGiServiceBridge bridge = new OSGiServiceBridgeImpl();
		((OSGiServiceBridgeImpl) bridge).setBundleContext(bundleContext);
		((OSGiServiceBridgeImpl) bridge).setMandatorId(mandatorId);
		
		// Usage: loadExecutables(), init() OR load()
		//
		boolean f = base.exists(id);
		Engine impl = base.createEngine(id);
		impl.setServiceBridge(bridge);
		
		if(f){
			impl.load();
		}else{
			impl.init();
		}
		//load or loadExecutables, init
		//impl.load();
		
		engines.put(id, impl);
		
		return impl;
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.uno.model.service.EngineBaseService#queryEngine(java.math.BigInteger, java.util.Map)
	 */
	public Engine queryEngine(BigInteger id, Map<String, Object> params) throws Exception {
		
		Engine engine = null;
		
		if( params.containsKey(IS_AVAILABLE) ){
			if( ((Boolean)params.get(IS_AVAILABLE)).booleanValue() ){
				engine = engines.get(id);
			}else{
				engine = getEngine(id);
			}
		}
		
		if( engine!=null && params.containsKey(IS_LOGIN) ){
			if( ((Boolean)params.get(IS_LOGIN)).booleanValue() ){
				if( ! engine.isLogin() )
					engine = null;
			}
		}
		
		return engine;
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.uno.model.service.EngineBaseService#dropEngine(java.math.BigInteger)
	 */
	public void dropEngine(BigInteger id) throws Exception {
		//returns existing engine
		if(engines.keySet().contains(id)) {
			engines.remove(id);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.uno.model.service.EngineBaseService#removeEngine(java.math.BigInteger)
	 */
	public void removeEngine(BigInteger id) throws Exception {
		//returns existing engine
		if(engines.keySet().contains(id)) {
			engines.remove(id);
		}
		base.removeEngine(id);
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.uno.model.service.EngineBaseService#initEngineBase()
	 */
	public void initEngineBase() throws Exception {
		base.init();
		
		reloadBundles();
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.uno.model.service.EngineBaseService#killEngineBase()
	 */
	public void killEngineBase() throws Exception {
		engines.clear();
		bundles.clear();
		base.killRemainingData();
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.uno.model.service.EngineBaseService#saveEngine(java.math.BigInteger)
	 */
	public void saveEngine(BigInteger id) throws Exception {
		base.save(id);
	}
	
	/**
	 * @return the mandatorId
	 */
	public String getMandatorId() {
		return mandatorId;
	}

	/**
	 * @param mandatorId the mandatorId to set
	 */
	public void setMandatorId(String mandatorId) {
		this.mandatorId = mandatorId;
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
	 * Returns the engine base
	 * 
	 * @param context The bundle context
	 * @param mandatorId The id of the mandator
	 * @param engineBaseId The id of the engine base
	 * @return
	 * @throws InvalidSyntaxException
	 */
	public static EngineBaseService getService(BundleContext context, String mandatorId, String engineBaseId) throws InvalidSyntaxException {
		
		ServiceReference<?>[] refs = context.getServiceReferences(EngineBaseService.class.getName(),
				"(&("+Mandator.MANDATORID+"="+mandatorId+")"+
						"("+EngineBaseService.ENGINE_BASE_ID+"="+engineBaseId+"))");
		
		if(refs==null)
			return null;
		
		return (EngineBaseService)context.getService(refs[0]);
	}
}
