package org.i3xx.step.uno.model.service;

/*
 * #%L
 * NordApp OfficeBase :: uno
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

public interface DeployService {
	
	
	/**
	 * Adds a file to a script default
	 * 
	 * @param mandatorId The id of the mandator
	 * @param path The path to deploy the file to
	 * @param target The target filename of the content (use '*.css', '*.js', '*.mf', '*.htm', '*.conf' for generic names)
	 * @param content The content file
	 * @throws IOException
	 */
	void deployDefault(String mandatorId, String path, String target, File content) throws IOException;
	
	/**
	 * Adds a file to a script default
	 * 
	 * @param mandatorId The id of the mandator
	 * @param path The path to deploy the file to
	 * @param target The target filename of the content (use '*.css', '*.js', '*.mf', '*.htm', '*.conf' for generic names)
	 * @param content The content as String
	 * @throws IOException
	 */
	void deployDefault(String mandatorId, String path, String target, String content) throws IOException;
	
	/**
	 * Adds a file to a script default
	 * 
	 * @param mandatorId The id of the mandator
	 * @param path The path to deploy the file to
	 * @param target The target filename of the content (use '*.css', '*.js', '*.mf', '*.htm', '*.conf' for generic names)
	 * @param content The content as InputStream
	 * @throws IOException
	 */
	void deployDefault(String mandatorId, String path, String target, InputStream content) throws IOException;
	
	/**
	 * Adds a file to a script bundle
	 * 
	 * @param mandatorId The id of the mandator
	 * @param groupId The group id of the bundle to deploy the file to
	 * @param artifactId The artifact id of the bundle to deploy to
	 * @param target The target filename of the content (use '*.css', '*.js', '*.mf', '*.htm', '*.conf' for generic names)
	 * @param content The content file
	 * @throws IOException
	 */
	void deployContent(String mandatorId, String groupId, String artifactId, String target, File content) throws IOException;

	/**
	 * Adds a file to a script bundle
	 * 
	 * @param mandatorId The id of the mandator
	 * @param groupId The group id of the bundle to deploy the file to
	 * @param artifactId The artifact id of the bundle to deploy to
	 * @param target The target filename of the content (use '*.css', '*.js', '*.mf', '*.htm', '*.conf' for generic names)
	 * @param content The content as String
	 * @throws IOException
	 */
	void deployContent(String mandatorId, String groupId, String artifactId, String target, String content) throws IOException;

	/**
	 * Adds a file to a script bundle
	 * 
	 * @param mandatorId The id of the mandator
	 * @param groupId The group id of the bundle to deploy the file to
	 * @param artifactId The artifact id of the bundle to deploy to
	 * @param target The target filename of the content (use '*.css', '*.js', '*.mf', '*.htm', '*.conf' for generic names)
	 * @param content The content as InputStream
	 * @throws IOException
	 */
	void deployContent(String mandatorId, String groupId, String artifactId, String target, InputStream content) throws IOException;
	
	/**
	 * Adds a file to a bundle
	 * 
	 * @param mandatorId The id of the mandator
	 * @param groupId The group id of the bundle to deploy the file to
	 * @param artifactId The artifact id of the bundle to deploy to
	 * @param target The target filename of the content (use '*.css', '*.js', '*.mf', '*.htm', '*.conf' for generic names)
	 * @param content The content file
	 * @throws IOException
	 */
	void deployResources(String mandatorId, String groupId, String artifactId, String target, File content) throws IOException;

	/**
	 * Adds a file to a bundle
	 * 
	 * @param mandatorId The id of the mandator
	 * @param groupId The group id of the bundle to deploy the file to
	 * @param artifactId The artifact id of the bundle to deploy to
	 * @param target The target filename of the content (use '*.css', '*.js', '*.mf', '*.htm', '*.conf' for generic names)
	 * @param content The content as String
	 * @throws IOException
	 */
	void deployResources(String mandatorId, String groupId, String artifactId, String target, String content) throws IOException;

	/**
	 * Adds a file to a bundle
	 * 
	 * @param mandatorId The id of the mandator
	 * @param groupId The group id of the bundle to deploy the file to
	 * @param artifactId The artifact id of the bundle to deploy to
	 * @param target The target filename of the content (use '*.css', '*.js', '*.mf', '*.htm', '*.conf' for generic names)
	 * @param content The content as InputStream
	 * @throws IOException
	 */
	void deployResources(String mandatorId, String groupId, String artifactId, String target, InputStream content) throws IOException;
	
	/**
	 * Adds a file to a bundle
	 * 
	 * @param mandatorId The id of the mandator
	 * @param groupId The group id of the bundle to deploy the file to
	 * @param artifactId The artifact id of the bundle to deploy to
	 * @param target The target filename of the content (use '*.css', '*.js', '*.mf', '*.htm', '*.conf' for generic names)
	 * @param content The content file
	 * @throws IOException
	 */
	void deployProperties(String mandatorId, String groupId, String artifactId, String target, File content) throws IOException;

	/**
	 * Adds a file to a bundle
	 * 
	 * @param mandatorId The id of the mandator
	 * @param groupId The group id of the bundle to deploy the file to
	 * @param artifactId The artifact id of the bundle to deploy to
	 * @param target The target filename of the content (use '*.css', '*.js', '*.mf', '*.htm', '*.conf' for generic names)
	 * @param content The content as String
	 * @throws IOException
	 */
	void deployProperties(String mandatorId, String groupId, String artifactId, String target, String content) throws IOException;

	/**
	 * Adds a file to a bundle
	 * 
	 * @param mandatorId The id of the mandator
	 * @param groupId The group id of the bundle to deploy the file to
	 * @param artifactId The artifact id of the bundle to deploy to
	 * @param target The target filename of the content (use '*.css', '*.js', '*.mf', '*.htm', '*.conf' for generic names)
	 * @param content The content as InputStream
	 * @throws IOException
	 */
	void deployProperties(String mandatorId, String groupId, String artifactId, String target, InputStream content) throws IOException;
}
