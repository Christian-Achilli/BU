package com.kp.marsh.ebt.client.webapp.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;

/**
 * Mapping an Activity to a Place
 * 
 * 
 */
public abstract class ActivityPlace<T extends Activity> extends Place {

	protected T activity;

	public ActivityPlace(T activity) {
		this.activity = activity;
		setPlaceName("");
	}

	public T getActivity() {
		return activity;
	}

	private String placeName;
	
	public void setPlaceName(String token) {
		this.placeName = token;
	}

	public String getPlaceName() {
		return placeName;
	}

}
