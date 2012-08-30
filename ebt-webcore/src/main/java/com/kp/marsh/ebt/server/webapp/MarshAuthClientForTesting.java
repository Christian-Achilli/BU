/**
 * 
 */
package com.kp.marsh.ebt.server.webapp;

import com.google.inject.Singleton;

/**
 * @author christianachilli
 * Stub for marsh active directory service
 */
@Singleton
public class MarshAuthClientForTesting implements IMarshAuthService {
	
	
	public MarshAuthClientForTesting() {}
	

	public String authenticate(String username, String password) {

		return username;
	}



}
