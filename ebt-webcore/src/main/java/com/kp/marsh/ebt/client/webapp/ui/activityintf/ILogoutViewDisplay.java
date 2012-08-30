package com.kp.marsh.ebt.client.webapp.ui.activityintf;

import com.google.gwt.user.client.ui.IsWidget;

public interface ILogoutViewDisplay extends IsWidget {

    public interface Listener {
        void onClick();
    }

    void setListener(ILogoutViewDisplay.Listener listener);

}