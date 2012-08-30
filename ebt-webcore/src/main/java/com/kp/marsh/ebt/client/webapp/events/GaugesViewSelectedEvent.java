/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Usato per segnalare che l'utente vuole visualizzare i gauges
 */
public class GaugesViewSelectedEvent extends GwtEvent<GaugesViewSelectedHandler> {

	public static Type<GaugesViewSelectedHandler> TYPE = new Type<GaugesViewSelectedHandler>();
	
	public GaugesViewSelectedEvent() {
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<GaugesViewSelectedHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(GaugesViewSelectedHandler handler) {
		handler.showGauges(this);

	}

}
