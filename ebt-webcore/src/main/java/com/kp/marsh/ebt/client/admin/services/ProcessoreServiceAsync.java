package com.kp.marsh.ebt.client.admin.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProcessoreServiceAsync {

	void importaAnagrafica(String fileCsvAnagrafiche, AsyncCallback<String> asyncCallback);

	void importaAchieved(String fileCsvAchieved, AsyncCallback<String> callback);

	void getParametriDB(AsyncCallback<String> callback);

	void importaGruppiCommerciali(String fileName,
			AsyncCallback<String> callback);

	void configuraDb(String fileDbConnectionProperties, AsyncCallback<String> callback);
	

}
