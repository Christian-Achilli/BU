/**
 * 
 */
package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author christianachilli
 * Information about an informationOwners
 */
public class NavigationDTO implements IsSerializable {

	private String description; 
	
	private String id; 
	
	private OwnerType informationOwnersValueType;
	
	public NavigationDTO() {
	}
	
	public NavigationDTO(String description, String id, OwnerType informationOwnersValueType) {
		this.description=description;
		this.id=id;
		this.informationOwnersValueType=informationOwnersValueType;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public OwnerType getInformationOwnersValueType() {
		return informationOwnersValueType;
	}

	public void setInformationOwnersValueType(
			OwnerType informationOwnersValueType) {
		this.informationOwnersValueType = informationOwnersValueType;
	}
	
}
