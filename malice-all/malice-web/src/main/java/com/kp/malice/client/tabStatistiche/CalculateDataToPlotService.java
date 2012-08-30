package com.kp.malice.client.tabStatistiche;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("calculate")
public interface CalculateDataToPlotService extends RemoteService {
		  public String calculateData(); 
}
