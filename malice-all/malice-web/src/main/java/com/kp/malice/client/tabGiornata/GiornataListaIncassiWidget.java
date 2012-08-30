package com.kp.malice.client.tabGiornata;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.kp.malice.client.ui.gwtEvent.SelezioneIncassoEvent;
import com.kp.malice.client.ui.gwtEvent.SelezioneIncassoHandler;
import com.kp.malice.client.ui.resources.ListeResources;
import com.kp.malice.client.ui.resources.MaliceResources;
import com.kp.malice.client.ui.resources.SimplePagerMalice;
import com.kp.malice.client.ui.resources.SimplePagerMalice.PagerResources;
import com.kp.malice.shared.Gender;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.proxies.DettaglioIncassoTitoloProxy;
import com.kp.malice.shared.proxies.DettaglioIncassoTitoloProxy;

public class GiornataListaIncassiWidget extends Composite implements IsWidget {

    private static ListaIncassiUiBinder uiBinder = GWT.create(ListaIncassiUiBinder.class);

    interface ListaIncassiUiBinder extends UiBinder<Widget, GiornataListaIncassiWidget> {
    }

    @UiField(provided = true)
    CellTable<DettaglioIncassoTitoloProxy> cellTable;

    @UiField(provided = true)
    SimplePagerMalice pager;
  
    private Column<DettaglioIncassoTitoloProxy, Date> dataIncasso;

    private Column<DettaglioIncassoTitoloProxy, Number> premi;

    private Column<DettaglioIncassoTitoloProxy, Number> importoIncasso;

    private Column<DettaglioIncassoTitoloProxy, Number> importoAbbuono;

    private Column<DettaglioIncassoTitoloProxy, Number> importoProvvigioni;

    private Column<DettaglioIncassoTitoloProxy, String> contraente;

    private Column<DettaglioIncassoTitoloProxy, String> numeroTitolo;

