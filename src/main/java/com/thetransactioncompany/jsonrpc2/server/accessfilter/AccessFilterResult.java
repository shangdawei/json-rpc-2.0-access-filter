package com.thetransactioncompany.jsonrpc2.server.accessfilter;


/**
 * Access filter result.
 *
 * @author Vladimir Dzhuvinov
 */
public class AccessFilterResult {

	
	/**
	 * Indicates whether access is allowed or denied.
	 */
	private boolean accessAllowed;
	
	
	/**
	 * The matching access denied error message if access is denied.
	 */
	private AccessDeniedError error;
	
	
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
	 * @param error The matching access denied error message. Must not be 
	 *              {@code null}.
	 */
	public AccessFilterResult(final AccessDeniedError error) {
	
		accessAllowed = false;
		
		if (error == null)
			throw new IllegalArgumentException("The access denied error must not be null");
		
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
	 * Gets the matching access denied error if access is denied.
	 *
	 * @return The matching access denied error if access is denied, else
	 *         {@code null}.
	 */
	public AccessDeniedError getAccessDeniedError() {
	
		return error;
	}
}
