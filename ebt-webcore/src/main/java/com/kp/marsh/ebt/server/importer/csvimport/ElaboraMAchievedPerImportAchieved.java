package com.kp.marsh.ebt.server.importer.csvimport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.kp.marsh.ebt.server.importer.ElaboratoreInfoController;
import com.kp.marsh.ebt.server.importer.csvimport.exception.ImportException;
import com.kp.marsh.ebt.server.importer.dao.ImportController;
import com.kp.marsh.ebt.server.importer.dto.MarshCE;
import com.kp.marsh.ebt.server.importer.dto.MarshGruppoCommerciale;
import com.kp.marsh.ebt.server.importer.dto.MarshOffice;
import com.kp.marsh.ebt.server.importer.dto.MarshProduct;
import com.kp.marsh.ebt.server.webapp.data.domain.BusinessInformation;
import com.kp.marsh.ebt.server.webapp.data.domain.InformationOwners;
import com.kp.marsh.ebt.server.webapp.data.domain.MarshPeopleCredentials;
import com.kp.marsh.ebt.server.webapp.data.domain.Products;
import com.kp.marsh.ebt.server.webapp.data.domain.ReferenceYears;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.DomainDrillerService;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;
import com.kp.marsh.ebt.shared.dto.OwnerType;

/**
 * @author dariobrambilla
 *
 * Elabora i record contenuti in M_ACHIEVED per creare/aggiornare le business information di tipo ACHIEVED
 *
 */
public class ElaboraMAchievedPerImportAchieved implements ElaboratoreInfoController {


	private ImportController importController;

	private DomainDrillerService domainDrillerService;

	private static Log log = LogFactory.getLog(ElaboraMAchievedPerImportAchieved.class);


	@Inject
	public ElaboraMAchievedPerImportAchieved(ImportController importController, DomainDrillerService domDrilSer) {
		this.importController = importController;
		this.domainDrillerService = domDrilSer;
	}


	/**
	 * Crea l'alberatura Ufficio, Ce, Gruppo commerciale da M_OFF_CE_ACCOUNTS. Recupera da M_Actuals i CE che hanno un consuntivo. Scrive su MARSH_PEOPLE_CREDENTIALS i CE che sono riportati in INFORMATION_OWNERS
	 *NON SO SE SERVE A QUALCOSA -->> rifattorizzare in altra classe: serve per importare i consuntivi e inizializzare la tabella information owners
	 */
	public void sincronizzaCredenzialiCeConConsuntivo() {

		try {
			// 1) CREAZIONE UFFICI - CE
			List<MarshOffice> marshOfficeList = importController.listOffices(); // la lista degli uffici e dei rispettivi ce presa da M_OFF_CE_ACCOUNTS
			creaAggiornaInformationOwnersUfficiCE(marshOfficeList);

			// 2) CREAZIONE CLIENT EXECUTIVES - GRUPPI COMMERCIALI
			List<MarshCE> ceList = importController.listClientExecutivesWithFinalBalance(); // la lista dei CE che nel 2010 ha fatto un consuntivo presa da M_ACTUALS
			creaAggiornaInformationOwnersCEGruppiCommerciali(ceList);


			// 3) AGGIORNAMENTO/SCRITTURA DELLE CREDENZIALI DI ACCESSO SU MARSH_PEOPLE_CREDENTIALS
			List<MarshOffice> officeList = importController.listOffices();
			creaAggiornaCredenzialiCE(officeList);


		} catch(Exception e) {
			log.error("Exception in salvaInformationOwners", e);
			e.printStackTrace();
		}
	}

