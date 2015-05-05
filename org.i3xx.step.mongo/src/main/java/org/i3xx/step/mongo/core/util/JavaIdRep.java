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


import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Just a wrapper to change the implementation
 * 
 * @author Stefan
 *
 */
public final class JavaIdRep implements IdRep, Serializable, Comparable<JavaIdRep> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1290313847556743342L;
	
	private final UUID uuid;
	
	public JavaIdRep(UUID uuid) {
		this.uuid = uuid;
	}
	
	// ------------------------------------------------------
	
	public int compareTo(JavaIdRep rep) {
		return uuid.compareTo(rep.uuid);
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof JavaIdRep))
			return false;
		
		return uuid.equals( ((JavaIdRep)obj).uuid );
	}
	
	public int hashCode() {
		return uuid.hashCode();
	}
	
	// ------------------------------------------------------
	
	public long getMostSignificantBits() {
		return uuid.getMostSignificantBits();
	}

	public long getLeastSignificantBits() {
		return uuid.getLeastSignificantBits();
	}

	public byte[] getBytes() {
		return ByteBuffer.allocate(16)
				.putLong(0, uuid.getMostSignificantBits())
				.putLong(8, uuid.getLeastSignificantBits())
				.array();
	}
	
	public BigInteger toBigInteger() {
		return new BigInteger(getBytes());
	}
	
	public String toString() {
		return uuid.toString();
	}

}
