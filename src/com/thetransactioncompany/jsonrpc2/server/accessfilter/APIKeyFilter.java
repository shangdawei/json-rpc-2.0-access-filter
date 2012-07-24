package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import java.util.Map;
import java.util.Set;

import com.thetransactioncompany.jsonrpc2.JSONRPC2ParamsType;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.util.NamedParamsRetriever;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Access filter based on a API key whitelist.
 *
 * <p>The filtered JSON-RPC 2.0 request must have named parameters and the API
 * must be passed in a string parameter called "apiKey".
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-23)
 */
public class APIKeyFilter implements AccessFilter {


	/**
	 * The API key store with matching allowed JSON-RPC 2.0 methods per key.
	 */
	private Map<APIKey,Set<String>> keyStore;
	
	
	/**
	 * Initialises this API key filter.
	 *
	 * @param keyStore The API key store with matching allowed JSON-RPC 2.0
	 *                 methods names per API key. Must not be {@code null}.
	 */
	public void init(final Map<APIKey,Set<String>> keyStore) {
	
		if (keyStore == null)
			throw new IllegalArgumentException("The API key store must not be null");
		
		this.keyStore = keyStore;
	}
	
	
	/**
	 * Returns the configured API key store.
	 *
	 * @return The API key store.
	 */
	public Map<APIKey,Set<String>> getAPIKeyStore() {
	
		return keyStore;
	}


	/**
	 * @inheritDoc
	 */
	public AccessFilterResult filter(final JSONRPC2Request request, 
	                                 final MessageContext messageCtx) {

		// Only named params expected
		if (! request.getParamsType().equals(JSONRPC2ParamsType.OBJECT))
			return new AccessFilterResult(AccessDeniedError.MISSING_API_KEY.toJSONRPC2Error());

		
		NamedParamsRetriever params = new NamedParamsRetriever((Map<String,Object>)request.getParams());
		
		// API key param present?
		APIKey key = null;
		
		try {
			key = new APIKey(params.getString("apiKey"));
			
		} catch (Exception e) {
		
			return new AccessFilterResult(AccessDeniedError.MISSING_API_KEY.toJSONRPC2Error());
		}
		
		// Get allowed methods for API key
		Set<String> allowedMethods = keyStore.get(key);
		
		if (allowedMethods == null)
			return new AccessFilterResult(AccessDeniedError.API_KEY_DENIED.toJSONRPC2Error());
		
		if (! allowedMethods.contains(request.getMethod()))
			return new AccessFilterResult(AccessDeniedError.API_KEY_DENIED.toJSONRPC2Error());
		
		return AccessFilterResult.ACCESS_ALLOWED;
	}
}
