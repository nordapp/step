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


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.i3xx.test.workspace.Workspace;
import org.i3xx.util.basic.io.FilePath;
import org.junit.Before;
import org.junit.Test;


public class ResourceServiceTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testA1() throws IOException {
		
		String home = Workspace.location()+("/src/test/LL".replace('/', File.separatorChar));
		
		FilePath fp = FilePath.get(home);
		File fx = fp.add("data").toFile();
		File fy = new File(fx.getParentFile(), "~"+fx.getName());
		
		String mandatorPath = home;
		String processId = "id1234";
		String groupId = "group-1";
		String artifactId = "artifact-1";
		
		DeployTool deploy = new DeployTool();
		long crc1 = deploy.processCRC(mandatorPath, processId, groupId, artifactId);
		//deploy.createEmptyZipA(mandatorPath, processId, groupId,artifactId);
		File zip = deploy.zipFromDataA(mandatorPath, processId, groupId, artifactId);
		
		//rename directory /data to /~data
		fx.renameTo(fy);
		String zipFileName = deploy.getLatestZipFile(mandatorPath, processId);
		deploy.zipToDataA(mandatorPath, processId, groupId, artifactId, zipFileName);
		long crc2 = deploy.processCRC(mandatorPath, processId, groupId, artifactId);
		
		killDir(fx);
		fy.renameTo(fx);
		deploy.cleanResultZip(zip.getParentFile(), 1);
		//zip.delete();
		
		assertEquals( crc1, crc2);
	}

	@Test
	public void testA2() throws IOException {
		
		String home = Workspace.location()+("/src/test/MM".replace('/', File.separatorChar));
		
		FilePath fp = FilePath.get(home);
		File fx = fp.add("data").toFile();
		File fy = new File(fx.getParentFile(), "~"+fx.getName());
		
		String mandatorPath = home;
		String processId = "id1234";
		String groupId = "group-1";
		String artifactId = "artifact-1";
		
		DeployTool deploy = new DeployTool();
		long crc1 = deploy.processCRC(mandatorPath, processId, groupId, artifactId);
		//deploy.createEmptyZipA(mandatorPath, processId, groupId,artifactId);
		File zip = deploy.zipFromDataA(mandatorPath, processId, groupId, artifactId);
		
		//rename directory /data to /~data
		fx.renameTo(fy);
		String zipFileName = deploy.getLatestZipFile(mandatorPath, processId);
		deploy.zipToDataA(mandatorPath, processId, groupId, artifactId, zipFileName);
		long crc2 = deploy.processCRC(mandatorPath, processId, groupId, artifactId);
		
		killDir(fx);
		fy.renameTo(fx);
		deploy.cleanResultZip(zip.getParentFile(), 1);
		//zip.delete();
		
		assertEquals( crc1, crc2);
	}

	@Test
	public void testA3() throws IOException {
		
		String home = Workspace.location()+("/src/test/NN".replace('/', File.separatorChar));
		
		FilePath fp = FilePath.get(home);
		File fx = fp.add("data").toFile();
		File fy = new File(fx.getParentFile(), "~"+fx.getName());
		
		String mandatorPath = home;
		String processId = "id1234";
		String groupId = "group-1";
		String artifactId = "artifact-1";
		
		DeployTool deploy = new DeployTool();
		long crc1 = deploy.processCRC(mandatorPath, processId, groupId, artifactId);
		//deploy.createEmptyZipA(mandatorPath, processId, groupId,artifactId);
		File zip = deploy.zipFromDataA(mandatorPath, processId, groupId, artifactId);
		
		//rename directory /data to /~data
		fx.renameTo(fy);
		String zipFileName = deploy.getLatestZipFile(mandatorPath, processId);
		deploy.zipToDataA(mandatorPath, processId, groupId, artifactId, zipFileName);
		long crc2 = deploy.processCRC(mandatorPath, processId, groupId, artifactId);
		
		killDir(fx);
		fy.renameTo(fx);
		deploy.cleanResultZip(zip.getParentFile(), 1);
		//zip.delete();
		
		assertEquals( crc1, crc2);
	}
	
	/**
	 * Deletes a file or directory
	 * 
	 * @param file The file or directory to delete
	 */
	private void killDir(File file) {
		
		//recursion
		if(file.isDirectory()){
			for(File f : file.listFiles()) {
				killDir(f);
			}//for
		}//fi
		
		file.delete();
	}

}
