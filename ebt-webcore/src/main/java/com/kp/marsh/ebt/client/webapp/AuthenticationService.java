package com.kp.marsh.ebt.client.webapp;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.kp.marsh.ebt.shared.dto.LoggedInUser;

@RemoteServiceRelativePath("login")
public interface AuthenticationService extends RemoteService {

    /**
     * Login service.
     *  
     * @param username
     * @param password
     * @return the session id if the login is successful, null otherwise
     */
    public String login(String username, String password);

    /**
     * @return true if the current session is user secured
     */
    public boolean isSessionAuthenticated();

    /**
     * Logout the user from current session
     * @return
     */
    public void logoutSession();

    /**
     * @return the current logged in user or null
     */
    public LoggedInUser getLoggedInUser();

}
