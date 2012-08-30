/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Usato per segnalare che l'utente vuole visualizzare i gauges
 */
public class NavigationBoxSelectedEvent extends GwtEvent<NavigationBoxSelectedHandler> {

	public static Type<NavigationBoxSelectedHandler> TYPE = new Type<NavigationBoxSelectedHandler>();
	
	public NavigationBoxSelectedEvent() {
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NavigationBoxSelectedHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(NavigationBoxSelectedHandler handler) {
		handler.createList(this);

	}

}
