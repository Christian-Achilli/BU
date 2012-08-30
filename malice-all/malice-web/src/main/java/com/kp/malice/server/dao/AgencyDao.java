package com.kp.malice.server.dao;

import java.util.List;

import com.kp.malice.entities.business.Agency;

public interface AgencyDao {

	/**
	 * @return lista di tutti gli agenti
	 */
	public List<Agency> loadAgencies();
    public Agency createAgency();
    public void persist(Agency agency);
    public void deleteAgency(Agency agency);
}
