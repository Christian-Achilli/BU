/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Invocato quando nel histogram activity o insert potential activity per dire all'header di cambiare la pagina con i totali di prodotto
 */
public interface AggiornaTotColonnaIstoHandler extends EventHandler {

	public void onAggiornaTotaliColonnaIstoEvent(AggiornaTotColonnaIstoEvent e);
	
}