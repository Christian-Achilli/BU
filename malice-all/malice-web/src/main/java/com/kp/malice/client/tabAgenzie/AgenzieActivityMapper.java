package com.kp.malice.client.tabAgenzie;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.kp.malice.client.place.UserPlace;

public class AgenzieActivityMapper implements ActivityMapper {

	private Provider<AgenzieActivity> activityProvider;

	@Inject
	public AgenzieActivityMapper(Provider<AgenzieActivity> activityProvider) {
		super();
		this.activityProvider = activityProvider;

	}

	@Override
	public Activity getActivity(Place place) {
		return activityProvider.get().withPlace((UserPlace) place);
	}

}
