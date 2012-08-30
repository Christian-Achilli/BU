package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;

/**
 * @author christianachilli
 */
public abstract class BusinessInfoDTO implements IsSerializable {
	
	
	private Integer businessInfoId; // the business information businessInfoId
	private String value;
	private int productId; // the product id this info relates to
	private int clientId; // the info owner id
	
	private String lobId; // the lob id
	
	private String postItText = "";
	
	
	@Deprecated /*Left only for RPC compatibility*/
	public BusinessInfoDTO() {}
	
	/**
	 * @param businessInfoId
	 * @param value
	 * @param 
	 */
	public BusinessInfoDTO(Integer bInfoId, String productValue, int productId, int clientId, String lobId) {
		this.businessInfoId = bInfoId; 
		this.value = productValue;
		this.productId = productId;
		this.clientId = clientId;
		this.lobId = lobId;
	}

	/**
	 * @return the value for this product
	 */
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the businessInfoId of the product
	 */
	public Integer getBusinessInfoId() {
		return businessInfoId;
	}

	
	/**
	 * Used because instance of is expensive
	 * @return the business value type of the instance
	 */
	public abstract BusinessInfoValueType getValueType();

	public int getProductId() {
		return productId;
	}

	public int getClientId() {
		return clientId;
	}

	public String getLobId() {
		return lobId;
	}

	public String getPostItText() {
		return postItText;
	}

	public void setPostItText(String postItText) {
		this.postItText = postItText;
	}

	public void setLobId(String lobId) {
		this.lobId = lobId;
	}

}