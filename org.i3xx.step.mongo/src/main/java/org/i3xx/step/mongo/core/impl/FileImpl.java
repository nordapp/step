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


import java.io.InputStream;
import java.util.Date;

import org.i3xx.step.mongo.core.model.DbFile;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

public class FileImpl implements DbFile {
	
	private boolean isDbFile;
	private GridFSFile file;
	
	public FileImpl(GridFSDBFile file) {
		isDbFile = true;
		this.file = file;
	}
	
	public FileImpl(GridFSFile file) {
		isDbFile = false;
		this.file = file;
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbFile#hasFile()
	 */
	public boolean hasFile() {
		return file!=null;
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbFile#getMimetype()
	 */
	public String getMimetype() {
		return file.getContentType();
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbFile#getFilename()
	 */
	public String getFilename() {
		return file.getFilename();
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbFile#getId()
	 */
	public Object getId() {
		return file.getId();
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbFile#getLength()
	 */
	public long getLength() {
		return file.getLength();
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbFile#getInputStream()
	 */
	public InputStream getInputStream() {
		return isDbFile ? ((GridFSDBFile)file).getInputStream() : null;
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.mongo.core.model.DbFile#getDate()
	 */
	public long getDate() {
		Date date = file.getUploadDate();
		return date==null ? 0 : date.getTime();
	}

}
