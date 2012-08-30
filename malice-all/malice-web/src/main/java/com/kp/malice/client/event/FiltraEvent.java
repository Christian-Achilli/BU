package com.kp.malice.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class FiltraEvent extends GwtEvent<FiltraEvent.Handler>{

	public interface Handler extends EventHandler{
		void onFiltraTitoli(FiltraEvent event);
	}

	String valuePerRicerca;

	public static Type<FiltraEvent.Handler> TYPE = new Type<FiltraEvent.Handler>();
	
	public FiltraEvent(String valuePerRicerca) {
		super();
		this.valuePerRicerca = valuePerRicerca;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<FiltraEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FiltraEvent.Handler handler) {
		handler.onFiltraTitoli(this);
	}

	public String getValuePerRicerca() {
		return valuePerRicerca;
	}
	
}
