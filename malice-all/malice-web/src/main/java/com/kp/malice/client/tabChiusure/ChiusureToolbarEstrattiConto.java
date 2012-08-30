package com.kp.malice.client.tabChiusure;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.gwtEvent.BackEvent;
import com.kp.malice.client.ui.gwtEvent.BackHandler;
import com.kp.malice.client.ui.gwtEvent.DownloadEvent;
import com.kp.malice.client.ui.gwtEvent.DownloadHandler;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.StatoChiusura;
import com.kp.malice.shared.proxies.ChiusuraLioProxy;

public class ChiusureToolbarEstrattiConto extends Composite {

    private static ToolbarChiusureEstrattiContoUiBinder uiBinder = GWT
            .create(ToolbarChiusureEstrattiContoUiBinder.class);

    interface ToolbarChiusureEstrattiContoUiBinder extends UiBinder<Widget, ChiusureToolbarEstrattiConto> {
    }

    @UiField
    Button indietroButton;

    @UiField
    Button downloadButton;

    @UiField
    Label mese;
    @UiField
    Label numEstrattoConto;
    @UiField
    Label numTitoli;
    @UiField
    Label premi;
    @UiField
    Label commissioni;
    @UiField
    Label dataInvio;
    @UiField
    Label stato;

    public ChiusureToolbarEstrattiConto() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("indietroButton")
    void onClickIndietro(ClickEvent e) {
        GWT.log("ToolbarChiusureEstrattiConto.onClickIndietro: catch ClickEvent");
        GWT.log("ToolbarChiusureEstrattiConto.onClickIndietro: fire BackEvent");
        fireEvent(new BackEvent());
    }

    @UiHandler("downloadButton")
    void onClickDownload(ClickEvent event) {
        GWT.log("ToolbarChiusureEstrattiConto.onClickDownload: catch ClickEvent");
        GWT.log("ToolbarChiusureEstrattiConto.onClickDownload: fire ClickEvent");
        fireEvent(new DownloadEvent());
    }

    public void init(ChiusuraLioProxy chiusuraLioProxy) {
        GWT.log("ToolbarChiusureEstrattiConto.init: chiusuraLioProxy.getLabel() --> " + chiusuraLioProxy.getLabel());
        mese.setText(chiusuraLioProxy.getLabel());
        numEstrattoConto.setText("" + chiusuraLioProxy.getTotEstrattiConto());
        numTitoli.setText("" + chiusuraLioProxy.getTotTitoli());
        premi.setText(MaliceUtil.getEuroFromBigDecimalCent(chiusuraLioProxy.getTotPremiEuroCent()));
        commissioni.setText(MaliceUtil.getEuroFromBigDecimalCent(chiusuraLioProxy.getTotCommissioniEuroCent()));
        dataInvio.setText(MaliceUtil.getStringDateFormatted(chiusuraLioProxy.getDtInvio()));
        stato.setText(chiusuraLioProxy.getStatoChiusuraString());
//        stato.setResource(StatoChiusura.fromString(chiusuraLioProxy.getStatoChiusuraString()).getImageResource());
    }

    public HandlerRegistration addDownloadHandler(DownloadHandler handler) {
        return addHandler(handler, DownloadEvent.TYPE);
    }
    
    public HandlerRegistration addBackHandler(BackHandler handler) {
        return addHandler(handler, BackEvent.TYPE);
    }
}
