package org.i3xx.step.uno.model;

/*
 * #%L
 * NordApp OfficeBase :: uno
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
import java.util.List;

public interface StepCard extends Serializable {

	/**
	 * @return the counter
	 */
	int getCounter();

	/**
	 * @param counter the counter to set
	 */
	void setCounter(int counter);

	/**
	 * @return the multiple
	 */
	boolean isMultiple();

	/**
	 * @param multiple the multiple to set
	 */
	void setMultiple(boolean multiple);

	/**
	 * @return the priority
	 */
	int getPriority();

	/**
	 * @param priority the priority to set
	 */
	void setPriority(int priority);
	
	/**
	 * @return the insertion order
	 */
	int getInsertion();
	
	/**
	 * The insertion Order is an inner field of the sequencer
	 * and should not be set manually. This field should ensure
	 * a particular order if the transaction and order fields
	 * are both equal. This field is set to a positive value
	 * if a card is added to the sequencer.
	 * 
	 * Sets the insertion order
	 * 
	 * @param insertion
	 */
	void setInsertion(int insertion);
	
	/**
	 * @return the order
	 */
	int getOrder();

	/**
	 * If the order is set to a positive integer the order must be respected
	 * to all other steps having an order to. Lower numbers are executed first.
	 * If a step has the order of 100 and another step has the order of 200,
	 * the step with the lower value has to be executed first in all cases.
	 * 
	 * As a result of this a step with the order of 200 cannot move or copy
	 * another step with a lower value behind itself.
	 * 
	 * @param order the order to set
	 */
	void setOrder(int order);

	/**
	 * @return the transaction
	 */
	int getTransaction();

	/**
	 * If steps are transactional they are processed together. No step
	 * can be put inside a transaction except it becomes a member of
	 * the transaction by setting the transaction to the same value.
	 * 
	 * The transaction is executed by the order of the first element of the
	 * transaction.
	 * 
	 * @param transaction the transaction to set
	 */
	void setTransaction(int transaction);

	/**
	 * @return the name
	 */
	String getName();

	/**
	 * @param name the name to set
	 */
	void setName(String name);

	/**
	 * @return the symbolic name
	 */
	String getSymbolicName();

	/**
	 * @param name the symbolic name to set
	 */
	void setSymbolicName(String name);

	/**
	 * @return the function
	 */
	String getFunction();

	/**
	 * @param function the function to set
	 */
	void setFunction(String function);
	
	/**
	 * @return The import list
	 */
	List<String> getImport();
	
	/**
	 * @param list
	 */
	void setImport(List<String> list);
	
	/**
	 * @return The export list
	 */
	List<String> getExport();
	
	/**
	 * @param list
	 */
	void setExport(List<String> list);
	
}
