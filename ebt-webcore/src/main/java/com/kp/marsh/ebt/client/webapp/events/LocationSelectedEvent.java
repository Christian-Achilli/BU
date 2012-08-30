/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.events;

import com.google.gwt.event.shared.GwtEvent;
import com.kp.marsh.ebt.shared.dto.OwnerType;

/**
 * Usato per segnalare che l'utente ha selezionato un diverso ufficio, CE nel cell browser
 */
public class LocationSelectedEvent extends GwtEvent<LocationSelectedHandler> {

	public static Type<LocationSelectedHandler> TYPE = new Type<LocationSelectedHandler>();
	
	private final String selectionId; 
	private final OwnerType type; 
	private final String selectedDescriptionUfficio;
	private final String selectedDescriptionCE;
	private final String selectedDescriptionGC;
	
	public LocationSelectedEvent(String selectionId, OwnerType type, String selectedDescriptionUfficio, String selectedDescriptionCE, String selectedDescriptionGC) {
		this.selectionId = selectionId;
		this.type = type;
		this.selectedDescriptionUfficio = selectedDescriptionUfficio;
		this.selectedDescriptionCE = selectedDescriptionCE;
		this.selectedDescriptionGC = selectedDescriptionGC;
	}
	
	public String getSelectionId() {
		return selectionId;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LocationSelectedHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(LocationSelectedHandler handler) {
		handler.onNewLocationSelected(this);

	}

	public OwnerType getType() {
		return type;
	}

	public String getSelectedDescriptionUfficio() {
		return selectedDescriptionUfficio;
	}

	public String getSelectedDescriptionCE() {
		return selectedDescriptionCE;
	}

	public String getSelectedDescriptionGC() {
		return selectedDescriptionGC;
	}

}
