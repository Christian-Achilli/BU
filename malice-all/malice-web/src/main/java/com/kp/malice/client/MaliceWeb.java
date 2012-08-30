package com.kp.malice.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.kp.malice.client.gin.MaliceWebGinjector;
import com.kp.malice.client.header.HeaderActivityMapper;
import com.kp.malice.client.place.AgenteRmaPlace;
import com.kp.malice.client.place.SuperAgenteRmaPlace;
import com.kp.malice.client.place.UserPlace;
import com.kp.malice.client.tabAgenzie.AgenzieActivityMapper;
import com.kp.malice.client.tabBenvenuto.BenvenutoActivityMapper;
import com.kp.malice.client.tabChiusure.ChiusureActivityMapper;
import com.kp.malice.client.tabGiornata.GiornataActivityMapper;
import com.kp.malice.client.tabSintesi.SintesiActivityMapper;
import com.kp.malice.client.tabTitoli.TitoliActivityMapper;
import com.kp.malice.client.ui.resources.ListeResources;
import com.kp.malice.client.ui.resources.MaliceResources;
import com.kp.malice.client.ui.resources.SimplePagerMalice.PagerResources;
import com.kp.malice.shared.LoggedInUserInfo;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MaliceWeb implements EntryPoint {

	private final MaliceWebGinjector injector = GWT.create(MaliceWebGinjector.class);

	static {
		MaliceResources.INSTANCE.main().ensureInjected();
		ListeResources.INSTANCE.cellTableStyle().ensureInjected();
		PagerResources.INSTANCE.simplePagerStyle().ensureInjected();
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		setupHistory();
		startApplication();
		startPingingServer();
	}

	private void setupHistory() {
		Window.addWindowClosingHandler(new ClosingHandler() {
			boolean reloading = false;

			@Override
			public void onWindowClosing(ClosingEvent event) {
				if (!reloading) {
					String userAgent = Window.Navigator.getUserAgent();
					if (userAgent.contains("MSIE")) {
						if (!Window.confirm("Vuoi veramente uscire?")) {
							reloading = true;
							Window.Location.reload(); // For IE
						}
					} else {
						event.setMessage(""); // For other browser
					}
				}
			}
		});
	}

	private void startApplication() {
		final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(injector.getMalicePlaceHistoryMapper());
		injector.getAuthService().retrieveLoggedInUserInfo(new AsyncCallback<LoggedInUserInfo>() {

			@Override
			public void onSuccess(LoggedInUserInfo arg0) {
				UserPlace place = UserPlace.createFromString(arg0);
//				if (place instanceof AgenteRmaPlace) {
					GWT.log("Log in AGENTE");
					bindActivitiesAgenteToDisplays();
					RootLayoutPanel.get().add(injector.getLayoutApplicazioneAgente());
					visualizzaPannelloAgente();
//				} else if (place instanceof SuperAgenteRmaPlace) {
//					bindActivitiesAgenteToDisplays();
//					RootLayoutPanel.get().add(injector.getLayoutApplicazioneAgente());
//				}
				// else if (place instanceof AmministratorePlace) {
				// bindActivitiesAmministratoreToDisplay();
				// RootLayoutPanel.get().add(injector.getLayoutApplicazioneAmministrazione());
				// }
				historyHandler.register(injector.getPlaceController(), injector.getEventBus(), place);
				historyHandler.handleCurrentHistory();
			}

			@Override
			public void onFailure(Throwable arg0) {
				GWT.log("Errore nel recupero dello username");
				DOM.removeChild(RootPanel.getBodyElement(), DOM.getElementById("loading"));
			}
		});

	}

	private void startPingingServer() {
		Timer keepAliveServerSession = new Timer() {

			@Override
			public void run() {
				injector.getAuthService().pingToKeepAliveServerSession(new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
					}

					@Override
					public void onFailure(Throwable caught) {
					}
				});
			}
		};
		keepAliveServerSession.scheduleRepeating(300000);
	}

	// private void bindActivitiesAmministratoreToDisplay() {
	// AmministrazioneActivityMapper amministrazioneActivityMapper =
	// injector.getAmministrazioneMapper();
	// ActivityManager amministrazioneActivityManager = new
	// ActivityManager(amministrazioneActivityMapper,
	// injector.getEventBus());
	// amministrazioneActivityManager.setDisplay(injector.getLayoutApplicazioneAmministrazione().getTab1Display());
	// }

	private void bindActivitiesAgenteToDisplays() {
		AgenzieActivityMapper selectAgenziaMapper = injector.getAgenzieActivityMapper();
		ActivityManager activityManager = new ActivityManager(selectAgenziaMapper, injector.getEventBus());
		activityManager.setDisplay(injector.getLayoutApplicazioneAgente().getSelectAgenziaDisplay());

		HeaderActivityMapper headerActivityMapper = injector.getHeaderActivityMapper();
		ActivityManager headerActivityManager = new ActivityManager(headerActivityMapper, injector.getEventBus());
		headerActivityManager.setDisplay(injector.getLayoutApplicazioneAgente().getToolbarsDisplay());

		BenvenutoActivityMapper benvenutoActivityMapper = injector.getBenvenutoActivityMapper();
		ActivityManager benvenutoActivityManager = new ActivityManager(benvenutoActivityMapper, injector.getEventBus());
		benvenutoActivityManager.setDisplay(injector.getLayoutApplicazioneAgente().getBenvenutoDisplay());

		GiornataActivityMapper incassoActivityMapper = injector.getGiornataActivityMapper();
		ActivityManager giornataActivityManager = new ActivityManager(incassoActivityMapper, injector.getEventBus());
		giornataActivityManager.setDisplay(injector.getLayoutApplicazioneAgente().getIncassiDisplay());

		TitoliActivityMapper titoliActivityMapper = injector.getTitoliActivityMapper();
		ActivityManager titoliActivityManager = new ActivityManager(titoliActivityMapper, injector.getEventBus());
		titoliActivityManager.setDisplay(injector.getLayoutApplicazioneAgente().getTitoliDisplay());

		ChiusureActivityMapper chiusureActivityMapper = injector.getChiusureActivityMapper();
		ActivityManager chiusureActivityManager = new ActivityManager(chiusureActivityMapper, injector.getEventBus());
		chiusureActivityManager.setDisplay(injector.getLayoutApplicazioneAgente().getChiusureDisplay());

		SintesiActivityMapper statisticheActivityMapper = injector.getStatisticheActivityMapper();
		ActivityManager statisticheActivityManager = new ActivityManager(statisticheActivityMapper, injector.getEventBus());
		statisticheActivityManager.setDisplay(injector.getLayoutApplicazioneAgente().getStatisticheDisplay());

	}

	private void visualizzaPannelloAgente() {
		injector.getLayoutApplicazioneAgente().visualizza();
	}

	// private void visualizzaPannelloAmministrazione() {
	// injector.getLayoutApplicazioneAmministrazione().visualizza();
	// }

}