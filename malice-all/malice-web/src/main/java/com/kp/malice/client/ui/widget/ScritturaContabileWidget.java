package com.kp.malice.client.ui.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.gwtEvent.CalendarioValueChangeEvent.CalendarioValueChangeHandler;
import com.kp.malice.client.ui.gwtEvent.DownloadHandler;
import com.kp.malice.client.ui.gwtEvent.SelezioneScritturaContabileHandler;
import com.kp.malice.client.ui.toolbar.ToolbarScritturaContabile;
import com.kp.malice.shared.proxies.ScritturaContabileProxy;

public class ScritturaContabileWidget extends Composite {

    private static ScritturaContabileUiBinder uiBinder = GWT.create(ScritturaContabileUiBinder.class);

    interface ScritturaContabileUiBinder extends UiBinder<Widget, ScritturaContabileWidget> {
    }

    @UiField
    ToolbarScritturaContabile toolbarScritturaContabile;
    @UiField
    ListaScritturaContabileWidget listaScritturaContabile;
    @UiField
    Label msgIncassiZero;

    public ScritturaContabileWidget() {
        initWidget(uiBinder.createAndBindUi(this));
        msgIncassiZero.setText("Nessun incasso presente.");
        toolbarScritturaContabile.visualizzaBottoneDownload();
    }

    public void visualizzaListaScrittureContabili() {
        msgIncassiZero.setVisible(false);
        listaScritturaContabile.setVisible(true);
        toolbarScritturaContabile.visualizzaBottoneDownload();
    }

    public void visualizzaMsgNoIncassi() {
        listaScritturaContabile.setVisible(false);
        msgIncassiZero.setVisible(true);
        toolbarScritturaContabile.nascondiBottoneDownload();
    }

    public HandlerRegistration addToolbarScritturaContabileHandler(CalendarioValueChangeHandler handler) {
        return toolbarScritturaContabile.addCalendarioValueChangeHandler(handler);
    }

    public HandlerRegistration addSelezioneScritturaContabileHandler(SelezioneScritturaContabileHandler handler) {
        return listaScritturaContabile.addSelezioneScritturaContabileHandler(handler);
    }

    public HandlerRegistration addDownloadHandler(DownloadHandler handler) {
        return toolbarScritturaContabile.addDownloadHandler(handler);
    }

    public void visualizzaErrore(String error) {
        toolbarScritturaContabile.visualizzaErrore(error);
    }

    public CellTable<ScritturaContabileProxy> getTabella() {
        return listaScritturaContabile.getTabella();
    }

    public void setComparatorsAndSortHandler(ListHandler<ScritturaContabileProxy> sortHandler) {
        listaScritturaContabile.setComparatorsAndSortHandler(sortHandler);
    }
}
