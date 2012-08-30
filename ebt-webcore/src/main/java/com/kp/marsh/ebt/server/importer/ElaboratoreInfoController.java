package com.kp.marsh.ebt.server.importer;

import java.util.List;

import com.kp.marsh.ebt.server.importer.dto.MarshCE;
import com.kp.marsh.ebt.server.importer.dto.MarshGruppoCommerciale;
import com.kp.marsh.ebt.server.importer.dto.MarshOffice;
import com.kp.marsh.ebt.server.webapp.data.domain.InformationOwners;

/**
 * @author dariobrambilla
 *
 * Contiene i metodi che servono ad elaborare la tabella M_ACHIEVED ed M_ACTUALS in modo da ricavarne le business information e le configurazioni
 * per le tabelle degli utenti information owners
 *
 */
public interface ElaboratoreInfoController {

	/**
	 * Crea le credenziali sulla tabella MARSH PEOPLE CREDENTIALS  e le associa ai rispettivi information owners
	 * @param officeList la lista degli uffici e dei rispettivi ce di afferenza 
	 * @throws Exception
	 */
	public abstract void creaAggiornaCredenzialiCE(List<MarshOffice> officeList)
			throws Exception;

	/**
	 * Crea gli information owners dei gruppi commerciali
	 * @param ceList la lista dei CE e dei loro rispettivi gruppi commerciali
	 * @throws Exception
	 */
	public abstract void creaAggiornaInformationOwnersCEGruppiCommerciali(
			List<MarshCE> ceList) throws Exception;

	/**
	 * Crea o aggiorna le business information per l'information owner gruppo commerciale <code>gruppoCommerciale</code>
	 * @param mGc il dto che contiene i dati sugli actuals forniti da marsh tramite il file csv e importati sulla tabella M_ACTUALS
	 * @param gruppoCommerciale l'information owners relativo al gruopo commerciale
	 * @throws Exception
	 */
	public abstract void creaAggiornaConsuntiviPerGruppoCommerciale(
			MarshGruppoCommerciale mGc, InformationOwners gruppoCommerciale)
			throws Exception;

	/**
	 * Crea gli information owners di uffici e ce
	 * @param marshOfficeList la lista degli uffici e dei relativi CE 
	 * @throws Exception
	 */
	public abstract void creaAggiornaInformationOwnersUfficiCE(
			List<MarshOffice> marshOfficeList) throws Exception;

	/**
	 * @param mGc
	 * @param gruppoCommerciale
	 * @return in posizione 0 il log da mostare a video e a seguire i nomi di tutti i prodotti non censiti che si trovano nel file csv in carico
	 * @throws Exception
	 */
	List<String> creaAggiornaAchievedPerGruppoCommerciale(MarshGruppoCommerciale mGc, InformationOwners gruppoCommerciale) throws Exception;
}
