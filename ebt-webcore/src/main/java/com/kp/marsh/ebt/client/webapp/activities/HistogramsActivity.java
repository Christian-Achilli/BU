package com.kp.marsh.ebt.client.webapp.activities;

import java.util.ArrayList;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.kp.marsh.ebt.client.webapp.DataServiceAsync;
import com.kp.marsh.ebt.client.webapp.events.AggiornaTotColonnaIstoEvent;
import com.kp.marsh.ebt.client.webapp.events.ChangePageEvent;
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
import com.kp.marsh.ebt.client.webapp.ui.activityintf.IHistogramsViewDisplay;
import com.kp.marsh.ebt.shared.dto.LightInfoOwnerDto;
import com.kp.marsh.ebt.shared.dto.LineOfBusiness;
import com.kp.marsh.ebt.shared.dto.ProductInfoDTO;

public class HistogramsActivity extends AbstractActivity  implements IHistogramsViewDisplay.Listener, LocationSelectedHandler, GaugesViewSelectedHandler, HistogramsViewSelectedHandler, PillsViewSelectedHandler {

	private final EventBus eventBus;

	private final IHistogramsViewDisplay display;

	private final DataServiceAsync dataService;

	private String lastDisplayedId = "-1254"/*dummy*/; // the information owner id that is parent of the data currently being displayed in the view
	// lastDisplayedId = currentlyDisplayedId quando display.asWidget().isVisible() == true
	private String currentlyDisplayedId = null; // the information owner id that is parent of the data currently being displayed in other view

	private int displayingPage = 0; // the page currently being displayed

	private LightInfoOwnerDto[] displayingData; // i dati visualizzati in pagina

	private ArrayList<ArrayList<LineOfBusiness>> lobsByPage; //The lobs contained in each page

	private String officeName = null; // nome dell'ufficio selezionato
	
	private String ceName = null;// nome del ce selezionato
	
	private String gcName = null; // nome del gruppo commerciale selezionato
	
	@Inject
	public HistogramsActivity(IHistogramsViewDisplay display, 
			DataServiceAsync dataService,
			EventBus eventBus) {
		this.dataService = dataService;
		this.display = display;
		this.eventBus = eventBus;

	}

	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		//TODO controllo della validità della sessione, else go to log in place
		eventBus.addHandler(LocationSelectedEvent.TYPE, this);
		eventBus.addHandler(GaugesViewSelectedEvent.TYPE,	 this);
		eventBus.addHandler(HistogramsViewSelectedEvent.TYPE, this);
		eventBus.addHandler(PillsViewSelectedEvent.TYPE, this);

		display.setListener(this);
		panel.setWidget(display.asWidget());

		display.initView(); // pulisce la view da eventuali dati precedenti
		
