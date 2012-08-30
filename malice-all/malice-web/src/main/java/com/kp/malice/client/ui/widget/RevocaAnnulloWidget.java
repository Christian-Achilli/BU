package com.kp.malice.client.ui.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.gwtEvent.AnnullaHandler;
import com.kp.malice.client.ui.gwtEvent.ConfermaEvent;
import com.kp.malice.client.ui.gwtEvent.RevocaAnnulloEvent;
import com.kp.malice.client.ui.gwtEvent.RevocaAnnulloHandler;

public class RevocaAnnulloWidget extends Composite {

    private static RevocaAnnulloWidgetUiBinder uiBinder = GWT.create(RevocaAnnulloWidgetUiBinder.class);

    interface RevocaAnnulloWidgetUiBinder extends UiBinder<Widget, RevocaAnnulloWidget> {
    }
    @UiField
    AnnullaConfermaWidget annullaConfermaWidget;
    
    public RevocaAnnulloWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @UiHandler("annullaConfermaWidget")
    void onConferma(ConfermaEvent e) {
        GWT.log("RevocaAnnulloWidget.onConferma: catch ConfermaEvent");
        GWT.log("RevocaAnnulloWidget.onConferma: fire RevocaAnnulloEvent");
        fireEvent(new RevocaAnnulloEvent());
    }
    
    public HandlerRegistration addAnnullaHandler(AnnullaHandler handler) {
        return annullaConfermaWidget.addAnnullaHandler(handler);
    }
    
    public HandlerRegistration addRevocaAnnulloHandler(RevocaAnnulloHandler handler) {
        return addHandler(handler, RevocaAnnulloEvent.TYPE);
    }
}
