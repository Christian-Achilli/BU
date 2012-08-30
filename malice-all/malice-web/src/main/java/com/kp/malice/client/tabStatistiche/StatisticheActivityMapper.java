package com.kp.malice.client.tabStatistiche;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.kp.malice.client.place.AgenteRmaPlace;

public class StatisticheActivityMapper implements ActivityMapper {

	private Provider<StatisticheActivity> activityProvider;

	@Inject
	public StatisticheActivityMapper(Provider<StatisticheActivity> activityProvider) {
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
