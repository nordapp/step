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


import java.io.IOException;

import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.util.basic.io.FilePath;


public interface FileLocationService {

	/**
	 * The result of the file location service is the application root directory of the mandator.
	 * 
	 * @return The file location (directory)
	 */
	FilePath getLocation(Mandator mandator) throws IOException;
}
