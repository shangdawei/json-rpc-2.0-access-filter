package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Access filter based on a secure HTTP (HTTPS) message transport presence.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-23)
 */
public class HTTPSFilter implements AccessFilter {


	/**
	 * @inheritDoc
	 */
	public AccessFilterResult filter(final JSONRPC2Request request, 
	                                 final MessageContext messageCtx) {
					 
		if (messageCtx.isSecure())
			return new AccessFilterResult(true);
		else
			return new AccessFilterResult(false);
	}
}
