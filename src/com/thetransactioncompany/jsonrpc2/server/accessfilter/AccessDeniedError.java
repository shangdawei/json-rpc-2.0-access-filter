package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;


/**
 * Enumeration of the access denied JSON-RPC 2.0 errors produced by this 
 * package.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-24)
 */
public enum AccessDeniedError {


	/**
	 * [-1000] Requests must be sent over HTTPS.
	 */
	HTTPS_REQUIRED (-1000, "Requests must be sent over a secure channel (HTTPS)"),
	
	
	/**
	 * [-1001] Client IP denied.
	 */
	CLIENT_IP_DENIED (-1001, "Client IP address denied access"),
	
	
	/**
	 * [-1002] Client X.509 certificate required.
	 */
	CLIENT_CERT_REQUIRED (-1002, "Trusted client X.509 certificate required"),
	
	
	/**
	 * [-1003] Client X.509 certificate principal denied.
	 */
	CLIENT_PRINCIPAL_DENIED (-1003, "Client X.509 certificate principal denied"),
	
	
	/**
	 * [-1004] Invalid client X.509 certificate principal DN.
	 */
	INVALID_CLIENT_PRINCIPAL_DN (-1004, "Invalid client X.509 certificate principal DN"),
	
	
	/**
	 * [-1005] Invalid API key.
	 */
	MISSING_API_KEY (-1005, "Missing API key"),
	
	
	/**
	 * [-1006] API key denied access.
	 */
	API_KEY_DENIED (-1006, "API key denied access");
	
	
	/**
	 * The error code.
	 */
	public final int code;
	 

	/**
	 * The error message.
	 */
	public final String message;
	
	
	/**
	 * Creates a new enumerated access denied error.
	 *
	 * @param code    The error code. Must not be {@code null}.
	 * @param message The error message. Must not be {@code null}.
	 */
	private AccessDeniedError(final int code, final String message) {
	
		this.code = code;
		this.message = message;
	}
	
	
	/**
	 * Returns a JSON-RPC 2.0 error representation of this access denied
	 * error.
	 *
	 * @return The JSON-RPC 2.0 error representation.
	 */
	public JSONRPC2Error toJSONRPC2Error() {
	
		return toJSONRPC2Error(null);
	}
	
	
	/**
	 * Returns a JSON-RPC 2.0 error representation of this access denied
	 * error.
	 *
	 * @param data Additional error data, should serialise to JSON 
	 *             (typically a string) and may be {@code null}.
	 *
	 * @return The JSON-RPC 2.0 error representation.
	 */
	public JSONRPC2Error toJSONRPC2Error(final Object data) {
	
		return new JSONRPC2Error(code, message, data);	
	}
}
