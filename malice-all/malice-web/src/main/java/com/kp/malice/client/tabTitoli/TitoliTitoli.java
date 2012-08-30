package com.kp.malice.client.tabTitoli;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.commonWidgets.ListaTitoliWidget;
import com.kp.malice.client.ui.gwtEvent.AggiornaStatoTitoloERimessaHandler;
import com.kp.malice.client.ui.gwtEvent.BackHandler;
import com.kp.malice.client.ui.gwtEvent.CalendarioValueChangeEvent.CalendarioValueChangeHandler;
import com.kp.malice.client.ui.gwtEvent.CreaTitoloHandler;
import com.kp.malice.client.ui.gwtEvent.DoppioCalendarioValueChangeEvent.DoppioCalendarioValueChangeHandler;
import com.kp.malice.client.ui.gwtEvent.DownloadHandler;
import com.kp.malice.client.ui.gwtEvent.FiltraTitoliEvent;
import com.kp.malice.client.ui.gwtEvent.FiltraTitoliHandler;
import com.kp.malice.client.ui.gwtEvent.RimuoviFiltroHandler;
import com.kp.malice.client.ui.gwtEvent.SelezioneTitoloHandler;
import com.kp.malice.client.ui.gwtEvent.VisualizeInsertTitleFormHandler;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class TitoliTitoli extends Composite {

    private static ListaTitoliWidgetPerTitoliUiBinder uiBinder = GWT.create(ListaTitoliWidgetPerTitoliUiBinder.class);

    interface ListaTitoliWidgetPerTitoliUiBinder extends UiBinder<Widget, TitoliTitoli> {
    }

    @UiField
    TitoliToolbarRicerca toolbarRicercaTitoli;

    @UiField
    ListaTitoliWidget listaTitoliWidget;

    public TitoliTitoli() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("toolbarRicercaTitoli")
    public void onFiltraTitoliFromToolbarRicercaTitoli(FiltraTitoliEvent event) {
        GWT.log("TitoliTitoli.onFiltraTitoliFromToolbarRicercaTitoli: catch FiltraTitoliEvent");
        event.setListaStatusTitolo(listaTitoliWidget.getListaStatiSelezionati());
        event.setSospesi(listaTitoliWidget.isSospesi());
        GWT.log("TitoliTitoli.onFiltraTitoliFromToolbarRicercaTitoli: fire FiltraTitoliEvent");
        fireEvent(event);
    }

    @UiHandler("listaTitoliWidget")
    public void onFiltraTitoliFromListaTitoliWidget(FiltraTitoliEvent event) {
        GWT.log("TitoliTitoli.onFiltraTitoliFromListaTitoliWidget: catchee FiltraTitoliEvent");
        event.setChiave(toolbarRicercaTitoli.getChiave());
        GWT.log("TitoliTitoli.onFiltraTitoliFromListaTitoliWidget: fire FiltraTitoliEvent");
        fireEvent(event);
    }

    public void setComparatorsAndSortHandler(ListHandler<NewTitoloProxy> sortHandler) {
        listaTitoliWidget.setComparatorsAndSortHandler(sortHandler);
    }

    public void setTitoliTrovati(int size) {
        toolbarRicercaTitoli.populate(size);
    }

    public CellTable<NewTitoloProxy> getTabella() {
        return listaTitoliWidget.getTabella();
    }

    //    public HandlerRegistration addFiltraTitoliHandlerByListaTitoli(FiltraTitoliHandler handler) {
    //        return listaTitoliWidget.addFiltraTitoliHandler(handler);
    //    }

    public HandlerRegistration addSelezioneTitoloHandler(SelezioneTitoloHandler handler) {
        return listaTitoliWidget.addSelezioneTitoloHandler(handler);
    }

    public HandlerRegistration addAggiornaStatoTitoloERimessaHandler(AggiornaStatoTitoloERimessaHandler handler) {
        return listaTitoliWidget.addAggiornaStatoTitoloERimessaHandler(handler);
    }

    public HandlerRegistration addCalendarioValueChangeHandler(DoppioCalendarioValueChangeHandler handler) {
        return toolbarRicercaTitoli.addDoppioCalendarioValueChangeHandler(handler);
    }

    public HandlerRegistration addRimuoviFiltroHandler(RimuoviFiltroHandler handler) {
        return toolbarRicercaTitoli.addRimuoviFiltroHandler(handler);
    }

    public HandlerRegistration addFiltraTitoliHandler(FiltraTitoliHandler handler) {
        //        return toolbarRicercaTitoli.addFiltraTitoliHandler(handler);
        return addHandler(handler, FiltraTitoliEvent.TYPE);
    }

    public HandlerRegistration addVisualizeInsertTitleFormHandler(VisualizeInsertTitleFormHandler handler) {
        return toolbarRicercaTitoli.addVisualizeInsertTitleFormHandler(handler);
    }

    public HandlerRegistration addBackEvent(BackHandler handler) {
        return toolbarRicercaTitoli.addBackEvent(handler);
    }

    public HandlerRegistration addDownloadListaTitoliEvent(DownloadHandler handler) {
        return toolbarRicercaTitoli.addDownloadListaTitoliEvent(handler);
    }

    public HandlerRegistration addCreaTitoloEvent(CreaTitoloHandler handler) {
        return toolbarRicercaTitoli.addCreaTitoloEvent(handler);
    }

    public void resetFiltri() {
        toolbarRicercaTitoli.resetFiltro();
        listaTitoliWidget.resetFiltri();
    }

}
