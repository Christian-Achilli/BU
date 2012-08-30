package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

public class SaveDettaglioTitoloEvent extends GwtEvent<SaveDettaglioTitoloHandler> {
private String subAgente;
private String codCig;
    public SaveDettaglioTitoloEvent() {
        super();
    }

    public static final Type<SaveDettaglioTitoloHandler> TYPE = new Type<SaveDettaglioTitoloHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SaveDettaglioTitoloHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(SaveDettaglioTitoloHandler handler) {
        handler.onSalva(this);
    }

    public String getSubAgente() {
        return subAgente;
    }

    public void setSubAgente(String subAgente) {
        this.subAgente = subAgente;
    }

    public String getCodCig() {
        return codCig;
    }

    public void setCodCig(String codCig) {
        this.codCig = codCig;
    }

    @Override
    public void kill() {
        super.kill();
    }
    
    public boolean isStillLive() {
        return super.isLive();
    }

}