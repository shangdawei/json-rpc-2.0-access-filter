package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import java.net.UnknownHostException;

import junit.framework.TestCase;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Tests the host name / IP address filter.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-30)
 */
public class HostFilterTest extends TestCase {
	
	
	public void testRun()
		throws UnknownHostException {
	
		String allow = "192.168.0.1 localhost 192.168.1.100";
		
		HostFilter filter = new HostFilter();
		filter.init(allow);
		
		assertEquals(allow, filter.getOriginalAllowedHosts());
	
		String resolvedIPs = filter.getResolvedAllowedIPs();
		assertNotNull(resolvedIPs);
		System.out.println("Host filter: Resolved IPs: " + resolvedIPs);
		
		assertTrue(resolvedIPs.contains("192.168.0.1"));
		assertTrue(resolvedIPs.contains("127.0.0.1")); // localhost
		assertTrue(resolvedIPs.contains("192.168.1.100"));
		
		assertTrue(filter.isAllowedIP("192.168.0.1"));
		assertTrue(filter.isAllowedIP("127.0.0.1"));
		assertTrue(filter.isAllowedIP("192.168.1.100"));
		
		assertFalse(filter.isAllowedIP("192.168.10.20"));
		
		JSONRPC2Request req = new JSONRPC2Request("users.list", 0);
		MessageContext ctx = new MessageContext(null, "192.168.0.1");
		
		AccessFilterResult result = filter.filter(req, ctx);
		assertTrue(result.accessAllowed());
		
		ctx = new MessageContext(null, "192.168.10.20");
		
		result = filter.filter(req, ctx);
		assertTrue(result.accessDenied());
		assertEquals(AccessDeniedError.CLIENT_IP_DENIED,
		             result.getAccessDeniedError());
	}
}
