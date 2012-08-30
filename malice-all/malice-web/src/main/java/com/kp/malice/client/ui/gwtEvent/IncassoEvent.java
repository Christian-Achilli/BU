package com.kp.malice.client.ui.gwtEvent;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.shared.MezzoPagamento;
import com.kp.malice.shared.StatoIncasso;

    public class IncassoEvent extends GwtEvent<IncassoHandler>{
       private BigDecimal importoIncasso;
       private StatoIncasso tipoIncasso;
       private MezzoPagamento mezzoPagamento;
       private Date data;

        public IncassoEvent() {
            super();
        }
        
        public IncassoEvent(BigDecimal bigDecimal, StatoIncasso tipoIncasso, MezzoPagamento mezzoPagamento, Date data) {
            super();
            this.importoIncasso = bigDecimal;
            this.tipoIncasso = tipoIncasso;
            this.mezzoPagamento = mezzoPagamento;
            this.data = data;
        }

        public static final Type<IncassoHandler> TYPE = new Type<IncassoHandler>();
        
        @Override
        public com.google.gwt.event.shared.GwtEvent.Type<IncassoHandler> getAssociatedType() {
            return (Type) TYPE;
        }

        @Override
        protected void dispatch(IncassoHandler handler) {
            handler.onIncasso(this);
        }

        public BigDecimal getImportoIncasso() {
            return importoIncasso;
        }

        public StatoIncasso getTipoIncasso() {
            return tipoIncasso;
        }

        public void setImportoIncasso(BigDecimal importoIncasso) {
            this.importoIncasso = importoIncasso;
        }

        public void setTipoIncasso(StatoIncasso tipoIncasso) {
            this.tipoIncasso = tipoIncasso;
        }

        public MezzoPagamento getMezzoPagamento() {
            return mezzoPagamento;
        }

        public void setMezzoPagamento(MezzoPagamento mezzoPagamento) {
            this.mezzoPagamento = mezzoPagamento;
        }

        public Date getData() {
            return data;
        }

        public void setData(Date data) {
            this.data = data;
        }
    }