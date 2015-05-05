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


import java.util.Map;


public interface Sequencer {
	
	/**
	 * Reads the manifest into a property map.
	 * 
	 * @param manifest
	 * @return
	 */
	Map<String, String> readManifest(String manifest);
	
	/**
	 * Adds a step to the list from a manifest
	 * 
	 * @param manifest
	 * @return True if the card is put in front of the pointer, false otherwise.
	 */
	boolean addManifest(String manifest);
	
	/**
	 * Adds a step to the list
	 * 
	 * @param card
	 * @return True if the card is put in front of the pointer, false otherwise.
	 */
	boolean add(StepCard card);
	
	/**
	 * Removes a step from the list
	 * 
	 * @param card
	 * @return True if the card is removed in front of the pointer, false otherwise.
	 */
	boolean remove(StepCard card);
	
	//
	// Sequential access
	//
	
	/**
	 * Resets the pointer
	 */
	void reset();
	
	/**
	 * Clears the sequencer
	 */
	void clear();
	
	/**
	 * @return True if there is one more element to execute, false otherwise.
	 */
	boolean hasNext();
	
	/**
	 * Moves the pointer forwards and gets the step card at the new position
	 * of the pointer.
	 * 
	 * @return The next step card
	 */
	StepCard next();
	
	//
	// Random access
	//
	
	/**
	 * @return The size of the list
	 */
	int size();
	
	/**
	 * Gets the step card at the index without moving the pointer.
	 * 
	 * @param index The index
	 * @return
	 */
	StepCard get(int index);
	
	/**
	 * Returns the index of the card with the matching symbolic name
	 * or -1 if nothing can be found.
	 * 
	 * @param symbolicName The symbolic name
	 * @return
	 */
	int find(String symbolicName);
	
	//
	// Sort
	//
	
	/**
	 * Sorts the list (needs a reset of the pointer)
	 */
	void sort();
	
	//
	// Serialize facility
	//
	
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
	 * Saves the sequencer list to the card cache.
	 * 
	 * @param cache
	 */
	void toCache(CardCache cache);
	
	/**
	 * Loads the sequencer list from the card cache.
	 * 
	 * @param cache
	 */
	void fromCache(CardCache cache);
	
}
