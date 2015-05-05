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


import org.apache.shiro.authc.UsernamePasswordToken;
import org.i3xx.step.zero.security.model.NaAuthenticationToken;

public class NaUsernamePasswordTokenImpl implements NaAuthenticationToken {
	
	UsernamePasswordToken token;
	
	public NaUsernamePasswordTokenImpl(String username, String password) {
		token = new UsernamePasswordToken(username, password);
		token.setRememberMe(true);
	}

	public Object getToken() {
		return token;
	}

}
