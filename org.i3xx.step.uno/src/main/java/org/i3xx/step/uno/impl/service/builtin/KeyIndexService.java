package org.i3xx.step.uno.impl.service.builtin;

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


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.i3xx.step.uno.impl.ContextImpl;
import org.i3xx.step.uno.impl.service.builtin.keyindex.KeyItemImpl;
import org.i3xx.step.uno.impl.service.builtin.keyindex.KeyItemWalkerPre;
import org.i3xx.step.uno.model.service.builtin.BuiltinService;
import org.i3xx.step.uno.model.service.builtin.keyindex.KeyItem;
import org.i3xx.step.uno.model.service.builtin.keyindex.KeyVisitor;

/**
 * The key index service provides an index to the keys of the key-value-pairs stored
 * at the context. This is the preferred way to get a key list of children of a key.
 * 
 * @author Stefan
 *
 */
public class KeyIndexService implements BuiltinService {
	
	private ContextImpl.ContextService context;
	private KeyItem item;
	
	public KeyIndexService(ContextImpl.ContextService context) {
		this.context = context;
		item = new KeyItemImpl();
		
		//initialize the index
		item.setKey("index");
	}
	
	/**
	 * Initializes this service
	 */
	public void initialize() {
		context.setKeyIndex(this);
	}
	
	/**
	 * Deactivates this service
	 */
	public void deactivate() {
		context.setKeyIndex(null);
	}
	
	/**
	 * Clears and reinitializes the service to a clean state.
	 * 
	 * Reinitialize is not thought to free the former used resources, but
	 * to start work in a clean state.
	 */
	public void reinitialize(ContextImpl.ContextService context) {
		this.context = context;
		item = new KeyItemImpl();
		
		//initialize the index
		item.setKey("index");
	}
	
	/**
	 * @param key Adds a key to the index
	 * @return
	 */
	public void addKey(String key) {
		process(split(key), true);
	}
	
	/**
	 * Gets a KeyItem from the index (creates one if not present)
	 * 
	 * @param key
	 * @return
	 */
	public KeyItem getKey(String key) {
		return process(split(key), false);
	}
	
	/**
	 * Removes a key from the index
	 * @param key
	 */
	public boolean removeKey(String key) {
		String[] arr = split(key);
		if(arr.length<=1)
			return false;
		
		String[] str = new String[arr.length-1];
		System.arraycopy(arr,  0,  str,  0,  str.length);
		
		KeyItem i = process(str, false);
		if(i==null)
			return false;
		
		if(i.getList()!=null){
			KeyItem j = process(arr, false);
			if(j==null)
				return false;
			
			return i.getList().remove(j);
		}
		
		return false;
	}
	
	/**
	 * Clears the index
	 */
	public void clear() {
		//Needs a pre-order walker, walk first then visit (clear)
		KeyItemWalkerPre w = new KeyItemWalkerPre(new KeyVisitor(){

			public void visit(KeyItem keyItem) {
				//clears every element to be garbage collected
				keyItem.setKey(null);
				keyItem.setList(null);
			}});
		
		w.walk(item);
		
		//reinitialize the index
		item.setKey("index");
	}
	
	/**
	 * Splits the key
	 * 
	 * @param key
	 * @return
	 */
	private String[] split(String key) {
		
		//replace namespace token ':'
		String s = key.replace(':', '.');
		
		//split
		String[] arr = s.split("\\.");
		
		return arr;
	}
	
	/**
	 * Process an array
	 * 
	 * @param key The key
	 * @parem create The factory flag
	 */
	private KeyItem process(String[] arr, boolean create) {
		
		//process
		KeyItem ref = new KeyItemImpl();
		
		KeyItem c = item;
		for(String tmp:arr) {
			String k = tmp.trim();
			//skip empty parts
			if(k.length()==0)
				continue;
			
			//create list if missing
			if(c.getList()==null){
				c.setList(new ArrayList<KeyItem>());
			}//fi
			
			//search the node
			ref.setKey(k);
			int i = Collections.binarySearch(c.getList(), ref, new Comparator<KeyItem>(){

				public int compare(KeyItem a, KeyItem b) {
					return a.getKey().compareTo(b.getKey());
				}});
			
			//
			if(i<0){
				if( !create)
					return null;
				
				KeyItem e = new KeyItemImpl();
				e.setKey(k);
				e.setList(new ArrayList<KeyItem>());
				
				//insert at index or append
				int n = c.getList().size();
				int p = (-i)-1;
				c.getList().add(n>p?p:n, e);
				c = e;
				
				//re-sorts the list
				//Collections.sort(c.getList(), new Comparator<KeyItem>(){
				//
				//	@Override
				//	public int compare(KeyItem a, KeyItem b) {
				//		return a.getKey().compareTo(b.getKey());
				//	}});
			}else{
				c = c.getList().get(i);
			}
				
		}//for
		
		return c;
	}
}
