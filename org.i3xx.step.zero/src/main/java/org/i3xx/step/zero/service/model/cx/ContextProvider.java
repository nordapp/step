package org.i3xx.step.zero.service.model.cx;

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


import java.util.Map;

/**
 * The context provider is a service that handles the access
 * to the context.
 * 
 * @author Stefan
 *
 */
public interface ContextProvider {

	/**
	 * Binds a key (session, mandator, etc) to a context.
	 * If there is no context available one is created.
	 * 
	 * @param properties The properties to match
	 * @return The context to bind to
	 */
	Context bind(Map<String, Object> properties);
	
	/**
	 * Returns the first context matching the properties
	 * 
	 * @param properties The properties to match
	 * @return The context to bind to
	 */
	Context find(Map<String, Object> properties);
	
	/**
	 * Get all matching context objects. If there is no result the array is empty.
	 * 
	 * @param properties
	 * @return A not null array of the context objects.
	 */
	Context[] findAll(Map<String, Object> properties);
	
	/**
	 * Frees a context from a binding. If the last binding
	 * is removed, the context is cleared and destroyed.
	 * 
	 * @param properties The properties to match
	 */
	void free(Map<String, Object> properties);
	
	/**
	 * Kill the matching contexts, every context is cleared and destroyed.
	 * 
	 * @param properties The properties to match
	 */
	void kill(Map<String, Object> properties);
}
