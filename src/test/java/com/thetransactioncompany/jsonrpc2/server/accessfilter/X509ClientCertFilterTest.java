package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import java.security.Principal;

import com.unboundid.ldap.sdk.DN;
import com.unboundid.ldap.sdk.LDAPException;

import junit.framework.TestCase;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Tests the X.509 client cert filter.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-30)
 */
public class X509ClientCertFilterTest extends TestCase {
	
	
	public void testRequireCertAndPrincipal()
		throws LDAPException {
	
		X509ClientCertFilter filter = new X509ClientCertFilter();
		
		boolean requireCert = true;
		DN expectedPrincipal = new DN("cn=admin,ou=people,dc=wondlerland,dc=net");
		
		filter.init(requireCert, expectedPrincipal);
		assertTrue(filter.requiresCertificate());
		assertEquals(new DN("cn=admin,ou=people,dc=wondlerland,dc=net"), filter.getCertificatePrincipal());
		
		JSONRPC2Request req = new JSONRPC2Request("users.list", 0);
		
		// Cert present, correct principal
		MessageContext ctx = new MessageContext(null, "192.168.0.1", true, "cn=admin,ou=people,dc=wondlerland,dc=net");
		AccessFilterResult result = filter.filter(req, ctx);
		assertTrue(result.accessAllowed());
		
		// No cert present
		ctx = new MessageContext(null, "192.168.0.1", true, (String)null);
		result = filter.filter(req, ctx);
		assertTrue(result.accessDenied());
		assertEquals(AccessDeniedError.CLIENT_CERT_REQUIRED,
		             result.getAccessDeniedError());
		
		// Cert present, incorrect principal
		ctx = new MessageContext(null, "192.168.0.1", true, "cn=bob,ou=people,dc=wondlerland,dc=net");
		result = filter.filter(req, ctx);
		assertTrue(result.accessDenied());
		assertEquals(AccessDeniedError.CLIENT_PRINCIPAL_DENIED,
		             result.getAccessDeniedError());
	}
	
	
	public void testRequireCertOnly() {
	
		X509ClientCertFilter filter = new X509ClientCertFilter();
		
		boolean requireCert = true;
		DN expectedPrincipal = null;
		
		filter.init(requireCert, expectedPrincipal);
		assertTrue(filter.requiresCertificate());
		assertNull(filter.getCertificatePrincipal());
		
		JSONRPC2Request req = new JSONRPC2Request("users.list", 0);
		
		// Cert present
		MessageContext ctx = new MessageContext(null, "192.168.0.1", true, "cn=admin,ou=people,dc=wondlerland,dc=net");
		AccessFilterResult result = filter.filter(req, ctx);
		assertTrue(result.accessAllowed());
		
		// No cert present
		ctx = new MessageContext(null, "192.168.0.1", true, (String)null);
		result = filter.filter(req, ctx);
		assertTrue(result.accessDenied());
		assertEquals(AccessDeniedError.CLIENT_CERT_REQUIRED,
		             result.getAccessDeniedError());
	}
	
	
	public void testRequireNoCert() {
	
		X509ClientCertFilter filter = new X509ClientCertFilter();
		
		boolean requireCert = false;
		DN expectedPrincipal = null;
		
		filter.init(requireCert, expectedPrincipal);
		assertFalse(filter.requiresCertificate());
		assertNull(filter.getCertificatePrincipal());
		
		JSONRPC2Request req = new JSONRPC2Request("users.list", 0);
		
		// Cert present
		MessageContext ctx = new MessageContext(null, "192.168.0.1", true, "cn=admin,ou=people,dc=wondlerland,dc=net");
		AccessFilterResult result = filter.filter(req, ctx);
		assertTrue(result.accessAllowed());
		
		// No cert present
		ctx = new MessageContext(null, "192.168.0.1", true, (String)null);
		result = filter.filter(req, ctx);
		assertTrue(result.accessAllowed());
	}
}
