package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

    public class BackEvent extends GwtEvent<BackHandler>{
        public static final Type<BackHandler> TYPE = new Type<BackHandler>();
        
        @Override
        public com.google.gwt.event.shared.GwtEvent.Type<BackHandler> getAssociatedType() {
            return (Type) TYPE;
        }

        @Override
        protected void dispatch(BackHandler handler) {
            handler.onBack(this);
        }
    }