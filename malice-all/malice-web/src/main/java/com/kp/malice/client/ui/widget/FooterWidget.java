package com.kp.malice.client.ui.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class FooterWidget extends Composite{

    private static FooterUiBinder uiBinder = GWT.create(FooterUiBinder.class);

    interface FooterUiBinder extends UiBinder<Widget, FooterWidget> {
    }

    public FooterWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
