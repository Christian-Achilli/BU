package com.kp.malice.client.amministrazione;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class AmministrazioneActivityMapper implements ActivityMapper {

    private Provider<AmministrazioneActivity> activityProvider;

    @Inject
    public AmministrazioneActivityMapper(Provider<AmministrazioneActivity> activityProvider) {
        super();
        this.activityProvider = activityProvider;

    }

    @Override
    public Activity getActivity(Place place) {
        //        if (place instanceof AmministratorePlace)
        //            return activityProvider.get().withPlace(place);
        //        else
        return null;
    }

}
