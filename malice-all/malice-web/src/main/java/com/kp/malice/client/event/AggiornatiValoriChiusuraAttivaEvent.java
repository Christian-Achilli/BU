package com.kp.malice.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class AggiornatiValoriChiusuraAttivaEvent extends GwtEvent<AggiornatiValoriChiusuraAttivaEvent.Handler>{

	public interface Handler  extends EventHandler {
		void onAggiornatiValoriChiusuraAttiva(AggiornatiValoriChiusuraAttivaEvent event);
	}
	
	public static Type<Handler> TYPE = new Type<Handler>();
	
	public AggiornatiValoriChiusuraAttivaEvent() {
		super();
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onAggiornatiValoriChiusuraAttiva(this);
	}

}
