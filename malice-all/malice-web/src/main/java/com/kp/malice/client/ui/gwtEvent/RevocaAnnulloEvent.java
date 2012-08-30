package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

public class RevocaAnnulloEvent extends GwtEvent<RevocaAnnulloHandler> {

    public RevocaAnnulloEvent() {
        super();
    }

    public static final Type<RevocaAnnulloHandler> TYPE = new Type<RevocaAnnulloHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<RevocaAnnulloHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(RevocaAnnulloHandler handler) {
        handler.onRevocaAnnullo(this);
    }
}