package org.i3xx.step.uno.model;

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
import java.security.NoSuchAlgorithmException;

import org.i3xx.util.store.Store;

public interface ScriptCache {
	
	/**
	 * @return The number of scripts in the buffer
	 */
	int size();
	
	/**
	 * @return The size of the buffer
	 */
	long sizeInBytes();
	
	/**
	 * Gets a digest to verify integrity
	 * 
	 * @return The digest
	 */
	String getDigest();
	
	/**
	 * Computes a MD5-Digest to verify integrity
	 * 
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 */
	void computeDigest() throws NoSuchAlgorithmException, IOException;
	
	/**
	 * Load scripts to the buffer
	 * 
	 * @param store
	 * @param key
	 * @throws IOException 
	 */
	void load(Store store, BigInteger key) throws IOException;
	
	/**
	 * Load scripts to the cache
	 * 
	 * @param files
	 * @param names
	 * @throws IOException
	 */
	public void load(File[] files, String[] names) throws IOException;
	
	/**
	 * Returns the name of an element of the buffer
	 * 
	 * @param index The index of the cached element
	 * @return
	 */
	String getName(int index);
	
	/**
	 * reads a script from the cache
	 * 
	 * @param index The index of the cached element
	 * @return
	 * @throws IOException
	 */
	String read(int index) throws IOException;
	
	/**
	 * Clears the cache
	 */
	void clear();
	
	/**
	 * Serializes the content to JSON
	 * 
	 * @return The JSON String
	 */
	String toJSON();
	
	/**
	 * Deserializes the content from JSON
	 * 
	 * @param json The JSON String
	 */
	void fromJSON(String json);
	
	/**
	 * Appends the caches
	 * 
	 * @param cache
	 */
	void append(ScriptCache cache);
	
}
