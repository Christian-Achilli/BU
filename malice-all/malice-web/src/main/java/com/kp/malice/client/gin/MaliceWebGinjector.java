package com.kp.malice.client.gin;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.kp.malice.client.AuthServiceAsync;
import com.kp.malice.client.header.HeaderActivityMapper;
import com.kp.malice.client.mvp.MalicePlaceHistoryMapper;
import com.kp.malice.client.tabBenvenuto.BenvenutoActivityMapper;
import com.kp.malice.client.tabChiusure.ChiusureActivityMapper;
import com.kp.malice.client.tabIncassi.IncassiActivityMapper;
import com.kp.malice.client.tabStatistiche.StatisticheActivityMapper;
import com.kp.malice.client.tabTitoli.TitoliActivityMapper;
import com.kp.malice.client.ui.widget.IAppLoadingViewDisplay;
import com.kp.malice.client.ui.widget.MainAgenteLayout;

@GinModules(MaliceGinClientModule.class)
public interface MaliceWebGinjector extends Ginjector {

	// public AmministrazioneActivityMapper getAmministrazioneMapper();

	public IAppLoadingViewDisplay getAppLoadingDisplay();

	public AuthServiceAsync getAuthService();

	public BenvenutoActivityMapper getBenvenutoActivityMapper();

	public ChiusureActivityMapper getChiusureActivityMapper();

	// public TitoliActivityMapper getDettaglioTitoloActivityMapper();

	public EventBus getEventBus();

	public HeaderActivityMapper getHeaderActivityMapper();

	public TitoliActivityMapper getTitoliActivityMapper();

	public IncassiActivityMapper getIncassiActivityMapper();

	public MainAgenteLayout getLayoutApplicazioneAgente();

	// public MainAmministratoreLayout getLayoutApplicazioneAmministrazione();

	public MalicePlaceHistoryMapper getMalicePlaceHistoryMapper();

	public PlaceController getPlaceController();

	public StatisticheActivityMapper getStatisticheActivityMapper();

}
