package com.kp.malice.client.tabTitoli;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.commonWidgets.AnnullaConfermaWidget;
import com.kp.malice.client.ui.gwtEvent.AnnullaHandler;
import com.kp.malice.client.ui.gwtEvent.ConfermaEvent.ConfermaHandler;

public class TitoliToolbarInserimento extends Composite {

    private static ToolbarChiusuraDelMeseUiBinder uiBinder = GWT.create(ToolbarChiusuraDelMeseUiBinder.class);

    interface ToolbarChiusuraDelMeseUiBinder extends UiBinder<Widget, TitoliToolbarInserimento> {
    }

    @UiField
    AnnullaConfermaWidget annullaConfermaWidget;

    public TitoliToolbarInserimento() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public HandlerRegistration addAnnullaHandler(AnnullaHandler handler) {
        return annullaConfermaWidget.addAnnullaHandler(handler);
    }

    public HandlerRegistration addConfermaHandler(ConfermaHandler handler) {
        return annullaConfermaWidget.addConfermaHandler(handler);
    }
}
