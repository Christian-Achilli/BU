package com.kp.malice.client.ui.widget;

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
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.HasData;
import com.kp.malice.client.ui.gwtEvent.SelezioneIncassoEvent;
import com.kp.malice.client.ui.gwtEvent.SelezioneIncassoHandler;
import com.kp.malice.client.ui.resources.ListeResources;
import com.kp.malice.client.ui.resources.MaliceResources;
import com.kp.malice.client.ui.resources.SimplePagerMalice;
import com.kp.malice.client.ui.resources.SimplePagerMalice.PagerResources;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.StatoIncasso;
import com.kp.malice.shared.TipoOperazione;
import com.kp.malice.shared.proxies.IncassoTitoloProxy;

public class ListaIncassiWidget extends Composite implements IsWidget {

    private static ListaIncassiUiBinder uiBinder = GWT.create(ListaIncassiUiBinder.class);

    interface ListaIncassiUiBinder extends UiBinder<Widget, ListaIncassiWidget> {
    }

    @UiField(provided = true)
    CellTable<IncassoTitoloProxy> cellTable;

    @UiField(provided = true)
    SimplePagerMalice pager;
    

    private Column<IncassoTitoloProxy, Date> dataIncasso;

    private Column<IncassoTitoloProxy, Date> dataInserimento;

    private Column<IncassoTitoloProxy, Number> importoIncasso;

    private Column<IncassoTitoloProxy, Number> importoAbbuono;

    private Column<IncassoTitoloProxy, String> mezzoPagamento;
    
    private Column<IncassoTitoloProxy, TipoOperazione> tipoOperazione;

    private Column<IncassoTitoloProxy, StatoIncasso> statoIncasso;

