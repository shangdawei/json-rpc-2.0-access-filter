package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import java.util.LinkedList;
import java.util.List;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Composite access filter with chained HTTPS filter, client IP address filter, 
 * X.509 client certificate filter and API key filter.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-25)
 */
public class CompositeFilter implements AccessFilter {

	
	/**
	 * The filter chain.
	 */
	private List<AccessFilter> filterChain = null;
	

	/**
	 * Initialises this composite filter with the specified configuration.
	 *
	 * @param config The composite filter configuration. Must not be 
	 *               {@code null}.
	 */
	public void init(final CompositeFilterConfiguration config)
		throws java.net.UnknownHostException {
	
		filterChain = new LinkedList<AccessFilter>();
		
		HTTPSFilter httpsFilter = new HTTPSFilter();
		httpsFilter.init(config.https.require);
		filterChain.add(httpsFilter);
		
		HostFilter hostFilter = new HostFilter();
		hostFilter.init(config.hosts.allow);
		filterChain.add(hostFilter);
		
		X509ClientCertFilter certFilter = new X509ClientCertFilter();
		certFilter.init(config.https.requireClientCert, config.https.clientCertPrincipal);
		filterChain.add(certFilter);
		
		if (config.apiKeys.require) {
			APIKeyFilter apiKeyFilter = new APIKeyFilter();
			apiKeyFilter.init(config.apiKeys.map, config.apiKeys.exemptedMethods);
			filterChain.add(apiKeyFilter);
		}
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
