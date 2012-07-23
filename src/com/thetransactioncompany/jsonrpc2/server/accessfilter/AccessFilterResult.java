package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;


/**
 * Access filter result.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-23)
 */
public class AccessFilterResult {

	
	/**
	 * Indicates whether access is allowed or denied.
	 */
	private boolean accessAllowed;
	
	
	/**
	 * The matching JSON-RPC 2.0 error if access is denied.
	 */
	private JSONRPC2Error error;
	
	
	/**
	 * Constant access filter result indicating access is allowed.
	 */
	public static final AccessFilterResult ACCESS_ALLOWED = new AccessFilterResult();
	
	
	/**
	 * Creates a new access filter result indicating access is allowed.
	 */
	public AccessFilterResult() {
	
		accessAllowed = true;
		error = null;
	}
	
	
	/**
	 * Creates a new access filter result indicating access is denied.
	 *
	 * @param error The matching JSON-RPC 2.0 error. Must not be 
	 *              {@code null}.
	 */
	public AccessFilterResult(final JSONRPC2Error error) {
	
		accessAllowed = false;
		
		if (error == null)
			throw new IllegalArgumentException("The JSON-RPC 2.0 error must not be null");
		
		this.error = error;
	}
	
	
	/**
	 * Returns {@code true} if access has been allowed.
	 *
	 * @see #accessDenied
	 *
	 * @return {@code true} if access has been allowed, else {@code false}.
	 */
	public boolean accessAllowed() {
	
		return accessAllowed;
	}
	
	
	/**
	 * Returns {@code true} if access has been denied.
	 *
	 * @see #accessAllowed
	 *
	 * @return {@code true} if access has been denied, else {@code false}.
	 */
	public boolean accessDenied() {
	
		return ! accessAllowed;
	}
	
	
	/**
	 * Gets the matching JSON-RPC 2.0 error if access is denied.
	 *
	 * @return The matching JSON-RPC 2.0 error if access is denied, else
	 *         {@code null}.
	 */
	public JSONRPC2Error getJSONRPC2Error() {
	
		return error;
	}
}
