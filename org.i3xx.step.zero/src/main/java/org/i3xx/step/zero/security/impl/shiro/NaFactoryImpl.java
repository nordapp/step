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
import java.util.Properties;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.i3xx.step.zero.security.NaFactory;
import org.i3xx.step.zero.security.model.NaSession;
import org.i3xx.step.zero.security.model.NaSubject;

public class NaFactoryImpl implements NaFactory {

	public void setSecurityManager(Properties props) {
		if(props==null){
			//does nothing
		}
		
		Realm realm = new NaMyRealm();
		
		DefaultSecurityManager securityManager = new DefaultSecurityManager(realm);
		SecurityUtils.setSecurityManager(securityManager);
		
		MemoryConstrainedCacheManager cacheManager = new MemoryConstrainedCacheManager();
		securityManager.setCacheManager(cacheManager);
	}
	
	public NaSubject createSubject(Serializable sessionId) {
		Subject requestSubject = new Subject.Builder().sessionId(sessionId).buildSubject();
		NaSubject subject = new NaSubjectImpl( requestSubject );
		return subject;
	}
	
	public NaSubject getSubject() {
		NaSubject subject = new NaSubjectImpl( SecurityUtils.getSubject() );
		return subject;
	}

	public NaSession getSession() {
		NaSession session = new NaSessionImpl( SecurityUtils.getSubject().getSession() );
		return session;
	}
}
