package com.kp.malice.client.tabIncassi;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.kp.malice.client.place.AgenteRmaPlace;
import com.kp.malice.client.tabBenvenuto.BenvenutoActivity;

public class IncassiActivityMapper implements ActivityMapper {

    private Provider<IncassiActivity> activityProvider;

    @Inject
    public IncassiActivityMapper(Provider<IncassiActivity> activityProvider) {
        super();
        this.activityProvider = activityProvider;
    }

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof AgenteRmaPlace)
            return activityProvider.get().withPlace(place);
        else
            return null;
    }

}
