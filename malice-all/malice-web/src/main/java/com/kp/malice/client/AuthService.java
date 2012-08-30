package com.kp.malice.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.kp.malice.shared.LoggedInUserInfo;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("auth")
public interface AuthService extends RemoteService {
    LoggedInUserInfo retrieveLoggedInUserInfo();

    void pingToKeepAliveServerSession();
}
