package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Access filter based on a secure HTTP (HTTPS) message transport presence.
 * If HTTPS access is required, the {@code MessageContext.isSecure()} method is
 * checked.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-23)
 */
public class HTTPSFilter implements AccessFilter {


	/**
	 * Indicates whether HTTPS is required.
	 */
	private boolean httpsRequired;
	
	
	/**
	 * Initialises the HTTPS filter.
	 *
	 * @param httpsRequired If {@code true} HTTPS access is required, else
	 *                      {@code false}.
	 */
	public void init(final boolean httpsRequired) {
	
		this.httpsRequired = httpsRequired;
	}
	

	/**
	 * @inheritDoc
	 */
	public AccessFilterResult filter(final JSONRPC2Request request, 
	                                 final MessageContext messageCtx) {

		if (! httpsRequired)
			return AccessFilterResult.ACCESS_ALLOWED;
		
		if (messageCtx.isSecure())
			return AccessFilterResult.ACCESS_ALLOWED;
		else
			return new AccessFilterResult(AccessDeniedError.HTTPS_REQUIRED.toJSONRPC2Error());
	}
}
