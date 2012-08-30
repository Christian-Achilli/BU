package com.kp.malice.client.tabChiusure;

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
import com.kp.malice.client.ui.commonWidgets.ConfermaAnnullaNotaChiusureWidget;
import com.kp.malice.client.ui.gwtEvent.AnnullaEvent;
import com.kp.malice.client.ui.gwtEvent.ConfermaNotaEvent.ConfermaNotaHandler;
import com.kp.malice.client.ui.gwtEvent.DownloadEvent;
import com.kp.malice.client.ui.gwtEvent.DownloadHandler;
import com.kp.malice.shared.StatoChiusura;
import com.kp.malice.shared.proxies.ChiusuraLioProxy;

/**
 * Mostra i comandi per filtrare i titoli sulla relativa tabella
 */
public class ChiusureToolbarChiusure extends Composite {

    private static ToolbarChiusuraDelMeseUiBinder uiBinder = GWT.create(ToolbarChiusuraDelMeseUiBinder.class);

    interface ToolbarChiusuraDelMeseUiBinder extends UiBinder<Widget, ChiusureToolbarChiusure> {
    }

    @UiField
    Button chiudiButton;

    @UiField
    Label meseDaChiudere;

    @UiField
    ConfermaAnnullaNotaChiusureWidget confermaAnnullaNotaWidget;

    private ChiusuraLioProxy chiusuraLioProxy;

    public ChiusureToolbarChiusure() {
        initWidget(uiBinder.createAndBindUi(this));
        hideAll();
    }

    @UiHandler("chiudiButton")
    void onClick(ClickEvent e) {
        GWT.log("ToolbarChiusureChiusure.onClick: catch ClickEvent");
        hideAll();
        confermaAnnullaNotaWidget.init();
        confermaAnnullaNotaWidget.setVisible(true);
    }

    private void hideAll() {
        chiudiButton.setVisible(false);
        confermaAnnullaNotaWidget.setVisible(false);
    }

    @UiHandler("confermaAnnullaNotaWidget")
    void onAnnulla(AnnullaEvent event) {
        GWT.log("ToolbarChiusureChiusure.onAnnulla: catch AnnullaEvent");
        init(chiusuraLioProxy);
        if (chiusuraLioProxy.isAperta())
            chiudiButton.setVisible(true);
    }

    public void init(ChiusuraLioProxy chiusuraLioProxy) {
        GWT.log("ToolbarChiusureChiusure.init: chiusuraLioProxy.getLabel() --> " + chiusuraLioProxy.getLabel());
        this.chiusuraLioProxy = chiusuraLioProxy; //serve per sapere in caso di annullo di chiusura e in accesso per la prima volta come inizializzare la toolbar
        meseDaChiudere.setText(chiusuraLioProxy.getLabel());
        if (StatoChiusura.DA_INVIARE == StatoChiusura.fromString(chiusuraLioProxy.getStatoChiusuraString()))
            chiudiButton.setVisible(true);
        confermaAnnullaNotaWidget.setVisible(false);
    }

    public HandlerRegistration addConfermaNotaHandler(ConfermaNotaHandler handler) {
        return confermaAnnullaNotaWidget.addConfermaNotaHandler(handler);
    }

    public HandlerRegistration addDownloadHandler(DownloadHandler handler) {
        return addHandler(handler, DownloadEvent.TYPE);
    }
}
