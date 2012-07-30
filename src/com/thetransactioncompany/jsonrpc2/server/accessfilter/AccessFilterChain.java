package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import java.util.LinkedList;
import java.util.List;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Access filter chain. Executes the filters in the order of they were added. 
 * All chained filters must allow access for the filtered JSON-RPC 2.0 request
 * to pass. If a single filter denies access the chain immediately returns an 
 * access denied.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-30)
 */
public class AccessFilterChain implements AccessFilter {


	/**
	 * The filter chain.
	 */
	private List<AccessFilter> filterChain = new LinkedList<AccessFilter>();
	
	
	/**
	 * Adds the specified access filter to this chain.
	 *
	 * @param filter The access filter to add. Must not be {@code null}.
	 */
	public void add(final AccessFilter filter) {
	
		if (filter == null)
			throw new IllegalArgumentException("The access filter must not be null");
		
		filterChain.add(filter);
	}
	
	
	/**
	 * Gets the list of the chained access filters, in the order they were 
	 * added.
	 *
	 * @return The chained access filters, empty list if none.
	 */
	public List<AccessFilter> getAccessFilters() {
	
		return filterChain;
	}
	
	
	/**
	 * Removes all access filters from the chain.
	 */
	public void clear() {
	
		filterChain = new LinkedList<AccessFilter>();
	}
	
	
	/**
	 * @inheritDoc
	 */
	public AccessFilterResult filter(final JSONRPC2Request request, 
	                                 final MessageContext messageCtx) {
					 
		for (AccessFilter filter: filterChain) {
		
			AccessFilterResult result = filter.filter(request, messageCtx);
			
			if (result.accessDenied())
				return result;
		}
		
		// All filter checks passed
		return AccessFilterResult.ACCESS_ALLOWED;
	}
}
