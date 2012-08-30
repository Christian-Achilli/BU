/**
 * 
 */
package com.kp.marsh.ebt.shared;

/**
 * @author christianachilli
 * wrapper for client id
 */
public class ClientIdentifier {
	int id;

	public int getId() {
		return id;
	}

	public ClientIdentifier(int id) {
		this.id = id;
	}
	
	@SuppressWarnings("unused")
	private ClientIdentifier(){}
}
