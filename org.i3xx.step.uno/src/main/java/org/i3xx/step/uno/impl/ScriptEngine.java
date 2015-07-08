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


import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.i3xx.step.uno.impl.api.FunctionWrapper;
import org.i3xx.step.uno.impl.api.Printf;
import org.i3xx.step.uno.impl.api.Require;
import org.i3xx.step.uno.impl.api.ScriptLogger;
import org.i3xx.step.uno.model.ScriptCache;
import org.i3xx.step.uno.model.StepContext;
import org.i3xx.util.store.Store;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The script engine creates and controls the java script scope.
 * 
 * To use different scopes with their own scripts, run fork and
 * load the scripts into the scope of the fork.
 * 
 * You can also load scripts to the parent scope before you do
 * the fork. These scripts are available to all scopes.
 * 
 * @author Stefan
 *
 */
public class ScriptEngine {

	static Logger logger = LoggerFactory.getLogger(ScriptEngine.class);
	
	//The shared scope
	private Scriptable scope;
	//The workflow context
	private StepContext stepContext;
	
	public ScriptEngine(StepContext stepContext) {
		this.scope = null;
		this.stepContext = stepContext;
	}
	
	private ScriptEngine(Scriptable scope, StepContext stepContext) {
		this.scope = scope;
		this.stepContext = stepContext;
	}
	
	/**
	 * Gets the step context
	 * 
	 * @return The step context
	 */
	public StepContext getStepContext() {
		return stepContext;
	}
	
	/**
	 * Sets the step context
	 * 
	 * @param stepContext The step context to set
	 */
	public void setStepContext(StepContext stepContext) {
		this.stepContext = stepContext;
	}
	
	/**
	 * Initializes the script engine
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	public void init() throws SecurityException, NoSuchMethodException {
		
		//The scope
		if( ! ((ContextImpl)stepContext).isLocked() ){
			//switch sandbox off
		}else{
			//
			// Errors may be caused by missing class definition in
			// SandboxClassShutter
			//
			
			//Maven workaround
			//if( ! ContextFactory.hasExplicitGlobal())
			//ContextFactory.initGlobal(new SandboxContextFactory());
		}
	    
	    try {
	    	Context context = Context.enter();
	    	
	    	ScriptableObject prototype = context.initStandardObjects();
	        prototype.setParentScope(null);
	        Scriptable scope = context.newObject(prototype);
	        scope.setPrototype(prototype);
	        
	        // *** We do not (and never) do this ***
	        //Force the LiveConnect stuff to be loaded.
	        if( ! ((ContextImpl)stepContext).isLocked() ){
	        	String loadMe = "RegExp; getClass; java; Packages; JavaAdapter;";
	        	context.evaluateString(scope , loadMe, "lazyLoad", 0, null);
	        }
	        
	        // *** Host objects ***
	        //Set static access to the environment
	        //
	        //Provides a function printOut to print out to the java environment
	        Method jnprintf = Printf.class.getMethod("println", new Class[]{Object.class});
	        FunctionObject jsprintf = new FunctionObject("println", jnprintf, new Printf(System.out));
	        ScriptableObject.putProperty(scope, "println", jsprintf);
	        //
	        //Provides a function log to log out to the java logger
	        Method jnlog = ScriptLogger.class.getMethod("info", new Class[]{String.class, Object.class, Object.class, Object.class, Object.class, Object.class});
	        FunctionObject jslog = new FunctionObject("log", jnlog, new ScriptLogger());
	        ScriptableObject.putProperty(scope, "log", jslog);
	        //
	        //Provides a function wrapFunction to register script functions to java services
	        Method jnnotify = FunctionWrapper.class.getMethod("functionWrap", new Class[]{Object.class, Object.class, Object.class});
	        FunctionObject jsnotify = new FunctionObject("wrapFunction", jnnotify, new FunctionWrapper());
	        ScriptableObject.putProperty(scope, "wrapFunction", jsnotify);
	        //
	        //Provides a function require to access the OSGi services
	        Method jnrequire = Require.class.getMethod("require", new Class[]{String.class, String.class});
	        FunctionObject jsrequire = new FunctionObject("require", jnrequire, new Require((ContextImpl)stepContext));
	        ScriptableObject.putProperty(scope, "require", jsrequire);
	        //
	        //Provides access to the step context
	        //Because we use sandbox behavior we have no access to java except
	        //the classes to be freed by the SandboxClassShutter. No other class
	        //is accessible to any script.
	        //
			ScriptableObject.putProperty(scope, "context", stepContext);
	        
	        this.scope = scope;
	        ((ContextImpl)stepContext).setScope(scope);
	        
	        logger.debug("Do init() scope: {}", scope.hashCode());
	    } catch(Throwable e){
	    	logger.error("Exception initializing script.", e);
	    } finally {
	        Context.exit();
	    }
        
        // do what you want within the scope
	}
	
	/**
	 * Load scripts to the scope
	 * 
	 * @param store The store containing the scripts
	 * @param key The key identifies the store to be used.
	 * @throws IOException 
	 * @throws NoSuchElementException If the resolving of a feature (script) fails.
	 */
	public void load(Store store, BigInteger key) throws IOException, NoSuchElementException {
		//
		// Gets a sorted map of the keys. The map is in the order
		// of the sort objects (not in key order) which are long
		// values (set by the ScriptLoader).
		// sh 19.11.2014, Replaced by the ScriptResolver.
		//
	    
		ScriptResolver res = new ScriptResolver();
		res.load(store, key);
		if( ! res.resolve() ){
			for(ScriptResolver.Entry e : res.getUnresolved()){
				logger.error("Unable to resolve the feature {} at {} '{}'", e.getFeature(), e.getOrder(), e.getName());
			}//for
			throw new NoSuchElementException("There is an unresolved feature. See log for further information.");
		}//fi
		List<ScriptResolver.Entry> list = res.getResolved();
		
	    try {
	    	Context context = Context.enter();
	    	
	    	int line = 0;
	    	
			for(ScriptResolver.Entry e : list) {
				String name = e.getName();
				BigInteger id = new BigInteger(e.getName());
				String source = store.readString(key, id);
				
				context.evaluateString(scope, source, name, line, null);
			}//for
        
	        logger.debug("Do load(Store, BigInteger) size: {} scope: {}", list.size(), scope.hashCode());
	    } finally {
	        Context.exit();
	    }
	}
	
