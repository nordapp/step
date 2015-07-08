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


import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.i3xx.step.uno.model.ScriptCache;
import org.i3xx.util.store.Store;

/**
 * Resolves the order of script loading.
 * 
 * The class takes a look into the script sources and filters out the following
 * information.
 * 
 * The feature name of a script can be set by:
 * 
 *     i3xx.feature("<the-name-of-the-feature>");
 * 
 * A required feature can be requested before loading:
 * 
 *     i3xx.require("<the-name-of-the-feature>");
 * 
 * The i3xx.js or i3xx-small.js must be annotated in the first comment,
 * because the feature command is not available there:
 * 
 *     &#064;feature = <the-name-of-the-feature>
 *     
 * The resolver take a look into the source and try to ensure every requested
 * feature to be loaded before its use. If the resolving is not possible the
 * return code is set to false.
 * 
 * The preferred order of loading the scripts is available by the resolved
 * list. The unresolved list contains the remaining data not to be resolved.
 * 
 * @author Stefan
 *
 */
public class ScriptResolver {
	
	private static final int NO_INDEX = -1;
	
	/*
	
	'i3xx'    ::= i3xx
	'feature' ::= feature
	'require' ::= require
	'.'       ::= \\.
	'('       ::= \\(
	S         ::= \\s*
	'"'       ::= [\\\"|']
	package   ::= ([A-Za-z0-9\\.\\-_]*)
	')'       ::= \\)
	';'       ::= ;
	
	*/
	
	// 'i3xx' '.' 'feature' '(' S '"' package '"' S ')' ';'
	private static final Pattern FEATURE = Pattern.compile("i3xx\\.feature\\(\\s*[\\\"|']([A-Za-z0-9\\.\\-_]*)[\\\"|']\\s*\\)\\s*;.*");
	// 'i3xx' '.' 'require' '(' S '"' package '"' S ')' ';'
	private static final Pattern REQUIRE = Pattern.compile("i3xx\\.require\\(\\s*[\\\"|']([A-Za-z0-9\\.\\-_]*)[\\\"|']\\s*\\)\\s*;.*");
	//
	private static final Pattern COMMENT = Pattern.compile("\\s*\\*\\s*\\@feature\\s*\\=\\s*([A-Za-z0-9\\.\\-_]*).*");
	//
	private static final Pattern SOURCES = Pattern.compile("\\w{3,}.*");
	
	//The available scripts
	private List<Entry> scripts;
	
	//The resolved scripts
	private List<Entry> resolved;
	
	//The resolved scripts
	private List<Entry> unresolved;
	
	public ScriptResolver() {
		scripts = new ArrayList<Entry>();
		resolved = new ArrayList<Entry>();
		unresolved = new ArrayList<Entry>();
	}
	
	/**
	 * Loads the scripts from the store into the resolver to get the load sequence.
	 * 
	 * @param store
	 * @param key
	 * @throws IOException
	 */
	public void load(Store store, BigInteger key) throws IOException {
		
		//
		// Gets a sorted map of the keys. The map is in the order
		// of the sort objects (not in key order) which are long
		// values (set by the ScriptLoader).
		//
		Map<Object, BigInteger> map = store.getSortMap(key);
		List<BigInteger> list = new ArrayList<BigInteger>();
		Iterator<Object> iter = map.keySet().iterator();
		while(iter.hasNext())
			list.add(map.get(iter.next()));
		
		for(BigInteger id : list) {
			String name = id.toString();
			String source = store.readString(key, id);
			
			parse(name, source, NO_INDEX);
		}//for
	}
	
	/**
	 * Loads the scripts from the cache into the resolver to get the load sequence.
	 * @param cache
	 * @throws IOException
	 */
	public void load(ScriptCache cache) throws IOException {
		
		for(int i=0;i<cache.size();i++) {
			String name = cache.getName(i);
			String source = cache.read(i);
			
			parse(name, source, i);
		}//for
	}
	