		display.asWidget().setVisible(false); // per default alla partenza mostro i manometri

	}

	/**
	 * @param page the page index, starting from 0
	 * @return The list of product  for the page in argument. The array's length is 8. Null values elements means blank space.
	 */
	private ArrayList<ProductInfoDTO> getProductIdListForPage(int page) {
		ArrayList<LineOfBusiness> lobsInPage = lobsByPage.get(displayingPage);
		ArrayList<ProductInfoDTO> resultList = new ArrayList<ProductInfoDTO>();
		for (LineOfBusiness lineOfBusiness : lobsInPage) {
			for (ProductInfoDTO product : lineOfBusiness.getProducts()) {
				resultList.add(product);

			}
			resultList.add(null);// blank space

		}
		return resultList;

	}

	// Gestione dei place. Metodi invocati dagli Activity Mapper che sono interessati a far partire questa activity

	public HistogramsActivity withPlace(final LoginManagerPlace place) {
		this.currentlyDisplayedId = place.getDefaultLocationId();
		//		this.displayingId=place.getDefaultLocationId(); // quando entra un manager gli si mostrano per default i CE del suo uffiico
		officeName = place.getOfficeName();
		dataService.getLobByPageSplit(new AsyncCallback<ArrayList<ArrayList<LineOfBusiness>>>() { // recupero le linee di business divise per pagina

			@Override
			public void onSuccess(ArrayList<ArrayList<LineOfBusiness>> result) {
				lobsByPage = result;
				asyncUpdateDisplay(Integer.valueOf(currentlyDisplayedId), false);

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});


		return this;

	}



	//  Gestione dell'evento di location selected: quando un utente seleziona un ufficio o un ce nel cell browser

	@Override
	public void onNewLocationSelected(LocationSelectedEvent event) {
		this.currentlyDisplayedId = event.getSelectionId();
		ceName = event.getSelectedDescriptionCE();
		gcName = event.getSelectedDescriptionGC();
		officeName = event.getSelectedDescriptionUfficio();
		asyncUpdateDisplay(Integer.valueOf(this.currentlyDisplayedId), false);

	}


	/**
	 * Aggiorna la pagina solo se è visualizzata o se stava visualizzando una location diversa
	 * @param locationId the location currently being requested
	 * @param force se true l'updare dell'interfaccia deve avvenire anche se la locatio in non è cambiata dall'ultimo aggiornamento: vuol dire che stiamo sfogliando le pagine
	 */
	private void asyncUpdateDisplay(final int locationId, boolean force) {


		if(display.asWidget().isVisible() ) { 
			if((Integer.valueOf(lastDisplayedId) != locationId) || force) {
			lastDisplayedId = currentlyDisplayedId;
			display.showLoader(true, "attendere prego...");
			Timer t = new Timer() {

				@Override
				public void run() {
					final ArrayList<ProductInfoDTO> productsArray = getProductIdListForPage(displayingPage); // array ordinato con gli id dei prodotti da mostrare in pagina 
					dataService.getAvailableInformationByParent(productsArray, locationId, new AsyncCallback<LightInfoOwnerDto[]>() {

						@Override
						public void onSuccess(LightInfoOwnerDto[] result) {
							display.setStatusBar(officeName, ceName, gcName);
							display.setLobHeadline(lobsByPage.get(displayingPage));
							display.setPageIndexes(lobsByPage.size(), displayingPage);
							display.appendPageData(result);
							display.showLoader(false, "");

							displayingData = result;

							visualizzaTotaliColonna();
							stopAggiornaAnimation(); // invia l'evento che è poi ricevuto dall'header bar activity per fermare l'icona di aggiorna che ruota

						}

						@Override
						public void onFailure(Throwable caught) {
							display.showLoader(false, "");
							stopAggiornaAnimation(); // invia l'evento che è poi ricevuto dall'header bar activity per fermare l'icona di aggiorna che ruota
						}

					});

				}
			};

			t.schedule(1100);

		} else {
			stopAggiornaAnimation(); // invia l'evento che è poi ricevuto dall'header bar activity per fermare l'icona di aggiorna che ruota  

		}
		}

	}

	private void stopAggiornaAnimation() {
		eventBus.fireEvent(new StopAnimationEvent());
		
	}

	@Override
	public void showHistograms(
			HistogramsViewSelectedEvent histogramsViewSelectedEvent) {
		display.asWidget().setVisible(true);
		asyncUpdateDisplay(Integer.valueOf(currentlyDisplayedId), false);
		visualizzaTotaliColonna();
		// sincronizza la pagina dei totali di colonna
		ChangePageEvent e = new ChangePageEvent(displayingPage);
		eventBus.fireEvent(e);

	}

	private void visualizzaTotaliColonna() {
		AggiornaTotColonnaIstoEvent ev = new AggiornaTotColonnaIstoEvent();
		ev.setInfoOwnerDtoArray(displayingData);
		eventBus.fireEvent(ev);

		// sincronizza la pagina dei totali di colonna
		ChangePageEvent e = new ChangePageEvent(displayingPage);
		eventBus.fireEvent(e);

	}

	@Override
	public void showGauges(GaugesViewSelectedEvent gaugesViewSelectedEvent) {
		display.asWidget().setVisible(false);
		asyncUpdateDisplay(Integer.valueOf(currentlyDisplayedId), false);
	}

	@Override
	public void showPills(PillsViewSelectedEvent e) {
		display.asWidget().setVisible(false);

	}

	@Override
	public void getNextPageData() {
		displayingPage++;
		asyncUpdateDisplay(Integer.valueOf(this.currentlyDisplayedId), true);
		ChangePageEvent e = new ChangePageEvent(displayingPage);
		eventBus.fireEvent(e);

	}

	@Override
	public void getPreviousPageData() {
		displayingPage--;
		asyncUpdateDisplay(Integer.valueOf(this.currentlyDisplayedId), true);
		ChangePageEvent e = new ChangePageEvent(displayingPage);
		eventBus.fireEvent(e);

	}


}