package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

public class DelegaEvent extends GwtEvent<DelegaHandler> {

    public DelegaEvent() {
        super();
    }

    public static final Type<DelegaHandler> TYPE = new Type<DelegaHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<DelegaHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(DelegaHandler handler) {
        handler.onDelega(this);
    }
}