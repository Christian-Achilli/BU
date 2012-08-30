package com.kp.malice.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class TitoloUpdatedEvent extends GwtEvent<TitoloUpdatedEvent.Handler> {

    public interface Handler extends EventHandler {
        void onTitoloUpdated(TitoloUpdatedEvent event);
    }
    public static Type<TitoloUpdatedEvent.Handler> TYPE = new Type<TitoloUpdatedEvent.Handler>();
    public TitoloUpdatedEvent() {
        super();
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<TitoloUpdatedEvent.Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TitoloUpdatedEvent.Handler handler) {
        handler.onTitoloUpdated(this);
    }

}
