package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.unboundid.ldap.sdk.DN;
import com.unboundid.ldap.sdk.LDAPException;

import com.thetransactioncompany.util.PropertyRetriever;
import com.thetransactioncompany.util.PropertyParseException;


/**
 * Immutable configuration of a {@link CompositeFilter composite access filter}.
 *
 * <p>Example configuration properties, prefixed by "access." and grouped into
 * domains:
 *
 * <pre>
 * access.https.require=true
 * access.https.requireClientCert=true
 * access.https.clientCertPrincipal=cn=John Doe,ou=people,cd=company,dc=org
 * 
 * access.hosts.allow=*
 * 
 * access.apiKeys.require=true
 * access.apiKeys.parameterName=apiKey
 * access.apiKeys.exemptedMethods=ws.getName ws.getVersion ws.getTime
 * access.apiKeys.map.f70defbe-b881-41f8-8138-bea52b6e1b9c=sso.login sso.logout sso.getSession
 * access.apiKeys.map.08d1e641-b1c1-4d88-8796-e47c06430efb=sso.proxiedLogin sso.proxiedLogout sso.getSession
 * access.apiKeys.map.d881afe0-4d7d-4520-9fda-bffffc3022ba=sso.userCount sso.sessionCount sso.listUsers
 * </pre>
 *
 * @author Vladimir Dzhuvinov
 */
public class CompositeFilterConfiguration {


	/**
	 * The default properties prefix.
	 */
	public static final String DEFAULT_PREFIX = "access.";
	
	
	/**
	 * HTTPS access configuration.
	 *
	 * <p>Property keys: access.https.*
	 */
	public static class HTTPS {
	
	
		/**
		 * If {@code true} clients are required to connect securely via
		 * HTTPS. Requests received via plain HTTP must be refused.
		 *
		 * <p>Property key: access.https.require
		 */
		public final boolean require;


		/**
		 * The default HTTPS requirement policy.
		 */
		public static final boolean DEFAULT_REQUIRE = true;


		/**
		 * If {@code true} clients are required to present a valid and 
		 * trusted X.509 certificate with each HTTPS request. Applies if 
		 * {@link #require require HTTPS} is set.
		 *
		 * <p>Property key: access.https.requireClientCert
		 */
		public final boolean requireClientCert;


		/**
		 * The default client certificate requirement policy.
		 */
		public static final boolean DEFAULT_REQUIRE_CLIENT_CERT = false;


		/**
		 * If not {@code null} requires the X.509 client certificate 
		 * subject to match the specified distinguished name (DN).
		 *
		 * <p>Property key: access.https.clientCertPrincipal
		 */
		public final DN clientCertPrincipal;
		
		
		/**
        	 * The logger.
        	 */
        	private final Logger log = Logger.getLogger(HTTPS.class);
		
		
		/**
		 * Creates a new HTTPS access configuration from the specified
		 * properties.
		 *
		 * @param prefix The properties prefix. Must not be 
		 *               {@code null}.
		 * @param props  The properties. Must not be {@code null}.
		 *
		 * @throws PropertyParseException On a missing or invalid 
		 *                                property.
		 */
		public HTTPS (final String prefix, final Properties props)
			throws PropertyParseException {
			
			PropertyRetriever pr = new PropertyRetriever(props);
			
			require = pr.getOptBoolean(prefix + "https.require", 
			                           DEFAULT_REQUIRE);
			
			requireClientCert = pr.getOptBoolean(prefix + "https.requireClientCert", 
			                                     DEFAULT_REQUIRE_CLIENT_CERT);
			
			String dnString = pr.getOptString(prefix + "https.clientCertPrincipal", 
			                                  null);
			
			if (dnString == null || dnString.trim().isEmpty()) {
			
				clientCertPrincipal = null;
			}
			else {
				try {
					clientCertPrincipal = new DN(dnString);
					
				} catch (LDAPException e) {
				
					throw new PropertyParseException("Invalid DN", 
					                                 prefix + "https.clientCertPrincipal", 
									 dnString);
				}
			}
		}
		
		
		/**
		 * Logs the configuration details at INFO level.
		 */
		private void log() {
			
			if (! log.isInfoEnabled())
				return;
			
			log.info("HTTPS access required: " + require);
			log.info("Client X.509 certificate required: " + requireClientCert);
			
			if (clientCertPrincipal != null)
				log.info("Client X.509 certificate principal: " + clientCertPrincipal);
			else
				log.info("Client X.509 certificate principal: Any");
		}
	}
	
	
	/**
	 * Host name / IP address access configuration.
	 *
	 * <p>Property keys: access.hosts.*
	 */
	public static class Hosts {
	
		
		/**
		 * String of space separated client IP addresses and / or 
		 * hostnames that are allowed access. If set to "*" (asterisk) 
		 * any IP address is allowed.
		 *
		 * <p>Property key: access.hosts.allow
		 */
		public final String allow;


