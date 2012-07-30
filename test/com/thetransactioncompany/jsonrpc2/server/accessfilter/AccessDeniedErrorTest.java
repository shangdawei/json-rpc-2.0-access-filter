package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import junit.framework.TestCase;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;


/**
 * Tests the access denied error enumeration.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-30)
 */
public class AccessDeniedErrorTest extends TestCase {


	public void testEnums() {
	
		assertNotNull(AccessDeniedError.HTTPS_REQUIRED);
		assertNotNull(AccessDeniedError.CLIENT_IP_DENIED);
		assertNotNull(AccessDeniedError.CLIENT_CERT_REQUIRED);
		assertNotNull(AccessDeniedError.INVALID_CLIENT_PRINCIPAL_DN);
		assertNotNull(AccessDeniedError.CLIENT_PRINCIPAL_DENIED);
		assertNotNull(AccessDeniedError.API_KEY_REQUIRES_NAMED_PARAM);
		assertNotNull(AccessDeniedError.MISSING_API_KEY);
		assertNotNull(AccessDeniedError.API_KEY_DENIED);
		
		assertEquals(8, AccessDeniedError.values().length);
	}
	
	
	public void testCodes() {
	
		assertEquals(-31100, AccessDeniedError.HTTPS_REQUIRED.code);
		assertEquals(-31105, AccessDeniedError.CLIENT_IP_DENIED.code);
		assertEquals(-31110, AccessDeniedError.CLIENT_CERT_REQUIRED.code);
		assertEquals(-31111, AccessDeniedError.INVALID_CLIENT_PRINCIPAL_DN.code);
		assertEquals(-31112, AccessDeniedError.CLIENT_PRINCIPAL_DENIED.code);
		assertEquals(-31120, AccessDeniedError.API_KEY_REQUIRES_NAMED_PARAM.code);
		assertEquals(-31121, AccessDeniedError.MISSING_API_KEY.code);
		assertEquals(-31123, AccessDeniedError.API_KEY_DENIED.code);
	}
	
	
	public void testToJSONRPC2Error() {
	
		JSONRPC2Error error = AccessDeniedError.HTTPS_REQUIRED.toJSONRPC2Error();
		assertNotNull(error);
		assertEquals(-31100, error.getCode());
		assertEquals("Requests must be sent over HTTPS", error.getMessage());
		assertNull(error.getData());
	}
}
