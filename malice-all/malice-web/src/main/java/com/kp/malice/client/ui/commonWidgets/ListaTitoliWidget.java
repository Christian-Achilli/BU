package com.kp.malice.client.ui.commonWidgets;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.kp.malice.client.tabTitoli.StatusTitoloAndIncasso;
import com.kp.malice.client.ui.UIMaliceUtil;
import com.kp.malice.client.ui.gwtEvent.AggiornaStatoTitoloERimessaEvent;
import com.kp.malice.client.ui.gwtEvent.AggiornaStatoTitoloERimessaHandler;
import com.kp.malice.client.ui.gwtEvent.CreaTitoloEvent;
import com.kp.malice.client.ui.gwtEvent.CreaTitoloHandler;
import com.kp.malice.client.ui.gwtEvent.FiltraTitoliEvent;
import com.kp.malice.client.ui.gwtEvent.FiltraTitoliHandler;
import com.kp.malice.client.ui.gwtEvent.SelezioneTitoloEvent;
import com.kp.malice.client.ui.gwtEvent.SelezioneTitoloHandler;
import com.kp.malice.client.ui.resources.ListeResources;
import com.kp.malice.client.ui.resources.ListeResourcesForChrome;
import com.kp.malice.client.ui.resources.MaliceResources;
import com.kp.malice.client.ui.resources.SimplePagerMalice;
import com.kp.malice.client.ui.resources.SimplePagerMalice.PagerResources;
import com.kp.malice.shared.Gender;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.StatoIncasso;
import com.kp.malice.shared.StatusTitolo;
import com.kp.malice.shared.proxies.ContraenteProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class ListaTitoliWidget extends Composite {

    private static ListaTitoliWidgetUiBinder uiBinder = GWT.create(ListaTitoliWidgetUiBinder.class);

    interface ListaTitoliWidgetUiBinder extends UiBinder<Widget, ListaTitoliWidget> {
    }

    private int NUM_TITOLI_X_PAG = 10;

    @UiField(provided = true)
    CellTable<NewTitoloProxy> cellTable;

    @UiField(provided = true)
    SimplePagerMalice pager;
    private Column<NewTitoloProxy, String> numeroColumn;

    private Column<NewTitoloProxy, String> referenteColumn;

    private Column<NewTitoloProxy, String> contraenteColumn;

    private Column<NewTitoloProxy, Date> decorrenzaColumn;

    private Column<NewTitoloProxy, Date> scadenzaColumn;

    private Column<NewTitoloProxy, String> rischioColumn;

    private Column<NewTitoloProxy, Number> premioTotaleColumn;

    private Column<NewTitoloProxy, Number> premioIncassatoColumn;

    private Column<NewTitoloProxy, Number> abbuonoColumn;

    private Column<NewTitoloProxy, StatusTitoloAndIncasso> statusColumn;

    @UiField
    Image daIncassareOff;
    @UiField
    Image daIncassareOn;
    @UiField
    Image IncassatoOff;
    @UiField
    Image ConsolidatoOff;
    @UiField
    Image AnnullatoOff;
    @UiField
    Image IncassatoOn;
    @UiField
    Image ConsolidatoOn;
    @UiField
    Image AnnullatoOn;
    @UiField
    CheckBox sospeso;
    @UiField
    Element didascaliaButton;
    @UiField
    Element didascaliaLegend;

    public ListaTitoliWidget() {
        if (MaliceUtil.isChromeBrowser())
            cellTable = new CellTable<NewTitoloProxy>(NUM_TITOLI_X_PAG, ListeResourcesForChrome.INSTANCE);
        else
            cellTable = new CellTable<NewTitoloProxy>(NUM_TITOLI_X_PAG, ListeResources.INSTANCE);
        cellTable.setWidth("100%", true);
        cellTable.addCellPreviewHandler(new Handler<NewTitoloProxy>() {
            long lastClick = -1000;

            @Override
            public void onCellPreview(CellPreviewEvent<NewTitoloProxy> event) {
                if (event.getNativeEvent().getType().contains("click")) {
                    NewTitoloProxy titoloProxy = event.getValue();
                    GWT.log("ListaTitoliWidget.onCellPreview: fire SelezioneTitoloEvent");
                    GWT.log("incassi size: " + titoloProxy.getIncassiOrderByDataInserimentoDesc().size());
                    fireEvent(new SelezioneTitoloEvent(titoloProxy));
                }
            }
        });

        pager = new SimplePagerMalice(TextLocation.CENTER, PagerResources.INSTANCE, false, 0, true);
        pager.setDisplay(cellTable);

        /**
         * Add the columns to the table.
         */

        numeroColumn = new Column<NewTitoloProxy, String>(new EnsuredDbgIdTextCell()) {
            @Override
            public String getValue(NewTitoloProxy object) {
                return object.getCertificatoLloyds().getNumero() + "-" + object.getProgressivo();
            }
        };
        referenteColumn = new Column<NewTitoloProxy, String>(new TextCell()) {
            @Override
            public String getValue(NewTitoloProxy object) {
                return object.getCertificatoLloyds().getFilieraLloyds().getReferente();
            }
        };
        contraenteColumn = new Column<NewTitoloProxy, String>(new TextCell()) {
            @Override
            public String getValue(NewTitoloProxy object) {
                return getIdentificativo(object.getContraente());
            }
        };
        decorrenzaColumn = new Column<NewTitoloProxy, Date>(new DateCell(MaliceUtil.getDayMonthYearFormat())) {
            @Override
            public Date getValue(NewTitoloProxy object) {
                // prints Monday, December 17, 2007 in the default locale
                return object.getInceptionDate();
            }
        };
        scadenzaColumn = new Column<NewTitoloProxy, Date>(new DateCell(MaliceUtil.getDayMonthYearFormat())) {
            @Override
            public Date getValue(NewTitoloProxy object) {
                return object.getExpiryDate();
            }
        };
        rischioColumn = new Column<NewTitoloProxy, String>(new TextCell()) {
            @Override
            public String getValue(NewTitoloProxy object) {
                return object.getCertificatoLloyds().getRischio();
            }
        };
        premioTotaleColumn = new Column<NewTitoloProxy, Number>(new NumberCell(MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(NewTitoloProxy object) {
                BigDecimal bd = object.getNetEuroCent().add(object.getAccessoriEuroCent())
                        .add(object.getTaxesEuroCent());
                return bd.movePointLeft(2);
            }
        };
        premioIncassatoColumn = new Column<NewTitoloProxy, Number>(new NumberCell(MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(NewTitoloProxy object) {
                BigDecimal incassato = MaliceUtil.getIncassoValueEuroCent(object);
                return incassato.movePointLeft(2);
            }
        };
        abbuonoColumn = new Column<NewTitoloProxy, Number>(new NumberCell(MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(NewTitoloProxy object) {
                BigDecimal bd = MaliceUtil.calcolaAbbuonoEuroCent(object);
                return bd.movePointLeft(2);
            }
        };
        statusColumn = new Column<NewTitoloProxy, StatusTitoloAndIncasso>(new StatoTitoloAndIncassoImageResourceCell()) {
            @Override
            public StatusTitoloAndIncasso getValue(NewTitoloProxy object) {
                // è stato creato un nuovo enum (StatusTitoloAndIncasso) per
                // poter gestire i casi INCASSATO_SOSPESO e CONSOLIDATO_SOSPESO

                if (object.getIncassiOrderByDataInserimentoDesc().size() > 0
                        && StatoIncasso.fromString(object.getIncassiOrderByDataInserimentoDesc().get(0)
                                .getStatoIncasso()) == StatoIncasso.SOSPESO) {
                    if (StatusTitolo.fromString(object.getStringStatoTitolo()) == StatusTitolo.INCASSATO) {
                        return StatusTitoloAndIncasso.INCASSATO_SOSPESO;
                    } else if (StatusTitolo.fromString(object.getStringStatoTitolo()) == StatusTitolo.CONSOLIDATO) {
                        return StatusTitoloAndIncasso.CONSOLIDATO_SOSPESO;
                    }
                }
                return StatusTitoloAndIncasso.fromString(object.getStringStatoTitolo());
            }
        };

        statusColumn.setCellStyleNames(MaliceResources.INSTANCE.main().columnStatoTitolo());

        cellTable.addColumn(numeroColumn, "NUMERO");
        cellTable.addColumn(referenteColumn, "REFERENTE");
        cellTable.addColumn(contraenteColumn, "CONTRAENTE");
        cellTable.addColumn(decorrenzaColumn, "DECORR.");
        cellTable.addColumn(scadenzaColumn, "SCAD.");
        cellTable.addColumn(rischioColumn, "RISCHIO");
        cellTable.addColumn(premioTotaleColumn, "PR. TOTALE");
        cellTable.addColumn(premioIncassatoColumn, "PR. INCASSATO");
        cellTable.addColumn(abbuonoColumn, "ABBUONO");
        cellTable.addColumn(statusColumn, "STATO");

        cellTable.setColumnWidth(numeroColumn, 120, Unit.PX);
        cellTable.setColumnWidth(referenteColumn, 85, Unit.PX);
        cellTable.setColumnWidth(contraenteColumn, 145, Unit.PX);
        cellTable.setColumnWidth(decorrenzaColumn, 70, Unit.PX);
        cellTable.setColumnWidth(scadenzaColumn, 70, Unit.PX);
        cellTable.setColumnWidth(rischioColumn, 65, Unit.PX);
        cellTable.setColumnWidth(premioTotaleColumn, 85, Unit.PX);
        cellTable.setColumnWidth(premioIncassatoColumn, 80, Unit.PX);
        cellTable.setColumnWidth(abbuonoColumn, 80, Unit.PX);
        cellTable.setColumnWidth(statusColumn, 40, Unit.PX);

        for (int i = 0; i < cellTable.getColumnCount(); i++) {
            cellTable.getColumn(i).setSortable(true);
        }

        initWidget(uiBinder.createAndBindUi(this));

        // nascondo, bottoni di ricerca non attiva
        daIncassareOff.setVisible(false);
        IncassatoOff.setVisible(false);
        ConsolidatoOff.setVisible(false);
        AnnullatoOff.setVisible(false);

        UIMaliceUtil.nascondi(didascaliaLegend);
    }

    @UiHandler("daIncassareOff")
    public void onClickDaIncassareOff(ClickEvent e) {
        if (!isSospesi()) {
            daIncassareOn.setVisible(true);
            daIncassareOff.setVisible(false);
            filtraTitoliPerStato();
        }
    }

    @UiHandler("daIncassareOn")
    public void onClickDaIncassareOn(ClickEvent e) {
        if (!isSospesi()) {
            daIncassareOn.setVisible(false);
            daIncassareOff.setVisible(true);
            filtraTitoliPerStato();
        }
    }

    @UiHandler("IncassatoOff")
    public void onClickIncassatoOff(ClickEvent e) {
        if (!isSospesi()) {
            IncassatoOff.setVisible(false);
            IncassatoOn.setVisible(true);
            filtraTitoliPerStato();
        }
    }

    @UiHandler("IncassatoOn")
    public void onClickIncassatoOn(ClickEvent e) {
        if (!isSospesi()) {
            IncassatoOff.setVisible(true);
            IncassatoOn.setVisible(false);
            filtraTitoliPerStato();
        }
    }

    @UiHandler("ConsolidatoOff")
    public void onClickConsolidatoOff(ClickEvent e) {
        if (!isSospesi()) {
            ConsolidatoOff.setVisible(false);
            ConsolidatoOn.setVisible(true);
            filtraTitoliPerStato();
        }
    }

    @UiHandler("ConsolidatoOn")
    public void onClickConsolidatoOn(ClickEvent e) {
        if (!isSospesi()) {
            ConsolidatoOff.setVisible(true);
            ConsolidatoOn.setVisible(false);
            filtraTitoliPerStato();
        }
    }

    @UiHandler("AnnullatoOff")
    public void onClickAnnullatoOff(ClickEvent e) {
        if (!isSospesi()) {
            AnnullatoOff.setVisible(false);
            AnnullatoOn.setVisible(true);
            filtraTitoliPerStato();
        }
    }

    @UiHandler("AnnullatoOn")
    public void onClickAnnullatoOn(ClickEvent e) {
        if (!isSospesi()) {
            AnnullatoOff.setVisible(true);
            AnnullatoOn.setVisible(false);
            filtraTitoliPerStato();
        }
    }

    @UiHandler("sospeso")
    public void onClickSospeso(ClickEvent e) {
        if (isSospesi()) {
            AnnullatoOff.setVisible(true);
            AnnullatoOn.setVisible(false);
            daIncassareOn.setVisible(false);
            daIncassareOff.setVisible(true);
            ConsolidatoOn.setVisible(true);
            ConsolidatoOff.setVisible(false);
            IncassatoOn.setVisible(true);
            IncassatoOff.setVisible(false);
        } else {
            AnnullatoOff.setVisible(false);
            AnnullatoOn.setVisible(true);
            daIncassareOn.setVisible(true);
            daIncassareOff.setVisible(false);
        }
        filtraTitoliPerStato();
    }

    private void filtraTitoliPerStato() {
        List<StatusTitolo> statusTitolos = getListaStatiSelezionati();
        GWT.log("ListaTitoliWidget.filtraTitoliPerStato: fire FiltraTitoliEvent");
        fireEvent(new FiltraTitoliEvent(null, statusTitolos, sospeso.getValue()));
    }

    public List<StatusTitolo> getListaStatiSelezionati() {
        List<StatusTitolo> statusTitolos = new ArrayList<StatusTitolo>();
        if (AnnullatoOn.isVisible()) {
            GWT.log("Annullato");
            statusTitolos.add(StatusTitolo.ANNULLATO);
        }
        if (ConsolidatoOn.isVisible()) {
            GWT.log("Consolidato");
            statusTitolos.add(StatusTitolo.CONSOLIDATO);
        }
        if (daIncassareOn.isVisible()) {
            GWT.log("DaIncassare");
            statusTitolos.add(StatusTitolo.DA_INCASSARE);
        }
        if (IncassatoOn.isVisible()) {
            GWT.log("Incassato");
            statusTitolos.add(StatusTitolo.INCASSATO);
        }
        return statusTitolos;
    }

    public boolean isSospesi() {
        return sospeso.getValue();
    }

    private boolean isPersonaFisica(ContraenteProxy cp) {
        return !(Gender.fromString(cp.getGenderString()) == Gender.C);
    }

    private String getIdentificativo(ContraenteProxy cp) {
        if (isPersonaFisica(cp)) {
            return cp.getFirstName() + " " + cp.getLastName();
        } else {
            return cp.getCompanyName();
        }
    }

    public void setComparatorsAndSortHandler(ListHandler<NewTitoloProxy> sortHandler) {

        sortHandler.setComparator(numeroColumn, new Comparator<NewTitoloProxy>() {
            public int compare(NewTitoloProxy o1, NewTitoloProxy o2) {
                return (o1.getCertificatoLloyds().getNumero() + "-" + o1.getProgressivo()).compareTo((o2
                        .getCertificatoLloyds().getNumero() + "-" + o2.getProgressivo()));
            }
        });

        sortHandler.setComparator(referenteColumn, new Comparator<NewTitoloProxy>() {
            public int compare(NewTitoloProxy o1, NewTitoloProxy o2) {
                return o1.getCertificatoLloyds().getFilieraLloyds().getReferente()
                        .compareTo(o2.getCertificatoLloyds().getFilieraLloyds().getReferente());
            }
        });

        sortHandler.setComparator(contraenteColumn, new Comparator<NewTitoloProxy>() {
            public int compare(NewTitoloProxy o1, NewTitoloProxy o2) {
                return getIdentificativo(o1.getContraente()).compareTo(getIdentificativo(o2.getContraente()));
            }
        });
        sortHandler.setComparator(decorrenzaColumn, new Comparator<NewTitoloProxy>() {
            public int compare(NewTitoloProxy o1, NewTitoloProxy o2) {
                return o1.getInceptionDate().compareTo(o2.getInceptionDate());
            }
        });
        sortHandler.setComparator(scadenzaColumn, new Comparator<NewTitoloProxy>() {
            public int compare(NewTitoloProxy o1, NewTitoloProxy o2) {
                return o1.getExpiryDate().compareTo(o2.getExpiryDate());
            }
        });
        sortHandler.setComparator(rischioColumn, new Comparator<NewTitoloProxy>() {
            public int compare(NewTitoloProxy o1, NewTitoloProxy o2) {
                return o1.getCertificatoLloyds().getRischio().compareTo(o2.getCertificatoLloyds().getRischio());
            }
        });
        sortHandler.setComparator(premioTotaleColumn, new Comparator<NewTitoloProxy>() {
            public int compare(NewTitoloProxy o1, NewTitoloProxy o2) {
                return o1.getNetEuroCent().add(o1.getAccessoriEuroCent()).add(o1.getTaxesEuroCent())
                        .compareTo(o2.getNetEuroCent().add(o1.getAccessoriEuroCent()).add(o1.getTaxesEuroCent()));
            }
        });
        sortHandler.setComparator(premioIncassatoColumn, new Comparator<NewTitoloProxy>() {
            public int compare(NewTitoloProxy o1, NewTitoloProxy o2) {
                return MaliceUtil.getIncassoValueEuroCent(o1).compareTo(MaliceUtil.getIncassoValueEuroCent(o2));
            }
        });
        sortHandler.setComparator(abbuonoColumn, new Comparator<NewTitoloProxy>() {
            public int compare(NewTitoloProxy o1, NewTitoloProxy o2) {
                return MaliceUtil.calcolaAbbuonoEuroCent(o1).compareTo(MaliceUtil.calcolaAbbuonoEuroCent(o2));
            }
        });
        sortHandler.setComparator(statusColumn, new Comparator<NewTitoloProxy>() {
            public int compare(NewTitoloProxy o1, NewTitoloProxy o2) {
                return o1.getStringStatoTitolo().compareTo(o2.getStringStatoTitolo());

            }
        });
        cellTable.addColumnSortHandler(sortHandler);
    }

    public CellTable<NewTitoloProxy> getTabella() {
        return cellTable;
    }

    public HandlerRegistration addSelezioneTitoloHandler(SelezioneTitoloHandler handler) {
        return addHandler(handler, SelezioneTitoloEvent.TYPE);
    }

    public HandlerRegistration addAggiornaStatoTitoloERimessaHandler(AggiornaStatoTitoloERimessaHandler handler) {
        return addHandler(handler, AggiornaStatoTitoloERimessaEvent.TYPE);
    }

    public HandlerRegistration addCreaTitoloEvent(CreaTitoloHandler handler) {
        return addHandler(handler, CreaTitoloEvent.TYPE);
    }

    public HandlerRegistration addFiltraTitoliHandler(FiltraTitoliHandler handler) {
        return addHandler(handler, FiltraTitoliEvent.TYPE);
    }

    public void resetFiltri() {
        sospeso.setValue(false);
        daIncassareOff.setVisible(false);
        IncassatoOff.setVisible(false);
        ConsolidatoOff.setVisible(false);
        AnnullatoOff.setVisible(false);
        daIncassareOn.setVisible(true);
        IncassatoOn.setVisible(true);
        ConsolidatoOn.setVisible(true);
        AnnullatoOn.setVisible(true);
    }

    public void disabilitaDidascaliaFiltro() {
        UIMaliceUtil.nascondi(didascaliaButton);
        UIMaliceUtil.visualizza(didascaliaLegend);
    }

}
