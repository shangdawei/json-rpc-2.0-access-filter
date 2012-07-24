package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import junit.framework.TestCase;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Tests the HTTPS filter.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-24)
 */
public class HTTPSFilterTest extends TestCase {
	
	
	private static final boolean HTTPS_ON = true;
	
	
	private static final boolean HTTPS_OFF = false;
	
	
	public void testAccessAllowedWithFilterEnabled() {

		HTTPSFilter filter = new HTTPSFilter();
		
		boolean requireHTTPS = true;
		filter.init(requireHTTPS);
			
		JSONRPC2Request req = new JSONRPC2Request("listUsers", 0);
		MessageContext ctx = new MessageContext("localhost", "127.0.0.1", HTTPS_ON);
		
		AccessFilterResult result = filter.filter(req, ctx);
		
		assertTrue("Access allowed test", result.accessAllowed());
	}
	
	
	public void testAccessDeniedWithFilterEnabled() {

		HTTPSFilter filter = new HTTPSFilter();
		
		boolean requireHTTPS = true;
		filter.init(requireHTTPS);
			
		JSONRPC2Request req = new JSONRPC2Request("listUsers", 0);
		MessageContext ctx = new MessageContext("localhost", "127.0.0.1", HTTPS_OFF);
		
		AccessFilterResult result = filter.filter(req, ctx);
		
		assertTrue("Access denied test", result.accessDenied());
		assertNotNull("JSON-RPC 2.0 error not null test", result.getJSONRPC2Error());
		assertEquals("JSON-RPC 2.0 error code test", AccessDeniedError.HTTPS_REQUIRED.code, result.getJSONRPC2Error().getCode());
	}
	
	
	public void testFilterDisabled() {
	
		HTTPSFilter filter = new HTTPSFilter();
		
		boolean requireHTTPS = false;
		filter.init(requireHTTPS);
		
		JSONRPC2Request req = new JSONRPC2Request("listUsers", 0);
		
		MessageContext ctx = new MessageContext("localhost", "127.0.0.1", HTTPS_OFF);
		AccessFilterResult result = filter.filter(req, ctx);
		assertTrue("Access allowed test", result.accessAllowed());
		
		
		ctx = new MessageContext("localhost", "127.0.0.1", HTTPS_ON);
		result = filter.filter(req, ctx);
		assertTrue("Access allowed test", result.accessAllowed());
	}
}
