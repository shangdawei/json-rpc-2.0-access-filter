package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import java.util.*;

import junit.framework.TestCase;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Tests the API key filter.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-08-23)
 */
public class APIKeyFilterTest extends TestCase {
	
	
	private static Map<APIKey,Set<String>> getAPIKeyMap() {
	
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
		
		// Key three - allow any method
		methods = new HashSet<String>();
		methods.add("*");
		map.put(new APIKey("7cf1beda"), methods);
		
		return map;
	}
	
	
	private static Set<String> getExemptedMethods() {
	
		Set<String> methods = new HashSet<String>();
		methods.add("ws.getName");
		methods.add("ws.getVersion");
		methods.add("ws.getTime");
		return methods;
	}
	
	
	public void testAllow() {
	
		APIKeyFilter filter = new APIKeyFilter();
		
		filter.init(getAPIKeyMap(), getExemptedMethods());
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("apiKey", "9cd19267");
		JSONRPC2Request req = new JSONRPC2Request("sso.login", params, 0);
		MessageContext ctx = new MessageContext();
		
		AccessFilterResult result = filter.filter(req, ctx);
		
		assertTrue(result.accessAllowed());
	}
	
	
	public void testAllowAny() {
	
		APIKeyFilter filter = new APIKeyFilter();
		
		filter.init(getAPIKeyMap(), getExemptedMethods());
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("apiKey", "7cf1beda");
		JSONRPC2Request req = new JSONRPC2Request("sso.login", params, 0);
		MessageContext ctx = new MessageContext();
		
		AccessFilterResult result = filter.filter(req, ctx);
		
		assertTrue(result.accessAllowed());
	}
	
	
	public void testDenyDueToMissingAPIKey() {
	
		APIKeyFilter filter = new APIKeyFilter();
		
		filter.init(getAPIKeyMap(), getExemptedMethods());
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("username", "alice");
		JSONRPC2Request req = new JSONRPC2Request("sso.login", params, 0);
		MessageContext ctx = new MessageContext();
		
		AccessFilterResult result = filter.filter(req, ctx);
		
		assertTrue(result.accessDenied());
		assertEquals(AccessDeniedError.MISSING_API_KEY.toJSONRPC2Error().getCode(),
		             result.getAccessDeniedError().toJSONRPC2Error().getCode());
	}
	
	
	public void testDenyDueToBadKey() {
	
		APIKeyFilter filter = new APIKeyFilter();
		
		filter.init(getAPIKeyMap(), getExemptedMethods());
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("apiKey", "no-such-key");
		JSONRPC2Request req = new JSONRPC2Request("sso.login", params, 0);
		MessageContext ctx = new MessageContext();
		
		AccessFilterResult result = filter.filter(req, ctx);
		
		assertTrue(result.accessDenied());
		assertEquals(AccessDeniedError.API_KEY_DENIED.toJSONRPC2Error().getCode(),
		             result.getAccessDeniedError().toJSONRPC2Error().getCode());
	}
	
	
	public void testAllowExemptedMethod() {
	
		APIKeyFilter filter = new APIKeyFilter();
		
		filter.init(getAPIKeyMap(), getExemptedMethods());
		
		Map<String,Object> params = new HashMap<String,Object>();
		JSONRPC2Request req = new JSONRPC2Request("ws.getTime", params, 0);
		MessageContext ctx = new MessageContext();
		
		AccessFilterResult result = filter.filter(req, ctx);
		
		assertTrue(result.accessAllowed());
	}
}
