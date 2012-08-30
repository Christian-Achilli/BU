/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Usato per segnalare che l'utente vuole visualizzare i gauges
 */
public class HistogramsViewSelectedEvent extends GwtEvent<HistogramsViewSelectedHandler> {

	public static Type<HistogramsViewSelectedHandler> TYPE = new Type<HistogramsViewSelectedHandler>();
	
	public HistogramsViewSelectedEvent() {
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<HistogramsViewSelectedHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(HistogramsViewSelectedHandler handler) {
		handler.showHistograms(this);

	}

}