		/**
		 * The default host allow policy.
		 */
		public static final String DEFAULT_ALLOW = "*";
		
		
		/**
        	 * The logger.
        	 */
        	private final Logger log = Logger.getLogger(Hosts.class);
		
		
		/**
		 * Creates a new client hosts access configuration from the 
		 * specified properties.
		 *
		 * @param prefix The properties prefix. Must not be 
		 *               {@code null}.
		 * @param props  The properties. Must not be {@code null}.
		 *
		 * @throws PropertyParseException On a missing or invalid 
		 *                                property.
		 */
		public Hosts (final String prefix, final Properties props)
			throws PropertyParseException {
			
			PropertyRetriever pr = new PropertyRetriever(props);
			
			allow = pr.getOptString(prefix + "hosts.allow", DEFAULT_ALLOW);
		}
		
		
		/**
		 * Logs the configuration details at INFO level.
		 */
		private void log() {
			
			log.info("Allowed hosts: " + allow);
		}
	}

	
	/**
	 * API key access configuration.
	 *
	 * <p>Property keys: access.apiKeys.*
	 */
	public static class APIKeys {
	
		
		/**
		 * If {@code true} clients must present an API key with each
		 * JSON-RPC 2.0 request.
		 *
		 * <p>Property key: access.apiKeys.require
		 */
		public final boolean require;


		/**
		 * The default API key requirement policy.
		 */
		public static final boolean DEFAULT_REQUIRE = true;
		
		
		/**
		 * The name of the JSON-RPC 2.0 parameter used to pass the API
		 * key.
		 *
		 * <p>Property key: access.apiKeys.parameterName
		 */
		public final String parameterName;
		
		
		/**
		 * Exempted JSON-RPC 2.0 methods for which an API key is not
		 * required.
		 *
		 * <p>Property key: access.apiKeys.exemptedMethods
		 */
		public final Set<String> exemptedMethods;


