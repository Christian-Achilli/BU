package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.EventHandler;

public interface ErrorMessageHandler extends EventHandler {
    void onErrorMessage(ErrorMessageEvent errorMessageEvent);
}