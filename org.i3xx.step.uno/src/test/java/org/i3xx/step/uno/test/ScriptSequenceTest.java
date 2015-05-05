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
import org.i3xx.step.uno.impl.ScriptCacheImpl;
import org.i3xx.step.uno.impl.ScriptEngine;
import org.i3xx.step.uno.impl.ScriptLoader;
import org.i3xx.step.uno.impl.ScriptRuntime;
import org.i3xx.step.uno.impl.SequencerImpl;
import org.i3xx.step.uno.impl.StepCardImpl;
import org.i3xx.step.uno.impl.service.builtin.ServiceFactoryService;
import org.i3xx.step.uno.model.ScriptCache;
import org.i3xx.step.uno.model.Sequencer;
import org.i3xx.step.uno.model.StepCard;
import org.i3xx.test.workspace.Workspace;
import org.i3xx.util.basic.io.CURL;
import org.i3xx.util.basic.io.FilePath;
import org.i3xx.util.store.Store;
import org.junit.Before;
import org.junit.Test;


public class ScriptSequenceTest {
	
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
		
		Store store = new Store("file:///"+RES_LOC.toString()+"/store");
		store.loadData();
		
		//
		// start of per user section
		//
		
		ScriptLoader loader = new ScriptLoader();
		
		BigInteger key = loader.getBigIntegerFromUuid(UUID.randomUUID());
		store.createStore(key);
		
		//Load store
		File dir = CURL.fileURLtoFile("file:///"+RES_LOC.toString()+"/js/card");
		loader.crawler(dir, store, key);
		loader.importf(RES_LOC.add("/js/i3xx-small.js").toFile(), store, key);
		loader.importf(RES_LOC.add("/js/test/Test-Sequence.js").toFile(), store, key);
		
		ScriptCache cache = new ScriptCacheImpl(true);
		cache.load(store, key);
		
		cache.computeDigest();
		String dA = cache.getDigest();
		String cjson = cache.toJSON();
		cache.clear();
		cache.fromJSON(cjson);
		
		// test equality
		cache.computeDigest();
		assertEquals(dA, cache.getDigest());
		
		//List<BigInteger> list = store.getStoreListing(key);
		//System.out.println("size: "+list.size());
		
		ContextImpl stepContext = new ContextImpl();
		stepContext.initializeBuiltinServices();
		stepContext.lock();
		
		ScriptEngine engine = new ScriptEngine(stepContext);
		engine.init();
		//engine.load(store, key);
		engine.load(cache);
		engine.seal();
		
		ScriptRuntime rt = engine.fork().runtime();
		Sequencer seq = new SequencerImpl();
		
		ServiceFactoryService f = (ServiceFactoryService)stepContext.getService("org.i3xx.step.uno.ServiceFactoryService");
		f.register("org.i3xx.step.uno.Sequencer", seq);
		
		seq.add(new StepCardImpl("Initialize", "init", 0));
		seq.add(new StepCardImpl("Test", "test", 0));
		
		String json = seq.toJSON();
		seq.clear();
		seq.fromJSON(json);
		
		while(seq.hasNext()){
			StepCard card = seq.next();
			
			//register the environment data 
			f.register("org.i3xx.step.runtime.current.card", card);
			
			rt.exec(card.getFunction(), new Object[]{});
			
			//unregister the environment data
			f.unregister("org.i3xx.step.runtime.current.card");
		}
		seq.reset();
		
		engine.exit();
		
		cache.clear();
			
		//
		// *** end of per user section ***
		//
		
		store.unloadData();
		store.cleanupStore(key);
		//store.gc();
		
		assertEquals( 6.0, stepContext.getValue("test.counter") );
		assertEquals( "My counter 6.0", stepContext.getValue("test.name") );
		
		
	}

}
