package org.i3xx.step.uno.impl.daemon;

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


import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.i3xx.step.uno.impl.CardCacheImpl;
import org.i3xx.step.uno.impl.ContextImpl;
import org.i3xx.step.uno.impl.ImAndExport;
import org.i3xx.step.uno.impl.ScriptCacheImpl;
import org.i3xx.step.uno.impl.ScriptEngine;
import org.i3xx.step.uno.impl.ScriptRuntime;
import org.i3xx.step.uno.impl.SequencerImpl;
import org.i3xx.step.uno.impl.service.builtin.ContextAdministrationService;
import org.i3xx.step.uno.impl.service.builtin.ContextPropertiesService;
import org.i3xx.step.uno.model.CardCache;
import org.i3xx.step.uno.model.OSGiServiceBridge;
import org.i3xx.step.uno.model.ScriptCache;
import org.i3xx.step.uno.model.StepContext;
import org.i3xx.step.uno.model.daemon.Engine;
import org.i3xx.step.uno.model.policy.StepPolicy;
import org.i3xx.util.basic.io.FilePath;
import org.i3xx.util.store.Store;
import org.i3xx.util.store.StoreEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The engine is the runtime environment of a single workflow and contains the access
 * to all resources.
 * 
 * @author Stefan
 *
 */
public class EngineImpl implements Engine {
	
	static Logger logger = LoggerFactory.getLogger(EngineImpl.class);
	
	protected static final BigInteger SCRIPT_CACHE = BigInteger.valueOf(5);
	protected static final BigInteger SEQUENCER_DATA = BigInteger.valueOf(6);
	protected static final BigInteger CONTEXT_DATA = BigInteger.valueOf(7);
	
	private FilePath location;
	
	/** The master script engine */
	private ScriptEngine stepEngine;
	
	/** The master context */
	private StepContext stepContext;
	
	/** The sequencer */
	private SequencerImpl sequencer;
	
	/** 
	 * The binary store
	 * 
	 * The engine serializes it's data to this store to proceed it's work later
	 * after an interrupt. To proceed the data is read into the script engine
	 * (java script), the sequencer (manifest)and the context (context).
	 */
	private Store binStore;
	
	/** The script cache */
	private ScriptCache binCache;
	
	/** The store for temporary data */
	private Store tempStore;
	
	/** The store for executables */
	private Store execStore;
	
	/** The store for cards */
	private Store cardStore;
	
	/** The cache for the cards */
	private CardCache cardCache;
	
	/** The store for persistent data */
	private Store dataStore;
	
	/** The OSGi service bridge */
	private OSGiServiceBridge serviceBridge;
	
	/**
	 * The compression Flag of the script cache
	 */
	private boolean fBinComp;
	
	/**
	 * The primary GUID of the engine.
	 */
	private BigInteger engineId;
	
	/**
	 * The continue flag
	 */
	private volatile boolean fContinue;
	
	/**
	 * The continue flag
	 */
	private volatile boolean fLogin;
	
	/**
	 * Local processing data (not serializable, not persistent)
	 */
	private Map<String, Object> localValues;
	
	/**
	 * The engine executes the workflow.
	 * 
	 * @param location The workspace location
	 * @param engineId The id of this engine
	 */
	public EngineImpl(FilePath location, BigInteger engineId) {
		this.stepContext = null;
		this.sequencer =  null;
		this.binStore = null;
		this.binCache = null;
		this.fBinComp = false;
		
		this.tempStore = null;
		this.execStore = null;
		this.cardStore = null;
		this.cardCache = null;
		this.dataStore = null;
		
		this.setServiceBridge(null);
		this.localValues = new HashMap<String, Object>();
		
		this.engineId = engineId;
		this.location = location;
		
		this.fContinue = false;
		this.fLogin = false;
	}
	
	/**
	 * Load the scripts into the script context. The loaded scripts
	 * are cached.
	 * 
	 * the executable scripts.
	 * 
	 * @throws IOException
	 */
	public void loadExecutables() throws IOException {
		//
		// Load the executables
		//
		fBinComp = true;
		binCache = new ScriptCacheImpl(fBinComp);
		binCache.load(execStore, engineId);
	}
	
