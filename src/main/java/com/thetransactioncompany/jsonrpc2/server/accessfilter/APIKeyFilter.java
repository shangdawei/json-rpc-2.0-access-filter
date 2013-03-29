package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import java.util.Map;
import java.util.Set;

import com.thetransactioncompany.jsonrpc2.JSONRPC2ParamsType;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.util.NamedParamsRetriever;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Access filter ensuring clients present an API key for protected JSON-RPC 2.0
 * request methods. An API key may be given access to zero, more or any 
 * (indicated by asterisk) methods.
 *
 * <p>The filtered JSON-RPC 2.0 request must have named parameters and the API
 * key must be passed in a designated {@link #DEFAULT_API_KEY_PARAMETER_NAME 
 * string parameter}.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2013-03-29)
 */
public class APIKeyFilter implements AccessFilter {


	/**
	 * Map of API keys to their allowed JSON-RPC 2.0 methods.
	 */
	private Map<APIKey,Set<String>> keyMap;
	
	
	/**
	 * Exempted JSON-RPC 2.0 methods.
	 */
	private Set<String> exemptedMethods;
	
	
	/**
	 * The JSON-RPC 2.0 parameter used to pass the API key.
	 */
	private String apiKeyParamName;
	
	
	/**
	 * The default name of the JSON-RPC 2.0 parameter used to pass the API
	 * key.
	 */
	public static final String DEFAULT_API_KEY_PARAMETER_NAME = "apiKey";
	
	
	/**
	 * Initialises this API key filter. The name of the JSON-RPC 2.0 
	 * parameter used to pass the API key is set to 
	 * {@link #DEFAULT_API_KEY_PARAMETER_NAME}.
	 *
	 * @param keyMap          Map of API keys to their allowed JSON-RPC 2.0
	 *                        methods. If a method name is set to "*" 
	 *                        (asterisk) then any method is allowed for the
	 *                        API key. Must not be {@code null}.
	 * @param exemptedMethods Exempted JSON-RPC 2.0 methods for which an API
	 *                        key is not required. Must not be {@code null}.
	 */
	public void init(final Map<APIKey,Set<String>> keyMap,
	                 final Set<String> exemptedMethods) {
			 
		init(keyMap, exemptedMethods, DEFAULT_API_KEY_PARAMETER_NAME);
	}
	
	
	/**
	 * Initialises this API key filter.
	 *
	 * @param keyMap          Map of API keys to their allowed JSON-RPC 2.0
	 *                        methods. If a method name is set to "*" 
	 *                        (asterisk) then any method is allowed for the
	 *                        API key. Must not be {@code null}.
	 * @param exemptedMethods Exempted JSON-RPC 2.0 methods for which an API
	 *                        key is not required. Must not be {@code null}.
	 * @param apiKeyParamName The name of the JSON-RPC 2.0 parameter used to
	 *                        pass the API key. Must not be {@code null}.
	 */
	public void init(final Map<APIKey,Set<String>> keyMap,
	                 final Set<String> exemptedMethods,
			 final String apiKeyParamName) {
	
		if (keyMap == null)
			throw new IllegalArgumentException("The API key map must not be null");
		
		this.keyMap = keyMap;
		
		if (exemptedMethods == null)
			throw new IllegalArgumentException("The exempted methods must not be null");
		
		this.exemptedMethods = exemptedMethods;
		
		if (apiKeyParamName == null)
			throw new IllegalArgumentException("The API key parameter name must not be null");
		
		this.apiKeyParamName = apiKeyParamName;
	}
	
	
	/**
	 * Gets the configured API key map.
	 *
	 * @return The API key map.
	 */
	public Map<APIKey,Set<String>> getAPIKeyMap() {
	
		return keyMap;
	}
	
	
	/**
	 * Gets the exempted JSON-RPC 2.0 methods for which an API key is not
	 * required.
	 *
	 * @return The exempted JSON-RPC 2.0 methods, empty set if none.
	 */
	public Set<String> getExemptedMethods() {
	
		return exemptedMethods;
	}
	
	
	/**
	 * Gets name of the JSON-RPC 2.0 parameter used to pass the API key.
	 *
	 * @return The name of the JSON-RPC 2.0 parameter used to pass the API
	 *         key.
	 */
	public String getAPIKeyParameterName() {
	
		return apiKeyParamName;
	}


	/**
	 * @inheritDoc
	 */
	@SuppressWarnings("unchecked")
	public AccessFilterResult filter(final JSONRPC2Request request, 
	                                 final MessageContext messageCtx) {

		// Exempted method?
		if (exemptedMethods.contains(request.getMethod()))
			return AccessFilterResult.ACCESS_ALLOWED;
		
		// Only named params expected
		if (! request.getParamsType().equals(JSONRPC2ParamsType.OBJECT))
			return new AccessFilterResult(AccessDeniedError.API_KEY_REQUIRES_NAMED_PARAM);

		
		NamedParamsRetriever params = new NamedParamsRetriever((Map<String,Object>)request.getParams());
		
		// API key param present?
		APIKey key = null;
		
		try {
			key = new APIKey(params.getString(apiKeyParamName));
			
		} catch (Exception e) {
		
			return new AccessFilterResult(AccessDeniedError.MISSING_API_KEY);
		}
		
		// Get allowed methods for API key
		Set<String> allowedMethods = keyMap.get(key);
		
		if (allowedMethods == null)
			return new AccessFilterResult(AccessDeniedError.API_KEY_DENIED);
		
		// Any method allowed?
		if (allowedMethods.contains("*"))
			return AccessFilterResult.ACCESS_ALLOWED;
		
		if (! allowedMethods.contains(request.getMethod()))
			return new AccessFilterResult(AccessDeniedError.API_KEY_DENIED);
		
		return AccessFilterResult.ACCESS_ALLOWED;
	}
}
