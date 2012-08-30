package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

    public class ValidEvent extends GwtEvent<ValidHandler>{
        public ValidEvent() {
            super();
        }

        public static final Type<ValidHandler> TYPE = new Type<ValidHandler>();
        
        @Override
        public com.google.gwt.event.shared.GwtEvent.Type<ValidHandler> getAssociatedType() {
            return (Type) TYPE;
        }

        @Override
        protected void dispatch(ValidHandler handler) {
            handler.onValid(this);
        }
    }