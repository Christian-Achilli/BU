/**
 * 
 */
package com.kp.marsh.ebt.server.importer.csvimport;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.kp.marsh.ebt.server.importer.csvimport.exception.ImportException;
import com.kp.marsh.ebt.server.importer.dao.ImportController;
import com.kp.marsh.ebt.server.importer.model.MOffCeAccounts;
import com.kp.marsh.ebt.server.webapp.data.domain.InformationOwners;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.DomainDrillerService;

/**
 * @author dariobrambilla
 *
 * Elabora i record contenuti in M_OFF_CE_ACCOUNTS per aggiornare l'ufficio di appartenenza delle information owner di tipo CE contenuti in marsh_people_credentials
 *
 */
public class ElaboraAnagCE  {


	private ImportController importController;

	private DomainDrillerService domainService;



	private static Log log = LogFactory.getLog(ElaboraAnagCE.class);


	@Inject
	public ElaboraAnagCE(ImportController importController, DomainDrillerService domainService) {
		this.importController = importController;
		this.domainService = domainService;

	}


	/** 
	 * Considerara ogni record in M_OFF_CE_ACCOUNTS e aggiorna il parent id del corrispondente information owner dopo aver controllato che l'ufficio esista e che il ce sia al momento appartnente a un altro ufficio
	 * @throws ImportException 
	 */
	public String spostaCEDiUfficio() throws ImportException {
		StringBuffer message = new StringBuffer("\ncontrollo per trasferimenti di ufficio dei CE ed aggiornamento DB\n");
		List<MOffCeAccounts> mOffCeAccounts = importController.listAnagCE();
		if(mOffCeAccounts==null || mOffCeAccounts.size()<=0){
			throw new ImportException("ElaboraAnagCE.ElaboraAnagCe: lista MOffCeAccounts nulla o vuota");
		}
		int ok = 0;
		for (MOffCeAccounts accounts : mOffCeAccounts) {
			try {
				String codiceEurosys = accounts.getCodiceEurosys();
				InformationOwners informationOwnerCE = importController.findInformationOwnerByEysCode(codiceEurosys);
				if(informationOwnerCE==null){
					log.debug("ATTENZIONE!!! Il CE il cui eysCode  "+codiceEurosys+" non  censito su INFORMATION_OWNERS.");
					continue;
				}
				String codiceUfficio = accounts.getCodiceUfficio();
				InformationOwners ufficioNelCsv = importController.findInformationOwnerByEysCode(codiceUfficio); // ufficio presente nel file csv associato al presente CE
				if(ufficioNelCsv==null){
					log.debug("ATTENZIONE!!! L'ufficio specificato dal CE il cui eysCode  "+codiceEurosys+" non esiste.");
					continue;
				}
				if(informationOwnerCE.getParentId()==null){
					log.debug("ATTENZIONE!!! informationOwnerCE id: "+informationOwnerCE.getId()+"ha ufficio nullo");
					continue;
				}
				if(informationOwnerCE.getParentId()==ufficioNelCsv.getId()){
					log.debug("ATTENZIONE!!! L'information owners (CE) id: "+informationOwnerCE.getId()+"  giˆ appartenete all'ufficio di cui viene richiesta l'attribuzione");
					continue;
				}

				// log to UI
				InformationOwners attualeUfficioSulDB = domainService.findInformationOwnerById(informationOwnerCE.getParentId());
				message.append(String.format("%s \t da %s (cod eys:%s) \t -->> \t a %s  (cod eys:%s)\n", informationOwnerCE.getDescription(), attualeUfficioSulDB.getDescription(), attualeUfficioSulDB.getCodEys(), ufficioNelCsv.getDescription(), ufficioNelCsv.getCodEys()));

				informationOwnerCE.setCreated(new Date());
				informationOwnerCE.setParentId(ufficioNelCsv.getId());
				importController.saveUpdate(informationOwnerCE);
				ok++;
			}catch (Exception e) {
				log.error("ElaboraAnagCE.elaboraAnagCe	--> Errore durante l'aggiornamento dell'ufficio di appartenenza per i CE presenti in M_OFF_CE_ACCOUNTS: "+e);
				e.printStackTrace();
			}
		}
		log.info("COMPLETATO AGGIORNAMENTO ufficio appartenenza per "+ok+" CE dei "+mOffCeAccounts.size()+" presenti in M_OFF_CE_ACCOUNTS");
		message.append("\nCOMPLETATO AGGIORNAMENTO ufficio appartenenza per "+ok+" CE dei "+mOffCeAccounts.size()+" presenti in M_OFF_CE_ACCOUNTS\n");
		return message.toString();
	}
}