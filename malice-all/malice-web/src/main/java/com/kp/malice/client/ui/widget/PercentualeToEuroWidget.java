package com.kp.malice.client.ui.widget;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.gwtEvent.MaliceChangeEvent;
import com.kp.malice.client.ui.gwtEvent.MaliceChangeHandler;
import com.kp.malice.shared.MaliceUtil;

public class PercentualeToEuroWidget extends Composite {
    private static PercentualeToEuroWidgetUiBinder uiBinder = GWT.create(PercentualeToEuroWidgetUiBinder.class);

    interface PercentualeToEuroWidgetUiBinder extends UiBinder<Widget, PercentualeToEuroWidget> {
    }

    @UiField
    PercentualeTextBox percentualeTextBox;
    @UiField
    EuroTextBox euroTextBox;

    /**
     * Variabile sulla quale eseguire il calcolo della percentuale
     */
    private BigDecimal valoreRiferimento;

    public PercentualeToEuroWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler({ "euroTextBox", "percentualeTextBox" })
    void onClickEvent(ClickEvent event) {
        GWT.log("PercentualeToEuroWidget onClickEvent");
        reset();
    }

    @UiHandler("euroTextBox")
    void onBlurEventEuro(BlurEvent event) {
        GWT.log("euroEuroTextBox onChangeEventEuro, value: " + euroTextBox.getValue());
        if (euroTextBox.isEmpty())
            reset();
        else {
            calculateAndSetPercentuale();
            fireEvent(new MaliceChangeEvent());
        }
    }

    @UiHandler("percentualeTextBox")
    void onBlurEventPercentuale(BlurEvent event) {
        GWT.log("PercentualeToEuroWidget onChangeEventEuro, value: " + percentualeTextBox.getValue());
        if ("".equals(percentualeTextBox.getText()) || "0".equals(percentualeTextBox.getText()))
            reset();
        else {
            calculateAndSetEuro();
            fireEvent(new MaliceChangeEvent());
        }
    }

    /**
     * Calcolo il valore in euro della percentuale del premio netto rispetto  al valore di riferimento
     * Se la percentuale eccede il 100% o non e' >0 allora resetto entrambe le box
     */
    public void calculateAndSetEuro() {
        if (valoreRiferimento == null)
            reset();
        else {
            BigDecimal percentualeValue = percentualeTextBox.getBigDecimalValue();
            if (percentualeValue.compareTo(new BigDecimal(100)) <= 0
                    && percentualeValue.compareTo(new BigDecimal(0)) == 1) {
                BigDecimal calculated = valoreRiferimento.multiply(percentualeValue);
                calculated = calculated.movePointLeft(2);
                euroTextBox.setValue(calculated);
            } else {
                manageBadValue();
            }
        }
    }

    public void manageBadValue() {
        Window.alert("Il valore inserito non e' valido.");
        reset();
    }

    /**
     * Calcolo la percentuale espressa in euro rispetto  al valore di riferimento
     * Se il valore espresso in euro non è minore in senso assoluto del valore di riferimento allora resetto il valore del campo percentuale e del campo in euro
     */
    public void calculateAndSetPercentuale() {
        if (valoreRiferimento == null)
            reset();
        else {
            BigDecimal euroValue = euroTextBox.getBigDecimalValue();
//            if (((valoreRiferimento.compareTo(new BigDecimal(0)) == 1) && (euroValue.compareTo(valoreRiferimento) <= 0 && euroValue.compareTo(new BigDecimal(0)) == 1)) || ((valoreRiferimento.compareTo(new BigDecimal(0)) == 1))
//                    && (euroValue.compareTo(valoreRiferimento) >= 0 && euroValue.compareTo(new BigDecimal(0)) == -1)) {
            // se valore di riferimento è > 0 allora euroValue non può essere negativo e viceversa
            if((valoreRiferimento.compareTo(new BigDecimal(0)) == 1 && euroValue.compareTo(valoreRiferimento)<= 0 && euroValue.compareTo(BigDecimal.ZERO)==1) || (valoreRiferimento.compareTo(new BigDecimal(0)) <= 0 && euroValue.compareTo(valoreRiferimento) == 1) && euroValue.compareTo(BigDecimal.ZERO)<=0){
                BigDecimal calculated = euroValue.movePointRight(4).divide(valoreRiferimento, RoundingMode.HALF_EVEN);
                calculated = calculated.movePointLeft(2);
                percentualeTextBox.setValue(calculated);
            } else {
                manageBadValue();
            }
        }
    }

    public TextBox getPercentuale() {
        return percentualeTextBox;
    }

    public EuroTextBox getEuro() {
        return euroTextBox;
    }

    /**
     * Quando setto il valore di riferimento inizializzo entrambi i campi euro e percentuale
     * @param valoreRiferimento
     */
    public void setValoreRiferimento(BigDecimal valoreRiferimento) {
        this.valoreRiferimento = valoreRiferimento;
        GWT.log("setValoreRiferimento: " + valoreRiferimento);
        reset();
    }

    private void reset() {
        percentualeTextBox.reset();
        euroTextBox.reset();
        fireEvent(new MaliceChangeEvent());
    }

    public BigDecimal getEuroValue() {
        return euroTextBox.getBigDecimalValue();
    }

    public HandlerRegistration addMaliceChangeHandler(MaliceChangeHandler handler) {
        return addHandler(handler, MaliceChangeEvent.TYPE);
    }

}
