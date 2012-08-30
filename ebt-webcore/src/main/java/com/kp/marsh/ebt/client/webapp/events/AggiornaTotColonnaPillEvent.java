/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.events;

import com.google.gwt.event.shared.GwtEvent;
import com.kp.marsh.ebt.shared.dto.ProductInfoDTO;
import com.kp.marsh.ebt.shared.dto.ProductTotalsManager;

/**
 * 
 */
public class AggiornaTotColonnaPillEvent extends GwtEvent<AggiornaTotColonnaPillHandler> {

	public static Type<AggiornaTotColonnaPillHandler> TYPE = new Type<AggiornaTotColonnaPillHandler>();
	
	
	public AggiornaTotColonnaPillEvent() {
	}
	
	private ProductTotalsManager productTotalsManager;
	
	private int val;
	
	private ProductInfoDTO committedProduct;
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AggiornaTotColonnaPillHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(AggiornaTotColonnaPillHandler handler) {
		handler.onAggiornaTotaliColonnaEvent(this);

	}


	public ProductTotalsManager getTotalsManager() {
		return productTotalsManager;
	}


	public ProductInfoDTO getCommittedProduct() {
		return committedProduct;
	}


	public int getChangeVal() {
		return val;
	}


	public void setTotalsManager(ProductTotalsManager totalsManager) {
		productTotalsManager = totalsManager;		
	}


	public void setCommittedProduct(ProductInfoDTO clickedPillDTO) {
		committedProduct = clickedPillDTO;		
	}


	public void setChangeVal(int newValMinusOldVal) {
		val= newValMinusOldVal;
		
	}

}
