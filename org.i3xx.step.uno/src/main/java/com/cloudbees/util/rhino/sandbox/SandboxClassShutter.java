/*
 * Copyright 2013, CloudBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudbees.util.rhino.sandbox;

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

import java.util.HashSet;
import java.util.Set;

import org.mozilla.javascript.ClassShutter;

/**
 * A {@link ClassShutter} that locks out access to all native classes.
 */
public class SandboxClassShutter implements ClassShutter {
	
	//Adds by Stefan Hauptmann, 19.11.2014, I.D.S DialogSysteme GmbH | nordAPP
	static Set<String> validNames = new HashSet<String>();
	
	//Adds by Stefan Hauptmann, 19.11.2014, I.D.S DialogSysteme GmbH | nordAPP
	static {
		validNames.add("java.lang.String");
		validNames.add("java.lang.Long");
		validNames.add("java.lang.Integer");
		validNames.add("java.lang.Short");
		validNames.add("java.lang.Byte");
		validNames.add("java.lang.Double");
		validNames.add("java.lang.Float");
		validNames.add("java.math.BigInteger");
		validNames.add("java.math.BigDecimal");
		validNames.add("org.i3xx.step.uno.impl.ContextImpl");
		validNames.add("org.i3xx.step.uno.impl.service.builtin.AccessOSGiService");
		validNames.add("org.i3xx.step.uno.impl.service.builtin.ContextServiceAdapter");
		validNames.add("org.i3xx.step.uno.impl.service.builtin.ContextAdministrationService");
		validNames.add("org.i3xx.step.uno.impl.service.builtin.KeyIndexService");
		validNames.add("org.i3xx.step.uno.impl.service.builtin.NotifyValueService");
		validNames.add("org.i3xx.step.uno.impl.service.builtin.ServiceFactoryService");
		validNames.add("org.i3xx.step.uno.impl.service.builtin.VerifyValueService");
		validNames.add("org.i3xx.step.uno.impl.service.builtin.AccessOSGiService");
		validNames.add("org.i3xx.step.uno.impl.SequencerImpl");
		validNames.add("org.i3xx.step.uno.impl.StepCardImpl");
	}
	
    public boolean visibleToScripts(String fullClassName) {
    	//Adds by Stefan Hauptmann, 19.11.2014, I.D.S DialogSysteme GmbH | nordAPP
    	if(validNames.contains(fullClassName)) {
    		return true;
    	}

        return false;
    }
}
