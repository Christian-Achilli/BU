package com.kp.marsh.ebt.client.webapp.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.kp.marsh.ebt.client.webapp.activities.AuthenticationActivity;
import com.kp.marsh.ebt.client.webapp.place.AuthenticationPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginCEPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginManagerPlace;
import com.kp.marsh.ebt.client.webapp.place.LogoutPlace;

public class AuthenticationActivityMapper implements ActivityMapper {

    private Provider<AuthenticationActivity> authenticationActivityProvider;

    @Inject
    public AuthenticationActivityMapper(Provider<AuthenticationActivity> authenticationActivityProvider) {
        super();
        this.authenticationActivityProvider = authenticationActivityProvider;

    }

    @Override
    public Activity getActivity(Place place) {

        if (place instanceof AuthenticationPlace) { // nel caso il login sia avvenuto con successo e il ruolo dell'utente sia manager, come default faccio vedere i gauge
            return authenticationActivityProvider.get().withPlace((AuthenticationPlace) place);

        }
        if (place instanceof LogoutPlace) { // nel caso il login sia avvenuto con successo e il ruolo dell'utente sia manager, come default faccio vedere i gauge
            return authenticationActivityProvider.get().withPlace((LogoutPlace) place);

        }
        if (place instanceof LoginManagerPlace
                && (null == ((LoginManagerPlace) place).getDefaultLocationId() || "".equals(((LoginManagerPlace) place)
                        .getDefaultLocationId()))) {
            return authenticationActivityProvider.get().withPlace(new AuthenticationPlace());

        } else if (place instanceof LoginCEPlace
                && (null == ((LoginCEPlace) place).getClientExecId() || "".equals(((LoginCEPlace) place)
                        .getClientExecId()))) {
            return authenticationActivityProvider.get().withPlace(new AuthenticationPlace());
        } else
            return null;
    }

}
