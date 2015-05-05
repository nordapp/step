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


import java.util.Map;

import org.i3xx.step.mongo.core.model.DbInputFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;

/**
 * @author Stefan
 *
 */
public class InputFileImpl extends FileImpl implements DbInputFile {

	private GridFSInputFile file;
	
	/**
	 * @param file
	 */
	public InputFileImpl( GridFSInputFile file) {
		super(file);
		
		this.file = file;
	}
	
	/**
	 * Sets the filename of the file
	 * 
	 * @param filename
	 */
	public void setFilename(String filename) {
		file.setFilename(filename);
	}
	
	/**
	 * Sets the content type (MIME type) of the file.
	 * 
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		file.setContentType(contentType);
	}
	
	/**
	 * Sets the metadata
	 * 
	 * @param jsonData
	 */
	public void setMetadata(String jsonData) {
		DBObject props = (DBObject) JSON.parse(jsonData);
		file.setMetaData(props);
	}
	
	/**
	 * Sets the metadata
	 * 
	 * @param metaData
	 */
	public void setMetadata(Map<String, Object> metaData) {
		file.setMetaData(new BasicDBObject(metaData));
	}
	
	/**
	 * Saves the file
	 */
	public void save() {
		file.save();
	}

}
