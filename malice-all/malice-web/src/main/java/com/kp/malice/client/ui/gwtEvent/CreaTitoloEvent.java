package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

public class CreaTitoloEvent extends GwtEvent<CreaTitoloHandler> {

    public CreaTitoloEvent() {
        super();
    }

    public static final Type<CreaTitoloHandler> TYPE = new Type<CreaTitoloHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<CreaTitoloHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(CreaTitoloHandler handler) {
        handler.onCreaTitolo(this);
    }
}