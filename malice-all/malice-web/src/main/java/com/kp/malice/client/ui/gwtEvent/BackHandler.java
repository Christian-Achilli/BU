package com.kp.malice.client.ui.gwtEvent;

import com.google.gwt.event.shared.EventHandler;

public interface BackHandler extends EventHandler {
    void onBack(BackEvent backEvent);
  }