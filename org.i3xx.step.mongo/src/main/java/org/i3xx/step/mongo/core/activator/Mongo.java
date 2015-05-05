package org.i3xx.step.mongo.core.activator;

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


import org.i3xx.step.mongo.service.impl.MandatorTrackerImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Take a look at Jongo.org and MongoJack.org to make things better.
 * 
 * @author Stefan
 *
 */
public class Mongo implements BundleActivator {

	private static Logger logger = LoggerFactory.getLogger(Mongo.class);
	
	/**  */
	private MandatorTrackerImpl mandatorTracker;
	
	public Mongo() {
		mandatorTracker = null;
	}

	public void start(BundleContext context) throws Exception {
		logger.debug("Bundle starts");
		
		//
		// Setup the mandator session
		//
		mandatorTracker = new MandatorTrackerImpl(context);
		mandatorTracker.open();
	}

	public void stop(BundleContext context) throws Exception {
		logger.debug("Bundle stops");
		
		mandatorTracker.close();
	}

}
