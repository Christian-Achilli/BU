package com.kp.marsh.ebt.client.webapp.mvp;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import com.kp.marsh.ebt.client.webapp.place.AuthenticationPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginCEPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginManagerPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginManagerWithClientsPlace;
import com.kp.marsh.ebt.client.webapp.place.LogoutPlace;

@WithTokenizers({ AuthenticationPlace.Tokenizer.class, LoginCEPlace.Tokenizer.class, LoginManagerPlace.Tokenizer.class,
        LoginManagerWithClientsPlace.Tokenizer.class, LogoutPlace.Tokenizer.class })
public interface ApplicationPlaceHistoryMapper extends PlaceHistoryMapper {
}
