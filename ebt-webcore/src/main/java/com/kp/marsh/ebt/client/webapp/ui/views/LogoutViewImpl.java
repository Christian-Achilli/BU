package com.kp.marsh.ebt.client.webapp.ui.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.kp.marsh.ebt.client.webapp.ui.activityintf.ILogoutViewDisplay;

public class LogoutViewImpl extends Composite implements ILogoutViewDisplay {

    @UiField
    Anchor loginLink;

    Listener listener;

    private static LogoutViewImplUiBinder uiBinder = GWT.create(LogoutViewImplUiBinder.class);

    interface LogoutViewImplUiBinder extends UiBinder<Widget, LogoutViewImpl> {
    }

    public LogoutViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("loginLink")
    void backToLogin(ClickEvent e) {
        listener.onClick();
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;

    }

}
