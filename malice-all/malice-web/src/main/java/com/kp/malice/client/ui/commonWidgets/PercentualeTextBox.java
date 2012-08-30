package com.kp.malice.client.ui.commonWidgets;

import java.math.BigDecimal;
import java.math.MathContext;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.kp.malice.client.ui.gwtEvent.MaliceChangeEvent;

public class PercentualeTextBox extends CifraTextBox {

    static final NumberFormat currencyNumberFormat = NumberFormat.getCurrencyFormat();

    public PercentualeTextBox() {
        super();
    }

    @Override
    public String getFormatted() {
        GWT.log("cifra: " + super.cifra);
        return bigDecimalToPercentualeString(super.cifra);
    }

    private String bigDecimalToPercentualeString(BigDecimal bd) {
        BigDecimal centesimi = bd.movePointRight(2);
        centesimi = centesimi.round(new MathContext(0));
        String percentuale = ""+centesimi.movePointLeft(2);
        percentuale = percentuale.replace(".", ",");
        return "% "+percentuale;
    }

    @Override
    public void setValue(BigDecimal bd) {
        String result = bigDecimalToPercentualeString(bd);
        setText(result);
        textBefore = result;
        fireEvent(new MaliceChangeEvent());
    }
}
