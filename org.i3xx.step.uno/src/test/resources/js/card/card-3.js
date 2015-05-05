i3xx.feature("i3xx.test.card-3");
i3xx.require("i3xx.i3xx");

i3xx.define("i3xx.test.fCard3", function (){
	
	//
	// This tests the remove of a card that has been executed
	//
	var sq = context.getService("org.i3xx.step.uno.Sequencer");
	sq.remove(sq.get(2));
	
	//
	// Increase the counter and put the new value to the (watched) variable at the context
	//
	var sc = context.getService("org.i3xx.step.uno.ContextService");
	var i = sc.get("test.counter")*1;
	sc.set("test.counter", (i+1));
	
	sc.set("test.name", "My counter "+sc.get("test.counter"));
	
});
