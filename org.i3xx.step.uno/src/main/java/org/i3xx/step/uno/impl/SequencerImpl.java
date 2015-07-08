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


import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.i3xx.step.uno.impl.util.GsonHashMapDeserializer;
import org.i3xx.step.uno.model.CardCache;
import org.i3xx.step.uno.model.Sequencer;
import org.i3xx.step.uno.model.StepCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The sequencer list is always sorted. If the order or transaction
 * of a step card changes the list must be resorted and the pointer
 * must be reset.
 * 
 * @author Stefan
 *
 */
public class SequencerImpl implements Sequencer {
	
	static Logger logger = LoggerFactory.getLogger(SequencerImpl.class);
	
	private List<StepCard> list;
	private int previous;
	private int iCounter;
	
	public SequencerImpl() {
		list = new ArrayList<StepCard>();
		previous = -1;
		iCounter = 0;
	}
	
	//
	// Add and remove
	//
	
	/**
	 * Reads the manifest into a property map
	 * 
	 * @param manifest
	 * @return The manifest property map
	 */
	public Map<String, String> readManifest(String manifest) {
		
		Map<String, String> props = new HashMap<String, String>();
		
		try{
			/* The data is stored ever, but a cleanup() will clean it. */
			LineNumberReader in = new LineNumberReader(new StringReader(manifest));
			String line = null, prev = null;
			try{
				while((line=in.readLine())!=null){
					line = line.trim();
					//comment
					if(line.startsWith("#"))
						continue;
					//empty line
					if(line.equals(""))
						continue;
					
					String[] p = line.split("\\:", 2);
					if(p.length==1){
						String pp = p[0].trim();
						if(pp.endsWith(","))
							pp = pp.substring(0, pp.length()-1);
						if(pp.equals(""))
							continue;
						
						String v = props.get(prev);
						v += ";" + pp;
						props.put(prev, v);
					}else
					if(p.length==2){
						String k = p[0].trim().toLowerCase();
						String v = p[1].trim();
						props.put(k, v);
						
						prev = k;
					}
				}//while
				
			}finally{ 
				in.close();
			}//try
		}catch(Exception e){
			logger.error("Loading the manifest fails.", e);
		}
		
		return props;
	}
	
	/**
	 * Loads a card defined by the manifest.
	 * 
	 * @param manifest The manifest
	 * @return True if the card is put in front of the pointer, false otherwise.
	 */
	public boolean addManifest(String manifest) {
		
		StepCard card = new StepCardImpl();
		Map<String, String> props = readManifest(manifest);
		
		//card.setCounter(0);
		if(props.containsKey("card-function")){
			card.setFunction( props.get("card-function") );
		}
		if(props.containsKey("card-insertion") && (! props.get("card-insertion").equals("")) ){
			card.setInsertion( Integer.parseInt(props.get("card-insertion")) );
		}
		if(props.containsKey("card-multiple")){
			card.setMultiple( Boolean.parseBoolean(props.get("card-multiple")) );
		}
		if(props.containsKey("card-name")){
			card.setName( props.get("card-name") );
		}
		if(props.containsKey("card-symbolicname")){
			card.setSymbolicName( props.get("card-symbolicname") );
		}
		if(props.containsKey("card-order") && (! props.get("card-order").equals("")) ){
			card.setOrder( Integer.parseInt(props.get("card-order")) );
		}
		if(props.containsKey("card-priority") && (! props.get("card-priority").equals("")) ){
			card.setPriority( Integer.parseInt(props.get("card-priority")) );
		}
		if(props.containsKey("card-transaction") && (! props.get("card-transaction").equals("")) ){
			card.setTransaction( Integer.parseInt(props.get("card-transaction")) );
		}
		if(props.containsKey("card-import") && (! props.get("card-import").equals("")) ){
			card.setImport( StepCardImpl.reel(props.get("card-import")) );
		}
		if(props.containsKey("card-export") && (! props.get("card-export").equals("")) ){
			card.setExport( StepCardImpl.reel(props.get("card-export")) );
		}
		
		return add(card);
	}
	
