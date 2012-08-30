package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.client.ui.gwtEvent.SelezioneEstrattoContoEvent.SelezioneEstrattoContoHandler;
import com.kp.malice.shared.proxies.EstrattoContoLioProxy;

public class SelezioneEstrattoContoEvent extends GwtEvent<SelezioneEstrattoContoHandler> {

    private EstrattoContoLioProxy estrattoContoLioProxySelezionato;

    public static final Type<SelezioneEstrattoContoHandler> TYPE = new Type<SelezioneEstrattoContoHandler>();

    public SelezioneEstrattoContoEvent(EstrattoContoLioProxy estrattoContoLioProxySelezionato) {
        super();
        this.estrattoContoLioProxySelezionato = estrattoContoLioProxySelezionato;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SelezioneEstrattoContoHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(SelezioneEstrattoContoHandler handler) {
        handler.onSelezioneEstrattoConto(this);
    }

    public interface SelezioneEstrattoContoHandler extends EventHandler {
        void onSelezioneEstrattoConto(SelezioneEstrattoContoEvent selezioneEstrattoContoEvent);
    }

    public EstrattoContoLioProxy getEstrattoContoLioProxySelezionato() {
        return estrattoContoLioProxySelezionato;
    }

    public void setEstrattoContoLioProxySelezionato(EstrattoContoLioProxy estrattoContoLioProxySelezionato) {
        this.estrattoContoLioProxySelezionato = estrattoContoLioProxySelezionato;
    }

}