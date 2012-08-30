package com.kp.malice.client.ui.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.gwtEvent.ErrorMessageEvent;
import com.kp.malice.client.ui.gwtEvent.ErrorMessageHandler;
import com.kp.malice.client.ui.gwtEvent.ValidEvent;
import com.kp.malice.client.ui.gwtEvent.ValidHandler;
import com.kp.malice.client.ui.resources.MaliceResources;

public class EditableValidableTextLabelWidget extends Composite {

    private static EditableValidableTextLabelWidgetUiBinder uiBinder = GWT
            .create(EditableValidableTextLabelWidgetUiBinder.class);

    interface EditableValidableTextLabelWidgetUiBinder extends UiBinder<Widget, EditableValidableTextLabelWidget> {
    }

    @UiField
    Label label;

    @UiField
    TextBox textBox;

    private String lastSaved;
    private String regExpValidation;
    private String regExpValidation2;
    private String errorMsg;

    public EditableValidableTextLabelWidget() {
        super();
        initWidget(uiBinder.createAndBindUi(this));
        textBox.setStylePrimaryName(MaliceResources.INSTANCE.main().blueInput());
    }

    public void setEditable(boolean isEditable) {
        textBox.setVisible(isEditable);
        label.setVisible(!isEditable);
    }

    @UiHandler("textBox")
    void onClick(ClickEvent e) {
        textBox.setStylePrimaryName(MaliceResources.INSTANCE.main().blueInput());
    }

    /**
     * Se isValid è true allora metto il bordo alla textBox di colore grigio, altrimenti rosso.
     * @param isValid
     */
    public void setValid(boolean isValid) {
        if (isValid)
            textBox.setStylePrimaryName(MaliceResources.INSTANCE.main().greenInput());
        else
            textBox.setStylePrimaryName(MaliceResources.INSTANCE.main().redInput());
    }

    public void applyOnKeyUpRegExp(final String exp) {
        textBox.addKeyUpHandler(new KeyUpHandler() {
            public void onKeyUp(KeyUpEvent event) {
                int cp = textBox.getCursorPos();
                if (!RegExp.compile(exp).test(textBox.getText())) {
                    textBox.setText(lastSaved);
                    setValid(false);
                    if (cp <= lastSaved.length())
                        textBox.setCursorPos(cp - 1);
                } else if (!lastSaved.equals(textBox.getText())) {
                    lastSaved = textBox.getText();
                    setValid(true);
                }
            }
        });
    }

    public void applyOnBlurRegExp(final String exp, final String errorMsg) {
        GWT.log("exp: "+exp);
        regExpValidation = exp;
        this.errorMsg = errorMsg;
        textBox.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                validate();
            }
        });
    }

    public void applyOnBlurORRegExp(final String exp, final String exp2, final String errorMsg) {
        regExpValidation = exp;
        regExpValidation2 = exp2;
        this.errorMsg = errorMsg;
        textBox.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                validateOrRegularExpression();
            }
        });
    }

    /**
     * Se è stata settata una reg exp per valutare la validita' del campo allora viene eseguita, altrimenti il campo è sempre valido
     * @return
     */
    public boolean validate() {
        if (null != regExpValidation) {
            GWT.log("regExpValidation: "+regExpValidation);
            if (RegExp.compile(regExpValidation).test(textBox.getText())) {
                return validatedTrue();
            } else {
                GWT.log("EditableValidableTextLabelWidget applyOnBlurRegExp: fire ErrorMessageEvent");
                fireEvent(new ErrorMessageEvent(errorMsg));
                setValid(false);
                return false;
            }
        } else
            return true;
    }

    public boolean validateOrRegularExpression() {
        if (null != regExpValidation && regExpValidation2 != null) {
            GWT.log("regExpValidation1: "+regExpValidation+"      regExpValidation2: "+regExpValidation2);
            // non è stato utlizzato l'|| per motivi di debug
            if (RegExp.compile(regExpValidation).test(textBox.getText())) {
                return validatedTrue();
            } else if (RegExp.compile(regExpValidation2).test(textBox.getText())) {
                return validatedTrue();
            }
        }
        GWT.log("EditableValidableTextLabelWidget applyOnBlurRegExp: fire ErrorMessageEvent");
        fireEvent(new ErrorMessageEvent(errorMsg));
        setValid(false);
        return false;

    }

    private boolean validatedTrue() {
        GWT.log("EditableValidableTextLabelWidget applyOnBlurRegExp: fire ValidEvent");
        fireEvent(new ValidEvent());
        setValid(true);
        return true;
    }

    public void setText(String str) {
        setTextLabel(str);
        setTextInput(str);
    }

    public void setTextLabel(String str) {
        textBox.setStylePrimaryName(MaliceResources.INSTANCE.main().blueInput());
        label.setText(str);
    }

    public void setTextInput(String str) {
        textBox.setText(str);
        lastSaved = str;
    }

    public void reset() {
        setTextLabel(label.getText());
    }

    public void setMaxLength(int length) {
        textBox.setMaxLength(length);
    }

    public String getInsertedText() {
        return textBox.getText();
    }

    public HandlerRegistration addErrorMessageHandler(ErrorMessageHandler handler) {
        return addHandler(handler, ErrorMessageEvent.TYPE);
    }

    public HandlerRegistration addValidHandler(ValidHandler handler) {
        return addHandler(handler, ValidEvent.TYPE);
    }

    public String getRegExpValidation2() {
        return regExpValidation2;
    }

    public void setRegExpValidation2(String regExpValidation2) {
        this.regExpValidation2 = regExpValidation2;
    }
}
