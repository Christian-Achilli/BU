package com.kp.marsh.ebt.client.webapp.activities;

import java.util.ArrayList;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.kp.marsh.ebt.client.webapp.AuthenticationServiceAsync;
import com.kp.marsh.ebt.client.webapp.DataServiceAsync;
import com.kp.marsh.ebt.client.webapp.events.AggiornaTotColonnaPillEvent;
import com.kp.marsh.ebt.client.webapp.events.ChangePageEvent;
import com.kp.marsh.ebt.client.webapp.events.GaugesViewSelectedEvent;
import com.kp.marsh.ebt.client.webapp.events.GaugesViewSelectedHandler;
import com.kp.marsh.ebt.client.webapp.events.HistogramsViewSelectedEvent;
import com.kp.marsh.ebt.client.webapp.events.HistogramsViewSelectedHandler;
import com.kp.marsh.ebt.client.webapp.events.PillsViewSelectedEvent;
import com.kp.marsh.ebt.client.webapp.events.PillsViewSelectedHandler;
import com.kp.marsh.ebt.client.webapp.place.AuthenticationPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginCEPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginManagerWithClientsPlace;
import com.kp.marsh.ebt.client.webapp.ui.activityintf.IInsertPotentialViewDisplay;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;
import com.kp.marsh.ebt.shared.dto.BusinessInfoDTO;
import com.kp.marsh.ebt.shared.dto.LineOfBusiness;
import com.kp.marsh.ebt.shared.dto.LoggedInUser;
import com.kp.marsh.ebt.shared.dto.MarshClientDTO;
import com.kp.marsh.ebt.shared.dto.ProductInfoDTO;
import com.kp.marsh.ebt.shared.dto.ProductTotalsManager;
import com.kp.marsh.ebt.shared.dto.RowTotalsManager;

public class InsertPotentialActivity extends AbstractActivity implements IInsertPotentialViewDisplay.Listener, GaugesViewSelectedHandler, HistogramsViewSelectedHandler, PillsViewSelectedHandler {

	// injected through the init method
	private final DataServiceAsync dataService;
	private final AuthenticationServiceAsync authService;
	private final IInsertPotentialViewDisplay display;

	private PlaceController placeController;

	private LoggedInUser loggedInUser;

	private final EventBus eventBus;

	private ProductTotalsManager totalsManager;
	
	private int displayingPage = 0;


	/**
	 * Lists the LOBs contained in each table listed in <code>headlineTableList</code>
	 */
	private ArrayList<ArrayList<LineOfBusiness>> lobsByPageList; // LOB list split per display page 


	/**
	 * @return an ordered list whose index + 1 is the page index on the app. <br>
	 * Any object of such array list contains the list of line of business shown on the page related to the associated containing list index
	 */
	//	ArrayList<ArrayList<LineOfBusiness>> getLobsShownByPageList();

	@Inject
	public InsertPotentialActivity(
			IInsertPotentialViewDisplay display,
			PlaceController placeController,
			DataServiceAsync dataService,
			AuthenticationServiceAsync authService,
			EventBus eventBus
	) {
		this.dataService = dataService;
		this.authService = authService;
		this.display = display;
		this.placeController = placeController;
		this.eventBus = eventBus;

	}

