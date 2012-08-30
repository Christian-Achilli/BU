package com.kp.malice.client.tabStatistiche;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CalculateDataToPlotServiceAsync {
	void calculateData(AsyncCallback<String> callback);
}
