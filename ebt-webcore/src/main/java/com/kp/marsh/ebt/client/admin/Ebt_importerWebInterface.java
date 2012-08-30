package com.kp.marsh.ebt.client.admin;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.kp.marsh.ebt.client.admin.ui.FormComando;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Ebt_importerWebInterface implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		FormComando controllPanel =  new FormComando();
		RootPanel.get().add(controllPanel);
	}
}
