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
import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.i3xx.step.uno.impl.SequencerImpl;
import org.i3xx.step.uno.impl.StepCardImpl;
import org.i3xx.step.uno.model.Sequencer;
import org.i3xx.test.workspace.Workspace;
import org.i3xx.util.basic.io.FilePath;
import org.junit.Before;
import org.junit.Test;


/**
 * Runs the test by name.
 * 
 * @author Stefan
 *
 */
public class Sequencer02Test {
	
	static FilePath RES_LOC = null;
	
	@Before
	public void setUp() throws Exception {
		
		String loc = Workspace.location().replace(File.separatorChar, '/');
		RES_LOC = FilePath.get(loc).add("/src/test/resources");
	}

	@Test
	public void test() throws IOException {
		
		String home = RES_LOC.getPath();
		
		//Log4j configuration and setup
		PropertyConfigurator.configure(home+File.separator+"Log4j.properties");
		
		
		Sequencer seq = new SequencerImpl();
		
		//
		// Test the sort and the operation add and remove.
		//
		
		seq.add( new StepCardImpl("Card 1", "fCard1", 3, 0, 5, false) ); //3 0-3
		seq.add( new StepCardImpl("Card 2", "fCard2", 0, 1, 5, false) ); //4 1-0
		seq.add( new StepCardImpl("Card 3", "fCard3", 1, 0, 5, false) ); //1 0-1
		seq.add( new StepCardImpl("Card 4", "fCard4", 2, 2, 5, false) ); //9 2-2
		seq.add( new StepCardImpl("Card 5", "fCard5", 0, 2, 5, false) ); //7 2-0
		seq.add( new StepCardImpl("Card 6", "fCard6", 2, 0, 5, false) ); //2 0-2
		seq.add( new StepCardImpl("Card 7", "fCard7", 1, 1, 5, false) ); //5 1-1
		seq.add( new StepCardImpl("Card 8", "fCard8", 2, 1, 5, false) ); //6 1-2
		seq.add( new StepCardImpl("Card 9", "fCard9", 0, 0, 5, false) ); //0 0-0
		seq.add( new StepCardImpl("Card 0", "fCard0", 1, 2, 5, false) ); //8 2-1
		
		seq.remove( seq.get(3) );
		seq.remove( seq.get(7) );
		seq.remove( seq.get(0) );
		seq.remove( seq.get(6) );
		
		seq.add( new StepCardImpl("Card 1", "fCard1", 3, 0, 5, false) );
		seq.add( new StepCardImpl("Card 0", "fCard0", 8, 2, 5, false) );
		seq.add( new StepCardImpl("Card 9", "fCard9", 0, 0, 5, false) );
		seq.add( new StepCardImpl("Card 4", "fCard4", 9, 2, 5, false) );
		
		assertEquals("Card 9", seq.get(0).getName());
		assertEquals("Card 3", seq.get(1).getName());
		assertEquals("Card 6", seq.get(2).getName());
		assertEquals("Card 1", seq.get(3).getName());
		assertEquals("Card 2", seq.get(4).getName());
		assertEquals("Card 7", seq.get(5).getName());
		assertEquals("Card 8", seq.get(6).getName());
		assertEquals("Card 5", seq.get(7).getName());
		assertEquals("Card 0", seq.get(8).getName());
		assertEquals("Card 4", seq.get(9).getName());
		
		//
		// Test the iteration
		//
		seq.reset();
		
		assertEquals("Card 9", seq.next().getName());
		assertEquals("Card 3", seq.next().getName());
		assertEquals("Card 6", seq.next().getName());
		
		seq.remove( seq.get(3) );
		seq.remove( seq.get(7) );
		
		assertEquals("Card 2", seq.next().getName());
		assertEquals("Card 7", seq.next().getName());
		
		seq.add( new StepCardImpl("Card 1", "fCard1", 3, 0, 5, false) ); //will be ignored but is there
		seq.add( new StepCardImpl("Card 0", "fCard0", 1, 2, 5, false) ); //                         |
		//                                                                                          |
		//Test with get not next (no iteration)                                                     |
		assertEquals("Card 1", seq.get(3).getName()); // <------------------------------------------
		
		assertEquals("Card 8", seq.next().getName());
		assertEquals("Card 5", seq.next().getName());
		assertEquals("Card 0", seq.next().getName());
		assertEquals("Card 4", seq.next().getName());
		
		
	}

}
