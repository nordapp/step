package org.i3xx.step.uno.model.policy;

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


import org.i3xx.step.uno.model.StepContext;

public final class StepPolicy {

	/**
	 * Reads the argument 'system.runtime.interrupt'.
	 * 
	 * If this value is set to true the sequencer has to stop it's work immediately after the
	 * running step. The value turns to 'false' after it has been read.
	 * The next start resumes the work.
	 * 
	 * @param stepContext The StepContext
	 * @return
	 */
	public static boolean isInterrupted(StepContext stepContext) {
		
		boolean f = false;
		
		Object ret = stepContext.getValue("system.runtime.interrupt");
		if(ret==null){
			return false;
		}else
		if(ret instanceof String){
			f = Boolean.parseBoolean((String) ret);
		}else
		if(ret instanceof Boolean) {
			f = ((Boolean) ret).booleanValue();
		}
		else {
			throw new IllegalArgumentException("The argument 'system.runtime.interrupt' must be a Boolean or a String");
		}
		
		stepContext.setValue("system.runtime.interrupt", new Boolean(false));
		return f;
	}
	
	/**
	 * Reads the argument 'system.runtime.persistent'.
	 * 
	 * If this value is set to true the engine has to make it's state persistent to the cache.
	 * The value turns to 'false' after it has been read.
	 * 
	 * @param stepContext The StepContext
	 * @return
	 */
	public static boolean isPersistent(StepContext stepContext) {
		
		boolean f = false;
		
		Object ret = stepContext.getValue("system.runtime.persistent");
		if(ret==null){
			return false;
		}else
		if(ret instanceof String){
			f = Boolean.parseBoolean((String) ret);
		}else
		if(ret instanceof Boolean) {
			f = ((Boolean) ret).booleanValue();
		}
		else {
			throw new IllegalArgumentException("The argument 'system.runtime.persistent' must be a Boolean or a String");
		}
		
		stepContext.setValue("system.runtime.persistent", new Boolean(false));
		return f;
	}
}