	/** Deve considerare ogni record in M_ACHIEVED e aggiornare/creare la corrispondente business information, escludendo quelle dei information owners non presenti nella relativa tabella INFORMATION_OWNERS
	 * Questo metodo aggiorna la relazione client executive --> gruppo commerciale
	 * @throws ImportException 
	 * 
	 */
	public String elaboraAchieved() throws ImportException {
		StringBuffer message = new StringBuffer("\nCarico della produzione  dei Gruppi commerciali presenti su INFORMATION_OWNERS e aggiornamento dei CE associati a una Capogruppo\n");
		StringBuffer achievedLog = new StringBuffer();
		StringBuffer allocazioneCapogruppoLog = new StringBuffer();
		Set<String> prodottiCaricatiNonCensiti = new HashSet<String>(); // sono i prodotto caricati in M_ACHIEVED ma non censiti su PRODUCTS
		// recupero la lista dei CE (e relativi prodotti e achieved) contenuta nella tabella M_ACHIEVED
		List<MarshGruppoCommerciale> gruppiCommercialiConAchieved = importController.listGruppiComercialiWithAchieved(); // recupera i gruppi commerciali come sono sconfigurati nel file ricevuto. Questa lista può avere dei gruppi commervciali nuovi oppure gruppi commerciali che sono passati da un CE ad un altro
		if(gruppiCommercialiConAchieved==null || gruppiCommercialiConAchieved.size()<=0){
			throw new ImportException("ElaboraMAchievedPerImportAchieved.elaboraAchieved: lista MarshGruppoCommerciale nulla o vuota");
		}
		int okGC = 0;
		try {
			// aggiorno su INFORMATION_OWNERS tutti gli achieved della lista precedente che appartengono ad un information owner GIA' PRESENTE su INFORMATION_OWNERS
			for (MarshGruppoCommerciale gruppoCommerciale : gruppiCommercialiConAchieved) {
				// è il gruppo commerciale che ha un riferimento su BUSINESS_INFORMATION, per cui devo controllare se questo gruppo commerciale è presente su INFORMATION_OWNERS.
				// Se lo è allora creo o aggiorno le relative informazioni di ACHIEVED.
				InformationOwners gruppoCommercialeIO = importController.findInformationOwnerByEysCode(gruppoCommerciale.getCodiceCapoGruppo());
				if(gruppoCommercialeIO==null){
					log.debug("ATTENZIONE: il gruppo commeriale con codice eurosys: "+gruppoCommerciale.getCodiceCapoGruppo()+" non è stato trovato in INFORMATION_OWNERS");
					continue;

				}

				if(!OwnerType.GRUPPO_COMMERCIALE.name().equals(gruppoCommercialeIO.getOwnerType())) {
					log.debug("ATTENZIONE: il codice eys "+gruppoCommerciale.getCodiceCapoGruppo()+ " dichiarato in M_ACHIEVED corrisponde  su INFORMATION_OWNERS ad un: "+gruppoCommercialeIO.getOwnerType());
					continue;

				}
				okGC++; // gruppo commerciale trovato su information owners


				// se esiste, controllare che il client executive di riferimento sia marshCE (vedi for sopra) e se non lo è aggiornare il parent id di conseguenza
				log.debug("Aggiorno il gruppo commerciale "+gruppoCommerciale.getDenominazione()+" - "+gruppoCommerciale.getCodiceCapoGruppo());
				List<String> result = creaAggiornaAchievedPerGruppoCommerciale(gruppoCommerciale, gruppoCommercialeIO);
				achievedLog.append(result.get(0));
				result.remove(0);
				prodottiCaricatiNonCensiti.addAll(result);

			}
		

	} catch(Exception e) {
		log.error("Exception in salvaInformationOwners: "+e.getMessage());
		e.printStackTrace();

	}

	StringBuffer prodottiLog = new StringBuffer();
	if(!prodottiCaricatiNonCensiti.isEmpty()) {
		for (String prodottoNonCensito : prodottiCaricatiNonCensiti) {
			prodottiLog.append(prodottoNonCensito);

		}
	}

	return message.toString()+achievedLog.toString()+String.format("\n\n ### (%s) PRODOTTI NON CENSITI PRESENTI NEL CSV ###\n",prodottiCaricatiNonCensiti.size())+prodottiLog.toString()+"\n";

}


/* (non-Javadoc)
 * @see com.kp.marsh.Main#creaAggiornaCredenzialiCE(java.util.List)
 */
@Override
public void creaAggiornaCredenzialiCE(List<MarshOffice> officeList) throws Exception {
	int marshPCcounter = 0;
	for (MarshOffice marshOffice : officeList) {
		boolean nuovoMarshPC = false;
		Iterator<String> ceIterator = marshOffice.getMarshCEMap().keySet().iterator();
		while (ceIterator.hasNext()) {
			String eysCeCode = (String) ceIterator.next();
			MarshCE mCe = marshOffice.getMarshCEMap().get(eysCeCode);
			// AGGIORNO CE-AD USERNAME E CREO ASSOCIAZIONE SU MARSH PEOPLE CREDENTIALS
			InformationOwners clientExecutive = importController.findInformationOwnerByEysCode(mCe.getEysCode());
			MarshPeopleCredentials mpc = importController.findMarshCredentialByADUsername(mCe.getUsername());
			if(null == mpc) {
				mpc = new MarshPeopleCredentials();
				mpc.setUsername(mCe.getUsername());
				nuovoMarshPC =  true;
			}
			mpc.setInformationOwners(clientExecutive);
			mpc.setCreated(new Date());
			importController.saveUpdate(mpc);
			if(nuovoMarshPC){
				log.info("Creato nuovo marsh people credential con id: "+mpc.getId());
				marshPCcounter++;
			}
		}
	}
	log.info("Creati "+marshPCcounter+" nuovi marsh people credential");
}

/* (non-Javadoc)
 * @see com.kp.marsh.Main#creaAggiornaInformationOwnersCEGruppiCommerciali(java.util.List)
 */
@Override
public void creaAggiornaInformationOwnersCEGruppiCommerciali(List<MarshCE> ceList) throws Exception {
	int nuoviGruppiCommercialiCounter = 0;
	for (MarshCE marshCE : ceList) {
		boolean nuovoGC = false;
		// CREO ASSOCIAZIONE CE-GRUPPO COMMERCIALE
		InformationOwners clientExecutive = importController.findInformationOwnerByEysCode(marshCE.getEysCode()); // recupero dal DB il CE caricato nel ciclo precedente. NON PUÒ ESSERE NULL
		// devo creare tutti i gruppi commerciali del client executive
		Iterator<String> gruppiCommIterator = marshCE.getGruppoCommercialeMap().keySet().iterator();
		while (gruppiCommIterator.hasNext()) {
			String codiceCapogruppo = (String) gruppiCommIterator.next();
			MarshGruppoCommerciale mGc = marshCE.getGruppoCommercialeMap().get(codiceCapogruppo);
			InformationOwners gruppoCommerciale = importController.findInformationOwnerByEysCode(StringUtils.leftPad(codiceCapogruppo, 6,"0"));
			if(null == gruppoCommerciale) {
				gruppoCommerciale = new InformationOwners();
				// padding a 6 left 0 per il codice capogruppo di Information Owners
				gruppoCommerciale.setCodEys(StringUtils.leftPad(codiceCapogruppo, 6,"0"));
				nuovoGC = true;
			}
			gruppoCommerciale.setEnabled(true);
			gruppoCommerciale.setCreated(new Date());
			gruppoCommerciale.setDescription(mGc.getDenominazione()+"-"+StringUtils.leftPad(codiceCapogruppo, 6,"0"));
			if(!nuovoGC && gruppoCommerciale.getParentId().intValue() != clientExecutive.getId()) {
				// il gruppo commerciale ha cambiato CE
				log.info("Il gruppo commerciale id: "+gruppoCommerciale.getId()+" description: "+gruppoCommerciale.getDescription()+" lascia il ce id: "+gruppoCommerciale.getParentId()+" per il ce id: "+clientExecutive.getId());
			}
			gruppoCommerciale.setParentId(clientExecutive.getId());
			gruppoCommerciale.setOwnerType(OwnerType.GRUPPO_COMMERCIALE.name());
			importController.saveUpdate(gruppoCommerciale);
			if(nuovoGC){
				log.info("Creato nuovo gruppo commerciale id: "+gruppoCommerciale.getId());
				nuoviGruppiCommercialiCounter++;
			}
		}
	}
	log.info("Creati "+nuoviGruppiCommercialiCounter+" nuovi gruppi commerciali");
}

/* (non-Javadoc)
 * @see com.kp.marsh.Main#creaAggiornaConsuntiviPerGruppoCommerciale(com.kp.marsh.dto.MarshGruppoCommerciale, com.kp.marsh.backend.data.domain.InformationOwners)
 */
@Override
public List<String> creaAggiornaAchievedPerGruppoCommerciale(MarshGruppoCommerciale mGc, InformationOwners gruppoCommerciale) throws Exception{
	return creaAggiornaBusinessInformationPerGruppoCommerciale(mGc, gruppoCommerciale, BusinessInfoValueType.ACHIEVED);		
}


@Override
public void creaAggiornaConsuntiviPerGruppoCommerciale(MarshGruppoCommerciale mGc, InformationOwners gruppoCommerciale) throws Exception{
	creaAggiornaBusinessInformationPerGruppoCommerciale(mGc, gruppoCommerciale, BusinessInfoValueType.FINAL_BALANCE);
}

private List<String> creaAggiornaBusinessInformationPerGruppoCommerciale(MarshGruppoCommerciale mGc, InformationOwners gruppoCommerciale, BusinessInfoValueType businessInfoValueType) throws Exception {
	int biAggiornate = 0;
	int biCreate = 0;
	StringBuffer logBuffer = new StringBuffer();
	Set<String> prodottiNonCensitiSet = new HashSet<String>();
	Iterator<String> prodNameIterator = mGc.getProductMap().keySet().iterator();
	// per tutti i prodotti del gruppo commerciale presenti su M_ACHIEVED
	while (prodNameIterator.hasNext()) {
		String productName = (String) prodNameIterator.next();
		MarshProduct mProduct = mGc.getProductMap().get(productName);
		String tableName="";
		long toBeTested = 0;
		switch (businessInfoValueType) {
		case FINAL_BALANCE:
			toBeTested = mProduct.getActual();
			tableName = "M_ACTUALS";
			break;

		case ACHIEVED:
			toBeTested = mProduct.getAchieved();
			tableName = "M_ACHIEVED";
			break;

		default:
			break;
		}

		if(toBeTested > 0) {
			ReferenceYears year = importController.findReferenceYearByName(mProduct.getYear());
			if(null == year) {
				log.debug("Year not found: "+mProduct.getYear()+ " Prodotto: "+mProduct.getName());
				continue;
			}
			Products product = importController.findEbtProductByName(productName);
			if(null == product)  {
				log.debug("ATTENZIONE!!! Nella tabella "+tableName+" è presente un prodotto non censito nella tabella PRODUCTS: "+productName);
				prodottiNonCensitiSet.add(productName);
				continue;
			}
			BusinessInformation bi = importController.findBusInfoByProdYearGruppoCommerciale(product, year, gruppoCommerciale, businessInfoValueType);
			boolean isCreato = false;
			if(null == bi) {
				isCreato = true;
				log.debug("\t\tCreo nuovo business information: "+product.getItemName()+" "+year.getReferenceYear()+ " "+gruppoCommerciale.getDescription()+" " +businessInfoValueType);
				bi = new BusinessInformation();
				bi.setValueType(businessInfoValueType.name());
				bi.setProducts(product);
				bi.setInformationOwners(gruppoCommerciale);
				bi.setReferenceYears(year);

			}
			bi.setCreated(new Date());

			long biValue = Math.round(toBeTested/100d)*100; // ARROTONDO E POI RIPORTO IN CENTINAIA

			bi.setValueAmount(""+biValue);

			if(Long.valueOf(bi.getValueAmount()) > 0) {
				logBuffer.append(String.format("\t %s \t %s \t %s \t da %s \t a %s \t %s \n", businessInfoValueType.name(), ""+year.getReferenceYear(), productName, bi.getValueAmount(),""+biValue,  gruppoCommerciale.getDescription()));
				importController.saveUpdate(bi);
				if(isCreato) {
					biCreate++;

				} else {
					biAggiornate++;

				}
				log.debug("Aggiornato/Creato businessInformation con valore: "+biValue+" e id: "+bi.getId());

			}else{
				log.debug("NON aggiorno/creo businessInformation con valore: "+biValue+" poichè in seguito all'arrotondamento il valore è < 0");

			}
			log.debug("\t\tValore su "+tableName+": "+toBeTested+" Valore riportato su BUSINESS_INFORMATION(id="+bi.getId()+"): "+biValue);

		}
	}
	//logging result
	String acticityLog = "";
	if(biAggiornate!=0)
		acticityLog = acticityLog + String.format("\nBusiness information aggiornate %s\n", ""+biAggiornate);
	if(biCreate!=0)
		acticityLog=  acticityLog+String.format("\nBusiness information create %s\n", ""+biCreate);
	if(biAggiornate!=0 || biCreate!=0)
		acticityLog = acticityLog+logBuffer.toString();
	List<String> result = new ArrayList<String>();
	result.add(acticityLog);
	if(!prodottiNonCensitiSet.isEmpty()) {
		for (String prodottoNonCensito : prodottiNonCensitiSet) {
			result.add(prodottoNonCensito);

		}
	}
	return result;

}


/* (non-Javadoc)
 * @see com.kp.marsh.Main#creaAggiornaInformationOwnersUfficiCE(java.util.List)
 */
@Override
public void creaAggiornaInformationOwnersUfficiCE(List<MarshOffice> marshOfficeList) throws Exception {
	int ufficiCreati = 0;
	int ceCreati = 0;
	for (MarshOffice marshOffice : marshOfficeList) {
		boolean nuovoUfficio = false;
		boolean nuovoCE = false;

		// CREAZIONE O RECUPERO DAL DB DELL'INFORMATION OWNER RELATIVO ALL'UFFICIO
		InformationOwners ufficio = importController.findInformationOwnerByEysCode(marshOffice.getEysCode());
		if(null == ufficio) { // se l'uffico con quel codice eurosys non esiste lo creo
			ufficio = new InformationOwners();
			ufficio.setCodEys(marshOffice.getEysCode());
			nuovoUfficio = true;
		}
		// AGGIORNAMENTO DELLE INFORMAZIONI RELATIVE ALL'UFFICIO
		ufficio.setEnabled(true);
		ufficio.setOwnerType(OwnerType.OFFICE.name());
		ufficio.setCreated(new Date());
		ufficio.setDescription(marshOffice.getOfficeName());
		ufficio.setParentId(null); // modificare nel caso si vogliano inserire gerarchie che contengono uffici

		//persisto subito l'ufficio così ho a disposizione l'id per i suoi client executives, da settargli come parent id
		importController.saveUpdate(ufficio);
		if(nuovoUfficio){
			log.info("Creato ufficio con id: "+ufficio.getId());
			ufficiCreati++;
		}

		// CREAZIONE O RECUPERO DEGLI INFORMATION OWNERS RELATIVI AI CE DI QUESTO UFFICIO
		Iterator<String> marshCeEysCodeIterator = marshOffice.getMarshCEMap().keySet().iterator();
		while (marshCeEysCodeIterator.hasNext()) {
			String ceCodEys = (String) marshCeEysCodeIterator.next();
			if(StringUtils.isEmpty(ceCodEys)) log.error("ATTENZIONE!!!!!!!!!!!!!! CODICE NULLO");
			MarshCE ce = marshOffice.getMarshCEMap().get(ceCodEys);
			InformationOwners clientExecutive = importController.findInformationOwnerByEysCode(ceCodEys); // recupero il CE e ne aggiorno le informazioni, oppure lo creo nuovo se non esiste
			if(null == clientExecutive) { // se il client executive con quel codice eurosys non esiste lo creo nuovo, altrimenti vado in update
				clientExecutive = new InformationOwners();
				clientExecutive.setCodEys(ceCodEys);
				nuovoCE = true;
			}
			clientExecutive.setEnabled(true);
			clientExecutive.setOwnerType(OwnerType.CE.name());
			clientExecutive.setCreated(new Date());
			if(!nuovoCE && clientExecutive.getParentId().intValue() != ufficio.getId()) {
				// il ce ha cambiato ufficio
				log.info("Il clientExecutive id: "+clientExecutive.getId()+" description: "+clientExecutive.getDescription()+" lascia l'ufficio id: "+clientExecutive.getParentId()+" per l'ufficio id: "+ufficio.getId());
			}
			clientExecutive.setParentId(ufficio.getId());
			clientExecutive.setDescription(ce.getNominativo());
			importController.saveUpdate(clientExecutive);
			if(nuovoCE){
				log.info("Creato ce con id: "+clientExecutive.getId());
				ceCreati++;

			}
		}
		log.debug(marshOffice.getEysCode() + " " +marshOffice.getOfficeName());
	}
	log.info("Creati "+ufficiCreati+" nuovi uffici");
	log.info("Creati "+ceCreati+" nuovi client executive ");
}





}