	/**
	 * Adds a step to the list
	 * 
	 * @param card
	 * @return True if the card is put in front of the pointer, false otherwise.
	 */
	public boolean add(StepCard card){
		//The insertion counter is a positive integer
		card.setInsertion(iCounter);
		iCounter = (iCounter < Integer.MAX_VALUE) ? (iCounter+1) : 0;
		
		int p = Collections.binarySearch(list, card, getComparator());
		int i = p>-1 ? p : (-p)-1;
		
		if(logger.isDebugEnabled()){
			logger.debug( ( (i>previous) ? "Append" : "Insert" )+
					" i:"+i+", p:"+p+", the card "+card.toString());
		}
		
		if(i>=previous){
			list.add(i, card);
			if(logger.isTraceEnabled()){
				logger.trace("Added p:"+i+", previous"+previous+", the card "+card.toString());
			}
			return true;
		}else /*if(i<=previous)*/ {
			list.add(i, card);
			if(logger.isTraceEnabled()){
				logger.trace("Added p:"+i+", previous"+previous+", the card "+card.toString());
			}
			previous++;
			return false;
		}
	}
	
	/**
	 * Removes a step from the list
	 * 
	 * @param card
	 * @return True if the card is removed in front of the pointer, false otherwise.
	 */
	public boolean remove(StepCard card) {
		int p = Collections.binarySearch(list, card, getComparator());
		
		if(logger.isDebugEnabled()){
			logger.debug("Remove p:"+p+" the card "+card.toString());
		}
		
		//
		// The element to be processed next ::= n
		// The previous element             ::= n - 1
		//
		if(p>previous){
			card = list.remove(p);
			if(logger.isTraceEnabled()){
				logger.trace("Removed p:"+p+", previous"+previous+", the card "+card.toString());
			}
			return true;
		}else if(p>-1){
			card = list.remove(p);
			if(logger.isTraceEnabled()){
				logger.trace("Removed p:"+p+", previous"+previous+", the card "+card.toString());
			}
			previous--;
			return false;
		}else{
			if(logger.isTraceEnabled()){
				logger.trace("Nothing to do p:"+p+", previous"+previous+", the card "+card.toString());
			}
			return false;
		}//fi
	}
	
	//
	// Sequential access
	//
	
	/**
	 * Resets the pointer
	 */
	public void reset() {
		previous = -1;
	}
	
	/**
	 * Clears the sequencer
	 */
	public void clear() {
		list = new ArrayList<StepCard>();
		previous = -1;
		iCounter = 0;
	}
	
	/**
	 * @return True if there is one more element to execute, false otherwise.
	 */
	public boolean hasNext() {
		return (previous+1) < list.size();
	}
	
	/**
	 * Moves the pointer forwards and gets the step card at the new position
	 * of the pointer.
	 * 
	 * @return The next step card
	 */
	public StepCard next() {
		previous++;
		
		return list.get(previous);
	}
	
	//
	// Random access
	//
	
	/**
	 * @return The size of the list
	 */
	public int size() {
		return list.size();
	}
	
	/**
	 * Gets the step card at the index without moving the pointer.
	 * 
	 * @param index The index
	 * @return The step card
	 */
	public StepCard get(int index) {
		return list.get(index);
	}
	
	/**
	 * Returns the index of the card with the matching symbolic name
	 * or -1 if nothing can be found.
	 * 
	 * @param symbolicName The symbolic name
	 * @return The index as integer
	 */
	public int find(String symbolicName) {
		for(int i=0;i<list.size();i++) {
			StepCard card = list.get(i);
			if(card.getSymbolicName()!=null && card.getSymbolicName().equals(symbolicName))
				return i;
		}
		
		return -1;
	}
	
	/**
	 * Creates a step card (For script use)
	 * 
	 * @param name The name of the card
	 * @param function The function to be called by the card
	 * @param order The execution order
	 * @param transaction The transaction (2nd order)
	 * @param priority The execution priority
	 * @param multiple True to allow multiple execution, false otherwise
	 */
	public StepCard create(String name, String function, int order, int transaction, int priority, boolean multiple) {
		return new StepCardImpl(name, function, order, transaction, priority, multiple);
	}
	
	//
	// Sort
	//
	
	/**
	 * Sorts the list (needs a reset of the pointer)
	 */
	public void sort() {
		Collections.sort(list, getComparator());
	}
	
	/**
	 * @return The comparator
	 */
	private Comparator<StepCard> getComparator() {
		return new Comparator<StepCard>(){

			public int compare(StepCard a, StepCard b) {
				
				return a.getTransaction() > b.getTransaction() ? 1 :
					a.getTransaction() < b.getTransaction() ? -1 :
						a.getOrder() > b.getOrder() ? 1 :
							a.getOrder() < b.getOrder() ? -1 : 
								a.getInsertion() > b.getInsertion() ? 1 :
									a.getInsertion() < b.getInsertion() ? -1 : 0;
			}};
	}
	
	//
	// Serialize facility
	//
	
