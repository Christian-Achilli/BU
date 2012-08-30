package com.kp.malice.client.tabTitoli;

import java.math.BigDecimal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.kp.malice.client.ui.commonWidgets.EuroTextBox;
import com.kp.malice.client.ui.commonWidgets.PercentualeToEuroWidget;
import com.kp.malice.client.ui.gwtEvent.AnnullaEvent;
import com.kp.malice.client.ui.gwtEvent.AnnullaHandler;
import com.kp.malice.client.ui.gwtEvent.ConfermaEvent;
import com.kp.malice.client.ui.gwtEvent.CreaTitoloEvent;
import com.kp.malice.client.ui.gwtEvent.CreaTitoloHandler;
import com.kp.malice.client.ui.gwtEvent.InserisciTitoloEvent;
import com.kp.malice.client.ui.gwtEvent.InserisciTitoloHandler;
import com.kp.malice.client.ui.gwtEvent.MaliceChangeEvent;
import com.kp.malice.client.ui.gwtEvent.VisualizzaListaTitoliEvent;
import com.kp.malice.client.ui.gwtEvent.VisualizzaListaTitoliHandler;
import com.kp.malice.shared.Gender;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.proxies.CertificatoLloydsProxy;
import com.kp.malice.shared.proxies.ContraenteProxy;
import com.kp.malice.shared.proxies.FilieraLloydsOpenMarketProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class TitoliInserimento extends Composite {

    private static TitoliInserimentoUiBinder uiBinder = GWT.create(TitoliInserimentoUiBinder.class);

    interface TitoliInserimentoUiBinder extends UiBinder<Widget, TitoliInserimento> {
    }

    NewTitoloProxy titoloProxy;

    @UiField
    TitoliToolbarInserimento toolbarInserimentoTitolo;

    @UiField
    ListBox codiceRamo;
    @UiField
    ListBox codiceLb;
    @UiField
    ListBox codiceDoc;
    //TODO se codice Doc è F allora è covernote, se B binder
    @UiField
    ListBox tipoContraente;
    @UiField
    TextBox numeroBinderCovernote;
    @UiField
    TextBox companyName;
    @UiField
    TextBox numeroPolizza;
    @UiField
    TextBox nomeContraente;
    @UiField
    TextBox cognomeContraente;
    @UiField
    TextBox viaContraente;
    @UiField
    TextBox capContraente;
    @UiField
    TextBox cittaContraente;
    @UiField
    TextBox codiceFiscale;
    @UiField
    TextBox vatNumber;
    @UiField
    EuroTextBox premioNettoComplessivo;
    @UiField
    EuroTextBox accessoriComplessivo;
    @UiField
    EuroTextBox imponibileComplessivo;
    @UiField
    EuroTextBox imposteComplessivo;
    @UiField
    EuroTextBox premioLordoComplessivo;
    @UiField
    PercentualeToEuroWidget commissioniNetteComplessive;
    @UiField
    PercentualeToEuroWidget commissioniAccessorieComplessive;
    @UiField
    EuroTextBox commissioniTotaliComplessive;
    @UiField
    EuroTextBox premioNettoFrazionato;
    @UiField
    EuroTextBox accessoriFrazionato;
    @UiField
    EuroTextBox imponibileFrazionato;
    @UiField
    EuroTextBox imposteFrazionato;
    @UiField
    EuroTextBox premioLordoFrazionato;
    @UiField
    PercentualeToEuroWidget commissioniNetteFrazionate;
    @UiField
    PercentualeToEuroWidget commissioniAccessorieFrazionate;
    @UiField
    EuroTextBox commissioniTotaliFrazionate;

    @UiField
    DateBox dataEffettoPolizza;
    @UiField
    DateBox dataScadenzaPolizza;
    @UiField
    DateBox dataProposta;
    @UiField
    DateBox dataEmissione;

    @UiField
    ListBox ramo;
    @UiField
    ListBox quotaLloyds;
    @UiField
    ListBox descrizioneFraz;

    public TitoliInserimento() {
        initWidget(uiBinder.createAndBindUi(this));
        imponibileComplessivo.setReadOnly(true);
        imponibileFrazionato.setReadOnly(true);
        premioLordoComplessivo.setReadOnly(true);
        premioLordoFrazionato.setReadOnly(true);
        commissioniTotaliComplessive.setReadOnly(true);
        commissioniTotaliFrazionate.setReadOnly(true);
        initializeCalendar(dataEffettoPolizza);
        initializeCalendar(dataScadenzaPolizza);
        initializeCalendar(dataProposta);
        initializeCalendar(dataEmissione);
        codiceDoc.addItem("");
        codiceDoc.addItem("B");
        codiceDoc.addItem("F");
        tipoContraente.addItem("");
        tipoContraente.addItem(Gender.C.name());
        tipoContraente.addItem(Gender.M.name());
        tipoContraente.addItem(Gender.F.name());
        quotaLloyds.addItem("");
        quotaLloyds.addItem("100");
        descrizioneFraz.addItem("");
        for (int i = 1; i <= 12; i++) {
            descrizioneFraz.addItem("" + i);
        }
        titoloProxy = null; //mi devo far tornare un nuovo titoloProxy, magari nel costruttore
    }

    public void resetFields() {
        ramo.setSelectedIndex(0);
        codiceLb.setSelectedIndex(0);
        codiceDoc.setSelectedIndex(0);
        numeroBinderCovernote.setText("");
        numeroPolizza.setText("");
        tipoContraente.setSelectedIndex(0);
        cognomeContraente.setText("");
        nomeContraente.setText("");
        viaContraente.setText("");
        capContraente.setText("");
        cittaContraente.setText("");
        codiceFiscale.setText("");
        premioNettoComplessivo.reset();
        accessoriComplessivo.reset();
        imponibileComplessivo.reset();
        imposteComplessivo.reset();
        premioLordoComplessivo.reset();
        commissioniNetteComplessive.getPercentuale().setText("");
        commissioniAccessorieComplessive.getEuro().reset();
        commissioniTotaliComplessive.reset();
        premioNettoFrazionato.reset();
        accessoriFrazionato.reset();
        imponibileFrazionato.reset();
        imposteFrazionato.reset();
        premioLordoFrazionato.setText("");
        commissioniAccessorieFrazionate.getPercentuale().setText("");
        commissioniAccessorieFrazionate.getEuro().reset();
        commissioniTotaliFrazionate.setValue("");
        codiceRamo.setSelectedIndex(0);
        codiceLb.setSelectedIndex(0);
        dataEffettoPolizza.setValue(null);
        dataScadenzaPolizza.setValue(null);
        dataProposta.setValue(null);
        dataEmissione.setValue(null);
        quotaLloyds.setSelectedIndex(0);
        descrizioneFraz.setSelectedIndex(0);
    }

    @UiHandler("toolbarInserimentoTitolo")
    void onConferma(ConfermaEvent event) {
        GWT.log("InserimentoTitoloWidget: onConferma");
        GWT.log("InserimentoTitoloWidget: fire CreaTitoloEvent");
        //        fireEvent(new CreaTitoloEvent());
        //TODO popolare il proxy
        //            GWT.log("InserimentoTitoloWidget: fire InserisciTitoloEvent");
        //            fireEvent(new InserisciTitoloEvent(titoloProxy));
    }

    @UiHandler("toolbarInserimentoTitolo")
    public void onAnnullaToolbarInserimentoTitolo(AnnullaEvent event) {
        GWT.log("InserimentoTitoloWidget: onAnnullaToolbarInserimentoTitolo");
        if (Window.confirm("Tutte le modifiche apportate andranno perse. \n Confermi?")) {
            resetFields();
            GWT.log("InserimentoTitoloWidget: fire VisualizzaListaTitoliEvent");
            fireEvent(new VisualizzaListaTitoliEvent());
        }
    }

    private void populateNuovoContrattoDtoProxy(NewTitoloProxy titoloProxy) {
        CertificatoLloydsProxy certificatoLloydsProxy = titoloProxy.getCertificatoLloyds();
        ContraenteProxy contraenteProxy = titoloProxy.getContraente();
        FilieraLloydsOpenMarketProxy filieraLloydsManualeProxy = certificatoLloydsProxy.getFilieraLloydsOpenMarket();

        //CODICI
        filieraLloydsManualeProxy.setCodiceLB(codiceLb.getItemText(codiceLb.getSelectedIndex()));
        String codiceDocString = codiceDoc.getItemText(codiceDoc.getSelectedIndex());
        filieraLloydsManualeProxy.setCodiceDoc(codiceDocString);
        if (codiceDocString.equals("B"))
            filieraLloydsManualeProxy.setBinder(numeroBinderCovernote.getText());
        else if (codiceDocString.equals("F"))
            filieraLloydsManualeProxy.setCoverNote(numeroBinderCovernote.getText());
        //        titoloProxy.setApplication(application.getText());
        certificatoLloydsProxy.setRischio(codiceRamo.getItemText(codiceRamo.getSelectedIndex()) + "."
                + ramo.getItemText(ramo.getSelectedIndex()));
        certificatoLloydsProxy.setNumero(numeroPolizza.getText());

        //CONTRAENTE
        contraenteProxy.setGenderString(tipoContraente.getItemText(tipoContraente.getSelectedIndex()));
        contraenteProxy.setCompanyName(companyName.getText());
        contraenteProxy.setFirstName(nomeContraente.getText());
        contraenteProxy.setLastName(cognomeContraente.getText());
        contraenteProxy.setAddressLine1(viaContraente.getText());
        contraenteProxy.setCity(cittaContraente.getText());
        contraenteProxy.setPostCode(capContraente.getText());
        contraenteProxy.setFiscalCode(codiceFiscale.getText());
        contraenteProxy.setVatNumber(vatNumber.getText());

        //DATE
        titoloProxy.setInceptionDate(dataEffettoPolizza.getValue());
        titoloProxy.setExpiryDate(dataScadenzaPolizza.getValue());
        certificatoLloydsProxy.setDataProposta(dataProposta.getValue());

        certificatoLloydsProxy.setQuotaLloyds(new BigDecimal(quotaLloyds.getItemText(quotaLloyds.getSelectedIndex())));

        //FRAZIONATO
        certificatoLloydsProxy.setPremioNettoPolizzaEuroCent(premioNettoFrazionato.getBigDecimalValue());
        certificatoLloydsProxy.setAccessoriEuroCent(accessoriFrazionato.getBigDecimalValue());
        certificatoLloydsProxy.setTotaleImposteEuroCent(imposteFrazionato.getBigDecimalValue());
        certificatoLloydsProxy.setCommissioniSulNettoEuroCent(commissioniNetteFrazionate.getEuroValue());
        certificatoLloydsProxy.setCommissioniAccessoriEuroCent(commissioniAccessorieFrazionate.getEuroValue());

        certificatoLloydsProxy.setNumberOfInstallments(Integer.parseInt(descrizioneFraz.getItemText(descrizioneFraz
                .getSelectedIndex())));
        certificatoLloydsProxy.setDataEmissioneDocumento(dataEmissione.getValue());
    }

    /***************** COMPLESSIVO *******************/
    /**
     * Aggiorno le commissioni totali
     * @param event
     */
    @UiHandler({ "commissioniAccessorieComplessive", "commissioniNetteComplessive" })
    void onChangeCommissioniNetteComplessive(MaliceChangeEvent event) {
        GWT.log("InserimentoTitoloWidget: onChangeCommissioniNetteComplessive");
        commissioniTotaliComplessive.setValue(commissioniNetteComplessive.getEuro().getBigDecimalValue()
                .add(commissioniAccessorieComplessive.getEuro().getBigDecimalValue()));
    }

    /**
     * Aggiorno il totale
     * @param event
     */
    @UiHandler({ "imponibileComplessivo", "imposteComplessivo" })
    void onChangeImponibileImposteComplessivo(MaliceChangeEvent event) {
        GWT.log("InserimentoTitoloWidget: onChangeImponibileImposteComplessivo");
        premioLordoComplessivo.setValue(imponibileComplessivo.getBigDecimalValue().add(
                imposteComplessivo.getBigDecimalValue()));
    }

    /**
     * Quando il premioNettoComplessivo e' valorizzato setto il valore di riferimento sulle commissioniNetteComplessive
     * @param event
     */
    @UiHandler("premioNettoComplessivo")
    void onBlurPremioNetto(BlurEvent event) {
        if (!premioNettoComplessivo.getText().equals(""))
            commissioniNetteComplessive.setValoreRiferimento(premioNettoComplessivo.getBigDecimalValue());
        calculateImponibileComplessivo();
    }

    /**
     * Quando l'accessoriComplessivo e' valorizzato setto il valore di riferimento sulle commissioniAccessorieComplessive
     * @param event
     */
    @UiHandler("accessoriComplessivo")
    void onBlurAccessoriNetto(BlurEvent event) {
        if (!accessoriComplessivo.getText().equals(""))
            commissioniAccessorieComplessive.setValoreRiferimento(accessoriComplessivo.getBigDecimalValue());
        calculateImponibileComplessivo();
    }

    /**
     * Calcolo l'imponibile complessivo quando entrambi accessoriComplessivo e premioNettoComplessivo sono valorizzati
     */
    private void calculateImponibileComplessivo() {
        if (!premioNettoComplessivo.isEmpty() && !accessoriComplessivo.isEmpty()) {
            imponibileComplessivo.setValue(premioNettoComplessivo.getBigDecimalValue().add(
                    accessoriComplessivo.getBigDecimalValue()));
        }
    }

    /***************** FRAZIONATO *******************/

    /**
     * Aggiorno le commissioni totali
     * @param event
     */
    @UiHandler({ "commissioniAccessorieFrazionate", "commissioniNetteFrazionate" })
    void onChangeCommissioniNetteFrazionate(MaliceChangeEvent event) {
        GWT.log("InserimentoTitoloWidget: onChangeCommissioniNetteFrazionate");
        commissioniTotaliFrazionate.setValue(commissioniNetteFrazionate.getEuro().getBigDecimalValue()
                .add(commissioniAccessorieFrazionate.getEuro().getBigDecimalValue()));
    }

    /**
     * Aggiorno il totale
     * @param event
     */
    @UiHandler({ "imponibileFrazionato", "imposteFrazionato" })
    void onChangeImponibileFrazionato(MaliceChangeEvent event) {
        GWT.log("InserimentoTitoloWidget: onChangeImponibileComplesivo");
        premioLordoFrazionato.setValue(imponibileFrazionato.getBigDecimalValue().add(
                imposteFrazionato.getBigDecimalValue()));
    }

    /**
     * Quando il premioNettoFrazionato e' valorizzato setto il valore di riferimento sulle commissioniNetteFrazionato
     * @param event
     */
    @UiHandler("premioNettoFrazionato")
    void onBlurPremioNettoFrazionato(BlurEvent event) {
        if (!premioNettoFrazionato.getText().equals(""))
            commissioniNetteFrazionate.setValoreRiferimento(premioNettoFrazionato.getBigDecimalValue());
        calculateImponibileFrazionato();
    }

    /**
     * Quando l'accessoriFrazionato e' valorizzato setto il valore di riferimento sulle commissioniAccessorieFrazionate
     * @param event
     */
    @UiHandler("accessoriFrazionato")
    void onBlurAccessoriNettoFrazionato(BlurEvent event) {
        if (!accessoriFrazionato.getText().equals(""))
            commissioniAccessorieFrazionate.setValoreRiferimento(accessoriFrazionato.getBigDecimalValue());
        calculateImponibileFrazionato();
    }

    /**
     * Calcolo l'imponibile Frazionato quando entrambi accessoriFrazionato e premioNettoFrazionato sono valorizzati
     */
    private void calculateImponibileFrazionato() {
        if (!premioNettoFrazionato.isEmpty() && !accessoriFrazionato.isEmpty()) {
            imponibileFrazionato.setValue(premioNettoFrazionato.getBigDecimalValue().add(
                    accessoriFrazionato.getBigDecimalValue()));
        }
    }

    /**************************************/
    public void initializeCalendar(DateBox dateBox) {
        //imposto il formato
        dateBox.setFormat(new DateBox.DefaultFormat(MaliceUtil.getDayMonthYearFormat()));
        //rendo i campi non modificabili
        dateBox.getElement().setAttribute("readonly", "readonly");
    }

    public HandlerRegistration addAnnullaHandler(AnnullaHandler handler) {
        return toolbarInserimentoTitolo.addAnnullaHandler(handler);
    }

    public HandlerRegistration addInserisciTitoloHandler(InserisciTitoloHandler handler) {
        return addHandler(handler, InserisciTitoloEvent.TYPE);
    }

    public HandlerRegistration addCreaTitoloHandler(CreaTitoloHandler handler) {
        return addHandler(handler, CreaTitoloEvent.TYPE);
    }

    public HandlerRegistration addVisualizzaListaTitoliHandler(VisualizzaListaTitoliHandler handler) {
        return addHandler(handler, VisualizzaListaTitoliEvent.TYPE);
    }
}
