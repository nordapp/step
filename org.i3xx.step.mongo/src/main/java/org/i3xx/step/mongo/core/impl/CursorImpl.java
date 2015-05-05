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


import org.i3xx.step.mongo.core.model.DbCursor;
import org.i3xx.step.mongo.core.model.DbObject;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class CursorImpl implements DbCursor {
	
	private DBCursor cursor;
	
	public CursorImpl(DBCursor cursor) {
		this.cursor = cursor;
	}
	
	public int numSeen() {
		return cursor.numSeen();
	}
	
	public DbObject one() {
		DBObject dbo = cursor.one();
		return dbo==null ? null : new ObjectImpl(dbo);
	}
	
	public DbObject curr() {
		DBObject dbo = cursor.curr();
		return dbo==null ? null : new ObjectImpl(dbo);
	}
	
	public boolean hasNext() {
		return cursor.hasNext();
	}
	
	public DbObject next() {
		DBObject dbo = cursor.next();
		return dbo==null ? null : new ObjectImpl(dbo);
	}

}
