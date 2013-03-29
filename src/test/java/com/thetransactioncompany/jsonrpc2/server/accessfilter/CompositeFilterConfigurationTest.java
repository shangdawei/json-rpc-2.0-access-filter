package com.thetransactioncompany.jsonrpc2.server.accessfilter;


import java.util.*;

import junit.framework.TestCase;

import com.unboundid.ldap.sdk.DN;


/**
 * Tests the composite filter configuration class.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-08-07)
 */
public class CompositeFilterConfigurationTest extends TestCase {


	public static Properties getAllConfigPropertiesSet() {
	
		Properties props = new Properties();
		props.setProperty("access.https.require", "true");
		props.setProperty("access.https.requireClientCert", "true");
		props.setProperty("access.https.clientCertPrincipal", "cn=John Doe,ou=people,cd=company,dc=org");

		props.setProperty("access.hosts.allow", "*");

		props.setProperty("access.apiKeys.require", "true");
		props.setProperty("access.apiKeys.parameterName", "api_key");
		props.setProperty("access.apiKeys.exemptedMethods", "ws.getName ws.getVersion ws.getTime");
		props.setProperty("access.apiKeys.map.f70defbe-b881-41f8-8138-bea52b6e1b9c", "sso.login sso.logout sso.getSession");
		props.setProperty("access.apiKeys.map.08d1e641-b1c1-4d88-8796-e47c06430efb", "sso.proxiedLogin sso.proxiedLogout sso.getSession");
		props.setProperty("access.apiKeys.map.d881afe0-4d7d-4520-9fda-bffffc3022ba", "sso.userCount sso.sessionCount sso.listUsers");
	
		return props;
	}
	
	
	public static Properties getMinimalConfigProperties() {
	
		return new Properties();
	}


	public void testParseAll()
		throws Exception {

		Properties props = getAllConfigPropertiesSet();

		CompositeFilterConfiguration config = new CompositeFilterConfiguration(props);
		
		assertTrue(config.https.require);
		assertTrue(config.https.requireClientCert);
		assertEquals(new DN("cn=John Doe,ou=people,cd=company,dc=org"), config.https.clientCertPrincipal);
		
		assertEquals("*", config.hosts.allow);
		
		assertTrue(config.apiKeys.require);
		
		assertEquals("api_key", config.apiKeys.parameterName);
		
		assertNotNull(config.apiKeys.exemptedMethods);
		assertTrue(config.apiKeys.exemptedMethods.contains("ws.getName"));
		assertTrue(config.apiKeys.exemptedMethods.contains("ws.getVersion"));
		assertTrue(config.apiKeys.exemptedMethods.contains("ws.getTime"));
		
		assertEquals(3, config.apiKeys.map.size());
		assertEquals(3, config.apiKeys.map.get(new APIKey("f70defbe-b881-41f8-8138-bea52b6e1b9c")).size());
		
		assertTrue(config.apiKeys.map.get(new APIKey("f70defbe-b881-41f8-8138-bea52b6e1b9c")).contains("sso.login"));
		assertTrue(config.apiKeys.map.get(new APIKey("f70defbe-b881-41f8-8138-bea52b6e1b9c")).contains("sso.logout"));
		assertTrue(config.apiKeys.map.get(new APIKey("f70defbe-b881-41f8-8138-bea52b6e1b9c")).contains("sso.getSession"));
		
		assertTrue(config.apiKeys.map.get(new APIKey("08d1e641-b1c1-4d88-8796-e47c06430efb")).contains("sso.proxiedLogin"));
		assertTrue(config.apiKeys.map.get(new APIKey("08d1e641-b1c1-4d88-8796-e47c06430efb")).contains("sso.proxiedLogout"));
		assertTrue(config.apiKeys.map.get(new APIKey("08d1e641-b1c1-4d88-8796-e47c06430efb")).contains("sso.getSession"));
		
		assertTrue(config.apiKeys.map.get(new APIKey("d881afe0-4d7d-4520-9fda-bffffc3022ba")).contains("sso.userCount"));
		assertTrue(config.apiKeys.map.get(new APIKey("d881afe0-4d7d-4520-9fda-bffffc3022ba")).contains("sso.sessionCount"));
		assertTrue(config.apiKeys.map.get(new APIKey("d881afe0-4d7d-4520-9fda-bffffc3022ba")).contains("sso.listUsers"));
	}
	
	
	public void testParseDefaults()
		throws Exception {

		Properties props = getMinimalConfigProperties();

		CompositeFilterConfiguration config = new CompositeFilterConfiguration(props);
		
		assertTrue(config.https.require);
		assertFalse(config.https.requireClientCert);
		assertNull(config.https.clientCertPrincipal);
		
		assertEquals("*", config.hosts.allow);
		
		assertTrue(config.apiKeys.require);
		
		assertEquals("apiKey", config.apiKeys.parameterName);
		
		assertTrue(config.apiKeys.exemptedMethods.isEmpty());
		
		assertTrue(config.apiKeys.map.isEmpty());
	}
}
