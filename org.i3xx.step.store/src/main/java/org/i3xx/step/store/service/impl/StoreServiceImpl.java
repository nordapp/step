package org.i3xx.step.store.service.impl;

/*
 * #%L
 * NordApp OfficeBase :: store
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.i3xx.step.store.service.model.StoreService;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.util.store.Store;
import org.i3xx.util.store.StoreEntry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wraps a store to use the store as a service.
 * 
 * @author Stefan
 *
 */
public class StoreServiceImpl implements StoreService {
	
	static Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);

	private Store store;
	
	public StoreServiceImpl(String repository) {
		store = new Store(repository);
	}

	public BigInteger createStore() throws IOException {
		return store.createStore();
	}

	public List<BigInteger> getStoreKeys() {
		return store.getStoreKeys();
	}

	public void loadData() throws IOException {
		store.loadData();
	}

	public void mergeData() throws IOException {
		store.mergeData();
	}

	public void unloadData() throws IOException {
		store.unloadData();
	}

	public void verify() throws IOException {
		store.verify();
	}

	public void gc() throws IOException {
		store.gc();
	}

	public void cleanup() throws IOException {
		store.cleanup();
	}

	public BigInteger createStore(BigInteger key) throws IOException {
		return store.createStore(key);
	}

	public boolean existStore(BigInteger key) {
		return store.existStore(key);
	}

	public Map<Object, BigInteger> getSortMap(BigInteger key)
			throws IOException {
		return store.getSortMap(key);
	}

	public List<BigInteger> getStoreListing(BigInteger key) throws IOException {
		return store.getStoreListing(key);
	}

	public void reorganizeStore(BigInteger key) throws IOException {
		store.reorganizeStore(key);
	}

	public void reorganizeStore(BigInteger key, Map<Object, BigInteger> order)
			throws IOException {
		store.reorganizeStore(key, order);
	}

	public void verifyStore(BigInteger key) throws IOException {
		store.verifyStore(key);
	}

	public void cleanupStore(BigInteger key) throws IOException {
		store.verifyStore(key);
	}

	public StoreEntry nextEntry(BigInteger key, long lifetime,
			boolean persistent, Object sort) throws IOException {
		return store.nextEntry(key, lifetime, persistent, sort);
	}

	public boolean existStoreEntry(BigInteger key, BigInteger id)
			throws IOException {
		return store.existStoreEntry(key, id);
	}

	public void reorganizeStoreEntry(BigInteger key, BigInteger oldId,
			BigInteger newId) throws IOException {
		
	}

	public void cleanupStoreEntry(BigInteger key, BigInteger id)
			throws IOException {
		store.cleanupStoreEntry(key, id);
	}

	public InputStream read(BigInteger key, BigInteger id) throws IOException {
		return store.read(key, id);
	}

	public String readString(BigInteger key, BigInteger id) throws IOException {
		return store.readString(key, id);
	}

	public File getFile(StoreEntry entry) throws IOException {
		return store.getFile(entry);
	}

	public OutputStream write(StoreEntry entry) throws IOException {
		return store.write(entry);
	}

	public void writeString(String data, StoreEntry entry) throws IOException {
		store.writeString(data, entry);
	}
	
	/**
	 * Gets the store with the id
	 * 
	 * @param context The bundle context
	 * @param cert The certificate
	 * @param id The id of the mandator
	 * @param sessionId The id of the session
	 * @return
	 */
	public static StoreService getStore(BundleContext bundleContext, String cert, String mandatorId) {
		
		checkPermission("read", "store");
		StoreService store = null;
		
		//
		// Gets the session
		//
		try {
			String clazz = StoreService.class.getName();
			String filter = "(&("+Constants.OBJECTCLASS+"="+
					StoreService.class.getName()+")("+Mandator.MANDATORID+"="+
					mandatorId+")("+Mandator.USERACCESSID+"=true))";
					
			
			ServiceReference<?>[] srv = bundleContext.getServiceReferences(clazz, filter);
			logger.trace("Search store service reference mandator:{} result:{}", mandatorId,
					srv==null ? -1 : srv.length);
			
			if(srv!=null && srv.length>0)
				store = (StoreService)bundleContext.getService(srv[0]);
		} catch (InvalidSyntaxException e) {
			logger.error("The syntax of the filter is not valid.", e);
		} catch (Exception e) {
			logger.error("The store search results in an exception.", e);
		}
		
		return store;
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
