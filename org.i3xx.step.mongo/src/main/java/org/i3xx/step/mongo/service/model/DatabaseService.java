package org.i3xx.step.mongo.service.model;

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

import org.i3xx.step.mongo.core.model.DbDatabase;

public interface DatabaseService {
	
	/**
	 * Restarts the database driver
	 * 
	 * @throws UnknownHostException
	 */
	void restart() throws UnknownHostException;
	
	/**
	 * @return The database
	 */
	DbDatabase getDatabase();
	
	/**
	 * @param newInstance If true, creates a new driver instance.
	 * @return The database
	 */
	DbDatabase getDatabase(boolean newInstance) throws UnknownHostException;
}
