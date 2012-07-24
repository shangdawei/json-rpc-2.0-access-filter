package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import java.util.*;

import junit.framework.TestCase;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Tests the API key filter.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-24)
 */
public class APIKeyFilterTest extends TestCase {
	
	
	private Map<APIKey,Set<String>> getAPIKeyMap() {
	
		Map<APIKey,Set<String>> map = new HashMap<APIKey,Set<String>>();
		
		// Key one
		Set<String> methods = new HashSet<String>();
		methods.add("sso.login");
		methods.add("sso.logout");
		methods.add("sso.getSession");
		map.put(new APIKey("9cd19267"), methods);
		
		
		// Key two
		methods = new HashSet<String>();
		methods.add("sso.login");
		methods.add("sso.logout");
		methods.add("sso.getSession");
		map.put(new APIKey("b9f89662"), methods);
		
		return map;
	}
	
	
	public void testAllow() {
	
		APIKeyFilter filter = new APIKeyFilter();
		
		filter.init(getAPIKeyMap());
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("apiKey", "9cd19267");
		JSONRPC2Request req = new JSONRPC2Request("sso.login", params, 0);
		MessageContext ctx = new MessageContext();
		
		AccessFilterResult result = filter.filter(req, ctx);
		
		assertTrue(result.accessAllowed());
	}
	
	
	public void testDenyDueToMissingAPIKey() {
	
		APIKeyFilter filter = new APIKeyFilter();
		
		filter.init(getAPIKeyMap());
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("username", "alice");
		JSONRPC2Request req = new JSONRPC2Request("sso.login", params, 0);
		MessageContext ctx = new MessageContext();
		
		AccessFilterResult result = filter.filter(req, ctx);
		
		assertTrue(result.accessDenied());
		assertEquals(AccessDeniedError.MISSING_API_KEY.toJSONRPC2Error().getCode(),
		             result.getJSONRPC2Error().getCode());
	}
	
	
	public void testDenyDueToBadKey() {
	
		APIKeyFilter filter = new APIKeyFilter();
		
		filter.init(getAPIKeyMap());
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("apiKey", "no-such-key");
		JSONRPC2Request req = new JSONRPC2Request("sso.login", params, 0);
		MessageContext ctx = new MessageContext();
		
		AccessFilterResult result = filter.filter(req, ctx);
		
		assertTrue(result.accessDenied());
		assertEquals(AccessDeniedError.API_KEY_DENIED.toJSONRPC2Error().getCode(),
		             result.getJSONRPC2Error().getCode());
	}
}
