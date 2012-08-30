package com.kp.malice.client.ui.commonWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.kp.malice.client.ui.gwtEvent.ErrorMessageEvent;
import com.kp.malice.client.ui.gwtEvent.ValidEvent;
import com.kp.malice.client.ui.resources.MaliceResources;
import com.kp.malice.shared.Gender;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.proxies.ContraenteProxy;
import com.kp.malice.shared.proxies.IncassoTitoloProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public class DettaglioTitoloWidget extends Composite {

    private static DettaglioTitoloWidgetUiBinder uiBinder = GWT.create(DettaglioTitoloWidgetUiBinder.class);

    interface DettaglioTitoloWidgetUiBinder extends UiBinder<Widget, DettaglioTitoloWidget> {
    }

    //DATI TITOLO
    @UiField
    SpanElement stato;
    @UiField
    SpanElement numero;
    @UiField
    SpanElement broker;
    @UiField
    SpanElement scadenza;
    @UiField
    SpanElement decorrenza;
    @UiField
    SpanElement bindAuth;
    @UiField
    SpanElement appendice;
    @UiField
    EditableValidableTextLabelWidget subAgente;
    @UiField
    EditableValidableTextLabelWidget codCig;

    // CONTRAENTE
    @UiField
    SpanElement identificativoRagSocValue;
    @UiField
    Element identificativoRagSocLabel;
    @UiField
    SpanElement cf;
    @UiField
    SpanElement pIva;
    @UiField
    SpanElement indirizzo1;
    @UiField
    SpanElement indirizzo2;
    @UiField
    SpanElement citta;
    @UiField
    SpanElement cap;
    @UiField
    SpanElement region;
    @UiField
    SpanElement country;

    //INCASSI
    @UiField
    Label msgIncassiZero;
    @UiField(provided = true)
    ListaIncassiWidget listaIncassiWidget;

    // PREMIO
    @UiField
    VerticalPanel premioVP;

    // DOCUMENTI
    //    @UiField
    //    DownloadDocumentiWidget downloadDocumenti;

    private String lastSaved = "";

    private Object[] inputArrayFields;

    public DettaglioTitoloWidget() {
        listaIncassiWidget = new ListaIncassiWidget(3);
        initWidget(uiBinder.createAndBindUi(this));
        inputArrayFields = new Object[] { stato, numero, broker, scadenza, decorrenza, bindAuth, appendice, cf, pIva,
                indirizzo1, indirizzo2, citta, cap, subAgente, codCig };

        //VALIDAZIONE
        subAgente.applyOnBlurORRegExp("^\\d{3}$", "^\\d{0}$", "Il codice sub-agente deve essere composto da 3 cifre");
        subAgente.applyOnKeyUpRegExp("^\\d{0,3}$");
        codCig.applyOnBlurORRegExp("^[a-zA-Z0-9]{10}$", "^[a-zA-Z0-9]{0}$", "Il campo COD-CIG deve essere composto da 10 caratteri alfanumerici");
        codCig.applyOnKeyUpRegExp("^[a-zA-Z0-9]{0,10}$");

        msgIncassiZero.setText("Nessun incasso presente.");
        msgIncassiZero.setVisible(false);
        premioVP.setWidth("100%");
        subAgente.setEditable(false);
        subAgente.setMaxLength(3);
        codCig.setEditable(false);
        codCig.setMaxLength(10);
    }

    public void init(NewTitoloProxy titoloProxy) {
        setTitolo(titoloProxy);
        setContraente(titoloProxy.getContraente());
        formatEmptyField();
        setPremi(titoloProxy);
        if (titoloProxy.getIncassiOrderByDataInserimentoDesc().size() <= 0) {
            listaIncassiWidget.setVisible(false);
            msgIncassiZero.setVisible(true);
        } else {
            listaIncassiWidget.setVisible(true);
            msgIncassiZero.setVisible(false);
        }
    };

    public void reset() {
        codCig.reset();
        subAgente.reset();
    }

    @UiHandler({ "subAgente", "codCig" })
    void onValid(ValidEvent e) {
        GWT.log("DettaglioTitoliWidget onValid: catch ValidEvent");
    }

    @UiHandler({ "subAgente", "codCig" })
    void onErrorMessage(ErrorMessageEvent e) {
        GWT.log("DettaglioTitoliWidget onErrorMessage, catch ErrorMessageEvent: " + e.getMsg());
        MaliceUtil.showError(e.getMsg());
    }

    private void formatEmptyField() {
        MaliceResources.INSTANCE.main().addButton();
        for (Object inputField : inputArrayFields) {
            if (inputField instanceof SpanElement) {
                if ("".equals(((SpanElement) inputField).getInnerText()))
                    ((Element) inputField).setInnerText(MaliceUtil.EMPTY_STRING);
            } else if (inputField instanceof Composite) {
                if ("".equals(((EditableValidableTextLabelWidget) inputField).getInsertedText()))
                    ((EditableValidableTextLabelWidget) inputField).setTextLabel(MaliceUtil.EMPTY_STRING);
            }
        }
    }

    //TITOLO
    private void setTitolo(NewTitoloProxy titoloProxy) {
        //TODO appendice
        stato.setInnerText(titoloProxy.getStringStatoTitolo());
        decorrenza.setInnerText(MaliceUtil.getStringDateFormatted(titoloProxy.getInceptionDate()));
        scadenza.setInnerText(MaliceUtil.getStringDateFormatted(titoloProxy.getExpiryDate()));
        bindAuth.setInnerText(titoloProxy.getCertificatoLloyds().getFilieraLloyds().getBindingAuthority()
                .getDescription());
        numero.setInnerText(titoloProxy.getCertificatoLloyds().getNumero());
        broker.setInnerText(titoloProxy.getCertificatoLloyds().getFilieraLloyds().getReferente());
        appendice.setInnerText(titoloProxy.getNumeroAppendice());
        if (titoloProxy.getCodiceSubagente() == null)
            subAgente.setText("");
        else
            subAgente.setText(titoloProxy.getCodiceSubagente());
        if (titoloProxy.getCodiceCig() == null)
            codCig.setText("");
        else
            codCig.setText(titoloProxy.getCodiceCig());
    }

    //CONTRAENTE
    private void setContraente(ContraenteProxy contraenteProxy) {
        Gender g = Gender.fromString(contraenteProxy.getGenderString());
        if (MaliceUtil.isPersonaFisica(g)) {
            identificativoRagSocValue
                    .setInnerText(contraenteProxy.getFirstName() + " " + contraenteProxy.getLastName());
            identificativoRagSocLabel.setInnerText("IDENTIFICATIVO");
        } else {
            identificativoRagSocValue.setInnerText(contraenteProxy.getCompanyName());
            identificativoRagSocLabel.setInnerText("RAG. SOC.");
        }
        setCodceFiscale_PartitaIva(contraenteProxy);
        indirizzo1.setInnerText(contraenteProxy.getAddressLine1());
        citta.setInnerText(contraenteProxy.getCity());
        cap.setInnerText(contraenteProxy.getPostCode());
        region.setInnerText(contraenteProxy.getRegion());
        country.setInnerText(contraenteProxy.getCountry());
    }

    //INCASSI
    public HasData<IncassoTitoloProxy> getTabellaIncassi() {
        return listaIncassiWidget.getTabellaIncassi();
    }

    //PREMI
    private void setPremi(NewTitoloProxy newTitoloProxy) {
        premioVP.clear();
        premioVP.add(new PremioWidget(newTitoloProxy, null));
    }

    // DOCUMENTI
    //    public void setDocumenti(List<DocumentoProxy> documentiProxy) {
    //        String arrStrDoc[] = new String[documentiProxy.size()];
    //        int i = 0;
    //        for (final DocumentoProxy documentoProxy : documentiProxy) {
    //            arrStrDoc[i++] = documentoProxy.getNomeDocumento();
    //        }
    //        downloadDocumenti.setDocumenti(arrStrDoc, null, null);
    //    }

    private void setCodceFiscale_PartitaIva(ContraenteProxy contraenteProxy) {
        cf.setInnerText("");
        pIva.setInnerText("");
        if (null != contraenteProxy.getFiscalCode() && !"".equals(contraenteProxy.getFiscalCode())
                && !"null".equals(contraenteProxy.getFiscalCode()))
            cf.setInnerText(contraenteProxy.getFiscalCode());
        if (null != contraenteProxy.getVatNumber() && !"".equals(contraenteProxy.getVatNumber())
                && !"null".equals(contraenteProxy.getVatNumber()))
            pIva.setInnerText(contraenteProxy.getVatNumber());
    }

    public EditableValidableTextLabelWidget getSubAgente() {
        return subAgente;
    }

    public void setSubAgente(EditableValidableTextLabelWidget subAgente) {
        this.subAgente = subAgente;
    }

    public EditableValidableTextLabelWidget getCodCig() {
        return codCig;
    }

    public void setCodCig(EditableValidableTextLabelWidget codCig) {
        this.codCig = codCig;
    }

    //    public void setComparatorsAndSortHandler(ListHandler<IncassoTitoloProxy> incassiSortHandler) {
    //        listaIncassiWidget.setComparatorsAndSortHandler(incassiSortHandler);
    //    }

}
