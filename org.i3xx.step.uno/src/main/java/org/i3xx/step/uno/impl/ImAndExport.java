package org.i3xx.step.uno.impl;

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
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.i3xx.step.uno.model.ScriptCache;
import org.i3xx.step.uno.model.Sequencer;
import org.i3xx.step.uno.model.StepCard;
import org.i3xx.util.basic.io.FilePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ImAndExport {
	
	//The logger
	static Logger logger = LoggerFactory.getLogger(ImAndExport.class);
	
	// (group-id.artifact-id)path/resource
	static Pattern pattern = Pattern.compile(".*\\(s*([A-Za-z_][A-Za-z0-9_-]+)\\.([A-Za-z_][A-Za-z0-9_-]+)s*\\)s*(.*)");
	
	//The location of the bundles
	private FilePath location;
	
	//The bundles
	private Set<String[]> imports;
	private Set<String[]> exports;
	
	private Set<String> indexNames;
	
	/**
	 * @param loc The location of the bundles
	 */
	public ImAndExport(FilePath loc) {
		this.location = loc;
		this.imports = new LinkedHashSet<String[]>();
		this.exports = new LinkedHashSet<String[]>();
		this.indexNames = new HashSet<String>();
	}
	
	/**
	 * @param sequencer The sequencer
	 * @throws IOException 
	 */
	public void analyzeCards(Sequencer sequencer) throws IOException {
		
		for(int i=0;i<sequencer.size();i++) {
			StepCard card = sequencer.get(i);
			//build an index of all cards
			indexNames.add( card.getSymbolicName() );
			
			List<String> importList = card.getImport();
			if(importList==null)
				continue;
			
			for(String s : importList){
				Matcher m = pattern.matcher(s);
				if(m.matches()) {
					String grp = m.group(1);
					String art = m.group(2);
					String res = m.group(3);
					
					imports.add(new String[]{card.getSymbolicName(), grp, art, res});
				}else{
					logger.warn("The statement '{}' is not a valid import.", s);
				}//fi
			}//for
			
		}//for
		
		
		//card.getExport();
		for(String[] arr : imports) {
			@SuppressWarnings("unused")
			String syn = arr[0];
			String grp = arr[1];
			String art = arr[2];
			String res = arr[3];
			
			FilePath loc = location.add("bin/bundle").add(grp).add(art).add(res);
			File file = loc.toFile();
			
			if(file.exists()) {
				if(res.toLowerCase().endsWith("mf")){
					String manifest = readFile(file);
					Map<String, String> props = sequencer.readManifest(manifest);
					String symbolicName = props.get("card-symbolicname");
					String export = props.get("card-export");
					
					List<String> exportList = StepCardImpl.reel(export);
					if(exportList==null)
						continue;
					
					for(String s : exportList){
						Matcher m = pattern.matcher(s);
						if(m.matches()) {
							grp = m.group(1);
							art = m.group(2);
							res = m.group(3);
							
							exports.add(new String[]{symbolicName, grp, art, res});
						}else{
							logger.warn("The statement '{}' is not a valid export.", s);
						}//fi
					}//for
				}//fi
			}//fi
			else{
				logger.warn("The requested export group-id:{}, artifact-id:{}, path:{} doesn't exist. (file:{})",
						grp, art, res, file.getAbsolutePath());
			}//fi
		}//for
	}
	
	/**
	 * @param sequencer The sequencer to load a manifest file to
	 * @param cache The cache load a script file to
	 * @throws IOException
	 */
	public void load(Sequencer sequencer, ScriptCache cache) throws IOException {
		
		List<File> fileList = new ArrayList<File>();
		List<String> nameList = new ArrayList<String>();
		
		Set<String> permission = new HashSet<String>();
		
		for(String[] arr : exports) {
			@SuppressWarnings("unused")
			String syn = arr[0];
			String grp = arr[1];
			String art = arr[2];
			String res = arr[3];
			
			permission.add(grp+"-"+art+"-"+res);
		}		
		
		for(String[] arr : imports) {
			@SuppressWarnings("unused")
			String syn = arr[0];
			String grp = arr[1];
			String art = arr[2];
			String res = arr[3];
			
			FilePath loc = location.add("bin/bundle").add(grp).add(art).add(res);
			File file = loc.toFile();
			
			if( ! permission.contains(grp+"-"+art+"-"+res)) {
				logger.info("No permission to load the import group-id:{}, artifact-id:{}, path:{}. (file:{})",
						grp, art, res, file.getAbsolutePath());
			}else
			if(file.exists()) {
				if(res.toLowerCase().endsWith("js")){
					fileList.add(file);
					nameList.add(res);
				}else
				if(res.toLowerCase().endsWith("mf")){
					String manifest = readFile(file);
					Map<String, String> props = sequencer.readManifest(manifest);
					String symbolicName = props.get("card-symbolicname");
					//Test whether the manifest has been loaded before
					if(symbolicName!=null && indexNames.contains(symbolicName)) {
						//added before
					}
					else{
						sequencer.addManifest(manifest);
						indexNames.add(symbolicName);
					}//fi
				}//fi
			}
			else{
				logger.warn("The requested import group-id:{}, artifact-id:{}, path:{} doesn't exist. (file:{})",
						grp, art, res, file.getAbsolutePath());
			}//fi
		}//for
		
		File[] files = fileList.toArray(new File[fileList.size()]);
		String[] names = nameList.toArray(new String[nameList.size()]);
		cache.load(files, names);
	}
	
	/**
	 * @param file
	 * @return The content of the file
	 * @throws IOException
	 */
	private String readFile(File file) throws IOException {
		
		StringWriter out = new StringWriter();
		FileReader in = new FileReader(file);
		
		int c = 0;
		char[] cbuf = new char[512];
		
		try{
			while((c=in.read(cbuf))>0)
				out.write(cbuf, 0, c);
			
		}finally{
			in.close();
			out.close();
		}
		
		return out.toString();
	}
}
