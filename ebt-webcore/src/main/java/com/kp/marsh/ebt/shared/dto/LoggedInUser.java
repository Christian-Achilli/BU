/**
 * 
 */
package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author christianachilli
 *
 * The user that is logged into the system. Under the hood it's an information owner.
 *
 */
public class LoggedInUser implements IsSerializable {

	
	private int id; // the associated information owner id
	private String ownerType; // cliente (gruppo commerciale), ce, office, nation, corporate
	private Integer officeId;
	private String officeName;
	private String description;
	private boolean isManager; // true se l'utente è un manager
	private boolean hasClients; // true if the manager has some gruppo commerciale
	
	public LoggedInUser() {
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Integer getParentId() {
		return officeId;
	}
	public void setParentId(Integer parentId) {
		this.officeId = parentId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isManager() {
		return isManager;
	}
	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}
	public boolean hasClients() {
		return hasClients;
	}
	public void setHasClients(boolean hasClients) {
		this.hasClients = hasClients;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getOfficeName() {
		return officeName;
	}
	
	
	
	
	
}
