package org.i3xx.step.due.service.model;

/*
 * #%L
 * NordApp OfficeBase :: due
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


import java.io.IOException;

public interface PropertyService {
	
	/**
	 * The path stored in the service registry as a property. The path
	 * must point to a file relative to mandator/data/page/file/bundle/group/artifact/properties/.
	 */
	public static final String PATH_ID = "pathId";
	
	/**
	 * Set the properties of the mandator
	 * 
	 * @param mandatorId The mandator to set
	 * @param groupId The id of the group
	 * @param artifactId The id of the artifact
	 * @param path The property file
	 * @throws IOException
	 */
	void setProperties(String mandatorId, String groupId, String artifactId, String path) throws IOException;
}
