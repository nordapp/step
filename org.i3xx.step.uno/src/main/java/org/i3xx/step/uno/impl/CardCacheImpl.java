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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.i3xx.step.uno.impl.util.GsonHashMapDeserializer;
import org.i3xx.step.uno.model.CardCache;
import org.i3xx.step.uno.model.StepCard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CardCacheImpl implements CardCache {
	
	List<byte[]> buffer;
	
	public CardCacheImpl() {
		buffer = new ArrayList<byte[]>();
	}

	public void addCard(StepCard card) {
		buffer.add( readCard(card) );
	}

	public void addCards(List<StepCard> cards) {
		for(StepCard card : cards)
			addCard(card);
	}

	public int size() {
		return buffer.size();
	}

	public StepCard getCard(int index) {
		return writeCard( buffer.get(index) );
	}

	public List<StepCard> getCards() {
		List<StepCard> list = new ArrayList<StepCard>();
		for(byte[] buf : buffer)
			list.add( writeCard(buf) );
		
		return list;
	}
	
	/**/
	private byte[] readCard(StepCard card) {
		if(card==null)
			return null;
		
		try{
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream( buffer );
			out.writeObject(card);
			out.close();
			
			return buffer.toByteArray();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**/
	private StepCard writeCard(byte[] buffer) {
		if(buffer==null)
			return null;
		
		try{
			ObjectInputStream in = new ObjectInputStream( new ByteArrayInputStream(buffer) );
			StepCard card = (StepCard)in.readObject();
			in.close();
			
			return card;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
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
		
		buf.append( gson.toJson("buffer") );
		buf.append( ':' );
		buf.append( '[' );
		for(int i=0;i<buffer.size();i++){
			if(i>0)
				buf.append( ',' );
			
			String stmt = Base64.encodeBase64URLSafeString( buffer.get(i) );
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
		
		List<?> ll = (List<?>)m.get("buffer");
		buffer.clear();
		for(int i=0;i<ll.size();i++){
			String stmt = (String)ll.get(i);
			buffer.add( Base64.decodeBase64( stmt ) );
		}
	}
}
