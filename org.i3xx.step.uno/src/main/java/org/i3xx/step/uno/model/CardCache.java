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


import java.util.List;

public interface CardCache {

	/**
	 * Adds a card to the cache
	 * 
	 * @param card
	 */
	void addCard(StepCard card);
	
	/**
	 * Add all cards from the list to the cache
	 * 
	 * @param cards
	 */
	void addCards(List<StepCard> cards);
	
	/**
	 * The size of the cache
	 * 
	 * @return
	 */
	int size();
	
	/**
	 * Gets a card from the cache
	 * 
	 * @param index
	 * @return
	 */
	StepCard getCard(int index);
	
	/**
	 * Get all cards from the cache
	 * 
	 * @return
	 */
	List<StepCard> getCards();

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
}
