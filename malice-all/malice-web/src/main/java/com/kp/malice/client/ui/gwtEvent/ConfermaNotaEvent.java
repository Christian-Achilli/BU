package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.client.ui.gwtEvent.ConfermaNotaEvent.ConfermaNotaHandler;

public class ConfermaNotaEvent extends GwtEvent<ConfermaNotaHandler> {

    private String nota;

    public ConfermaNotaEvent(String nota) {
        super();
        this.nota = nota;
    }

    public static final Type<ConfermaNotaHandler> TYPE = new Type<ConfermaNotaHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ConfermaNotaHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(ConfermaNotaHandler handler) {
        handler.onConfermaNota(this);
    }

    public interface ConfermaNotaHandler extends EventHandler {
        void onConfermaNota(ConfermaNotaEvent confermaNotaEvent);
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}