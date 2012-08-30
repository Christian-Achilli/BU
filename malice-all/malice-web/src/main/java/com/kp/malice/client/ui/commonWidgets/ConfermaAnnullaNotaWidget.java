package com.kp.malice.client.ui.commonWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.gwtEvent.AnnullaHandler;
import com.kp.malice.client.ui.gwtEvent.ConfermaEvent;
import com.kp.malice.client.ui.gwtEvent.ConfermaNotaEvent;
import com.kp.malice.client.ui.gwtEvent.ConfermaNotaEvent.ConfermaNotaHandler;

public class ConfermaAnnullaNotaWidget extends Composite {
    private static ConfermaAnnullaNotaUiBinder uiBinder = GWT.create(ConfermaAnnullaNotaUiBinder.class);

    interface ConfermaAnnullaNotaUiBinder extends UiBinder<Widget, ConfermaAnnullaNotaWidget> {
    }

    @UiField
    TextArea note;

    @UiField
    AnnullaConfermaWidget annullaConfermaWidget;

    public ConfermaAnnullaNotaWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("annullaConfermaWidget")
    void onConferma(ConfermaEvent event) {
        GWT.log("ConfermaAnnullaNota onAnnulla: catch ConfermaEvent");
        GWT.log("ConfermaAnnullaNota onAnnulla: fire ConfermaNotaEvent");
        fireEvent(new ConfermaNotaEvent(note.getText()));
    }

    public HandlerRegistration addConfermaNotaHandler(ConfermaNotaHandler handler) {
        return addHandler(handler, ConfermaNotaEvent.TYPE);
    }

    public HandlerRegistration addAnnullaHandler(AnnullaHandler handler) {
        return annullaConfermaWidget.addAnnullaHandler(handler);
    }

    public void init() {
        note.setText("");
    }
}
