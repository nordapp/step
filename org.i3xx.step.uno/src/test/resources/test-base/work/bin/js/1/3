i3xx.feature("i3xx.group-1.artifact-1");
i3xx.require("i3xx.i3xx");
i3xx.require("i3xx.json");

i3xx.define("i3xx.group-1.artifact-1.activate", function() {

	println("Hello world from group-1 :: artifact-1 :: card-1");
	
	var sc = context.getService("org.i3xx.step.uno.ContextService");
	var j = sc.get("test.data");
	
	println( j.name );
});