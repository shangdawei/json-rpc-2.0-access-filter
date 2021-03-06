Access Filter for JSON-RPC 2.0 Requests

Copyright (c) Vladimir Dzhuvinov, 2012 - 2015


README

Access filters for JSON-RPC 2.0 requests, for use on the server-side prior to 
request execution. Control access according to client host name / IP address, 
HTTPS transport, client X.509 certificate presence and / or API key policy.


Usage:

	JSON-RPC 2.0 services would typically plug in a CompositeFilter 
	instance and configure it with a simple properties file.


Package requirements:

	* Java 1.6 or later.
	
	* The package dependencies listed in the pom.xml.


For Java client, server and shell libraries implementing JSON-RPC 2.0 visit

	http://software.dzhuvinov.com/json-rpc-2.0.html


The JSON-RPC 2.0 specification and user group forum can be found at

        http://groups.google.com/group/json-rpc

[EOF]
