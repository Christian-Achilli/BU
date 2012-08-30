package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

    public class VisualizzaPremiEvent extends GwtEvent<VisualizzaPremiHandler>{

        public VisualizzaPremiEvent() {
            super();
        }

        public static final Type<VisualizzaPremiHandler> TYPE = new Type<VisualizzaPremiHandler>();
        
        @Override
        public com.google.gwt.event.shared.GwtEvent.Type<VisualizzaPremiHandler> getAssociatedType() {
            return (Type) TYPE;
        }

        @Override
        protected void dispatch(VisualizzaPremiHandler handler) {
            handler.onVisualizzaPremi(this);
        }
    }