	private void init() {

		// se entro e non sono loggato mandami alla login
		authService.isSessionAuthenticated(new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {

				if(!Boolean.TRUE.equals(result)) {// session unauthenticated

					placeController.goTo(new AuthenticationPlace());

				} else { // session authenticated
					authService.getLoggedInUser(new AsyncCallback<LoggedInUser>() {

						@Override
						public void onSuccess(LoggedInUser result) {

							loggedInUser = result;


							if(loggedInUser!= null) {
								//								display.setLoggedUserName(loggedInUser.getDescription());

								GWT.log("init(Logged in user is + "+loggedInUser.getDescription()+")");



								dataService.getLobByPageSplit(new AsyncCallback<ArrayList<ArrayList<LineOfBusiness>>>() {

									@Override
									public void onSuccess(ArrayList<ArrayList<LineOfBusiness>> result) {

										lobsByPageList = result;

										// TODO non dovrebbe più essere necessaria se riesco a riusare le headline generate per la view degli istogrammi
										display.configureLobHeadlines(lobsByPageList);

										//TODO questa chiamata non dovrebbe servire perchè le tabelle coi totali di colonna dovrebbero già essere state disegnate in fase di inizializzazione dell'applicazione
										//										display.configureSubTotals(lobsByPageList);

										dataService.getAvailableBusinessInformationByCE(loggedInUser.getId(), new AsyncCallback<ArrayList<MarshClientDTO>>() {

											@Override
											public void onSuccess(ArrayList<MarshClientDTO> result) {
												display.configureClientTables(result, lobsByPageList); // metodo molto lento, ottimizzarlo!!!!
												//											if(result.get(0).getProductInfoList().isEmpty()) return; //HACK, non dovrebbe succedere
												//il get(0) non va bene perchè non è detto che la lista di business info per i prodotti del primo cliente non sia vuota. POtrebbero anche essere tutte vuote. Quindi bisogna ciclare

												//												ProductTotalsManager productTotalsManager = null;// the product totals manager with totals map inside is static, so I can use get(0)

												for (MarshClientDTO marshClientDTO : result) {
													if(!marshClientDTO.getProductInfoList().isEmpty()) {
														totalsManager = marshClientDTO.getProductInfoList().get(0).getTotalsManager();
														break;
														
													}
												}

												if(null != totalsManager) {
													inizializzaTotaliColonna();
													
												}

												if(!result.isEmpty()) {
													RowTotalsManager rowTotalsManager = result.get(0).getClientTotalsManager();
													display.initRowTotals(rowTotalsManager);
													
												} 

												display.showLoader(false);

											}

											@Override
											public void onFailure(Throwable caught) {
												placeController.goTo(new AuthenticationPlace());

											}
										});




									}

									@Override
									public void onFailure(Throwable caught) {
										placeController.goTo(new AuthenticationPlace());

									}
								});

							}
						}

						@Override
						public void onFailure(Throwable caught) {
							placeController.goTo(new AuthenticationPlace());

						}

					});

				}

			}

			public void onFailure(Throwable caught) {
				placeController.goTo(new AuthenticationPlace());
				
			}
		});

	}


	public void start(AcceptsOneWidget container, EventBus eventBus) {

		eventBus.addHandler(PillsViewSelectedEvent.TYPE, this);
		eventBus.addHandler(GaugesViewSelectedEvent.TYPE,	 this);
		eventBus.addHandler(HistogramsViewSelectedEvent.TYPE, this);

		display.showLoader(true);
		display.setListener(this);
		display.initView();

		// called once all the data are properly set
		container.setWidget(display.asWidget());

	}

	@Override
	public void onInsertPopUpClose() {


		final BusinessInfoDTO commitMePlease = display.getClickedPillDTO().getWhatInfoAmI(); 

		if(null != loggedInUser) { // WARN: onClose is called twice, first time loggedIn is null. Should be called once and loggedIn never be null

			//					if(!commitMePlease.equals(display.getClickedPill().getWhatInfoWasI()) non è un controllo stabile, perchè anche il salvataggio delle note passa di qua
			//							|| !display.getClickedPill().getWhatInfoWasI().getPostItText().equals(commitMePlease.getPostItText())) // se non è cambiato nulla non vado sul db. as meno che la  

			dataService.commitBusinessInformation(commitMePlease, new AsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) { 
					updateTotalsAfterCommit(display.getClickedPillDTO(), lobsByPageList);
					display.getClickedPillDTO().setPreviousBusinessInfo(commitMePlease);// se il commit ha successo, inizializzo il nuovo stato precedente del ProductInfoDTO
					display.initPill();
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("error on commit, resetting: ");
					caught.printStackTrace();
					// far ritornare la pillola come era, perchè il commit è andato male
					display.rollback();
					display.initPill();
					
				}
			});

		}

	}


	@Override
	public void disableClient(int clientId) {
		dataService.disableClient(clientId, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				// fare il rollback dell'interfaccia
			}
		});

	}


	private void updateTotalsAfterCommit(ProductInfoDTO committedProduct, ArrayList<ArrayList<LineOfBusiness>> lobsByPageList) {
		// qui devo elaborare l'informazione appena committata per aggiornare i totali in pagina

		BusinessInfoDTO buInfoDTO = committedProduct.getWhatInfoAmI();

		BusinessInfoDTO buInfoDTOOld = committedProduct.getWhatInfoWasI();

		if(buInfoDTOOld == null || !buInfoDTO.getValue().equals(buInfoDTOOld.getValue())) {
			int newValue = decodeValue(buInfoDTO);

			int oldValue = decodeValue(buInfoDTOOld);

			GWT.log("newValue-OldValue="+(newValue-oldValue));

			// AGGIORNO TOTALI DI RIGA
			// recupera il TotalManager del Cliente in aggiornamento
			RowTotalsManager updatedClientTotManager = (RowTotalsManager)committedProduct.getmClient().getClientTotalsManager();

			updatedClientTotManager.updateValue(BusinessInfoValueType.POTENTIAL, buInfoDTO.getClientId(), newValue-oldValue);

			display.refreshRowTotals(updatedClientTotManager);

			//AGGIORNO TOTALI DI COLONNA
			//per aggiornare i tot di colonna devo lanciare un evento che viene intercettato dall'header display che fa il refresh dei totali: spostare sull'activity
			aggTotColonna(newValue-oldValue);
		}

	}


	/**
	 * @param busInfo
	 * @return il valore del potenziale in euro
	 */
	private int decodeValue(BusinessInfoDTO busInfo) {
		int value = 0;
		if(null != busInfo) {
			switch (busInfo.getValueType()) {
			case POTENTIAL:
			case BROKER_POT:
			case COMPANY_POT:
				if(null != busInfo.getValue() &&  RegExp.compile("[1-9]\\d{0,}").test(busInfo.getValue())) {
					value = Integer.valueOf(busInfo.getValue());
				}
				break;
			default:
				break;
			}
		}
		return value;
	}



	public Activity withPlace(LoginCEPlace place) {
		display.asWidget().setVisible(true); 
		init();
		return this;
	}

	public Activity withPlace(LoginManagerWithClientsPlace place) {
		display.asWidget().setVisible(false); // per default alla partenza mostro i manometri
//		init(); lo faccio solo se viene selezionata l'icona delle pillole
		return this;
	}

	private void aggTotColonna(int newValMinusOldVal) {
		AggiornaTotColonnaPillEvent aggEvent = new AggiornaTotColonnaPillEvent();
		aggEvent.setTotalsManager(totalsManager);
		aggEvent.setCommittedProduct(display.getClickedPillDTO());
		aggEvent.setChangeVal(newValMinusOldVal);
		eventBus.fireEvent(aggEvent);

	}

	@Override
	public ProductTotalsManager getTotalsManager() {
		return totalsManager;
	}


	@Override
	public void showHistograms(
			HistogramsViewSelectedEvent histogramsViewSelectedEvent) {
		display.asWidget().setVisible(false);

	}

	@Override
	public void showGauges(GaugesViewSelectedEvent gaugesViewSelectedEvent) {
		display.asWidget().setVisible(false);

	}

	@Override
	public void showPills(PillsViewSelectedEvent e) {
		display.asWidget().setVisible(true);
		if(null == loggedInUser) { // vuol dire che l'init non è mai stato chiamato
			init();
		}
		// sincronizza la pagina dei totali di colonna
		ChangePageEvent ee = new ChangePageEvent(displayingPage);
		eventBus.fireEvent(ee);
		inizializzaTotaliColonna();
	}

	@Override
	public void goToPage(int page) {
		displayingPage = page;
		ChangePageEvent e = new ChangePageEvent(page);
		eventBus.fireEvent(e);

	}

	@Override
	public void inizializzaTotaliColonna() {
		AggiornaTotColonnaPillEvent aggEvent = new AggiornaTotColonnaPillEvent();
		aggEvent.setTotalsManager(totalsManager);
		aggEvent.setCommittedProduct(null);
		aggEvent.setChangeVal(-200); // dummy val
		eventBus.fireEvent(aggEvent);
		
		// sincronizza la pagina dei totali di colonna
		ChangePageEvent e = new ChangePageEvent(displayingPage);
		eventBus.fireEvent(e);

	}

}