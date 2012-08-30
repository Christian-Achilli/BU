package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;

/**
 * @author christianachilli
 * Handle totals by product.
 * 
 * The totals map has a total manager for each product.
 * 
 */
public class ProductTotalsManager extends TotalsManager implements IsSerializable {
	
	
	/**
	 * @param productId
	 * @return the totals manager for the product with id <code>id</code>
	 */
	public TotalsManager getTotalManagerByProductid(int productId) {
		return totalsMap.get(productId);
	}
	
	public TotalsManager getTotalsManagerByLobId(String lobId) {
		return lobId!= null ? totalsMap.get(Integer.parseInt(lobId)) : null;
	}
	
	public ProductTotalsManager() {
		super();
	}
	
	
	/**
	 * Add a business info value to a product or a line of business
	 * Called when iterating business info from DB
	 * @param whatInfoAmI
	 */
	public void add(BusinessInfoDTO whatInfoAmI) {
		
		TotalsManager prodTotMan = getTotalManagerByProductid(whatInfoAmI.getProductId()); // to test if it's a product
		
		TotalsManager lobTotMan  = getTotalsManagerByLobId(whatInfoAmI.getLobId()); // to test if it's a lob
		
		if(null == prodTotMan) {
			prodTotMan = new ProductTotalsManager();
			totalsMap.put(whatInfoAmI.getProductId(), prodTotMan);
		}
		if(null == lobTotMan && null != whatInfoAmI.getLobId()) {
			lobTotMan = new ProductTotalsManager();
			totalsMap.put(Integer.parseInt(whatInfoAmI.getLobId()), lobTotMan);
		}
		

		try {
			switch (whatInfoAmI.getValueType()) {

			case ACHIEVED:
				prodTotMan.addAchieved(Integer.parseInt(whatInfoAmI.getValue()));
				if(lobTotMan != null) lobTotMan.addAchieved(Integer.parseInt(whatInfoAmI.getValue()));
				break;

			case POTENTIAL:
			case BROKER_POT:
			case COMPANY_POT:
				prodTotMan.addPotential(Integer.parseInt(whatInfoAmI.getValue()));
				if(lobTotMan != null) lobTotMan.addPotential(Integer.parseInt(whatInfoAmI.getValue()));
				break;

			case FINAL_BALANCE:
				prodTotMan.addFinalBalance(Integer.parseInt(whatInfoAmI.getValue()));
				if(lobTotMan != null) lobTotMan.addFinalBalance(Integer.parseInt(whatInfoAmI.getValue()));
				break;

			default:
				break;
			}
		} catch (NumberFormatException e) {
			// no need to trace this exception the potential may not be a number. Don't use cast because it's expensive
		}

	}
	
	public void updateLobValue(BusinessInfoValueType infoType, String lobId, int newValue) {
		TotalsManager lobTotMan  = getTotalsManagerByLobId(lobId); // to test if it's a lob
		if(null == lobTotMan) {
			lobTotMan = new ProductTotalsManager();
			totalsMap.put(Integer.parseInt(lobId), lobTotMan);
		}
		switch (infoType) {
			case ACHIEVED:
				lobTotMan.addAchieved(newValue);
				break;
			case POTENTIAL:
			case BROKER_POT:
			case COMPANY_POT:
				lobTotMan.addPotential(newValue);
				break;
			case FINAL_BALANCE:
				break;
			default:
				break;
		}
	}
	
	public void updateProdValue(BusinessInfoValueType infoType, int productId, int newValue) {
		TotalsManager prodTotMan = getTotalManagerByProductid(productId); // to test if it's a product
		if(null == prodTotMan) {
			prodTotMan = new ProductTotalsManager();
			totalsMap.put(productId, prodTotMan);
		}
		switch (infoType) {
			case ACHIEVED:
				prodTotMan.addAchieved(newValue);
				break;
			case POTENTIAL:
			case BROKER_POT:
			case COMPANY_POT:
				prodTotMan.addPotential(newValue);
				break;
			case FINAL_BALANCE:
				break;
			default:
				break;
		}
	}
	
	public void updateValue(BusinessInfoValueType infoType, int productId, String lobId, int newValue) {
		updateProdValue(infoType, productId, newValue);
		updateLobValue(infoType, lobId, newValue);
	}
	
	public void setValue(BusinessInfoValueType infoType, int productId, String lobId, int newValue) {
		setProdValue(infoType, productId, newValue);
		setLobValue(infoType, lobId, newValue);
	}
	public void setLobValue(BusinessInfoValueType infoType, String lobId, int newValue) {
		TotalsManager lobTotMan  = getTotalsManagerByLobId(lobId); // to test if it's a lob
		if(null == lobTotMan) {
			lobTotMan = new ProductTotalsManager();
			totalsMap.put(Integer.parseInt(lobId), lobTotMan);
		}
		lobTotMan.setPotential(newValue);
	}
	
	public void setProdValue(BusinessInfoValueType infoType, int productId, int newValue) {
		TotalsManager prodTotMan = getTotalManagerByProductid(productId); // to test if it's a product
		if(null == prodTotMan) {
			prodTotMan = new ProductTotalsManager();
			totalsMap.put(productId, prodTotMan);
		}
		prodTotMan.setPotential(newValue);
	}
	
	

}