package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Access filter based on a static API key whitelist.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-23)
 */
public class APIKeyFilter implements AccessFilter {


	/**
	 * @inheritDoc
	 */
	public AccessFilterResult filter(final JSONRPC2Request request, 
	                                 final MessageContext messageCtx) {
					 
		return null;
	}
}
