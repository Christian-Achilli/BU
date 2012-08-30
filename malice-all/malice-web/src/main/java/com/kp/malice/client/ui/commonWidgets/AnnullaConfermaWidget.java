package com.kp.malice.client.ui.commonWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.gwtEvent.AnnullaEvent;
import com.kp.malice.client.ui.gwtEvent.AnnullaHandler;
import com.kp.malice.client.ui.gwtEvent.ConfermaEvent;
import com.kp.malice.client.ui.gwtEvent.ConfermaEvent.ConfermaHandler;

public class AnnullaConfermaWidget extends Composite {

    private static AnnullaProseguiWidgetUiBinder uiBinder = GWT.create(AnnullaProseguiWidgetUiBinder.class);

    interface AnnullaProseguiWidgetUiBinder extends UiBinder<Widget, AnnullaConfermaWidget> {
    }

    @UiField
    Button annullaButton;

    @UiField
    Button proseguiButton;

    public AnnullaConfermaWidget() {
        super();
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("proseguiButton")
    void onClickConferma(ClickEvent e) {
        GWT.log("throw ConfermaEvent");
        fireEvent(new ConfermaEvent());
    }

    @UiHandler("annullaButton")
    void onClickAnnulla(ClickEvent e) {
        GWT.log("throw AnnullaEvent");
        fireEvent(new AnnullaEvent());
    }

    public HandlerRegistration addAnnullaHandler(AnnullaHandler handler) {
        return addHandler(handler, AnnullaEvent.TYPE);
    }

    public HandlerRegistration addConfermaHandler(ConfermaHandler handler) {
        return addHandler(handler, ConfermaEvent.TYPE);
    }
}
