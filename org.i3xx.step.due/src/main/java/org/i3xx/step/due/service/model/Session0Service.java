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


import org.i3xx.util.symbol.service.model.SymbolService;


/**
 * A '0' session contains all data and services that are commonly used
 * by all sessions of the same mandator.
 * 
 * @author Stefan
 *
 */
public interface Session0Service {

	/**
	 * Creates a '0' session
	 * 
	 * @param sessionService The session service
	 * @param symbolService The symbol service
	 * @param mandatorId The mandator id
	 */
	public void initialize(SessionService sessionService, SymbolService symbolService, String mandatorId);
}