	/**
	 * Load the scripts into the script context. The loaded scripts
	 * are cached.
	 * 
	 * @param key The key of the executable store to load
	 * the executable scripts.
	 * 
	 * @throws IOException
	 */
	public void loadExecutables(BigInteger key) throws IOException {
		
		if(binCache==null)
			throw new IllegalStateException("Load the private files before using the utilities (binCache=null).");
		
		//
		// Load the executables
		//
		binCache.load(execStore, key);
	}
	
	/**
	 * Initializes the engine. The script cache and context
	 * must be initialized before.
	 * 
	 * Workflow: loadExecutables(<id>), init()
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		//
		// The workflow context
		//
		ContextImpl cx = new ContextImpl();
		cx.setServiceBridge(serviceBridge);
		cx.setLocalValues(localValues);
		cx.initializeBuiltinServices();
		cx.lock();
		
		stepContext = cx;
		
		//
		// The sequencer
		//
		sequencer = new SequencerImpl();
		for(BigInteger id : cardStore.getStoreListing(engineId)) {
			try{
				String manifest = cardStore.readString(engineId, id);
				sequencer.addManifest(manifest);
			}catch(Exception e){
				logger.debug("Add the manifest {}", id);
				logger.warn("Adding the manifest fails.", e);
			}
		}
		
		//
		// Handle import and export (TODO:)
		//
		ImAndExport im = new ImAndExport(location);
		im.analyzeCards(sequencer);
		ScriptCache cache = new ScriptCacheImpl(fBinComp);
		im.load(sequencer, cache);
		binCache.append(cache);
		
		//writes the card content to the cache
		cardCache = new CardCacheImpl();
		sequencer.toCache(cardCache);
		
		//
		//
		//
		stepEngine = new ScriptEngine(cx);
		stepEngine.init();
		stepEngine.load(binCache);
		stepEngine.seal();
	}
	
	/**
	 * Cleanup the engine and exit (TODO: complete)
	 */
	public void exit() {
		fLogin = false;
		
		//
		// The workflow context
		//
		ContextImpl cx = (ContextImpl)stepContext;
		cx.exit();
		
		//
		// The sequencer
		//
		sequencer.clear();
		
		//
		//
		//
		stepEngine.exit();
		
		localValues.clear();
	}
	
	/**
	 * Starts the sequence
	 */
	public void start() {
		fContinue = true;
		
		ScriptRuntime rt = stepEngine.fork().runtime();
		while( sequencer.hasNext() && fContinue ){
			String f = sequencer.next().getFunction();
			//A card with import/export but without a function call is valid.
			//Skip such a card.
			if(f.equals(""))
				continue;
			
			rt.exec(f, new Object[]{});
			
			//Interrupt if 'system.runtime.interrupt = true'
			if( StepPolicy.isInterrupted(stepContext) ) {
				systemPersist();
				
				break;
			}//fi
		}//while
	}
	
	/**
	 * Runs a single step
	 * 
	 * @param symbolicName
	 */
	public void call(String symbolicName) {
		int i = sequencer.find(symbolicName);
		if(i<0)
			throw new NoSuchElementException("The symbolic name "+symbolicName+" is not available.");
		
		ScriptRuntime rt = stepEngine.fork().runtime();
		rt.exec(sequencer.get(i).getFunction(), new Object[]{});
	}
	
	/**
	 * Interrupts the sequence.
	 * 
	 * Usually this is called by a step of this workflow (script)
	 */
	public void interrupt() {
		
		fContinue = false;
	}
	
	/**
	 * Resumes the interrupted sequence
	 * 
	 * Usually this is called by another workflow.
	 */
	public void resume() {
		if(fContinue==false){
			start();
			fContinue = true;
		}
	}
	
	/**
	 * @return True if the sequencer has a next step. Otherwise
	 * (re-)initialize the engine to continue.
	 */
	public boolean hasNext() {
		return (sequencer!= null && sequencer.hasNext());
	}
	
