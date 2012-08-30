/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.events;

import com.google.gwt.event.shared.GwtEvent;
import com.kp.marsh.ebt.shared.dto.LightInfoOwnerDto;

/**
 * 
 */
public class AggiornaTotColonnaIstoEvent extends GwtEvent<AggiornaTotColonnaIstoHandler> {

	public static Type<AggiornaTotColonnaIstoHandler> TYPE = new Type<AggiornaTotColonnaIstoHandler>();
	
	
	public AggiornaTotColonnaIstoEvent() {}
	
	private  LightInfoOwnerDto[] infoOwnerDtoArray;
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AggiornaTotColonnaIstoHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
		
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(AggiornaTotColonnaIstoHandler handler) {
		handler.onAggiornaTotaliColonnaIstoEvent(this);

	}

	public void setInfoOwnerDtoArray(LightInfoOwnerDto[] infoOwnerDtoArray) {
		this.infoOwnerDtoArray = infoOwnerDtoArray;
		
	}

	public LightInfoOwnerDto[] getInfoOwnerDtoArray() {
		return infoOwnerDtoArray;
		
	}


}
