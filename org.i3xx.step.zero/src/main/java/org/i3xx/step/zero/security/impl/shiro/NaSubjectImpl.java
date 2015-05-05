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

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.i3xx.step.zero.security.model.NaAuthenticationToken;
import org.i3xx.step.zero.security.model.NaPrincipalCollection;
import org.i3xx.step.zero.security.model.NaSession;
import org.i3xx.step.zero.security.model.NaSubject;

public class NaSubjectImpl implements NaSubject {

	private Subject subject;
	
	public NaSubjectImpl(Subject subject) {
		this.subject = subject;
	}

	public Object getPrincipal() {
		return subject.getPrincipal();
	}

	public NaPrincipalCollection getPrincipals() {
		return new NaPrincipalCollectionImpl(subject.getPrincipals());
	}

	public boolean isPermitted(String permission) {
		return subject.isPermitted(permission);
	}

	public boolean[] isPermitted(String... permissions) {
		return subject.isPermitted(permissions);
	}

	public boolean isPermittedAll(String... permissions) {
		return subject.isPermittedAll(permissions);
	}

	public void checkPermission(String permission) throws RuntimeException {
		subject.checkPermission(permission);
	}

	public void checkPermissions(String... permissions) throws RuntimeException {
		subject.checkPermissions(permissions);
	}

	public boolean hasRole(String roleIdentifier) {
		return subject.hasRole(roleIdentifier);
	}

	public boolean[] hasRoles(List<String> roleIdentifiers) {
		return subject.hasRoles(roleIdentifiers);
	}

	public boolean hasAllRoles(Collection<String> roleIdentifiers) {
		return subject.hasAllRoles(roleIdentifiers);
	}

	public void checkRole(String roleIdentifier) throws RuntimeException {
		subject.checkRole(roleIdentifier);
	}

	public void checkRoles(Collection<String> roleIdentifiers)
			throws RuntimeException {
		subject.checkRoles(roleIdentifiers);
	}

	public void checkRoles(String... roleIdentifiers) throws RuntimeException {
		subject.checkRoles(roleIdentifiers);
	}
	
	public void login(NaAuthenticationToken token) {
		subject.login((AuthenticationToken) token.getToken());
	}
	
	public boolean isAuthenticated() {
		return subject.isAuthenticated();
	}

	public boolean isRemembered() {
		return subject.isRemembered();
	}

	public NaSession getSession() {
		return new NaSessionImpl(subject.getSession());
	}

	public NaSession getSession(boolean create) {
		return new NaSessionImpl(subject.getSession(create));
	}

	public void logout() {
		subject.logout();
	}

	public void execute(Runnable runnable) {
		subject.execute(runnable);
	}

	public Runnable associateWith(Runnable runnable) {
		return subject.associateWith(runnable);
	}

	public void runAs(NaPrincipalCollection principals)
			throws NullPointerException, IllegalStateException {
		
		subject.runAs( (PrincipalCollection) principals.getPrincipals());
	}

	public boolean isRunAs() {
		return subject.isRunAs();
	}

	public NaPrincipalCollection getPreviousPrincipals() {
		return new NaPrincipalCollectionImpl(subject.getPreviousPrincipals());
	}

	public NaPrincipalCollection releaseRunAs() {
		return new NaPrincipalCollectionImpl(subject.releaseRunAs());
	}
	
	public int hashCode() {
		return subject.hashCode();
	}
	
	public boolean equals(Object obj) {
		return subject.equals(obj);
	}
	
	public String toString() {
		return subject.toString();
	}

}
