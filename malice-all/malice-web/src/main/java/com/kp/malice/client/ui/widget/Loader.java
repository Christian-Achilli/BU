package com.kp.malice.client.ui.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.MaliceDebugIds;

public class Loader extends Composite {

    private static LoaderUiBinder uiBinder = GWT.create(LoaderUiBinder.class);

    interface LoaderUiBinder extends UiBinder<Widget, Loader> {
    }

    @UiField
    DivElement opacizedDiv;
    @UiField
    ImageElement loaderImage;

    public Loader() {
        initWidget(uiBinder.createAndBindUi(this));
        this.ensureDebugId(MaliceDebugIds.LOADER);
    }

}
