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


import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import org.apache.shiro.session.Session;
import org.i3xx.step.zero.security.model.NaSession;

public class NaSessionImpl implements NaSession {

	private Session session;
	
	public NaSessionImpl(Session session) {
		this.session = session;
	}
	
	public Object getAttribute(Object key) {
		return session.getAttribute(key);
	}
	
	public Collection<Object> getAttributeKeys() {
		return session.getAttributeKeys();
	}
	
	public String getHost() {
		return session.getHost();
	}
	
	public Serializable getId() {
		return session.getId();
	}
	
	public Date getLastAccessTime() {
		return session.getLastAccessTime();
	}
	
	public long getTimeout() {
		return session.getTimeout();
	}
	
	public Date getStartTimestamp() {
		return session.getStartTimestamp();
	}
	
	public Object removeAttribute(Object key) {
		return session.removeAttribute(key);
	}
	
	public void setAttribute(Object key, Object value) {
		session.setAttribute(key, value);
	}
	
	public void setTimeout(long timeout) {
		session.setTimeout(timeout);
	}
	
	public void stop() {
		session.stop();
	}
	
	public String toString() {
		return session.toString();
	}
}
