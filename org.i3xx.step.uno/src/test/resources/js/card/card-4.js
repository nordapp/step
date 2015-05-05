i3xx.feature("i3xx.test.card-4");
i3xx.require("i3xx.i3xx");

i3xx.define("i3xx.test.fCard4", function (){

	//
	// Increase the counter and put the new value to the (watched) variable at the context
	//
	var sc = context.getService("org.i3xx.step.uno.ContextService");
	var i = sc.get("test.counter")*1;
	sc.set("test.counter", (i+1));
	
	sc.set("test.name", "My counter "+sc.get("test.counter"));
	
});
