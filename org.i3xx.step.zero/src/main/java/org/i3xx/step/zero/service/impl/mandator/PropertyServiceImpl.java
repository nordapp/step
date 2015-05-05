package org.i3xx.step.zero.service.impl.mandator;

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


import java.util.HashMap;
import java.util.Map;

import org.i3xx.step.zero.service.model.mandator.PropertyService;

public class PropertyServiceImpl implements PropertyService {
	
	private final String[] names;
	private final Map<String, String> props;
	
	public PropertyServiceImpl(Map<String, String> props) {
		this.props = new HashMap<String, String>();
		this.props.putAll(props);
		this.names = props.keySet().toArray(new String[props.size()]);
	}

	public String[] getPropertyNames() {
		return names;
	}

	public boolean hasProperty(String name) {
		return props.containsKey(name);
	}

	public String getProperty(String name) {
		return props.get(name);
	}

}
