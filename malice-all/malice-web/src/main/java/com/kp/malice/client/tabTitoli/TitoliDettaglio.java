package com.kp.malice.client.tabTitoli;
    
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.kp.malice.client.ui.gwtEvent.AnnullaHandler;
import com.kp.malice.client.ui.gwtEvent.BackHandler;
import com.kp.malice.client.ui.gwtEvent.ConfermaNotaEvent.ConfermaNotaHandler;
import com.kp.malice.client.ui.gwtEvent.EditEvent;
import com.kp.malice.client.ui.gwtEvent.IncassoHandler;
import com.kp.malice.client.ui.gwtEvent.RecuperoTitoloHandler;
import com.kp.malice.client.ui.gwtEvent.RevocaAnnulloHandler;
import com.kp.malice.client.ui.gwtEvent.SaveDettaglioTitoloEvent;
import com.kp.malice.client.ui.gwtEvent.SaveDettaglioTitoloHandler;
import com.kp.malice.client.ui.gwtEvent.StornoHandler;
import com.kp.malice.client.ui.toolbar.ToolbarDettaglioTitolo;
import com.kp.malice.shared.proxies.DettaglioIncassoTitoloProxy;
import com.kp.malice.shared.proxies.IncassoTitoloProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class TitoliDettaglio extends Composite {

    private static DettaglioTitoloUiBinder uiBinder = GWT.create(DettaglioTitoloUiBinder.class);

    interface DettaglioTitoloUiBinder extends UiBinder<Widget, TitoliDettaglio> {
    }

    @UiField
    ToolbarDettaglioTitolo toolbarDettaglioTitolo;
    @UiField
    DettaglioTitoloWidget dettaglioTitoloWidget;

    public TitoliDettaglio() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void init(NewTitoloProxy titoloProxy) {
        toolbarDettaglioTitolo.populate(titoloProxy);
        dettaglioTitoloWidget.init(titoloProxy);
    };
    
    @UiHandler("toolbarDettaglioTitolo")
    void onEdit(EditEvent e) {
        GWT.log("DettaglioTitolo onEdit: catch onEdit = " + e.isEditable());
        if (!e.isEditable())
            dettaglioTitoloWidget.reset();
        dettaglioTitoloWidget.getSubAgente().setEditable(e.isEditable());
        dettaglioTitoloWidget.getCodCig().setEditable(e.isEditable());
    }
    
    @UiHandler("toolbarDettaglioTitolo")
    void onSave(SaveDettaglioTitoloEvent e) {
        GWT.log("DettaglioTitolo onSave: catch SaveEvent");
        if (!dettaglioTitoloWidget.getSubAgente().validateOrRegularExpression() || !dettaglioTitoloWidget.getCodCig().validateOrRegularExpression()) {
            e.kill(); //così che la view quando catcha a sua volta SaveDettaglioTitoloEvent non invochi il listener
        } else {
            e.setCodCig(dettaglioTitoloWidget.getCodCig().getInsertedText());
            e.setSubAgente(dettaglioTitoloWidget.getSubAgente().getInsertedText());
            onEdit(new EditEvent(false));
        }
    }

    //EVENT REGISTRATION
    public HandlerRegistration addBackHandler(BackHandler handler) {
        return toolbarDettaglioTitolo.addBackHandler(handler);
    }

    public HandlerRegistration addIncassoHandler(IncassoHandler handler) {
        return toolbarDettaglioTitolo.addIncassoHandler(handler);
    }

    public HandlerRegistration addAnnullaHandler(AnnullaHandler handler) {
        return toolbarDettaglioTitolo.addAnnullaHandler(handler);
    }

    public HandlerRegistration addConfermaNotaHandler(ConfermaNotaHandler handler) {
        return toolbarDettaglioTitolo.addConfermaNotaHandler(handler);
    }

    public HandlerRegistration addStornoHandler(StornoHandler handler) {
        return toolbarDettaglioTitolo.addStornoHandler(handler);
    }

    public HandlerRegistration addRecuperoHandler(RecuperoTitoloHandler handler) {
        return toolbarDettaglioTitolo.addRecuperoHandler(handler);
    }

    public HandlerRegistration addSaveDettaglioTitoloHandler(SaveDettaglioTitoloHandler handler) {
        return toolbarDettaglioTitolo.addSaveDettaglioTitoloHandler(handler);
    }

    public HandlerRegistration addRevocaAnnulloHandler(RevocaAnnulloHandler handler) {
        return toolbarDettaglioTitolo.addRevocaAnnulloHandler(handler);
    }

    public HasData<IncassoTitoloProxy> getTabellaIncassi() {
        return dettaglioTitoloWidget.getTabellaIncassi();
    }
}
