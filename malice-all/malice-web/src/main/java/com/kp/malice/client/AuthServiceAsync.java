package com.kp.malice.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.kp.malice.shared.LoggedInUserInfo;

/**
 * The async counterpart of <code>AuthService</code>.
 */
public interface AuthServiceAsync {
    void retrieveLoggedInUserInfo(AsyncCallback<LoggedInUserInfo> callback);

    void pingToKeepAliveServerSession(AsyncCallback<Void> callback);
}
