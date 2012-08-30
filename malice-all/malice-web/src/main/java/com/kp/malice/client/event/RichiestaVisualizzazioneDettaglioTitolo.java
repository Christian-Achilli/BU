package com.kp.malice.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class RichiestaVisualizzazioneDettaglioTitolo extends GwtEvent<RichiestaVisualizzazioneDettaglioTitolo.Handler> {

    public interface Handler extends EventHandler {
        public void onVisualizzaDettaglioTitolo(RichiestaVisualizzazioneDettaglioTitolo event);
    }

    NewTitoloProxy titoloProxy;

    public static Type<RichiestaVisualizzazioneDettaglioTitolo.Handler> TYPE = new Type<RichiestaVisualizzazioneDettaglioTitolo.Handler>();

    public RichiestaVisualizzazioneDettaglioTitolo(NewTitoloProxy titoloProxy) {
        super();
        this.titoloProxy = titoloProxy;
    }

    @Override
    public Type<RichiestaVisualizzazioneDettaglioTitolo.Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RichiestaVisualizzazioneDettaglioTitolo.Handler handler) {
        handler.onVisualizzaDettaglioTitolo(this);
    }

    public NewTitoloProxy getTitolo() {
        return titoloProxy;
    }

}