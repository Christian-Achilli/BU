package com.kp.malice.client.tabTitoli;

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
import com.kp.malice.client.ui.gwtEvent.ConfermaNotaEvent;
import com.kp.malice.client.ui.gwtEvent.CreaTitoloEvent;
import com.kp.malice.client.ui.gwtEvent.DoppioCalendarioValueChangeEvent;
import com.kp.malice.client.ui.gwtEvent.DownloadEvent;
import com.kp.malice.client.ui.gwtEvent.FiltraTitoliEvent;
import com.kp.malice.client.ui.gwtEvent.IncassoEvent;
import com.kp.malice.client.ui.gwtEvent.InserisciTitoloEvent;
import com.kp.malice.client.ui.gwtEvent.RecuperoTitoloEvent;
import com.kp.malice.client.ui.gwtEvent.RevocaAnnulloEvent;
import com.kp.malice.client.ui.gwtEvent.SaveDettaglioTitoloEvent;
import com.kp.malice.client.ui.gwtEvent.SelezioneTitoloEvent;
import com.kp.malice.client.ui.gwtEvent.StornoEvent;
import com.kp.malice.client.ui.gwtEvent.VisualizeInsertTitleFormEvent;
import com.kp.malice.client.ui.gwtEvent.VisualizzaListaTitoliEvent;
import com.kp.malice.shared.proxies.IncassoTitoloProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class TitoliView extends Composite implements ITitoliViewDisplay {

    interface ListaTitoliViewUiBinder extends UiBinder<Widget, TitoliView> {
    }

    private static ListaTitoliViewUiBinder uiBinder = GWT.create(ListaTitoliViewUiBinder.class);

    private ITitoliViewDisplay.Listener listener;

    @UiField
    TitoliTitoli titoliTitoli;

    @UiField
    TitoliDettaglio titoliDettaglio;

    @UiField
    TitoliInserimento titoliInserimento;

    @Inject
    public TitoliView() {
        initWidget(uiBinder.createAndBindUi(this));
        setWidgetsInvisible();
        titoliTitoli.setVisible(true);
    }

    /*** DETTAGLIO TITOLI WIDGET ***/
    @UiHandler("titoliDettaglio")
    void onConfermaNota(ConfermaNotaEvent e) {
        GWT.log("TitoliView.onConfermaNota: catch ConfermaNotaEventÀ");
        listener.annullaTitolo(e.getNota());
    }

    @UiHandler("titoliDettaglio")
    public void onStornoTitolo(StornoEvent event) {
        GWT.log("TitoliView.onStornoTitolo: catch StornoEvent ");
        listener.stornaTitolo();
    }

    @UiHandler("titoliDettaglio")
    public void onRecuperoTitolo(RecuperoTitoloEvent event) {
        GWT.log("TitoliView.onRecuperoTitolo: catch RecuperoTitoloEvent");
        listener.recuperaTitoloSospeso(event.getDatatIncasso(), event.getImportoIncasso().movePointRight(2),
                event.getMezzoPagamento());
    }

    @UiHandler("titoliDettaglio")
    public void onRevocaAnnullo(RevocaAnnulloEvent event) {
        GWT.log("TitoliView.onRevocaAnnullo: catch RevocaAnnulloEvent");
        listener.revocaAnnulloTitolo();
    }

    @UiHandler("titoliDettaglio")
    public void onIncassoTitolo(IncassoEvent event) {
        GWT.log("TitoliView.onIncassoTitolo: catch IncassoEvent");
        listener.incassaTitolo(event.getImportoIncasso().movePointRight(2), event.getTipoIncasso(),
                event.getMezzoPagamento(), event.getData());
    }

    @UiHandler("titoliDettaglio")
    public void onBackFromDettaglio(final BackEvent event) {
        visualizzaListaTitoli();
    }

    @UiHandler("titoliDettaglio")
    public void onSaveDettaglioTitolo(SaveDettaglioTitoloEvent event) {
        GWT.log("TitoliView.onSaveDettaglioTitolo: catch SaveDettaglioTitoloEvent");
        if (event.isStillLive()) { //l'evento viene killato se i campi del dettaglio editabili non passano la validazione
            GWT.log("TitoliView onSaveDettaglioTitolo: event still live");
            listener.saveDettaglioTitolo(event.getSubAgente(), event.getCodCig());
        }
    }

    @Override
    public void visualizzaDettaglio(NewTitoloProxy titoloProxy) {
        mostraDettaglioTitolo();
        titoliDettaglio.init(titoloProxy);
    }

    private void mostraDettaglioTitolo() {
        setWidgetsInvisible();
        titoliDettaglio.setVisible(true);
    }

    @Override
    public HasData<IncassoTitoloProxy> getTabellaIncassi() {
        return titoliDettaglio.getTabellaIncassi();
    }

    @Override
    public void populateDettaglioTitolo(NewTitoloProxy newTitoloProxy) {
        titoliDettaglio.init(newTitoloProxy);
    }

    /*** LISTA TITOLI WIDGET ***/
    @UiHandler("titoliTitoli")
    public void onCambioRangeDateRicercaTitoli(final DoppioCalendarioValueChangeEvent event) {
        GWT.log("onCambioRangeDateRicercaTitoli start: " + event.getStartDate());
        GWT.log("onCambioRangeDateRicercaTitoli end: " + event.getEndDate());
        listener.ricaricaListaTitoli(event.getStartDate(), event.getEndDate());
    }

    @UiHandler("titoliTitoli")
    public void onDownloadListaTitoli(final DownloadEvent event) {
        GWT.log("TitoliView.onDownloadListaTitoli: catch DownloadListaTitoliEvent");
        listener.downloadListaTitoli();
    }

    @UiHandler("titoliTitoli")
    public void onCreaTitoloEvent(final CreaTitoloEvent event) {
        GWT.log("TitoliView.onCreaTitoloEvent: catch CreaTitoloEvent");
        setWidgetsInvisible();
        titoliInserimento.setVisible(true);
    }

    @UiHandler("titoliTitoli")
    public void onFiltraTitoli(final FiltraTitoliEvent event) {
        GWT.log("TitoliView.onFiltraTitoli: catch FiltraTitoliEvent");
        listener.filtraTitoli(event.getChiave(), event.getListaStatusTitolo(), event.getSospesi());
    }

    @UiHandler("titoliTitoli")
    public void onSelezioneTitolo(final SelezioneTitoloEvent event) {
        GWT.log("onSelezioneTitolo");
        listener.onSelezioneTitolo(event.getTitoloProxy());
    }

    @UiHandler("titoliTitoli")
    public void onVisualizzaInserimentoTitoloForm(final VisualizeInsertTitleFormEvent event) {
        GWT.log("onVisualizzaInserimentoTitoloForm");
        setWidgetsInvisible();
        titoliInserimento.setVisible(true);
    }

    private void visualizzaListaTitoli() {
        GWT.log("onBack");
        setWidgetsInvisible();
        titoliTitoli.setVisible(true);
    }

    @Override
    public void setTitoliTrovati(int size) {
        titoliTitoli.setTitoliTrovati(size);
    }

    @Override
    public HasData<NewTitoloProxy> getTabellaTitoli() {
        return titoliTitoli.getTabella();
    }

    @Override
    public void setComparatorsAndSortHandler(ListHandler<NewTitoloProxy> sortHandler) {
        titoliTitoli.setComparatorsAndSortHandler(sortHandler);
    }

    @Override
    public void resetFiltri() {
        titoliTitoli.resetFiltri();
    }

    /*** INSERIMENTO TITOLI WIDGET ***/
    @UiHandler("titoliInserimento")
    public void onCreaTitolo(final CreaTitoloEvent event) {
        GWT.log("TitoloView: onCreaTitolo");
        listener.onCreateNewTitolo();
    }

    @UiHandler("titoliInserimento")
    public void onVisualizzaListaTitoliFromInserimentoForm(VisualizzaListaTitoliEvent event) {
        GWT.log("TitoloView: onVisualizzaListaTitoliFromInserimentoForm");
        visualizzaListaTitoli();
    }

    @UiHandler("titoliInserimento")
    public void onInserisciTitoloFromInserimento(InserisciTitoloEvent event) {
        GWT.log("TitoloView: onInserisciTitoloFromInserimento");
    }

    private void setWidgetsInvisible() {
        titoliDettaglio.setVisible(false);
        titoliInserimento.setVisible(false);
        titoliTitoli.setVisible(false);
    }

    @Override
    public void setListener(ITitoliViewDisplay.Listener listener) {
        this.listener = listener;
    }
}