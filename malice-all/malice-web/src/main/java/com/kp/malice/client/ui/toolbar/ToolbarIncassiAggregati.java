package com.kp.malice.client.ui.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.gwtEvent.BackEvent;
import com.kp.malice.client.ui.gwtEvent.BackHandler;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.proxies.ScritturaContabileProxy;

public class ToolbarIncassiAggregati extends Composite {

    private static ToolbarScritturaContabileUiBinder uiBinder = GWT.create(ToolbarScritturaContabileUiBinder.class);

    interface ToolbarScritturaContabileUiBinder extends UiBinder<Widget, ToolbarIncassiAggregati> {
    }

    @UiField
    Label data;
    @UiField
    Label tipoOperazione;
    @UiField
    Label statoIncasso;
    @UiField
    Label mezzoPagamento;
    @UiField
    Button backButton;

    public ToolbarIncassiAggregati() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("backButton")
    void onBack(ClickEvent event) {
        GWT.log("ToolbarIncassiAggregati.onBack: catch ClickEvent");
        GWT.log("ToolbarIncassiAggregati.onBack: fire BackEvent");
        fireEvent(new BackEvent());
    }

    public void populate(ScritturaContabileProxy scritturaContabileProxy) {
        DateTimeFormat dtf = DateTimeFormat.getFormat("dd/MM/yyyy");
        String dataIncasso = MaliceUtil.formatDate(scritturaContabileProxy.getTmstInsRig(), dtf);
        data.setText(dataIncasso);
        tipoOperazione.setText(scritturaContabileProxy.getTipoOperazione());
        statoIncasso.setText(scritturaContabileProxy.getStatoIncasso());
        mezzoPagamento.setText(scritturaContabileProxy.getCodMzzPagm());
    }

    public HandlerRegistration addBackHandler(BackHandler handler) {
        return addHandler(handler, BackEvent.TYPE);
    }
}
