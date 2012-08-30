/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Usato per segnalare che l'utente ha selezionato una pagina con altre lob
 */
public class ChangePageEvent extends GwtEvent<ChangePageHandler> {

	public static Type<ChangePageHandler> TYPE = new Type<ChangePageHandler>();
	
	private final int page; 
	
	public ChangePageEvent(int newPageIndex) {
		this.page = newPageIndex;
	}
	
	public int getPage() {
		return page;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ChangePageHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(ChangePageHandler handler) {
		handler.onChangePage(this);

	}

}
