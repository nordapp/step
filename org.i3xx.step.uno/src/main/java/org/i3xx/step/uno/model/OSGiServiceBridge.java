package org.i3xx.step.uno.model;

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


import java.util.Map;

/**
 * <p>The bridge implements a filter to ensure that only
 * mandator specific services are accessible.</p>
 * 
 * @author Stefan
 *
 */
public interface OSGiServiceBridge {

	/**
	 * Gets access to an OSGi Service to an inner scope.
	 * 
	 * @param name The name of the service to be accessed
	 * @param properties The properties to select the service
	 * @return
	 */
	Object getService(String name, Map<String, Object> properties);
}
