package com.kp.malice.client.tabChiusure;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ChiusureDettaglio extends Composite {

    private static ChiusureDettaglioUiBinder uiBinder = GWT.create(ChiusureDettaglioUiBinder.class);

    interface ChiusureDettaglioUiBinder extends UiBinder<Widget, ChiusureDettaglio> {
    }

    public ChiusureDettaglio() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
