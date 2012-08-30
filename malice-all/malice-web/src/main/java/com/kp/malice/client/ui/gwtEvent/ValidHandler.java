package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.EventHandler;

public interface ValidHandler extends EventHandler {
    void onValid(ValidEvent validEvent);
}