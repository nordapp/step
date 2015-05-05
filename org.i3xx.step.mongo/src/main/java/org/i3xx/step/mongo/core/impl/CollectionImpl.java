package org.i3xx.step.mongo.core.impl;

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


import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.i3xx.step.mongo.core.model.DbCollection;
import org.i3xx.step.mongo.core.model.DbCursor;
import org.i3xx.step.mongo.core.model.DbObject;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class CollectionImpl implements DbCollection {

	private DBCollection col;
	
	public CollectionImpl(DBCollection col) {
		this.col = col;
	}
	
	public DbCursor getCursor() {
		return new CursorImpl( col.find() );
	}
	
	public DbCursor getCursor(String jsonQuery) {
		DBObject query = (DBObject) JSON.parse(jsonQuery);
		return new CursorImpl( col.find(query) );
	}
	
	public DbObject findOneFromId(String id) {
		DBObject dbo = col.findOne(new ObjectId(id));
		return dbo==null ? null : new ObjectImpl(dbo);
	}
	
	public DbObject findOne(String jsonQuery) {
		DBObject query = (DBObject) JSON.parse(jsonQuery);
		DBObject dbo = col.findOne(query);
		return dbo==null ? null : new ObjectImpl(dbo);
	}
	
	public DbObject findOne(String jsonQuery, String jsonFields) {
		DBObject query = (DBObject) JSON.parse(jsonQuery);
		DBObject dbo = col.findOne(query);
		return dbo==null ? null : new ObjectImpl(dbo);
	}
	
	public long count() {
		return col.count();
	}
	
	public List<DbObject> getIndexInfo() {
		List<DBObject> list = col.getIndexInfo();
		List<DbObject> resl = new ArrayList<DbObject>();
		
		for(DBObject dbo : list) {
			resl.add( new ObjectImpl(dbo) );
		}
		
		return resl;
	}
	
	public void createIndex(String field) {
		col.createIndex(field);
	}
	
	public void createIndex(String jsonKeys, String jsonOption) {
		DBObject keys = (DBObject) JSON.parse(jsonKeys);
		DBObject option = (DBObject) JSON.parse(jsonOption);
		col.createIndex(keys, option);
	}
	
	public void dropIndex(String indexName) {
		col.dropIndex(indexName);
	}
	
	public void insert(String jsonData) {
		DBObject data = (DBObject) JSON.parse(jsonData);
		col.insert(data);
	}
	
	public void remove(String jsonQuery) {
		DBObject query = (DBObject) JSON.parse(jsonQuery);
		col.remove(query);
	}
	
	public void update(String jsonQuery, String jsonData) {
		DBObject query = (DBObject) JSON.parse(jsonQuery);
		DBObject data = (DBObject) JSON.parse(jsonData);
		col.update(query, data);
	}
	
	public void update(String jsonQuery, String jsonData, boolean upsert, boolean multi) {
		DBObject query = (DBObject) JSON.parse(jsonQuery);
		DBObject data = (DBObject) JSON.parse(jsonData);
		col.update(query, data, upsert, multi);
	}
	
	public void drop() {
		col.drop();
	}

}
