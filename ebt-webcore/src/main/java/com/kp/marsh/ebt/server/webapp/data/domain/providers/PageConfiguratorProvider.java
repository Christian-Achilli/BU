package com.kp.marsh.ebt.server.webapp.data.domain.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.kp.marsh.ebt.server.webapp.PageConfigurator;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.DomainDrillerService;

public class PageConfiguratorProvider implements Provider<PageConfigurator> {

	private DomainDrillerService domainDriller;
	
	
	 @Inject
	 public PageConfiguratorProvider(DomainDrillerService domainDriller) {
	        this.domainDriller = domainDriller;
	    }
	
	@Override
	public PageConfigurator get() {
		return new PageConfigurator(domainDriller);
	}

}
