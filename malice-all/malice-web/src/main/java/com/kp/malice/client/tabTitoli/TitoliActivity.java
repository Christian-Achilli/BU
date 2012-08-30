package com.kp.malice.client.tabTitoli;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.EntityProxyChange;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.kp.malice.client.RFErrorHandler;
import com.kp.malice.client.event.ChiusuraInviataEvent;
import com.kp.malice.client.event.TitoloUpdatedEvent;
import com.kp.malice.client.place.AgenteRmaPlace;
import com.kp.malice.shared.Gender;
import com.kp.malice.shared.MaliceRequestFactory.ServiziPortale;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.MezzoPagamento;
import com.kp.malice.shared.StatoIncasso;
import com.kp.malice.shared.StatusTitolo;
import com.kp.malice.shared.TipoOperazione;
import com.kp.malice.shared.proxies.ContraenteProxy;
import com.kp.malice.shared.proxies.DettaglioIncassoTitoloProxy;
import com.kp.malice.shared.proxies.IncassoTitoloProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class TitoliActivity extends AbstractActivity implements ITitoliViewDisplay.Listener,
        ChiusuraInviataEvent.Handler {

    private final ITitoliViewDisplay display;
    private final EventBus eventBus;
    private final Provider<ServiziPortale> serviziTitoloProvider;
    private boolean isAppBootstrapping = true;
    private Date startDate;
    private Date endDate;
    private List<NewTitoloProxy> listaTitoliFiltrati; // è la lista dei titoli
                                                      // filtrati
    private List<NewTitoloProxy> backUpList = new ArrayList<NewTitoloProxy>();
    private ListDataProvider<NewTitoloProxy> titoliDataProvider = new ListDataProvider<NewTitoloProxy>();
    private ListHandler<NewTitoloProxy> sortHandler;
    private AgenteRmaPlace place;
    protected NewTitoloProxy titoloSelezionatoInDettaglio;
    private ListDataProvider<IncassoTitoloProxy> incassiDataProvider = new ListDataProvider<IncassoTitoloProxy>();

    @Inject
    public TitoliActivity(ITitoliViewDisplay display, EventBus eventBus, Provider<ServiziPortale> serviziTitoloProvider) {
        this.display = display;
        this.eventBus = eventBus;
        this.serviziTitoloProvider = serviziTitoloProvider;
        display.setListener(this);
        eventBus.addHandler(ChiusuraInviataEvent.TYPE, this);
        titoliDataProvider.addDataDisplay(display.getTabellaTitoli());
        EntityProxyChange.registerForProxyType(eventBus, NewTitoloProxy.class,
                new EntityProxyChange.Handler<NewTitoloProxy>() {
                    public void onProxyChange(EntityProxyChange<NewTitoloProxy> event) {
                        // GWT.log("proxy id:" + event.getProxyId() + " has been " +
                        // event.getWriteOperation().name());
                    }
                });
        incassiDataProvider.addDataDisplay(display.getTabellaIncassi());
    }

    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        startDate = MaliceUtil.getTodayPlus(-20);
        endDate = MaliceUtil.getTodayPlus(10);
        listaTitoliFiltrati = titoliDataProvider.getList();
        sortHandler = new ListHandler<NewTitoloProxy>(listaTitoliFiltrati);
        display.setComparatorsAndSortHandler(sortHandler);
        nuovaCaricaTitoli();
        panel.setWidget(display.asWidget());
    }

    public TitoliActivity withPlace(final Place place) {
        this.place = (AgenteRmaPlace) place;
        return this;
    }

    private void nuovaCaricaTitoli() {
        GWT.log("TitoliActivity.nuovaCaricaTitoli");

        ServiziPortale request = serviziTitoloProvider.get();
        Request<List<NewTitoloProxy>> req = request.findAllTitoliInPeriodoInizioCopertura(startDate, endDate).with(
                propsForDettaglio);
        try {
            req.fire(new Receiver<List<NewTitoloProxy>>() {
                @Override
                public void onSuccess(List<NewTitoloProxy> listaTitoli) {
                    GWT.log("success RequestFactory retrieved " + listaTitoli.size() + " newTitoloProxy");
                    display.setTitoliTrovati(listaTitoli.size());
                    display.resetFiltri();
                    if (null != backUpList)
                        backUpList.clear();
                    backUpList = listaTitoli;
                    if (null != listaTitoliFiltrati)
                        listaTitoliFiltrati.clear();
                    listaTitoliFiltrati.addAll(backUpList);
                    if (isAppBootstrapping) {
                        isAppBootstrapping = false;
                    }

                }

                @Override
                public void onFailure(ServerFailure error) {
                    GWT.log("caricaTitoli failure!");
                    GWT.log(error.getExceptionType());

                    RFErrorHandler.handle(error);
                }
            });
        } catch (Exception e) {
            GWT.log("requestFactory.chiusureRequest().chiudiMese() GwtSecurityException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /************ LISTA *************/
    @Override
    public void ricaricaListaTitoli(Date startDate, Date endDate) {
        GWT.log("TitoliActivity.ricaricaListaTitoli: titoli dal " + startDate + " al " + endDate);
        // se le date sono null sono nel caso di chiusura effettuata e voglio
        // solo ricaricare la lista con le precedenti date
        if (null != startDate)
            this.startDate = startDate;
        if (null != endDate)
            this.endDate = endDate;
        nuovaCaricaTitoli();
    }

    private boolean isUltimoIncassoSospeso(NewTitoloProxy titoloProxy) {
        int size = titoloProxy.getIncassiOrderByDataInserimentoDesc().size();
        if (size <= 0)
            return false;
        else if (StatoIncasso.fromString(titoloProxy.getIncassiOrderByDataInserimentoDesc().get(0).getStatoIncasso()) == StatoIncasso.SOSPESO)
            return true;
        else
            return false;
    }

    @Override
    public void filtraTitoli(String key, List<StatusTitolo> statoTitolo, Boolean sospesi) {
        GWT.log("TitoliActivity.filtraTitoli per: " + key);
        listaTitoliFiltrati.clear();
        for (NewTitoloProxy titoloProxy : backUpList) {
            if (statoTitolo.contains(StatusTitolo.fromString(titoloProxy.getStringStatoTitolo()))) {
                if ((sospesi && isUltimoIncassoSospeso(titoloProxy)) || !sospesi) {
                    if (key != "") {
                        final String referente = titoloProxy.getCertificatoLloyds().getFilieraLloyds().getReferente();
                        final String numeroCertificato = titoloProxy.getCertificatoLloyds().getNumero();
                        final String rischio = titoloProxy.getCertificatoLloyds().getRischio();
                        final String identificativoContraente = getIdentificativoContraente(titoloProxy);
                        if (referente.toLowerCase().contains(key.toLowerCase())
                                || identificativoContraente.toLowerCase().contains(key.toLowerCase())
                                || numeroCertificato.toLowerCase().contains(key.toLowerCase())
                                || rischio.toLowerCase().contains(key.toLowerCase())) {
                            listaTitoliFiltrati.add(titoloProxy);
                        }
                    } else
                        listaTitoliFiltrati.add(titoloProxy);
                }
            }
        }
        display.setTitoliTrovati(listaTitoliFiltrati.size());
    }

    private String getIdentificativoContraente(NewTitoloProxy pp) {
        final ContraenteProxy contraente = pp.getContraente();
        if (!(Gender.fromString(contraente.getGenderString()) == Gender.C)) {
            return contraente.getFirstName() + " " + contraente.getLastName();
        } else {
            return contraente.getCompanyName();
        }
    }

    private String[] propsForDettaglio = new String[] { "certificatoLloyds", "contraente", "funzioniAbilitate",
            "ultimoIncassoCheHaMessoIlTitoloInStatoIncassato", "filieraLloyds", "incassiOrderByDataInserimentoDesc" };

    @Override
    public void onSelezioneTitolo(final NewTitoloProxy titoloProxy) {
        GWT.log("TitoloActivity onSelezioneTitolo");

        final ServiziPortale request = serviziTitoloProvider.get();
        Request<NewTitoloProxy> req = request.findTitolo(titoloProxy.getId()).with(propsForDettaglio);
        req.fire(new Receiver<NewTitoloProxy>() {
            @Override
            public void onSuccess(NewTitoloProxy updatedtitoloProxy) {
                GWT.log("SELEZIONE OK: " + updatedtitoloProxy.getCertificatoLloyds().getNumero());
                int index = backUpList.indexOf(titoloProxy);
                int index2 = listaTitoliFiltrati.indexOf(titoloProxy);
                titoloSelezionatoInDettaglio = updatedtitoloProxy;
                incassiDataProvider.getList().clear();
                incassiDataProvider.setList(updatedtitoloProxy.getIncassiOrderByDataInserimentoDesc());
                backUpList.remove(index);
                backUpList.add(index, updatedtitoloProxy);
                listaTitoliFiltrati.remove(index2);
                listaTitoliFiltrati.add(index2, updatedtitoloProxy);

                display.visualizzaDettaglio(updatedtitoloProxy);
            }

            public void onFailure(ServerFailure error) {
                GWT.log("SELEZIONE FAIL");
                RFErrorHandler.handle(error);
            }
        });
    }

    @Override
    public void saveDettaglioTitolo(String subAgente, String codCig) {
        GWT.log("TitoloActivity saveDettaglioTitolo: " + subAgente + " " + codCig);
        ServiziPortale request = serviziTitoloProvider.get();
        NewTitoloProxy updatingProxy = (NewTitoloProxy) request.edit(titoloSelezionatoInDettaglio);
        updatingProxy.setCodiceCig(codCig);
        updatingProxy.setCodiceSubagente(subAgente);
        request.aggiornaDatiTitolo(updatingProxy).with(propsForDettaglio).fire(new Receiver<Void>() {
            @Override
            public void onSuccess(Void response) {
                GWT.log("AGGIORNAMENTO OK");
                aggiornaTitoloProxyInListe();
            }

            @Override
            public void onFailure(ServerFailure error) {
                GWT.log("AGGIORNAMENTO FAIL:" + error.getStackTraceString());
                MaliceUtil.showError("Errore durante l'operazione di salvataggio del titolo.");
            }
        });
    }

    /************ INSERIMENTO *************/
    @Override
    public void onCreateNewTitolo() {
        GWT.log("TitoloActivity: createNewTitolo");
    }

    /************ DETTAGLIO *************/
    @Override
    public void incassaTitolo(BigDecimal importoIncasso, StatoIncasso tipoIncasso, MezzoPagamento mezzoPagamento,
            Date dataIncasso) {
        GWT.log("TitoliActivity.incassaTitolo: importoIncasso= " + importoIncasso);
        ServiziPortale sp = serviziTitoloProvider.get();
        DettaglioIncassoTitoloProxy incasso = sp.create(DettaglioIncassoTitoloProxy.class);
        incasso.setDataIncasso(dataIncasso);
        incasso.setImportoIncassoEuroCent(importoIncasso);
        incasso.setStringMezzoPagamento(mezzoPagamento.name());
        incasso.setStatoIncasso(tipoIncasso.name());
        incasso.setStringTipoOperazione(TipoOperazione.INCASSO.name());
        incasso.setImportoPremioEuroCent(titoloSelezionatoInDettaglio.getNetEuroCent());
        sp.incassaTitolo(titoloSelezionatoInDettaglio, incasso).with(propsForDettaglio).fire(new Receiver<Void>() {

            @Override
            public void onSuccess(Void response) {
                GWT.log("INCASSO OK");
                aggiornaTitoloProxyInListe();
            }

            @Override
            public void onFailure(ServerFailure error) {
                GWT.log("INCASSO FAIL:" + error.getStackTraceString());
                MaliceUtil.showError("Errore durante l'operazione di incasso del titolo. Titolo  non incassato.");
            }
        });
    }

    private void aggiornaTitoloProxyInListe() {
        MaliceUtil.showSuccess();
        onSelezioneTitolo(titoloSelezionatoInDettaglio);
        titoliDataProvider.refresh();
        incassiDataProvider.refresh();
        eventBus.fireEvent(new TitoloUpdatedEvent());
    }

    @Override
    public void annullaTitolo(String notaIncasso) {
        GWT.log("TitoloActivity: annullaTitolo.");

        Date dataDiAnnullamento = new Date();
        serviziTitoloProvider.get().annulloTitolo(titoloSelezionatoInDettaglio, dataDiAnnullamento, notaIncasso)
                .with(propsForDettaglio).fire(new Receiver<Void>() {

                    @Override
                    public void onSuccess(Void response) {
                        GWT.log("ANNULLO OK");
                        aggiornaTitoloProxyInListe();
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        GWT.log("ANNULLO FAIL:" + error.getStackTraceString());

                        MaliceUtil.showError("Errore durante l'operazione di annullo del titolo.");
                    }
                });
    }

    @Override
    public void recuperaTitoloSospeso(Date datatIncasso, BigDecimal sommaRecuperataEuroCent,
            MezzoPagamento mezzoPagamento) {
        GWT.log("TitoliActivity.recuperaTitoloSospeso: id=" + titoloSelezionatoInDettaglio.getId());

        // lo stato è fisso a EFFETTIVO
        serviziTitoloProvider
                .get()
                .recuperaTitoloSospeso(titoloSelezionatoInDettaglio, sommaRecuperataEuroCent, datatIncasso,
                        mezzoPagamento.name()).with(propsForDettaglio).fire(new Receiver<Void>() {

                    @Override
                    public void onSuccess(Void response) {
                        GWT.log("RECUPERO SOSPESO OK");
                        aggiornaTitoloProxyInListe();

                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        GWT.log("RECUPERO SOSPESO FAIL:" + error.getStackTraceString());

                        MaliceUtil
                                .showError("Errore durante l'operazione di recupero del titolo. Riprovare piu' tardi.");
                    }
                });
    }

    @Override
    public void revocaAnnulloTitolo() {
        GWT.log("TitoloAcitivity.revocaAnnulloTitolo: id=" + titoloSelezionatoInDettaglio.getId());

        serviziTitoloProvider.get().revocaAnnulloTitolo(titoloSelezionatoInDettaglio).with(propsForDettaglio)
                .fire(new Receiver<Void>() {

                    @Override
                    public void onSuccess(Void response) {
                        GWT.log("REVOCA ANNULLO OK");
                        aggiornaTitoloProxyInListe();

                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        GWT.log("REVOCA ANNULLO FAIL:" + error.getStackTraceString());

                        MaliceUtil.showError("Errore durante l'operazione di revoca titolo. Riprovare piu' tardi.");
                    }
                });
    }

    @Override
    public void stornaTitolo() {
        GWT.log("TitoloAcitivity.stornaTitolo: id=" + titoloSelezionatoInDettaglio.getId());
        Date dataInserimento = titoloSelezionatoInDettaglio.getIncassiOrderByDataInserimentoDesc().get(0)
                .getDataInserimento();
        Date today = new Date();
        // if
        // (MaliceUtil.formatDateToDayMeseAnno(dataInserimento).equals(MaliceUtil.formatDateToDayMeseAnno(today)))
        // {
        // GWT.log("eliminazione fisica dell'incasso poiche' la richiesta di storno avviene nella stessa giornata di registrazione dell'incasso");
        // serviziTitoloProvider.get().eliminaFisicamenteIncassoTitolo(titoloSelezionatoInDettaglio)
        // .with(propsForDettaglio).fire(new Receiver<Void>() {
        // @Override
        // public void onSuccess(Void response) {
        // GWT.log("ELIMINATO FISICAMENTE OK");
        // aggiornaTitoloProxyInListe();
        // }
        //
        // @Override
        // public void onFailure(ServerFailure error) {
        // GWT.log("ELIMINAZIONE FISICA INCASSO TITOLO FAIL:" +
        // error.getStackTraceString());
        // MaliceUtil
        // .showError("Errore durante l'operazione di eliminazione fisica dell'incasso. Riprovare piu' tardi.");
        // }
        // });
        // } else {
        GWT.log("eseguo operazione di storno in quanto il precedente incasso non e' stato eseguito in giornata");
        serviziTitoloProvider.get().stornaIncassoTitolo(titoloSelezionatoInDettaglio).with(propsForDettaglio)
                .fire(new Receiver<Void>() {
                    @Override
                    public void onSuccess(Void response) {
                        GWT.log("STORNO TITOLO OK");
                        aggiornaTitoloProxyInListe();
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        GWT.log("STORNO TITOLO FAIL:" + error.getStackTraceString());
                        MaliceUtil.showError("Errore durante l'operazione di storno del titolo. Riprovare piu' tardi.");
                    }
                });
        // }
    }

    @Override
    public void downloadListaTitoli() {
        GWT.log("TitoliActivity.downloadListaTitoli");
        StringBuilder stringToGet = new StringBuilder();
        for (NewTitoloProxy titoloFiltrato : listaTitoliFiltrati) {
            stringToGet.append(titoloFiltrato.getId() + ",");
        }
        Date today = new Date();
        DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy.MM.dd HH-mm-ss");
        stringToGet.append("Lista Titoli Del " + fmt.format(today) + ".xls");
        stringToGet.append(",1"); // Identificatore del tipo di download
        System.out.println(stringToGet.toString());
        MaliceUtil.downloadListaTitoli(stringToGet.toString());
    }

    @Override
    public void onChiusuraInviata(ChiusuraInviataEvent event) {
        nuovaCaricaTitoli();
    }
}