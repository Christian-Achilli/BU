package com.kp.marsh.ebt.server.importer.dao;

import java.util.List;

import com.kp.marsh.ebt.server.importer.csvimport.exception.ImportException;
import com.kp.marsh.ebt.server.importer.dto.MarshCE;
import com.kp.marsh.ebt.server.importer.dto.MarshGruppoCommerciale;
import com.kp.marsh.ebt.server.importer.dto.MarshOffice;
import com.kp.marsh.ebt.server.importer.model.MAchieved;
import com.kp.marsh.ebt.server.importer.model.MOffCeAccounts;
import com.kp.marsh.ebt.server.webapp.data.domain.BusinessInformation;
import com.kp.marsh.ebt.server.webapp.data.domain.InformationOwners;
import com.kp.marsh.ebt.server.webapp.data.domain.MarshPeopleCredentials;
import com.kp.marsh.ebt.server.webapp.data.domain.Products;
import com.kp.marsh.ebt.server.webapp.data.domain.ReferenceYears;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;

public interface ImportController {

	/**
	 * Crea una lista dalla M_OFF_CE_ACCOUNTS
	 * @return una lista di MOffCeAccounts
	 */
	public List<MOffCeAccounts> listAnagCE();
	
	/**
	 * truncate della tabella M_OFF_CE_ACCOUNTS
	 */
	public void pulisciAnagCe();
	
	/**
	 * truncate della tabella M_ACHIEVED
	 */
	public void pulisciAchieved();
	
	/**
	 * @return the CElist and their clients from M_ACTUALS. La lista dei CE è indipendente da quello che c'è su INFORMATION_OWNERS
	 */
	public abstract List<MarshCE> listClientExecutivesWithFinalBalance();
	
	/**
	 * @return the MarshGruppoCommerciale and their clients from MACHIEVED. La lista dei gruppi commerciali è indipendente da quello che c'è su INFORMATION_OWNERS.
	 * Per ogni gruppo commerciale c'è la mappa dei prodotti-achieved recuperata da MAchieved
	 */
	public abstract List<MarshGruppoCommerciale> listGruppiComercialiWithAchieved() ;

	/**
	 * @return the office list and their CE from M_OFF_CE_ACCOUNTS
	 */
	public abstract List<MarshOffice> listOffices();

	/**
	 * @param eysCode
	 * @return the InformationOwner with the given codEys
	 * @throws Exception
	 */
	public abstract InformationOwners findInformationOwnerByEysCode(
			String codEys) throws Exception;

	public abstract void saveUpdate(Object object);
	public void saveUpdateThrowable(Object object) throws ImportException;

	public abstract MarshPeopleCredentials findMarshCredentialByADUsername(
			String username) throws Exception;

	/**
	 * @param prd
	 * @param year
	 * @param io
	 * @param valtype
	 * @return la eventuale business information per il quartetto specificato nella firma or null
	 * @throws Exception
	 */
	public abstract BusinessInformation findBusInfoByProdYearGruppoCommerciale(
			Products prd, ReferenceYears year, InformationOwners io, BusinessInfoValueType valtype)
			throws Exception;

	public abstract Products findEbtProductByName(String name);

	public abstract ReferenceYears findReferenceYearByName(String year);

	/**
	 * @return tutta la tabella M_ACHIEVED
	 */
	public List<MAchieved> listGruppiCommerciali();

}