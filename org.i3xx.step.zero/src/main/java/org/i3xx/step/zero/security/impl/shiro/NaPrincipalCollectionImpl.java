package org.i3xx.step.zero.security.impl.shiro;

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


import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.shiro.subject.PrincipalCollection;
import org.i3xx.step.zero.security.model.NaPrincipalCollection;

public class NaPrincipalCollectionImpl implements NaPrincipalCollection {

	private PrincipalCollection principals; 
	
	public NaPrincipalCollectionImpl(PrincipalCollection principals) {
		this.principals = principals;
	}

	public Object getPrimaryPrincipal() {
		return principals.getPrimaryPrincipal();
	}

	public <T> T oneByType(Class<T> type) {
		return principals.oneByType(type);
	}

	public <T> Collection<T> byType(Class<T> type) {
		return principals.byType(type);
	}

	public List<?> asList() {
		return principals.asList();
	}

	public Set<?> asSet() {
		return principals.asSet();
	}

	public Collection<?> fromRealm(String realmName) {
		return principals.fromRealm(realmName);
	}

	public Set<String> getRealmNames() {
		return principals.getRealmNames();
	}

	public boolean isEmpty() {
		return principals.isEmpty();
	}
	
	public int hashCode() {
		return principals.hashCode();
	}
	
	public boolean equals(Object obj) {
		return principals.equals(obj);
	}
	
	public String toString() {
		return principals.toString();
	}
	
	public Object getPrincipals() {
		return principals;
	}

}
