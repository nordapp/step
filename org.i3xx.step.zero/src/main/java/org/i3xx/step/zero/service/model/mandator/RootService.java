package org.i3xx.step.zero.service.model.mandator;

/*
 * #%L
 * NordApp OfficeBase :: zero
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

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.ConfigurationException;

public interface RootService {

	/**
	 * Creates a mandator with the id
	 * 
	 * @param mandatorId
	 */
	void createMandator(String mandatorId) throws IOException, InvalidSyntaxException, ConfigurationException;
	
	/**
	 * Starts a mandator with the id
	 * 
	 * @param mandatorId
	 */
	void startMandator(String mandatorId) throws IOException, InvalidSyntaxException, ConfigurationException;
	
	/**
	 * Stops a mandator with the id
	 * 
	 * @param mandatorId
	 */
	void stopMandator(String mandatorId) throws IOException, InvalidSyntaxException, ConfigurationException;
	
	/**
	 * Destroys a configuration
	 * 
	 * @param servicePid The PID of the service
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 * @throws ConfigurationException
	 */
	void destroyMandator(String servicePid) throws IOException, InvalidSyntaxException, ConfigurationException;
	
	/**
	 * Prints a list of all running mandator into the log
	 */
	void listMandator();
	
	/**
	 * prints a list of all configured mandator
	 * 
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 */
	void listConfiguration() throws IOException, InvalidSyntaxException;
}
