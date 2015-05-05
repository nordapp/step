package org.i3xx.step.uno.core.impl.cmd;

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


import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(scope = "ob", name = "deploy-help", description="Deploys a help text")
public class DeployHelpCommand extends OsgiCommandSupport {
	
	static Logger logger = LoggerFactory.getLogger(DeployHelpCommand.class);
	
    @Argument(index = 0, name = "command", description = "The command to print the helptext", required = false, multiValued = false)
    private String command = null;
	
	public DeployHelpCommand() {
		super();
	}
    
	@Override
	protected Object doExecute() throws Exception {
		
		//logger.info("");
		if(command==null)
			printUsage0();
		else if(command.trim().equalsIgnoreCase("default"))
			printUsage1();
		else if(command.trim().equalsIgnoreCase("step"))
			printUsage2();
		else if(command.trim().equalsIgnoreCase("feature"))
			printUsage3();
		else
			System.out.println("Unknown command "+command);
		
        return null;
	}
	
	private void printUsage0() {
		
		System.out.println("Usage:");
		System.out.println("ob:deploy-help ['default' | 'step' | 'feature' ]");
		System.out.println("ob:deploy-default arg0 arg1 arg2 arg3");
		System.out.println("ob:deploy-step arg0 arg1 arg2 arg3 arg4");
		System.out.println("ob:deploy-feature arg0 arg1 arg2 arg3 arg4 arg5");
	}
	
	private void printUsage1() {
		
		System.out.println("Usage:");
		System.out.println("ob:deploy-default arg0 arg1 arg2 arg3");
		System.out.println("arg0: The ID of the mandator to deploy to.");
		System.out.println("arg1: The path to deploy the resource or file to.");
		System.out.println("arg2: The URI of the resource (file, url).");
		System.out.println("arg3: The target filename the resource will be stored.");
		System.out.println("\nExamples (feature-file):\n");
		System.out.println("\"default\" \"$0\" \"\" \"file:///D:/eclipse/workspace/org.i3xx.step.uno/src/test/resources/test-base/bin/main/js/i3xx-small.js\" \"i3xx-small.js\"");
		System.out.println("\"default\" \"$0\" \"\" \"file:///D:/eclipse/workspace/org.i3xx.step.uno/src/test/resources/test-base/bin/main/js/json.js\" \"json.js\"");
	}
	
	private void printUsage2() {
		
		System.out.println("Usage:");
		System.out.println("ob:deploy-step arg0 arg1 arg2 arg3 arg4");
		System.out.println("arg0: The ID of the mandator to deploy to.");
		System.out.println("arg1: The group-ID of the bundle.");
		System.out.println("arg2: The artifact-ID of the bundle.");
		System.out.println("arg3: The URI of the resource (file, url).");
		System.out.println("arg4: The target filename the resource will be stored.");
		System.out.println("\nExamples (feature-file):\n");
		System.out.println("\"step\" \"IDS\" \"group-1\" \"artifact-1\" \"file:///D:/eclipse/workspace/org.i3xx.step.uno/src/test/resources/test-base/bin/bundle/group-1/artifact-1/js/card-1.js\" \"card-1.js\"");
		System.out.println("\"step\" \"IDS\" \"group-1\" \"artifact-1\" \"file:///D:/eclipse/workspace/org.i3xx.step.uno/src/test/resources/test-base/bin/bundle/group-1/artifact-1/js/card-1.mf\" \"card-1.mf\"");
		System.out.println("\"step\" \"IDS\" \"group-1\" \"artifact-1\" \"file:///D:/eclipse/workspace/org.i3xx.step.uno/src/test/resources/test-base/bin/bundle/group-1/artifact-1/js/card-2.js\" \"card-2.js\"");
		System.out.println("\"step\" \"IDS\" \"group-1\" \"artifact-1\" \"file:///D:/eclipse/workspace/org.i3xx.step.uno/src/test/resources/test-base/bin/bundle/group-1/artifact-1/js/card-2.mf\" \"card-2.mf\"");

	}
	
	private void printUsage3() {
		
		System.out.println("Usage:");
		System.out.println("ob:deploy-feature arg0 arg1 arg2 arg3 arg4 arg5");
		System.out.println("arg0: The feature file.");
		System.out.println("arg1: Optional parameter $0");
		System.out.println("arg2: Optional parameter $1");
		System.out.println("arg3: Optional parameter $2");
		System.out.println("arg4: Optional parameter $3");
		System.out.println("arg4: Optional parameter $4");
		System.out.println("\nExamples:\n");
		System.out.println("Using the parameter arg1 as $0 in a feature file.");
		System.out.println("The feature file contains the following line:");
		System.out.println("\"step\" \"$0\" \"group-1\" \"artifact-1\" \"file:///D:/eclipse/workspace/org.i3xx.step.uno/src/test/resources/test-base/bin/bundle/group-1/artifact-1/js/card-2.mf\" \"card-2.mf\"");
	}
}
