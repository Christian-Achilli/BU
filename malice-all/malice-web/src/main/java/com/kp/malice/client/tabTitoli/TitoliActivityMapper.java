package com.kp.malice.client.tabTitoli;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.kp.malice.client.place.AgenteRmaPlace;

public class TitoliActivityMapper implements ActivityMapper {

    private Provider<TitoliActivity> activityProvider;

    @Inject
    public TitoliActivityMapper(Provider<TitoliActivity> activityProvider) {
        super();
        this.activityProvider = activityProvider;

    }

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof AgenteRmaPlace)
            return activityProvider.get().withPlace(place); // la lista titoli � visibile a tutti gli utenti
        else
            return null;
    }

}
