package com.kp.malice.client.tabChiusure;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.kp.malice.client.ui.commonWidgets.DettaglioTitoloWidget;
import com.kp.malice.client.ui.gwtEvent.BackHandler;
import com.kp.malice.client.ui.gwtEvent.DownloadEvent;
import com.kp.malice.client.ui.gwtEvent.DownloadHandler;
import com.kp.malice.shared.proxies.DettaglioIncassoTitoloProxy;
import com.kp.malice.shared.proxies.EstrattoContoLioProxy;
import com.kp.malice.shared.proxies.IncassoTitoloProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class ChiusureDettaglioTitolo extends Composite {

    private static ChiusureDettaglioTitoloUiBinder uiBinder = GWT.create(ChiusureDettaglioTitoloUiBinder.class);

    interface ChiusureDettaglioTitoloUiBinder extends UiBinder<Widget, ChiusureDettaglioTitolo> {
    }

    @UiField
    ChiusureToolbarDettaglioTitolo toolbarChiusureDettaglioTitolo;

    @UiField
    DettaglioTitoloWidget dettaglio;
    
    private NewTitoloProxy newTitoloProxy;

    public ChiusureDettaglioTitolo() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void init(NewTitoloProxy titoloProxy, String meseChiusura, EstrattoContoLioProxy estrattoContoLioProxySelezionato) {
        GWT.log("ChiusureDettaglioTitolo.init");
        toolbarChiusureDettaglioTitolo.init(titoloProxy, meseChiusura, estrattoContoLioProxySelezionato);
        dettaglio.init(titoloProxy);
        newTitoloProxy = titoloProxy;
    }
    
    @UiHandler("toolbarChiusureDettaglioTitolo")
    void onDownloadDettaglioTitolo(DownloadEvent event){
        GWT.log("ChiusureDettaglioTitolo.onDownloadDettaglioTitolo: catch DownloadEvent");
        GWT.log("ChiusureDettaglioTitolo.onDownloadDettaglioTitolo: aggiungo titolo al DownloadEvent");
        event.setObject(newTitoloProxy);
    }

    public HandlerRegistration addBackHandler(BackHandler handler) {
        return toolbarChiusureDettaglioTitolo.addBackHandler(handler);
    }

    public HandlerRegistration addDownloadHandler(DownloadHandler handler) {
        return toolbarChiusureDettaglioTitolo.addDownloadHandler(handler);
    }

    public HasData<IncassoTitoloProxy> getTabellaIncassi() {
        return dettaglio.getTabellaIncassi();
    }
}
