package com.kp.malice.client.tabAgenzie;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.commonWidgets.ConfermaAnnullaNotaChiusureWidget;
import com.kp.malice.client.ui.gwtEvent.AnnullaEvent;
import com.kp.malice.client.ui.gwtEvent.ConfermaNotaEvent.ConfermaNotaHandler;
import com.kp.malice.client.ui.gwtEvent.DownloadEvent;
import com.kp.malice.client.ui.gwtEvent.DownloadHandler;
import com.kp.malice.shared.StatoChiusura;
import com.kp.malice.shared.proxies.ChiusuraLioProxy;

/**
 * Mostra i comandi per filtrare i titoli sulla relativa tabella
 */
public class AgenzieToolbar extends Composite {

    private static ToolbarChiusuraDelMeseUiBinder uiBinder = GWT.create(ToolbarChiusuraDelMeseUiBinder.class);

    interface ToolbarChiusuraDelMeseUiBinder extends UiBinder<Widget, AgenzieToolbar> {
    }
    
    @UiField
    Label tolaleElementiTrovati;
    
    public AgenzieToolbar() {
        initWidget(uiBinder.createAndBindUi(this));
    }

	public void setNumAgenzieTrovate(int size) {
		tolaleElementiTrovati.setText(""+size);
	}
}