    public ListaIncassiWidget(int pageSize) {
        //INCASSI PER PAGINA
        cellTable = new CellTable<IncassoTitoloProxy>(pageSize, ListeResources.INSTANCE);

        cellTable.addCellPreviewHandler(new Handler<IncassoTitoloProxy>() {
            @Override
            public void onCellPreview(CellPreviewEvent<IncassoTitoloProxy> event) {
                if (event.getNativeEvent().getType().contains("click")) {
                	IncassoTitoloProxy incassoTitoloProxy = event.getValue();
                    GWT.log("fire SelezioneIncassoEvent");
                    fireEvent(new SelezioneIncassoEvent(incassoTitoloProxy));
                }
            }
        });

        //TIPO
        tipoOperazione = new Column<IncassoTitoloProxy, TipoOperazione>(new TipoOperazioneCell()) {
            @Override
            public TipoOperazione getValue(IncassoTitoloProxy object) {
                return TipoOperazione.fromString(object.getStringTipoOperazione());
            }
        };        
        dataIncasso = new Column<IncassoTitoloProxy, Date>(new DateCell(MaliceUtil.getDayMonthYearFormat())) {
            @Override
            public Date getValue(IncassoTitoloProxy object) {
                return object.getDataIncasso();
            }
        };
        dataInserimento = new Column<IncassoTitoloProxy, Date>(new DateCell(MaliceUtil.getDayMonthYearFormat())) {
            @Override
            public Date getValue(IncassoTitoloProxy object) {
                return object.getDataInserimento();
            }
        };
        importoIncasso = new Column<IncassoTitoloProxy, Number>(new NumberCell(MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(IncassoTitoloProxy object) {
                BigDecimal bd = object.getImportoIncassoEuroCent();
                return bd.movePointLeft(2);
            }
        };
        importoAbbuono = new Column<IncassoTitoloProxy, Number>(new NumberCell(MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(IncassoTitoloProxy object) {
                BigDecimal bd = object.getImportoAbbuonoEuroCent();
                return bd.movePointLeft(2);
            }
        };
        mezzoPagamento = new Column<IncassoTitoloProxy, String>(new TextCell()) {
            @Override
            public String getValue(IncassoTitoloProxy object) {
                return object.getStringMezzoPagamento();
            }
        };
        
        statoIncasso = new Column<IncassoTitoloProxy, StatoIncasso>(new StatoIncassoImageResourceCell()) {
            @Override
            public StatoIncasso getValue(IncassoTitoloProxy object) {
                return StatoIncasso.decodeFromIncasso(object);
            }
        };

        //INTESTAZIONE
        cellTable.addColumn(tipoOperazione, "TIPO OPERAZIONE");
        cellTable.addColumn(dataInserimento, "DATA REGISTRAZIONE");
        cellTable.addColumn(dataIncasso, "DATA INCASSO");
        cellTable.addColumn(importoIncasso, "IMPORTO INCASSO");
        cellTable.addColumn(importoAbbuono, "IMPORTO ABBUONO");
        cellTable.addColumn(mezzoPagamento, "MEZZO PAGAMENTO");
        cellTable.addColumn(statoIncasso, "STATO INCASSO");

        //STILE
        cellTable.setWidth("100%", true);
        statoIncasso.setCellStyleNames(MaliceResources.INSTANCE.main().columnIncassiStatoTitolo());
        final int COLUMN_WIDTH = 20;
        final Style.Unit STYLE_UNIT = Unit.PCT;
        cellTable.setColumnWidth(tipoOperazione, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(dataIncasso, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(dataInserimento, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(importoIncasso, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(importoAbbuono, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(mezzoPagamento, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(statoIncasso, COLUMN_WIDTH, STYLE_UNIT);

        //        //SORTABLE
        //        for (int i = 0; i < cellTable.getColumnCount(); i++) {
        //            cellTable.getColumn(i).setSortable(true);
        //        }

        //PAGINATORE
        pager = new SimplePagerMalice(TextLocation.CENTER, PagerResources.INSTANCE, false, 0, true);
        pager.setDisplay(cellTable);
        initWidget(uiBinder.createAndBindUi(this));
    }

    public HasData<IncassoTitoloProxy> getTabellaIncassi() {
        return cellTable;
    }

    public void setComparatorsAndSortHandler(ListHandler<IncassoTitoloProxy> incassiSortHandler) {
        //        incassiSortHandler.setComparator(dataIncasso, new Comparator<IncassoTitoloProxy>() {
        //            public int compare(IncassoTitoloProxy o1, IncassoTitoloProxy o2) {
        //                return o1.getDataIncasso().compareTo(o2.getDataIncasso());
        //            }
        //        });
        //        incassiSortHandler.setComparator(dataInserimento, new Comparator<IncassoTitoloProxy>() {
        //            public int compare(IncassoTitoloProxy o1, IncassoTitoloProxy o2) {
        //                return o1.getDataRecupero().compareTo(o2.getDataRecupero());
        //            }
        //        });
        //        incassiSortHandler.setComparator(importoIncasso, new Comparator<IncassoTitoloProxy>() {
        //            public int compare(IncassoTitoloProxy o1, IncassoTitoloProxy o2) {
        //                return o1.getImportoIncassoEuroCent().compareTo(o2.getImportoIncassoEuroCent());
        //            }
        //        });
        //        incassiSortHandler.setComparator(mezzoPagamento, new Comparator<IncassoTitoloProxy>() {
        //            public int compare(IncassoTitoloProxy o1, IncassoTitoloProxy o2) {
        //                return o1.getStringMezzoPagamento().compareTo(o2.getStringMezzoPagamento());
        //            }
        //        });
        //        incassiSortHandler.setComparator(statoIncasso, new Comparator<IncassoTitoloProxy>() {
        //            public int compare(IncassoTitoloProxy o1, IncassoTitoloProxy o2) {
        //                return o1.getStringStatoIncasso().compareTo(o2.getStringStatoIncasso());
        //            }
        //        });
        //        cellTable.addColumnSortHandler(incassiSortHandler);
    }

    public CellTable<IncassoTitoloProxy> getTabella() {
        return cellTable;
    }

    public HandlerRegistration addSelezioneIncassoHandler(SelezioneIncassoHandler handler) {
        return addHandler(handler, SelezioneIncassoEvent.TYPE);
    }
}
