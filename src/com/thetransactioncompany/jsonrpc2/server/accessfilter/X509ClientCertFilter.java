package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import java.security.Principal;

import com.unboundid.ldap.sdk.DN;
import com.unboundid.ldap.sdk.LDAPException;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Access filter based on a X.509 client certificate presence and principal.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-23)
 */
public class X509ClientCertFilter implements AccessFilter {


	/**
	 * Indicates whether a X.509 client certificate must be presented for an
	 * incoming request.
	 */
	private boolean requireCert;
	
	
	/**
	 * If defined requires the X.509 client certificate subject to match the
	 * specified distinguished name (DN).
	 */
	private DN certPrincipal;
	
	
	/**
	 * Initialises this X.509 client certificate filter.
	 *
	 * @param requireCert   If {@code true} a X.509 certificate must be 
	 *                      presented for an incoming request.
	 * @param certPrincipal If defined (and other argument is {@code true})
	 *                      requires the X.509 client certificate subject to
	 *                      matchthe specified distinguished name (DN).
	 */
	public void init(final boolean requireCert, final DN certPrincipal) {
	
		this.requireCert = requireCert;
		this.certPrincipal = certPrincipal;
	}
	
	 
	/**
	 * @inheritDoc
	 */
	public AccessFilterResult filter(final JSONRPC2Request request, 
	                                 final MessageContext messageCtx) {
		
		// Certs required at all?
		if (! requireCert)
			return AccessFilterResult.ACCESS_ALLOWED;
		
		// Cert required, but no particular principal DN?
		if (requireCert && 
		    certPrincipal == null && 
		    messageCtx.getPrincipal() != null)
			return AccessFilterResult.ACCESS_ALLOWED;
		
		// Cert with specified principal required?
		if (requireCert && certPrincipal != null) {
		
			for (final Principal clientPrincipal: messageCtx.getPrincipals()) {
			
				if (clientPrincipal == null)
					continue;
					
				String name = clientPrincipal.getName();
				
				if (name == null)
					continue;
					
				DN dn = null;
				
				try {
					dn = new DN(name);
				
				} catch (LDAPException e) {
			
					return new AccessFilterResult(AccessDeniedError.INVALID_CLIENT_PRINCIPAL_DN.toJSONRPC2Error());
				}
				
				if (dn.equals(certPrincipal))
					return AccessFilterResult.ACCESS_ALLOWED;
			}
		}
		
		return new AccessFilterResult(AccessDeniedError.CLIENT_PRINCIPAL_DENIED.toJSONRPC2Error());
	}
}
