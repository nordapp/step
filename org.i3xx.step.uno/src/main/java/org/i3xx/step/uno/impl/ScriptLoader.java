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
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.i3xx.util.store.Store;
import org.i3xx.util.store.StoreEntry;

/**
 * The loader adds one or more scripts into the store. The store to be used is
 * identified by it's key.
 * 
 * @author Stefan
 * @since 19.11.2014
 */
public class ScriptLoader {
	
	private static Logger logger = Logger.getLogger(ScriptLoader.class);
	
	private static volatile long counter = 0;
	
	public ScriptLoader() {
	}
	
	/**
	 * Load all scripts of the directory into the store
	 * 
	 * @param dir The source directory to load the scripts from.
	 * @param store The store is the target to load the scripts.
	 * @param key The key identifies the store to be used.
	 */
	public void crawler(File dir, Store store, BigInteger key) {
		crawler(dir, store, key, null);
	}
	
	/**
	 * Load all scripts of the directory into the store
	 * 
	 * @param dir The source directory to load the scripts from.
	 * @param store The store is the target to load the scripts.
	 * @param key The key identifies the store to be used.
	 * @param suffix The suffix of the files to load (optional)
	 */
	public void crawler(File dir, Store store, BigInteger key, String suffix) {
		//
		// Load files first than search directories
		//
		
		final String suf = suffix;
		FileFilter ff = new FileFilter() {
			/* The filter */
			public boolean accept(File file) {
				if(file.isDirectory())
					return true;
				
				if(suf==null || suf.equals("") || suf.equals("*"))
					return true;
				
				if( getSuffix(file).equals(suf) )
					return true;
				
				return false;
			}
			
		};
		
		List<File> list = new ArrayList<File>();
		for(File f : dir.listFiles(ff)) {
			if(f.isDirectory()){
				list.add(f);
			}else if(f.isFile()){
				importf(f, store, key);
			}//fi
		}//for
		
		for(File f : list){
			crawler(f, store, key, suffix);
		}//for
	}
	
	/**
	 * Loads a single script into the store.
	 * 
	 * @param file The source file of the scripts.
	 * @param store The store is the target to load the script.
	 * @param key The key identifies the store to be used.
	 */
	public void importf(File file, Store store, BigInteger key) {
		
		//
		// do not throw anything to avoid breaking the load process
		// use logging instead.
		//
		if(logger.isDebugEnabled()){
			logger.debug("Loading the file '"+file.getAbsolutePath()+"' at store '"+key.toString()+"' starts.");
		}
		
		try{
			/* The data is stored ever, but a cleanup() will clean it. */
			StoreEntry entry = store.nextEntry(key, 0, true, new Long(counter++));
			entry.setLifetime(Long.MAX_VALUE);
			//
			OutputStream out = store.write(entry);
			InputStream in = new FileInputStream(file);
			int c = 0;
			byte[] buf = new byte[1024];
			try{
				while((c=in.read(buf))>-1)
					out.write(buf, 0, c);
			}finally{ 
				try{
					out.close();
				}finally{
					in.close();
				}//try
			}//try
		}catch(Exception e){
			logger.error("Loading the script '"+file.getAbsolutePath()+"' fails.", e);
		}
	}
	
	/** Converts a UUID into a BigInteger */
	public BigInteger getBigIntegerFromUuid(UUID randomUUID) {
		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		bb.putLong(randomUUID.getMostSignificantBits());
		bb.putLong(randomUUID.getLeastSignificantBits());
		return new BigInteger(bb.array());
	}
	
	/**
	 * @param file The file
	 * @return The suffix
	 */
	private String getSuffix(File file) {
		if(file==null)
			return "";
		
		String name = file.getName();
		int p = name.lastIndexOf('.');
		return ( p<0 ? "" : name.substring(p+1).toLowerCase() );
	}

}