		/**
		 * Map of API keys to the JSON-RPC 2.0 methods they are allowed
		 * access to.
		 */
		public final Map<APIKey,Set<String>> map;
		
		
		/**
        	 * The logger.
        	 */
        	private final Logger log = Logger.getLogger(APIKeys.class);
		
		
		/**
		 * Parses the exempted methods.
		 *
		 * @param prefix The properties prefix. Must not be 
		 *               {@code null}.
		 * @param props  The properties. Must not be {@code null}.
		 *
		 * @return The exempted methods, empty set if none.
		 *
		 * @throws PropertyParseException If parsing failed.
		 */
		private static Set<String> parseExemptedMethods(final String prefix, final Properties props)
			throws PropertyParseException {
		
			PropertyRetriever pr = new PropertyRetriever(props);
		
			String s = pr.getOptString(prefix + "apiKeys.exemptedMethods", null);
				
			if (s == null || s.trim().isEmpty())
				return Collections.unmodifiableSet(new HashSet<String>());
			else
				return new HashSet<String>(Arrays.asList(s.split("\\s+")));
		}
		
		
		/**
		 * Parses the API key map.
		 *
		 * @param prefix The properties prefix. Must not be 
		 *               {@code null}.
		 * @param props  The properties. Must not be {@code null}.
		 *
		 * @return The API keys map, empty if no keys are specified.
		 *
		 * @throws PropertyParseException If parsing failed.
		 */
		private static Map<APIKey,Set<String>> parseAPIKeysMap(final String prefix, final Properties props)
			throws PropertyParseException {
		
			PropertyRetriever pr = new PropertyRetriever(props);
			
			Map<APIKey,Set<String>> map = new HashMap<APIKey,Set<String>>();
			
			Iterator<String> it = props.stringPropertyNames().iterator();
			
			while(it.hasNext()) {
			
				String propKey = it.next();
				
				if (! propKey.startsWith(prefix + "apiKeys.map."))
					continue;
				
				// Extract API key from last dotted domain of
				// the property key
				String keyString = propKey.substring((prefix + "apiKeys.map.").length());
				
				if (keyString.isEmpty())
					continue;
				
				APIKey apiKey = new APIKey(keyString);
				
				// Get the allowed methods for this API key
				
				Set<String> methods = new HashSet<String>();
				
				for (String method: pr.getString(propKey).split("\\s+"))
					methods.add(method);
				
				map.put(apiKey, methods);
			}
			
			return map;
		}
		
		
		/**
		 * Creates a new client hosts access configuration from the 
		 * specified properties.
		 *
		 * @param prefix The properties prefix. Must not be 
		 *               {@code null}.
		 * @param props  The properties. Must not be {@code null}.
		 *
		 * @throws PropertyParseException On a missing or invalid 
		 *                                property.
		 */
		public APIKeys (final String prefix, final Properties props)
			throws PropertyParseException {
			
			PropertyRetriever pr = new PropertyRetriever(props);
			
			require = pr.getOptBoolean(prefix + "apiKeys.require", DEFAULT_REQUIRE);
			
			parameterName = pr.getOptString(prefix + "apiKeys.parameterName", 
			                                APIKeyFilter.DEFAULT_API_KEY_PARAMETER_NAME);
			
			if (require) {
			
				exemptedMethods = parseExemptedMethods(prefix, props);
				map = parseAPIKeysMap(prefix, props);
			}
			else {
				exemptedMethods = null;
				map = null;
			}
		}
		
		
		/**
		 * Logs the configuration details at INFO level.
		 */
		private void log() {
			
			log.info("API key access required: " + require);
			
			if (! require)
				return;
			
			log.info("API key parameter name: " + parameterName);
			
			StringBuilder sb = new StringBuilder();
			sb.append("API key exempted JSON-RPC 2.0 methods: ");
			
			for (String method: exemptedMethods) {
				sb.append(method);
				sb.append(' ');
			}
			
			log.info(sb.toString());
		}
	}
	
	
	/**
	 * The HTTPS configuration.
	 */
	public final HTTPS https;
	
	
	/**
	 * The hosts access configuration.
	 */
	public final Hosts hosts;
	
	
	/**
	 * The API key access configuration.
	 */
	public final APIKeys apiKeys;
	
	
	/**
	 * Creates a new composite filter configuration from the specified 
	 * properties. The expected prefix is {@link #DEFAULT_PREFIX}.
	 *
	 * @param props The properties. Must not be {@code null}.
	 *
	 * @throws PropertyParseException On a missing or invalid property.
	 */
	public CompositeFilterConfiguration (final Properties props)
		throws PropertyParseException {
	
		this(DEFAULT_PREFIX, props);
	}
	
	
	/**
	 * Creates a new composite filter configuration from the specified 
	 * properties.
	 *
	 * @param prefix The properties prefix. Must not be {@code null}.
	 * @param props  The properties. Must not be {@code null}.
	 *
	 * @throws PropertyParseException On a missing or invalid property.
	 */
	public CompositeFilterConfiguration (final String prefix, final Properties props)
		throws PropertyParseException {
	
		https = new HTTPS(prefix, props);
		hosts = new Hosts(prefix, props);
		apiKeys = new APIKeys(prefix, props);
	}
	
	
	/**
	 * Logs the configuration details at INFO level.
	 */
	public void log() {
		
		https.log();
		hosts.log();
		apiKeys.log();
	}
}
