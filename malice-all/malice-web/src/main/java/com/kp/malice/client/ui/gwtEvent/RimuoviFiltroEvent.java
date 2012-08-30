package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

public class RimuoviFiltroEvent extends GwtEvent<RimuoviFiltroHandler> {

    public RimuoviFiltroEvent() {
        super();
    }

    public static final Type<RimuoviFiltroHandler> TYPE = new Type<RimuoviFiltroHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<RimuoviFiltroHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(RimuoviFiltroHandler handler) {
        handler.rimuovi(this);
    }
}
