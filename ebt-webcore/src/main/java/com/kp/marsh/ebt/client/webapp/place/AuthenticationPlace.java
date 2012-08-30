package com.kp.marsh.ebt.client.webapp.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class AuthenticationPlace extends Place {

    public AuthenticationPlace() {
    }

    @Prefix("auth")
    public static class Tokenizer implements PlaceTokenizer<AuthenticationPlace> {

        @Override
        public String getToken(AuthenticationPlace place) {
            return "::*";
        }

        @Override
        public AuthenticationPlace getPlace(String token) {
            return new AuthenticationPlace();
        }
    }

}
