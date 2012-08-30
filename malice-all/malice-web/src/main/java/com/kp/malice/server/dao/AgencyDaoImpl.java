package com.kp.malice.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gwt.user.client.Random;
import com.google.inject.Inject;
import com.kp.malice.server.model.Agency;
import com.kp.malice.server.model.Agent;
import com.kp.malice.shared.proxies.AgencyProxy;

public class AgencyDaoImpl implements AgencyDao {

    private static Logger log = Logger.getLogger(AgencyDaoImpl.class);

    public static List<Agency> agencies;

    @Override
    public List<Agency> loadAgencies() {
        agencies = new ArrayList<Agency>();
        String[] catNames = new String[] { "Friends", "Agents", "Businesses", "Coworkers", "Family" };
        for (int i = 0; i < catNames.length; i++) {
            Agency a = new Agency(catNames[i]);
            a.setMaliceCode("" + (long) (Math.random() * 100000));
            a.setLoydsCode("" + (long) (Math.random() * 100000));
            a.setRmaCode("" + (long) (Math.random() * 100000));
            a.setId((long) i);
            agencies.add(a);
        }
        return agencies;
    }

    @Override
    public Agency createAgency() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void persist(Agency agency) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAgency(Agency agency) {
        // TODO Auto-generated method stub

    }
}