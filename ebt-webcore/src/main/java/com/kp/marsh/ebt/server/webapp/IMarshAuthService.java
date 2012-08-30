/**
 * 
 */
package com.kp.marsh.ebt.server.webapp;

/**
 * @author christianachilli
 * Methids to interface marsh active directory
 */
public interface IMarshAuthService {

	
	/**
	 * @param username
	 * @param password
	 * @return the username if the user is authenticated, empty string otherwise.
	 */
	public String authenticate(String username, String password);
	
	
}
