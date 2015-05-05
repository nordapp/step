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
import java.util.Set;

import org.i3xx.step.mongo.core.model.DbObject;

import com.mongodb.DBObject;

public class ObjectImpl implements DbObject {
	
	DBObject dbo;
	
	public ObjectImpl(DBObject dbo) {
		this.dbo = dbo;
	}
	
	public boolean containsField(String key) {
		return dbo.containsField(key);
	}
	
	public Object get(String key) {
		return dbo.get(key);
	}
	
	public Set<String> keySet() {
		return dbo.keySet();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMap() {
		return dbo.toMap();
	}
	
	public String toString() {
		return dbo.toString();
	}

}
