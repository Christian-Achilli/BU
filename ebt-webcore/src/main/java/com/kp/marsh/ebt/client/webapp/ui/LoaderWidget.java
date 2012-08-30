package com.kp.marsh.ebt.client.webapp.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoaderWidget extends Composite {

    private static LoaderWidgetUiBinder uiBinder = GWT.create(LoaderWidgetUiBinder.class);

    interface LoaderWidgetUiBinder extends UiBinder<Widget, LoaderWidget> {
    }

    @UiField
    HTMLPanel htmlPanel;

    public LoaderWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public HTMLPanel getHtmlPanel() {
        return htmlPanel;
    }

}
