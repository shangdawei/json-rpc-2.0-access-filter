package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import junit.framework.TestCase;


/**
 * Tests the access filter result class.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-30)
 */
public class AccessFilterResultTest extends TestCase {


	public void testAccessAllowed() {
	
		AccessFilterResult result = new AccessFilterResult();
		
		assertTrue(result.accessAllowed());
		assertFalse(result.accessDenied());
	}
	
	
	public void testAccessDenied() {
	
		AccessFilterResult result = 
			new AccessFilterResult(AccessDeniedError.HTTPS_REQUIRED);
		
	
		assertTrue(result.accessDenied());
		assertFalse(result.accessAllowed());
		
		assertNotNull(result.getAccessDeniedError());
		
		assertEquals(AccessDeniedError.HTTPS_REQUIRED,
		             result.getAccessDeniedError());
	}
	
	
	public void testConstant() {
	
		assertTrue(AccessFilterResult.ACCESS_ALLOWED.accessAllowed());
		assertFalse(AccessFilterResult.ACCESS_ALLOWED.accessDenied());
		assertNull(AccessFilterResult.ACCESS_ALLOWED.getAccessDeniedError());
	}
}
