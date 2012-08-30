package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.shared.StatusTitolo;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class AggiornaStatoTitoloERimessaEvent extends GwtEvent<AggiornaStatoTitoloERimessaHandler> {
    private NewTitoloProxy titoloProxy;
    private StatusTitolo statusTitolo;

    public static final Type<AggiornaStatoTitoloERimessaHandler> TYPE = new Type<AggiornaStatoTitoloERimessaHandler>();

    public AggiornaStatoTitoloERimessaEvent(NewTitoloProxy titoloProxy, StatusTitolo statusTitolo) {
        super();
        this.titoloProxy = titoloProxy;
        this.statusTitolo = statusTitolo;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<AggiornaStatoTitoloERimessaHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(AggiornaStatoTitoloERimessaHandler handler) {
        handler.aggiornaStatoTitoloERimessa(this);
    }

    public NewTitoloProxy getTitoloProxy() {
        return titoloProxy;
    }

    public void setTitoloProxy(NewTitoloProxy titoloProxy) {
        this.titoloProxy = titoloProxy;
    }

    public StatusTitolo getStatusTitolo() {
        return statusTitolo;
    }

    public void setStatusTitolo(StatusTitolo statusTitolo) {
        this.statusTitolo = statusTitolo;
    }

}