package com.kp.malice.client.tabIncassi;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.kp.malice.client.ui.gwtEvent.BackEvent;
import com.kp.malice.client.ui.gwtEvent.CalendarioValueChangeEvent;
import com.kp.malice.client.ui.gwtEvent.DownloadEvent;
import com.kp.malice.client.ui.gwtEvent.SelezioneIncassoEvent;
import com.kp.malice.client.ui.gwtEvent.SelezioneScritturaContabileEvent;
import com.kp.malice.client.ui.widget.ScritturaContabileWidget;
import com.kp.malice.shared.proxies.DettaglioIncassoTitoloProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;
import com.kp.malice.shared.proxies.ScritturaContabileProxy;

public class IncassiView extends Composite implements IIncassiViewDisplay {

    private static IncassiViewUiBinder uiBinder = GWT.create(IncassiViewUiBinder.class);

    interface IncassiViewUiBinder extends UiBinder<Widget, IncassiView> {
    }

    private IIncassiViewDisplay.Listener listener;

    @UiField
    ScritturaContabileWidget scritturaContabile;
    @UiField
    IncassiAggregatiWidget incassiAggregati;

    @Inject
    public IncassiView() {
        initWidget(uiBinder.createAndBindUi(this));
        setWidgetsInvisible();
        scritturaContabile.setVisible(true);
    }

    @UiHandler("scritturaContabile")
    void onSelezioneScirtturaContabile(SelezioneScritturaContabileEvent event) {
        GWT.log("IncassiView.onSelezioneScirtturaContabile: catch SelezioneScritturaContabileEvent");
        listener.loadIncassiAggregati(event.getScritturaContabileProxy());
    }

    @UiHandler("scritturaContabile")
    void onCalendarioValueChange(CalendarioValueChangeEvent event) {
        GWT.log("IncassiView.onCalendarioValueChange: catch CalendarioValueChangeEvent");
        Date date = event.getDate();
        listener.loadScritturaContabileByDay(date);
    }

    @UiHandler("scritturaContabile")
    void onDownloadScritturaContabile(DownloadEvent event) {
        GWT.log("IncassiView.onDownloadScritturaContabile: catch DownloadEvent");
        listener.downloadScrittureContabili(event.getDate());
    }

    @UiHandler("incassiAggregati")
    void onBackEvent(BackEvent event) {
        GWT.log("IncassiView.onBackEvent: catch BackEvent");
        visualizzaScritturaContabileWidget();
    }

    @UiHandler("incassiAggregati")
    void onSelezioneIncassoEvent(SelezioneIncassoEvent event) {
        GWT.log("IncassiView.onSelezioneIncassoEvent: catch SelezioneIncassoEvent");
        listener.caricaTitoloDettaglio(event.getIncassoTitoloProxy());
    }

    @Override
    public void setComparatorsAndSortHandler(ListHandler<ScritturaContabileProxy> sortHandler) {
        scritturaContabile.setComparatorsAndSortHandler(sortHandler);
    }

    public void visualizzaIncassiAggregatiWidget() {
        setWidgetsInvisible();
        incassiAggregati.setVisible(true);
    }

    @Override
    public void visualizzaErroreOnScritturaContabileWidget(String error) {
        scritturaContabile.visualizzaErrore(error);
    }

    public void visualizzaScritturaContabileWidget() {
        setWidgetsInvisible();
        scritturaContabile.setVisible(true);
    }

    private void setWidgetsInvisible() {
        scritturaContabile.setVisible(false);
        incassiAggregati.setVisible(false);
    }

    public IIncassiViewDisplay.Listener getListener() {
        return listener;
    }

    @Override
    public void setListener(IIncassiViewDisplay.Listener listener) {
        this.listener = listener;
    }

    @Override
    public HasData<ScritturaContabileProxy> getTabellaScritturaContabile() {
        return scritturaContabile.getTabella();
    }

    @Override
    public HasData<DettaglioIncassoTitoloProxy> getTabellaIncassiAggregati() {
        return incassiAggregati.getTabella();
    }

    @Override
    public void visualizeIncassiAggregati(ScritturaContabileProxy scritturaContabileProxy) {
        incassiAggregati.populate(scritturaContabileProxy);
        setWidgetsInvisible();
        incassiAggregati.setVisible(true);
    }

    @Override
    public void visualizzaMsgNoIncassi() {
        scritturaContabile.visualizzaMsgNoIncassi();
    }

    @Override
    public void visualizzaListaScrittureContabili() {
        scritturaContabile.visualizzaListaScrittureContabili();
    }

    @Override
    public void visualizzaDettaglio(NewTitoloProxy updatedtitoloProxy) {
        // TODO Auto-generated method stub

    }
}
