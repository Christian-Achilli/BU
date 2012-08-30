package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.EventHandler;

public interface MaliceChangeHandler extends EventHandler {
    void onChange(MaliceChangeEvent maliceChangeEvent);
  }