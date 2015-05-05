package org.i3xx.step.mongo.service.impl;

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

import org.i3xx.step.mongo.core.impl.DatabaseImpl;
import org.i3xx.step.mongo.core.model.DbDatabase;
import org.i3xx.step.mongo.service.model.DatabaseService;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;

public class DatabaseServiceImpl implements DatabaseService {
	
	static Logger logger = LoggerFactory.getLogger(DatabaseServiceImpl.class);
	
	/** The mongodb host */
	final private String host;
	
	/** The port of the mongodb */
	final private int port;
	
	/** The mandator id */
	final private String mandatorId;
	
	private DbDatabase db;
	
	public DatabaseServiceImpl(String host, int port, String mandatorId) throws UnknownHostException {
		this.host = host;
		this.port = port;
		this.mandatorId = mandatorId;
		this.db = null;
		
		restart();
	}
	
	public void restart() throws UnknownHostException {
		MongoClient mongo = new MongoClient(host, port);
		db = new DatabaseImpl(mongo, "db"+mandatorId);
	}
	
	public DbDatabase getDatabase() {
		return db;
	}
	
	public DbDatabase getDatabase(boolean newInstance) throws UnknownHostException {
		if(! newInstance)
			return db;
		
		MongoClient mongo = new MongoClient(host, port);
		DbDatabase mdb = new DatabaseImpl(mongo, "db"+mandatorId);
		
		return mdb;
	}
	
	/**
	 * Gets the session with the id
	 * 
	 * @param context The bundle context
	 * @param cert The certificate
	 * @param id The id of the mandator
	 * @param sessionId The id of the session
	 * @return
	 */
	public static DatabaseService getDatabase(BundleContext bundleContext, String cert, String mandatorId) {
		
		checkPermission("read", "database");
		DatabaseService dbService = null;
		
		//
		// Gets the session
		//
		try {
			String clazz = DatabaseService.class.getName();
			String filter = "(&("+Constants.OBJECTCLASS+"="+
					DatabaseService.class.getName()+")("+Mandator.MANDATORID+"="+
					mandatorId+")("+Mandator.USERACCESSID+"=true))";
					
			
			ServiceReference<?>[] srv = bundleContext.getServiceReferences(clazz, filter);
			logger.trace("Search session service reference mandator:{} result:{}", mandatorId,
					srv==null ? -1 : srv.length);
			
			if(srv!=null && srv.length>0)
				dbService = (DatabaseService)bundleContext.getService(srv[0]);
		} catch (InvalidSyntaxException e) {
			logger.error("The syntax of the filter is not valid.", e);
		} catch (Exception e) {
			logger.error("The session search results in an exception.", e);
		}
		
		return dbService;
	}
	
	/**
	 * Checks the permission of the Service ( read | create | destroy )
	 * 
	 * @param action The action to be done
	 * @param target The target the permission is requested for
	 */
	private static void checkPermission(String action, String target) {
		
		//TODO Implement the permission to access the mandator configuration
	}
}
