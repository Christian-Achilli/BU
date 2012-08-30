package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.shared.proxies.ScritturaContabileProxy;

public class SelezioneScritturaContabileEvent extends GwtEvent<SelezioneScritturaContabileHandler> {
    private ScritturaContabileProxy scritturaContabileProxy;

    public SelezioneScritturaContabileEvent(ScritturaContabileProxy scritturaContabileProxy) {
        super();
        this.scritturaContabileProxy = scritturaContabileProxy;
    }

    public static final Type<SelezioneScritturaContabileHandler> TYPE = new Type<SelezioneScritturaContabileHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SelezioneScritturaContabileHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(SelezioneScritturaContabileHandler handler) {
        handler.onSelezioneScritturaContabile(this);
    }

    public ScritturaContabileProxy getScritturaContabileProxy() {
        return scritturaContabileProxy;
    }

    public void setScritturaContabileProxy(ScritturaContabileProxy scritturaContabileProxy) {
        this.scritturaContabileProxy = scritturaContabileProxy;
    }
}