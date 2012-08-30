package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class InserisciTitoloEvent extends GwtEvent<InserisciTitoloHandler> {

    private NewTitoloProxy titoloProxy;

    public InserisciTitoloEvent() {
        super();
    }

    public InserisciTitoloEvent(NewTitoloProxy titoloProxy) {
        super();
        this.titoloProxy = titoloProxy;
    }

    public static final Type<InserisciTitoloHandler> TYPE = new Type<InserisciTitoloHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<InserisciTitoloHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(InserisciTitoloHandler handler) {
        handler.onInserisciTitolo(this);
    }

    public NewTitoloProxy getTitoloProxy() {
        return titoloProxy;
    }

    public void setTitoloProxy(NewTitoloProxy titoloProxy) {
        this.titoloProxy = titoloProxy;
    }
}