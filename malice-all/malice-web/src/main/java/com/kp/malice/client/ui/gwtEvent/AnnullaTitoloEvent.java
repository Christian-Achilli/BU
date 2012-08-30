package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.GwtEvent;

    public class AnnullaTitoloEvent extends GwtEvent<AnnullaTitoloHandler>{
        public static final Type<AnnullaTitoloHandler> TYPE = new Type<AnnullaTitoloHandler>();
        
        private String nota;
        
        public AnnullaTitoloEvent(String nota) {
            super();
            this.nota = nota;
        }

        @Override
        public com.google.gwt.event.shared.GwtEvent.Type<AnnullaTitoloHandler> getAssociatedType() {
            return (Type) TYPE;
        }

        @Override
        protected void dispatch(AnnullaTitoloHandler handler) {
            handler.onAnnulla(this);
        }

        public String getNota() {
            return nota;
        }

        public void setNota(String nota) {
            this.nota = nota;
        }
    }