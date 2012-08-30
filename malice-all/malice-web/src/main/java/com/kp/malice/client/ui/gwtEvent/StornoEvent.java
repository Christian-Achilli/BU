package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

public class StornoEvent extends GwtEvent<StornoHandler> {

    public StornoEvent() {
        super();
    }

    public static final Type<StornoHandler> TYPE = new Type<StornoHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<StornoHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(StornoHandler handler) {
        handler.onStorno(this);
    }
}