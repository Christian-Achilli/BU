package com.kp.malice.client.header;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.kp.malice.client.place.UserPlace;

public class HeaderActivityMapper implements ActivityMapper {

    private Provider<HeaderActivity> headerActivityProvider;

    @Inject
    public HeaderActivityMapper(Provider<HeaderActivity> headerActivityProvider) {
        super();
        this.headerActivityProvider = headerActivityProvider;

    }

    @Override
    public Activity getActivity(Place place) {
        return headerActivityProvider.get().withPlace((UserPlace) place);
    }

}
