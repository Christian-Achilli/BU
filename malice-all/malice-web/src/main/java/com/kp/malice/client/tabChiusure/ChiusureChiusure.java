package com.kp.malice.client.tabChiusure;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.kp.malice.client.ui.gwtEvent.ConfermaNotaEvent.ConfermaNotaHandler;
import com.kp.malice.client.ui.gwtEvent.DownloadHandler;
import com.kp.malice.client.ui.gwtEvent.SelezioneChiusuraEvent.SelezioneChiusuraHandler;
import com.kp.malice.client.ui.toolbar.ToolbarChiusureChiusure;
import com.kp.malice.shared.proxies.ChiusuraLioProxy;

public class ChiusureChiusure extends Composite {

    private static ChiusureChiusureUiBinder uiBinder = GWT.create(ChiusureChiusureUiBinder.class);

    interface ChiusureChiusureUiBinder extends UiBinder<Widget, ChiusureChiusure> {
    }
    
    @UiField
    ToolbarChiusureChiusure toolbarChiusureChiusure; 
 
    @UiField
    ChiusureListaChiusure chiusureListaChiusure;
    
    public ChiusureChiusure() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    public HandlerRegistration addConfermaNotaHandler(ConfermaNotaHandler handler) {
        return toolbarChiusureChiusure.addConfermaNotaHandler(handler);
    }

    public void init(ChiusuraLioProxy chiusuraLioProxy) {
        toolbarChiusureChiusure.init(chiusuraLioProxy);
    }

    public HasData<ChiusuraLioProxy> getTabella() {
        return chiusureListaChiusure.getTabella();
    }
    
    public HandlerRegistration addSelezioneChiusuraHandler(SelezioneChiusuraHandler handler) {
        return chiusureListaChiusure.addSelezioneChiusuraHandler(handler);
    }
    
    public HandlerRegistration addDownloadHandler(DownloadHandler handler) {
        return toolbarChiusureChiusure.addDownloadHandler(handler);
    }
}
