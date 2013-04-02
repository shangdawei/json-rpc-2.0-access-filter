package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Access filter ensuring requests are passed over HTTPS. The transport security
 * of the JSON-RPC 2.0 request is checked by means of the 
 * {@code MessageContext.isSecure()} method.
 *
 * @author Vladimir Dzhuvinov
 */
public class HTTPSFilter implements AccessFilter {


	/**
	 * Indicates whether HTTPS is required, default on.
	 */
	private boolean requireHTTPS = true;
	
	
	/**
	 * Initialises this HTTPS filter.
	 *
	 * @param requireHTTPS If {@code true} HTTPS access is required, else
	 *                     {@code false}.
	 */
	public void init(final boolean requireHTTPS) {
	
		this.requireHTTPS = requireHTTPS;
	}
	
	
	/**
	 * Returns {@code true} if the filter is configured to require HTTPS
	 * transport of the request.
	 *
	 * @return {@code true} if HTTPS is required, else {@code false}.
	 */
	public boolean requiresHTTPS() {
	
		return requireHTTPS;
	}
	

	/**
	 * @inheritDoc
	 */
	public AccessFilterResult filter(final JSONRPC2Request request, 
	                                 final MessageContext messageCtx) {

		if (! requireHTTPS)
			return AccessFilterResult.ACCESS_ALLOWED;
		
		if (messageCtx.isSecure())
			return AccessFilterResult.ACCESS_ALLOWED;
		else
			return new AccessFilterResult(AccessDeniedError.HTTPS_REQUIRED);
	}
}
