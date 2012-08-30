package com.kp.marsh.ebt.client.webapp.activities;

import java.util.ArrayList;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.kp.marsh.ebt.client.webapp.AuthenticationServiceAsync;
import com.kp.marsh.ebt.client.webapp.DataServiceAsync;
import com.kp.marsh.ebt.client.webapp.events.AggiornaTotColonnaIstoEvent;
import com.kp.marsh.ebt.client.webapp.events.AggiornaTotColonnaIstoHandler;
import com.kp.marsh.ebt.client.webapp.events.AggiornaTotColonnaPillEvent;
import com.kp.marsh.ebt.client.webapp.events.AggiornaTotColonnaPillHandler;
import com.kp.marsh.ebt.client.webapp.events.ChangePageEvent;
import com.kp.marsh.ebt.client.webapp.events.ChangePageHandler;
import com.kp.marsh.ebt.client.webapp.events.GaugesViewSelectedEvent;
import com.kp.marsh.ebt.client.webapp.events.HistogramsViewSelectedEvent;
import com.kp.marsh.ebt.client.webapp.events.LocationSelectedEvent;
import com.kp.marsh.ebt.client.webapp.events.PillsViewSelectedEvent;
import com.kp.marsh.ebt.client.webapp.events.StopAnimationEvent;
import com.kp.marsh.ebt.client.webapp.events.StopAnimationEventHandler;
import com.kp.marsh.ebt.client.webapp.place.AuthenticationPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginCEPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginManagerPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginManagerWithClientsPlace;
import com.kp.marsh.ebt.client.webapp.ui.activityintf.IHeaderBarDisplay;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;
import com.kp.marsh.ebt.shared.dto.AchievedInfoDTO;
import com.kp.marsh.ebt.shared.dto.BusinessInfoDTO;
import com.kp.marsh.ebt.shared.dto.LightInfoOwnerDto;
import com.kp.marsh.ebt.shared.dto.LightProductInfoDto;
import com.kp.marsh.ebt.shared.dto.LineOfBusiness;
import com.kp.marsh.ebt.shared.dto.NavigationDTO;
import com.kp.marsh.ebt.shared.dto.OwnerType;
import com.kp.marsh.ebt.shared.dto.PotentialInfoDTO;
import com.kp.marsh.ebt.shared.dto.ProductInfoDTO;
import com.kp.marsh.ebt.shared.dto.ProductTotalsManager;
import com.kp.marsh.ebt.shared.dto.TotalsManager;

public class HeaderBarActivity extends AbstractActivity  implements IHeaderBarDisplay.Listener, ChangePageHandler, AggiornaTotColonnaPillHandler, AggiornaTotColonnaIstoHandler, StopAnimationEventHandler {

	private final IHeaderBarDisplay display;

	private String managerDisplayName = null;

	private String userOfficeId; // the id of the location being visualized by default with the current user: its office. Serve per selezionare la combo all'inizio

	private EventBus eventBus;

	private PlaceController placeController;

	private final AuthenticationServiceAsync authService;

	private final DataServiceAsync dataService;

	private ArrayList<ArrayList<LineOfBusiness>> lobsByPageList;

	@Inject
	public HeaderBarActivity(EventBus eventBus,
			PlaceController placeController,
			IHeaderBarDisplay display,
			AuthenticationServiceAsync authService,
			DataServiceAsync dataService) {

		this.eventBus=eventBus;
		this.display = display;
		this.placeController=placeController;
		this.authService=authService;
		this.dataService=dataService;

		eventBus.addHandler(ChangePageEvent.TYPE, this);
		eventBus.addHandler(AggiornaTotColonnaPillEvent.TYPE, this);
		eventBus.addHandler(AggiornaTotColonnaIstoEvent.TYPE, this);
		eventBus.addHandler(StopAnimationEvent.TYPE, this);

		display.setListener(this);

	}

	public void start(final AcceptsOneWidget panel, EventBus eventBus) {


		// se entro e non sono loggato o non ho i diritti mandami alla login
		authService.isSessionAuthenticated(new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {

				if(!Boolean.TRUE.equals(result)) {// session unauthenticated
					placeController.goTo(new AuthenticationPlace());

				} else { // session authenticated

					display.setUserName(managerDisplayName);
					dataService.getLobByPageSplit(new AsyncCallback<ArrayList<ArrayList<LineOfBusiness>>>() {

						@Override
						public void onSuccess(ArrayList<ArrayList<LineOfBusiness>> result) {
							lobsByPageList = result;
							display.configureSubTotals(result); // inizializzo le tabelle con i totali per colonna, o uso le precedenti se sono giˆ disponibili, reimpostando per˜ la tabella di prima pagina
							
						}

						@Override
						public void onFailure(Throwable caught) {
							placeController.goTo(new AuthenticationPlace());
							
						}
					});

					panel.setWidget(display.asWidget());
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				placeController.goTo(new AuthenticationPlace());
				
			}
		});

	}


