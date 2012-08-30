package com.kp.malice.client.tabChiusure;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.kp.malice.client.place.AgenteRmaPlace;
import com.kp.malice.client.place.UserPlace;

public class ChiusureActivityMapper implements ActivityMapper {

    private Provider<ChiusureActivity> activityProvider;

    @Inject
    public ChiusureActivityMapper(Provider<ChiusureActivity> activityProvider) {
        super();
        this.activityProvider = activityProvider;

    }

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof AgenteRmaPlace)
            return activityProvider.get().withPlace((UserPlace) place);
        else
            return null;
    }

}