	/**
	 * Reinitializes the engine to a clean state.
	 * 
	 * @throws Exception 
	 */
	public void reinit() throws Exception {
		
		//
		// Reset the values
		//
		//TODO
		ContextAdministrationService srv = (ContextAdministrationService)
				stepContext.getService("org.i3xx.step.uno.ContextAdministrationService");
		srv.clearValues();
		
		//
		// Reset the context
		//
		((ContextImpl)stepContext).reinitializeBuiltinServices();
		
		//
		// Reset the sequencer
		//
		sequencer = new SequencerImpl();
		if(cardCache!=null) {
			sequencer.fromCache(cardCache);
		}
		
		//this should not happen
		//else if(cardCache == null) {
		//	for(BigInteger id : cardStore.getStoreListing(engineId)) {
		//		try{
		//			String manifest = cardStore.readString(engineId, id);
		//			sequencer.addManifest(manifest);
		//		}catch(Exception e){
		//			logger.debug("Add the manifest {}", id);
		//			logger.warn("Adding the manifest fails.", e);
		//		}
		//	}//for
		//}//fi
		
		//
		// Reset the step engine
		//
		// The step engine is sealed and needs no cleanup
	}
	
	/**
	 * Load the content from the binStore and restore the engine
	 * with the data stored before.
	 * @throws Exception 
	 */
	public void load() throws Exception {
		//
		// Load the executables
		//
		fBinComp = true;
		binCache = new ScriptCacheImpl(fBinComp);
		
		//read available binaries
		if( binStore.existStoreEntry(engineId, SCRIPT_CACHE) ) {
			String json = binStore.readString(engineId, SCRIPT_CACHE);
			binCache.fromJSON(json);
		}
		
		//
		// The workflow context
		//
		ContextImpl cx = new ContextImpl();
		cx.setServiceBridge(serviceBridge);
		cx.setLocalValues(localValues);
		cx.initializeBuiltinServices();
		cx.lock();
		
		stepContext = cx;
		
		//
		// The sequencer
		//
		sequencer = new SequencerImpl();
		if( binStore.existStoreEntry(engineId, SEQUENCER_DATA) ) {
			String json = binStore.readString(engineId, SEQUENCER_DATA);
			sequencer.fromJSON(json);
			
			cardCache = new CardCacheImpl();
			sequencer.toCache(cardCache);
		}
		
		//this should not happen
		//else if(cardCache != null){
		//	sequencer.fromJSON(cardCache);
		//}
		
		//
		//
		//
		stepEngine = new ScriptEngine(cx);		
		stepEngine.init();
		stepEngine.load(binCache);
		stepEngine.seal();
		
		// *** UNABLE TO RESOLVE SCRIPTABLE DATA WITHOUT A SCOPE ***
		
		//
		// The context data
		//
		if( binStore.existStoreEntry(engineId, CONTEXT_DATA) ) {
			String json = binStore.readString(engineId, CONTEXT_DATA);
			ContextAdministrationService srv = (ContextAdministrationService)
					cx.getService("org.i3xx.step.uno.ContextAdministrationService");
			srv.fromJSON(json);
		}
	}
	
	/**
	 * Saves the content of the engine to the binStore.
	 * 
	 * @throws IOException 
	 */
	public void save() throws Exception {
		//stores the script cache
		String json = binCache.toJSON();
		binStore.writeString(json, createEntry(SCRIPT_CACHE));
		
		//stores the sequencer data
		json = sequencer.toJSON();
		binStore.writeString(json, createEntry(SEQUENCER_DATA));
		
		//stores the context data
		ContextAdministrationService srv = (ContextAdministrationService)
				stepContext.getService("org.i3xx.step.uno.ContextAdministrationService");
		json = srv.toJSON();
		binStore.writeString(json, createEntry(CONTEXT_DATA));
		
		//save the store
		binStore.unloadData(engineId);
	}
	
	public void addScript() {
		//TODO
	}

	public void removeScript() {
		//TODO
	}

	/**
	 * @return the binStore
	 */
	public Store getBinStore() {
		return binStore;
	}

	/**
	 * @param binStore the binStore to set
	 */
	public void setBinStore(Store binStore) {
		this.binStore = binStore;
	}

	/**
	 * @return the tempStore
	 */
	public Store getTempStore() {
		return tempStore;
	}

	/**
	 * @param tempStore the tempStore to set
	 */
	public void setTempStore(Store tempStore) {
		this.tempStore = tempStore;
	}

