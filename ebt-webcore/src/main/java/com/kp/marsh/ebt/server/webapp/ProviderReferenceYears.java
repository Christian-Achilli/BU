package com.kp.marsh.ebt.server.webapp;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.kp.marsh.ebt.server.webapp.data.domain.ReferenceYears;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.DomainDrillerService;

/**
 * @author dariobrambilla
 * In posisiozne 0 l'anno corrente valido
 * In posizione 1 l'anno precedente valido
 * LA VALIDITA' E' DEFINITA SULLA TABELLA REFERENCE_YEARS
 */
public class ProviderReferenceYears implements Provider<ReferenceYears[]> {

	private DomainDrillerService domainDrillerService;

	@Inject
	public ProviderReferenceYears(DomainDrillerService dds) {
		this.domainDrillerService = dds;
	}

	@Override
	public ReferenceYears[] get() {
		ReferenceYears[] array = new ReferenceYears[2];
		array[0] = domainDrillerService.getEnabledReferenceYear(); 
		array[1] = domainDrillerService.findReferenceYearByNumber(array[0].getReferenceYear()-1); 
		return array;
	}
}
