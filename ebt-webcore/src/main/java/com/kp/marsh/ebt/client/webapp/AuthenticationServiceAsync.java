package com.kp.marsh.ebt.client.webapp;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.kp.marsh.ebt.shared.dto.LoggedInUser;


public interface AuthenticationServiceAsync {

	/**
	 * Return the sessionId if login is successful, null otherwise
	 * @param username
	 * @param password
	 * @param callback
	 */
	void login(String username, String password, AsyncCallback<String> callback);

	void isSessionAuthenticated(AsyncCallback<Boolean> callback);
	
	void logoutSession(AsyncCallback<Void> callback);

	void getLoggedInUser(AsyncCallback<LoggedInUser> callback);
	
}