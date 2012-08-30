package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class SelezioneTitoloEvent extends GwtEvent<SelezioneTitoloHandler> {
    private NewTitoloProxy titoloProxy;

    public SelezioneTitoloEvent(NewTitoloProxy titoloProxy) {
        super();
        this.titoloProxy = titoloProxy;
        GWT.log("incassi size: "+this.titoloProxy.getIncassiOrderByDataInserimentoDesc().size());
    }

    public static final Type<SelezioneTitoloHandler> TYPE = new Type<SelezioneTitoloHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SelezioneTitoloHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(SelezioneTitoloHandler handler) {
        handler.onSelezioneTitolo(this);
    }

    public NewTitoloProxy getTitoloProxy() {
        return titoloProxy;
    }

    public void setTitoloProxy(NewTitoloProxy titoloProxy) {
        this.titoloProxy = titoloProxy;
    }
}