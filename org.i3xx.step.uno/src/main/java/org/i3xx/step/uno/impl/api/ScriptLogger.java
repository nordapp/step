package org.i3xx.step.uno.impl.api;

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


import org.mozilla.javascript.ScriptableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides functionality to print out to the java context.
 * 
 * @author Stefan
 *
 */
public class ScriptLogger extends ScriptableObject{
	
	static Logger logger = LoggerFactory.getLogger(ScriptLogger.class);
	
    private static final long serialVersionUID = 1L;
    private static String scriptClassName = "Undefined";
    
	public ScriptLogger() {
	}

	@Override
	public String getClassName() {
		return scriptClassName;
	}
	
	/**
	 * Prints the value out. Use this with the FunctionObject of Rhino
	 * 
	 * @param value
	 */
	public void info(String value) {
		logger.info(value);
	}
	
	/**
	 * Prints the value out. Use this with the FunctionObject of Rhino
	 * 
	 * @param value
	 */
	public void info(String stmt, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
		logger.info(stmt, arg0, arg1, arg2, arg3, arg4);
	}

}
