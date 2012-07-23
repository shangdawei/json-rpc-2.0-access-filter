/**
 * Filters JSON-RPC 2.0 requests on the server-side according to client IP
 * whitelist, client X.509 certificate or API token access policy.
 *
 * <p>Package dependencies:
 *
 * <ul>
 *     <li>JSON-RPC 2.0 Server, version 1.6 or compatible.
 *     <li>JSON-RPC 2.0 Base, version 1.27 or compatible.
 *     <li>Property Util, version 1.6 or compatible.
 *     <li>JSON Smart, version 1.1.1 or compatible.
 *     <li>Log4j, version 1.2 or compatible.
 * </ul>
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ ($version-date$)
 */
package com.thetransactioncompany.jsonrpc2.server.accessfilter;
