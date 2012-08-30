/**
 * 
 */
package com.kp.marsh.ebt.shared.dto;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author christianachilli
 *
 * A client pertaining to a certain Client Executive
 */
public class MarshClientDTO implements IsSerializable {

	private int ceId; // the id of the CE this client belongs to
	
	private int clientId; // the id of this client
	
	private String clientName; // the description of this client
	
	private List<ProductInfoDTO> productInfoList;// initialized in this method getAvailableBusinessInformationByCE(int id) in the class DataServiceImpl
	
	private RowTotalsManager clientTotalsManager;
	
	/*left for RPC compatibility*/
	@Deprecated 
	public MarshClientDTO() {}
	
	
	
	public MarshClientDTO(RowTotalsManager rtm) {
		this.clientTotalsManager = rtm;
	}
	
	public int getCeId() {
		return ceId;
	}

	public void setCeId(int ceId) {
		this.ceId = ceId;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public List<ProductInfoDTO> getProductInfoList() {
		return productInfoList;
	}

	public void setProductInfoList(List<ProductInfoDTO> productInfoList) {
		this.productInfoList = productInfoList;
	}
	
	
	/**
	 * Usata per popolare la ui perchè restituisce l'info di reset al bisogno
	 * @param id the business information id
	 * @return the available business info with id <code>id</code> or the corresponding reset info
	 */
//	public ProductInfoDTO getProductInfoById(int id) {
//		for (ProductInfoDTO productInfo : getProductInfoList()) {
//			if(productInfo.getId() ==id) 
//				return productInfo;
//		}
//		
//		ProductInfoDTO resetInfo = new ProductInfoDTO(this, new ResetInfoDTO(clientId, id, null, "", null), null);
//		
//		return resetInfo; 
//	}
	
	/**
	 * @param productId the id of the product
	 * @return the product info available for the product with Id <code>productId</code> or null
	 */
	public ProductInfoDTO getProductInfoByProductId(int productId) {
		for (ProductInfoDTO productInfo : getProductInfoList()) {
			if(productInfo.getId() == productId) {
				return productInfo;
			}
		}
		
		return null;
	}

	public RowTotalsManager getClientTotalsManager() {
		return clientTotalsManager;
	}
	

}
