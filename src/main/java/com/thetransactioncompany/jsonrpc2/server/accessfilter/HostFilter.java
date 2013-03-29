package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import java.net.InetAddress;
import java.net.UnknownHostException;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import com.thetransactioncompany.jsonrpc2.server.MessageContext;


/**
 * Access filter ensuring JSON-RPC 2.0 requests originate from selected host 
 * names / IP addresses.
 *
 * <p>Requests are filtered by taking the client IP address and matching it 
 * against a list of allowed IP addresses or host names (that resolve to an IP 
 * address). Both IPv4 as well as IPv6 addresses are supported. To allow any 
 * host set the whitelist to "*" (asterisk).
 *
 * <p>Important: To speed up checking all host names in the supplied whitelist 
 * are resolved during initialisation. This means that if the IP address for a 
 * whitelisted host name changes later, {@link #isAllowedIP} will return a false
 * negative. To prevent this from occurring you may choose to periodically 
 * reinitialise the host filter.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-07-27)
 */
public class HostFilter implements AccessFilter {


	/**
	 * The original space-separated string of allowed IPv4/IPv6 addresses 
	 * and/or host names.
	 */
	private String allow;
	
	
	/**
	 * Space-separated string of allowed IPv4 or IPv6 addresses.
	 */
	private String allowedIPs;
	
	
	/**
	 * Indicates that any host is allowed.
	 */
	private boolean allowAny;
	
	
	/**
	 * Initialises this host filter.
	 *
	 * @param whitelist Space-separated string of allowed host names and/or 
	 *                  IPv4 / IPv6 addresses. All host names will be 
	 *                  resolved to their corresponding IP addresses. If set
	 *                  to "*" (asterisk) any IP address will be allowed.
	 *                  Must not be {@code null}.
	 *
	 * @throws UnknownHostException If the allow list contains a badly 
	 *                              formatted IP address or if a host name
	 *                              could not be resolved to an IP address.
	 */
	public void init(final String whitelist)
		throws UnknownHostException {
	
		allow = whitelist;
		
		if (whitelist.trim().equals("*")) {
			
			allowAny = true;
			
			allowedIPs = "*";
		}
		else {
			allowAny = false;
			
			String[] tokens = whitelist.split("\\s+");
			
			allowedIPs = "";

			for (String t: tokens) {

				// Resolve all hostnames to IP addresses
				// and compose final IP allow list
				InetAddress[] hostAddresses = InetAddress.getAllByName(t);

				for (InetAddress addr: hostAddresses)
					allowedIPs += addr.getHostAddress() + " ";
			}
		}
	}
	
	
	/**
	 * Returns the originial whitelist of host names and IP addresses
	 * supplied to {@link #init}.
	 *
	 * @return The original whitelist.
	 */
	public String getOriginalAllowedHosts() {
	
		return allow;
	}
	
	
	/**
	 * Returns the whitelist of IP addresses and resolved host names (if 
	 * any) used by the {@link #isAllowedIP} method.
	 *
	 * @return The resolved whitelist (with resolved host names if any).
	 */
	public String getResolvedAllowedIPs() {
	
		return allowedIPs;
	}
	

	/**
	 * Checks if the specified IP address is allowed.
	 *
	 * @param ip A valid IPv4 or IPv6 address. Must not be {@code null}.
	 *
	 * @return {@code true} if the address is allowed, else {@code false}.
	 */
	public boolean isAllowedIP(final String ip) {
	
		if (allowAny || allowedIPs.indexOf(ip) >= 0)
			return true;
		else
			return false;
	}
	
	
	/**
	 * @inheritDoc
	 */
	public AccessFilterResult filter(final JSONRPC2Request request, 
	                                 final MessageContext messageCtx) {
					 
		final String ip = messageCtx.getClientInetAddress();
		
		if (ip == null) {
		
			if (allowAny)
				return AccessFilterResult.ACCESS_ALLOWED;
			else
				return new AccessFilterResult(AccessDeniedError.CLIENT_IP_DENIED);
		}
		
		if (isAllowedIP(ip))
			return AccessFilterResult.ACCESS_ALLOWED;
		else
			return new AccessFilterResult(AccessDeniedError.CLIENT_IP_DENIED);
	}
}
