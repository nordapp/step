package org.i3xx.step.zero.service.model.mandator;

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


import java.util.Dictionary;

public interface MandatorService {
	
	/**
	 * Get a list of all available mandator.
	 * 
	 * @return The mandator keys as an array
	 */
	String[] getMandatorList();
	
	/**
	 * Gets the mandator with the id
	 * 
	 * @param id The id of the mandator
	 * @return The mandator object
	 */
	Mandator getMandator(String id);
	
	/**
	 * Saves the data of the mandator with the id
	 * 
	 * @param id The id of the mandator
	 * @param config The configuration
	 * @return true if a value has been saved, false otherwise.
	 */
	boolean save(String id, Dictionary<String, Object> config);
}
