package com.kp.malice.client.tabGiornata;

import java.util.Date;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.EntityProxyChange;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.kp.malice.client.event.TitoloUpdatedEvent;
import com.kp.malice.client.place.AgenteRmaPlace;
import com.kp.malice.client.ui.UIMaliceUtil;
import com.kp.malice.shared.MaliceRequestFactory.ServiziPortale;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.proxies.DettaglioIncassoTitoloProxy;
import com.kp.malice.shared.proxies.IncassoTitoloProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;
import com.kp.malice.shared.proxies.ScritturaContabileProxy;

public class GiornataActivity extends AbstractActivity implements IGiornataViewDisplay.Listener,
        TitoloUpdatedEvent.Handler {

    private final IGiornataViewDisplay display;
    private EventBus eventBus;
    private final Provider<ServiziPortale> serviziProvider;
    private AgenteRmaPlace place;
    private ListDataProvider<ScritturaContabileProxy> scritturaContabileDataProvider = new ListDataProvider<ScritturaContabileProxy>();
    private ListDataProvider<DettaglioIncassoTitoloProxy> incassiAggregatiDataProvider = new ListDataProvider<DettaglioIncassoTitoloProxy>();
    private Date dataScrittureContabiliInVisualizzazione;
    private ListHandler<ScritturaContabileProxy> sortHandler;

    private final String[] propsForDettaglio = new String[] { "certificatoLloyds", "funzioniAbilitate",
            "ultimoIncassoCheHaMessoIlTitoloInStatoIncassato", "filieraLloyds", "incassiOrderByDataInserimentoDesc" };

    @Inject
    public GiornataActivity(IGiornataViewDisplay display, EventBus eventBus, Provider<ServiziPortale> serviziProvider) {
        this.display = display;
        this.eventBus = eventBus;
        this.serviziProvider = serviziProvider;
        eventBus.addHandler(TitoloUpdatedEvent.TYPE, this);
        dataScrittureContabiliInVisualizzazione = new Date();
        EntityProxyChange.registerForProxyType(eventBus, NewTitoloProxy.class,
                new EntityProxyChange.Handler<NewTitoloProxy>() {
                    public void onProxyChange(EntityProxyChange<NewTitoloProxy> event) {
                        //                        GWT.log("INCASSI proxy id:" + event.getProxyId() + " has been "
                        //                                + event.getWriteOperation().name());
                    }
                });
    }

    public void start(final AcceptsOneWidget panel, EventBus eventBus) {
        display.setListener(this);
        scritturaContabileDataProvider.addDataDisplay(display.getTabellaScritturaContabile());
        incassiAggregatiDataProvider.addDataDisplay(display.getTabellaIncassiAggregati());
        sortHandler = new ListHandler<ScritturaContabileProxy>(scritturaContabileDataProvider.getList());
        loadScritturaContabileByDay(new Date());
        display.setComparatorsAndSortHandler(sortHandler);
        panel.setWidget(display.asWidget());
    }

    public GiornataActivity withPlace(final Place place) {
        this.place = (AgenteRmaPlace) place;
        return this;
    }

    @Override
    public void loadScritturaContabileByDay(Date date) {
        GWT.log("IncassiActivity.loadChiusuraContabileByDay: date=" + date);
        dataScrittureContabiliInVisualizzazione = date;
        serviziProvider.get().findListByDataRegistrazioneIncasso(date)
                .fire(new Receiver<List<ScritturaContabileProxy>>() {
                    @Override
                    public void onSuccess(List<ScritturaContabileProxy> scritturaContabileProxyList) {
                        GWT.log("CARICAMENTO SCRITTURA CONTABILE OK");
                        if (scritturaContabileProxyList.isEmpty())
                            display.visualizzaMsgNoIncassi();
                        else {
                            scritturaContabileDataProvider.getList().clear();
                            scritturaContabileDataProvider.getList().addAll(scritturaContabileProxyList);
                            display.visualizzaListaScrittureContabili();
                        }
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        GWT.log("CARICAMENTO SCRITTURA CONTABILE FAIL:" + error.getStackTraceString());
                        display.visualizzaErroreOnScritturaContabileWidget("Errore durante l'operazione di caricamento.");
                    }
                });
    }

    @Override
    public void loadIncassiAggregati(final ScritturaContabileProxy scritturaContabileProxy) {
        GWT.log("IncassiActivity.loadIncassiAggregati");
        serviziProvider.get().findListIncassiAggregati(scritturaContabileProxy).with(propsForDettaglio).fire(new Receiver<List<DettaglioIncassoTitoloProxy>>() {
                    @Override
                    public void onSuccess(List<DettaglioIncassoTitoloProxy> dettaglioIncassiTitoloProxy) {
                        GWT.log("CARICAMENTO INCASSI AGGREGATI OK");
                        incassiAggregatiDataProvider.getList().clear();
                        GWT.log("primo titolo id: " + dettaglioIncassiTitoloProxy.get(0).getIdentificativoContraente());
                        incassiAggregatiDataProvider.getList().addAll(dettaglioIncassiTitoloProxy);
                        display.visualizeIncassiAggregati(scritturaContabileProxy);
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        GWT.log("CARICAMENTO INCASSI AGGREGATI FAIL:" + error.getStackTraceString());
                        display.visualizzaErroreOnScritturaContabileWidget("Errore durante l'operazione di caricamento.");
                    }
                });
    }

    @Override
    public void downloadScrittureContabili(Date date) {
        GWT.log("IncassiActivity.downloadScrittureContabili");
        StringBuilder parametriDiScarico = new StringBuilder();
        parametriDiScarico.append(MaliceUtil.getStringDateFormatted(date));
        DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy.MM.dd");
        parametriDiScarico.append(",Scritture Contabili del Giorno " + fmt.format(date) + ".xls");
        parametriDiScarico.append(",2");
        if (parametriDiScarico != null)
            Window.open(GWT.getModuleBaseURL() + "download?data=" + parametriDiScarico, "Scarico_Incasso", "");
    }

    @Override
    public void onTitoloUpdated(TitoloUpdatedEvent event) {
        if (UIMaliceUtil.isSameDayDate(new Date(), dataScrittureContabiliInVisualizzazione)) {
            loadScritturaContabileByDay(dataScrittureContabiliInVisualizzazione);
        }
    }

    @Override
    public void caricaTitoloDettaglio(IncassoTitoloProxy titoloProxy) {
        GWT.log("IncassiActivity.caricaTitoloDettaglio: IncassoTitoloProxy.id=");
        //        serviziProvider.get().findTitolo(titoloProxy.getId()).with(propsForDettaglio)
        //                .fire(new Receiver<NewTitoloProxy>() {
        //                    @Override
        //                    public void onSuccess(NewTitoloProxy updatedtitoloProxy) {
        //                        GWT.log("CARICAMENTO TITOLO OK: " + updatedtitoloProxy.getCertificatoLloyds().getNumero());
        //                        incassiAggregatiDataProvider.getList().clear();
        //                        incassiAggregatiDataProvider.setList(updatedtitoloProxy.getIncassiOrderByDataInserimentoDesc());
        //                        display.visualizzaDettaglio(updatedtitoloProxy);
        //                    }
        //
        //                    @Override
        //                    public void onFailure(ServerFailure error) {
        //                        GWT.log("CARICAMENTO TITOLO FAIL:" + error.getStackTraceString());
        //                        MaliceUtil.showError("Errore durante l'operazione di caricamento Titoli. Riprovare piu' tardi");
        //                    }
        //                });
    }
}