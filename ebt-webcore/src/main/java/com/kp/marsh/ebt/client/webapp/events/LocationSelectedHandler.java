/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Invocato quando nel cell browser viene selezionato uno location (CE o ufficio) diversa dalla precedente selezione
 */
public interface LocationSelectedHandler extends EventHandler {

	public void onNewLocationSelected(LocationSelectedEvent e);
	
}
