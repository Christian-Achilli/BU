package com.kp.malice.client.amministrazione;

/**
 * Gestioni degli utenti e delle agenzie per l'amministrazione (cell browser agenzie/agente, form agente e agenzia )
 * @author dariobrambilla
 *
 */
public class AmministrazioneActivity {
    //extends AbstractActivity implements IAmministrazioneViewDisplay.Listener {
    //    //CONTEXT
    //    private final Provider<AgencyRequest> agencyReqContextProvider;
    //    private final Provider<AgentRequest> agentReqContextProvider;
    //    //REQUEST
    //    private AgentRequest requestAgent;
    //    private AgencyRequest requestAgency;
    //    //DATA PROVIDER (what is visualized on page: list of agencies and list of agent from selected agency)
    //    private ListDataProvider<AgencyProxy> agenciesDataProvider = new ListDataProvider<AgencyProxy>();
    //    private ListDataProvider<AgentProxy> agentsDataProvider = new ListDataProvider<AgentProxy>();
    //
    //    /**
    //     * The list of all agents (not visualized, only the filtered list in
    //     * dataProvider is visualized)
    //     */
    //    private List<AgentProxy> agents = new ArrayList<AgentProxy>();
    //    private final IAmministrazioneViewDisplay display;
    //    private final EventBus eventBus;
    //    private AmministratorePlace place;
    //
    //    @Inject
    //    public AmministrazioneActivity(IAmministrazioneViewDisplay display, EventBus eventBus,
    //            Provider<AgentRequest> agentReqContextProvider, Provider<AgencyRequest> agencyReqContextProvider) {
    //        this.display = display;
    //        this.eventBus = eventBus;
    //        this.agentReqContextProvider = agentReqContextProvider;
    //        this.agencyReqContextProvider = agencyReqContextProvider;
    //        display.setListener(this);
    //    }
    //
    //    public void refreshDisplays() {
    //        agentsDataProvider.refresh();
    //        agenciesDataProvider.refresh();
    //    }
    //
    //    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    //        // AGENCY RFC
    //        AgencyRequest requestAgency = agencyReqContextProvider.get();
    //        Request<List<AgencyProxy>> reqAgency = requestAgency.loadAgencies();
    //        try {
    //            reqAgency.fire(new Receiver<List<AgencyProxy>>() {
    //                @Override
    //                public void onFailure(ServerFailure error) {
    //                    GWT.log("loadAgencies FAILED!!!! -->" + error.getMessage());
    //                }
    //
    //                @Override
    //                public void onSuccess(List<AgencyProxy> agenciesFromRPC) {
    //                    GWT.log("loadAgencies request factory succeded! agenciesFromRPC size: " + agenciesFromRPC.size());
    //                    GWT.log("populating agency data provider with agencies loaded by rfc");
    //                    getAgencies().addAll(agenciesFromRPC);
    //                    sortAgenciesDataProviderByDescription();
    //
    //                    // AGENT RFC (all agencies are loaded, so now can load all agent )
    //                    AgentRequest requestAgent = agentReqContextProvider.get();
    //                    Request<List<AgentProxy>> reqAgent = requestAgent.loadAgents();
    //                    try {
    //                        reqAgent.fire(new Receiver<List<AgentProxy>>() {
    //                            @Override
    //                            public void onFailure(ServerFailure error) {
    //                                GWT.log("loadAgents FAILED!!!! -->" + error.getMessage());
    //                            }
    //
    //                            @Override
    //                            public void onSuccess(List<AgentProxy> agentsFromRFC) {
    //                                GWT.log("loadAgents request factory succeded!!!!");
    //                                GWT.log("populating agent data provider with agents loaded by rfc");
    //                                agentsDataProvider.getList().addAll(agentsFromRFC);
    //                                GWT.log("populating agent list with agents loaded by rfc");
    //                                agents.addAll(agentsFromRFC);
    //                                sortDataProviderAgentsByUsername();
    //                                display.initAllComponent();
    //                                panel.setWidget(display.asWidget());
    //                            }
    //                        });
    //                    } catch (Exception e) {
    //                        GWT.log("requestFactory loadAgents GwtSecurityException: " + e.getMessage());
    //                        e.printStackTrace();
    //                    }
    //                    eventBus.fireEvent(new AppLoadingCompleteEvent(place));
    //                }
    //
    //            });
    //        } catch (Exception e) {
    //            GWT.log("requestFactory loadAgencies GwtSecurityException: " + e.getMessage());
    //            e.printStackTrace();
    //        }
    //    }
    //
    //    public AmministrazioneActivity withPlace(final Place place) {
    //        this.place = (AmministratorePlace) place;
    //        return this;
    //    }
    //
    //    //***************************START AGENT ****************************
    //    /**
    //     * Ordina gli agenti del dataProvider per username
    //     */
    //    private void sortDataProviderAgentsByUsername() {
    //        GWT.log("ordino la lista agenti del data provider per username");
    //        Collections.sort(getAgentsDataProvider().getList(), new Comparator<AgentProxy>() {
    //
    //            @Override
    //            public int compare(AgentProxy o1, AgentProxy o2) {
    //                return (int) (o1.getSurname().toLowerCase().compareTo(o2.getSurname().toLowerCase()));
    //            }
    //
    //        });
    //        Collections.sort(agents, new Comparator<AgentProxy>() {
    //
    //            @Override
    //            public int compare(AgentProxy o1, AgentProxy o2) {
    //                return (int) (o1.getSurname().toLowerCase().compareTo(o2.getSurname().toLowerCase()));
    //            }
    //
    //        });
    //    }
    //
    //    /**
    //     * Aggiunge l'agente alla lista agenti del data provider (e ordina per username) e alla lista agents 
    //     * @param agent agente da aggiungere
    //     * @param agency agenzia selezionata
    //     */
    //    private void addAgentToDataProviderAndAgentsList(final AgentProxy agent, final AgencyProxy agency) {
    //        GWT.log("aggiungo alla lista degli agenti il nuovo");
    //        agents.add(agent);
    //        GWT.log("aggiorno la lista del dataProvider con gli agenti dell'agenzia che è selezionata, così se il nuovo agente è di questa agenzia lo visualizzo subito");
    //        cleanAndPopulateAgentDataProvider(agency);
    //        sortDataProviderAgentsByUsername();
    //        //il proxy precedente non esiste + pertanto aggiorno il reference del form con il nuovo
    //        display.setAgentProxy(agent);
    //    }
    //
    //    /**
    //     * Query all agents for the specified agencyInfo that begin with the
    //     * specified first name prefix.
    //     * 
    //     * @param agency
    //     *            the agencyInfo
    //     * @param namePrefix
    //     *            the prefix of the first name
    //     * @return the list of agents in the agencyInfo
    //     */
    //    public List<AgentProxy> queryAgentsByagencyInfoAndFirstName(AgencyProxy agency, String namePrefix) {
    //        List<AgentProxy> matches = new ArrayList<AgentProxy>();
    //        for (AgentProxy agent : agentsDataProvider.getList()) {
    //            if (agent.getAgency() == agency && agent.getName().startsWith(namePrefix)) {
    //                matches.add(agent);
    //            }
    //        }
    //        return matches;
    //    }
    //
    //    private void deleteAgentFromProxyLists(AgentProxy agent) {
    //        // from agents
    //        boolean removedAgents = agents.remove(agent);
    //        if (removedAgents)
    //            GWT.log("AgentProxy rimosso dalla lista agents.");
    //        else
    //            GWT.log("AgentProxy NON rimosso dalla lista agents.");
    //        // from agentDataProvider
    //        List<AgentProxy> agentsDataProviderList = agentsDataProvider.getList();
    //        boolean removedFromDataProvider = agentsDataProviderList.remove(agent);
    //        if (removedFromDataProvider)
    //            GWT.log("AgentProxy rimosso dalla lista del dataProvider.");
    //        else
    //            GWT.log("AgentProxy NON rimosso dalla lista del dataProvider.");
    //    }
    //
    //    private AgentProxy getAgentByUsername(String username) {
    //        for (AgentProxy agentInfo : agents) {
    //            if (username.equalsIgnoreCase(agentInfo.getUsername()))
    //                return agentInfo;
    //        }
    //        return null;
    //    }
    //
    //    /**
    //     * Query all agents for the specified agencyInfo.
    //     * 
    //     * @param agency
    //     *            the agencyInfo
    //     * @return the list of agents in the agencyInfo
    //     */
    //    public List<AgentProxy> queryAgentsByAgencyProxy(AgencyProxy agency) {
    //        List<AgentProxy> matches = new ArrayList<AgentProxy>();
    //        for (AgentProxy agent : agents) {
    //            if (agent.getAgency().getId() == agency.getId()) {
    //                matches.add(agent);
    //            }
    //        }
    //        return matches;
    //    }
    //
    //    //================LISTENER=================
    //
    //    /**
    //     * Clean agentsDataProvider list and add all the element from agentsSelected
    //     * 
    //     * @param agentsSelected
    //     *            the agent list to add
    //     */
    //    @Override
    //    public void cleanAndPopulateAgentDataProvider(AgencyProxy agencyInfo) {
    //        List<AgentProxy> agentsSelectedByAgency = queryAgentsByAgencyProxy(agencyInfo);
    //        List<AgentProxy> listAgentFromDataProvider = agentsDataProvider.getList();
    //        listAgentFromDataProvider.clear();
    //        for (Iterator iterator = agentsSelectedByAgency.iterator(); iterator.hasNext();) {
    //            AgentProxy agentInfo = (AgentProxy) iterator.next();
    //            listAgentFromDataProvider.add(agentInfo);
    //        }
    //    }
    //
    //    @Override
    //    public ListDataProvider<AgentProxy> getAgentsDataProvider() {
    //        return agentsDataProvider;
    //    }
    //
    //    @Override
    //    public List<String> getRoles() {
    //        List<String> roles = Arrays.asList("ROLE_ADMIN", "ROLE_USER", "ROLE_AGENTE");
    //        return roles;
    //    }
    //
    //    //-----------------CREATION----------------
    //    @Override
    //    public AgentProxy prepareCreationAgent() {
    //        requestAgent = agentReqContextProvider.get();
    //        return requestAgent.create(AgentProxy.class);
    //    }
    //
    //    @Override
    //    public void fireCreateAgentRequest(final AgentProxy agent, final AgencyProxy agency)
    //            throws AgentAlreadyExistException {
    //        if (requestAgent != null) {
    //            String username = agent.getUsername();
    //            AgentProxy alreadyExistingAgent = getAgentByUsername(username);
    //            if (null == alreadyExistingAgent) {
    //                try {
    //                    requestAgent.fire(new Receiver<Void>() {
    //                        @Override
    //                        public void onFailure(ServerFailure error) {
    //                            GWT.log("fireCreateAgentRequest FAILED!!!! -->" + error.getMessage());
    //                        }
    //
    //                        @Override
    //                        public void onSuccess(Void response) {
    //                            GWT.log("fireCreateAgentRequest request factory succeded!!!!");
    //                            addAgentToDataProviderAndAgentsList(agent, agency);
    //                        }
    //                    });
    //                } catch (Exception e) {
    //                    GWT.log("requestFactory crate GwtSecurityException: " + e.getMessage());
    //                    e.printStackTrace();
    //                } finally {
    //                    requestAgent = null;
    //                }
    //            } else
    //                throw new AgentAlreadyExistException(username);
    //        } else {
    //            GWT.log("ERROR!!! requestAgent IS NULL");
    //        }
    //    }
    //
    //    //-----------------UPDATE----------------
    //    @Override
    //    public AgentProxy prepareUpdateAgent(final AgentProxy agent) {
    //        requestAgent = agentReqContextProvider.get();
    //        AgentProxy agentEditable = requestAgent.edit(agent);
    //        requestAgent.persist(agentEditable);
    //        return agentEditable;
    //    }
    //
    //    @Override
    //    public void fireUpdateAgentRequest(final AgentProxy agent, final AgentProxy oldAgent)
    //            throws AgentAlreadyExistException {
    //        if (requestAgent != null) {
    //            String username = agent.getUsername();
    //            AgentProxy alreadyExistingAgent = getAgentByUsername(username);
    //            if (null == alreadyExistingAgent) {
    //                try {
    //                    requestAgent.fire(new Receiver<Void>() {
    //                        @Override
    //                        public void onFailure(ServerFailure error) {
    //                            GWT.log("fireAgentRequest FAILED!!!! -->" + error.getMessage());
    //                        }
    //
    //                        @Override
    //                        public void onSuccess(Void response) {
    //                            GWT.log("fireAgentRequest request factory succeded!!!!");
    //                            //elimino dalle liste il vecchio agent
    //                            deleteAgentFromProxyLists(oldAgent);
    //                            addAgentToDataProviderAndAgentsList(agent, oldAgent.getAgency());
    //                        }
    //                    });
    //                } catch (Exception e) {
    //                    GWT.log("requestFactory addAgent GwtSecurityException: " + e.getMessage());
    //                    e.printStackTrace();
    //                } finally {
    //                    requestAgent = null;
    //                }
    //            } else
    //                throw new AgentAlreadyExistException(username);
    //        } else {
    //            GWT.log("ERROR!!! requestAgent IS NULL");
    //        }
    //    }
    //
    //    //-----------------DELETE----------------
    //    @Override
    //    public void deleteAgent(final AgentProxy agent) {
    //        AgentRequest requestAgent = agentReqContextProvider.get();
    //        requestAgent.deleteAgent(agent);
    //        try {
    //            requestAgent.fire(new Receiver<Void>() {
    //                @Override
    //                public void onFailure(ServerFailure error) {
    //                    GWT.log("deleteAgent FAILED!!!! -->" + error.getMessage());
    //                }
    //
    //                @Override
    //                public void onSuccess(Void response) {
    //                    GWT.log("deleteAgent request factory succeded!!!!");
    //                    //elimino dalle liste il vecchio agent
    //                    deleteAgentFromProxyLists(agent);
    //                    display.setAgentProxy(null);
    //                }
    //            });
    //        } catch (Exception e) {
    //            GWT.log("requestFactory addAgent GwtSecurityException: " + e.getMessage());
    //            e.printStackTrace();
    //        }
    //    }
    //
    //    //*******************AGENCY***********************
    //    /**
    //     * Ordina le agenzie del dataProvider per descrizione
    //     */
    //    private void sortAgenciesDataProviderByDescription() {
    //        Collections.sort(getAgencies(), new Comparator<AgencyProxy>() {
    //
    //            @Override
    //            public int compare(AgencyProxy o1, AgencyProxy o2) {
    //                return (int) (o1.getAge().toLowerCase().compareTo(o2.getAge().toLowerCase()));
    //            }
    //
    //        });
    //    }
    //
    //    /**
    //     * Se l'agenzia è presente nel data provider delle agenzie allora lancia un'eccezione
    //     * @param agency agenzia di cui si verifica la presenza nel data provider
    //     * @throws AgencyAlreadyExistException eccezione lanciata
    //     */
    //    private void checkAgencyExisting(AgencyProxy agency) throws AgencyAlreadyExistException {
    //        for (AgencyProxy agencyTmp : getAgencies()) {
    //            if (agency.getMaliceCode().equals(agencyTmp.getMaliceCode())
    //                    || agency.getLoydsCode().equals(agencyTmp.getLoydsCode())
    //                    || agency.getRmaCode().equals(agencyTmp.getRmaCode())) {
    //                if (agency.getId() != agencyTmp.getId())
    //                    throw new AgencyAlreadyExistException(agency.getMaliceCode(), agency.getLoydsCode(),
    //                            agency.getRmaCode());
    //            }
    //        }
    //    }
    //
    //    /**
    //     * @param agency
    //     */
    //    private void addAgency(final AgencyProxy agency) {
    //        //aggiungo alla lista delle agenzie nel data provider la nuova
    //        getAgencies().add(agency);
    //        //riordino la lista delle agenzie
    //        sortAgenciesDataProviderByDescription();
    //        //il proxy precedente non esiste + pertanto aggiorno il reference del form con il nuovo
    //        display.setAgencyProxy(agency);
    //        //reinizializo la combo delle agenzie per gli agenti così che contenga anche quella nuova
    //        display.initAgentProxyForm();
    //    }
    //
    //    //=================LISTENER================
    //    /**
    //     * Ritona la lista delle agenzie del data provider
    //     * 
    //     * @return the list of agencies
    //     */
    //    public List<AgencyProxy> getAgencies() {
    //        return agenciesDataProvider.getList();
    //    }
    //
    //    public ListDataProvider<AgencyProxy> getAgenciesDataProvider() {
    //        return agenciesDataProvider;
    //    }
    //
    //    //------------------------DELETE--------------------------
    //    /**
    //     * Delete the agency from agenciesDataProvider
    //     * @param agency the agency to delete
    //     */
    //    public void deleteAgency(final AgencyProxy agency) {
    //        AgencyRequest requestAgency = agencyReqContextProvider.get();
    //        try {
    //            requestAgency.deleteAgency(agency).fire(new Receiver<Void>() {
    //                @Override
    //                public void onFailure(ServerFailure error) {
    //                    GWT.log("deleteAgency request factory FAILED!!!! -->" + error.getMessage());
    //                }
    //
    //                @Override
    //                public void onSuccess(Void response) {
    //                    GWT.log("deleteAgency request factory succeded!!!!");
    //                    // from agencyDataProvider
    //                    List<AgencyProxy> agenciesDataProviderList = agenciesDataProvider.getList();
    //                    boolean removedFromDataProvider = agenciesDataProviderList.remove(agency);
    //                    if (removedFromDataProvider) {
    //                        GWT.log("AgencyProxy rimosso dalla lista del agenciesDataProvider.");
    //                        display.initAgentProxyForm(); // altrimenti la combo dell'agente non
    //                        display.setAgencyProxy(null);
    //                        // viene aggiornata
    //                        refreshDisplays();
    //                    } else
    //                        GWT.log("AgencyProxy NON rimosso dalla lista del agenciesDataProvider.");
    //                }
    //            });
    //        } catch (Exception e) {
    //            GWT.log("requestFactory addAgency GwtSecurityException: " + e.getMessage());
    //            e.printStackTrace();
    //        }
    //    }
    //
    //    //------------------------CREATE--------------------------
    //    @Override
    //    public AgencyProxy prepareCreationAgency() {
    //        requestAgency = agencyReqContextProvider.get();
    //        return requestAgency.create(AgencyProxy.class);
    //    }
    //
    //    @Override
    //    public void fireCreateAgencyRequest(final AgencyProxy agency) throws AgencyAlreadyExistException {
    //        if (requestAgency != null) {
    //            checkAgencyExisting(agency); //i may throw AgencyAlreadyExistException
    //
    //            try {
    //                requestAgency.fire(new Receiver<Void>() {
    //                    @Override
    //                    public void onFailure(ServerFailure error) {
    //                        GWT.log("fireCreateAgencyRequest FAILED!!!! -->" + error.getMessage());
    //                    }
    //
    //                    @Override
    //                    public void onSuccess(Void response) {
    //                        GWT.log("fireCreateAgencyRequest request factory succeded!!!!");
    //                        addAgency(agency);
    //                    }
    //                });
    //            } catch (Exception e) {
    //                GWT.log("fireCreateAgencyRequest GwtSecurityException: " + e.getMessage());
    //                e.printStackTrace();
    //            } finally {
    //                requestAgency = null;
    //            }
    //        } else {
    //            GWT.log("ERROR!!! fireCreateAgencyRequest IS NULL");
    //        }
    //    }
    //
    //    //------------------------UPDATE--------------------------
    //    @Override
    //    public AgencyProxy prepareUpdateAgency(AgencyProxy agency) {
    //        requestAgency = agencyReqContextProvider.get();
    //        AgencyProxy agencyEditable = requestAgency.edit(agency);
    //        requestAgency.persist(agencyEditable);
    //        return agencyEditable;
    //    }
    //
    //    @Override
    //    public void fireUpdateAgencyRequest(final AgencyProxy editableAgency, final AgencyProxy oldAgency)
    //            throws AgencyAlreadyExistException {
    //        if (requestAgency != null) {
    //            checkAgencyExisting(editableAgency);
    //            try {
    //                requestAgency.fire(new Receiver<Void>() {
    //                    @Override
    //                    public void onFailure(ServerFailure error) {
    //                        GWT.log("fireUpdateAgencyRequest FAILED!!!! -->" + error.getMessage());
    //                    }
    //
    //                    @Override
    //                    public void onSuccess(Void response) {
    //                        GWT.log("fireUpdateAgencyRequest request factory succeded!!!!");
    //                        //elimino dalla lista del data provider delle agenzie la vecchia agenzia
    //                        if (!getAgencies().remove(oldAgency))
    //                            GWT.log("ERROR: oldAgency with description " + oldAgency.getAge() + " not removed");
    //                        addAgency(editableAgency);
    //                    }
    //                });
    //            } catch (Exception e) {
    //                GWT.log("fireUpdateAgencyRequest GwtSecurityException: " + e.getMessage());
    //                e.printStackTrace();
    //            } finally {
    //                requestAgency = null;
    //            }
    //        } else {
    //            GWT.log("ERROR!!! requestAgency IS NULL");
    //        }
    //    }
    //
    //    @Override
    //    public void closeAgency(AgencyProxy agency) {
    //
    //    }
}