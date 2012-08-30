package com.kp.marsh.ebt.client.webapp.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.kp.marsh.ebt.client.webapp.activities.HeaderBarActivity;
import com.kp.marsh.ebt.client.webapp.place.LoginCEPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginManagerPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginManagerWithClientsPlace;

public class HeaderActivityMapper implements ActivityMapper {


	private Provider<HeaderBarActivity> headerManagerActivityProvider;

	@Inject
	public HeaderActivityMapper(Provider<HeaderBarActivity> headerManagerActivityProvider) {
		super();

		this.headerManagerActivityProvider=headerManagerActivityProvider;
	}


	@Override
	public Activity getActivity(Place place) {

		if(place instanceof LoginManagerWithClientsPlace) {
			if(null == ((LoginManagerPlace)place).getDefaultLocationId() || "".equals(((LoginManagerPlace)place).getDefaultLocationId())) {
				return null;
			} else
				return headerManagerActivityProvider.get().withPlace((LoginManagerWithClientsPlace)place);
		}
		else if (place instanceof LoginManagerPlace) {
			if(null == ((LoginManagerPlace)place).getDefaultLocationId() || "".equals(((LoginManagerPlace)place).getDefaultLocationId())) {
				return null;
			} else
				return headerManagerActivityProvider.get().withPlace((LoginManagerPlace)place);
		}
		else if (place instanceof LoginCEPlace) {
			if(null == ((LoginCEPlace)place).getClientExecId() || "".equals(((LoginCEPlace)place).getClientExecId())) {
				return null;
			} else
			return headerManagerActivityProvider.get().withPlace((LoginCEPlace)place);
		}
		else
			return null;

	}

}
