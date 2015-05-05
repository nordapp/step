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


import java.io.InputStream;

public interface DbFile {

	/**
	 * Returns true if the file exists, false otherwise. If the file
	 * doesn't exist, the query returns no result.
	 * 
	 * @return
	 */
	boolean hasFile();
	
	/**
	 * Gets the mime type of the file
	 * 
	 * @return
	 */
	String getMimetype();
	
	/**
	 * Gets the filename of the file
	 * 
	 * @return
	 */
	String getFilename();
	
	/**
	 * Gets the id of the file in the database
	 * 
	 * @return
	 */
	Object getId();
	
	/**
	 * gets the length of the file
	 * 
	 * @return
	 */
	long getLength();
	
	/**
	 * Gets the input stream of the file
	 * 
	 * @return
	 */
	InputStream getInputStream();
	
	/**
	 * Gets the data of the upload
	 * 
	 * @return
	 */
	long getDate();
}
