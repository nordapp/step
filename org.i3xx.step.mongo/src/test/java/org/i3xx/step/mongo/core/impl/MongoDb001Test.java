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


import java.io.File;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.i3xx.step.mongo.core.model.DbCollection;
import org.i3xx.step.mongo.core.model.DbCursor;
import org.i3xx.step.mongo.core.model.DbDatabase;
import org.i3xx.step.mongo.core.model.DbObject;
import org.i3xx.step.mongo.core.util.TestData;
import org.i3xx.test.workspace.Workspace;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;

public class MongoDb001Test {

	static Logger logger = LoggerFactory.getLogger(MongoDb001Test.class);
	
	private List<String[]> data;
	
	@Before
	public void setUp() throws Exception {
		//set the system property '-Dworkspace.home=${workspace.home}/org.i3xx.step.cinque'
		//to run this in eclipse [Alt + Shift + X, T]
		String home = Workspace.location()+("/src/test/properties".replace('/', File.separatorChar));
		System.out.println(home);
		
		//Log4j configuration and setup
		PropertyConfigurator.configure(home+File.separator+"Log4j.properties");
		
		TestData td = new TestData(home+File.separator+"export_96202011.csv");
		data = td.getData();
		
		/*
		 * Start logging
		 */
		logger.info("Test org.i3xx.data.engine.TJdomParser -start-");
	}
	
	//
	// 
	// db.database = "{ 'database' : { 'name' : 'my', 'create' : true }}";
	// db.database = "{ 'database' : { 'name' : 'my', 'drop' : true }}";
	//
	// db.database = "{ 'database' : { 'name' : 'my', 'collection' : { 'name' : 'test', 'create' : true }}}";
	// db.database = "{ 'database' : { 'name' : 'my', 'collection' : { 'name' : 'test', 'drop' : true }}}";
	//
	// db.database = "{ 'database' : { 'name' : 'my', 'collection' : { 'name' : 'test', 'index' : { 'name' : 'field1', 'create' : true }}}}";
	// db.database = "{ 'database' : { 'name' : 'my', 'collection' : { 'name' : 'test', 'index' : { 'name' : 'field1', 'drop' : true }}}}";
	//
	//
	//
	
	
	
	@Test
	public void testA() throws UnknownHostException {
		
		MongoClient mongo = new MongoClient("localhost", 27017);
		DbDatabase db = new DatabaseImpl(mongo, "dbVDV");
		
		String[] h = data.get(0);
		
		for(int k=1;k<data.size();k++) {
			String[] p = data.get(k);
			
			StringBuffer data = new StringBuffer();
			data.append('{');
			for(int i=0;i<h.length;i++) {
				data.append("\""+h[i]+"\":\""+p[i]+"\",");
			}
			data.setCharAt(data.length()-1, '}');
			
			StringBuffer query = new StringBuffer();
			query.append('{');
			query.append("\""+h[0]+"\":\""+p[0]+"\"");
			query.append('}');
			
			DbCollection col = db.getCollection("anschrift");
			col.update(query.toString(), data.toString(), true, false);
		}//for
	}
	
	@Test
	public void testB() throws UnknownHostException {
		
		MongoClient mongo = new MongoClient("localhost", 27017);
		DbDatabase db = new DatabaseImpl(mongo, "dbVDV");
		DbCollection col = db.getCollection("anschrift");
		col.createIndex("{ \"Nr\" : \"1\"}", "{}");
		
	}
	
	@Test
	public void testC() throws UnknownHostException {
		
		MongoClient mongo = new MongoClient("localhost", 27017);
		DbDatabase db = new DatabaseImpl(mongo, "dbVDV");
		DbCollection col = db.getCollection("anschrift");
		DbCursor c = col.getCursor("{Nr: {$ne: \"\"}}");
		System.out.println( col.count() );
		
		while( c.hasNext() ) {
			DbObject o = c.next();
			//System.out.println( o.toString() );
		}
		
		//fail("Not yet implemented"); // TODO
	}
	
	@Ignore
	public void testD() throws UnknownHostException {
		
		MongoClient mongo = new MongoClient("localhost", 27017);
		DbDatabase db = new DatabaseImpl(mongo, "dbVDV");
		DbCollection col = db.getCollection("anschrift");
		
		col.remove("{\"Nr\": {$exists: true}}");
	}

}
