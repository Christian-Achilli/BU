package com.kp.marsh.ebt.client.webapp.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class LogoutPlace extends Place {

    public LogoutPlace() {
        super();

    }

    @Prefix("logout")
    public static class Tokenizer implements PlaceTokenizer<LogoutPlace> {

        public Tokenizer() {
        }

        public String getToken(LogoutPlace place) {
            return "bye-bye";
        }

        public LogoutPlace getPlace(String token) {
            return new LogoutPlace();
        }

    }
}
