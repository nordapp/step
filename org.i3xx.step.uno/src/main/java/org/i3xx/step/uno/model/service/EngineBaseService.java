package org.i3xx.step.uno.model.service;

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
import java.util.List;
import java.util.Map;

import org.i3xx.step.uno.model.daemon.Engine;
import org.osgi.framework.BundleContext;

public interface EngineBaseService {
	
	/**
	 * 
	 */
	public static final String ENGINE_BASE_ID = "engineBaseId";
	
	/**
	 * Flag to query an available engine
	 */
	public static final String IS_AVAILABLE = "isAvailable";
	
	/**
	 * Flag to query an engine with a valid login
	 */
	public static final String IS_LOGIN = "isLogin";
	
	/**
	 * Load all bundles to the store
	 * 
	 * @param key The store key is the engine id
	 * @throws IOException
	 */
	void setupStore(BigInteger key) throws IOException;	
	
	/**
	 * Loads the bundles to the store.
	 * 
	 * Each bundle is named by the String: group-id '/' artifact-id.
	 * The entries of the list matches the name by equality or a matching of
	 * the first patterns or a regular expression.
	 * 
	 * If both, group-id and artifact-id are valid names than there is an
	 * equality match. If the artifact-id is '*' the match is a starts-with
	 * group-id match (removes the '/*' at the end and matches startsWith).
	 * If the group-id is empty the match is done by a regular
	 * expression built from the artifact-id (removes the '/' at the beginning
	 * and uses the Pattern.compile command)).
	 * 
	 * Examples:
	 * 
	 * group-1/artifact-1, group2/artifact2,
	 * group-1/*, group/*,
	 * /^group-\d*\/artifact-\d*$
	 * 
	 * @param key The store key is the engine id
	 * @param bundles The list of the bundles to load
	 * @throws IOException
	 */
	void setupStore(BigInteger key, List<String> bundles) throws IOException;
	
	/**
	 * Reload the bundles after an configuration update
	 * 
	 * @throws IOException 
	 */
	public void reloadBundles() throws IOException;
	
	/**
	 * Gets a list of all available bundles
	 * 
	 * @return The list of bundles
	 */
	List<String> getBundles();
	
	/**
	 * Creates an engine with a random engine id
	 *  
	 * @return The engine
	 * @throws Exception
	 */
	Engine addEngine() throws Exception;
	
	/**
	 * Creates a new engine or returns the existing engine with the specified id.
	 * 
	 * @param id The engine id
	 * @return The engine
	 * @throws Exception
	 */
	Engine getEngine(BigInteger id) throws Exception;
	
	/**
	 * <p>Creates a new engine or returns the existing engine with the specified id
	 * and if the parameter doesn't match null.</p>
	 * 
	 * <p>If the IS_AVAILABLE parameter is set to true, only an engine is returned
	 * if one is available. If the flag is set to false and there is no engine a
	 * new engine will be created.</p>
	 * 
	 * <p>If the IS_LOGIN parameter is set to true, only an engine with a valid
	 * login can be returned.</p>
	 * 
	 * @param id The id of the engine
	 * @param params The parameter to query the engine
	 * @return The engine or null if no engine can be found or created.
	 * @throws Exception
	 */
	Engine queryEngine(BigInteger id, Map<String, Object> params) throws Exception;	
	
	/**
	 * Restarts the engine base (after a kill)
	 * 
	 * @throws Exception
	 */
	void initEngineBase() throws Exception;	
	
	/**
	 * Removes an engine from the engine collection and keep the resources valid.
	 * 
	 * @param id The engine id
	 * @throws Exception
	 */
	void dropEngine(BigInteger id) throws Exception;
	
	/**
	 * Removes an engine from the engine collection and destroys the resources.
	 * 
	 * @param id The engine id
	 * @throws Exception
	 */
	void removeEngine(BigInteger id) throws Exception;
	
	/**
	 * Destroys all data of an engine base. (Needs a restart of the service)
	 * 
	 * @throws Exception
	 */
	void killEngineBase() throws Exception;
	
	/**
	 * Saves the state of the engine (not the content)
	 * 
	 * @param id The engine id
	 * @throws Exception
	 */
	void saveEngine(BigInteger id) throws Exception;
	
	/**
	 * @return the mandatorId
	 */
	String getMandatorId();

	/**
	 * @param mandatorId the mandatorId to set
	 */
	void setMandatorId(String mandatorId);
	
	/**
	 * @return the bundleContext
	 */
	BundleContext getBundleContext();

	/**
	 * @param bundleContext the bundleContext to set
	 */
	void setBundleContext(BundleContext bundleContext);
}
