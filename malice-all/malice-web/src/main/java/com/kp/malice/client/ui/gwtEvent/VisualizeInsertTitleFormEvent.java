package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

    public class VisualizeInsertTitleFormEvent extends GwtEvent<VisualizeInsertTitleFormHandler>{
        public static final Type<VisualizeInsertTitleFormHandler> TYPE = new Type<VisualizeInsertTitleFormHandler>();
        
        @Override
        public com.google.gwt.event.shared.GwtEvent.Type<VisualizeInsertTitleFormHandler> getAssociatedType() {
            return (Type) TYPE;
        }

        @Override
        protected void dispatch(VisualizeInsertTitleFormHandler handler) {
            handler.visualizeInsertForm(this);
        }
    }