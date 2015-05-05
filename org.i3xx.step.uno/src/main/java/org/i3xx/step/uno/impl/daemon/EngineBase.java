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


import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.i3xx.step.uno.impl.ScriptLoader;
import org.i3xx.step.uno.impl.util.GsonHashMapDeserializer;
import org.i3xx.step.uno.model.daemon.Engine;
import org.i3xx.util.basic.io.FilePath;
import org.i3xx.util.store.Store;
import org.i3xx.util.store.StoreEntry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The engine base is the admin of the script bundles. Each script bundle stores it's data
 * in the bundle store execStore (trans=0)
 * 
 * The engine loads the known bundles when initialize has started. To add a new bundle
 * do this by add(group-id, artifact-id) and use the returned id as argument at load(id).
 * First add, then load.
 * 
 * The engine base uses the directory /work as it's work directory
 * 
 * There are some stores
 * /work/tmp       The temporary data
 * /work/bin/js    The added java scripts (deploy any generated script here)
 * /work/bin/card  The sequencer cards
 * /work/bin/cache The binary content of the script cache
 * /work/db        The work database
 * 
 * The stores to place the scripts
 * /bin/main/js    The scripts of the shared scope
 * /bin/${group-id}/${artifact-id} The bundle source
 * 
 * The scripts and manifests are updated when load(BigInteger) is called.
 * 
 * The execution order is determined by the transaction and the order. The priority belongs to
 * the thread execution and doesn't turn the position in the queue.
 * 
 * The manifest defines the execution of a script. Here is an example:
 * 
 * Manifest-Version: 1.0
 * Card-GroupId: group-1
 * Card-ArtifactId: artifact-1
 * Card-Version: 1.0.0
 * Card-SymbolicName: group-1.artifact-1.Step-1
 * Card-Name: Step-1
 * Card-Function: i3xx.group-1.artifact-1.activate
 * Card-Multiple:
 * Card-Priority: 5
 * Card-Transaction: 0
 * Card-Order: 0
 * 
 * Card-Multiple is not yet implemented.
 * 
 * @author Stefan
 *
 */
public class EngineBase {
	
	private static final BigInteger BUNDLE_STORE = BigInteger.ZERO;
	
	/** The base location */
	private FilePath location;
		
	/** The store for temporary data */
	private Store tempStore;
	
	/** The store for executables */
	private Store execStore;
	
	/** The store for cards */
	private Store cardStore;
	
	/** The store for persistent data */
	private Store dataStore;
	
	/** The store for intern binary content */
	private Store binStore;
	
	private Map<BigInteger, String[]> bundles;
	
	/**
	 * @param location The base directory
	 */
	public EngineBase(FilePath location) {
		this.tempStore = null;
		this.execStore = null;
		this.cardStore = null;
		this.dataStore = null;
		this.binStore = null;
		
		this.location = location;
		this.bundles = new HashMap<BigInteger, String[]>();
	}
	
	/**
	 * Initializes the engine base
	 * 
	 * @throws IOException
	 */
	public void init() throws IOException {
		tempStore = new Store( location.add("work").add("tmp").getPath() );
		execStore = new Store( location.add("work").add("bin").add("js").getPath() );
		cardStore = new Store( location.add("work").add("bin").add("card").getPath() );
		dataStore = new Store( location.add("work").add("db").getPath() );
		
		//intern data
		binStore = new Store( location.add("work").add("bin").add("cache").getPath() );
		binStore.loadData();
		
		//load bundle store
		execStore.loadData();
		if( ! execStore.existStore( BUNDLE_STORE ) ){
			execStore.createStore( BUNDLE_STORE );
		}
		cardStore.loadData();
		if( ! cardStore.existStore( BUNDLE_STORE ) ){
			cardStore.createStore( BUNDLE_STORE );
		}
		for(BigInteger id : execStore.getStoreListing( BUNDLE_STORE )) {
			String data = execStore.readString(BUNDLE_STORE, id);
			Map<String, String> props = fromJSON(data);
			String[] ids = new String[]{ props.get("group-id"), props.get("artifact-id") };
			bundles.put(id, ids);
		}
		
		tempStore.loadData();
		
		dataStore.loadData();
	}
	
	/**
	 * Saves the state of the specified engine
	 * 
	 * @param id The id of the engine
	 * 
	 * @throws IOException
	 */
	public void save(BigInteger id) throws IOException {
		
		if( dataStore.existStore(id)){
			dataStore.unloadData(id);
		}
		if( execStore.existStore(id)){
			execStore.unloadData(id);
		}
		if( cardStore.existStore(id)){
			cardStore.unloadData(id);
		}
		if( tempStore.existStore(id)){
			tempStore.unloadData(id);
		}
		if( binStore.existStore(id)){
			binStore.unloadData(id);
		}
	}
	
