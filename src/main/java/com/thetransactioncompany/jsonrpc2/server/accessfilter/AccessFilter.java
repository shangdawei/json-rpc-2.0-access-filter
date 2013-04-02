package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Access filter interface.
 *
 * @author Vladimir Dzhuvinov
 */
public interface AccessFilter {


	/**
	 * Filters a JSON-RPC 2.0 request according to its message context
	 * and the preconfigured access filter policy.
	 *
	 * @param request    The JSON-RPC 2.0 request to filter. Must not be
	 *                   {@code null}.
	 * @param messageCtx The context of the JSON-RPC 2.0 request message.
	 *                   Must not be {@code null}.
	 */
	public AccessFilterResult filter(final JSONRPC2Request request, 
	                                 final MessageContext messageCtx);
}
