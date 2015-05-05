package org.i3xx.step.uno.impl.service.builtin;

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


import java.io.Serializable;

import org.i3xx.step.uno.impl.ContextImpl;
import org.i3xx.step.uno.model.service.builtin.BuiltinService;

/**
 * This service verifies the values of the key-value-pairs to be one of the following:
 * - Number
 * - String
 * - Serializable Object
 * - Null (null)
 * 
 * @author Stefan
 *
 */
public class VerifyValueService implements BuiltinService {

	private ContextImpl.ContextService context;
	
	public VerifyValueService(ContextImpl.ContextService context) {
		this.context = context;
	}
	
	/**
	 * Initializes this service
	 */
	public void initialize() {
		context.setVerifyValue(this);
	}
	
	/**
	 * Deactivates this service
	 */
	public void deactivate() {
		context.setVerifyValue(null);
	}
	
	/**
	 * Clears and reinitializes the service to a clean state.
	 * 
	 * Reinitialize is not thought to free the former used resources, but
	 * to start work in a clean state.
	 */
	public void reinitialize(ContextImpl.ContextService context) {
		this.context = context;
	}
	
	/**
	 * Verifies the value
	 * Returns true if the value is Serializable or a String or a Number or null
	 * 
	 * @param value The value
	 * @return
	 */
	public boolean verify(Object value) {
		return (value==null || value instanceof Serializable || value instanceof Number || value instanceof String);
	}

}
