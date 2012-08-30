package com.kp.marsh.ebt.client.webapp.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.kp.marsh.ebt.client.webapp.activities.InsertPotentialActivity;
import com.kp.marsh.ebt.client.webapp.place.LoginCEPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginManagerPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginManagerWithClientsPlace;

public class PillActivityMapper implements ActivityMapper {

    private Provider<InsertPotentialActivity> pillsActivityProvider;

    @Inject
    public PillActivityMapper(Provider<InsertPotentialActivity> pillsActivityProvider) {
        super();
        this.pillsActivityProvider = pillsActivityProvider;
    }

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof LoginManagerWithClientsPlace) {
            if (null == ((LoginManagerPlace) place).getDefaultLocationId()
                    || "".equals(((LoginManagerPlace) place).getDefaultLocationId())) {
                return null;
            } else
                return pillsActivityProvider.get().withPlace((LoginManagerWithClientsPlace) place);
        } else if (place instanceof LoginCEPlace) {
            if (null == ((LoginCEPlace) place).getClientExecId() || "".equals(((LoginCEPlace) place).getClientExecId())) {
                return null;
            } else
                return pillsActivityProvider.get().withPlace((LoginCEPlace) place);
        } else
            return null;
    }

}
