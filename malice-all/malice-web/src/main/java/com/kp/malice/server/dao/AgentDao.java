package com.kp.malice.server.dao;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.kp.malice.server.model.Agent;
import com.kp.malice.shared.proxies.AgentProxy;

public interface AgentDao {
	
	/**
	 * @return lista di tutti gli agenti
	 */
	public List<Agent> loadAgents();
    public Agent createAgent();
    public void deleteAgent(Agent agent);
    public void persist(Agent agent);
}
