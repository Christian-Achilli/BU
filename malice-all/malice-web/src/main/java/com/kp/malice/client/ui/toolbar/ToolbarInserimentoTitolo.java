package com.kp.malice.client.ui.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.gwtEvent.AnnullaHandler;
import com.kp.malice.client.ui.gwtEvent.ConfermaEvent.ConfermaHandler;
import com.kp.malice.client.ui.widget.AnnullaConfermaWidget;

public class ToolbarInserimentoTitolo extends Composite {

    private static ToolbarChiusuraDelMeseUiBinder uiBinder = GWT.create(ToolbarChiusuraDelMeseUiBinder.class);

    interface ToolbarChiusuraDelMeseUiBinder extends UiBinder<Widget, ToolbarInserimentoTitolo> {
    }

    @UiField
    AnnullaConfermaWidget annullaConfermaWidget;

    public ToolbarInserimentoTitolo() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public HandlerRegistration addAnnullaHandler(AnnullaHandler handler) {
        return annullaConfermaWidget.addAnnullaHandler(handler);
    }

    public HandlerRegistration addConfermaHandler(ConfermaHandler handler) {
        return annullaConfermaWidget.addConfermaHandler(handler);
    }
}
