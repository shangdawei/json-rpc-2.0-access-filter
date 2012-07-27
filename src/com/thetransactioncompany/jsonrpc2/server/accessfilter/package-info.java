/**
 * Access filters for JSON-RPC 2.0 requests, for use on the server-side prior to
 * request execution. Control access according to client host name / IP address,
 * HTTPS transport, client X.509 certificate presence and / or API key policy.
 *
 * <p>JSON-RPC 2.0 services would typically plug in a 
 * {@link com.thetransactioncompany.jsonrpc2.server.accessfilter.CompositeFilter} 
 * instance.
 *
 * <p>Package dependencies:
 *
 * <ul>
 *     <li>JSON-RPC 2.0 Server, version 1.6 or compatible.
 *     <li>JSON-RPC 2.0 Base, version 1.27 or compatible.
 *     <li>Property Util, version 1.6 or compatible.
 *     <li>JSON Smart, version 1.1.1 or compatible.
 *     <li>UnboundID LDAP SDK, version 2.3 or compatible.
 *     <li>Log4j, version 1.2 or compatible.
 *     <li>Servlet API, version 2.5 or higher.
 * </ul>
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ ($version-date$)
 */
package com.thetransactioncompany.jsonrpc2.server.accessfilter;