	/**
	 * Serializes the content to JSON
	 * 
	 * @return The JSON String
	 */
	public String toJSON() {
		
		Gson gson = new Gson();
		StringBuffer buf = new StringBuffer();
		
		buf.append( '{' );
		
		buf.append( gson.toJson("previous") );
		buf.append( ':' );
		buf.append( gson.toJson(Integer.valueOf(previous)) );
		buf.append( ',' );
		
		buf.append( gson.toJson("iCounter") );
		buf.append( ':' );
		buf.append( gson.toJson(Integer.valueOf(iCounter)) );
		buf.append( ',' );
		
		buf.append( gson.toJson("list") );
		buf.append( ':' );
		buf.append( '[' );
		
		for(int i=0;i<list.size();i++){
			if(i>0)
				buf.append( ',' );
			
			unload(gson, buf, list.get(i));
		}
		
		buf.append( ']' );
		buf.append( '}' );
		
		return buf.toString();
	}
	
	/**  */
	private void unload(Gson gson, StringBuffer buf, StepCard card) {
		
		buf.append( '{' );
		
		buf.append( gson.toJson("counter") );
		buf.append( ':' );
		buf.append( gson.toJson(Integer.valueOf(card.getCounter())) );
		buf.append( ',' );
		
		buf.append( gson.toJson("function") );
		buf.append( ':' );
		buf.append( gson.toJson(card.getFunction()) );
		buf.append( ',' );
		
		buf.append( gson.toJson("insertion") );
		buf.append( ':' );
		buf.append( gson.toJson(Integer.valueOf(card.getInsertion())) );
		buf.append( ',' );
		
		buf.append( gson.toJson("multiple") );
		buf.append( ':' );
		buf.append( gson.toJson(Boolean.valueOf(card.isMultiple())) );
		buf.append( ',' );
		
		buf.append( gson.toJson("name") );
		buf.append( ':' );
		buf.append( gson.toJson(card.getName()) );
		buf.append( ',' );
		
		buf.append( gson.toJson("symbolicname") );
		buf.append( ':' );
		buf.append( gson.toJson(card.getSymbolicName()) );
		buf.append( ',' );
		
		buf.append( gson.toJson("order") );
		buf.append( ':' );
		buf.append( gson.toJson(Integer.valueOf(card.getOrder())) );
		buf.append( ',' );
		
		buf.append( gson.toJson("priority") );
		buf.append( ':' );
		buf.append( gson.toJson(Integer.valueOf(card.getPriority())) );
		buf.append( ',' );
		
		buf.append( gson.toJson("transaction") );
		buf.append( ':' );
		buf.append( gson.toJson(Integer.valueOf(card.getTransaction())) );
		buf.append( ',' );
		
		buf.append( gson.toJson("import") );
		buf.append( ':' );
		buf.append( gson.toJson( StepCardImpl.unreel(card.getImport()) ) );
		buf.append( ',' );
		
		buf.append( gson.toJson("export") );
		buf.append( ':' );
		buf.append( gson.toJson( StepCardImpl.unreel(card.getExport()) ) );
		
		buf.append( '}' );
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
		previous = ((Number)m.get("previous")).intValue();
		iCounter = ((Number)m.get("iCounter")).intValue();
		
		List<?> ls = (List<?>)m.get("list");
		for(Object obj : ls){
			load(gson, obj);
		}
	}
	
	/**  */
	private void load(Gson gson, Object obj) {
		
		StepCard card = new StepCardImpl();
		Map<?, ?> m = (Map<?, ?>)obj;
		
		card.setCounter( ((Number)m.get("counter")).intValue() );
		card.setFunction( (String)m.get("function") );
		card.setInsertion( ((Number)m.get("insertion")).intValue() );
		card.setMultiple( ((Boolean)m.get("multiple")).booleanValue() );
		card.setName( (String)m.get("name") );
		card.setSymbolicName( (String)m.get("symbolicname") );
		card.setOrder( ((Number)m.get("order")).intValue() );
		card.setPriority( ((Number)m.get("priority")).intValue() );
		card.setTransaction( ((Number)m.get("transaction")).intValue() );
		card.setImport( StepCardImpl.reel((String)m.get("import")) );
		card.setExport( StepCardImpl.reel((String)m.get("export")) );
		
		list.add(card);
	}
	
	/**
	 * Saves the sequencer list to the card cache.
	 * 
	 * @param cache
	 */
	public void toCache(CardCache cache) {
		cache.addCards(list);
	}
	
	/**
	 * Loads the sequencer list from the card cache.
	 * 
	 * @param cache
	 */
	public void fromCache(CardCache cache) {
		list = cache.getCards();
	}
}
