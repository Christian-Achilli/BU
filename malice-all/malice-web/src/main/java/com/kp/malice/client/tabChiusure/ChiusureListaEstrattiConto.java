package com.kp.malice.client.tabChiusure;

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
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.kp.malice.client.ui.gwtEvent.SelezioneEstrattoContoEvent;
import com.kp.malice.client.ui.gwtEvent.SelezioneEstrattoContoEvent.SelezioneEstrattoContoHandler;
import com.kp.malice.client.ui.resources.ListeResources;
import com.kp.malice.client.ui.resources.MaliceResources;
import com.kp.malice.client.ui.resources.SimplePagerMalice;
import com.kp.malice.client.ui.resources.SimplePagerMalice.PagerResources;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.proxies.EstrattoContoLioProxy;

public class ChiusureListaEstrattiConto extends Composite {

    private static ChiusureListaChiusureUiBinder uiBinder = GWT.create(ChiusureListaChiusureUiBinder.class);

    interface ChiusureListaChiusureUiBinder extends UiBinder<Widget, ChiusureListaEstrattiConto> {
    }

    @UiField(provided = true)
    CellTable<EstrattoContoLioProxy> cellTable;

    @UiField(provided = true)
    SimplePagerMalice pager;

    private Column<EstrattoContoLioProxy, String> brockerCh;

    private Column<EstrattoContoLioProxy, Number> numTitoliColumn;

    private Column<EstrattoContoLioProxy, Number> premiColumn;

    private Column<EstrattoContoLioProxy, Number> commissioniColumn;

    private Column<EstrattoContoLioProxy, Number> rimessaColumn;

    public ChiusureListaEstrattiConto() {
        cellTable = new CellTable<EstrattoContoLioProxy>(MaliceUtil.ROW_X_PAG, ListeResources.INSTANCE);
        cellTable.setWidth("100%", true);
        cellTable.addCellPreviewHandler(new Handler<EstrattoContoLioProxy>() {
            @Override
            public void onCellPreview(CellPreviewEvent<EstrattoContoLioProxy> event) {
                if (event.getNativeEvent().getType().contains("click")) {
                    EstrattoContoLioProxy estrattoContoLioProxySelezionato = event.getValue();
                    GWT.log("ChiusureListaChiusure.onCellPreview: fire SelezioneChiusuraEvent");
                    fireEvent(new SelezioneEstrattoContoEvent(estrattoContoLioProxySelezionato));
                }
            }
        });

        pager = new SimplePagerMalice(TextLocation.CENTER, PagerResources.INSTANCE, false, 0, true);
        pager.setDisplay(cellTable);

        brockerCh = new Column<EstrattoContoLioProxy, String>(new TextCell()) {
            @Override
            public String getValue(EstrattoContoLioProxy object) {
                return object.getLabel();
            }
        };

        numTitoliColumn = new Column<EstrattoContoLioProxy, Number>(new NumberCell()) {
            @Override
            public Number getValue(EstrattoContoLioProxy object) {
                return object.getTotTitoli();
            }
        };

        premiColumn = new Column<EstrattoContoLioProxy, Number>(new NumberCell(MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(EstrattoContoLioProxy object) {
                return object.getTotPremiEuroCent().movePointLeft(2);
            }
        };

        commissioniColumn = new Column<EstrattoContoLioProxy, Number>(new NumberCell(
                MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(EstrattoContoLioProxy object) {
                return object.getTotCommissioniEuroCent().movePointLeft(2);
            }
        };

        rimessaColumn = new Column<EstrattoContoLioProxy, Number>(new NumberCell(MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(EstrattoContoLioProxy object) {
                return object.getTotRimessaEuroCent().movePointLeft(2);
            }
        };

        cellTable.addColumn(brockerCh, "REFERENTE");
        cellTable.addColumn(numTitoliColumn, "NUM TITOLI");
        cellTable.addColumn(premiColumn, "TOT PREMI");
        cellTable.addColumn(commissioniColumn, "TOT COMMISSIONI");
        cellTable.addColumn(rimessaColumn, "TOT RIMESSE");

        cellTable.setWidth("100%", true);
        rimessaColumn.setCellStyleNames(MaliceResources.INSTANCE.main().columnRightAlign());
        final double COLUMN_WIDTH = 18;
        final Style.Unit STYLE_UNIT = Unit.PCT;
        cellTable.setColumnWidth(brockerCh, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(numTitoliColumn, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(premiColumn, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(commissioniColumn, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(rimessaColumn, COLUMN_WIDTH, STYLE_UNIT);

        initWidget(uiBinder.createAndBindUi(this));
    }

    public CellTable<EstrattoContoLioProxy> getTabella() {
        return cellTable;
    }

    public HandlerRegistration addSelezioneEstrattoContoHandler(SelezioneEstrattoContoHandler handler) {
        return addHandler(handler, SelezioneEstrattoContoEvent.TYPE);
    }
}
