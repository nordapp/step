package org.i3xx.step.uno.model.service.builtin.keyindex;

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


import java.util.List;

/**
 * @author Stefan
 *
 */
public interface KeyItem {

	/**
	 * Sets the key of the item
	 * 
	 * @param key
	 */
	void setKey(String key);
	
	/**
	 * @return The key of the item
	 */
	String getKey();
	
	/**
	 * Sets the list of key items
	 * 
	 * @param list
	 */
	void setList(List<KeyItem> list);
	
	/**
	 * @return The list of key items
	 */
	List<KeyItem> getList();
}