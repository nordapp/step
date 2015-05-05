package org.i3xx.step.due.service.impl;

/*
 * #%L
 * NordApp OfficeBase :: due
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.i3xx.test.workspace.Workspace;
import org.i3xx.util.basic.io.FilePath;
import org.junit.Ignore;


public class ZipFileTest {

	public ZipFileTest() {
	}
	
	@Ignore
	public void testZip() throws IOException {
		
		final byte[] buffer = new byte[1024];
		
		String home = Workspace.location()+("/src/test/NN");
		
		FilePath fp = FilePath.get(home);
		FilePath fx = fp.add("data/page/file/bundle");
		
		File dir = fx.add("group-1/artifact-1/resources").toFile();
		
		FileOutputStream os = new FileOutputStream(fp.add("/temp/Test.zip").toFile());
		ZipOutputStream zip = new ZipOutputStream(os);
		
		for(File file : dir.listFiles()) {
			
			String name = file.getAbsolutePath();
			int n = fx.add("group-1/artifact-1").toFile().getAbsolutePath().length();
			n = (name.charAt(n)==File.separatorChar) ? n+1 : n;
			name = name.substring(n).replace(File.separatorChar, '/');
			
			ZipEntry ze = new ZipEntry(name);
			zip.putNextEntry(ze);
			
			InputStream is = new FileInputStream(file);
			try{
				int len;
				while ((len = is.read(buffer)) > -1) {
					zip.write(buffer, 0, len);
				}
			}finally{
				is.close();
				zip.closeEntry();
			}
		}//
		
		zip.close();
		
	}

}
