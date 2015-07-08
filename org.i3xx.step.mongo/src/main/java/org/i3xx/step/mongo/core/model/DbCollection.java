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


import java.util.List;



public interface DbCollection {

	/**
	 * Gets a cursor on all elements in the collection
	 * 
	 * @return The database cursor
	 */
	DbCursor getCursor();
	
	/**
	 * Gets a cursor on the result of the query
	 * 
	 * @param jsonQuery
	 * @return The database cursor
	 */
	DbCursor getCursor(String jsonQuery);
	
	/**
	 * Gets the element matching the id
	 * 
	 * @param id
	 * @return The element as Object
	 */
	DbObject findOneFromId(String id);
	
	/**
	 * Gets the first element matching the query
	 * 
	 * @param jsonQuery
	 * @return The element as Object
	 */
	DbObject findOne(String jsonQuery);
	
	/**
	 * Gets the first element matching the query
	 * 
	 * @param jsonQuery
	 * @param jsonFields
	 * @return The element as Object
	 */
	DbObject findOne(String jsonQuery, String jsonFields);
	
	/**
	 * Gets the number of elements ion this query
	 * 
	 * @return The number of elements
	 */
	long count();
	
	/**
	 * Gets the info about the indexes of the collection. Each object
	 * in the list is the "info document" from MongoDB.
	 * 
	 * @return The information as a list of Objects
	 */
	List<DbObject> getIndexInfo();
	
	/**
	 * Creates an index
	 * 
	 * @param field The field name to create the index.
	 */
	void createIndex(String field);
	
	/**
	 * Forces creation of an index on a set of fields, if one does not already exist.
	 * 
	 * @param jsonKeys a document that contains pairs with the name of the field or fields to index and order of the index
	 * @param jsonOption a document that controls the creation of the index.
	 */
	void createIndex(String jsonKeys, String jsonOption);
	
	/**
	 * Drops an index
	 * 
	 * @param indexName The field name to drop the index.
	 */
	void dropIndex(String indexName);

	/**
	 * Inserts the data
	 * 
	 * @param jsonData The data to insert
	 */
	void insert(String jsonData);
	
	/**
	 * Removes the data
	 * 
	 * @param jsonQuery The query to select the data to be removed
	 */
	void remove(String jsonQuery);
	
	/**
	 * Updates data
	 * 
	 * @param jsonQuery The query to select the data to be updated
	 * @param jsonData The data to update
	 */
	void update(String jsonQuery, String jsonData);
	
	/**
	 * Updates data
	 * 
	 * @param jsonQuery The query to select the data to be updated
	 * @param jsonData The data to update
	 * @param upsert when true, inserts a document if no
	 * document matches the update query criteria
	 * @param multi when true, updates all documents in
	 * the collection that match the update query criteria, otherwise only updates one
	 */
	void update(String jsonQuery, String jsonData, boolean upsert, boolean multi);
	
	/**
	 * Destroys the collection
	 */
	void drop();
}
