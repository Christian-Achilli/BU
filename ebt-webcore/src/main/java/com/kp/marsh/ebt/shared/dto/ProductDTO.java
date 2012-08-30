package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * @author christianachilli
 *
 *DTO instantiated by Dozer. The id is the one of the product.
 *
 */
public abstract class ProductDTO implements IsSerializable {

	
	private int id;
	private String itemType;
	private String itemName;
	private String parentId;
	private String notes = "Content not available"; // the note describing this product
	
	
	public ProductDTO() {
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getNotes() {
		return notes;
	}


	public void setNotes(String htmlNote) {
		this.notes = htmlNote;
	}

	
}
