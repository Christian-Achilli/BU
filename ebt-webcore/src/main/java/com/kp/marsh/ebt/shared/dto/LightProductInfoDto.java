package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * Contiene il valore di potenziale e achieved per un certo prodotto, relativamente a un determinato information owner
 * 
 */
public class LightProductInfoDto implements IsSerializable {
	
	
	private String productName;
	
	private String productDescription;
	
	private int euroPotValue; // potential in euros
	
	private int euroAchValue; // achieved in euros
	
	private int productId;
	
	
	public LightProductInfoDto() {}


	public int getEuroPotValue() {
		return euroPotValue;
	}


	public void setEuroPotValue(int euroPotValue) {
		this.euroPotValue = euroPotValue;
	}


	public int getEuroAchValue() {
		return euroAchValue;
	}


	public void setEuroAchValue(int euroAchValue) {
		this.euroAchValue = euroAchValue;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getProductDescription() {
		return productDescription;
	}


	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}


	public void setProductId(int productId) {
		this.productId = productId;
	}


	public int getProductId() {
		return productId;
	}
}
