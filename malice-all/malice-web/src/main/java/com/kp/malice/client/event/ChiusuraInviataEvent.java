package com.kp.malice.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ChiusuraInviataEvent extends GwtEvent<ChiusuraInviataEvent.Handler> {

    public interface Handler extends EventHandler {
        void onChiusuraInviata(ChiusuraInviataEvent event);
    }

    public static Type<ChiusuraInviataEvent.Handler> TYPE = new Type<ChiusuraInviataEvent.Handler>();

    public ChiusuraInviataEvent() {
        super();
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ChiusuraInviataEvent.Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ChiusuraInviataEvent.Handler handler) {
        handler.onChiusuraInviata(this);
    }

}
