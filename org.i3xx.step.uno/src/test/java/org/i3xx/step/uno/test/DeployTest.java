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


import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import org.i3xx.step.uno.impl.ScriptLoader;
import org.i3xx.test.workspace.Workspace;
import org.i3xx.util.basic.io.CURL;
import org.i3xx.util.basic.io.FilePath;
import org.i3xx.util.store.Store;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests the scripts of '/src/test/resources/js' and loads them into
 * the store '/src/test/resources/store'.
 * 
 * The size must be '10'. The store must be empty after the test.
 * 
 * Needs the '-Dworkspace.home=${workspace_loc:org.i3xx.step.uno}' parameter.
 * 
 * @author Stefan
 *
 */
public class DeployTest {
	
	static FilePath RES_LOC = null;
	
	@Before
	public void setUp() throws Exception {
		
		String loc = Workspace.location().replace(File.separatorChar, '/');
		RES_LOC = FilePath.get(loc).add("/src/test/resources");
	}	
	
	@Test
	public void test() throws IOException, URISyntaxException {
		
		String home = RES_LOC.getPath();
		System.out.println(home);
		
		Store store = new Store("file:///"+home+"/store");
		store.loadData();
		
		ScriptLoader loader = new ScriptLoader();
		
		BigInteger key = loader.getBigIntegerFromUuid(UUID.randomUUID());
		store.createStore(key);
		
		//Load store
		File rt = CURL.fileURLtoFile("file:///"+home+"/js");
		loader.crawler(rt, store, key);
		
		/*
		 * TODO:
		 * 
		 * Implement the deployment to run the scripts
		 */
		List<BigInteger> list = store.getStoreListing(key);
		System.out.println("size: "+list.size());
		
		store.unloadData();
		store.cleanupStore(key);
		//store.gc();
		
	}

}
