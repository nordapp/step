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


import java.util.NoSuchElementException;

import org.i3xx.step.uno.impl.service.builtin.NotifyValueService;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.Wrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ThisHostObject provides a bridge between a Function object and the 
 * NotifyValueService.Change extends NotifyValueService.Event interface.
 * 
 * @author Stefan
 *
 */
public class FunctionWrapper extends ScriptableObject {
	
	static Logger logger = LoggerFactory.getLogger(FunctionWrapper.class);
	
    private static final long serialVersionUID = 1L;
    private static String scriptClassName = "Undefined";
    
	@Override
	public String getClassName() {
		return scriptClassName;
	}
    
	/**
	 * @param function
	 */
	public FunctionWrapper() {
	}
	
	/**
	 * Converts a Rhino Function Object to a NotifyChange wrapper
	 * 
	 * @param function The script function object (a function name not a function object is used)
	 * @param target The target that uses the function (to select the interface used by the caller). 
	 * @param funcScope The scope the function is defined (optional or null).
	 * @return
	 */
	public Object functionWrap(Object function, Object target, Object funcScope) {
		//The funtion scope may be null or missing
		if( funcScope == null || (funcScope instanceof Undefined))
			funcScope = ((Function) function).getParentScope();
		
		final Context context = Context.getCurrentContext();
		final Scriptable scope = (Scriptable)funcScope;
		final Function func = (Function)function;
		
		String type = null;
		if(target instanceof Wrapper){
			Object w = ((Wrapper)target).unwrap();
			if(w!=null)
				type = w.getClass().getSimpleName();
		}else if(target instanceof Scriptable){
			type = ((Scriptable)target).getClassName();
		}
		if(type==null){
			logger.error("The wrapper type null is not supported ("+target+").");
			throw new IllegalArgumentException("The wrapper type null is not supported ("+target+").");
			
		}else if(type.equals("NotifyValueService")) {
		
			return new NotifyValueService.Change() {
				
				public void notify(String key, Object oldValue, Object newValue) {
					func.call( context, scope, scope,
							new Object[]{key, oldValue, newValue});
				}
			};
		
		}else{
			logger.error("The target '"+target+"' is unknown.");
			throw new NoSuchElementException("The target '"+target+"' is unknown.");
		}
	}

}
