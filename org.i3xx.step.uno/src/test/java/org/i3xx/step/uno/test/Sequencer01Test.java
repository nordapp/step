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
 * Runs the test by order.
 * 
 * @author Stefan
 *
 */
public class Sequencer01Test {
	
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
		
		seq.add( new StepCardImpl("Card 1", "fCard1", 3) );
		seq.add( new StepCardImpl("Card 2", "fCard2", 4) );
		seq.add( new StepCardImpl("Card 3", "fCard3", 1) );
		seq.add( new StepCardImpl("Card 4", "fCard4", 9) );
		seq.add( new StepCardImpl("Card 5", "fCard5", 7) );
		seq.add( new StepCardImpl("Card 6", "fCard6", 2) );
		seq.add( new StepCardImpl("Card 7", "fCard7", 5) );
		seq.add( new StepCardImpl("Card 8", "fCard8", 6) );
		seq.add( new StepCardImpl("Card 9", "fCard9", 0) );
		seq.add( new StepCardImpl("Card 0", "fCard0", 8) );
		
		seq.remove( seq.get(3) );
		seq.remove( seq.get(7) );
		seq.remove( seq.get(0) );
		seq.remove( seq.get(6) );
		
		seq.add( new StepCardImpl("Card 1", "fCard1", 3) );
		seq.add( new StepCardImpl("Card 0", "fCard0", 8) );
		seq.add( new StepCardImpl("Card 9", "fCard9", 0) );
		seq.add( new StepCardImpl("Card 4", "fCard4", 9) );
		
		assertEquals(0, seq.get(0).getOrder());
		assertEquals(1, seq.get(1).getOrder());
		assertEquals(2, seq.get(2).getOrder());
		assertEquals(3, seq.get(3).getOrder());
		assertEquals(4, seq.get(4).getOrder());
		assertEquals(5, seq.get(5).getOrder());
		assertEquals(6, seq.get(6).getOrder());
		assertEquals(7, seq.get(7).getOrder());
		assertEquals(8, seq.get(8).getOrder());
		assertEquals(9, seq.get(9).getOrder());
		
		//
		// Test the iteration
		//
		seq.reset();
		
		assertEquals(0, seq.next().getOrder());
		assertEquals(1, seq.next().getOrder());
		assertEquals(2, seq.next().getOrder());
		
		seq.remove( seq.get(3) );
		seq.remove( seq.get(7) );
		
		assertEquals(4, seq.next().getOrder());
		assertEquals(5, seq.next().getOrder());
		
		seq.add( new StepCardImpl("Card 1", "fCard1", 3) ); //will be ignored but is there
		seq.add( new StepCardImpl("Card 0", "fCard0", 8) ); //                         |
		//                                                                             |
		//Test with get not next (no iteration)                                        |
		assertEquals(3, seq.get(3).getOrder()); // <-----------------------------------
		
		assertEquals(6, seq.next().getOrder());
		assertEquals(7, seq.next().getOrder());
		assertEquals(8, seq.next().getOrder());
		assertEquals(9, seq.next().getOrder());
		
		
	}

}
