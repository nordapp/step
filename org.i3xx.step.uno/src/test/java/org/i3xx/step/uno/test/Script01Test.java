package org.i3xx.step.uno.test;

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


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.math.BigInteger;
import java.util.UUID;

import org.apache.log4j.PropertyConfigurator;
import org.i3xx.step.uno.impl.ContextImpl;
import org.i3xx.step.uno.impl.ScriptEngine;
import org.i3xx.step.uno.impl.ScriptLoader;
import org.i3xx.step.uno.impl.ScriptRuntime;
import org.i3xx.test.workspace.Workspace;
import org.i3xx.util.basic.io.FilePath;
import org.i3xx.util.store.Store;
import org.junit.Before;
import org.junit.Test;


/**
 * The test produces a simple output.
 * 
 * @author Stefan
 *
 */
public class Script01Test {
	
	static FilePath RES_LOC = null;
	
	@Before
	public void setUp() throws Exception {
		
		String loc = Workspace.location().replace(File.separatorChar, '/');
		RES_LOC = FilePath.get(loc).add("/src/test/resources");
	}
	
	@Test
	public void test() throws Exception {
		
		String home = RES_LOC.getPath();
		System.out.println(home);
		
		//Log4j configuration and setup
		PropertyConfigurator.configure(home+File.separator+"Log4j.properties");
		
		Store store = new Store("file://"+RES_LOC.toString()+"/store");
		store.loadData();
		
		ScriptLoader loader = new ScriptLoader();
		
		BigInteger key = loader.getBigIntegerFromUuid(UUID.randomUUID());
		store.createStore(key);
		
		//Load store
		//File dir = CURL.fileURLtoFile("file://"+RES_LOC.toString()+"/js");
		//loader.crawler(dir, store, key);
		loader.importf(RES_LOC.add("/js/i3xx-small.js").toFile(), store, key);
		loader.importf(RES_LOC.add("/js/test/Test-01.js").toFile(), store, key);
		
		//List<BigInteger> list = store.getStoreListing(key);
		//System.out.println("size: "+list.size());
		
		ContextImpl stepContext = new ContextImpl();
		stepContext.initializeBuiltinServices();
		stepContext.lock();
		
		ScriptEngine engine = new ScriptEngine(stepContext);
		engine.init();
		engine.load(store, key);
		engine.seal();
		
		ScriptRuntime rt = engine.fork().runtime();
		rt.exec("init", new Object[]{});
		rt.exec("test", new Object[]{});
		rt.exec("test", new Object[]{});
		rt.exec("test", new Object[]{});
		rt.exec("test", new Object[]{});
		rt.exec("test", new Object[]{});
		rt.exec("test", new Object[]{});
		
		assertEquals( 6.0, stepContext.getValue("test.counter") );
		assertEquals( "My counter 6.0", stepContext.getValue("test.name") );
		
		engine.exit();
		
		store.unloadData();
		//store.gc();
		store.cleanupStore(key);
		//store.cleanup();
		
		
	}

}
