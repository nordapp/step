package org.i3xx.step.uno.impl;

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


import org.apache.log4j.Logger;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import org.mozilla.javascript.Context;

public class ScriptRuntime {

	static Logger logger = Logger.getLogger(ScriptRuntime.class);
	
	private ScriptEngine engine;
	private Scriptable scope;
	private Object[] noargs;
	
	public ScriptRuntime(Scriptable scope, ScriptEngine engine) {
		this.scope = scope;
		this.engine = engine;
		this.noargs = new Object[]{};
	}
	
	public Object exec(String name) {
		return exec(name, noargs);
	}
	
	/**
	 * Executes a loaded function
	 * 
	 * @param name The function name
	 * @param args The arguments
	 */
	public Object exec(String name, Object[] args) {
		Context context = Context.enter();
		try{
			
	        Function fx = null;
        	try{
        		Scriptable sx = engine.searchObject(name, scope);
    			fx = (Function)sx;
        	}catch(ClassCastException ee) {
        		if(fx==null)
        			throw ee;
        		
        		throw new ClassCastException("The function '"+name+
        				"' is not a JavaScript function (function="+fx.getClass().getName()+").");
        	}
        	return fx.call(context, scope, scope, args);
			
		}finally{
			Context.exit();
		}
	}

}
