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
import java.io.InputStream;
import java.util.Map;

public interface ResourceService {
	
	public static final int FILE_RESOURCE = 1;
	
	
	/*
	 * Returns true if the resource is available, false otherwise
	 * 
	 * @param mandatorId The mandator that holds the resource
	 * @param groupId The id of the group
	 * @param artifactId The id of the artifact
	 * @param path The resource path relative to the mandator root
	 * @return
	 * @throws IOException
	 */
	//currently not implemented because, if you need the resource try to get it and deal
	//with the exception thrown if the resource is not there.
	//boolean available(String mandatorId, String groupId, String artifactId, String path);
	
	/**
	 * Gets a resource as a String
	 * 
	 * @param mandatorId The mandator that holds the resource
	 * @param groupId The id of the group
	 * @param artifactId The id of the artifact
	 * @param path The resource path relative to the mandator root
	 * @param type The type of the resource
	 * @param props Properties (optional)
	 * @return The resource as a String
	 * @throws IOException If the resource cannot be read
	 */
	String getResource(String mandatorId, String groupId, String artifactId, String path, int type, Map<String, String> props) throws IOException;
	
	/**
	 * Gets a resource as an InputStream
	 * 
	 * @param mandatorId The mandator that holds the resource
	 * @param groupId The id of the group
	 * @param artifactId The id of the artifact
	 * @param path The resource path relative to the mandator root
	 * @param type The type of the resource
	 * @param props Properties (optional)
	 * @return The resource as an InputStream
	 * @throws IOException If the resource cannot be read
	 */
	InputStream getResourceAsStream(String mandatorId, String groupId, String artifactId, String path, int type, Map<String, String> props) throws IOException;
}
