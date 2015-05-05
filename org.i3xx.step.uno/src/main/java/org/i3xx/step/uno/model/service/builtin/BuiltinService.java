package org.i3xx.step.uno.model.service.builtin;

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


import org.i3xx.step.uno.impl.ContextImpl;

public interface BuiltinService {
	
	/**
	 * Initializes this service
	 */
	void initialize();
	
	/**
	 * Deactivates this service
	 */
	void deactivate();
	
	/**
	 * Clears and reinitializes the service to a clean state.
	 * 
	 * Reinitialize is not thought to free the former used resources, but
	 * to start work in a clean state.
	 */
	void reinitialize(ContextImpl.ContextService context);

}
