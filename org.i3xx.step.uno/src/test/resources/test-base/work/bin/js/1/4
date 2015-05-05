i3xx.feature("i3xx.group-1.artifact-2");
i3xx.require("i3xx.i3xx");
i3xx.require("i3xx.json");

i3xx.define("i3xx.group-1.artifact-2.activate", function() {

	println("Hello world from group-1 :: artifact-2 :: card-2");
	
	var sc = context.getService("org.i3xx.step.uno.ContextService");
	sc.set("test.counter", 1);
	
	var s = "{ name:'Stefan Hauptmann' }";
	var j = i3xx.fromJson( s );
	sc.set("test.data", j);
	
	println(j);
});