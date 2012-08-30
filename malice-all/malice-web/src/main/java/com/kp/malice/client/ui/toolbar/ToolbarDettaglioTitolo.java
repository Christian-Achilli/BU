package com.kp.malice.client.ui.toolbar;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.MaliceDebugIds;
import com.kp.malice.client.ui.UIMaliceUtil;
import com.kp.malice.client.ui.gwtEvent.AnnullaEvent;
import com.kp.malice.client.ui.gwtEvent.AnnullaHandler;
import com.kp.malice.client.ui.gwtEvent.AnnullaTitoloHandler;
import com.kp.malice.client.ui.gwtEvent.BackEvent;
import com.kp.malice.client.ui.gwtEvent.BackHandler;
import com.kp.malice.client.ui.gwtEvent.ConfermaEvent;
import com.kp.malice.client.ui.gwtEvent.ConfermaNotaEvent.ConfermaNotaHandler;
import com.kp.malice.client.ui.gwtEvent.EditEvent;
import com.kp.malice.client.ui.gwtEvent.EditHandler;
import com.kp.malice.client.ui.gwtEvent.IncassoEvent;
import com.kp.malice.client.ui.gwtEvent.IncassoHandler;
import com.kp.malice.client.ui.gwtEvent.MaliceChangeEvent;
import com.kp.malice.client.ui.gwtEvent.MaliceChangeHandler;
import com.kp.malice.client.ui.gwtEvent.RecuperoTitoloHandler;
import com.kp.malice.client.ui.gwtEvent.RevocaAnnulloHandler;
import com.kp.malice.client.ui.gwtEvent.SaveDettaglioTitoloEvent;
import com.kp.malice.client.ui.gwtEvent.SaveDettaglioTitoloHandler;
import com.kp.malice.client.ui.gwtEvent.StornoEvent;
import com.kp.malice.client.ui.gwtEvent.StornoHandler;
import com.kp.malice.client.ui.widget.AnnullaConfermaWidget;
import com.kp.malice.client.ui.widget.ConfermaAnnullaNotaWidget;
import com.kp.malice.client.ui.widget.IncassoForm;
import com.kp.malice.client.ui.widget.RecuperoWidget;
import com.kp.malice.client.ui.widget.RevocaAnnulloWidget;
import com.kp.malice.client.ui.widget.StornaTitoloWidget;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.proxies.FunzioniAbilitateProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;

/**
 * Mostrato nella toolbar in visione del dettaglio titolo
 */
public class ToolbarDettaglioTitolo extends Composite {

    private static ToolbarDettaglioTitoloUiBinder uiBinder = GWT.create(ToolbarDettaglioTitoloUiBinder.class);

    interface ToolbarDettaglioTitoloUiBinder extends UiBinder<Widget, ToolbarDettaglioTitolo> {
    }

    @UiField
    Label numeroTitolo;
    @UiField
    AnnullaConfermaWidget annullaConfermaWidgetEdit;
    @UiField
    Button buttonEdit;
    @UiField
    Button buttonIndietro;

    //TITLE OPERATION BUTTON
    @UiField
    Button buttonIncasso;
    @UiField
    Button buttonAnnulla;
    @UiField
    Button buttonStorno;
    @UiField
    Button buttonRecupero;
    @UiField
    Button buttonRevocaAnnullo;
    //TITLE OPERATION DIV
    @UiField
    DivElement divIncasso;
    @UiField
    DivElement divAnnulla;
    @UiField
    DivElement divStorno;
    @UiField
    DivElement divRecupero;
    @UiField
    DivElement divRevocaAnnullo;

    @UiField
    DivElement divSeparatore;
    @UiField
    DivElement divSeparatoreUltimoDestra;
    @UiField
    DivElement rightButtonsContainer;
    @UiField
    DivElement buttonsTitleOperation;

    @UiField
    IncassoForm incassoForm;

    @UiField
    RecuperoWidget recuperoWidget;

    @UiField
    RevocaAnnulloWidget revocaAnnulloWidget;

    @UiField
    ConfermaAnnullaNotaWidget annullaTitoloWidget;

    @UiField
    StornaTitoloWidget stornaTitoloWidget;

    private FunzioniAbilitateProxy funzioniAbilitateProxy;

    public ToolbarDettaglioTitolo() {
        initWidget(uiBinder.createAndBindUi(this));
        nascondiTutto();
        buttonIncasso.ensureDebugId(MaliceDebugIds.BTN_DTT_TTL_INCASSO);
        buttonStorno.ensureDebugId(MaliceDebugIds.BTN_DTT_TTL_STORNO);
        buttonIndietro.ensureDebugId(MaliceDebugIds.BTN_DTT_TTL_INDIETRO);
        buttonEdit.ensureDebugId(MaliceDebugIds.BTN_DTT_TTL_EDIT);
    }

