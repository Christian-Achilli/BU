package com.kp.malice.client.ui.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.gwtEvent.AnnullaHandler;
import com.kp.malice.client.ui.gwtEvent.ConfermaEvent.ConfermaHandler;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.proxies.DettaglioIncassoTitoloProxy;
import com.kp.malice.shared.proxies.IncassoTitoloProxy;

public class StornaTitoloWidget extends Composite {
    private static AnnullaTitoloWidgetUiBinder uiBinder = GWT.create(AnnullaTitoloWidgetUiBinder.class);

    interface AnnullaTitoloWidgetUiBinder extends UiBinder<Widget, StornaTitoloWidget> {
    }
    
    @UiField
    Label dataIncasso;
    @UiField
    Label dataRegistrazione;
    @UiField
    Label importoIncasso;
    @UiField
    Label stato;
    @UiField
    Label mezzoPagamento;
    @UiField 
    AnnullaConfermaWidget annullaConfermaWidget;
    
    public StornaTitoloWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    public HandlerRegistration addConfermaHandler(ConfermaHandler handler) {
        return annullaConfermaWidget.addConfermaHandler(handler);
    }
    public HandlerRegistration addConfermaHandler(AnnullaHandler handler) {
        return annullaConfermaWidget.addAnnullaHandler(handler);
    }
    public void populate(IncassoTitoloProxy incassoTitoloProxy){
        dataIncasso.setText(MaliceUtil.getStringDateFormatted(incassoTitoloProxy.getDataIncasso()));
        dataRegistrazione.setText(MaliceUtil.getStringDateFormatted(incassoTitoloProxy.getDataInserimento()));
        importoIncasso.setText(MaliceUtil.getEuroFromBigDecimalCent(incassoTitoloProxy.getImportoIncassoEuroCent()));
        stato.setText(incassoTitoloProxy.getStatoIncasso());
        mezzoPagamento.setText(incassoTitoloProxy.getStringMezzoPagamento()); 
    }
}
