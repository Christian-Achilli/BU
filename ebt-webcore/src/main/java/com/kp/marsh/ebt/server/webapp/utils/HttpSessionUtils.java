/**
 * 
 */
package com.kp.marsh.ebt.server.webapp.utils;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

/**
 * @author christianachilli
 *
 * Utilities to handle session values used by the application
 *
 */
public final class HttpSessionUtils {


	private static final String SESSION_VALUE_MAP = "SESSION_VALUE_MAP";


	/**
	 * Get a HashMap from the session that the app uses for its own purposes
	 *
	 * @param session   session from which to get HashMap
	 * @return          A HashMap
	 */
	@SuppressWarnings("unchecked")
	private static HashMap<String, String> getAppSessionValueMap(HttpSession session) {
		// If the session does not contain anything, create a new HashMap
		if (session.getAttribute(SESSION_VALUE_MAP) == null) {
			session.setAttribute(SESSION_VALUE_MAP, new HashMap<String, String>());
		}

		// Return the HashMap
		return (HashMap<String, String>) session.getAttribute(SESSION_VALUE_MAP);
	}

	
	/**
	 * @param session the session we want the app value hash map from
	 * @param key the key we choose
	 * @param value the value we want
	 */
	public static void addStringToAppSessionValue(HttpSession session, String key, String value) {
		
		getAppSessionValueMap(session).put(key, value);
	
	}
	
	
	/**
	 * @param session
	 * @param key
	 * @return the value paired to the provided <code>key</code> or null
	 */
	public static String getStringFromAppSessionValueMap(HttpSession session, String key) {
		
		return (String)getAppSessionValueMap(session).get(key);
		
	}
	
	
}
