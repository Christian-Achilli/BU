package com.kp.malice.client.ui.commonWidgets;

import java.math.BigDecimal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.kp.malice.client.ui.gwtEvent.MaliceChangeEvent;

public class EuroTextBox extends CifraTextBox {

    static final NumberFormat currencyNumberFormat = NumberFormat.getCurrencyFormat("EUR");

    public EuroTextBox() {
        super();
    }

    @Override
    public String getFormatted() {
        GWT.log("cifra: " + super.cifra);
        return bigDecimalToEuroString(super.cifra);
    }

    private String bigDecimalToEuroString(BigDecimal bd) {
        BigDecimal centesimi = bd.movePointRight(2);
        if (null != centesimi)
            return currencyNumberFormat.format(centesimi.movePointLeft(2));
        return "0,00";
    }
    
    @Override
    public void setValue(BigDecimal calculated) {
        String result = bigDecimalToEuroString(calculated);
        setText(result);
        textBefore = result;
        cifra = calculated;
        fireEvent(new MaliceChangeEvent());
    }
}
