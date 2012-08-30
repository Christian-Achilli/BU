package com.kp.marsh.ebt.client.webapp.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.kp.marsh.ebt.client.webapp.activities.GaugesActivity;
import com.kp.marsh.ebt.client.webapp.place.LoginManagerPlace;

public class GaugesActivityMapper implements ActivityMapper {

	private Provider<GaugesActivity> gaugesActivityProvider;
	

	@Inject
	public GaugesActivityMapper(Provider<GaugesActivity> gaugesActivityProvider) {
		super();
		this.gaugesActivityProvider = gaugesActivityProvider;

	}




	@Override
	public Activity getActivity(Place place) {

		if (place instanceof LoginManagerPlace) {  // nel caso il login sia avvenuto con successo e il ruolo dell'utente sia manager, come default faccio vedere i gauge
			if(null == ((LoginManagerPlace)place).getDefaultLocationId() || "".equals(((LoginManagerPlace)place).getDefaultLocationId())) {
				return null;
			} else return gaugesActivityProvider.get().withPlace((LoginManagerPlace)place);
		}else
			return null;

	}

}
