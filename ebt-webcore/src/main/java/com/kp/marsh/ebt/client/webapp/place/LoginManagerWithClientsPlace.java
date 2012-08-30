package com.kp.marsh.ebt.client.webapp.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * @author christianachilli
 * Mark up class to mark when a manager has clients and pills has to be shown on its UI
 */
public class LoginManagerWithClientsPlace extends LoginManagerPlace {

    public LoginManagerWithClientsPlace(String managerName, String managerId, String defaulLocationId, String officeName) {
        super(managerName, managerId, defaulLocationId, officeName);
    }

    @Prefix("manager-ce")
    public static class Tokenizer implements PlaceTokenizer<LoginManagerWithClientsPlace> {

        @Override
        public String getToken(LoginManagerWithClientsPlace place) {
            return place.getDisplayName();//+":"+place.getManagerId()+":"+place.getDefaultLocationId()+":"+place.getOfficeName();
        }

        @Override
        public LoginManagerWithClientsPlace getPlace(String token) {
            // Checking the token: if correct return the login associated login manager place, else send to login (token is null);
            LoginManagerWithClientsPlace place = null;
            String[] tokenElems = token.split(":");
            if (tokenElems != null && tokenElems.length == 1) {
                place = new LoginManagerWithClientsPlace(tokenElems[0], null, null, null);
            }
            return place;
        }
    }

}
