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

public interface MandatorCommand {
	
	/**
	 * Setup the mandatory environment
	 * 
	 * @param arg The mandator id argument
	 * @throws IOException
	 */
	void setupMandatory(String arg) throws IOException;
	
	/**
	 * Removes the mandatory environment and deletes all data
	 * 
	 * @param arg The mandator id argument
	 * @throws IOException
	 */
	void removeMandatory(String arg) throws IOException;

}