	/**
	 * Saves the state of the engine base (all engines)
	 * 
	 * @throws IOException
	 */
	public void saveAll() throws IOException {
		if(tempStore!=null){
			tempStore.gc();
			tempStore.unloadData();
		}
		if(execStore!=null){
			execStore.gc();
			execStore.unloadData();
		}
		if(cardStore!=null){
			cardStore.gc();
			cardStore.unloadData();
		}
		if(dataStore!=null){
			dataStore.gc();
			dataStore.unloadData();
		}
		
		if(binStore!=null){
			binStore.gc();
			binStore.unloadData();
		}
	}
	
	/**
	 * Note: Each engine has it's own working directory. It's /store/{id}, do not forget
	 * to load the script and manifest data of the base.
	 * 
	 * @param id The id of the engine (each engine has it's own id)
	 * @return
	 * @throws IOException
	 */
	public Engine createEngine(BigInteger id) throws IOException {
		
		if(id.equals(BUNDLE_STORE))
			throw new IllegalArgumentException("The engine '0' is reserved. Use another id.");
		
		//
		//ensure existing stores
		//
		if( ! dataStore.existStore(id))
			dataStore.createStore(id);
		if( ! execStore.existStore(id))
			execStore.createStore(id);
		if( ! cardStore.existStore(id))
			cardStore.createStore(id);
		if( ! tempStore.existStore(id))
			tempStore.createStore(id);
		if( ! binStore.existStore(id))
			binStore.createStore(id);
		
		//
		//create the engine
		//
		EngineImpl engine = new EngineImpl(location, id);
		engine.setDataStore(dataStore);
		engine.setExecStore(execStore);
		engine.setCardStore(cardStore);
		engine.setTempStore(tempStore);
		
		engine.setBinStore(binStore);
		engine.loadExecutables();
		
		return engine;
	}
	
	/**
	 * Returns true if an engine exists
	 * 
	 * @param id The id of the engine (each engine has it's own id)
	 * @return
	 * @throws IOException 
	 */
	public boolean exists(BigInteger id) throws IOException {
		return binStore.existStore(id) && 
				binStore.existStoreEntry(id, EngineImpl.SCRIPT_CACHE) && 
				binStore.existStoreEntry(id, EngineImpl.CONTEXT_DATA) && 
				binStore.existStoreEntry(id, EngineImpl.SEQUENCER_DATA);
	}
	
	/**
	 * Cleans the store
	 * 
	 * @param id The id of the engine (each engine has it's own id)
	 * @throws IOException
	 */
	public void removeEngine(BigInteger id) throws IOException {
		
		if( dataStore.existStore(id)){
			dataStore.cleanupStore(id);
		}
		if( execStore.existStore(id)){
			execStore.cleanupStore(id);
		}
		if( cardStore.existStore(id)){
			cardStore.cleanupStore(id);
		}
		if( tempStore.existStore(id)){
			tempStore.cleanupStore(id);
		}
		if( binStore.existStore(id)){
			binStore.cleanupStore(id);
		}
	}
	
	/**
	 * Cleanup all stores and kill any kind of data
	 * 
	 * @throws IOException
	 */
	public void killRemainingData() throws IOException {
		
		if( dataStore!=null){
			dataStore.cleanup();
		}
		if( execStore!=null){
			execStore.cleanup();
		}
		if( cardStore!=null){
			cardStore.cleanup();
		}
		if( tempStore!=null){
			tempStore.cleanup();
		}
		if( binStore!=null){
			binStore.cleanup();
		}
		bundles.clear();
	}
	
	/**
	 * Adds a bundle to the engine base. The bundles are stored at the execStore-(trans=0)
	 * 
	 * @param groupId The group-id of the bundle
	 * @param artifactId The artifact-id of the bundle
	 * @return The bundle id The id of the bundle
	 * @throws IOException 
	 */
	public BigInteger addBundle(String groupId, String artifactId) throws IOException {
		
		for(BigInteger bi : bundles.keySet()) {
			String[] bundle = bundles.get(bi);
			if(bundle[0]!=null && bundle[1]!=null && 
					bundle[1].equals(artifactId) &&
					bundle[0].equals(groupId)) {
				
				return bi;
			}
		}
		
		StoreEntry entry = execStore.nextEntry(BUNDLE_STORE, 0, true, null);
		entry.setSort(entry.getId());
		entry.setLifetime(Long.MAX_VALUE);
		
		Map<String, String> props = new HashMap<String, String>();
		props.put("group-id", groupId);
		props.put("artifact-id", artifactId);
		execStore.writeString(toJSON(props), entry);
		//store the data
		execStore.unloadData(BUNDLE_STORE);
		
		bundles.put(entry.getId(), new String[]{groupId, artifactId});
		
		return entry.getId();
	}
	
