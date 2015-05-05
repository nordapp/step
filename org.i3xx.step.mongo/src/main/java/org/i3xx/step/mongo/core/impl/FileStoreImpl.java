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
import java.io.IOException;
import java.io.InputStream;

import org.bson.types.ObjectId;
import org.i3xx.step.mongo.core.model.DbFile;
import org.i3xx.step.mongo.core.model.DbFileStore;
import org.i3xx.step.mongo.core.model.DbInputFile;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;

public class FileStoreImpl implements DbFileStore {
	
	private GridFS fs;
	
	public FileStoreImpl(DB dbs) {
		fs = new GridFS(dbs);
	}
	
	public FileStoreImpl(DB dbs, String bucket) {
		fs = new GridFS(dbs, bucket);
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbFileStore#createFile(byte[])
	 */
	public DbInputFile createFile(byte[] buffer) throws IOException {
		GridFSInputFile f = fs.createFile(buffer);
		return new InputFileImpl(f);
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbFileStore#createFile(java.io.File)
	 */
	public DbInputFile createFile(File file) throws IOException {
		GridFSInputFile f = fs.createFile(file);
		return new InputFileImpl(f);
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbFileStore#createFile(java.io.InputStream)
	 */
	public DbInputFile createFile(InputStream in) throws IOException {
		GridFSInputFile f = fs.createFile(in);
		return new InputFileImpl(f);
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbFileStore#getFile(java.lang.String)
	 */
	public DbFile getFile(String jsonQuery) {
		
		DBObject props = (DBObject) JSON.parse(jsonQuery);
		
		GridFSDBFile file = fs.findOne(props);
		return new FileImpl(file);
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbFileStore#getFileFromId(java.lang.String)
	 */
	public DbFile getFileFromId(String id) {
		
		GridFSDBFile file = fs.findOne( new ObjectId(id) );
		return new FileImpl(file);
	}

}
