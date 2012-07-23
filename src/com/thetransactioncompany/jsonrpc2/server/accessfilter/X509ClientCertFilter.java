package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Access filter based on a X.509 client certificate presence and principal.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-23)
 */
public class X509ClientCertFilter implements AccessFilter {


	/**
	 * @inheritDoc
	 */
	public AccessFilterResult filter(final JSONRPC2Request request, 
	                                 final MessageContext messageCtx) {
					 
		return null;
	}
}
