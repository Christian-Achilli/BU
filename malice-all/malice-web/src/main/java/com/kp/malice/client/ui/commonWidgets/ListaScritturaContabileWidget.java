package com.kp.malice.client.ui.commonWidgets;

import java.util.Comparator;

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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.kp.malice.client.ui.gwtEvent.SelezioneScritturaContabileEvent;
import com.kp.malice.client.ui.gwtEvent.SelezioneScritturaContabileHandler;
import com.kp.malice.client.ui.resources.ListeResources;
import com.kp.malice.client.ui.resources.MaliceResources;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.proxies.ScritturaContabileProxy;

public class ListaScritturaContabileWidget extends Composite {

    private static ListaScritturaContabileUiBinder uiBinder = GWT.create(ListaScritturaContabileUiBinder.class);

    interface ListaScritturaContabileUiBinder extends UiBinder<Widget, ListaScritturaContabileWidget> {
    }

    private int NUM_TITOLI_X_PAG = 16;

    @UiField(provided = true)
    CellTable<ScritturaContabileProxy> cellTable;

    private Column<ScritturaContabileProxy, String> tipoOperazioneColumn;

    private Column<ScritturaContabileProxy, String> statoIncassoColumn;

    private Column<ScritturaContabileProxy, String> mezzoPagamentoColumn;

    private Column<ScritturaContabileProxy, Number> premiColumn;

    private Column<ScritturaContabileProxy, Number> incassiColumn;

    private Column<ScritturaContabileProxy, Number> abbuoniColumn;

    private Column<ScritturaContabileProxy, Number> provvigioniColumn;

    private Column<ScritturaContabileProxy, String> counterColumn;

    public ListaScritturaContabileWidget() {
        cellTable = new CellTable<ScritturaContabileProxy>(NUM_TITOLI_X_PAG, ListeResources.INSTANCE);
        cellTable.setWidth("100%", true);
        cellTable.addCellPreviewHandler(new Handler<ScritturaContabileProxy>() {

            @Override
            public void onCellPreview(CellPreviewEvent<ScritturaContabileProxy> event) {
                if (event.getNativeEvent().getType().contains("click")) {
                    ScritturaContabileProxy scritturaContabileProxy = event.getValue();
                    GWT.log("ListaScritturaContabileWidget.onCellPreview: fire SelezioneScritturaContabileEvent");
                    fireEvent(new SelezioneScritturaContabileEvent(scritturaContabileProxy));
                }
            }
        });

        //        pager = new SimplePagerMalice(TextLocation.CENTER, PagerResources.INSTANCE, false, 0, true);
        //        pager.setDisplay(cellTable);

        /**
         * Add the columns to the table.
         */
        tipoOperazioneColumn = new Column<ScritturaContabileProxy, String>(new TextCell()) {
            @Override
            public String getValue(ScritturaContabileProxy object) {
                return object.getTipoOperazione();
            }
        };
        statoIncassoColumn = new Column<ScritturaContabileProxy, String>(new TextCell()) {
            @Override
            public String getValue(ScritturaContabileProxy object) {
                return object.getStatoIncasso();
            }
        };
        mezzoPagamentoColumn = new Column<ScritturaContabileProxy, String>(new TextCell()) {
            @Override
            public String getValue(ScritturaContabileProxy object) {
                return object.getCodMzzPagm();
            }
        };
        premiColumn = new Column<ScritturaContabileProxy, Number>(new NumberCell(MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(ScritturaContabileProxy object) {
                return object.getPremiEuroCent().movePointLeft(2);
            }
        };
        incassiColumn = new Column<ScritturaContabileProxy, Number>(
                new NumberCell(MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(ScritturaContabileProxy object) {
                return object.getImportiEuroCent().movePointLeft(2);
            }
        };
        abbuoniColumn = new Column<ScritturaContabileProxy, Number>(
                new NumberCell(MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(ScritturaContabileProxy object) {
                return object.getAbbuoniEuroCent().movePointLeft(2);
            }
        };
        provvigioniColumn = new Column<ScritturaContabileProxy, Number>(new NumberCell(
                MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(ScritturaContabileProxy object) {
                return object.getProvvigioniEuroCent().movePointLeft(2);
            }
        };
        counterColumn = new Column<ScritturaContabileProxy, String>(new TextCell()) {
            @Override
            public String getValue(ScritturaContabileProxy object) {
                return "" + object.getCounter();
            }
        };

        cellTable.addColumn(tipoOperazioneColumn, "TIPO OPERAZIONE");
        cellTable.addColumn(statoIncassoColumn, "STATO INCASSO");
        cellTable.addColumn(mezzoPagamentoColumn, "MEZZO PAGAMENTO");
        cellTable.addColumn(premiColumn, "PREMI");
        cellTable.addColumn(incassiColumn, "INCASSI");
        cellTable.addColumn(abbuoniColumn, "ABBUONI");
        cellTable.addColumn(provvigioniColumn, "COMMISSIONI");
        cellTable.addColumn(counterColumn, "NUM OPERAZIONI");
        //STILE
        cellTable.setWidth("100%", true);
        counterColumn.setCellStyleNames(MaliceResources.INSTANCE.main().columnRightAlign());
        final double COLUMN_WIDTH = 12.5;
        final Style.Unit STYLE_UNIT = Unit.PCT;
        cellTable.setColumnWidth(tipoOperazioneColumn, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(statoIncassoColumn, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(mezzoPagamentoColumn, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(premiColumn, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(incassiColumn, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(abbuoniColumn, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(provvigioniColumn, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(counterColumn, COLUMN_WIDTH, STYLE_UNIT);

        tipoOperazioneColumn.setSortable(true);
        statoIncassoColumn.setSortable(true);
        mezzoPagamentoColumn.setSortable(true);

        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setComparatorsAndSortHandler(ListHandler<ScritturaContabileProxy> sortHandler) {
        sortHandler.setComparator(tipoOperazioneColumn, new Comparator<ScritturaContabileProxy>() {
            public int compare(ScritturaContabileProxy o1, ScritturaContabileProxy o2) {
                return o1.getTipoOperazione().compareTo(o2.getTipoOperazione());
            }
        });
        sortHandler.setComparator(statoIncassoColumn, new Comparator<ScritturaContabileProxy>() {
            public int compare(ScritturaContabileProxy o1, ScritturaContabileProxy o2) {
                return o1.getStatoIncasso().compareTo(o2.getStatoIncasso());
            }
        });
        sortHandler.setComparator(mezzoPagamentoColumn, new Comparator<ScritturaContabileProxy>() {
            public int compare(ScritturaContabileProxy o1, ScritturaContabileProxy o2) {
                return o1.getCodMzzPagm().compareTo(o2.getCodMzzPagm());
            }
        });
        cellTable.addColumnSortHandler(sortHandler);
    }

    public CellTable<ScritturaContabileProxy> getTabella() {
        return cellTable;
    }

    public HandlerRegistration addSelezioneScritturaContabileHandler(SelezioneScritturaContabileHandler handler) {
        return addHandler(handler, SelezioneScritturaContabileEvent.TYPE);
    }
}
