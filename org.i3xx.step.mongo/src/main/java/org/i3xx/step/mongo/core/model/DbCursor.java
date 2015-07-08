package org.i3xx.step.mongo.core.model;

/*
 * #%L
 * NordApp OfficeBase :: mongo
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



public interface DbCursor {

	
	/**
	 * Gets the number of elements iterated.
	 * 
	 * @return The number of elements
	 */
	public int numSeen();
	
	/**
	 * Gets the first element of the cursor.
	 * 
	 * @return The element as Object
	 */
	public DbObject one();
	
	/**
	 * Gets the current element of the cursor.
	 * 
	 * @return The current element as Object
	 */
	public DbObject curr();
	
	/**
	 * Returns true if the cursor has one more element.
	 * 
	 * @return The flag
	 */
	public boolean hasNext();
	
	/**
	 * Gets the next element of the cursor.
	 * 
	 * @return The element as Object
	 */
	public DbObject next();
}
