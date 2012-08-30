package com.kp.marsh.ebt.client.webapp.activities;

import java.util.ArrayList;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.kp.marsh.ebt.client.webapp.CalculateIndicatorInfoServiceAsync;
import com.kp.marsh.ebt.client.webapp.DataServiceAsync;
import com.kp.marsh.ebt.client.webapp.events.GaugesViewSelectedEvent;
import com.kp.marsh.ebt.client.webapp.events.GaugesViewSelectedHandler;
import com.kp.marsh.ebt.client.webapp.events.HistogramsViewSelectedEvent;
import com.kp.marsh.ebt.client.webapp.events.HistogramsViewSelectedHandler;
import com.kp.marsh.ebt.client.webapp.events.LocationSelectedEvent;
import com.kp.marsh.ebt.client.webapp.events.LocationSelectedHandler;
import com.kp.marsh.ebt.client.webapp.events.PillsViewSelectedEvent;
import com.kp.marsh.ebt.client.webapp.events.PillsViewSelectedHandler;
import com.kp.marsh.ebt.client.webapp.events.StopAnimationEvent;
import com.kp.marsh.ebt.client.webapp.place.LoginManagerPlace;
import com.kp.marsh.ebt.client.webapp.ui.activityintf.IGaugesViewDisplay;
import com.kp.marsh.ebt.shared.dto.SintesiDto;

public class GaugesActivity extends AbstractActivity implements
		LocationSelectedHandler, GaugesViewSelectedHandler,
		HistogramsViewSelectedHandler, PillsViewSelectedHandler {

	private final IGaugesViewDisplay display;

	private final CalculateIndicatorInfoServiceAsync indicatorService;
	
	private final DataServiceAsync dataService;

	private final EventBus eventBus;

	private String lastDisplayedId = "-1254"/*dummy*/; // the information owner id that is
											// parent of the data currently
											// being displayed in the view
	// lastDisplayiedId = currentlyDisplayedId quando
	// display.asWidget().isVisible() == true
	private String currentlyDisplayedId = null; // the information owner id that
												// is parent of the data
												// currently being displayed in
												// other view
	private String officeName = null; // nome dell'ufficio selezionato
	
	private String ceName = null;// nome del ce selezionato
	
	private String gcName = null; // nome del gruppo commerciale selezionato

	@Inject
	public GaugesActivity(IGaugesViewDisplay display,
			CalculateIndicatorInfoServiceAsync indicatorService,
			DataServiceAsync ds,
			EventBus eventBus) {
		this.indicatorService = indicatorService;
		this.display = display;
		this.dataService = ds;
		this.eventBus = eventBus;

	}

	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		// TODO controllo della validità della sessione, else go to log in place
		eventBus.addHandler(LocationSelectedEvent.TYPE, this);
		eventBus.addHandler(GaugesViewSelectedEvent.TYPE, this);
		eventBus.addHandler(HistogramsViewSelectedEvent.TYPE, this);
		eventBus.addHandler(PillsViewSelectedEvent.TYPE, this);
		panel.setWidget(display.asWidget());

		dataService.getLobNames(new AsyncCallback<ArrayList<String>>() {
			
			@Override
			public void onSuccess(ArrayList<String> result) {
				display.initView();
				display.setLobNames(result.toArray(new String[result.size()]));

			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Errore in getLobNames durante lo start()di GaugesActivity");
				caught.printStackTrace();
			}
		});
		
	}

	// Gestione dei place. Metodi invocati dagli Activity Mapper che sono
	// interessati a far partire questa activity

	public GaugesActivity withPlace(final LoginManagerPlace place) {
		// Dal manger id bisogna ricavare l'ufficio che gli corrisponde e poi
		// mostrarne i valori degli indici sui manometri
		this.currentlyDisplayedId = place.getDefaultLocationId();
		// serve per attivare la view dei gauges che in seguito a logout con
		// istogrammi e login è disabilitata
		display.asWidget().setVisible(true);
		officeName = place.getOfficeName();
		asyncUpdateDisplay(Integer.valueOf(this.currentlyDisplayedId));
		return this;

	}

	// Gestione dell'evento di location selected: quando un utente seleziona un
	// ufficio o un ce nel cell browser

	@Override
	public void onNewLocationSelected(LocationSelectedEvent event) {
		currentlyDisplayedId = event.getSelectionId(); // the information owner id of
		// the office or CE being shown
		ceName = event.getSelectedDescriptionCE();
		gcName = event.getSelectedDescriptionGC();
		officeName = event.getSelectedDescriptionUfficio();
		asyncUpdateDisplay(Integer.valueOf(currentlyDisplayedId));
	}

	private void asyncUpdateDisplay(int locationId) {
		if(display.asWidget().isVisible()) {
			if( Integer.valueOf(lastDisplayedId) != locationId) {
				lastDisplayedId = currentlyDisplayedId;
				display.showLoaders(true);

				indicatorService.calcolaIndiciByOwnerId(locationId, new AsyncCallback<ArrayList<SintesiDto>>() {
					@Override
					public void onSuccess(ArrayList<SintesiDto> result) {
						display.updateData(result); // il display disegna i manometri e istogrammi per ogni area di business prendendo i dati dalla lista result
						display.setStatusBar(officeName, ceName, gcName);
						display.showLoaders(false);
						stopAggiornaAnimation(); // invia l'evento che è poi ricevuto dall'header bar activity per fermare l'icona di aggiorna che ruota

					}

					@Override
					public void onFailure(Throwable caught) {
						display.showLoaders(false);
						stopAggiornaAnimation(); // invia l'evento che è poi ricevuto dall'header bar activity per fermare l'icona di aggiorna che ruota

					}
				});

			} else {
				stopAggiornaAnimation(); // invia l'evento che è poi ricevuto dall'header bar activity per fermare l'icona di aggiorna che ruota

			}
		}

	}

	private void stopAggiornaAnimation() {
		eventBus.fireEvent( new StopAnimationEvent() );
		
	}
	
	@Override
	public void showHistograms(
			HistogramsViewSelectedEvent histogramsViewSelectedEvent) {
		display.asWidget().setVisible(false);

	}

	@Override
	public void showGauges(GaugesViewSelectedEvent gaugesViewSelectedEvent) {
		display.asWidget().setVisible(true);
		asyncUpdateDisplay(Integer.valueOf(currentlyDisplayedId));

	}

	@Override
	public void showPills(PillsViewSelectedEvent e) {
		display.asWidget().setVisible(false);
	}

}