package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

    public class VisualizzaListaTitoliEvent extends GwtEvent<VisualizzaListaTitoliHandler>{
        public static final Type<VisualizzaListaTitoliHandler> TYPE = new Type<VisualizzaListaTitoliHandler>();
        
        @Override
        public com.google.gwt.event.shared.GwtEvent.Type<VisualizzaListaTitoliHandler> getAssociatedType() {
            return (Type) TYPE;
        }

        @Override
        protected void dispatch(VisualizzaListaTitoliHandler handler) {
            handler.visualizzaListaTitoli(this);
        }
    }