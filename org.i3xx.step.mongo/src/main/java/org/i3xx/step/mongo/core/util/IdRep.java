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

public interface IdRep {
	
	/**
	 * @return The most significant 64 bits
	 */
	long getMostSignificantBits();
	
	/**
	 * @return The least significant 64 bits
	 */
	long getLeastSignificantBits();
	
	/**
	 * @return The content as a byte array
	 */
	byte[] getBytes();
	
	/**
	 * @return The content as a BigInteger
	 */
	BigInteger toBigInteger();
	
	/**
	 * @return The string representation
	 */
	String toString();
}
