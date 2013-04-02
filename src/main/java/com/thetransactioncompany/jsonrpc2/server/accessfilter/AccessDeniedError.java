package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;


/**
 * Enumeration of the access denied errors produced by the access filters in 
 * this package and intended for return to the calling clients. Use the
 * {@link #toJSONRPC2Error} method to convert to a JSON-RPC 2.0 error.
 *
 * <p>The error codes are in the range [-31100 .. -31199].
 *
 * @author Vladimir Dzhuvinov
 */
public enum AccessDeniedError {


	/**
	 * [-31100] Requests must be sent over HTTPS.
	 */
	HTTPS_REQUIRED (-31100, "Requests must be sent over HTTPS"),
	
	
	/**
	 * [-31105] Client IP denied.
	 */
	CLIENT_IP_DENIED (-31105, "Client IP address denied access"),
	
	
	/**
	 * [-31110] Client X.509 certificate required.
	 */
	CLIENT_CERT_REQUIRED (-31110, "Trusted client X.509 certificate required"),
	
	
	/**
	 * [-31111] Invalid client X.509 certificate principal DN.
	 */
	INVALID_CLIENT_PRINCIPAL_DN (-31111, "Invalid client X.509 certificate principal DN"),
	
	
	/**
	 * [-31112] Client X.509 certificate principal denied.
	 */
	CLIENT_PRINCIPAL_DENIED (-31112, "Client X.509 certificate principal denied"),
	
	
	/**
	 * [-31120] API key filter requires named JSON-RPC 2.0 parameters.
	 */
	API_KEY_REQUIRES_NAMED_PARAM (-31120, "API key filter requires named JSON-RPC 2.0 parameters"),
	
	
	/**
	 * [-31121] Missing API key.
	 */
	MISSING_API_KEY (-31121, "Missing API key"),
	
	
	/**
	 * [-31123] API key denied access.
	 */
	API_KEY_DENIED (-31123, "API key denied access");
	
	
	/**
	 * The error code.
	 */
	public final int code;
	 

	/**
	 * The error message.
	 */
	public final String message;
	
	
	/**
	 * Creates a new access denied error.
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
