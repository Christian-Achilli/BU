package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.client.ui.gwtEvent.SelezioneChiusuraEvent.SelezioneChiusuraHandler;
import com.kp.malice.shared.proxies.ChiusuraLioProxy;

public class SelezioneChiusuraEvent extends GwtEvent<SelezioneChiusuraHandler> {
    private ChiusuraLioProxy chiusuraProxy;

    public SelezioneChiusuraEvent(ChiusuraLioProxy chiusuraProxy) {
        super();
        this.chiusuraProxy = chiusuraProxy;
    }

    public static final Type<SelezioneChiusuraHandler> TYPE = new Type<SelezioneChiusuraHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SelezioneChiusuraHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(SelezioneChiusuraHandler handler) {
        handler.onSelezioneChiusura(this);
    }

    public ChiusuraLioProxy getChiusuraProxy() {
        return chiusuraProxy;
    }

    public void setChiusuraProxy(ChiusuraLioProxy chiusuraProxy) {
        this.chiusuraProxy = chiusuraProxy;
    }

    public interface SelezioneChiusuraHandler extends EventHandler {
        void onSelezioneChiusura(SelezioneChiusuraEvent selezioneChiusuraEvent);
    }
}