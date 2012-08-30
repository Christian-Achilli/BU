package com.kp.malice.client.gin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.RequestTransport;
import com.kp.malice.client.CustomRequestTransport;
import com.kp.malice.client.header.HeaderActivityMapper;
import com.kp.malice.client.header.HeaderView;
import com.kp.malice.client.header.IHeaderViewDisplay;
import com.kp.malice.client.tabBenvenuto.BenvenutoActivityMapper;
import com.kp.malice.client.tabBenvenuto.BenvenutoView;
import com.kp.malice.client.tabBenvenuto.IBenvenutoViewDisplay;
import com.kp.malice.client.tabChiusure.ChiusureActivityMapper;
import com.kp.malice.client.tabChiusure.ChiusureView;
import com.kp.malice.client.tabChiusure.IChiusureViewDisplay;
import com.kp.malice.client.tabIncassi.IIncassiViewDisplay;
import com.kp.malice.client.tabIncassi.IncassiActivityMapper;
import com.kp.malice.client.tabIncassi.IncassiView;
import com.kp.malice.client.tabStatistiche.IStatisticheViewDisplay;
import com.kp.malice.client.tabStatistiche.StatisticheView;
import com.kp.malice.client.tabTitoli.ITitoliViewDisplay;
import com.kp.malice.client.tabTitoli.TitoliActivityMapper;
import com.kp.malice.client.tabTitoli.TitoliView;
import com.kp.malice.client.ui.widget.AppLoadingView;
import com.kp.malice.client.ui.widget.IAppLoadingViewDisplay;
import com.kp.malice.client.ui.widget.MainAgenteLayout;
import com.kp.malice.shared.MaliceRequestFactory;
import com.kp.malice.shared.MaliceRequestFactory.ServiziPortale;

public class MaliceGinClientModule extends AbstractGinModule {

	@Override
	protected void configure() {
		// event bus
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		// activity mappers
		bind(HeaderActivityMapper.class).in(Singleton.class);
		bind(TitoliActivityMapper.class).in(Singleton.class);
		bind(IncassiActivityMapper.class).in(Singleton.class);
		bind(BenvenutoActivityMapper.class).in(Singleton.class);
		bind(ChiusureActivityMapper.class).in(Singleton.class);
		// bind(StatisticheActivityMapper.class).in(Singleton.class);
		// bind(AmministrazioneActivityMapper.class).in(Singleton.class);
		bind(PlaceController.class).to(InjectablePlaceController.class).in(Singleton.class);
		// views
		bind(IHeaderViewDisplay.class).to(HeaderView.class).in(Singleton.class);
		bind(ITitoliViewDisplay.class).to(TitoliView.class).in(Singleton.class);
		bind(IIncassiViewDisplay.class).to(IncassiView.class).in(Singleton.class);
		bind(IBenvenutoViewDisplay.class).to(BenvenutoView.class).in(Singleton.class);
		bind(IStatisticheViewDisplay.class).to(StatisticheView.class).in(Singleton.class);
		bind(IChiusureViewDisplay.class).to(ChiusureView.class).in(Singleton.class);
		bind(IAppLoadingViewDisplay.class).to(AppLoadingView.class).in(Singleton.class);
		// bind(IAmministrazioneViewDisplay.class).to(AmministrazioneView.class).in(Singleton.class);
		bind(MainAgenteLayout.class).in(Singleton.class);
		// bind(MainAmministratoreLayout.class).in(Singleton.class);
	}

	@Provides
	public ServiziPortale createServiziTitolo(MaliceRequestFactory factory) {
		return factory.serviziPortale();
	}

	@Provides
	@Singleton
	public RequestTransport createRequestTransport() {
		return new CustomRequestTransport();
	}

	@Provides
	@Singleton
	public MaliceRequestFactory createMaliceRequestFactory(EventBus eventBus, RequestTransport rt) {
		MaliceRequestFactory factory = GWT.create(MaliceRequestFactory.class);
		factory.initialize(eventBus, rt);
		return factory;
	}

}
