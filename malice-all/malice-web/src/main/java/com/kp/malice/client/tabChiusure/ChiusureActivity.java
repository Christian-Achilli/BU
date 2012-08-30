package com.kp.malice.client.tabChiusure;

import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.kp.malice.client.event.ChiusuraInviataEvent;
import com.kp.malice.client.event.TitoloUpdatedEvent;
import com.kp.malice.client.place.UserPlace;
import com.kp.malice.shared.MaliceRequestFactory.ServiziPortale;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.proxies.ChiusuraLioProxy;
import com.kp.malice.shared.proxies.EstrattoContoLioProxy;
import com.kp.malice.shared.proxies.IncassoTitoloProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class ChiusureActivity extends AbstractActivity implements IChiusureViewDisplay.Listener,
        TitoloUpdatedEvent.Handler {

    private ListDataProvider<ChiusuraLioProxy> chiusureDataProvider = new ListDataProvider<ChiusuraLioProxy>();

    private ListDataProvider<EstrattoContoLioProxy> estrattiContoDataProvider = new ListDataProvider<EstrattoContoLioProxy>();

    private ListDataProvider<NewTitoloProxy> titoliDataProvider = new ListDataProvider<NewTitoloProxy>();

    private ListDataProvider<IncassoTitoloProxy> incassiDataProvider = new ListDataProvider<IncassoTitoloProxy>();

    private ChiusuraLioProxy chiusuraProxySelezionata;

    private EstrattoContoLioProxy estrattoContoSelezionato;

    private NewTitoloProxy titoloProxySelezionato;

    private final IChiusureViewDisplay display;

    private final EventBus eventBus;

    private final Provider<ServiziPortale> contextProvider;

    private final String[] propsForDettaglio = new String[] { "certificatoLloyds", "funzioniAbilitate",
            "ultimoIncassoCheHaMessoIlTitoloInStatoIncassato", "filieraLloyds", "incassiOrderByDataInserimentoDesc",
            "contraente" };

    @Inject
    public ChiusureActivity(IChiusureViewDisplay display, EventBus eventBus, Provider<ServiziPortale> contextProvider) {
        this.display = display;
        this.eventBus = eventBus;
        this.contextProvider = contextProvider;
        eventBus.addHandler(TitoloUpdatedEvent.TYPE, this);
    }

    public void start(final AcceptsOneWidget panel, EventBus eventBus) {
        display.setListener(this);
        caricaChiusure();
        chiusureDataProvider.addDataDisplay(display.getTabellaChiusure());
        estrattiContoDataProvider.addDataDisplay(display.getTabellaEstrattiConto());
        titoliDataProvider.addDataDisplay(display.getTabellaTitoli());
        incassiDataProvider.addDataDisplay(display.getTabellaIncassi());
        panel.setWidget(display.asWidget());
    }

    public ChiusureActivity withPlace(final UserPlace place) {
        return this;
    }

    private void caricaChiusure() {
        contextProvider.get().getListaChiusure().fire(new Receiver<List<ChiusuraLioProxy>>() {
            @Override
            public void onSuccess(List<ChiusuraLioProxy> chiusuraProxyList) {
                GWT.log("CARICAMENTO CHIUSURE OK: trovate " + chiusuraProxyList.size() + " chiusure");
                chiusureDataProvider.getList().clear();
                chiusureDataProvider.getList().addAll(chiusuraProxyList);
                for (ChiusuraLioProxy chiusuraLioProxy : chiusuraProxyList) {
                    if (chiusuraLioProxy.isNextToBeSent()) {
                        display.initChiusureChiusure(chiusuraLioProxy);
                    }
                }
            }

            @Override
            public void onFailure(ServerFailure error) {
                GWT.log("CARICAMENTO CHIUSURE FAIL:" + error.getStackTraceString());
                MaliceUtil.showError("Errore durante l'operazione di caricamento delle chiusure.");
            }
        });
    }

    @Override
    public void chiudiMese(String nota) {
        GWT.log("ChiusureActivity.chiudiMese: nota --> " + nota);

        contextProvider.get().inviaChiusura(nota).fire(new Receiver<Void>() {

            @Override
            public void onSuccess(Void response) {
                caricaChiusure();
                // ricaricare i tioli per mostrare i consolidati
                eventBus.fireEvent(new ChiusuraInviataEvent());
                MaliceUtil.showSuccess();
            }

            @Override
            public void onFailure(ServerFailure error) {
                MaliceUtil.showError("Errore durante l'operazione di Chiusura Mese. Riprovare piu' tardi");
            }

        });
    }

    @Override
    public void caricaEstrattiConto(final ChiusuraLioProxy chiusuraProxy) {
        GWT.log("ChiusureActivity.caricaEstrattiConto: chiusuraProxy..getLabel() --> " + chiusuraProxy.getLabel());
        contextProvider.get().getListaEstrattiConto(chiusuraProxy).fire(new Receiver<List<EstrattoContoLioProxy>>() {
            @Override
            public void onSuccess(List<EstrattoContoLioProxy> estrattoContoLioProxyList) {
                GWT.log("CARICAMENTO ESTRATTI CONTO OK");
                estrattiContoDataProvider.getList().clear();
                estrattiContoDataProvider.getList().addAll(estrattoContoLioProxyList);
                display.visualizzaEstrattiConto(chiusuraProxy);
                chiusuraProxySelezionata = chiusuraProxy;
            }

            @Override
            public void onFailure(ServerFailure error) {
                GWT.log("CARICAMENTO ESTRATTI CONTO FAIL:" + error.getStackTraceString());
                MaliceUtil.showError("Errore durante l'operazione di recupero Estratti Conto. Riprovare piu' tardi");
            }
        });
    }

    @Override
    public void caricaTitoli(final EstrattoContoLioProxy estrattoContoLioProxySelezionato) {
        GWT.log("ChiusureActivity.caricaTitoli: estrattoContoLioProxySelezionato.getLabel() --> "
                + estrattoContoLioProxySelezionato.getLabel());
        contextProvider.get().getListaTitoliInEstrattoConto(estrattoContoLioProxySelezionato).with(propsForDettaglio)
                .fire(new Receiver<List<NewTitoloProxy>>() {
                    @Override
                    public void onSuccess(List<NewTitoloProxy> titoliProxyList) {
                        GWT.log("CARICAMENTO TITOLI OK");
                        estrattoContoSelezionato = estrattoContoLioProxySelezionato;
                        titoliDataProvider.getList().clear();
                        titoliDataProvider.getList().addAll(titoliProxyList);
                        display.visualizzaTitoli(estrattoContoLioProxySelezionato, chiusuraProxySelezionata.getLabel());
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        GWT.log("CARICAMENTO TITOLI FAIL:" + error.getStackTraceString());
                        MaliceUtil.showError("Errore durante l'operazione di recupero Titoli. Riprovare piu' tardi");
                    }
                });
    }

    @Override
    public void downloadChiusure() {
        GWT.log("ChiusureActivity.downloadChiusure");
        // TODO livio
    }

    @Override
    public void downloadEstrattoConto() {
        GWT.log("ChiusureActivity.downloadEstrattoConto");
        StringBuilder stringToGet = new StringBuilder();
        stringToGet.append(chiusuraProxySelezionata.getId() + ",");
        stringToGet.append("Ch_" + chiusuraProxySelezionata.getLabel() + "-"
                + chiusuraProxySelezionata.getStatoChiusuraString() + " - Sintesi Estratti Conto.xls");
        stringToGet.append(",3"); // Identificatore del tipo di download
        MaliceUtil.downloadListaTitoli(stringToGet.toString());
    }

    @Override
    public void downloadTitoli() {
        GWT.log("ChiusureActivity.downloadTitoli");
        StringBuilder stringToGet = new StringBuilder();
        for (NewTitoloProxy titoloFiltrato : titoliDataProvider.getList()) {
            stringToGet.append(titoloFiltrato.getId() + ",");
        }
        stringToGet.append("Ch_" + chiusuraProxySelezionata.getLabel() + "-"
                + chiusuraProxySelezionata.getStatoChiusuraString() + "-EC_" + estrattoContoSelezionato.getLabel()
                + " - Lista Titoli.xls");
        stringToGet.append(",1"); // Identificatore del tipo di download
        MaliceUtil.downloadListaTitoli(stringToGet.toString());
    }

    @Override
    public void downloadDettaglioTitolo() {
        GWT.log("ChiusureActivity.downloadDettaglioTitolo: titoloProxySelezionato.id=" + titoloProxySelezionato.getId());
        // TODO livio (usa titoloProxySelezionato)
    }

    @Override
    public void onTitoloUpdated(TitoloUpdatedEvent event) {
        caricaChiusure();
    }

    @Override
    public void caricaDettaglioTitolo(NewTitoloProxy titoloProxy) {
        GWT.log("ChiusureActivity.caricaDettaglioTitolo: incassi size "
                + titoloProxy.getIncassiOrderByDataInserimentoDesc().size());
        titoloProxySelezionato = titoloProxy;
        incassiDataProvider.setList(titoloProxy.getIncassiOrderByDataInserimentoDesc());
        display.visualizzaDettaglio(titoloProxy, chiusuraProxySelezionata.getLabel(), estrattoContoSelezionato);
    }

}