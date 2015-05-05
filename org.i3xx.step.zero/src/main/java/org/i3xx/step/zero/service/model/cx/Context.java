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


import java.math.BigInteger;

/**
 * A context provides attributes to one or more sessions
 * bound to it.
 * 
 * @author Stefan
 *
 */
public interface Context {
	
	/**
	 * The key of the context id
	 */
	public static final String CONTEXT_ID = "contextId";
	
	/**
	 * Gets the id of the context.
	 * 
	 * @return The id
	 */
	BigInteger getId();
	
	/**
	 * Increase the bind counter and return the current value.
	 * 
	 * @return
	 */
	int bind();
	
	/**
	 * Decrease the bind counter and return the current value.
	 * 
	 * @return
	 */
	int unbind();
	
	/**
	 * Get the names of the attributes provided by this context
	 * 
	 * @return The attribute names
	 */
	String[] getAttributeNames();
	
	/**
	 * Returns an attribute
	 * 
	 * @param name The name of the attribute
	 * @return The attribute
	 */
	Object getAttribute(String name);
	
	/**
	 * @param name The name of the attribute
	 * @param value The value of the attribute
	 */
	void setAttribute(String name, Object value);
	
	/**
	 * Removes an attribute and returns it
	 * 
	 * @param name The name of the attribute
	 * @return The attribute
	 */
	Object removeAttribute(String name);
	
	/**
	 * Clear all attributes
	 */
	void clearAttributes();
}
