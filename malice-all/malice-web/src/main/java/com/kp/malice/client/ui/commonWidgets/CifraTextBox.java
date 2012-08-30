package com.kp.malice.client.ui.commonWidgets;

import java.math.BigDecimal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.TextBox;
import com.kp.malice.client.ui.gwtEvent.MaliceChangeEvent;
import com.kp.malice.client.ui.gwtEvent.MaliceChangeHandler;

public abstract class CifraTextBox extends TextBox {

    public static interface CurrencyTextBoxHandler {
        public void onChange(CifraTextBox currencyTextBox);
    }

    protected BigDecimal cifra = BigDecimal.ZERO;
    private CurrencyTextBoxHandler handler = null;
    private RegExp inputValidator = RegExp.compile("^-*([0-9]*|\\d*,\\d{0,2}?)$");
    private BlurHandler bh;

    public CifraTextBox() {
        this(true);
    }

    public CifraTextBox(boolean enableMinus) {

        super.setTextAlignment(TextBox.ALIGN_CENTER);

        addKeyPressHandler(keyPressHandler);
        addKeyUpHandler(keyUpHandler);
        bh = new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                if (CifraTextBox.this.isReadOnly()) {
                    return;
                }
                parse();
                showFormatted();
                if (handler != null) {
                    handler.onChange(CifraTextBox.this);
                }
            }

        };
        addBlurHandler(bh);

        addFocusHandler(new FocusHandler() {

            @Override
            public void onFocus(FocusEvent event) {
                if (CifraTextBox.this.isReadOnly()) {
                    return;
                }
                CifraTextBox.super.setText("");
            }
        });
    }
    
    private void parse() {
        GWT.log("parse: " + CifraTextBox.super.getText());
        if (CifraTextBox.super.getText().equals(""))
            cifra = BigDecimal.ZERO;
        else {
            String s = CifraTextBox.super.getText().replace(',', '.');
            cifra = new BigDecimal(s);
        }
        fireEvent(new MaliceChangeEvent());
    }
    
    private void showFormatted() {
        CifraTextBox.super.setText(getFormatted());
    }
    
    public abstract String getFormatted(); 
    
    private KeyUpHandler keyUpHandler = new KeyUpHandler() {

        @Override
        public void onKeyUp(KeyUpEvent event) {
            int c = event.getNativeKeyCode();
            if (KeyCodes.KEY_ENTER == c){
                NativeEvent nativeEvent = 
                        Document.get().
                          createKeyUpEvent(false,false,false,false,
                                       KeyCodes.KEY_TAB ,KeyCodes.KEY_TAB);
                DomEvent.fireNativeEvent(nativeEvent, CifraTextBox.this, CifraTextBox.this.getElement());
                return;
            } 
            if (KeyCodes.KEY_TAB == c) {
                if (getText().endsWith(",")) {
                    setText(getText() + "00");
                }
//                bh.onBlur(null);
                return;
            }

            GWT.log("key: " + c);
            if (!"".equals(getText()) && !inputValidator.test(getText())) {
                setText(textBefore);
            }

        };
    };
    protected String textBefore;

    private KeyPressHandler keyPressHandler = new KeyPressHandler() {

        @Override
        public void onKeyPress(KeyPressEvent event) {
            //se gli eventi si succedono veleocemente alle volte vengono persi pertanto aggiungo un controllo 
            if (!"".equals(getText()) && inputValidator.test(getText()))
                textBefore = getText();
        };
    };

    public BigDecimal getNumber() {
        return cifra;
    }

    /**
     * ATTENZIONE questo metodo ritorna 0 anche se il campo è stato popolato con un blank
     * @return il valore del campo
     */
    public BigDecimal getBigDecimalValue() {
        return cifra;
    }

    public boolean isEmpty() {
        return getText().equals("");
    }

    public void reset() {
        setText("");
        cifra=BigDecimal.ZERO;
    }

    public void setCurrencyTextBoxHandler(CurrencyTextBoxHandler handler) {
        this.handler = handler;
    }

    public abstract void setValue(BigDecimal calculated);

    @Override
    public void setValue(String text) {
        setText(text);
    }

    public HandlerRegistration addMaliceChangeHandler(MaliceChangeHandler handler) {
        return addHandler(handler, MaliceChangeEvent.TYPE);
    }

}
