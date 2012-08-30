package com.kp.marsh.ebt.client.webapp.ui.activityintf;

import com.google.gwt.user.client.ui.IsWidget;

public interface ILoginViewDisplay extends IsWidget {

    public void setListener(Listener listener);

    public interface Listener {

        void login(String username, String password);
    }

    /**
     * Login has failed either for wrong username and/or password or login server unreachable.
     */
    void setLoginErrorMessage(String errorMessage);

    /**
     * Cleans up the username and password field, set the focus on the username, swith off the loader on the login button
     */
    void clear();

}
