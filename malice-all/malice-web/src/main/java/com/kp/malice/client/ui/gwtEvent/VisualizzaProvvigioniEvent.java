package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

    public class VisualizzaProvvigioniEvent extends GwtEvent<VisualizzaProvvigioniHandler>{

        public VisualizzaProvvigioniEvent() {
            super();
        }

        public static final Type<VisualizzaProvvigioniHandler> TYPE = new Type<VisualizzaProvvigioniHandler>();
        
        @Override
        public com.google.gwt.event.shared.GwtEvent.Type<VisualizzaProvvigioniHandler> getAssociatedType() {
            return (Type) TYPE;
        }

        @Override
        protected void dispatch(VisualizzaProvvigioniHandler handler) {
            handler.onVisualizzaProvvigioni(this);
        }
    }