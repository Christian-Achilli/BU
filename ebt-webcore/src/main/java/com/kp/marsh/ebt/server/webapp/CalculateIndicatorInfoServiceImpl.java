package com.kp.marsh.ebt.server.webapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kp.marsh.ebt.client.webapp.CalculateIndicatorInfoService;
import com.kp.marsh.ebt.server.webapp.data.domain.Products;
import com.kp.marsh.ebt.server.webapp.data.domain.ReferenceYears;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.CalculationService;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.DomainDrillerService;
import com.kp.marsh.ebt.shared.dto.SintesiDto;

/**
 * Calcola gli indici necessari per la costruzione dei gauges
 * @author dariobrambilla 
 * @author christianachilli
 */
@Singleton
public class CalculateIndicatorInfoServiceImpl extends GuiceRemoteServiceServlet implements CalculateIndicatorInfoService {
	private static Logger  log = Logger.getLogger(CalculateIndicatorInfoServiceImpl.class);

	private DomainDrillerService domainDrillerService;
	
	private CalculationService calculationService;
	
	private ReferenceYears[] yearArray;
	
	//questi due indici posso essere intesi come indici a livello nazionale se al metodo calculate passo l'id di un IO che abbia come padre supremo la nazione
//	private int indicePenetrazioneTotale;
//	private int indiceSaturazionePotenzialeTotale;

	@Inject
	public CalculateIndicatorInfoServiceImpl(DomainDrillerService domainDrillerService, CalculationService calService, ReferenceYears[] yearArray) {
		this.domainDrillerService = domainDrillerService;
		this.calculationService = calService;
		this.yearArray = yearArray;
	}

	
	/**
	 * @param id id dell'owner del quale vengono considerati tutti i gruppi commerciali che gli afferiscono
	 * @return la lista degli indici di penetrazione divisi per lob
	 */
	public ArrayList<SintesiDto> calcolaIndiciByOwnerId(int id) {
		ArrayList<SintesiDto> result = new ArrayList<SintesiDto>();
		
		List<Integer> iOGruppoCommerciale = domainDrillerService.findGruppoCommerciale(id); // la lista di tutti i gruppi commerciali che afferiscono all'owner con id in argomento
		List<Products> lobs = domainDrillerService.getAvailableLOB(); // la lista di tutte le lob e i relativi prodotti
		int referenceThisYearId = yearArray[0].getId();
		int referenceLastYearId = yearArray[1].getId();
		
		int idNazione = calculationService.getIdIOAntenatoFromIdIODiscendente(id);
		List<Integer> iOGruppoCommercialeNazione = domainDrillerService.findGruppoCommerciale(idNazione);
		
		log.debug("calcolaIndiciByOwnerId: Ownerid: "+id);
		if(iOGruppoCommerciale!=null && !iOGruppoCommerciale.isEmpty()) {
			for (Products lob : lobs) { //itero sulle LOB
				List<Products> products = domainDrillerService.getAvailableProductsByLOB(lob);
				log.debug("\tLOB: "+lob.getItemName());
				SintesiDto lobSintesiDto = new SintesiDto();
				lobSintesiDto.setLobName(lob.getItemName());

				// CALCOLO INDICE DI PENETRAZIONE
				calculationService.calcolaIndiceDiPenetrazione(iOGruppoCommerciale, iOGruppoCommercialeNazione, products, referenceThisYearId, referenceLastYearId, lobSintesiDto);
				
				// CALCOLO INDICE DI SATURAZIONE DEL POTENZIALE
				calculationService.calcolaIndiceDiSaturazioneDelPotenziale(iOGruppoCommerciale, iOGruppoCommercialeNazione, referenceThisYearId, referenceLastYearId, products, lobSintesiDto);
				
				result.add(lobSintesiDto);
			}
		}
		return result;
	}
}
