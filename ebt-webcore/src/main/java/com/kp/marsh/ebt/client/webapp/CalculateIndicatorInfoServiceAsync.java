package com.kp.marsh.ebt.client.webapp;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.kp.marsh.ebt.shared.dto.SintesiDto;


public interface CalculateIndicatorInfoServiceAsync {

	void calcolaIndiciByOwnerId(int id, AsyncCallback<ArrayList<SintesiDto>> callback);

}