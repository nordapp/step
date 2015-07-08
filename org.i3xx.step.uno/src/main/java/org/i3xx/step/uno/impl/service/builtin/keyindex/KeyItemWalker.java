package org.i3xx.step.uno.impl.service.builtin.keyindex;

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


import org.i3xx.step.uno.model.service.builtin.keyindex.KeyItem;
import org.i3xx.step.uno.model.service.builtin.keyindex.KeyVisitor;

/**
 * A post-order walker for the key index tree
 * 
 * @author Stefan
 *
 */
public class KeyItemWalker {

	private KeyVisitor visitor;
	
	public KeyItemWalker(KeyVisitor visitor) {
		this.visitor = visitor;
	}
	
	/**
	 * Walks the tree of KeyItems
	 * 
	 * @param start The start node
	 */
	public void walk(KeyItem start) {
		visitor.visit(start);
		
		if(start.getList()!=null){
			for(KeyItem item:start.getList()) {
				walk(item);
			}//for
		}//fi
	}

}