    /* INCASSO */
    @UiHandler("buttonIncasso")
    void onClickIncasso(ClickEvent e) {
        nascondiTutto();
        incassoForm.setVisible(true);
    }

    @UiHandler("incassoForm")
    void onAnnullaIncasso(AnnullaEvent e) {
        GWT.log("catch annullaEvent");
        incassoForm.init();
        resetToolbar();
        incassoForm.setVisible(false);
    }

    @UiHandler("incassoForm")
    void onConfermaWidgetIncasso(IncassoEvent e) {
        GWT.log("catch ConfermaEvent from annullaConfermaWidgetIncasso");
        GWT.log("fire IncassoEvent from ToolbarDettaglioTitolo");
        fireEvent(e);
    }

    /* ANNULLA */
    @UiHandler("buttonAnnulla")
    void onClickAnnulla(ClickEvent e) {
        GWT.log("fire AnnullaEvent");
        nascondiTutto();
        annullaTitoloWidget.setVisible(true);
    }

    @UiHandler("annullaTitoloWidget")
    void onAnnullaAnnulla(AnnullaEvent e) {
        GWT.log("ToolbarDettaglioTitolo onAnnullaAnnulla: catch annullaEvent");
        resetToolbar();
        annullaTitoloWidget.setVisible(false);
    }

    /* STORNO */
    @UiHandler("buttonStorno")
    void onClickStorno(ClickEvent e) {
        GWT.log("fire StornoEvent");
        nascondiTutto();
        stornaTitoloWidget.setVisible(true);
    }

    @UiHandler("stornaTitoloWidget")
    void onAnnullaStorno(AnnullaEvent e) {
        GWT.log("catch annullaEvent");
        resetToolbar();
    }

    @UiHandler("stornaTitoloWidget")
    void onConfermatStorno(ConfermaEvent e) {
        GWT.log("ToolbarDettaglioTitolo.onConfermatStorno: catch ConfermaEvent");
        GWT.log("ToolbarDettaglioTitolo.onConfermatStorno: fire StornoEvent");
        fireEvent(new StornoEvent());
    }

    /* DELEGA */
    //    @UiHandler("buttonDelega")
    //    void onClickDelega(ClickEvent e) {
    //        GWT.log("fire DelegaEvent");
    //        fireEvent(new DelegaEvent());
    //        Window.alert("Da implementare.");
    //    }

    /* REVOCA */
    @UiHandler("buttonRevocaAnnullo")
    void onClickRevocaAnnullo(ClickEvent e) {
        GWT.log("ToolbarDettaglioTitolo.onClickRevocaAnnullo: catch ClickEvent");
        nascondiTutto();
        revocaAnnulloWidget.setVisible(true);
    }

    @UiHandler("revocaAnnulloWidget")
    void onAnnullaRevocaAnnullo(AnnullaEvent e) {
        GWT.log("ToolbarDettalgioTitolo.onAnnullaRevocaAnnullo: catch annullaEvent");
        resetToolbar();
    }

    /* RECUPERO */
    @UiHandler("buttonRecupero")
    void onClickRecupero(ClickEvent e) {
        GWT.log("onClickRecupero");
        nascondiTutto();
        recuperoWidget.setVisible(true);
    }

    @UiHandler("recuperoWidget")
    void onAnnullaRecupero(AnnullaEvent e) {
        GWT.log("ToolbarDettaglio.onAnnullaRecuperoe: catch onAnnulla");
        resetToolbar();
    }

    /* EDIT */
    @UiHandler("buttonEdit")
    void onClickEdit(ClickEvent e) {
        GWT.log("fire EditEvent from toolbarDettaglioTitolo");
        fireEvent(new EditEvent(true));
        nascondiTutto();
        UIMaliceUtil.visualizza(rightButtonsContainer);
        UIMaliceUtil.visualizza(divSeparatore);
        annullaConfermaWidgetEdit.setVisible(true);
    }

    @UiHandler("annullaConfermaWidgetEdit")
    void onConferma(ConfermaEvent e) {
        GWT.log("DettaglioTitoloEditabile: catch ConfermaEvent");
        GWT.log("DettaglioTitoloEditabile: fire SaveEvent");
        fireEvent(new SaveDettaglioTitoloEvent());
    }

    @UiHandler("annullaConfermaWidgetEdit")
    void onAnnulla(AnnullaEvent e) {
        GWT.log("DettaglioTitoloEditabile: catch onAnnulla");
        GWT.log("DettaglioTitoloEditabile: fire EditEvent");
        fireEvent(new EditEvent(false));
        resetToolbar();
    }

    /* INDIETRO */
    @UiHandler("buttonIndietro")
    void onClickIndietro(ClickEvent e) {
        GWT.log("fire BackEvent from toolbarDettaglioTitolo");
        fireEvent(new BackEvent());
        nascondiTutto();
    }

    public void resetToolbar() {
        nascondiTutto();
        resettaTuttiWidgetDivDiscesa();
        visualizzaButtons();
    }

