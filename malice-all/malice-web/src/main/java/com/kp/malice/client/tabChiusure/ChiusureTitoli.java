package com.kp.malice.client.tabChiusure;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.commonWidgets.ListaTitoliWidget;
import com.kp.malice.client.ui.gwtEvent.BackHandler;
import com.kp.malice.client.ui.gwtEvent.DownloadHandler;
import com.kp.malice.client.ui.gwtEvent.SelezioneTitoloHandler;
import com.kp.malice.shared.proxies.EstrattoContoLioProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class ChiusureTitoli extends Composite {

    private static ChiusureTitoliUiBinder uiBinder = GWT.create(ChiusureTitoliUiBinder.class);

    interface ChiusureTitoliUiBinder extends UiBinder<Widget, ChiusureTitoli> {
    }

    public ChiusureTitoli() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    ChiusureToolbarTitoli toolbarChiusureTitoli;

    @UiField
    ListaTitoliWidget listaTitoliWidget;
    
    public CellTable<NewTitoloProxy> getTabella() {
        return listaTitoliWidget.getTabella();
    }

    public void init(EstrattoContoLioProxy estrattoContoLioProxySelezionato, String meseChiusura) {
        toolbarChiusureTitoli.init(estrattoContoLioProxySelezionato, meseChiusura);
        listaTitoliWidget.disabilitaDidascaliaFiltro();
    }
    
    public HandlerRegistration addBackHandler(BackHandler handler) {
        return toolbarChiusureTitoli.addBackHandler(handler);
    }

    public HandlerRegistration addDownloadHandler(DownloadHandler handler) {
        return toolbarChiusureTitoli.addDownloadHandler(handler);
    }
    
    public HandlerRegistration addSelezioneTitoloHandler(SelezioneTitoloHandler handler) {
        return listaTitoliWidget.addSelezioneTitoloHandler(handler);
    }
}
