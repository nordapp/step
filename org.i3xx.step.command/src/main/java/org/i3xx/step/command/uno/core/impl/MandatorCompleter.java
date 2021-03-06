package org.i3xx.step.command.uno.core.impl;

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


import java.util.List;

import org.apache.karaf.shell.console.Completer;
import org.apache.karaf.shell.console.completer.StringsCompleter;
import org.i3xx.step.zero.service.model.mandator.MandatorService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class MandatorCompleter implements Completer {
	
	/** The osgi bundle context */
	private BundleContext bundleContext;

	public MandatorCompleter() {
		bundleContext = null;
	}

	public int complete(String buffer, int cursor, List<String> candidates) {
		
		//
		// Gets a list of all configured mandator and initialize them.
		//
		ServiceReference<MandatorService> msr = bundleContext.getServiceReference(MandatorService.class);
		MandatorService ms = bundleContext.getService(msr);
		
        StringsCompleter delegate = new StringsCompleter();
		for( String mn : ms.getMandatorList() ){
	        delegate.getStrings().add(mn);
		}
        
        return delegate.complete(buffer, cursor, candidates);
	}

	/**
	 * @return the bundleContext
	 */
	public BundleContext getBundleContext() {
		return bundleContext;
	}

	/**
	 * @param bundleContext the bundleContext to set
	 */
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

}
