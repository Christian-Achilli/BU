package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BusinessInformationLiteDTO implements IsSerializable {
	private int productId;
	private int informationOwnerId;
	private int amount;
	
	
	public BusinessInformationLiteDTO(int productId, int ownerId, int amount) {
		this.informationOwnerId = ownerId;
		this.productId = productId;
		this.setAmount(amount);
	}
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getInformationOwnerId() {
		return informationOwnerId;
	}
	public void setInformationOwnerId(int informationOwnerId) {
		this.informationOwnerId = informationOwnerId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + informationOwnerId;
		result = prime * result + productId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessInformationLiteDTO other = (BusinessInformationLiteDTO) obj;
		if (informationOwnerId != other.informationOwnerId)
			return false;
		if (productId != other.productId)
			return false;
		return true;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}
	
	
	
}
