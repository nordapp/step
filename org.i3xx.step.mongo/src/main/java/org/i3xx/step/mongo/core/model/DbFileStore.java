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


import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface DbFileStore {

	
	/**
	 * @param buffer
	 * @param jsonProps
	 * @throws IOException
	 */
	DbInputFile createFile(byte[] buffer) throws IOException;
	
	/**
	 * @param file
	 * @param jsonProps
	 * @throws IOException
	 */
	DbInputFile createFile(File file) throws IOException;
	
	/**
	 * @param in
	 * @param jsonProps
	 * @throws IOException
	 */
	DbInputFile createFile(InputStream in) throws IOException;
	
	/**
	 * @param query
	 * @return
	 */
	DbFile getFile(String query);
	
	/**
	 * @param id
	 * @return
	 */
	DbFile getFileFromId(String id);
}