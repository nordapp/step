package org.i3xx.step.uno.model.daemon;

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

import org.i3xx.step.uno.model.OSGiServiceBridge;
import org.i3xx.util.store.Store;


public interface Engine {

	/**
	 * Load the scripts into the script context. The loaded scripts
	 * are cached.
	 * 
	 * @throws IOException
	 */
	void loadExecutables() throws IOException;
	
	/**
	 * Load the scripts into the script context. The loaded scripts
	 * are cached.
	 * 
	 * @param key The key of the executable store to load
	 * the executable (utility) scripts.
	 * 
	 * @throws IOException
	 */
	void loadExecutables(BigInteger key) throws IOException;
	
	/**
	 * Initializes the engine. The script cache and context
	 * must be initialized before.
	 * 
	 * Workflow: loadExecutables(<id>), init()
	 * 
	 * @throws Exception
	 */
	void init() throws Exception;
	
	/**
	 * Cleanup the engine and exit (TODO: complete)
	 */
	void exit();
	
	/**
	 * Starts the sequence
	 */
	void start();
	
	/**
	 * Runs a single step
	 * 
	 * @param symbolicName
	 */
	void call(String symbolicName);
	
	/**
	 * Interrupts the sequence.
	 * 
	 * Usually this is called by a step of this workflow (script)
	 */
	void interrupt();
	
	/**
	 * Resumes the interrupted sequence
	 * 
	 * Usually this is called by another workflow.
	 */
	void resume();
	
	
	/**
	 * Returns true if the sequencer has a next step. Otherwise
	 * (re-)initialize the engine to continue.
	 * 
	 * @return True if the sequencer has a next step, false otherwise.
	 */
	boolean hasNext();
	
	/**
	 * (Re-)Initializes the engine to run in a clear state. The state must be the same
	 * as after an init.
	 * 
	 * @throws Exception
	 */
	void reinit() throws Exception;
	
	/**
	 * Load the content from the binStore and restore the engine
	 * with the data stored before.
	 * @throws Exception 
	 */
	void load() throws Exception;
	
	/**
	 * Saves the content of the engine to the binStore.
	 * 
	 * @throws Exception 
	 */
	void save() throws Exception;
	
	void addScript();

	public void removeScript();

	/**
	 * @return the binStore
	 */
	Store getBinStore();

	/**
	 * @param binStore the binStore to set
	 */
	void setBinStore(Store binStore);

	/**
	 * @return the tempStore
	 */
	Store getTempStore();

	/**
	 * @param tempStore the tempStore to set
	 */
	void setTempStore(Store tempStore);

	/**
	 * @return the execStore
	 */
	Store getExecStore();

	/**
	 * @param execStore the execStore to set
	 */
	void setExecStore(Store execStore);

	/**
	 * @return the cardStore
	 */
	Store getCardStore();

	/**
	 * @param cardStore the cardStore to set
	 */
	void setCardStore(Store cardStore);

	/**
	 * @return the dataStore
	 */
	Store getDataStore();

	/**
	 * @param dataStore the dataStore to set
	 */
	void setDataStore(Store dataStore);
	
	/**
	 * gets a local value
	 * 
	 * @return The value
	 */
	Object getLocalValue(String key);

	/**
	 * Sets a local value
	 * 
	 * @param key The key
	 * @param value The value
	 */
	void setLocalValue(String key, Object value);

	/**
	 * @return the serviceBridge
	 */
	OSGiServiceBridge getServiceBridge();

	/**
	 * @param serviceBridge the serviceBridge to set
	 */
	void setServiceBridge(OSGiServiceBridge serviceBridge);
	
	/**
	 * @return
	 */
	boolean isLogin();
	
	/**
	 * @param login
	 */
	void setLogin(boolean login);

	/**
	 * Get the names of the values in the context as a String array.
	 * 
	 * @return The String array containing names of the the values.
	 */
	String[] getNames();
	
	/**
	 * Gets a value from the stepContext.
	 * 
	 * @param key The key of the value in the context
	 * @return The value
	 */
	Object getValue(String key);
	
	/**
	 * Sets a value to the step context
	 * 
	 * @param key The key of the value in the context
	 * @param value The value to set
	 */
	void setValue(String key, Object value);
	
	/**
	 * Sets the values of the step context from the JSON statement
	 * 
	 * @param json The JSON statement
	 * @throws Exception
	 */
	void fromJSON(String json) throws Exception;
	
	/**
	 * Gets the values of the step context as JSON data.
	 * 
	 * @param names The names of the property to print out to the JSON
	 * 		or null for all properties.
	 * @return The JSON
	 * @throws Exception
	 */
	String toJSON(String[] names) throws Exception;
	
}
