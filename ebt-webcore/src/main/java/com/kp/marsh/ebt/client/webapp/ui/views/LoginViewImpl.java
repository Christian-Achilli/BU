package com.kp.marsh.ebt.client.webapp.ui.views;

import org.adamtacy.client.ui.effects.examples.Show;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.kp.marsh.ebt.client.webapp.ui.activityintf.ILoginViewDisplay;
import com.kp.marsh.ebt.client.webapp.ui.resources.Resources;

public class LoginViewImpl extends Composite implements ILoginViewDisplay, HasKeyUpHandlers, KeyUpHandler {

    private static LoginViewImplUiBinder uiBinder = GWT.create(LoginViewImplUiBinder.class);

    interface LoginViewImplUiBinder extends UiBinder<Widget, LoginViewImpl> {
    }

    static {
        Resources.INSTANCE.login().ensureInjected();
        Resources.INSTANCE.mainStructure().ensureInjected();
        Resources.INSTANCE.popup().ensureInjected();
    }

    @UiField
    TextBox username;
    @UiField
    PasswordTextBox password;
    @UiField
    Anchor logIn;
    @UiField
    Label errorMessage;
    @UiField
    Image loadingImg;

    Listener listener;

    public @UiConstructor
    LoginViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        username.setFocus(true);
        this.addKeyUpHandler(this);
        loadingImg.setVisible(false);
    }

    @UiHandler("logIn")
    public void logInClick(ClickEvent e) {
        sendLogin();
        listener.login(username.getText(), password.getText());
    }

    private void sendLogin() {
        logIn.setVisible(false);
        loadingImg.setVisible(true);
        logIn.setEnabled(false);
        password.setEnabled(false);
        username.setEnabled(false);
    }

    public void setLoginErrorMessage(String errorMessage) {
        this.errorMessage.setText(errorMessage);

    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;

    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            sendLogin();

            listener.login(username.getText(), password.getText());
        }

    }

    @Override
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return addDomHandler(handler, KeyUpEvent.getType());
    }

    @Override
    public void clear() {
        logIn.setEnabled(true);
        logIn.setVisible(true);
        loadingImg.setVisible(false);
        password.setText("");
        password.setFocus(true);
        password.setEnabled(true);
        username.setEnabled(true);

        Show s = new Show(this.getElement());
        s.play();

    }

}
