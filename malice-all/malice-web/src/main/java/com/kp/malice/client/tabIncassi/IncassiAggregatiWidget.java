package com.kp.malice.client.tabIncassi;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.gwtEvent.BackHandler;
import com.kp.malice.client.ui.gwtEvent.SelezioneIncassoHandler;
import com.kp.malice.client.ui.toolbar.ToolbarIncassiAggregati;
import com.kp.malice.shared.proxies.DettaglioIncassoTitoloProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;
import com.kp.malice.shared.proxies.ScritturaContabileProxy;

public class IncassiAggregatiWidget extends Composite {

    private static IncassiAggregatiUiBinder uiBinder = GWT.create(IncassiAggregatiUiBinder.class);

    interface IncassiAggregatiUiBinder extends UiBinder<Widget, IncassiAggregatiWidget> {
    }

    @UiField
    ToolbarIncassiAggregati toolbarIncassiAggregati;
    @UiField(provided=true)
    IncassiListaIncassiWidget listaIncassiAggregati;
    
    public IncassiAggregatiWidget() {
        listaIncassiAggregati = new IncassiListaIncassiWidget(15);
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    public HandlerRegistration addBackHandler(BackHandler handler) {
        return toolbarIncassiAggregati.addBackHandler(handler);
    }
    
    public HandlerRegistration addSelezioneIncassoHandler(SelezioneIncassoHandler handler) {
        return listaIncassiAggregati.addSelezioneIncassoHandler(handler);
    }

    public CellTable<DettaglioIncassoTitoloProxy> getTabella() {
        return listaIncassiAggregati.getTabella();
    }

    public void populate(ScritturaContabileProxy scritturaContabileProxy) {
        toolbarIncassiAggregati.populate(scritturaContabileProxy);
    }
}
