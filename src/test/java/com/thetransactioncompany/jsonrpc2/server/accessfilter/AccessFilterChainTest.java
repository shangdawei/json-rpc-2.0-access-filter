package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import java.util.*;

import junit.framework.TestCase;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Tests the access filter chain class.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-30)
 */
public class AccessFilterChainTest extends TestCase {


	public void testSingleFilterChain()
		throws Exception {

		AccessFilterChain chain = new AccessFilterChain();
		assertTrue(chain.getAccessFilters().isEmpty());
		
		HTTPSFilter httpsFilter = new HTTPSFilter();
		httpsFilter.init(true);
		
		chain.add(httpsFilter);
		assertEquals(1, chain.getAccessFilters().size());
		
		JSONRPC2Request req = new JSONRPC2Request("ws.getTime", 0);
		MessageContext mctx = new MessageContext("localhost", "127.0.0.1", true);
		
		AccessFilterResult result = chain.filter(req, mctx);
		assertNotNull(result);
		assertTrue(result.accessAllowed());
		
		chain.clear();
		assertTrue(chain.getAccessFilters().isEmpty());
		
		// Request must pass with empty chain
		req = new JSONRPC2Request("ws.getTime", 0);
		mctx = new MessageContext("localhost", "127.0.0.1", false);
		
		result = chain.filter(req, mctx);
		assertNotNull(result);
		assertTrue(result.accessAllowed());
	}
	
	
	public void testTwoFilterChain()
		throws Exception {

		AccessFilterChain chain = new AccessFilterChain();
		assertTrue(chain.getAccessFilters().isEmpty());
		
		HTTPSFilter httpsFilter = new HTTPSFilter();
		httpsFilter.init(true);
		
		chain.add(httpsFilter);
		assertEquals(1, chain.getAccessFilters().size());
		
		HostFilter hostFilter = new HostFilter();
		hostFilter.init("127.0.0.1");
		
		chain.add(hostFilter);
		assertEquals(2, chain.getAccessFilters().size());
		
		JSONRPC2Request req = new JSONRPC2Request("ws.getTime", 0);
		MessageContext mctx = new MessageContext("localhost", "127.0.0.1", true);
		
		AccessFilterResult result = chain.filter(req, mctx);
		assertNotNull(result);
		assertTrue(result.accessAllowed());
		
		chain.clear();
		assertTrue(chain.getAccessFilters().isEmpty());
		
		// Request must pass with empty chain
		req = new JSONRPC2Request("ws.getTime", 0);
		mctx = new MessageContext(null, "192.168.0.1", false);
		
		result = chain.filter(req, mctx);
		assertNotNull(result);
		assertTrue(result.accessAllowed());
	}
	
	
	public void testTwoFilterChainAccessDenied()
		throws Exception {

		AccessFilterChain chain = new AccessFilterChain();
		assertTrue(chain.getAccessFilters().isEmpty());
		
		HTTPSFilter httpsFilter = new HTTPSFilter();
		httpsFilter.init(true);
		
		chain.add(httpsFilter);
		assertEquals(1, chain.getAccessFilters().size());
		
		HostFilter hostFilter = new HostFilter();
		hostFilter.init("127.0.0.1");
		
		chain.add(hostFilter);
		assertEquals(2, chain.getAccessFilters().size());
		
		JSONRPC2Request req = new JSONRPC2Request("ws.getTime", 0);
		MessageContext mctx = new MessageContext("localhost", "127.0.0.1", false);
		
		AccessFilterResult result = chain.filter(req, mctx);
		assertNotNull(result);
		assertTrue(result.accessDenied());
		assertEquals(AccessDeniedError.HTTPS_REQUIRED, result.getAccessDeniedError());
		
		
		req = new JSONRPC2Request("ws.getTime", 0);
		mctx = new MessageContext("localhost", "192.168.0.1", true);
		
		result = chain.filter(req, mctx);
		assertNotNull(result);
		assertTrue(result.accessDenied());
		assertEquals(AccessDeniedError.CLIENT_IP_DENIED, result.getAccessDeniedError());
		
		
		chain.clear();
		assertTrue(chain.getAccessFilters().isEmpty());
		
		// Request must pass with empty chain
		req = new JSONRPC2Request("ws.getTime", 0);
		mctx = new MessageContext(null, "192.168.0.1", false);
		
		result = chain.filter(req, mctx);
		assertNotNull(result);
		assertTrue(result.accessAllowed());
	}
}
