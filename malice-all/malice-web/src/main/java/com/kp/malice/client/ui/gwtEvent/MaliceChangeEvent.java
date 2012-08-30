package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

    public class MaliceChangeEvent extends GwtEvent<MaliceChangeHandler>{
        public static final Type<MaliceChangeHandler> TYPE = new Type<MaliceChangeHandler>();
        
        @Override
        public com.google.gwt.event.shared.GwtEvent.Type<MaliceChangeHandler> getAssociatedType() {
            return (Type) TYPE;
        }

        @Override
        protected void dispatch(MaliceChangeHandler handler) {
            handler.onChange(this);
        }
    }