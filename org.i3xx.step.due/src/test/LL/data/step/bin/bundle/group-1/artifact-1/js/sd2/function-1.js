i3xx.feature("i3xx.group-2.artifact-1");
i3xx.require("i3xx.i3xx");
i3xx.require("i3xx.json");

i3xx.define("i3xx.group-2.artifact-1.get", function() {

	//get data from the script context
	var sc = context.getService("org.i3xx.step.uno.ContextService");
	var httpio = sc.get("http.io");
	var s_uuid = sc.get("http.sessionID");
	var p_uuid = sc.get("http.processUUID");
	
	//put data to the script context
	sc.set("test.counter", 1);
	sc.set("http.io.procId", ""+httpio.getProcessUUID());
	
	//get the database access from an OSGi service
	var srv = context.getService("org.i3xx.step.uno.AccessOSGiService");
	var drv = srv.getService("org.i3xx.step.mongo.service.model.DatabaseService", "{}");
	var dbs = drv.getDatabase();
	var col = dbs.getCollection("docs");
	var fst = dbs.getFileStore();
	
	var names = httpio.getFieldnames();
	var fUuid = httpio.getField( names[0] );
	var query  = "{id: \""+fUuid+"\"}";
	var reslt = col.findOne(query, "{ _id: false, ref: true }");
	var file = fst.getFileFromId( reslt.get("ref") );
	
	httpio.setFile(fUuid, file);
});