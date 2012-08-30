package com.kp.malice.client.amministrazione;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.kp.malice.client.exception.AgentAlreadyExistException;
import com.kp.malice.shared.MaliceUtil;
import com.kp.malice.shared.proxies.AgencyProxy;
import com.kp.malice.shared.proxies.AgentProxy;

public class AgentForm extends Composite {

    interface Binder extends UiBinder<Widget, AgentForm> {
    }

    private static Binder uiBinder = GWT.create(Binder.class);

    private List<AgencyProxy> agencies;
    private AgentProxy agent;
    private List<String> roles;
    private IAmministrazioneViewDisplay.Listener listener;

    @UiField
    ListBox agencyInfoBox;
    @UiField
    Button createButton;
    @UiField
    Button deleteButton;
    @UiField
    TextBox firstNameBox;
    @UiField
    TextBox lastNameBox;
    @UiField
    TextBox passwordBox;
    @UiField
    ListBox roleBox;
    @UiField
    Button updateButton;
    @UiField
    TextBox usernameBox;
    @UiField
    TextBox ruiNumberBox;
    @UiField
    DateBox ruiAcceptanceDateBox;
    @UiField
    TextBox directNumberBox;
    @UiField
    TextBox mobileBox;
    @UiField
    TextBox emailBox;

    public AgentForm() {
        initWidget(uiBinder.createAndBindUi(this));

        //initialize date filed
        ruiAcceptanceDateBox.setFormat(new DateBox.DefaultFormat(MaliceUtil.getDayMonthYearFormat()));
        ruiAcceptanceDateBox.getElement().setAttribute("readonly", "readonly");

        // Initialize agent to null.
        setAgentProxy(null);

        //nascondi button
        deleteButton.setVisible(false);
        createButton.setVisible(false);
        updateButton.setVisible(false);

        // EVENTS
        updateButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (agent != null && checkMandatoryFields()) {
                    int agencyInfoIndex = agencyInfoBox.getSelectedIndex();
                    // if agency is changed then remove from agent
                    // list of that agency view (= remove from
                    // AgentDataProvider)
                    AgentProxy editableAgent = listener.prepareUpdateAgent(agent);
                    popolateEditableAgent(editableAgent);
                    try {
                        listener.fireUpdateAgentRequest(editableAgent, agent);
                    } catch (AgentAlreadyExistException e) {
//                        Window.alert("An agent with username " + e.getUsername()
//                                + " already exist. \nPlease change your username.");
                        MaliceUtil.showError("An agent with username " + e.getUsername()
                                + " already exist. \nPlease change your username");
                        
                        e.printStackTrace();
                    }
                    agent = editableAgent;
                    // Update the views.
                    listener.refreshDisplays();
                }
            }

        });
        deleteButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (agent != null) {
                    if (Window.confirm("Eliminare l'agente?")) {
                        listener.deleteAgent(agent);
                        // Update the views.
                        listener.refreshDisplays();
                    }
                }
            }
        });
        createButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (agent != null && checkMandatoryFields()) {
                    int agencyInfoIndex = agencyInfoBox.getSelectedIndex();
                    AgencyProxy agency = agencies.get(agencyInfoIndex);
                    AgentProxy newAgent = listener.prepareCreationAgent();
                    try {
                        popolateEditableAgent(newAgent);
                        //l'agenzia serve per ricaricare la lista degli agenti dell'agenzia in caso il nuovo agente appartenga a quella attualmente selezionata
                        listener.fireCreateAgentRequest(newAgent, agent.getAgency());
                    } catch (AgentAlreadyExistException e) {
//                        Window.alert("An agent with username " + e.getUsername()
//                                + " already exist. \nPlease change your username.");
                        MaliceUtil.showError("An agent with username " + e.getUsername()
                                + " already exist. \nPlease change your username");
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean checkMandatoryFields() {
        if (usernameBox.getText() == null || usernameBox.getText().equals("")) {
            // Window.alert("Username is mandatory.");
            MaliceUtil.showError("Username is mandatory");
            return false;
        }
        if (passwordBox.getText() == null || passwordBox.getText().equals("")) {
            // Window.alert("Password is mandatory.");
            MaliceUtil.showError("Password is mandatory");
            return false;
        }
        return true;
    }

    public void init() {
        // Add the roles to the role box.
        roles = listener.getRoles();
        roleBox.clear();
        for (String role : roles) {
            roleBox.addItem(role);
        }
        // Add the agencies to the agency box.
        agencies = listener.getAgencies();
        agencyInfoBox.clear();
        for (AgencyProxy agencyFromDatatProviderList : agencies) {
            agencyInfoBox.addItem(agencyFromDatatProviderList.getAge());
        }
    }

    /**
     * Popolo il proxy dell'agente per poi poterlo salvare
     * @param editableAgent proxy dell'agent (editabile)
     */
    private void popolateEditableAgent(AgentProxy editableAgent) {
        editableAgent.setUsername(usernameBox.getValue());
        editableAgent.setPassword(passwordBox.getValue());
        editableAgent.setName(firstNameBox.getText());
        editableAgent.setSurname(lastNameBox.getText());
        editableAgent.setRole(roles.get(roleBox.getSelectedIndex()));
        editableAgent.setAgency(agencies.get(agencyInfoBox.getSelectedIndex()));
        editableAgent.setDirectNumberBox(directNumberBox.getText());
        editableAgent.setEmailBox(emailBox.getText());
        editableAgent.setMobileBox(mobileBox.getText());
        editableAgent.setRuiAcceptanceDateBox(ruiAcceptanceDateBox.getValue());
        editableAgent.setRuiNumberBox(ruiNumberBox.getValue());
    }

    /**
     * Popolo i campi della form con gli attributi del proxy agente
     * @param agent proxy con il quale popolare la i campi della form
     */
    public void setAgentProxy(AgentProxy agent) {
        this.agent = agent;
        updateButton.setEnabled(agent != null);
        if (agent != null) {
            usernameBox.setText(agent.getUsername());
            passwordBox.setText(agent.getPassword());
            firstNameBox.setText(agent.getName());
            lastNameBox.setText(agent.getSurname());
            directNumberBox.setText(agent.getDirectNumberBox());
            emailBox.setText(agent.getEmailBox());
            mobileBox.setText(agent.getMobileBox());
            ruiNumberBox.setText(agent.getRuiNumberBox());
            ruiAcceptanceDateBox.setValue(agent.getRuiAcceptanceDateBox());

            // SETTO L'ATTIBUTO SELECTED CORRISPONDENTE NELLE DUE COMBO (RUOLO E AGENZIA)
            // role box (combo)
            String role = agent.getRole();
            List<String> roles = listener.getRoles();
            int i = 0;
            for (String roleFromList : roles) {
                if (roleFromList.equals(agent.getRole())) {
                    roleBox.setSelectedIndex(i);
                    break;
                }
                i++;
            }
            // agency box (combo)
            AgencyProxy agency = agent.getAgency();
            List<AgencyProxy> agencies = listener.getAgencies();
            int ii = 0;
            for (Iterator<AgencyProxy> iterator = agencies.iterator(); iterator.hasNext();) {
                AgencyProxy agencyFromQuery = iterator.next();
                if (agencyFromQuery.equals(agency)) {
                    agencyInfoBox.setSelectedIndex(ii);
                    break;
                }
                ii++;
            }

            //visualizza buttons
            deleteButton.setVisible(true);
            createButton.setVisible(true);
            updateButton.setVisible(true);
        }
    }

    public void setListener(IAmministrazioneViewDisplay.Listener listener) {
        this.listener = listener;
    }
}