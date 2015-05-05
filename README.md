# step

Engine to run javascript in an OSGi server with the support of multiple mandators and access to other OSGi bundles. The engine has a few features of a workflow engine but doesn't support lanuages like BPMN.

The resource delivered by the OSGi server should have a RESTful API. The javascript sets the resource from one state to another.

The project should have a small footprint with the possibility to delegate work to other (software) products if necessary. But not to have any hard dependency. One goal of this project is to get a few bundles from maven and start.

Some things inside of the server have RPC like APIs. These resources are not visible from outside the server. There are three storage areas that depends on a (maybe different) session id.

1. The Apache Shiro session and it's attributes.
2. The serializable context of the script.
3. The step session cache of the public data.

These storage areas have different use cases. The Shiro session is for user (subject) session and security purposes. The script engine uses a serializable, persistable context to store data between single calls to scripts (if necessary). The session cache provides a non persistent storage area per session and per mandator.

The shiro session may be used to transport data. The script context is available to the javascript running in the script engine. Each call to a script by the REST API causes a new, empty script context. A script may persist data by serializing the context or loading the data of a serialized context to it's own context.

If some data must be shared between different REST API calls, the step session cache must be used. This project implements only simple caches. It is necessary to use the interfaces, because we plan to support products like Ehcache in future.
