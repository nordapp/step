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


import java.net.UnknownHostException;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.util.JSON;

public class MongoDb002Test {

	@Before
	public void setUp() throws Exception {
	}

	@Ignore
	public void testA() throws UnknownHostException {
		
		MongoClient mongo = new MongoClient("localhost", 27017);
		DB dbs = mongo.getDB("dbVDV");
		DBCollection col = dbs.getCollection("docs");
		
		DBObject db2 = col.findOne( new BasicDBObject("id", "629c4177-b3ee-495c-871a-da49843d5f26") );
		
		String ref = (String)db2.get("ref");
		System.out.println(ref);
		
		GridFS fs = new GridFS(dbs);
		
		ObjectId dbo = new ObjectId(ref);
		GridFSDBFile f = fs.find(dbo);
		
		System.out.println( f.getId() );
		System.out.println( f.getFilename() );
		System.out.println( f.getContentType() );
		System.out.println( f.getLength() );
		System.out.println( f.toString() );
		System.out.println( f.getMetaData() );
		
		//fail("Not yet implemented"); // TODO
	}

	@Ignore
	public void testB() throws UnknownHostException {
		
		MongoClient mongo = new MongoClient("localhost", 27017);
		DB dbs = mongo.getDB("dbVDV");
		GridFS fs = new GridFS(dbs);
		
		String query = "{ _id: {$exists: true} }";
		DBObject dbo = (DBObject)JSON.parse(query);
		List<GridFSDBFile> list = fs.find(dbo);
		
		for(GridFSDBFile f : list) {
			System.out.println( f.getId() );
			System.out.println( f.getFilename() );
			System.out.println( f.getContentType() );
			System.out.println( f.getLength() );
			System.out.println( f.toString() );
			System.out.println( f.getMetaData() );
		}
		
		System.out.println( list.size() );
		
		//fail("Not yet implemented"); // TODO
	}

}
