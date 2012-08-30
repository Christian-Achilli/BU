package com.kp.marsh.ebt.client.webapp.gin;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.kp.marsh.ebt.client.webapp.AuthenticationServiceAsync;
import com.kp.marsh.ebt.client.webapp.CalculateIndicatorInfoServiceAsync;
import com.kp.marsh.ebt.client.webapp.DataServiceAsync;
import com.kp.marsh.ebt.client.webapp.mvp.AuthenticationActivityMapper;
import com.kp.marsh.ebt.client.webapp.mvp.GaugesActivityMapper;
import com.kp.marsh.ebt.client.webapp.mvp.HeaderActivityMapper;
import com.kp.marsh.ebt.client.webapp.mvp.HistogramActivityMapper;
import com.kp.marsh.ebt.client.webapp.mvp.PillActivityMapper;

@GinModules(ManagerWidgetsClientModule.class)
public interface ManagerWidgetsGinjector extends Ginjector {

    public AuthenticationServiceAsync getAuthenticationService();

    public DataServiceAsync getDataService();

    public CalculateIndicatorInfoServiceAsync getCalculatorService();

    public HeaderActivityMapper getHeaderActivityMapper();

    public GaugesActivityMapper getGaugesActivityMapper();

    public HistogramActivityMapper getHistogramActivityMapper();

    public AuthenticationActivityMapper getAuthActivityMapper();

    public PlaceController getPlaceController();

    public EventBus getEventBus();

    public PillActivityMapper getPillActivityMapper();

}
