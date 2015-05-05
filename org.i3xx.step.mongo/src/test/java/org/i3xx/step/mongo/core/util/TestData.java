package org.i3xx.step.mongo.core.util;

/*
 * #%L
 * NordApp OfficeBase :: mongo
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
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Test data by 'http://migano.de/testdaten.php', thanks.
 * 
 * @author Stefan
 *
 */
public class TestData {
	
	final List<String[]> data;
	
	public TestData(String home) throws IOException {
		data = new ArrayList<String[]>();
		
		File file = new File(home);
		LineNumberReader in = new LineNumberReader(new FileReader(file));
		
		try{
			//headline
			String line = null;
			while((line = in.readLine()) != null) {
				String[] p = line.split("\\;");
				
				data.add(p);
			}//while
		}finally{
			in.close();
		}
		
		//the fields must match some rules
		String[] h = data.get(0);
		for(int i=0;i<h.length;i++) {
			//allow a-z A-Z 0-9 _ -
			h[i] = h[i].replaceAll("[^a-zA-Z0-9_-]", "");
		
		}//for
		data.set(0, h);
	}
	
	public List<String[]> getData() { return data; }

}
