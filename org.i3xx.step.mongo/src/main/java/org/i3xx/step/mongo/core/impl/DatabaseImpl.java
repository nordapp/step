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


import org.i3xx.step.mongo.core.model.DbCollection;
import org.i3xx.step.mongo.core.model.DbDatabase;
import org.i3xx.step.mongo.core.model.DbFileStore;
import org.i3xx.step.mongo.core.util.IdGen;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class DatabaseImpl implements DbDatabase {

	private DB db;
	
	/**
	 * @param mongo The mongodb database client
	 * @param dbname
	 */
	public DatabaseImpl(MongoClient mongo, String dbname) {
		db = mongo.getDB(dbname);
	}
	
	public DatabaseImpl() {
		db = null;
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbDatabase#createCollection(java.lang.String, java.lang.String)
	 */
	public DbCollection createCollection(String colname, String optionsString) {
		DBObject options = (DBObject)JSON.parse(optionsString);
		return new CollectionImpl( db.createCollection(colname, options));
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbDatabase#getCollection(java.lang.String)
	 */
	public DbCollection getCollection(String colname) {
		return new CollectionImpl( db.getCollection(colname));
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbDatabase#getFileStore()
	 */
	public DbFileStore getFileStore() {
		DbFileStore fs = new FileStoreImpl(db);
		return fs;
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbDatabase#getFileStore(java.lang.String)
	 */
	public DbFileStore getFileStore(String bucket) {
		DbFileStore fs = new FileStoreImpl(db, bucket);
		return fs;
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbDatabase#getId(int)
	 */
	public Object getId(int type) {
		if(type==UUID_AS_STRING)
			return IdGen.getUUID().toString();
		else if(type==UUID_AS_BIGINT)
			return IdGen.getUUID().toBigInteger();
		else if(type==UUID_AS_URLSAVESTRING)
			return IdGen.getURLSafeString( IdGen.getUUID() );
		else
			throw new IllegalArgumentException("The type "+type+" is not allowed.");
	}

}