    private void resettaTuttiWidgetDivDiscesa() {
        incassoForm.init();
        annullaTitoloWidget.init();
        recuperoWidget.init();
    }

    public void nascondiTutto() {
        UIMaliceUtil.nascondi(buttonsTitleOperation);
        UIMaliceUtil.nascondi(buttonEdit);
        UIMaliceUtil.nascondi(buttonIndietro);
        annullaConfermaWidgetEdit.setVisible(false);
        incassoForm.setVisible(false);
        annullaTitoloWidget.setVisible(false);
        recuperoWidget.setVisible(false);
        stornaTitoloWidget.setVisible(false);
        revocaAnnulloWidget.setVisible(false);
        UIMaliceUtil.nascondi(divSeparatore);
        UIMaliceUtil.nascondi(rightButtonsContainer);
    }

    private void visualizzaButtons() {
        UIMaliceUtil.visualizza(buttonsTitleOperation);
        visualizzaNascondiDivElement(funzioniAbilitateProxy.isIncassoTitolo(), divIncasso);
        visualizzaNascondiDivElement(funzioniAbilitateProxy.isAnnulloTitolo(), divAnnulla);
        visualizzaNascondiDivElement(funzioniAbilitateProxy.isRevocaAnnulloTitolo(), divRevocaAnnullo);
        visualizzaNascondiDivElement(funzioniAbilitateProxy.isStornoIncasso(), divStorno);
        visualizzaNascondiDivElement(funzioniAbilitateProxy.isRecuperoSospeso(), divRecupero);
        buttonEdit.setVisible(funzioniAbilitateProxy.isModificaTitolo());
        buttonIndietro.setVisible(true);
        UIMaliceUtil.visualizza(divSeparatore);
        UIMaliceUtil.visualizza(rightButtonsContainer);
        buttonEdit.setVisible(true);//TODO eliminare (da considerarsi solo temporaneo per demo)
    }

    private void visualizzaNascondiDivElement(boolean isVisualizable, DivElement element) {
        if (isVisualizable)
            UIMaliceUtil.visualizza(element);
        else
            UIMaliceUtil.nascondi(element);
    }

    public void populate(NewTitoloProxy titoloProxy) {
        funzioniAbilitateProxy = titoloProxy.getFunzioniAbilitate();
        resetToolbar();
        setNumeroTitolo(titoloProxy.getCertificatoLloyds().getNumero() + "-" + titoloProxy.getProgressivo());
        BigDecimal cent = titoloProxy.getNetEuroCent().add(titoloProxy.getAccessoriEuroCent())
                .add(titoloProxy.getTaxesEuroCent());
        recuperoWidget.setDataRegistrazione(new Date());
        setImportoTitolo(cent.movePointLeft(2)); //per poter calcolare l'abbuono
        if (titoloProxy.getFunzioniAbilitate().isStornoIncasso())
            stornaTitoloWidget.populate(titoloProxy.getUltimoIncassoCheHaMessoIlTitoloInStatoIncassato());
    }

    public void setNumeroTitolo(String codiceTit) {
        this.numeroTitolo.setText(codiceTit);
    }

    public void setImportoTitolo(BigDecimal bigDecimal) {
        incassoForm.populate(bigDecimal);
        recuperoWidget.populate(bigDecimal);
    }

    //REGISTRO EVENTI
    public HandlerRegistration addBackHandler(BackHandler handler) {
        return addHandler(handler, BackEvent.TYPE);
    }

    public HandlerRegistration addIncassoHandler(IncassoHandler handler) {
        return addHandler(handler, IncassoEvent.TYPE);
    }

    public HandlerRegistration addAnnullaHandler(AnnullaHandler handler) {
        return addHandler(handler, AnnullaEvent.TYPE);
    }

    public HandlerRegistration addStornoHandler(StornoHandler handler) {
        return addHandler(handler, StornoEvent.TYPE);
    }

    public HandlerRegistration addEditHandler(EditHandler handler) {
        return addHandler(handler, EditEvent.TYPE);
    }

    public HandlerRegistration addSaveDettaglioTitoloHandler(SaveDettaglioTitoloHandler handler) {
        return addHandler(handler, SaveDettaglioTitoloEvent.TYPE);
    }

    public HandlerRegistration addRecuperoHandler(RecuperoTitoloHandler handler) {
        return recuperoWidget.addRecuperoHandler(handler);
    }

    public HandlerRegistration addRevocaAnnulloHandler(RevocaAnnulloHandler handler) {
        return revocaAnnulloWidget.addRevocaAnnulloHandler(handler);
    }

    public HandlerRegistration addConfermaNotaHandler(ConfermaNotaHandler handler) {
        return annullaTitoloWidget.addConfermaNotaHandler(handler);
    }

    public HandlerRegistration addMaliceChangeHandler(MaliceChangeHandler handler) {
        return addHandler(handler, MaliceChangeEvent.TYPE);
    }
}
