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


public interface DbDatabase {
	
	/**
	 * The UUID is returned as an 128-bit integer
	 */
	public static final int UUID_AS_BIGINT = 0;
	
	/**
	 * The UUID is returned as it's String representation
	 * Example: df571790-997f-11e3-a398-000c292da3cc
	 */
	public static final int UUID_AS_STRING = 1;
	
	/**
	 * The UUID is base64 URL-save encoded
	 */
	public static final int UUID_AS_URLSAVESTRING = 2;
	
	/**
	 * @param colname The name of the collection to create
	 * @param optionsString The option string as JSON string or '{}' for default
	 * @return The database collection object
	 */
	DbCollection createCollection(String colname, String optionsString);
	
	/**
	 * Gets a collection of the database.
	 * 
	 * @param colname The name of the collection.
	 * @return The database collection object
	 */
	DbCollection getCollection(String colname);
	
	/**
	 * Gets a file store of the database.
	 * 
	 * @return The database file store
	 */
	DbFileStore getFileStore();
	
	/**
	 * Gets a file store of the database.
	 * 
	 * @param bucket The name of the bucket (optional namespace, may be null).
	 * @return The database file store
	 */
	DbFileStore getFileStore(String bucket);
	
	/**
	 * Generates an 128-bit UUID
	 * 
	 * @param type UUID_AS_BIGINT | UUID_AS_STRING | UUID_AS_URLSAVESTRING
	 * @return The id object
	 */
	Object getId(int type);
}