	/**
	 * Removes a bundle
	 * 
	 * @param groupId The group-id of the bundle
	 * @param artifactId The artifact-id of the bundle
	 */
	public void removeBundle(String groupId, String artifactId) throws IOException {
		//TODO removes a bundle
		
		BigInteger key = null;
		
		//search the bundle id (key)
		for(BigInteger bi : bundles.keySet()) {
			String[] bundle = bundles.get(bi);
			if(bundle[0]!=null && bundle[1]!=null && 
					bundle[1].equals(artifactId) &&
					bundle[0].equals(groupId)) {
				
				key = bi;
				break;
			}//fi
		}//for
		
		try{
			execStore.cleanupStoreEntry(BUNDLE_STORE, key);
			//store the data
			execStore.unloadData(BUNDLE_STORE);
		}finally{
			bundles.remove(key);
		}
	}
	
	/**
	 * Clears the execution store before loading
	 * 
	 * @param key The key of the execution store
	 * @throws IOException
	 */
	public void clean(BigInteger key) throws IOException {
		if(execStore.existStore(key)){
			execStore.cleanupStore(key);
		}//fi
		if(cardStore.existStore(key)){
			cardStore.cleanupStore(key);
		}//fi
	}
	
	/**
	 * The loader has the following load policy.
	 * 
	 * The start directory is ${base}/bin/main/js. The files of this directory and
	 * it's subdirectories are loaded to each engine.
	 * 
	 * @param key The key of the execution store
	 * @throws IOException
	 */
	public void load(BigInteger key) throws IOException {
		
		if( ! execStore.existStore(key) )
			execStore.createStore(key);
		if( ! cardStore.existStore(key) )
			cardStore.createStore(key);
		
		File dir = location.add("bin/main/js").toFile();
		
		ScriptLoader loader = new ScriptLoader();
		loader.crawler(dir, execStore, key, "js");
		loader.crawler(dir, cardStore, key, "mf");
		
		//save the data
		execStore.unloadData(key);
		cardStore.unloadData(key);
	}
	
	/**
	 * The loader has the following load policy.
	 * 
	 * The start directory is ${base}/bin/bundle/${group-id}/${artifact-id}/js. The files of this directory and
	 * it's subdirectories are loaded to the bundle engine.
	 * 
	 * @param bundle The key of the bundle
	 * @param key The key of the execution store
	 * @throws IOException
	 * @throws NoSuchElementException If the key is not associated with a bundle.
	 */
	public void load(BigInteger key, BigInteger bundle) throws IOException {
		
		String[] val = bundles.get(bundle);
		if(val==null)
			throw new NoSuchElementException("The script-artifact "+bundle+" is not present.");
		
		FilePath loc = location.add("bin/bundle").add(val[0]).add(val[1]);
		File dir = loc.toFile();
		if( ! dir.exists())
			throw new NoSuchElementException("The directory "+dir.getAbsolutePath()+" doesn't exist.");
		if( ! dir.isDirectory())
			throw new NoSuchElementException("The fie "+dir.getAbsolutePath()+" is not a directory.");
		
		//The location is /bin/bundle/group-id/artifact-id
		ScriptLoader loader = new ScriptLoader();
		loader.crawler(dir, execStore, key, "js");
		loader.crawler(dir, cardStore, key, "mf");
		
		//save the data
		execStore.unloadData(key);
		cardStore.unloadData(key);
	}
	
	/**
	 * @param json
	 * @return
	 */
	private Map<String, String> fromJSON(String json) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(LinkedHashMap.class, new GsonHashMapDeserializer());
		Gson gson = gsonBuilder.create();
		
		HashMap<?, ?> m = gson.fromJson(json, HashMap.class);
		
		HashMap<String, String> props = new HashMap<String, String>();
		for(Object k : m.keySet()) {
			Object v = m.get(k);
			try{
				props.put((String)k, (String)v);
			}catch(ClassCastException e){
				props.put(k.toString(), v.toString());
			}
		}//for
		
		return props;
	}
	
	/**
	 * @param props
	 * @return
	 */
	private String toJSON(Map<String, String> props) {
		Gson gson = new Gson();
		StringBuffer buf = new StringBuffer();
		
		buf.append( '{' );
		
		for(String k : props.keySet()) {
			String v = props.get(k);
			
			buf.append( gson.toJson(k) );
			buf.append( ':' );
			buf.append( gson.toJson(v) );
			buf.append( ',' );
		}
		//remove the last ','
		buf.setLength(buf.length()-1);
		
		buf.append( '}' );
		
		return buf.toString();
	}

}
