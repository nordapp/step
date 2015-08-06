package org.i3xx.step.command.zero.core.impl;

import java.util.List;

import org.apache.karaf.shell.console.Completer;
import org.apache.karaf.shell.console.completer.StringsCompleter;

public class SetupCompleter implements Completer {

	public SetupCompleter() {
	}

	public int complete(String buffer, int cursor, List<String> candidates) {

        StringsCompleter delegate = new StringsCompleter();
        //delegate.getStrings().add("default");
        //delegate.getStrings().add("step");
        //delegate.getStrings().add("feature");
        
        return delegate.complete(buffer, cursor, candidates);
	}
}
