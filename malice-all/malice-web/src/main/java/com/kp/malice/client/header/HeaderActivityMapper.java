package com.kp.malice.client.header;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.kp.malice.client.place.AgenteRmaPlace;
import com.kp.malice.client.place.SuperAgenteRmaPlace;
import com.kp.malice.client.place.UserPlace;
import com.kp.malice.client.tabAgenzie.AgenzieActivity;

public class HeaderActivityMapper implements ActivityMapper {

	private Provider<HeaderActivity> headerActivityProvider;
	private Provider<Activity> selectAgenziaActivityProvider;

	@Inject
	public HeaderActivityMapper(Provider<HeaderActivity> headerActivityProvider, Provider<Activity> selectAgenziaActivityProvider) {
		super();
		this.headerActivityProvider = headerActivityProvider;
		this.selectAgenziaActivityProvider = selectAgenziaActivityProvider;
	}

	@Override
	public Activity getActivity(Place place) {
//		if (place instanceof AgenteRmaPlace)
			return headerActivityProvider.get().withPlace((UserPlace) place);
//		else if (place instanceof SuperAgenteRmaPlace)
//			return selectAgenziaActivityProvider.get().withPlace((UserPlace) place);
//		else
//			return null;
	}

}
