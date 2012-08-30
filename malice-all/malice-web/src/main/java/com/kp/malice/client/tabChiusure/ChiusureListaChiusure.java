package com.kp.malice.client.tabChiusure;

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
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.kp.malice.client.ui.commonWidgets.StatoChiusuraImageResourceCell;
import com.kp.malice.client.ui.gwtEvent.SelezioneChiusuraEvent;
import com.kp.malice.client.ui.gwtEvent.SelezioneChiusuraEvent.SelezioneChiusuraHandler;
import com.kp.malice.client.ui.resources.ListeResources;
import com.kp.malice.client.ui.resources.MaliceResources;
import com.kp.malice.client.ui.resources.SimplePagerMalice;
import com.kp.malice.client.ui.resources.SimplePagerMalice.PagerResources;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.StatoChiusura;
import com.kp.malice.shared.proxies.ChiusuraLioProxy;

public class ChiusureListaChiusure extends Composite{

    private static ChiusureListaChiusureUiBinder uiBinder = GWT.create(ChiusureListaChiusureUiBinder.class);

    interface ChiusureListaChiusureUiBinder extends UiBinder<Widget, ChiusureListaChiusure> {
    }
    
    @UiField(provided = true)
    CellTable<ChiusuraLioProxy> cellTable;

    @UiField(provided = true)
    SimplePagerMalice pager;
    
    private Column<ChiusuraLioProxy, String> meseColumn;
    
    private Column<ChiusuraLioProxy, Number> numEstrattoConto;

    private Column<ChiusuraLioProxy, Number> numTitoliColumn;

    private Column<ChiusuraLioProxy, Number> premiColumn;

    private Column<ChiusuraLioProxy, Number> commissioniColumn;

    private Column<ChiusuraLioProxy, Date> dataInvioColumn;

    private Column<ChiusuraLioProxy, StatoChiusura> statoColumn;
    
    public ChiusureListaChiusure() {
        cellTable = new CellTable<ChiusuraLioProxy>(MaliceUtil.ROW_X_PAG, ListeResources.INSTANCE);
        cellTable.setWidth("100%", true);
        cellTable.addCellPreviewHandler(new Handler<ChiusuraLioProxy>() {
            @Override
            public void onCellPreview(CellPreviewEvent<ChiusuraLioProxy> event) {
                if (event.getNativeEvent().getType().contains("click")) {
                    ChiusuraLioProxy chiusuraLioProxy = event.getValue();
                    GWT.log("ChiusureListaChiusure.onCellPreview: fire SelezioneChiusuraEvent");
                    fireEvent(new SelezioneChiusuraEvent(chiusuraLioProxy));
                }
            }
        });
        
        pager = new SimplePagerMalice(TextLocation.CENTER, PagerResources.INSTANCE, false, 0, true);
        pager.setDisplay(cellTable);
        
        meseColumn = new Column<ChiusuraLioProxy, String>(new TextCell()) {
            @Override
            public String getValue(ChiusuraLioProxy object) {
                return object.getLabel();
            }
        };
        
        numEstrattoConto = new Column<ChiusuraLioProxy, Number>(new NumberCell()) {
            @Override
            public Number getValue(ChiusuraLioProxy object) {
                return object.getTotEstrattiConto();
            }
        };
        
        numTitoliColumn = new Column<ChiusuraLioProxy, Number>(new NumberCell()) {
            @Override
            public Number getValue(ChiusuraLioProxy object) {
                return object.getTotTitoli();
            }
        };
        
        premiColumn = new Column<ChiusuraLioProxy, Number>(new NumberCell(MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(ChiusuraLioProxy object) {
                return object.getTotPremiEuroCent().movePointLeft(2);
            }
        };
        
        commissioniColumn = new Column<ChiusuraLioProxy, Number>(new NumberCell(MaliceUtil.getNumberCurrencyFormat())) {
            @Override
            public Number getValue(ChiusuraLioProxy object) {
                return object.getTotCommissioniEuroCent().movePointLeft(2);
            }
        };
        
        dataInvioColumn = new Column<ChiusuraLioProxy, Date>(new DateCell(MaliceUtil.getDayMonthYearFormat())) {
            @Override
            public Date getValue(ChiusuraLioProxy object) {
                return object.getDtInvio();
            }
        };
        
        statoColumn = new Column<ChiusuraLioProxy, StatoChiusura>(new StatoChiusuraImageResourceCell()) {
            @Override
            public StatoChiusura getValue(ChiusuraLioProxy object) {
                return StatoChiusura.fromString(object.getStatoChiusuraString());
            }
        };
        
        cellTable.addColumn(meseColumn, "MESE");
        cellTable.addColumn(numEstrattoConto, "NUM E/C");
        cellTable.addColumn(numTitoliColumn, "NUM TITOLI");
        cellTable.addColumn(premiColumn, "TOT PREMI");
        cellTable.addColumn(commissioniColumn, "TOT COMMISSIONI");
        cellTable.addColumn(dataInvioColumn, "DATA INVIO");
        cellTable.addColumn(statoColumn, "STATO");
        
        cellTable.setWidth("100%", true);
        statoColumn.setCellStyleNames(MaliceResources.INSTANCE.main().columnRightAlign());
        final double COLUMN_WIDTH = 14;
        final Style.Unit STYLE_UNIT = Unit.PCT;
        cellTable.setColumnWidth(meseColumn, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(numEstrattoConto, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(numTitoliColumn, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(premiColumn, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(commissioniColumn, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(dataInvioColumn, COLUMN_WIDTH, STYLE_UNIT);
        cellTable.setColumnWidth(statoColumn, 4, Unit.PCT);
        
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    public CellTable<ChiusuraLioProxy> getTabella() {
        return cellTable;
    }
    
    public HandlerRegistration addSelezioneChiusuraHandler(SelezioneChiusuraHandler handler) {
        return addHandler(handler, SelezioneChiusuraEvent.TYPE);
    }
}
