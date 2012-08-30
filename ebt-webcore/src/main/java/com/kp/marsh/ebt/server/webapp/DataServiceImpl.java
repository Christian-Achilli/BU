package com.kp.marsh.ebt.server.webapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kp.marsh.ebt.client.webapp.DataService;
import com.kp.marsh.ebt.server.webapp.data.domain.BusinessInformation;
import com.kp.marsh.ebt.server.webapp.data.domain.InformationOwners;
import com.kp.marsh.ebt.server.webapp.data.domain.Products;
import com.kp.marsh.ebt.server.webapp.data.domain.ReferenceYears;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.CalculationService;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.DomainDrillerService;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;
import com.kp.marsh.ebt.shared.ClientIdentifier;
import com.kp.marsh.ebt.shared.ProductIdentifier;
import com.kp.marsh.ebt.shared.dto.AchievedInfoDTO;
import com.kp.marsh.ebt.shared.dto.BrokerPotBalanceInfoDTO;
import com.kp.marsh.ebt.shared.dto.BusinessInfoDTO;
import com.kp.marsh.ebt.shared.dto.CompanyPotBalanceInfoDTO;
import com.kp.marsh.ebt.shared.dto.DummyPotentialInfoDTO;
import com.kp.marsh.ebt.shared.dto.FinalBalanceInfoDTO;
import com.kp.marsh.ebt.shared.dto.LightInfoOwnerDto;
import com.kp.marsh.ebt.shared.dto.LightProductInfoDto;
import com.kp.marsh.ebt.shared.dto.LineOfBusiness;
import com.kp.marsh.ebt.shared.dto.MarshClientDTO;
import com.kp.marsh.ebt.shared.dto.NavigationDTO;
import com.kp.marsh.ebt.shared.dto.OwnerType;
import com.kp.marsh.ebt.shared.dto.PotentialInfoDTO;
import com.kp.marsh.ebt.shared.dto.ProductInfoDTO;
import com.kp.marsh.ebt.shared.dto.ProductTotalsManager;
import com.kp.marsh.ebt.shared.dto.ResetInfoDTO;
import com.kp.marsh.ebt.shared.dto.RowTotalsManager;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
@Singleton
public class DataServiceImpl extends GuiceRemoteServiceServlet implements
		DataService {

	private static Logger log = Logger.getLogger(DataServiceImpl.class);

	private DomainDrillerService domainDrillerService;

	private CalculationService calculationService;

	private ReferenceYears[] yearArray;

	private PageConfigurator pageConfigurator;

	@Inject
	public DataServiceImpl(DomainDrillerService domainDrillerService,
			CalculationService calculationService, ReferenceYears[] yearArray,
			PageConfigurator pageConfigurator) {
		this.domainDrillerService = domainDrillerService;
		this.calculationService = calculationService;
		this.pageConfigurator = pageConfigurator;
		this.yearArray = yearArray;
	}

	@Override
	public ProductTotalsManager getUpdatedProductTotalsManger(
			int clientExecutiveId, int producId) {
		List<BusinessInformation> bInfoList = domainDrillerService
				.getAvailablePotentialBusinessInformationByProduct(
						clientExecutiveId, producId, yearArray[0].getId());
		// while updating just one product is retrieved. To get the product's
		// lob total we need one more service, used below
		ProductTotalsManager ptm = new ProductTotalsManager();
		if (bInfoList.isEmpty()) {
			BusinessInfoDTO buInfoDTO = new DummyPotentialInfoDTO(
					new ProductIdentifier(producId));
			ptm.add(buInfoDTO);
		} else {
			for (BusinessInformation businessInfo : bInfoList) { // discriminare
																	// i vari
																	// potenziali
				BusinessInfoDTO buInfoDTO = null;
				/**
				 * the parent id has to be null otherwise the potential for the
				 * product <code>productId</code> is accounted twice.
				 */
				buInfoDTO = new PotentialInfoDTO(businessInfo
						.getInformationOwners().getId(), producId,
						businessInfo.getId(), businessInfo.getValueAmount(),
						null);
				ptm.add(buInfoDTO);
			}
		}

		Products p = domainDrillerService.findProductById(producId);
		int lobTotalCounter = domainDrillerService.getLOBTotalByLobId(
				clientExecutiveId, p.getParentId(), yearArray[0],
				BusinessInfoValueType.POTENTIAL);

		lobTotalCounter += domainDrillerService.getLOBTotalByLobId(
				clientExecutiveId, p.getParentId(), yearArray[0],
				BusinessInfoValueType.BROKER_POT);

		lobTotalCounter += domainDrillerService.getLOBTotalByLobId(
				clientExecutiveId, p.getParentId(), yearArray[0],
				BusinessInfoValueType.COMPANY_POT);

		ptm.add(new PotentialInfoDTO(-1, -1, null, "" + lobTotalCounter, p
				.getParentId()));

		return ptm;
	}

	@Override
	public RowTotalsManager getUpdatedRowTotalsManager(int clientId) {

		List<BusinessInformation> businessInfoList = domainDrillerService
				.getAvailableBusinessInformationByType(clientId,
						yearArray[0].getId(), BusinessInfoValueType.POTENTIAL); // get
																				// the
																				// clients
																				// of
																				// the
																				// CE
		businessInfoList
				.addAll(domainDrillerService
						.getAvailableBusinessInformationByType(clientId,
								yearArray[0].getId(),
								BusinessInfoValueType.BROKER_POT));
		businessInfoList
				.addAll(domainDrillerService
						.getAvailableBusinessInformationByType(clientId,
								yearArray[0].getId(),
								BusinessInfoValueType.COMPANY_POT));
		businessInfoList.addAll(domainDrillerService
				.getAvailableBusinessInformationByType(clientId,
						yearArray[0].getId(), BusinessInfoValueType.ACHIEVED));
		businessInfoList.addAll(domainDrillerService
				.getAvailableBusinessInformationByType(clientId,
						yearArray[1].getId(),
						BusinessInfoValueType.FINAL_BALANCE));
		RowTotalsManager updatedClientTotManager = new RowTotalsManager();
		if (businessInfoList.isEmpty()) {

			BusinessInfoDTO buInfoDTO = new DummyPotentialInfoDTO(
					new ClientIdentifier(clientId));
			updatedClientTotManager.add(buInfoDTO);

		} else {
			for (BusinessInformation businessInformation : businessInfoList) {

				BusinessInfoDTO buInfoDTO = null;

				String lobId = businessInformation.getProducts().getParentId();// lob
																				// id

				BusinessInfoValueType thisInfoType = BusinessInfoValueType
						.fromString(businessInformation.getValueType());

				switch (thisInfoType) {
				case POTENTIAL:
					buInfoDTO = new PotentialInfoDTO(clientId,
							businessInformation.getProducts().getId(),
							businessInformation.getId(),
							businessInformation.getValueAmount(), lobId);

					break;

				case BROKER_POT:
					buInfoDTO = new BrokerPotBalanceInfoDTO(clientId,
							businessInformation.getProducts().getId(),
							businessInformation.getId(),
							businessInformation.getValueAmount(), lobId);
					break;

				case COMPANY_POT:
					buInfoDTO = new CompanyPotBalanceInfoDTO(clientId,
							businessInformation.getProducts().getId(),
							businessInformation.getId(),
							businessInformation.getValueAmount(), lobId);
					break;

				case FINAL_BALANCE:
					buInfoDTO = new FinalBalanceInfoDTO(clientId,
							businessInformation.getProducts().getId(),
							businessInformation.getId(),
							businessInformation.getValueAmount(), lobId);
					break;

				case ACHIEVED:
					buInfoDTO = new AchievedInfoDTO(clientId,
							businessInformation.getProducts().getId(),
							businessInformation.getId(),
							businessInformation.getValueAmount(), lobId);
					break;

				default:
					break;
				}

				updatedClientTotManager.add(buInfoDTO);

			}

		}

		return updatedClientTotManager;
	}

	public ArrayList<MarshClientDTO> getAvailableBusinessInformationByCE(int id) {
		ArrayList<MarshClientDTO> result = new ArrayList<MarshClientDTO>();

		List<InformationOwners> marshClientsList = domainDrillerService
				.getInformationOwnersChildren(id); // get the clients of the CE

		RowTotalsManager rtm = new RowTotalsManager();
		ProductTotalsManager ptm = new ProductTotalsManager();

		for (InformationOwners informationOwners : marshClientsList) {
			log.debug("INFORMATION OWNER: "
					+ informationOwners.getDescription() + " parentId = " + id);

			MarshClientDTO mc = new MarshClientDTO(rtm);// create the client dto

			// create the total manager

			result.add(mc);
			mc.setCeId(id); // the CE whom this client refers to
			mc.setClientId(informationOwners.getId());
			mc.setClientName(informationOwners.getDescription());

			List<ProductInfoDTO> clientProductList = new ArrayList<ProductInfoDTO>(); // instantiate
																						// the
																						// list
																						// of
																						// products
																						// that'll
																						// be
																						// filled
																						// with
																						// available
																						// business
																						// info

			mc.setProductInfoList(clientProductList);

			List<BusinessInformation> businessInfoList = new ArrayList<BusinessInformation>();
			List<BusinessInformation> resetInfoList = domainDrillerService
					.getAvailableBusinessInformationByType(
							informationOwners.getId(), yearArray[0].getId(),
							BusinessInfoValueType.RESET);
			log.debug("\tRESET: " + resetInfoList.size());
			businessInfoList.addAll(resetInfoList);

			List<BusinessInformation> potentialInfoList = domainDrillerService
					.getAvailableBusinessInformationByType(
							informationOwners.getId(), yearArray[0].getId(),
							BusinessInfoValueType.POTENTIAL);
			log.debug("\tPOTENTIAL: " + potentialInfoList.size());
			businessInfoList.addAll(potentialInfoList);

			List<BusinessInformation> brokerPotentialInfoList = domainDrillerService
					.getAvailableBusinessInformationByType(
							informationOwners.getId(), yearArray[0].getId(),
							BusinessInfoValueType.BROKER_POT);
			log.debug("\tBROKER_POT: " + brokerPotentialInfoList.size());
			businessInfoList.addAll(brokerPotentialInfoList);

			List<BusinessInformation> companyPotentialInfoList = domainDrillerService
					.getAvailableBusinessInformationByType(
							informationOwners.getId(), yearArray[0].getId(),
							BusinessInfoValueType.COMPANY_POT);
			log.debug("\tCOMPANY_POT: " + companyPotentialInfoList.size());
			businessInfoList.addAll(companyPotentialInfoList);

			List<BusinessInformation> achievedInfoList = domainDrillerService
					.getAvailableBusinessInformationByType(
							informationOwners.getId(), yearArray[0].getId(),
							BusinessInfoValueType.ACHIEVED);
			log.debug("\tACHIEVED: " + achievedInfoList.size());
			businessInfoList.addAll(achievedInfoList);

			List<BusinessInformation> finalBalanceInfoList = domainDrillerService
					.getAvailableBusinessInformationByType(
							informationOwners.getId(), yearArray[1].getId(),
							BusinessInfoValueType.FINAL_BALANCE);
			log.debug("\tFINAL_BALANCE: " + finalBalanceInfoList.size());
			businessInfoList.addAll(finalBalanceInfoList);

			// iterate the available business info
			Iterator<BusinessInformation> it = businessInfoList.iterator();

			while (it.hasNext()) {
				BusinessInformation buInfo = it.next();

				String lobId = buInfo.getProducts().getParentId();// lob id

				BusinessInfoValueType thisInfoType = BusinessInfoValueType
						.fromString(buInfo.getValueType());

				BusinessInfoDTO buInfoDTO = null;

				switch (thisInfoType) {

				case RESET:
					buInfoDTO = new ResetInfoDTO(mc.getClientId(), buInfo
							.getProducts().getId(), buInfo.getId(),
							buInfo.getValueAmount(), lobId);
					break;

				case POTENTIAL:
					buInfoDTO = new PotentialInfoDTO(mc.getClientId(), buInfo
							.getProducts().getId(), buInfo.getId(),
							buInfo.getValueAmount(), lobId);
					break;

				case ACHIEVED:
					buInfoDTO = new AchievedInfoDTO(mc.getClientId(), buInfo
							.getProducts().getId(), buInfo.getId(),
							buInfo.getValueAmount(), lobId);
					break;

				case BROKER_POT:
					buInfoDTO = new BrokerPotBalanceInfoDTO(mc.getClientId(),
							buInfo.getProducts().getId(), buInfo.getId(),
							buInfo.getValueAmount(), lobId);
					break;

				case COMPANY_POT:
					buInfoDTO = new CompanyPotBalanceInfoDTO(mc.getClientId(),
							buInfo.getProducts().getId(), buInfo.getId(),
							buInfo.getValueAmount(), lobId);
					break;

				case FINAL_BALANCE:
					buInfoDTO = new FinalBalanceInfoDTO(mc.getClientId(),
							buInfo.getProducts().getId(), buInfo.getId(),
							buInfo.getValueAmount(), lobId);
					break;

				default:
					break;
				}

				buInfoDTO.setPostItText(buInfo.getPostIt());// TODO caricare le
															// note sulla
															// business info che
															// viene
															// visualizzata
															// prendendole da
															// ogni nota di ogni
															// business info
															// disponibile per
															// la triade
															// prodotto-cliente-anno
															// di riferimento
				mc.getClientTotalsManager().add(buInfoDTO);

				ProductInfoDTO prodInfoDTO = null;
				// cerco l'info di prodotto tra quelle già create per questo
				// gruppo commerciale
				prodInfoDTO = mc.getProductInfoByProductId(buInfo.getProducts()
						.getId());

				if (prodInfoDTO == null)
					prodInfoDTO = new ProductInfoDTO(mc, buInfo.getProducts()
							.getId(), ptm);

				prodInfoDTO.addBusinessInfo(buInfoDTO);

				prodInfoDTO.setItemName(buInfo.getProducts().getItemName());
				prodInfoDTO.setItemType(buInfo.getProducts().getItemType());
				// prodInfoDTO.setId(buInfo.getProducts().getId()); già messo
				// nel costruttore
				prodInfoDTO.setNotes(buInfo.getProducts().getNotes());
				prodInfoDTO.setParentId(lobId);

				clientProductList.add(prodInfoDTO);

			}

		}

		return result;

	}

	@Override
	public ArrayList<ArrayList<LineOfBusiness>> getLobByPageSplit() {

		return pageConfigurator.lobsByPageList;
	}

	@Override
	public void commitBusinessInformation(BusinessInfoDTO toBeCommitted) {

		ReferenceYears methodYear = yearArray[0];

		if (BusinessInfoValueType.FINAL_BALANCE == toBeCommitted.getValueType()) {
			methodYear = yearArray[1];
		}

		// create the detached object for the DB
		BusinessInformation myInfo = new BusinessInformation();

		// retrieve the information owners of the client
		InformationOwners mClient = domainDrillerService
				.findInformationOwnerById(toBeCommitted.getClientId());
		myInfo.setInformationOwners(mClient);

		myInfo.setReferenceYears(methodYear);

		// retrieve the product
		Products product = domainDrillerService.findProductById(toBeCommitted
				.getProductId());
		myInfo.setProducts(product);

		myInfo.setId(toBeCommitted.getBusinessInfoId());

		// cerco, se esiste, una business info che non sia un consuntivo e che
		// abbia stesso anno, owner id, product id.
		List<BusinessInformation> bInfoList = domainDrillerService
				.findBusinessInformationByExample(mClient.getId(),
						methodYear.getId(), product.getId());

		if (!bInfoList.isEmpty()) {// During T0 period the lenght is 1 and it
									// might be a reset or a potential: TODO
									// define the use cases with FINAL BALANCE
			// se non è un final balance, allora posso andarci in update.
			// L'IPOTESI E' CHE CI POSSA ESSERE UN SOLO CONSUNTIVO E UN SOLO
			// POTENZIALE/COMPAGNIA/BROKER/RESET PER UNO STESSO PRODOTTO DI UNO
			// STESSO GRUPPO COMMERCIALE NELLO STESSO ANNO.
			for (BusinessInformation businessInformation : bInfoList) {
				BusinessInfoValueType temp = BusinessInfoValueType
						.fromString(businessInformation.getValueType());

				if (temp == toBeCommitted.getValueType()) {
					myInfo = businessInformation;
					break;
				} else if (isPotentialAtLarge(temp)
						&& isPotentialAtLarge(toBeCommitted.getValueType())) {
					myInfo = businessInformation;
					break;
				}

				// if( // FB e ACH non vengono aggiornati tramite l'interfaccia
				// !
				// BusinessInfoValueType.FINAL_BALANCE.name().equals(businessInformation.getValueType())
				// &&
				// !
				// BusinessInfoValueType.ACHIEVED.name().equals(businessInformation.getValueType())
				// ) {
				// // devo controllare che un FB o un ACH non aggiornino un POT
				// if(
				// toBeCommitted.getValueType() == temp ||
				// BusinessInfoValueType.RESET == temp ||
				// BusinessInfoValueType.BROKER_POT == temp ||
				// BusinessInfoValueType.POTENTIAL == temp ||
				// BusinessInfoValueType.COMPANY_POT == temp
				// ) {
				// myInfo = businessInformation;// TODO major rethinking: one
				// pot val during T0 period, one per day max following period
				// break;
				// }
				// }
			}
		}

		myInfo.setPostIt(toBeCommitted.getPostItText());

		myInfo.setCreated(new Date());

		switch (toBeCommitted.getValueType()) {

		case POTENTIAL:// save it, no matter what
			myInfo.setValueAmount(toBeCommitted.getValue());
			myInfo.setValueType(BusinessInfoValueType.POTENTIAL.name());
			domainDrillerService.pushToDB(myInfo);
			break;

		case BROKER_POT:
			myInfo.setValueAmount(toBeCommitted.getValue());
			myInfo.setValueType(BusinessInfoValueType.BROKER_POT.name());
			domainDrillerService.pushToDB(myInfo);
			break;

		case COMPANY_POT:
			myInfo.setValueAmount(toBeCommitted.getValue());
			myInfo.setValueType(BusinessInfoValueType.COMPANY_POT.name());
			domainDrillerService.pushToDB(myInfo);
			break;

		case RESET: // delete or save, depending on the attribute post it note
					// being empty or not
			myInfo.setValueType(BusinessInfoValueType.RESET.name());
			if (StringUtils.isEmpty(toBeCommitted.getPostItText())) {
				domainDrillerService.deleteBusinessInfo(myInfo);
			} else {
				domainDrillerService.pushToDB(myInfo);
			}
			break;

		// final balance e achieved sono usati SOLO dalle classi di test
		case FINAL_BALANCE:
			myInfo.setValueAmount(toBeCommitted.getValue());
			myInfo.setValueType(BusinessInfoValueType.FINAL_BALANCE.name());
			// myInfo.setReferenceYears(lastYear); // !! dal 2012 i test non
			// funzioneranno più, bisogna specializzare la gestione dell'anno
			domainDrillerService.pushToDB(myInfo);
			break;
		case ACHIEVED:
			myInfo.setValueAmount(toBeCommitted.getValue());
			myInfo.setValueType(BusinessInfoValueType.ACHIEVED.name());
			domainDrillerService.pushToDB(myInfo);
			break;
		}

	}

	/**
	 * Le business info di tipo potential, concettualmente, sono anche quelle di
	 * tipo RESET, POTENTIAL, BROKER_POT, COMPANY_POT.
	 * 
	 * @param temp
	 * @return true if temp is of type RESET, POTENTIAL, BROKER_POT, COMPANY_POT
	 */
	private boolean isPotentialAtLarge(BusinessInfoValueType temp) {
		if (BusinessInfoValueType.RESET == temp
				|| BusinessInfoValueType.BROKER_POT == temp
				|| BusinessInfoValueType.POTENTIAL == temp
				|| BusinessInfoValueType.COMPANY_POT == temp) {
			return true;
		} else
			return false;
	}

	@Override
	public int getCETotalForPotential(int clientExecutiveId) {
		return calculationService.getTotalPotentialForAnyInformationOwner(
				clientExecutiveId, yearArray[0].getId());
	}

	@Override
	public int getCETotalForAchieved(int id) {
		return calculationService.getTotalAchievedForAnyInformationOwner(id,
				yearArray[0].getId());
	}

	@Override
	public LightInfoOwnerDto[] getAvailableInformationByParent(
			ArrayList<ProductInfoDTO> orderdProductsArray, int parentId) {

		List<InformationOwners> children = domainDrillerService
				.findInformationOwnerByParentId(parentId); // recupero tutti i
															// children del
															// parent in
															// argomento

		if (null == children || children.isEmpty()) {// se children è nullo vuol
														// dire che in ingresso
														// c'è un gruppo
														// commerciale. Il
														// gruppo commerciale è
														// figlio di se stesso
			InformationOwners o = domainDrillerService
					.findInformationOwnerById(parentId);
			children.add(o);
		}

		LightInfoOwnerDto[] resultArray = new LightInfoOwnerDto[children.size()];

		int counter = 0;

		long start = new Date().getTime();

		for (InformationOwners child : children) { // per ogni child creo il dto
													// con le informazioni sulla
													// saturazione del
													// potenziale e le altre
													// info da mostrare in
													// pagina
			if (child != null) {
				LightInfoOwnerDto resultElement = new LightInfoOwnerDto();
				resultArray[counter++] = resultElement;
				resultElement.setIdentificativo(child.getDescription()); // nome
																			// della
																			// location:
																			// cliente,
																			// ufficio,
																			// client
																			// executive
				resultElement.setEntityType(child.getOwnerType());
				int totalePotenzialeRiga = calculationService
						.getTotalPotentialForAnyInformationOwner(child.getId(),
								yearArray[0].getId());
				resultElement.setTotalePotenzialeEuro(totalePotenzialeRiga); // totale
																				// di
																				// riga
																				// del
																				// potenziale,
																				// in
																				// euro
				int totaleAchievedRiga = calculationService
						.getTotalAchievedForAnyInformationOwner(child.getId(),
								yearArray[0].getId());
				resultElement.setTotaleAchievedEuro(totaleAchievedRiga);// totale
																		// di
																		// riga
																		// dell'achieved,
																		// in
																		// euro

				// preparo l'array con i prodotti da visualizzare in pagina
				LightProductInfoDto[] productArray = new LightProductInfoDto[orderdProductsArray
						.size()];
				resultElement.setOrderedProductArray(productArray);

				// deduco gli id negativi, relativi a colonne della pagina
				// vuote.
				ArrayList<Integer> integerList = new ArrayList<Integer>();
				for (ProductInfoDTO product : orderdProductsArray) {
					if (product != null)
						integerList.add(new Integer(product.getId()));
				}

				Map<Integer, Integer> productTotPotMap = calculationService
						.getTotalPotentialByProductList(child,
								yearArray[0].getId(), integerList);
				Map<Integer, Integer> productTotAchMap = calculationService
						.getTotalAchievedByProductList(child,
								yearArray[0].getId(), integerList);

				int arrayIndex = 0;
				for (ProductInfoDTO productDto : orderdProductsArray) { // la
																		// lista
																		// DEVE
																		// essere
																		// lunga
																		// 8,
																		// con
																		// elementi
																		// null
																		// in
																		// corrispondenza
																		// delle
																		// colonne
																		// vuote

					if (productDto == null) { // è una convenzione per dire che
												// il prodotto in questa
												// posizione dell'array in
												// realtà è un blank space.
						productArray[arrayIndex] = null;

					} else {
						LightProductInfoDto resultProduct = new LightProductInfoDto();
						productArray[arrayIndex] = resultProduct;

						resultProduct.setProductName(productDto.getItemName());
						resultProduct.setProductDescription(productDto
								.getNotes());
						resultProduct.setProductId(productDto.getId());

						resultProduct
								.setEuroPotValue(productTotPotMap
										.get(productDto.getId()) != null ? productTotPotMap
										.get(productDto.getId()) : 0);
						resultProduct
								.setEuroAchValue(productTotAchMap
										.get(productDto.getId()) != null ? productTotAchMap
										.get(productDto.getId()) : 0);

					}
					arrayIndex++;
				}

			}
		}

		long stop = new Date().getTime();

		long totTime = stop - start;
		long avgTime = 1;
		if (children.size() != 0)
			avgTime = totTime / children.size();

		log.warn("!!!!!!!!!!!!!!!!!!!tempo di esecuzione: " + totTime
				+ " tempo per ciclo: " + avgTime);

		return resultArray;
	}

	@Override
	public int[] getChildrenIdByParentId(int parentId) {
		List<InformationOwners> children = domainDrillerService
				.findInformationOwnerByParentId(parentId); // recupero tutti i
															// children del
															// parent in
															// argomento
		int[] ids = new int[children.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = children.get(i).getId();
		}
		return ids;
	}

	public void disableClient(int id) {
		InformationOwners cliente = domainDrillerService
				.findInformationOwnerById(id);
		if (null != cliente) {
			cliente.setEnabled(false);
			domainDrillerService.mergeInformationOwner(cliente);
		}
	}

	@Override
	public ArrayList<NavigationDTO> getUffici() {
		ArrayList<NavigationDTO> dtoList = new ArrayList<NavigationDTO>();
		List<InformationOwners> uffici = domainDrillerService.getUffici();
		for (InformationOwners informationOwners : uffici) {
			dtoList.add(new NavigationDTO(informationOwners.getDescription(),
					"" + informationOwners.getId(), OwnerType.OFFICE));
		}
		return dtoList;
	}

	@Override
	public ArrayList<NavigationDTO> getCE(int id) {
		ArrayList<NavigationDTO> dtoList = new ArrayList<NavigationDTO>();
		List<InformationOwners> ce = domainDrillerService.getCE(id);
		if (ce != null) {
			if (ce.size() >= 2) {
				dtoList.add(new NavigationDTO("Tutti", "" + id, OwnerType.CE));
			}
			for (InformationOwners informationOwners : ce) {
				dtoList.add(new NavigationDTO(informationOwners
						.getDescription(), "" + informationOwners.getId(),
						OwnerType.CE));
			}
		}
		return dtoList;
	}

	@Override
	public ArrayList<NavigationDTO> getGC(int id) {
		ArrayList<NavigationDTO> dtoList = new ArrayList<NavigationDTO>();
		List<InformationOwners> gc = domainDrillerService.getGC(id);
		if (gc != null) {
			if (gc.size() >= 2) {
				dtoList.add(new NavigationDTO("Tutti", "" + id,
						OwnerType.GRUPPO_COMMERCIALE)); // aggiungo tutti
			}
			for (InformationOwners informationOwners : gc) {
				dtoList.add(new NavigationDTO(informationOwners
						.getDescription(), "" + informationOwners.getId(),
						OwnerType.GRUPPO_COMMERCIALE));
			}
		}
		return dtoList;
	}

	@Override
	public ArrayList<String> getLobNames() {
		List<Products> products = domainDrillerService.getAvailableLOB();
		ArrayList<String> result = new ArrayList<String>();
		for (Products lob : products) {
			result.add(lob.getItemName());

		}
		return result;

	}

}
