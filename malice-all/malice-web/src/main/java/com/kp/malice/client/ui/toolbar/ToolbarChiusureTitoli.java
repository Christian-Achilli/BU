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

public class ToolbarChiusureTitoli extends Composite {

    private static ToolbarChiusureEstrattiContoUiBinder uiBinder = GWT
            .create(ToolbarChiusureEstrattiContoUiBinder.class);

    interface ToolbarChiusureEstrattiContoUiBinder extends UiBinder<Widget, ToolbarChiusureTitoli> {
    }

    @UiField
    Button indietroButton;

    @UiField
    Button downloadButton;

    @UiField
    Label mese;
    @UiField
    Label brockerCh;
    @UiField
    Label numTitoli;
    @UiField
    Label premi;
    @UiField
    Label commissioni;
    @UiField
    Label rimesse;

    public ToolbarChiusureTitoli() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("indietroButton")
    void onClickIndietro(ClickEvent e) {
        GWT.log("ToolbarChiusureTitoli.onClickIndietro: catch ClickEvent");
        GWT.log("ToolbarChiusureTitoli.onClickIndietro: fire BackEvent");
        fireEvent(new BackEvent());
    }

    @UiHandler("downloadButton")
    void onClickDownload(ClickEvent event) {
        GWT.log("ToolbarChiusureTitoli.onClickDownload: catch ClickEvent");
        GWT.log("ToolbarChiusureTitoli.onClickDownload: fire ClickEvent");
        fireEvent(new DownloadEvent());
    }

    public void init(EstrattoContoLioProxy estrattoContoLioProxy, String meseChiusura) {
        GWT.log("ToolbarChiusureTitoli.init: chiusuraLioProxy.getLabel() --> " + estrattoContoLioProxy.getLabel());
        mese.setText(meseChiusura);
        brockerCh.setText("" + estrattoContoLioProxy.getLabel());
        numTitoli.setText("" + estrattoContoLioProxy.getTotTitoli());
        premi.setText(MaliceUtil.getEuroFromBigDecimalCent(estrattoContoLioProxy.getTotPremiEuroCent()));
        commissioni.setText(MaliceUtil.getEuroFromBigDecimalCent(estrattoContoLioProxy.getTotCommissioniEuroCent()));
        rimesse.setText(MaliceUtil.getEuroFromBigDecimalCent(estrattoContoLioProxy.getTotRimessaEuroCent()));
    }

    public HandlerRegistration addDownloadHandler(DownloadHandler handler) {
        return addHandler(handler, DownloadEvent.TYPE);
    }

    public HandlerRegistration addBackHandler(BackHandler handler) {
        return addHandler(handler, BackEvent.TYPE);
    }
}
