/**
 * 
 */
package com.kp.malice.client.ui.toolbar;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.MaliceSuggestBox;
import com.kp.malice.client.ui.gwtEvent.BackEvent;
import com.kp.malice.client.ui.gwtEvent.BackHandler;
import com.kp.malice.client.ui.gwtEvent.CreaTitoloEvent;
import com.kp.malice.client.ui.gwtEvent.CreaTitoloHandler;
import com.kp.malice.client.ui.gwtEvent.DoppioCalendarioValueChangeEvent.DoppioCalendarioValueChangeHandler;
import com.kp.malice.client.ui.gwtEvent.DownloadEvent;
import com.kp.malice.client.ui.gwtEvent.DownloadHandler;
import com.kp.malice.client.ui.gwtEvent.FiltraTitoliEvent;
import com.kp.malice.client.ui.gwtEvent.FiltraTitoliHandler;
import com.kp.malice.client.ui.gwtEvent.MaliceChangeEvent;
import com.kp.malice.client.ui.gwtEvent.RimuoviFiltroEvent;
import com.kp.malice.client.ui.gwtEvent.RimuoviFiltroHandler;
import com.kp.malice.client.ui.gwtEvent.VisualizeInsertTitleFormEvent;
import com.kp.malice.client.ui.gwtEvent.VisualizeInsertTitleFormHandler;
import com.kp.malice.client.ui.widget.DoppioCalendarioWidget;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.StatusTitolo;

/**
 * Mostra i comandi per filtrare i titoli sulla relativa tabella
 */
public class ToolbarRicercaTitoli extends Composite {

    private static ToolbarRicercaTitoliUiBinder uiBinder = GWT.create(ToolbarRicercaTitoliUiBinder.class);

    interface ToolbarRicercaTitoliUiBinder extends UiBinder<Widget, ToolbarRicercaTitoli> {
    }

    StatusTitolo statoTitolo;
    MultiWordSuggestOracle oracle;

    /**
     * N elementi della table
     */
    @UiField
    Label tolaleElementiTrovati;

    /**
     * Campo autocompletamento
     */
    @UiField(provided = true)
    MaliceSuggestBox suggestBox;

    /**
     * X per reset campo autocompletamento
     */
    @UiField
    Image xImg;

    /**
     * Widget con i calendari per le date d'intervallo di ricerca
     */
    @UiField(provided = true)
    DoppioCalendarioWidget calendari;

    @UiField
    Button downloadButton;
    @UiField
    Button addTitleButton;

    @SuppressWarnings("deprecation")
    public ToolbarRicercaTitoli() {
        calendari = new DoppioCalendarioWidget(null, MaliceUtil.getDayMonthYearFormat());
        // inizializzo il campo ad autocompletamento e la combo per
        // l'autocompletamento del campo filtro
        oracle = new MultiWordSuggestOracle();
        suggestBox = new MaliceSuggestBox(oracle);
        initWidget(uiBinder.createAndBindUi(this));
        inizializzaCalendariPlus10Minus30FromToday();
        addTitleButton.setVisible(false);
    }

    private void verificaCheckBox(CheckBox ch) {
        ch.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                fireFiltraTitoloEvent();
                GWT.log("clik della checkbox");
            }
        });
    }

    public void fireFiltraTitoloEvent() {
        GWT.log("ToolbarRicercaTitoli.fireFiltraTitoloEvent: launch a search");
        fireEvent(new FiltraTitoliEvent(suggestBox.getTextBox().getValue(), null, null));
    }

    @UiHandler("suggestBox")
    void onMaliceChangeEvent(MaliceChangeEvent event) {
        GWT.log("ToolbarRicercaTitoli.onValueChangeEvent: catch MaliceChangeEvent");
        fireFiltraTitoloEvent();
    }

    @UiHandler("addTitleButton")
    void onClickAddTitleButton(ClickEvent e) {
        GWT.log("ToolbarRicercaTitoli.onClickAddTitleButton: catch ClickEvent");
        GWT.log("ToolbarRicercaTitoli.onClickAddTitleButton: fire DownloadListaTitoliEvent");
        fireEvent(new CreaTitoloEvent());
    }

    @UiHandler("downloadButton")
    void onClickDownloadButton(ClickEvent e) {
        GWT.log("ToolbarRicercaTitoli.onClickDownloadButton: catch ClickEvent");
        GWT.log("ToolbarRicercaTitoli.onClickDownloadButton: fire DownloadListaTitoliEvent");
        fireEvent(new DownloadEvent());
    }

    @UiHandler("xImg")
    void onClick(ClickEvent e) {
        resetFiltro();
        GWT.log("throw RimuoviFiltriEvent");
        fireFiltraTitoloEvent();
    }

    private void inizializzaCalendariPlus10Minus30FromToday() {
        calendari.initPlus10Minus30FromToday();
    }

    public void setListaAutoCompletamento(List<String> listDistinct) {
        this.oracle.clear();
        for (String item : listDistinct) {
            this.oracle.add(item);
        }
    }

    public void populate(int size) {
        tolaleElementiTrovati.setText("" + size);
        if(size<=0)
        	downloadButton.setVisible(false);
        else
        	downloadButton.setVisible(true);
    }

    public String getChiave() {
        return suggestBox.getTextBox().getValue();
    }

    public HandlerRegistration addDoppioCalendarioValueChangeHandler(DoppioCalendarioValueChangeHandler handler) {
        return calendari.addDoppioCalendarioValueChangeHandler(handler);
    }

    public HandlerRegistration addRimuoviFiltroHandler(RimuoviFiltroHandler handler) {
        return addHandler(handler, RimuoviFiltroEvent.TYPE);
    }

    public HandlerRegistration addFiltraTitoliHandler(FiltraTitoliHandler handler) {
        return addHandler(handler, FiltraTitoliEvent.TYPE);
    }

    public HandlerRegistration addVisualizeInsertTitleFormHandler(VisualizeInsertTitleFormHandler handler) {
        return addHandler(handler, VisualizeInsertTitleFormEvent.TYPE);
    }

    public HandlerRegistration addBackEvent(BackHandler handler) {
        return addHandler(handler, BackEvent.TYPE);
    }

    public HandlerRegistration addDownloadListaTitoliEvent(DownloadHandler handler) {
        return addHandler(handler, DownloadEvent.TYPE);
    }

    public HandlerRegistration addCreaTitoloEvent(CreaTitoloHandler handler) {
        return addHandler(handler, CreaTitoloEvent.TYPE);
    }

    public void resetFiltro() {
        suggestBox.setText("");
    }
}
