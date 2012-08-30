package com.kp.malice.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class VisualizzaStatisticheEvent extends GwtEvent<VisualizzaStatisticheEvent.Handler> {

    public interface Handler extends EventHandler {
        public void onVisualizzaStatistiche(VisualizzaStatisticheEvent event);
    }

    public enum Tipo {
        Montante, Provvigioni;
    }

    private Tipo tipo;

    public static Type<VisualizzaStatisticheEvent.Handler> TYPE = new Type<VisualizzaStatisticheEvent.Handler>();

    public VisualizzaStatisticheEvent(Tipo tipo) {
        super();
        this.tipo = tipo;
    }

    @Override
    public Type getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(VisualizzaStatisticheEvent.Handler handler) {
        handler.onVisualizzaStatistiche(this);
    }

    public Tipo getTipo() {
        return tipo;
    }

}
