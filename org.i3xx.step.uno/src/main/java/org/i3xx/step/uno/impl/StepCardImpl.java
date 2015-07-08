package org.i3xx.step.uno.impl;

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


import java.util.ArrayList;
import java.util.List;

import org.i3xx.step.uno.model.StepCard;

public class StepCardImpl implements StepCard {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The execution counter */
	private int counter;
	
	/** True if the step runs more than once */
	private boolean multiple;
	
	/** The priority of the step */
	private int priority;
	
	/**
	 * The insertion Order is an inner field of the sequencer
	 * and should not be set manually. This field should ensure
	 * a particular order if the transaction and order fields
	 * are both equal. This field is set to a positive value
	 * if a card is added to the sequencer.
	 */
	private int insertion;
	
	/**
	 * The order of the step
	 * 
	 * If the order is set to a positive integer the order must be respected
	 * to all other steps having an order to. Lower numbers are executed first.
	 * If a step has the order of 100 and another step has the order of 200,
	 * the step with the lower value has to be executed first in all cases.
	 * 
	 * As a result of this a step with the order of 200 cannot move or copy
	 * another step with a lower value behind itself.
	 */
	private int order;
	
	/**
	 * If steps are transactional they are processed together. No step
	 * can be put inside a transaction except it becomes a member of
	 * the transaction by setting the transaction to the same value.
	 * 
	 * The transaction is executed by the order of the first element of the
	 * transaction.
	 */
	private int transaction;
	
	/** The name of the step */
	private String name;
	
	/** The symbolic name of the step */
	private String symbolicName;
	
	/** The function to be called by the step */
	private String function;
	
	/** The list of the imported objects */
	private List<String> importList;
	
	/** The list of the exported objects */
	private List<String> exportList;

	/**
	 * Creates a step card
	 */
	public StepCardImpl() {
		counter = 0;
		multiple = false;
		priority = 5;
		order = 0;
		transaction = 0;
		name = null;
		symbolicName = null;
		function = null;
		importList = null;
		exportList = null;
	}
	
	/**
	 * Creates a step card
	 * 
	 * @param name The name of the card
	 * @param function The function to be called by the card
	 * @param order The execution order
	 */
	public StepCardImpl(String name, String function, int order) {
		this();
		
		this.order = order;
		this.name = name;
		this.function = function;
	}

	
	/**
	 * Creates a step card
	 * 
	 * @param name The name of the card
	 * @param function The function to be called by the card
	 * @param order The execution order
	 * @param transaction The transaction (2nd order)
	 * @param priority The execution priority
	 * @param multiple True to allow multiple execution, false otherwise
	 */
	public StepCardImpl(String name, String function, int order, int transaction, int priority, boolean multiple) {
		this();
		
		this.counter = 0;
		this.multiple = multiple;
		this.priority = priority;
		this.order = order;
		this.transaction = transaction;
		this.name = name;
		this.function = function;
	}
	
	/**
	 * @return the counter
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * @param counter the counter to set
	 */
	public void setCounter(int counter) {
		this.counter = counter;
	}

	/**
	 * @return the multiple
	 */
	public boolean isMultiple() {
		return multiple;
	}

	/**
	 * @param multiple the multiple to set
	 */
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	/**
	 * @return the insertion order
	 */
	public int getInsertion(){
		return insertion;
	}
	
	/**
	 * The insertion Order is an inner field of the sequencer
	 * and should not be set manually. This field should ensure
	 * a particular order if the transaction and order fields
	 * are both equal. This field is set to a positive value
	 * if a card is added to the sequencer.
	 * 
	 * Sets the insertion order
	 */
	public void setInsertion(int insertion){
		this.insertion = insertion;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

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
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * @return the transaction
	 */
	public int getTransaction() {
		return transaction;
	}

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
	public void setTransaction(int transaction) {
		this.transaction = transaction;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the symbolicName
	 */
	public String getSymbolicName() {
		return symbolicName;
	}

	/**
	 * @param name the symbolicName to set
	 */
	public void setSymbolicName(String name) {
		this.symbolicName = name;
	}

	/**
	 * @return the function
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * @param function the function to set
	 */
	public void setFunction(String function) {
		this.function = function;
	}
	
	/**
	 * @return The import list
	 */
	public List<String> getImport() {
		return importList;
	}
	
	/**
	 * @param list
	 */
	public void setImport(List<String> list) {
		importList = list;
	}
	
	/**
	 * @return The export list
	 */
	public List<String> getExport() {
		return exportList;
	}
	
	/**
	 * @param list
	 */
	public void setExport(List<String> list) {
		exportList = list;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "name:"+name+
				", function:"+function+
				", counter:"+counter+
				", multiple:"+multiple+
				", priority:"+priority+
				", insertion:"+insertion+
				", order:"+order+
				", transaction:"+transaction+
				", import:"+StepCardImpl.unreel(importList)+
				", export:"+StepCardImpl.unreel(exportList);
	}
	
	/** Enrolls a list of Strings to a single String containing the elements separated by ';' */
	public static String unreel(List<String> list) {
		StringBuffer buffer = new StringBuffer();
		
		if(list!=null) {
			for(int i=0;i<list.size();i++) {
				if(i>0)
					buffer.append(';');
				
				buffer.append(list.get(i));
			}//for
		}//fi
		
		return buffer.toString();
	}
	
	/** Reel (roll up) a semicolon separated list and returns a List of Strings. */
	public static List<String> reel(String stmt) {
		List<String> list = new ArrayList<String>();
		
		if(stmt!=null) {
			String[] arr = stmt.split(";");
			for(int i=0;i<arr.length;i++){
				list.add( arr[i].trim() );
			}//for
		}//fi
		
		return list;
	}

}
