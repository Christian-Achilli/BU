package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

public class AnnullaEvent extends GwtEvent<AnnullaHandler> {

    public AnnullaEvent() {
        super();
    }

    public static final Type<AnnullaHandler> TYPE = new Type<AnnullaHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<AnnullaHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(AnnullaHandler handler) {
        handler.onAnnulla(this);
    }

    @Override
    public void kill() {
        super.kill();
    }
    
    
    
}