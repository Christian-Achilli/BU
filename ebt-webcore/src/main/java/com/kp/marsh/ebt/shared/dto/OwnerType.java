/**
 * 
 */
package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author christianachilli
 *
 * Used to enum types of information owners
 *
 */
public enum OwnerType implements IsSerializable{

	GRUPPO_COMMERCIALE,
	CE,
	OFFICE,
	NATION,
	AREA,
	CORPORATE;
	
	public static OwnerType fromString(String testString) {
		for (OwnerType ot : OwnerType.values()) {
			if(ot.name().equals(testString)) {
				return ot;
			}
		}
		return null;
	}
}
