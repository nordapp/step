package org.i3xx.step.zero.service.impl.cx;

/*
 * #%L
 * NordApp OfficeBase :: zero
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


import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.i3xx.step.zero.service.model.cx.Context;

public class ContextImpl implements Context {
	
	final private BigInteger id;
	final private Map<String, Object> attributes;
	
	private int counter;
	
	public ContextImpl() {
		UUID ui = UUID.randomUUID();
		this.id = BigInteger
				.valueOf(ui.getMostSignificantBits())
				.shiftLeft(64)
				.or(BigInteger.valueOf(ui.getLeastSignificantBits()));
		
		this.attributes = new HashMap<String, Object>();
		this.counter = 0;
	}
	
	public BigInteger getId() {
		return id;
	}

	public int bind() {
		synchronized(id) {
			counter++;
		}
		return counter;
	}

	public int unbind() {
		synchronized(id) {
			counter--;
			if(counter==Integer.MAX_VALUE)
				counter = 0;
		}
		return counter;
	}

	public String[] getAttributeNames() {
		return attributes.keySet().toArray(new String[attributes.size()]);
	}

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	public Object removeAttribute(String name) {
		return attributes.remove(name);
	}

	public void clearAttributes() {
		attributes.clear();
	}

}
