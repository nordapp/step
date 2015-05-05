package org.i3xx.step.zero.service.model.mandator;

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


import java.util.Dictionary;

import org.osgi.framework.ServiceReference;

public interface Mandator {
	
	//
	// Configuration data
	//
	/** The service property key of the mandator */
	public static final String MANDATORID = "mandatorId";
	/** The service property key of the group */
	public static final String GROUPID = "groupId";
	/** The service property key of the artifact */
	public static final String ARTIFACTID = "artifactId";
	/** The service is accessible by the system only */
	public static final String SYSTEMACCESSID = "systemaccessId";
	/** The service is accessible by the user */
	public static final String USERACCESSID = "useraccessId";

	/**
	 * @return the title
	 */
	String getTitle();

	/**
	 * @param title the title to set
	 */
	void setTitle(String title);
	
	/**
	 * @return the id
	 */
	String getId();

	/**
	 * @param id the id to set
	 */
	void setId(String id);

	/**
	 * @return the root
	 */
	String getRoot();

	/**
	 * @param root the root to set
	 */
	void setRoot(String root);

	/**
	 * @return the path
	 */
	String getPath();

	/**
	 * @param path the path to set
	 */
	void setPath(String path);
	
	/**
	 * Gets a key array of all property names
	 * 
	 * @return
	 */
	String[] getPropertyKeys();
	
	/**
	 * Gets a property
	 * 
	 * @param key The key of the property
	 * @return
	 */
	String getProperty(String key);
	
	/**
	 * Sets a a property
	 * 
	 * @param key The key of the property
	 * @param value The value of the property
	 */
	void setProperty(String key, String value);
	
	//
	// Administration data
	//
	
	/**
	 * @return True if the data has been initialized, false otherwise.
	 */
	boolean isInitialized();
	
	/**
	 * Set initialized
	 * 
	 * @param initialized The flag
	 */
	void setInitialized(boolean initialized);
	
	/**
	 * @return True if the mandator should be started, false otherwise.
	 */
	boolean isStarted();
	
	/**
	 * Set started
	 * 
	 * @param started The flag
	 */
	void setStarted(boolean started);
	
	/**
	 * @return True if the data changes and the configuration needs to be updated,
	 * false otherwise.
	 */
	boolean isDirty();
	
	/**
	 * Put the data into the dictionary
	 * 
	 * @param config The dictionary
	 */
	void save(Dictionary<String, Object> config);
	
	/**
	 * @return the factoryPid
	 */
	String getFactoryPid();

	/**
	 * @param factoryPid the factoryPid to set
	 */
	void setFactoryPid(String factoryPid);

	/**
	 * @return the servicePid
	 */
	String getServicePid();

	/**
	 * @param servicePid the servicePid to set
	 */
	void setServicePid(String servicePid);

	/**
	 * @return the serviceRef
	 */
	ServiceReference<?> getServiceRef();

	/**
	 * @param serviceRef the serviceRef to set
	 */
	void setServiceRef(ServiceReference<?> serviceRef);
}
