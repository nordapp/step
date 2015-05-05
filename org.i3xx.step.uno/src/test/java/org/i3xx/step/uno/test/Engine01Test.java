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
import java.math.BigInteger;

import org.apache.log4j.PropertyConfigurator;
import org.i3xx.step.uno.impl.daemon.EngineBase;
import org.i3xx.step.uno.impl.daemon.EngineImpl;
import org.i3xx.step.uno.impl.service.builtin.ContextAdministrationService;
import org.i3xx.step.uno.impl.service.builtin.ContextServiceAdapter;
import org.i3xx.step.uno.model.daemon.Engine;
import org.i3xx.test.workspace.Workspace;
import org.i3xx.util.basic.io.FilePath;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class Engine01Test {
	
	static FilePath RES_LOC = null;
	
	@Before
	public void setUp() throws Exception {
		
		String loc = Workspace.location().replace(File.separatorChar, '/');
		RES_LOC = FilePath.get(loc).add("/src/test/resources");
		
		PropertyConfigurator.configure(RES_LOC.toString()+File.separator+"Log4j.properties");
	}
	
	@Ignore
	public void ensure() throws Exception {
		
		ensureLocation( RES_LOC.add("test-base").toFile() );
		ensureLocation( RES_LOC.add("test-base").add("/bin/main/js").toFile() );
		ensureLocation( RES_LOC.add("test-base").add("/bin/group-1/artifact-1/js").toFile() );
		ensureLocation( RES_LOC.add("test-base").add("/bin/group-1/artifact-2/js").toFile() );
		
	}	
	
	@Test
	public void test() throws Exception {
		
		BigInteger engineId = BigInteger.valueOf(1);
		
		EngineBase base = new EngineBase( RES_LOC.add("test-base") );
		base.init();
		
		BigInteger bundle1 = base.addBundle("group-1", "artifact-1");
		BigInteger bundle2 = base.addBundle("group-1", "artifact-2");
		
		//load the executable
		base.clean(engineId);
		base.load(engineId);
		base.load(engineId, bundle1);
		base.load(engineId, bundle2);
		
		// Usage: loadExecutables(BigInteger), init() OR load()
		//
		Engine impl = base.createEngine(engineId);
		impl.loadExecutables(engineId);
		impl.init();
		//load or loadExecutables, init
		//impl.load();
		
		//The cards must be loaded into the sequencer to run
		
		//TODO:
		// Add access to the engine
		// Add serialization to the engine
		// Add serialization to the context
		
		impl.start();
		
		impl.reinit();
		impl.start();
		
		ContextServiceAdapter s = (ContextServiceAdapter)((EngineImpl)impl).getContext().getService("org.i3xx.step.uno.ContextService");
		System.out.println( s.get("test.counter") );
		
		ContextAdministrationService a = (ContextAdministrationService)((EngineImpl)impl).getContext().getService("org.i3xx.step.uno.ContextAdministrationService");
		String stmt = a.toJSON();
		System.out.println( stmt );
		a.fromJSON(stmt);
		System.out.println( a.toJSON() );
		
		impl.call("group-1.artifact-1.Step-1");
		
		impl.exit();
	}
	
	/*  */
	private void ensureLocation(File loc) {
		
		//ensure the location
		if( ! loc.exists() ){
			loc.mkdirs();
		}
	}

}
