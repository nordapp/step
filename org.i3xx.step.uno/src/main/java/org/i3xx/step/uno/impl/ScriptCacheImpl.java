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


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.i3xx.step.uno.impl.util.GsonHashMapDeserializer;
import org.i3xx.step.uno.model.ScriptCache;
import org.i3xx.util.store.Store;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ScriptCacheImpl implements ScriptCache {
	
	private String digest;
	private boolean compress;
	private String[] names;
	private byte[][] buffer;
	
	/**
	 * @param c True to use GZIP compression, false otherwise
	 */
	public ScriptCacheImpl(boolean c) {
		digest = null;
		compress = c;
		names = null;
		buffer = null;
	}
	
	/**
	 * @return The number of scripts in the buffer
	 */
	public int size() {
		return buffer==null ? -1 : buffer.length;
	}
	
	/**
	 * @return The size of the buffer
	 */
	public long sizeInBytes() {
		if(buffer==null)
			return 0;
		
		long size = 0;
		for(int i=0;i<buffer.length;i++)
			size += buffer[i].length;
			
		return size;
	}
	
	/**
	 * Gets a digest to verify integrity
	 * 
	 * @return The digest
	 */
	public String getDigest() {
		return digest;
	}
	
	/**
	 * Computes a MD5-Digest to verify integrity
	 * 
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 */
	public void computeDigest() throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		for(int i=0;i<buffer.length;i++){
			ByteArrayInputStream in = new ByteArrayInputStream(buffer[i]);
			byte[] buf = new byte[1024];
			int c = 0;
			
			while((c=in.read(buf))>-1)
				md.update(buf, 0, c);
		}//for
		
		byte[] dig = md.digest();
		digest = new String(Hex.encodeHex(dig));
	}
	
	/**
	 * Load scripts to the cache
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
	    
		names = new String[list.size()];
		buffer = new byte[list.size()][];
		
		int c = 0;
		byte[] buf = new byte[1024];
		
		for(int i=0;i<list.size();i++) {
			BigInteger id = list.get(i);
			names[i] = id.toString();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			OutputStream out = compress ? new GZIPOutputStream(bout): bout;
			InputStream in = store.read(key, id);
			try{
				while((c=in.read(buf))>-1)
					out.write(buf, 0, c);
			}finally{
				out.flush();
				out.close();
				in.close();
			}
			buffer[i] = bout.toByteArray();
		}//for
	}	
	
	/**
	 * Load scripts to the cache
	 * 
	 * @param files The files to load
	 * @param names The names of the files
	 * @throws IOException
	 */
	public void load(File[] files, String[] names) throws IOException {
		//
		// Gets a sorted map of the keys. The map is in the order
		// of the sort objects (not in key order) which are long
		// values (set by the ScriptLoader).
		//
	    
		this.buffer = new byte[files.length][];
		this.names = names;
		
		int c = 0;
		byte[] buf = new byte[1024];
		
		for(int i=0;i<files.length;i++) {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			OutputStream out = compress ? new GZIPOutputStream(bout): bout;
			InputStream in = new FileInputStream(files[i]);
			try{
				while((c=in.read(buf))>-1)
					out.write(buf, 0, c);
			}finally{
				out.flush();
				out.close();
				in.close();
			}
			buffer[i] = bout.toByteArray();
		}//for
	}	
	
	/**
	 * Returns the name of an element of the buffer
	 * 
	 * @param index The index of the cached element
	 * @return The name
	 */
	public String getName(int index) {
		return names[index];
	}
	
	/**
	 * Reads a script from the cache
	 * 
	 * @param index The index of the cached element
	 * @return The String read
	 * @throws IOException
	 */
	public String read(int index) throws IOException {
		ByteArrayInputStream bin = new ByteArrayInputStream( buffer[index] );
		InputStream in = compress ? new GZIPInputStream(bin) : bin;
		InputStreamReader r = new InputStreamReader(in);
		StringWriter w = new StringWriter();
		
		char[] cbuf = new char[1024];
		int c = 0;
		
		while((c=r.read(cbuf))>-1)
			w.write(cbuf, 0, c);
		
		r.close();
		w.close();
		
		return w.toString();
	}
	
	/**
	 * Serializes the content to JSON
	 * 
	 * @return The JSON String
	 */
	public String toJSON() {
		Gson gson = new Gson();
		StringBuffer buf = new StringBuffer();
		
		buf.append( '{' );
		
		buf.append( gson.toJson("digest") );
		buf.append( ':' );
		buf.append( gson.toJson(digest) );
		buf.append( ',' );
		
		buf.append( gson.toJson("compress") );
		buf.append( ':' );
		buf.append( gson.toJson(Boolean.valueOf(compress)) );
		buf.append( ',' );
		
		buf.append( gson.toJson("names") );
		buf.append( ':' );
		buf.append( '[' );
		for(int i=0;i<names.length;i++){
			if(i>0)
				buf.append( ',' );
			
			buf.append( gson.toJson(names[i]) );
		}
		buf.append( ']' );
		buf.append( ',' );
		
		buf.append( gson.toJson("buffer") );
		buf.append( ':' );
		buf.append( '[' );
		for(int i=0;i<buffer.length;i++){
			if(i>0)
				buf.append( ',' );
			
			String stmt = Base64.encodeBase64URLSafeString(buffer[i]);
			buf.append( gson.toJson(stmt) );
		}
		buf.append( ']' );
		
		buf.append( '}' );
		
		return buf.toString();
	}
	
	/**
	 * Deserializes the content from JSON
	 * 
	 * @param json The JSON String
	 */
	public void fromJSON(String json) {
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(LinkedHashMap.class, new GsonHashMapDeserializer());
		Gson gson = gsonBuilder.create();
		
		LinkedHashMap<?, ?> m = gson.fromJson(json, LinkedHashMap.class);
		digest = ((String)m.get("digest"));
		compress = ((Boolean)m.get("compress")).booleanValue();
		
		List<?> ls = (List<?>)m.get("names");
		names = new String[ls.size()];
		for(int i=0;i<ls.size();i++){
			names[i] = (String)ls.get(i);
		}
		
		List<?> ll = (List<?>)m.get("buffer");
		buffer = new byte[ll.size()][];
		for(int i=0;i<ll.size();i++){
			String stmt = (String)ll.get(i);
			buffer[i] = Base64.decodeBase64( stmt );
		}
	}
	
	/**
	 * Clears the cache
	 */
	public void clear() {
		names = null;
		buffer = null;
	}
	
	/**
	 * Appends the caches
	 * 
	 * @param cache
	 */
	public void append(ScriptCache cache) {
		ScriptCacheImpl c = (ScriptCacheImpl)cache;
		
		String[] n = new String[names.length+c.names.length];
		System.arraycopy(names, 0, n, 0, names.length);
		System.arraycopy(c.names, 0, n, names.length, c.names.length);
		names = n;
		
		byte[][] b = new byte[buffer.length+c.buffer.length][];
		System.arraycopy(buffer, 0, b, 0, buffer.length);
		System.arraycopy(c.buffer, 0, b, buffer.length, c.buffer.length);
		buffer = b;
	}
}
