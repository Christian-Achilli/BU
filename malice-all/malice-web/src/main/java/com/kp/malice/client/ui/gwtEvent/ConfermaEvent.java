package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.client.ui.gwtEvent.ConfermaEvent.ConfermaHandler;

public class ConfermaEvent extends GwtEvent<ConfermaHandler> {

    public ConfermaEvent() {
        super();
    }

    public static final Type<ConfermaHandler> TYPE = new Type<ConfermaHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ConfermaHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(ConfermaHandler handler) {
        handler.onConferma(this);
    }

    public interface ConfermaHandler extends EventHandler {
        void onConferma(ConfermaEvent confermaEvent);
    }
}