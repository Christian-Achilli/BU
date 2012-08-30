package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.shared.proxies.IncassoTitoloProxy;

public class SelezioneIncassoEvent extends GwtEvent<SelezioneIncassoHandler> {
    private IncassoTitoloProxy incassoTitoloProxy;

    public SelezioneIncassoEvent(IncassoTitoloProxy incassoTitoloProxy) {
        super();
        this.incassoTitoloProxy = incassoTitoloProxy;
    }

    public static final Type<SelezioneIncassoHandler> TYPE = new Type<SelezioneIncassoHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SelezioneIncassoHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(SelezioneIncassoHandler handler) {
        handler.onSelezioneIncasso(this);
    }

    public IncassoTitoloProxy getIncassoTitoloProxy() {
        return incassoTitoloProxy;
    }

    public void setIncassoTitoloProxy(IncassoTitoloProxy incassoTitoloProxy) {
        this.incassoTitoloProxy = incassoTitoloProxy;
    }
    
}