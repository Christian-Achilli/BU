package com.kp.marsh.ebt.client.webapp.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class LoginCEPlace extends Place {

    private String displayName; // the string near to Benvenuto

    private String clientExecId; // the corresponding information owner id that identify the ce just logged in

    public LoginCEPlace(String ceName, String clientExecId) {
        this.displayName = ceName;
        this.clientExecId = clientExecId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getClientExecId() {
        return clientExecId;
    }

    @Prefix("clientExecutive")
    public static class Tokenizer implements PlaceTokenizer<LoginCEPlace> {

        @Override
        public String getToken(LoginCEPlace place) {
            return place.getDisplayName();//+":"+place.getClientExecId();
        }

        @Override
        public LoginCEPlace getPlace(String token) {
            // Checking the token: if correct return the login associated login ce place, else send to login (token is null);
            LoginCEPlace place = null;
            String[] tokenElems = token.split(":");
            if (tokenElems != null && tokenElems.length == 1) {
                place = new LoginCEPlace(tokenElems[0], null);
            }
            return place;
        }
    }

}
