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


import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;

public class NaMyRealm extends AuthorizingRealm {
	
	private String _salt;
	private String _hash;

	public NaMyRealm() {
		super();
		
		@SuppressWarnings("unused")
		String username = "admin";
		String plainTextPassword = "ffpbanm";
		
		//We'll use a Random Number Generator to generate salts.  This
		//is much more secure than using a username as a salt or not
		//having a salt at all.  Shiro makes this easy.
		//
		//Note that a normal app would reference an attribute rather
		//than create a new RNG every time:
		RandomNumberGenerator rng = new SecureRandomNumberGenerator();
		ByteSource salt = rng.nextBytes();
		_salt = salt.toBase64();
		
		//Now hash the plain-text password with the random salt and multiple
		//iterations and then Base64-encode the value (requires less space than Hex):
		//String hashedPasswordBase64 = new Sha256Hash(plainTextPassword, salt, 1024).toBase64();
		
		String hash = new Sha256Hash(plainTextPassword, _salt, 1024).toBase64();
		_hash = hash;
		
		//User user = new User(username, hashedPasswordBase64);
		//save the salt with the new account.  The HashedCredentialsMatcher
		//will need it later when handling login attempts:
		//user.setPasswordSalt(salt);
		//userDAO.create(user);		
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		String username = (String) getAvailablePrincipal(principals);
		return getAccount(username, null);
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		return getAccount(upToken.getUsername(), upToken.getCredentials());
	}

	protected SimpleAccount getAccount(String username, Object credentials) {
		
		//TODO: Remove the System.out
		System.out.println("get account user: "+username);
		//Account account = new SimpleAccount(username, "sha256EncodedPasswordFromDatabase", getName());
		
		if (username == null) throw new AccountException("Null usernames are not allowed by this realm.");
		//Account account=_store.getAccounts().get(username);
		//if (account == null) throw new UnknownAccountException("No account found for user [" + username + "]");
		String hash = _hash/*account.getPasswordHash()*/;
		ByteSource salt=new SimpleByteSource(_salt/*account.getSalt()*/);
		
		SimpleAccount account = new SimpleAccount(username, hash, salt, getName());
		  
		//SimpleAccount account = new SimpleAccount(username, "sha256EncodedPasswordFromDatabase", getName());
		account.addRole("user");
		account.addRole("admin");
		account.addStringPermission("blogEntry:edit");
		account.addStringPermission("printer:print:laserjet");
		
		//The password or private key
		account.setCredentials(credentials);
		 
		return account;
	}

}
