package com.kp.malice.client.tabChiusure;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.gwtEvent.BackHandler;
import com.kp.malice.client.ui.gwtEvent.DownloadHandler;
import com.kp.malice.client.ui.gwtEvent.SelezioneEstrattoContoEvent.SelezioneEstrattoContoHandler;
import com.kp.malice.client.ui.toolbar.ToolbarChiusureEstrattiConto;
import com.kp.malice.shared.proxies.ChiusuraLioProxy;
import com.kp.malice.shared.proxies.EstrattoContoLioProxy;

public class ChiusureEstrattiConto extends Composite {

    private static ChiusureEstrattiContoUiBinder uiBinder = GWT.create(ChiusureEstrattiContoUiBinder.class);

    interface ChiusureEstrattiContoUiBinder extends UiBinder<Widget, ChiusureEstrattiConto> {
    }

    @UiField
    ToolbarChiusureEstrattiConto toolbarChiusureEstrattiConto;

    @UiField
    ChiusureListaEstrattiConto chiusureListaEstrattiConto;

    public ChiusureEstrattiConto() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public CellTable<EstrattoContoLioProxy> getTabella() {
        return chiusureListaEstrattiConto.getTabella();
    }

    public void init(ChiusuraLioProxy chiusuraProxy) {
        toolbarChiusureEstrattiConto.init(chiusuraProxy);
    }

    public HandlerRegistration addDownloadHandler(DownloadHandler handler) {
        return toolbarChiusureEstrattiConto.addDownloadHandler(handler);
    }

    public HandlerRegistration addBackHandler(BackHandler handler) {
        return toolbarChiusureEstrattiConto.addBackHandler(handler);
    }

    public HandlerRegistration addSelezioneEstrattoContoHandler(SelezioneEstrattoContoHandler handler) {
        return chiusureListaEstrattiConto.addSelezioneEstrattoContoHandler(handler);
    }
}