    public GiornataListaIncassiWidget(int pageSize) {
        cellTable = new CellTable<DettaglioIncassoTitoloProxy>(pageSize, ListeResources.INSTANCE);
       
        dataIncasso = new Column<DettaglioIncassoTitoloProxy, Date>(new DateCell(MaliceUtil.getDayMonthYearFormat())) {
            @Override
            public Date getValue(DettaglioIncassoTitoloProxy object) {
                return object.getDataIncasso();
            }
        };
        
        premi = new Column<DettaglioIncassoTitoloProxy, Number>(new NumberCell(MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(DettaglioIncassoTitoloProxy object) {
                BigDecimal bd = object.getImportoPremioEuroCent();
                return bd.movePointLeft(2);
            }
        };

        importoIncasso = new Column<DettaglioIncassoTitoloProxy, Number>(new NumberCell(MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(DettaglioIncassoTitoloProxy object) {
                BigDecimal bd = object.getImportoIncassoEuroCent();
                return bd.movePointLeft(2);
            }
        };
        
        importoAbbuono = new Column<DettaglioIncassoTitoloProxy, Number>(new NumberCell(MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(DettaglioIncassoTitoloProxy object) {
                BigDecimal bd = object.getImportoAbbuonoEuroCent();
                return bd.movePointLeft(2);
            }
        };
        
        importoProvvigioni = new Column<DettaglioIncassoTitoloProxy, Number>(new NumberCell(MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(DettaglioIncassoTitoloProxy object) {
                BigDecimal bd = object.getImportoProvvigioniEuroCent();
                return bd.movePointLeft(2);
            }
        };
        
        contraente = new Column<DettaglioIncassoTitoloProxy, String>(new TextCell()) {
            @Override
            public String getValue(DettaglioIncassoTitoloProxy object) {
               return object.getIdentificativoContraente();
            }
        };
        
        numeroTitolo = new Column<DettaglioIncassoTitoloProxy, String>(new TextCell()) {
            @Override
            public String getValue(DettaglioIncassoTitoloProxy object) {
               return object.getNumeroTitolo();
            }
        };
        
        //INTESTAZIONE
        cellTable.addColumn(dataIncasso, "DATA INCASSO");
        cellTable.addColumn(premi, "PREMI");
        cellTable.addColumn(importoIncasso, "INCASSI");
        cellTable.addColumn(importoAbbuono, "ABBUONI");
        cellTable.addColumn(importoProvvigioni, "COMMISSIONI");
        cellTable.addColumn(contraente, "CONTRAENTE");
        cellTable.addColumn(numeroTitolo, "NUMERO TITOLO");

        //STILE
        cellTable.setWidth("100%", true);
        numeroTitolo.setCellStyleNames(MaliceResources.INSTANCE.main().columnIncassiNumeroTitolo());
        final int COLUMN_WIDTH = 20;
        final Style.Unit STYLE_UNIT = Unit.PCT;
        cellTable.setColumnWidth(premi, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(dataIncasso, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(importoIncasso, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(importoAbbuono, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(importoProvvigioni, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(contraente, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(numeroTitolo, COLUMN_WIDTH, STYLE_UNIT);

        //        //SORTABLE
        //        for (int i = 0; i < cellTable.getColumnCount(); i++) {
        //            cellTable.getColumn(i).setSortable(true);
        //        }

        //PAGINATORE
        pager = new SimplePagerMalice(TextLocation.CENTER, PagerResources.INSTANCE, false, 0, true);
        pager.setDisplay(cellTable);
        initWidget(uiBinder.createAndBindUi(this));
    }

    public HasData<DettaglioIncassoTitoloProxy> getTabellaIncassi() {
        return cellTable;
    }

    public void setComparatorsAndSortHandler(ListHandler<DettaglioIncassoTitoloProxy> incassiSortHandler) {
        //        incassiSortHandler.setComparator(dataIncasso, new Comparator<DettaglioIncassoTitoloProxy>() {
        //            public int compare(DettaglioIncassoTitoloProxy o1, DettaglioIncassoTitoloProxy o2) {
        //                return o1.getDataIncasso().compareTo(o2.getDataIncasso());
        //            }
        //        });
        //        incassiSortHandler.setComparator(dataInserimento, new Comparator<DettaglioIncassoTitoloProxy>() {
        //            public int compare(DettaglioIncassoTitoloProxy o1, DettaglioIncassoTitoloProxy o2) {
        //                return o1.getDataRecupero().compareTo(o2.getDataRecupero());
        //            }
        //        });
        //        incassiSortHandler.setComparator(importoIncasso, new Comparator<DettaglioIncassoTitoloProxy>() {
        //            public int compare(DettaglioIncassoTitoloProxy o1, DettaglioIncassoTitoloProxy o2) {
        //                return o1.getImportoIncassoEuroCent().compareTo(o2.getImportoIncassoEuroCent());
        //            }
        //        });
        //        incassiSortHandler.setComparator(mezzoPagamento, new Comparator<DettaglioIncassoTitoloProxy>() {
        //            public int compare(DettaglioIncassoTitoloProxy o1, DettaglioIncassoTitoloProxy o2) {
        //                return o1.getStringMezzoPagamento().compareTo(o2.getStringMezzoPagamento());
        //            }
        //        });
        //        incassiSortHandler.setComparator(statoIncasso, new Comparator<DettaglioIncassoTitoloProxy>() {
        //            public int compare(DettaglioIncassoTitoloProxy o1, DettaglioIncassoTitoloProxy o2) {
        //                return o1.getStringStatoIncasso().compareTo(o2.getStringStatoIncasso());
        //            }
        //        });
        //        cellTable.addColumnSortHandler(incassiSortHandler);
    }

    public CellTable<DettaglioIncassoTitoloProxy> getTabella() {
        return cellTable;
    }

    public HandlerRegistration addSelezioneIncassoHandler(SelezioneIncassoHandler handler) {
        return addHandler(handler, SelezioneIncassoEvent.TYPE);
    }
}