	/**
	 * Parses the source and put entries into the script list.
	 * 
	 * @param name The name of the script (in common the key of the store)
	 * @param source The script source
	 * @param index The index (used by the cache)
	 * @throws IOException
	 */
	public void parse(String name, String source, int index) throws IOException {
		
		Entry entry = new EntryImpl();
		entry.setOrder(index);
		entry.setName(name);
		
		scripts.add(entry);
		
		LineNumberReader r = new LineNumberReader(new StringReader(source));
		String line = null;
		boolean comment = false;
		while((line=r.readLine())!=null){
			line = line.trim();
			
			// comment
			if(comment){
				if(line.endsWith("*/"))
					comment = false;
				
				//Set feature by annotation in comment; for i3xx.i3xx
				Matcher m = COMMENT.matcher(line);
				if(m.matches())
					if(entry.getFeature()==null)
						entry.setFeature( m.group(1) );
				
				continue;
			}
			if(line.startsWith("/*")){
				if(line.endsWith("*/"))
					continue;
				comment = true;
				continue;
			}
			if(line.equals(""))
				continue;
			if(line.startsWith("//"))
				continue;
			
			Matcher m = FEATURE.matcher(line);
			if(m.matches()){
				if(entry.getFeature()==null)
					entry.setFeature( m.group(1) );
				continue;
			}
			m = REQUIRE.matcher(line);
			if(m.matches()){
				entry.addRequire( m.group(1) );
				continue;
			}
			
			//
			m = SOURCES.matcher(line);
			if(m.matches()){
				break;
			}
		}
		r.close();
	}
	
	/**
	 * The resolving process. Resolves the order of script loading.
	 * 
	 * @return True if every script could be resolved.
	 */
	public boolean resolve() {
		final int test = 20;
		
		List<Entry> resolved = new ArrayList<Entry>();
		List<Entry> work = new ArrayList<Entry>();
		Set<String> features = new HashSet<String>();
		
		work.addAll(scripts);
		
		//try one more time
		for(int k=0;k<test&&work.size()>0;k++){
			//to resolve the list
			for(int i=0;i<work.size();i++) {
				Entry e = work.remove(0);
				
				boolean f = true;
				for(String s : e.getRequire()){
					f = features.contains(s) ? f : false;
				}
				if(f){
					features.add(e.getFeature());
					resolved.add(e);
					continue;
				}
				work.add(e);
			}//for
		}//for
		
		if(work.size()>0){
			//unresolved
			this.unresolved.addAll( resolved );
			return false;
		}
		
		this.resolved.addAll( resolved );
		return true;
	}

	/**
	 * @return the scripts
	 */
	public List<Entry> getScripts() {
		return scripts;
	}

	/**
	 * @return the resolved
	 */
	public List<Entry> getResolved() {
		return resolved;
	}

	/**
	 * @return the unresolved
	 */
	public List<Entry> getUnresolved() {
		return unresolved;
	}
	
	/**
	 * Resets the resolver
	 */
	public void reset() {
		scripts.clear();
		resolved.clear();
		unresolved.clear();
	}

	public static interface Entry {
		/**
		 * @return the order
		 */
		int getOrder();
		/**
		 * @param order the order to set
		 */
		void setOrder(int order);
		/**
		 * @return the name
		 */
		String getName();
		/**
		 * @param name the name to set
		 */
		void setName(String name);
		/**
		 * @return the feature
		 */
		String getFeature();
		/**
		 * @param feature the feature to set
		 */
		void setFeature(String feature);
		/**
		 * @return the require
		 */
		List<String> getRequire();
		/**
		 * @param require the require to set
		 */
		void addRequire(String require);
		
	}/*INTERFACE*/
	
	private static class EntryImpl implements Entry {
		
		int order;
		String name;
		String feature;
		List<String> require;
		
		public EntryImpl() {
			order = 0;
			name = null;
			feature = null;
			require = new ArrayList<String>();
		}
		
		/**
		 * @return the order
		 */
		public int getOrder() {
			return order;
		}
		/**
		 * @param order the order to set
		 */
		public void setOrder(int order) {
			this.order = order;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the feature
		 */
		public String getFeature() {
			return feature;
		}
		/**
		 * @param feature the feature to set
		 */
		public void setFeature(String feature) {
			this.feature = feature;
		}
		/**
		 * @return the require
		 */
		public List<String> getRequire() {
			return require;
		}
		/**
		 * @param require the require to set
		 */
		public void addRequire(String require) {
			this.require.add(require);
		}
		
		public String toString() {
			return "<"+order+"|"+name+"|"+feature+">";
		}
		
	}/*CLASS*/
}
