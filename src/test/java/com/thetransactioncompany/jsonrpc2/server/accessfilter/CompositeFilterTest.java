package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import java.util.*;

import junit.framework.TestCase;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Tests the composite filter.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2013-03-29)
 */
public class CompositeFilterTest extends TestCase {


	public void testInit()
		throws Exception {

		Properties props = CompositeFilterConfigurationTest.getAllConfigPropertiesSet();
		
		CompositeFilter filter = new CompositeFilter();
		
		filter.init(new CompositeFilterConfiguration(props));
		
		assertNotNull(filter.getConfiguration());
	}
	
	
	public void testAllow()
		throws Exception {
	
		JSONRPC2Request req = new JSONRPC2Request("ws.getTime", 0);
		MessageContext mctx = new MessageContext("localhost", 
		                                         "127.0.0.1", 
							 true,
							 "cn=John Doe,ou=people,cd=company,dc=org");
		
		CompositeFilter filter = new CompositeFilter();
		filter.init(new CompositeFilterConfiguration(CompositeFilterConfigurationTest.getAllConfigPropertiesSet()));
	
		AccessFilterResult result = filter.filter(req, mctx);
		
		assertNotNull("Result must not be null", result);
		assertTrue("Access must be allowed", result.accessAllowed());
	}
	
	
	public void testDenyDueToNonObjectParams()
		throws Exception {
	
		JSONRPC2Request req = new JSONRPC2Request("sso.login", 0);
		MessageContext mctx = new MessageContext("localhost", 
		                                         "127.0.0.1", 
							 true,
							 "cn=John Doe,ou=people,cd=company,dc=org");
		
		CompositeFilter filter = new CompositeFilter();
		filter.init(new CompositeFilterConfiguration(CompositeFilterConfigurationTest.getAllConfigPropertiesSet()));
	
		AccessFilterResult result = filter.filter(req, mctx);
		
		assertNotNull("Result must not be null", result);
		assertTrue("Access must be denied", result.accessDenied());
		assertEquals(AccessDeniedError.API_KEY_REQUIRES_NAMED_PARAM, result.getAccessDeniedError());
	}


	public void testDenyDueToMissingAPIKey()
		throws Exception {
	
		Map<String,Object> params = new HashMap<String,Object>();
		JSONRPC2Request req = new JSONRPC2Request("sso.login", params, 0);
		MessageContext mctx = new MessageContext("localhost", 
		                                         "127.0.0.1", 
							 true,
							 "cn=John Doe,ou=people,cd=company,dc=org");
		
		CompositeFilter filter = new CompositeFilter();
		filter.init(new CompositeFilterConfiguration(CompositeFilterConfigurationTest.getAllConfigPropertiesSet()));
	
		AccessFilterResult result = filter.filter(req, mctx);
		
		assertNotNull("Result must not be null", result);
		assertTrue("Access must be denied", result.accessDenied());
		assertEquals(AccessDeniedError.MISSING_API_KEY, result.getAccessDeniedError());
	}
}
