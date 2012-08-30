package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

    public class ErrorMessageEvent extends GwtEvent<ErrorMessageHandler>{
       private String msg;

        public ErrorMessageEvent() {
            super();
        }
        
        public ErrorMessageEvent(String msg) {
            super();
            this.msg = msg;
        }

        public static final Type<ErrorMessageHandler> TYPE = new Type<ErrorMessageHandler>();
        
        @Override
        public com.google.gwt.event.shared.GwtEvent.Type<ErrorMessageHandler> getAssociatedType() {
            return (Type) TYPE;
        }

        @Override
        protected void dispatch(ErrorMessageHandler handler) {
            handler.onErrorMessage(this);
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
        
    }