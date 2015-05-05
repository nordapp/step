i3xx.feature("i3xx.group-2.artifact-1");
i3xx.require("i3xx.i3xx");
i3xx.require("i3xx.json");

i3xx.define("i3xx.group-2.artifact-1.upload", function() {

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

	//get access to an OSGi session service. This session service is used to request
	//data to the client or put data into documents. put data to the service.
	var ses = srv.getService("org.i3xx.step.due.service.model.Session", "{sessionID:\""+s_uuid+"\"}");
	ses.setValue("processUUID", httpio.getProcessUUID());
	
	//process the uploaded files
	nms = httpio.getFilenames();
	for(var i in nms){
		var uuid = nms[i];
		var item = httpio.getFile(uuid);
		var file = fst.createFile(item.getInputStream());
		file.setContentType(item.getContentType());
		file.setFilename(item.getName());
		file.save();
		
		var id = file.getId();
		var data = "{id: \""+uuid+"\", process: \""+httpio.getProcessUUID()+"\", ref: \""+id+"\"}";
		var filter = "{id: \""+uuid+"\"}";
		col.update(filter, data, true, false);
		
		log("Insert document id:"+uuid+" by process:"+httpio.getProcessUUID()+" ref:"+id+"");
		
		ses.setValue("upload.file."+i, ""+uuid.toString());
	}
	ses.setValue("upload.length", ""+nms.length);

});