	/**
	 * @return the execStore
	 */
	public Store getExecStore() {
		return execStore;
	}

	/**
	 * @param execStore the execStore to set
	 */
	public void setExecStore(Store execStore) {
		this.execStore = execStore;
	}

	/**
	 * @return the cardStore
	 */
	public Store getCardStore() {
		return cardStore;
	}

	/**
	 * @param cardStore the cardStore to set
	 */
	public void setCardStore(Store cardStore) {
		this.cardStore = cardStore;
	}

	/**
	 * @return the dataStore
	 */
	public Store getDataStore() {
		return dataStore;
	}

	/**
	 * @param dataStore the dataStore to set
	 */
	public void setDataStore(Store dataStore) {
		this.dataStore = dataStore;
	}
	
	/**
	 * Gets an array containing the keys of the local values
	 * 
	 * @return The String array
	 */
	public String[] getLocalKeys() {
		return localValues.keySet().toArray(new String[localValues.size()]);
	}
	
	/**
	 * gets a local value
	 * 
	 * @return The value
	 */
	public Object getLocalValue(String key) {
		return localValues.get(key);
	}

	/**
	 * Sets a local value
	 * 
	 * @param key The key
	 * @param value The value
	 */
	public void setLocalValue(String key, Object value) {
		localValues.put(key, value);
	}

	/**
	 * @return the serviceBridge
	 */
	public OSGiServiceBridge getServiceBridge() {
		return serviceBridge;
	}

	/**
	 * @param serviceBridge the serviceBridge to set
	 */
	public void setServiceBridge(OSGiServiceBridge serviceBridge) {
		this.serviceBridge = serviceBridge;
	}

	/**
	 * @return
	 */
	public boolean isLogin() {
		return fLogin;
	}
	
	/**
	 * @param login
	 */
	public void setLogin(boolean login) {
		this.fLogin = login;
	}
	
	/**
	 * Creates a store entry to store data persistent.
	 * 
	 * @param id The id of the store entry
	 * @return
	 */
	private StoreEntry createEntry(BigInteger id) {
		
		StoreEntry entry = new StoreEntry();
		entry.setTrans(engineId);
		entry.setId(id);
		entry.setLifetime(Long.MAX_VALUE);
		entry.setPersistent(true);
		
		return entry;
	}

	/**
	 * Get the names of the values in the context as a String array.
	 * 
	 * @return The String array containing names of the the values.
	 */
	public String[] getNames() {
		return stepContext.getNames();
	}
	
	/**
	 * Gets a value from the stepContext.
	 * 
	 * @param key The key of the value in the context
	 * @return The value
	 */
	public Object getValue(String key) {
		return stepContext.getValue(key);
	}
	
	/**
	 * Sets a value to the step context
	 * 
	 * @param key The key of the value in the context
	 * @param value The value to set
	 */
	public void setValue(String key, Object value) {
		stepContext.setValue(key, value);
	}
	
	/**
	 * Sets the values of the step context from the JSON statement
	 * 
	 * @param json The JSON statement
	 * @throws Exception
	 */
	public void fromJSON(String json) throws Exception {
		ContextPropertiesService adm = (ContextPropertiesService)
				stepContext.getService("org.i3xx.step.uno.ContextPropertiesService");
		
		adm.fromJSON(json);
	}
	
	/**
	 * Gets the values of the step context as JSON data.
	 * 
	 * @param names The names of the property to print out to the JSON
	 * 		or null for all properties.
	 * @return The JSON
	 * @throws Exception
	 */
	public String toJSON(String[] names) throws Exception {
		ContextPropertiesService adm = (ContextPropertiesService)
				stepContext.getService("org.i3xx.step.uno.ContextPropertiesService");
		
		return adm.toJSON(names);
	}
	
	/**
	 * Persists the system state
	 */
	private void systemPersist() {
		if(StepPolicy.isPersistent(stepContext)) {
			try{
				save();
			}catch(Exception e){
				logger.error("Saving the state fails.", e);
			}
		}//fi
	}
	
	//TODO: remove after test
	public StepContext getContext() {
		return stepContext;
	}
	
	//TODO: remove after test
	protected void setContext(StepContext context) {
		stepContext = context;
	}
}
