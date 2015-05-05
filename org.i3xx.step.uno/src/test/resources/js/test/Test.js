i3xx.feature("i3xx.test.Test");
i3xx.require("i3xx.i3xx");

function init() {

	var sc = context.getService("org.i3xx.step.uno.ContextService");
	
	//
	// Watch the variable and call 'i3xx.test.notify' at any change
	//
	var nf = context.getService("org.i3xx.step.uno.NotifyValueService");
	nf.register("test.name", wrapFunction(i3xx.test.notify, nf, null));
	
	//
	// Set the counter and put the new value to the (watched) variable at the context
	//
	sc.set("test.counter", 0);
	sc.set("test.name", "My counter "+sc.get("test.counter"));
	
	//
	// The test must produce this output:
	//
	//:::>test.name null My counter 0.0
	//:::>test.name My counter 0.0 My counter 1.0
	//:::>test.name My counter 1.0 My counter 2.0
	//:::>test.name My counter 2.0 My counter 3.0
	//:::>test.name My counter 3.0 My counter 4.0
	//:::>test.name My counter 4.0 My counter 5.0
	//:::>test.name My counter 5.0 My counter 6.0	
}

function test() {

	var sc = context.getService("org.i3xx.step.uno.ContextService");
	
	//
	// Increase the counter and put the new value to the (watched) variable at the context
	//
	var i = sc.get("test.counter")*1;
	sc.set("test.counter", (i+1));
	sc.set("test.name", "My counter "+sc.get("test.counter"));
	
	//
	//Remove a card
	//
	var sq = context.getService("org.i3xx.step.uno.Sequencer");
	sq.remove(sq.get(0));
	
	//
	//Add five cards
	//
	sq.add( sq.create("Card-0", "i3xx.test.fCard0", 0, 0, 0, false) );
	sq.add( sq.create("Card-1", "i3xx.test.fCard1", 0, 0, 0, false) );
	sq.add( sq.create("Card-2", "i3xx.test.fCard2", 0, 0, 0, false) );
	sq.add( sq.create("Card-3", "i3xx.test.fCard3", 0, 0, 0, false) );
	sq.add( sq.create("Card-4", "i3xx.test.fCard4", 0, 0, 0, false) );
}

i3xx.define("i3xx.test.notify", function(a, b, c) {
	
	//
	//Print the changes of the watched variable at the context
	//
	println(":::>"+a+" "+b+" "+c);
	
});

