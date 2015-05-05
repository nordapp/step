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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.PropertyConfigurator;
import org.i3xx.step.uno.impl.ScriptCacheImpl;
import org.i3xx.step.uno.impl.ScriptLoader;
import org.i3xx.step.uno.impl.ScriptResolver;
import org.i3xx.step.uno.model.ScriptCache;
import org.i3xx.test.workspace.Workspace;
import org.i3xx.util.basic.io.FilePath;
import org.i3xx.util.store.Store;
import org.junit.Before;
import org.junit.Test;


public class ScriptResolverTest {
	
	static FilePath RES_LOC = null;
	
	@Before
	public void setUp() throws Exception {
		
		String loc = Workspace.location().replace(File.separatorChar, '/');
		RES_LOC = FilePath.get(loc).add("/src/test/resources");
	}

	@Test
	public void test() throws Exception {
		
		String home = RES_LOC.getPath();
		
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
		
		//Load scripts to the store
		loader.importf(RES_LOC.add("/js/card/card-0.js").toFile(), store, key);
		loader.importf(RES_LOC.add("/js/card/card-1.js").toFile(), store, key);
		loader.importf(RES_LOC.add("/js/card/card-2.js").toFile(), store, key);
		loader.importf(RES_LOC.add("/js/card/card-3.js").toFile(), store, key);
		loader.importf(RES_LOC.add("/js/card/card-4.js").toFile(), store, key);
		loader.importf(RES_LOC.add("/js/i3xx-small.js").toFile(), store, key);
		loader.importf(RES_LOC.add("/js/test/Test-Sequence.js").toFile(), store, key);
		
		//Cache scripts
		ScriptCache cache = new ScriptCacheImpl(true);
		cache.load(store, key);
		
		assertEquals(cache.size(), 7);
		
		//Get load sequence
		ScriptResolver resolver = new ScriptResolver();
		resolver.load(cache);
		boolean f = resolver.resolve();
		
		assertTrue(f);
		
		List<ScriptResolver.Entry> list = resolver.getResolved();
		assertEquals( list.get(0).toString(), "<5|6|i3xx.i3xx>");
		assertEquals( list.get(1).toString(), "<6|7|i3xx.test.Test-Sequence>");
		assertEquals( list.get(2).toString(), "<0|1|i3xx.test.card-0>");
		assertEquals( list.get(3).toString(), "<1|2|i3xx.test.card-1>");
		assertEquals( list.get(4).toString(), "<2|3|i3xx.test.card-2>");
		assertEquals( list.get(5).toString(), "<3|4|i3xx.test.card-3>");
		assertEquals( list.get(6).toString(), "<4|5|i3xx.test.card-4>");
		
		resolver.reset();
		cache.clear();
			
		//
		// *** end of per user section ***
		//
		
		store.unloadData();
		store.cleanupStore(key);
		//store.gc();
		
	}

}
