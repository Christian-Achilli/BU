package com.kp.malice.client.ui.commonWidgets;

import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.examples.Fade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.shared.MaliceUtil;

public abstract class AlertMessage extends Composite {

    private static AlertMessageUiBinder uiBinder = GWT.create(AlertMessageUiBinder.class);

    interface AlertMessageUiBinder extends UiBinder<Widget, AlertMessage> {
    }

    @UiField
    DivElement message;
    @UiField
    SpanElement messageText;
    @UiField
    Anchor dismissLink;
    private double EFFECT_DURATION = 3.0; //sec
    private int DELAY = 4000; //ms

    public AlertMessage() {
        initWidget(uiBinder.createAndBindUi(this));
        RootLayoutPanel.get().add(this);

    }

    protected void show() {
        MaliceUtil.eliminaConFadeEffect(this, DELAY, EFFECT_DURATION);
    }

    protected void setMessageText(String text) {
        messageText.setInnerHTML(SafeHtmlUtils.fromString(text).asString());
    }

    @UiHandler("dismissLink")
    protected void dismiss(ClickEvent e) {
        RootLayoutPanel.get().remove(AlertMessage.this);
    }

}