	/**
	 * Invocato da insert potential activity o Histogram activity per dire quale pagina dei totali di colonna visualizzare
	 * @param e
	 */
	public void onChangePage(ChangePageEvent e) {
		display.goToPage(e.getPage());

	}


	// Gestione dei place. Metodi invocati dagli Activity Mapper che sono interessati a far partire questa activity

	public HeaderBarActivity withPlace(final LoginManagerPlace place) {
		this.managerDisplayName=place.getDisplayName();
		this.userOfficeId = place.getDefaultLocationId();
		display.initManagerIcons();
		display.showLoader(true);
		return this;
		
	}

	public HeaderBarActivity withPlace(final LoginManagerWithClientsPlace place) {
		this.managerDisplayName=place.getDisplayName();
		this.userOfficeId = place.getDefaultLocationId();
		display.initManagerWithClientsIcons();
		display.showLoader(true);
		return this;
		
	}

	public Activity withPlace(LoginCEPlace place) {
		this.managerDisplayName=place.getDisplayName();
		display.initCEIcons();
		display.showLoader(true);
		return this;
		
	}

	private native void reload() /*-{ 
     		$wnd.location.reload(); 
 	}-*/; 

	@Override
	public void logout() {

		authService.logoutSession(new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
//				placeController.goTo(new LogoutPlace());
				reload();
				
			}

			@Override
			public void onFailure(Throwable caught) {
//				placeController.goTo(new LogoutPlace()); // vado lo stesso alla login
				logout();
			}
		});

	}

	@Override
	public void inizializzaComboUffici() {
		dataService.getUffici(new AsyncCallback<ArrayList<NavigationDTO>>() {
			@Override
			public void onFailure(Throwable caught) {

			}
			@Override
			public void onSuccess(ArrayList<NavigationDTO> result) {
				display.updateUff(result);
				display.initUserOffice(getLoggedUserOfficeId());
			}
		});

	}
	
	@Override
	public void locationIsSelected(String locationId,
			OwnerType informationOwnersValueType,
			String selectedDescriptionUfficio, String selectedDescriptionCE,
			String selectedDescriptionGC) {
		LocationSelectedEvent event = new LocationSelectedEvent(locationId, informationOwnersValueType, selectedDescriptionUfficio, selectedDescriptionCE, selectedDescriptionGC);
		eventBus.fireEvent(event);
	}

	@Override
	public void switchToGauges() {
		GaugesViewSelectedEvent event = new GaugesViewSelectedEvent();
		eventBus.fireEvent(event);

	}

	@Override
	public void switchToHistograms() {
		HistogramsViewSelectedEvent event = new HistogramsViewSelectedEvent();
		eventBus.fireEvent(event);

	}

	@Override
	public void switchToPills() {
		PillsViewSelectedEvent e = new PillsViewSelectedEvent();
		eventBus.fireEvent(e);

	}




	@Override
	public void onAggiornaTotaliColonnaEvent(AggiornaTotColonnaPillEvent e) {


		ProductTotalsManager updatedProductTotalManager = e.getTotalsManager();

		int newValMinusOldVal = e.getChangeVal(); 

		if(null == e.getCommittedProduct()) { // trick per identificare la fase di inizializzazione di tutti i prodotti 

			/*do nothing*/

		} else { // aggiornamento dei totali di una sola colonna
			//AGGIORNO TOTALI DI COLONNA

			ProductInfoDTO committedProduct = e.getCommittedProduct();

			if(null == updatedProductTotalManager) { // pu˜ esserlo nel caso di una reset info
				updatedProductTotalManager = new ProductTotalsManager();
			}

			addLobIdToProduct(committedProduct);

			updatedProductTotalManager.updateValue(BusinessInfoValueType.POTENTIAL, committedProduct.getId(), committedProduct.getParentId(), newValMinusOldVal);

		}

		display.refreshTotals(updatedProductTotalManager);
		aggiornaTotPotentialAchieved(updatedProductTotalManager);

	}

	/**
	 * Aggiorna il totale per colonne accanto ai radio button.
	 * Il totale viene calcolato tramite il ProductTotalsManager
	 * @param lobsByPageList
	 * @param updatedProductTotalManager
	 */
	private void aggiornaTotPotentialAchieved(ProductTotalsManager updatedProductTotalManager) {
		int totCe = 0;
		int totAchCe = 0;
		if(null != updatedProductTotalManager) {
			for (ArrayList<LineOfBusiness> page : lobsByPageList) {
				for (LineOfBusiness lineOfBusiness : page) {
					TotalsManager lobTotMan = updatedProductTotalManager.getTotalsManagerByLobId(""+lineOfBusiness.getId());
					if(null != lobTotMan){
						totCe += lobTotMan.getTotalPotential();
						totAchCe += lobTotMan.getTotalAchieved();
					}
				}
			}
			display.setPotentialTotal(totCe);
			display.setAchievedTotal(totAchCe);
		}
	}

	@Override
	public void onAggiornaTotaliColonnaIstoEvent(AggiornaTotColonnaIstoEvent e) {

		LightInfoOwnerDto[] rowArray = e.getInfoOwnerDtoArray();
		if(null != rowArray) {
			int totCe = 0;
			int totAchCe = 0;
			// Creo un ProductTotalsManager per poter riusare il metodo refreshTotals del display
			ProductTotalsManager prodTotMan = new ProductTotalsManager();
			for (int i = 0; i < rowArray.length; i++) {
				LightInfoOwnerDto lightInfoOwnerDto = rowArray[i];
				totAchCe += lightInfoOwnerDto.getTotaleAchievedEuro();
				totCe += lightInfoOwnerDto.getTotalePotenzialeEuro();

				LightProductInfoDto[] productArray = lightInfoOwnerDto.getOrderedProductArray();
				for (int j = 0; j < productArray.length; j++) {
					LightProductInfoDto lightProductInfoDto = productArray[j];
					if(null != lightProductInfoDto) {
						BusinessInfoDTO potential = new PotentialInfoDTO(0/*dummy*/, lightProductInfoDto.getProductId(), null, ""+lightProductInfoDto.getEuroPotValue(), null);
						addLobIdToBusinessInfo(potential);
						prodTotMan.add(potential);
						BusinessInfoDTO achieved = new AchievedInfoDTO(0/*dummy*/, lightProductInfoDto.getProductId(), -0/*dummy*/, ""+lightProductInfoDto.getEuroAchValue(), null);
						addLobIdToBusinessInfo(achieved);
						prodTotMan.add(achieved);
					}
				}
			}

			display.refreshTotals(prodTotMan);

			// aggiorno i totali accanto al radio button
			display.setPotentialTotal(totCe);
			display.setAchievedTotal(totAchCe);
		}
		
	}


	/**
	 * Metodo fatto apposta per poter usare refreshTotals dal display
	 * @param businessInfo
	 */
	private void addLobIdToBusinessInfo(BusinessInfoDTO businessInfo) {
		if(null == businessInfo.getLobId()) {
			//	RICAVA L'ID DELLA LOB DALLA LISTA DELLE PAGINE
			for (ArrayList<LineOfBusiness> page : lobsByPageList) {
				for (LineOfBusiness lineOfBusiness : page) {
					if(lineOfBusiness.hasInTheList(businessInfo.getProductId())) {
						businessInfo.setLobId(""+lineOfBusiness.getId());
						break;
					}
					if(businessInfo.getLobId() != null) break;
				}
			}

		}
	}
	
	private void addLobIdToProduct(ProductInfoDTO product) {
		if(null == product.getParentId()) {// Pu˜ esserlo nel caso di un prodotto senza business info in fase di inizializzazione
			//	RICAVA L'ID DELLA LOB DALLA LISTA DELLE PAGINE
			for (ArrayList<LineOfBusiness> page : lobsByPageList) {
				for (LineOfBusiness lineOfBusiness : page) {
					if(lineOfBusiness.hasInTheList(product.getId())) {
						product.setParentId(""+lineOfBusiness.getId());
						break;
					}
					if(product.getParentId() != null) break;
				}
			}

		}
	}

	@Override
	public void ufficioSelected(int id) {
		dataService.getCE(id, new AsyncCallback<ArrayList<NavigationDTO>>() {
			@Override
			public void onFailure(Throwable caught) {

			}
			@Override
			public void onSuccess(ArrayList<NavigationDTO> result) {
				display.updateCE(result);
				if(result.size() == 1) {
					ceSelected(Integer.valueOf(result.get(0).getId()));
				}
			}
		});
	}
	
	@Override
	public void ceSelected(int id) {
		dataService.getGC(id, new AsyncCallback<ArrayList<NavigationDTO>>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onSuccess(ArrayList<NavigationDTO> result) {
				display.updateGC(result);
			}
		});
	}

	private String getLoggedUserOfficeId() {
		return userOfficeId;
	}

	@Override
	public void onStopAnimationEvent(StopAnimationEvent e) {
		display.stopAnimation();
		
	}

}