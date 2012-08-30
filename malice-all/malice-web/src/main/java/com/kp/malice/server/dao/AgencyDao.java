package com.kp.malice.server.dao;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.kp.malice.server.model.Agency;
import com.kp.malice.server.model.Agent;
import com.kp.malice.shared.proxies.AgencyProxy;

public interface AgencyDao {

	/**
	 * @return lista di tutti gli agenti
	 */
	public List<Agency> loadAgencies();
    public Agency createAgency();
    public void persist(Agency agency);
    public void deleteAgency(Agency agency);
}
