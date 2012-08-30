package com.kp.malice.client.ui.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.gwtEvent.BackEvent;
import com.kp.malice.client.ui.gwtEvent.BackHandler;
import com.kp.malice.client.ui.gwtEvent.DownloadEvent;
import com.kp.malice.client.ui.gwtEvent.DownloadHandler;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.proxies.EstrattoContoLioProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class ToolbarChiusureDettaglioTitolo extends Composite {

    private static ToolbarChiusureDettaglioTitoloUiBinder uiBinder = GWT
            .create(ToolbarChiusureDettaglioTitoloUiBinder.class);

    interface ToolbarChiusureDettaglioTitoloUiBinder extends UiBinder<Widget, ToolbarChiusureDettaglioTitolo> {
    }

    @UiField
    Button indietroButton;

//    @UiField
//    Button downloadButton;

    @UiField
    Label mese;
    @UiField
    Label brokerCh;
    @UiField
    Label numeroTitolo;

    public ToolbarChiusureDettaglioTitolo() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("indietroButton")
    void onClickIndietro(ClickEvent e) {
        GWT.log("ToolbarChiusureTitoli.onClickIndietro: catch ClickEvent");
        GWT.log("ToolbarChiusureTitoli.onClickIndietro: fire BackEvent");
        fireEvent(new BackEvent());
    }

//    @UiHandler("downloadButton")
//    void onClickDownload(ClickEvent event) {
//        GWT.log("ToolbarChiusureTitoli.onClickDownload: catch ClickEvent");
//        GWT.log("ToolbarChiusureTitoli.onClickDownload: fire DownloadEvent");
//        fireEvent(new DownloadEvent());
//    }

    public void init(NewTitoloProxy titoloProxy, String meseChiusura, EstrattoContoLioProxy estrattoContoLioProxySelezionato) {
        GWT.log("ToolbarChiusureDettaglioTitolo.init: mese=" + meseChiusura);
        mese.setText(meseChiusura);
        brokerCh.setText(estrattoContoLioProxySelezionato.getLabel());
        numeroTitolo.setText(titoloProxy.getCertificatoLloyds().getNumero() +"-"+titoloProxy.getProgressivo());
    }

    public HandlerRegistration addDownloadHandler(DownloadHandler handler) {
        return addHandler(handler, DownloadEvent.TYPE);
    }

    public HandlerRegistration addBackHandler(BackHandler handler) {
        return addHandler(handler, BackEvent.TYPE);
    }

}
