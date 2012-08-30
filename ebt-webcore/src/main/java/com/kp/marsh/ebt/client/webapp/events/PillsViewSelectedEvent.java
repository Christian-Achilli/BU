/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Usato per segnalare che l'utente vuole visualizzare i gauges
 */
public class PillsViewSelectedEvent extends GwtEvent<PillsViewSelectedHandler> {

	public static Type<PillsViewSelectedHandler> TYPE = new Type<PillsViewSelectedHandler>();
	
	public PillsViewSelectedEvent() {
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PillsViewSelectedHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(PillsViewSelectedHandler handler) {
		handler.showPills(this);

	}

}
