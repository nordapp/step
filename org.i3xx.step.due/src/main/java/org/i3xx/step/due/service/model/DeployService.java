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


import java.io.File;
import java.io.IOException;

/**
 * The service creates a ZIP file containing the content
 * of the artifact or an empty ZIP file with the default
 * structure of the artifact.
 * 
 * The default structure is
 * /js
 * /properties
 * /resources
 * 
 * @author Stefan
 *
 */
public interface DeployService {

	/**
	 * Creates the default structure in the temporary directory and make a ZIP file
	 * 
	 * @param processID The id of the process
	 * @param mandatorID The id of the mandator
	 * @param groupID The id of the group
	 * @param artifactID The id of the artifact
	 * @return The zip file
	 * 
	 * @throws IOException
	 */
	File createEmptyZip(String processID, String mandatorID, String groupID, String artifactID) throws IOException;

	/**
	 * Creates the default structure in the temporary directory and copies the files into
	 * the structure and makes a ZIP file from it.
	 * 
	 * @param processID The id of the process
	 * @param mandatorID The id of the mandator
	 * @param groupID The id of the group
	 * @param artifactID The id of the artifact
	 * @return The zip file
	 * 
	 * @throws IOException
	 */
	File zipFromData(String processID, String mandatorID, String groupID, String artifactID) throws IOException;

	/**
	 * Creates the structure from a ZIP file and copies the files to the target.
	 * 
	 * @param processID The id of the process
	 * @param mandatorID The id of the mandator
	 * @param groupID The id of the group
	 * @param artifactID The id of the artifact
	 * @param zipFileName The name of the ZIP file to process
	 * 
	 * @throws IOException
	 */
	void zipToData(String processID, String mandatorID, String groupID, String artifactID, String zipFileName) throws IOException;
}