	/**
	 * Load scripts to the scope
	 * 
	 * @param cache The script cache
	 * @throws IOException
	 */
	public void load(ScriptCache cache) throws IOException {
		
		ScriptResolver res = new ScriptResolver();
		res.load(cache);
		if( ! res.resolve() ){
			for(ScriptResolver.Entry e : res.getUnresolved()){
				logger.error("Unable to resolve the feature {} at {} '{}'", e.getFeature(), e.getOrder(), e.getName());
			}//for
			throw new NoSuchElementException("There is an unresolved feature. See log for further information.");
		}//fi
		List<ScriptResolver.Entry> list = res.getResolved();
		
	    try {
	    	Context context = Context.enter();
	    	
	    	int line = 0;
	    	
			for(ScriptResolver.Entry e : list) {
			//for(int i=0;i<cache.size();i++) {
				int i = e.getOrder();
				String name = cache.getName(i);
				String source = cache.read(i);
				
				context.evaluateString(scope, source, name, line, null);
			}//for
        
	        logger.debug("Do load(ScriptCache) list: {} scope: {}", list.size(), scope.hashCode());
	    } finally {
	        Context.exit();
	    }
	}
	
	/**
	 * Seals the scope
	 */
	public void seal() {
		((ScriptableObject) scope).sealObject();
	}
	
	/**
	 * Gets a fork with the shared scope as parent scope
	 * 
	 * @return The script engine
	 */
	public ScriptEngine fork() {
        Context context = Context.enter();
		try{
			ScriptableObject myscope = (ScriptableObject)context.newObject(this.scope);
			myscope.setPrototype(this.scope);
	        
	        // We want "myScope" to be a new top-level
	        // scope, so set its parent scope to null. This
	        // means that any variables created by assignments
	        // will be properties of "myScope".
			myscope.setParentScope(null);
			
	        logger.debug("Do fork() scope: {} parent: {}", myscope.hashCode(), scope.hashCode());
			return new ScriptEngine(myscope, stepContext);
		}finally{
			Context.exit();
		}
	}
	
	/**
	 * Gets the runtime engine to run the loaded scripts with the scope
	 * 
	 * @return The script runtime
	 */
	public ScriptRuntime runtime() {
		
        logger.debug("Do runtime() scope: {}", scope.hashCode());
		return new ScriptRuntime(scope, this);
	}
	
	/**
	 * The scriptable object is searched the parent chain then the prototype chain
	 * Only JavaScript Functions or Objects can be found.
	 * 
	 * @param name A function or object name
	 * @param object The object to start relative from.
	 * @return The scriptable object
	 */
	public Scriptable searchObject(String name, Scriptable object) {
		
		//
		// Resolves recursive names eg: "name1.name2.name3"
		//
        logger.debug("Do search() name: {} scope: {}", name, object.hashCode());
		
		if(name.contains(".")){
			String[] n = name.split("\\.");
			for(int i=0;i<n.length;i++){
				object = searchObject(n[i], object);
			}	
			return object;
		}//fi
		
		//
		// Searches the object
		//
		
		Object fx = null;
		Set<Scriptable> list = new HashSet<Scriptable>();
		
		for(;;){
			fx = object.get(name, object);
			if(fx==null){
	    		throw new NullPointerException("The object '"+name+
	    				"' is not a JavaScript object (object=null).");
	    		
			}else if(fx.equals(Scriptable.NOT_FOUND)){
				//not found, use parent scope
				Scriptable s = object.getParentScope();
				if(s==null){
					//not found use prototype scope
					s = object.getPrototype();
					if(s==null){
						//no parent or prototype scope
						break;
					}
				}
				if( !list.add(s)){
					//loop condition
					break;
				}
				object = s;
			}else{
				//found
		    	try{
		    		return (Scriptable) fx;
		    	}catch(ClassCastException ee) {
		    		throw new ClassCastException("The object '"+name+
		    				"' is not a JavaScript object (object="+fx.getClass().getName()+").");
		    	}
			}
		}//for
		
		throw new NoSuchElementException("The object '"+name+"' is not defined.");
	}
	
	/**
	 * 
	 */
	public void exit() {
		scope = null;
		((ContextImpl)stepContext).setScope(null);
	}

}
