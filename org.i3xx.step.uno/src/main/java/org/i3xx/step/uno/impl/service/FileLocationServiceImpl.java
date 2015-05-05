package org.i3xx.step.uno.impl.service;

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


import java.io.IOException;

import org.i3xx.step.uno.model.service.FileLocationService;
import org.i3xx.step.zero.service.model.mandator.Mandator;
import org.i3xx.util.basic.io.FilePath;
import org.i3xx.util.basic.platform.Platform;
import org.i3xx.util.general.setup.impl.Setup;
import org.i3xx.util.general.setup.model.SetupService;
import org.i3xx.util.platform.impl.AvailableKeys;
import org.osgi.framework.BundleContext;


public class FileLocationServiceImpl implements FileLocationService {
	
	private final FilePath location;
	
	public FileLocationServiceImpl(BundleContext bundleContext, SetupService setupService, Platform platformService) throws IOException {
		
		Setup setup = setupService.getGeneralSetup();
		String _root = Setup.setCurrentId(setup.getRoot(), "");
		FilePath resLoc = null;
		if(_root.startsWith(".")){
			String home = (String)platformService.getObject(AvailableKeys.SERVER_HOME);
			resLoc = FilePath.get(home).add(_root.substring(1));
		}else{
			resLoc = FilePath.get(_root);
		}
		
		location = resLoc;
	}

	public FilePath getLocation(Mandator mandator) {
		//
		// TODO
		//
		
		return location;
	}
	
	public void setLocation(FilePath location) {
		throw new UnsupportedOperationException("The property 'location' is final and cannot be set.");
		//this.location = location;
	}

}
