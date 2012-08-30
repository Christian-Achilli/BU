package com.kp.malice.client.tabGiornata;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.kp.malice.client.place.AgenteRmaPlace;
import com.kp.malice.client.tabBenvenuto.BenvenutoActivity;

public class GiornataActivityMapper implements ActivityMapper {

    private Provider<GiornataActivity> activityProvider;

    @Inject
    public GiornataActivityMapper(Provider<GiornataActivity> activityProvider) {
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
