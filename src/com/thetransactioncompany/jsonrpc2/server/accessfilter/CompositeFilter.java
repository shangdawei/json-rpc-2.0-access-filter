package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import java.net.UnknownHostException;

import java.util.LinkedList;
import java.util.List;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Composite access filter with chained HTTPS filter, client IP address filter, 
 * X.509 client certificate filter and API key filter.
 *
 * <p>The contained filters are invoked in the following order:
 *
 * <ol>
 *     <li>{@link HostFilter}
 *     <li>{@link HTTPSFilter}
 *     <li>{@link X509ClientCertFilter}
 *     <li>{@link APIKeyFilter}
 * </ol>
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-27)
 */
public class CompositeFilter implements AccessFilter {

	
	/**
	 * The filter configuration.
	 */
	private CompositeFilterConfiguration config;
	
	 
	/**
	 * The filter chain.
	 */
	private List<AccessFilter> filterChain = null;
	

	/**
	 * Initialises this composite filter with the specified configuration.
	 *
	 * @param config The composite filter configuration. Must not be 
	 *               {@code null}.
	 *
	 * @throws UnknownHostException If the allow list contains a badly 
	 *                              formatted IP address or if a host name
	 *                              could not be resolved to an IP address.
	 */
	public void init(final CompositeFilterConfiguration config)
		throws UnknownHostException {
	
		this.config = config;
	
		filterChain = new LinkedList<AccessFilter>();
		
		HostFilter hostFilter = new HostFilter();
		hostFilter.init(config.hosts.allow);
		filterChain.add(hostFilter);
		
		// Add HTTPS / client cert filter?
		if (config.https.require) {
			
			HTTPSFilter httpsFilter = new HTTPSFilter();
			httpsFilter.init(config.https.require);
			filterChain.add(httpsFilter);
			
			if (config.https.requireClientCert) {
		
				X509ClientCertFilter certFilter = new X509ClientCertFilter();
				certFilter.init(config.https.requireClientCert, config.https.clientCertPrincipal);
				filterChain.add(certFilter);
			}
		}
		
		if (config.apiKeys.require) {
			APIKeyFilter apiKeyFilter = new APIKeyFilter();
			apiKeyFilter.init(config.apiKeys.map, config.apiKeys.exemptedMethods);
			filterChain.add(apiKeyFilter);
		}
	}
	
	
	/**
	 * Gets the configuration of this composite filter.
	 *
	 * @return The configuration.
	 */
	public CompositeFilterConfiguration getConfiguration() {
	
		return config;
	}
	
	
	/**
	 * @inheritDoc
	 */
	public AccessFilterResult filter(final JSONRPC2Request request, 
	                                 final MessageContext messageCtx) {
		
		if (filterChain == null)
			throw new IllegalStateException("The composite filter must be initialized before it can be used");
		
		for (AccessFilter filter: filterChain) {
		
			AccessFilterResult result = filter.filter(request, messageCtx);
			
			if (result.accessDenied())
				return result;
		}
		
		// All filter checks passed
		return AccessFilterResult.ACCESS_ALLOWED;
	}
}
