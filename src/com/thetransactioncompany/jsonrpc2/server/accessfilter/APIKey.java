package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import java.util.UUID;


/**
 * Immutable API key.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-24)
 */
public final class APIKey {


	/**
	 * The API key value.
	 */
	private final String value;
	
	
	/**
	 * Creates a new unique API key based on a pseudo-randomly generated 
	 * (type 4) UUID.
	 *
	 * <p>See {@code java.util.UUID}.
	 */
	public APIKey() {
	
		value = UUID.randomUUID().toString();
	}
	
	
	/**
	 * Creates a new API key with the specified value.
	 *
	 * @param value The API key value. Must not be {@code null}.
	 */
	public APIKey(final String value) {
	
		if (value == null)
			throw new IllegalArgumentException("The API key value must not be null");
		
		this.value = value;
	}
	
	
	/**
	 * Overrides {@code Object.hashCode()}.
	 *
	 * @return The object hash code.
	 */
	public int hashCode() {
	
		return value.hashCode();
	}
	
	
	/**
	 * Overrides {@code Object.equals()}.
	 *
	 * @param object The object to compare to.
	 *
	 * @return {@code true} if the objects have the same value, otherwise
	 *         {@code false}.
	 */
	public boolean equals(Object object) {
	
		return object instanceof APIKey && this.toString().equals(object.toString());
	}
	
	
	/**
	 * Returns the string representation of this API key.
	 *
	 * @return The string representation.
	 */
	public String toString() {
	
		return value;
	}
}
