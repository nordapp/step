package org.i3xx.step.zero.security;

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


import org.i3xx.step.zero.security.impl.shiro.NaUsernamePasswordTokenImpl;
import org.i3xx.step.zero.security.model.NaAuthenticationToken;
import org.i3xx.step.zero.security.model.NaSession;
import org.i3xx.step.zero.security.model.NaSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SessionTest {

	@Before
	public void setUp() throws Exception {
		
		//Shiro configuration and setup
		NaSecurityUtil.setSecurityManager(null);
		
		NaSubject subject = NaSecurityUtil.getSubject();
		
		NaAuthenticationToken token = new NaUsernamePasswordTokenImpl("admin", "ffpbanm");
		subject.login(token);
		
		@SuppressWarnings("unused")
		NaSession session = NaSecurityUtil.getSession();
		//session.
	}
	
	@Test
	public void testA() {
		NaSession session = NaSecurityUtil.getSession();
		
		System.out.println( session.getId() );
	}
	
	@Test
	public void testB() {
		NaSession session = NaSecurityUtil.getSession();
		
		System.out.println( session.getId() );
	}
	
	@Test
	public void testC() {
		NaSession session = NaSecurityUtil.getSession();
		
		System.out.println( session.getId() );
	}
	
	@After
	public void tearDown() throws Exception {
		NaSession session = NaSecurityUtil.getSession();
		session.stop();
	}
}
