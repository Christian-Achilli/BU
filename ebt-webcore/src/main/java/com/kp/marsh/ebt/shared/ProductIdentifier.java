/**
 * 
 */
package com.kp.marsh.ebt.shared;

/**
 * @author christianachilli
 * Wrapper for product identifier
 */
public class ProductIdentifier {
	int id;
	
	public int getId() {
		return id;
	}
	
	public ProductIdentifier(int id) {
		this.id = id;
	}
	
	@SuppressWarnings("unused")
	private ProductIdentifier() {}

}
