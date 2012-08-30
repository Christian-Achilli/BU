package com.kp.malice.client.ui.commonWidgets;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.kp.malice.client.ui.gwtEvent.ConfermaEvent;
import com.kp.malice.client.ui.gwtEvent.RecuperoTitoloEvent;
import com.kp.malice.client.ui.gwtEvent.RecuperoTitoloHandler;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.MezzoPagamento;

public class RecuperoWidget extends IncassoForm {

    public RecuperoWidget() {
        super();
        init();
    }
    @Override
    public void init() {
        super.init();
        tipo.setItemSelected(1, true);//EFFETTIVO
        GWT.log("RecuperoWidget.init");
        labelDataRegistrazione.setText(MaliceUtil.getStringDateFormatted(new Date()));
    }
    
    @Override
    @UiHandler("tipo")
    void onChangeValueTipo(ChangeEvent e) {
        GWT.log("RecuperoWidget: onChangeValueTipo");
        tipo.setItemSelected(1, true);//EFFETTIVO
    }
    
    @Override
    @UiHandler("annullaConfermaWidget")
    void onConferma(ConfermaEvent e) {
        GWT.log("RecuperoWidget: catch ConfermaEvent");
        if (checkFormCompleted()) {
            GWT.log("RecuperoWidget: fire RecuperoTitoloEven" +
            		"t");
            GWT.log("importo incasso: " + importoIncasso.getBigDecimalValue());
            fireEvent(new RecuperoTitoloEvent(importoIncasso.getBigDecimalValue(),MezzoPagamento.fromString(mezzoPagamento.getValue(mezzoPagamento
                    .getSelectedIndex())), dataBoxIncasso.getValue()));
        } else {
            Window.alert("Attenzione: tutti i campi sono obbligatori.");
        }
    }

    public HandlerRegistration addRecuperoHandler(RecuperoTitoloHandler handler) {
        return addHandler(handler, RecuperoTitoloEvent.TYPE);
    }
    
    public void setDataRegistrazione(Date dataRegistrazione) {
        labelDataRegistrazione.setText(MaliceUtil.getStringDateFormatted(dataRegistrazione)); 
    }
}
