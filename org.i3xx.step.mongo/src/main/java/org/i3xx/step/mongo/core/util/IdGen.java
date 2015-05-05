package org.i3xx.step.mongo.core.util;

/*
 * #%L
 * NordApp OfficeBase :: mongo
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


import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

public class IdGen {
	
	/**
	 * Gets 128 bit IdRep representation with a generated UUID
	 * 
	 * @return
	 */
	public static final IdRep getUUID() {
		return new JavaIdRep( UUID.randomUUID() );
	}
	
	/**
	 * Gets 128 bit IdRep representation from an UUID-String
	 * 
	 * @param uuid
	 * @return
	 */
	public static final IdRep fromString( String uuid ) {
		return new JavaIdRep( UUID.fromString(uuid) );
	}
	
	/**
	 * Gets a 128 bit IdRep in an URL save representation (slow)
	 * 
	 * @return
	 */
	public static final String getURLSafeString(IdRep ui) {
		return Base64.encodeBase64URLSafeString( ui.getBytes() );
	}
	
	/**
	 * Gets a 128 bit IdRep from an URL save representation
	 * 
	 * @param urlSave
	 * @return
	 */
	public static final IdRep fromURLSaveString(String urlSave) {
		
		byte[] bytes = Base64.decodeBase64(urlSave);
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		UUID uuid = new UUID(buffer.getLong(0), buffer.getLong(8));
		
		return new JavaIdRep( uuid );
	}

	/**
	 * Gets a 128 bit IdRep from a BigInteger
	 * 
	 * @return
	 */
	public static final IdRep fromBigInteger( BigInteger ui ) {
		
		UUID uuid = new UUID(
				ui.shiftRight(64).longValue(),
				ui.longValue()
			);
		
		return new JavaIdRep( uuid ) ;
	}
}
