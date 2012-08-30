/**
 * 
 */
package com.kp.malice.client.place;

import java.util.Date;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.kp.malice.shared.LoggedInUserInfo;

/**
 * The user that is logged in
 *
 */
public abstract class UserPlace extends Place {

    private String userName; // the name To be displayed

    private String role; // the role of the user logged in

    public UserPlace(String userName, String role) {
        this.userName = userName;
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public String getRole() {
        return role;
    }

    @Prefix("welcome")
    public static class Tokenizer implements PlaceTokenizer<UserPlace> {

        public Tokenizer() {
        }

        public String getToken(UserPlace place) {
            return place.getUserName() + ", logged in at " + new Date().toString();
        }

        public UserPlace getPlace(String token) {
            return null;
        }

    }

    public static UserPlace createFromString(LoggedInUserInfo arg0) {
        UserPlace result = null;
        if (arg0.getRole().equals("ROLE_AGENTE")) {
            result = new AgenteRmaPlace(arg0.getDisplayName());
        } else if (arg0.getRole().equals("ROLE_ADMIN")) {
            result = new AmministratorePlace(arg0.getDisplayName());
        }
        else if (arg0.getRole().equals("ROLE_SUPER_AGENTE")) {
            result = new SuperAgenteRmaPlace(arg0.getDisplayName());
        }
        return result;
    }

}
