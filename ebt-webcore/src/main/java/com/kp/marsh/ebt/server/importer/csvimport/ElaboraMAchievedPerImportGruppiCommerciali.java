package com.kp.marsh.ebt.server.importer.csvimport;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.kp.marsh.ebt.server.importer.dao.ImportController;
import com.kp.marsh.ebt.server.importer.model.MAchieved;
import com.kp.marsh.ebt.server.webapp.data.domain.InformationOwners;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.DomainDrillerService;

public class ElaboraMAchievedPerImportGruppiCommerciali {

	private ImportController importController;

	private DomainDrillerService domainDrillerService;

	private static Log log = LogFactory.getLog(ElaboraMAchievedPerImportGruppiCommerciali.class);
	
	@Inject
	public ElaboraMAchievedPerImportGruppiCommerciali(ImportController importController, DomainDrillerService domDrilSer) {
		this.importController = importController;
		this.domainDrillerService = domDrilSer;
		
	}
	
	public String importaGruppiCommerciali() throws Exception {
		StringBuffer result = new StringBuffer("\n");
		int counterCreati = 0;
		int counterAggiornati = 0;
		List<MAchieved> gruppiCommerciali = importController.listGruppiCommerciali(); // lista dei record di M_ACHIEVED
		
		for (MAchieved mAchieved : gruppiCommerciali) {
			InformationOwners toBeSaved = null;
			InformationOwners clientExecutive = importController.findInformationOwnerByEysCode(mAchieved.getIdAeCgruppo()); // cerco il ce su information owners
			if(null != clientExecutive) {
				InformationOwners gruppoCommerciale = importController.findInformationOwnerByEysCode(mAchieved.getCodiceCapogruppo());
				if(null != gruppoCommerciale) {
					// controlla/aggiorna
					if(gruppoCommerciale.getParentId() != clientExecutive.getId()) {
						gruppoCommerciale.setParentId(clientExecutive.getId());
						gruppoCommerciale.setDescription(mAchieved.getCapogruppo()+"-"+mAchieved.getCodiceCapogruppo()); // per completezza aggiorno la descrizione che potrebbe essere cambiata
						gruppoCommerciale.setCreated(new Date());
						toBeSaved = gruppoCommerciale;
						counterAggiornati++;
						result.append(String.format("\tAggiornato %s\n", gruppoCommerciale.getDescription()));
						
					}
				} 
//				else { I GRUPPI COMMERCIALI NON DEVONO ESSERE AGGIUNTI
//					// crea associa
//					InformationOwners nuovoGruppoCommerciale = new InformationOwners();
//					nuovoGruppoCommerciale.setCodEys(mAchieved.getCodiceCapogruppo());
//					nuovoGruppoCommerciale.setEnabled(true);
//					nuovoGruppoCommerciale.setDescription(mAchieved.getCapogruppo()+"-"+mAchieved.getCodiceCapogruppo());
//					nuovoGruppoCommerciale.setCreated(new Date());
//					nuovoGruppoCommerciale.setOwnerType(OwnerType.GRUPPO_COMMERCIALE.name());
//					nuovoGruppoCommerciale.setParentId(clientExecutive.getId());
//					toBeSaved = nuovoGruppoCommerciale;
//					counterCreati++;
//					result.append(String.format("\tCreato %s\n", nuovoGruppoCommerciale.getDescription()));
//					
//				}
			}
			if(null != toBeSaved) {
				importController.saveUpdate(toBeSaved);
				
			}
		}
		result.append(String.format("------------------\nCreati %s\n------------------\nAggiornati %s\n------------------\n", ""+counterCreati, ""+counterAggiornati));

		return result.toString();
	}

//	public String sincronizza() {
//		
//
//		
//		update INFORMATION_OWNERS set enabled = 0 where cod_eys not in (select Codice_Capogruppo from M_ACHIEVED)
//		and owner_type = 'GRUPPO_COMMERCIALE'
//		AND enabled = 1;
//
//		update INFORMATION_OWNERS  set enabled = 0
//		where cod_eys in (select Codice_Capogruppo from M_ACHIEVED where `ID_AE_CGruppo` = '')
//		and owner_type = 'GRUPPO_COMMERCIALE';
//
//		
//		return null;
//	}

}
