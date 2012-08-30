package com.kp.malice.client.tabChiusure;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.kp.malice.client.ui.gwtEvent.BackEvent;
import com.kp.malice.client.ui.gwtEvent.ConfermaNotaEvent;
import com.kp.malice.client.ui.gwtEvent.DownloadEvent;
import com.kp.malice.client.ui.gwtEvent.SelezioneChiusuraEvent;
import com.kp.malice.client.ui.gwtEvent.SelezioneEstrattoContoEvent;
import com.kp.malice.client.ui.gwtEvent.SelezioneTitoloEvent;
import com.kp.malice.shared.proxies.ChiusuraLioProxy;
import com.kp.malice.shared.proxies.DettaglioIncassoTitoloProxy;
import com.kp.malice.shared.proxies.EstrattoContoLioProxy;
import com.kp.malice.shared.proxies.IncassoTitoloProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class ChiusureView extends Composite implements IChiusureViewDisplay {

    @UiField
    ChiusureChiusure chiusureChiusure;

    @UiField
    ChiusureEstrattiConto chiusureEstrattiConto;

    @UiField
    ChiusureTitoli chiusureTitoli;

    @UiField
    ChiusureDettaglioTitolo chiusureDettaglioTitolo;

    interface ChiusureViewUiBinder extends UiBinder<Widget, ChiusureView> {
    }

    private static ChiusureViewUiBinder uiBinder = GWT.create(ChiusureViewUiBinder.class);

    private IChiusureViewDisplay.Listener listener;

    @Inject
    public ChiusureView() {
        initWidget(uiBinder.createAndBindUi(this));
        hideAll();
        chiusureChiusure.setVisible(true);
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void hideAll() {
        chiusureChiusure.setVisible(false);
        chiusureEstrattiConto.setVisible(false);
        chiusureTitoli.setVisible(false);
        chiusureDettaglioTitolo.setVisible(false);
    }

    /******* CHIUSURE ********/
    @UiHandler("chiusureChiusure")
    void onConfermaNota(ConfermaNotaEvent event) {
        GWT.log("ToolbarChiusureChiusure.onConfermaNota: catch ConfermaNotaEvent");
        listener.chiudiMese(event.getNota());
    }

    @UiHandler("chiusureChiusure")
    void onSelezioneChiusura(SelezioneChiusuraEvent event) {
        GWT.log("ChiusureView.onSelezioneChiusura: catch SelezioneChiusuraEvent");
        listener.caricaEstrattiConto(event.getChiusuraProxy());
    }

    @UiHandler("chiusureChiusure")
    void onDownloadChiusure(DownloadEvent event) {
        GWT.log("ChiusureView.onDownloadChiusure: catch DownloadEvent");
        listener.downloadChiusure();
    }

    @Override
    public void initChiusureChiusure(ChiusuraLioProxy chiusuraLioProxy) {
        chiusureChiusure.init(chiusuraLioProxy);
    }

    @Override
    public HasData<ChiusuraLioProxy> getTabellaChiusure() {
        return chiusureChiusure.getTabella();
    }

    /******* ESTRATTI CONTO ********/
    @UiHandler("chiusureEstrattiConto")
    void onDownloadEstrattoConto(DownloadEvent event) {
        GWT.log("ChiusureView.onDownloadEstrattoConto: catch DownloadEvent");
        listener.downloadEstrattoConto();
    }

    @UiHandler("chiusureEstrattiConto")
    void onBackEstrattoConto(BackEvent event) {
        GWT.log("ChiusureView.onBackEstrattoConto: catch BackEvent");
        hideAll();
        chiusureChiusure.setVisible(true);
    }

    @UiHandler("chiusureEstrattiConto")
    void onSelezioneEstrattoConto(SelezioneEstrattoContoEvent event) {
        GWT.log("ChiusureView.onSelezioneEstrattoConto: catch SelezioneEstrattoContoEvent");
        listener.caricaTitoli(event.getEstrattoContoLioProxySelezionato());
    }

    @Override
    public HasData<EstrattoContoLioProxy> getTabellaEstrattiConto() {
        return chiusureEstrattiConto.getTabella();
    }

    @Override
    public void visualizzaEstrattiConto(ChiusuraLioProxy chiusuraProxy) {
        chiusureEstrattiConto.init(chiusuraProxy);
        hideAll();
        chiusureEstrattiConto.setVisible(true);
    }

    /******* TITOLI ********/
    @UiHandler("chiusureTitoli")
    public void onDownloadTitoli(DownloadEvent event) {
        GWT.log("ChiusureView.onDownload: catch DownloadEvent");
        listener.downloadTitoli();
    }

    @UiHandler("chiusureTitoli")
    void onBackTitoli(BackEvent event) {
        GWT.log("ChiusureView.onBackTitoli: catch BackEvent");
        hideAll();
        chiusureEstrattiConto.setVisible(true);
    }

    @UiHandler("chiusureTitoli")
    void onSelezioneTitolo(SelezioneTitoloEvent event) {
        GWT.log("ChiusureView.onSelezioneTitolo: catch SelezioneTitoloEvent");
        GWT.log("ChiusureView.onSelezioneTitolo: incassi size "+event.getTitoloProxy().getIncassiOrderByDataInserimentoDesc().size());
        listener.caricaDettaglioTitolo(event.getTitoloProxy());
    }

    @Override
    public HasData<NewTitoloProxy> getTabellaTitoli() {
        return chiusureTitoli.getTabella();
    }

    @Override
    public void visualizzaTitoli(EstrattoContoLioProxy estrattoContoLioProxySelezionato, String meseChiusura) {
        chiusureTitoli.init(estrattoContoLioProxySelezionato, meseChiusura);
        hideAll();
        chiusureTitoli.setVisible(true);
    }

    /******* DETTAGLIO TITOLO ********/
    @UiHandler("chiusureDettaglioTitolo")
    public void onDownloadDettaglioTitolo(DownloadEvent event) {
        GWT.log("ChiusureView.onDownloadDettaglioTitolo: catch DownloadEvent");
        listener.downloadDettaglioTitolo();
    }

    @UiHandler("chiusureDettaglioTitolo")
    void onBackDettaglioTitolo(BackEvent event) {
        GWT.log("ChiusureView.onBackDettaglioTitolo: catch BackEvent");
        hideAll();
        chiusureTitoli.setVisible(true);
    }

    @Override
    public HasData<IncassoTitoloProxy> getTabellaIncassi() {
        return chiusureDettaglioTitolo.getTabellaIncassi();
    }

    @Override
    public void visualizzaDettaglio(NewTitoloProxy titoloProxy, String meseChiusura,
            EstrattoContoLioProxy estrattoContoLioProxySelezionato) {
        hideAll();
        chiusureDettaglioTitolo.init(titoloProxy, meseChiusura, estrattoContoLioProxySelezionato);
        chiusureDettaglioTitolo.setVisible(true);
    }

}
