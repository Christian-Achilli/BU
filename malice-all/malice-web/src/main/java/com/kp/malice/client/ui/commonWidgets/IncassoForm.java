package com.kp.malice.client.ui.commonWidgets;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.kp.malice.client.MaliceDebugIds;
import com.kp.malice.client.ui.UIMaliceUtil;
import com.kp.malice.client.ui.UtcSafetyDateBox;
import com.kp.malice.client.ui.gwtEvent.AnnullaHandler;
import com.kp.malice.client.ui.gwtEvent.ConfermaEvent;
import com.kp.malice.client.ui.gwtEvent.IncassoEvent;
import com.kp.malice.client.ui.gwtEvent.IncassoHandler;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.MezzoPagamento;
import com.kp.malice.shared.StatoIncasso;

public class IncassoForm extends Composite {

    private static IncassoFormUiBinder uiBinder = GWT.create(IncassoFormUiBinder.class);

    interface IncassoFormUiBinder extends UiBinder<Widget, IncassoForm> {
    }

    private BigDecimal importoTitolo;

    @UiField
    Label labelDataRegistrazione;
    @UiField
    UtcSafetyDateBox dataBoxIncasso;
    @UiField
    EuroTextBox importoIncasso;
    @UiField
    Label abbuonoIncasso;
    @UiField
    ListBox tipo;
    @UiField
    ListBox mezzoPagamento;
    @UiField
    AnnullaConfermaWidget annullaConfermaWidget;

    public IncassoForm() {
        initWidget(uiBinder.createAndBindUi(this));
        tipo.addItem("");
        tipo.addItem(StatoIncasso.EFFETTIVO.name());
        tipo.addItem(StatoIncasso.SOSPESO.name());
        mezzoPagamento.addItem("");
        mezzoPagamento.addItem(MezzoPagamento.ASSEGNO.name());
        mezzoPagamento.addItem(MezzoPagamento.BANCOMAT.name());
        mezzoPagamento.addItem(MezzoPagamento.BONIFICO.name());
        mezzoPagamento.addItem(MezzoPagamento.CONTANTI.name());
        dataBoxIncasso.setFormat(new DateBox.DefaultFormat(MaliceUtil.getDayMonthYearFormat()));
        dataBoxIncasso.getElement().setAttribute("readonly", "readonly");
        String today = MaliceUtil.getStringDateFormatted(new Date());
        labelDataRegistrazione.setText(today);

        dataBoxIncasso.ensureDebugId(MaliceDebugIds.INC_DT_INCASSO);
        importoIncasso.ensureDebugId(MaliceDebugIds.INC_IMPORTO_INCASSO);
        mezzoPagamento.ensureDebugId(MaliceDebugIds.INC_MEZZO_PAG);
    }

    @UiHandler("importoIncasso")
    void onBlurEventImportoIncasso(BlurEvent event) {
        GWT.log("IncassoForm onBlurEventImportoIncasso: BlurEvent");
        GWT.log("importoTitolo: " + importoTitolo);
        GWT.log("importoIncasso: " + importoIncasso.getBigDecimalValue());
        if (importoTitolo != null) {
            BigDecimal abbuono = importoTitolo.subtract(importoIncasso.getBigDecimalValue());
            GWT.log("BigDecimal abbuono: " + abbuono);
            abbuonoIncasso.setText(MaliceUtil.getEuroFromBigDecimal(abbuono));
        } else {
            GWT.log("ATTENZIONE!!! importo titolo non valorizzato");
        }
    }

    @UiHandler("tipo")
    void onChangeValueTipo(ChangeEvent e) {
        GWT.log("IncassoForm: onChangeValueTipo");
    }

    @UiHandler("dataBoxIncasso")
    void onDataIncassoValueChange(ValueChangeEvent<Date> e) {
        Date dateIncasso = e.getValue();
        GWT.log("dataIncasso: " + dateIncasso);
        if (UIMaliceUtil.isAfterToday(dateIncasso)) {
            UIMaliceUtil.evidenziaPerErrore(dataBoxIncasso.getElement());
            dataBoxIncasso.setValue(null);
            MaliceUtil
                    .showError("Data inserita non valida. La data d'incasso non può essere successiva al giorno odierno");
        } else {
            UIMaliceUtil.eliminaStile(dataBoxIncasso.getElement());
        }
    }

    @UiHandler("annullaConfermaWidget")
    void onConferma(ConfermaEvent e) {
        GWT.log("IncassoFrom: catch ConfermaEvent");
        if (checkFormCompleted()) {
            GWT.log("IncassoFrom: fire IncassoEvent");
            GWT.log("importo incasso: " + importoIncasso.getBigDecimalValue());
            fireEvent(new IncassoEvent(importoIncasso.getBigDecimalValue(), StatoIncasso.fromString(tipo.getValue(tipo
                    .getSelectedIndex())), MezzoPagamento.fromString(mezzoPagamento.getValue(mezzoPagamento
                    .getSelectedIndex())), dataBoxIncasso.getLocalPlus2HoursDate()));
        } else {
            MaliceUtil.showError("Attenzione: tutti i campi sono obbligatori");
        }
    }

    //    && importoIncasso.getBigDecimalValue().compareTo(BigDecimal.ZERO) != 0;
    protected boolean checkFormCompleted() {
        boolean result = mezzoPagamento.getSelectedIndex() != 0 && tipo.getSelectedIndex() != 0
                && dataBoxIncasso.getValue() != null;
        GWT.log(mezzoPagamento.getValue(mezzoPagamento.getSelectedIndex()) + "-"
                + tipo.getValue(tipo.getSelectedIndex()) + "-" + dataBoxIncasso.getValue() + "-"
                + importoIncasso.getValue() + "--" + result);
        return result;
    }

    public void init() {
        GWT.log("IncassoForm.init");
        dataBoxIncasso.setValue(null);
        if (importoTitolo != null)
            importoIncasso.setValue(importoTitolo);
        abbuonoIncasso.setText(MaliceUtil.getEuroFromBigDecimal(BigDecimal.ZERO));
        tipo.setSelectedIndex(0);
        mezzoPagamento.setSelectedIndex(0);
    }

    public HandlerRegistration addAnnullaHandler(AnnullaHandler handler) {
        return annullaConfermaWidget.addAnnullaHandler(handler);
    }

    public HandlerRegistration addIncassoHandler(IncassoHandler handler) {
        return addHandler(handler, IncassoEvent.TYPE);
    }

    public BigDecimal getImportoTitolo() {
        return importoTitolo;
    }

    public void populate(BigDecimal importoTitolo) {
        this.importoTitolo = importoTitolo;
        init();
    }
}
