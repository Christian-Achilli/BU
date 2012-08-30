package com.kp.malice.client.ui.gwtEvent;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.shared.MezzoPagamento;

public class RecuperoTitoloEvent extends GwtEvent<RecuperoTitoloHandler> {
    private BigDecimal importoIncasso;
    private MezzoPagamento mezzoPagamento;
    private Date datatIncasso;
    
    public RecuperoTitoloEvent() {
        super();
    }


    public RecuperoTitoloEvent(BigDecimal importoIncasso, MezzoPagamento mezzoPagamento, Date datatIncasso) {
        super();
        this.importoIncasso = importoIncasso;
        this.mezzoPagamento = mezzoPagamento;
        this.datatIncasso = datatIncasso;
    }


    public static final Type<RecuperoTitoloHandler> TYPE = new Type<RecuperoTitoloHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<RecuperoTitoloHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(RecuperoTitoloHandler handler) {
        handler.onRecuperoTitolo(this);
    }


    public BigDecimal getImportoIncasso() {
        return importoIncasso;
    }


    public void setImportoIncasso(BigDecimal importoIncasso) {
        this.importoIncasso = importoIncasso;
    }


    public MezzoPagamento getMezzoPagamento() {
        return mezzoPagamento;
    }


    public void setMezzoPagamento(MezzoPagamento mezzoPagamento) {
        this.mezzoPagamento = mezzoPagamento;
    }


    public Date getDatatIncasso() {
        return datatIncasso;
    }


    public void setDatatIncasso(Date datatIncasso) {
        this.datatIncasso = datatIncasso;
    }

}