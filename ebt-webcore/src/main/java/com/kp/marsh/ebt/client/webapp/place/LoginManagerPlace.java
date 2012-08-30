package com.kp.marsh.ebt.client.webapp.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class LoginManagerPlace extends Place {

    private String displayName; // the string near to Benvenuto

    private String managerId; // the corresponding information owner id that identify the manager just logged in

    private String defaultLocationId;// the default information owner Id for this manager (likely its office)

    private String officeName;

    public LoginManagerPlace(String managerName, String managerId, String defaulLocationId, String officeName) {
        this.displayName = managerName;
        this.managerId = managerId;
        this.defaultLocationId = defaulLocationId;
        this.officeName = officeName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getManagerId() {
        return managerId;
    }

    public String getDefaultLocationId() {
        return defaultLocationId;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOfficeName() {
        return officeName;
    }

    @Prefix("manager")
    public static class Tokenizer implements PlaceTokenizer<LoginManagerPlace> {

        @Override
        public String getToken(LoginManagerPlace place) {
            return place.getDisplayName();//+":"+place.getManagerId()+":"+place.getDefaultLocationId()+":"+place.getOfficeName();
        }

        @Override
        public LoginManagerPlace getPlace(String token) {
            // Checking the token: if correct return the login associated login manager place, else send to login (token is null);
            LoginManagerPlace place = null;
            String[] tokenElems = token.split(":");
            if (tokenElems != null && tokenElems.length == 1) {
                place = new LoginManagerPlace(tokenElems[0], null, null, null);
            }
            return place;
        }
    }

}
