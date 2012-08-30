/**
 * 
 */
package com.kp.malice.client.amministrazione;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.ListDataProvider;
import com.kp.malice.client.exception.AgencyAlreadyExistException;
import com.kp.malice.client.exception.AgentAlreadyExistException;
import com.kp.malice.shared.proxies.AgencyProxy;
import com.kp.malice.shared.proxies.AgentProxy;

public interface IAmministrazioneViewDisplay extends IsWidget {
    public interface Listener {
        void refreshDisplays();

        //AGENCY
        List<AgencyProxy> getAgencies();

        ListDataProvider<AgencyProxy> getAgenciesDataProvider();

        void deleteAgency(AgencyProxy agency);

        AgencyProxy prepareCreationAgency();

        void fireCreateAgencyRequest(AgencyProxy newAgency) throws AgencyAlreadyExistException;

        AgencyProxy prepareUpdateAgency(AgencyProxy agency);

        void fireUpdateAgencyRequest(AgencyProxy editableAgency, AgencyProxy agency) throws AgencyAlreadyExistException;

        void cleanAndPopulateAgentDataProvider(AgencyProxy agency);

        //AGENT
        List<String> getRoles();

        ListDataProvider<AgentProxy> getAgentsDataProvider();

        void deleteAgent(AgentProxy agent);

        AgentProxy prepareUpdateAgent(AgentProxy agent);

        void fireUpdateAgentRequest(AgentProxy agent, AgentProxy oldAgent) throws AgentAlreadyExistException;

        AgentProxy prepareCreationAgent();

        void fireCreateAgentRequest(AgentProxy agent, AgencyProxy agency) throws AgentAlreadyExistException;

        void closeAgency(AgencyProxy agency);

    }

    void initAgentProxyForm();

    void initAllComponent();

    void initCellBrowser();

    void initViewModel();

    void setListener(Listener listener);

    void setAgencyProxy(AgencyProxy agency);

    void setAgentProxy(AgentProxy agent);

}
