package com.kp.malice.client.gin;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.kp.malice.client.AuthServiceAsync;
import com.kp.malice.client.amministrazione.AmministrazioneActivityMapper;
import com.kp.malice.client.amministrazione.MainAmministratoreLayout;
import com.kp.malice.client.header.HeaderActivityMapper;
import com.kp.malice.client.mvp.MalicePlaceHistoryMapper;
import com.kp.malice.client.tabAgenzie.AgenzieActivityMapper;
import com.kp.malice.client.tabBenvenuto.BenvenutoActivityMapper;
import com.kp.malice.client.tabChiusure.ChiusureActivityMapper;
import com.kp.malice.client.tabGiornata.GiornataActivityMapper;
import com.kp.malice.client.tabSintesi.SintesiActivityMapper;
import com.kp.malice.client.tabTitoli.TitoliActivityMapper;
import com.kp.malice.client.ui.commonWidgets.IAppLoadingViewDisplay;
import com.kp.malice.client.ui.commonWidgets.MainAgenteLayout;

@GinModules(MaliceGinClientModule.class)
public interface MaliceWebGinjector extends Ginjector {

    public IAppLoadingViewDisplay getAppLoadingDisplay();

    public AuthServiceAsync getAuthService();

    public BenvenutoActivityMapper getBenvenutoActivityMapper();

    public ChiusureActivityMapper getChiusureActivityMapper();

    public AmministrazioneActivityMapper getAmministrazioneActivityMapper();

    public EventBus getEventBus();

    public AgenzieActivityMapper getAgenzieActivityMapper();

    public HeaderActivityMapper getHeaderActivityMapper();

    public TitoliActivityMapper getTitoliActivityMapper();

    public GiornataActivityMapper getGiornataActivityMapper();

    public MainAgenteLayout getLayoutApplicazioneAgente();

    public MainAmministratoreLayout getMainAmministratoreLayout();

    public MalicePlaceHistoryMapper getMalicePlaceHistoryMapper();

    public PlaceController getPlaceController();

    public SintesiActivityMapper getStatisticheActivityMapper();

}
