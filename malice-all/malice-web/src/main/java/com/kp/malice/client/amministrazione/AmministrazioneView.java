package com.kp.malice.client.amministrazione;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.kp.malice.shared.proxies.AgencyProxy;
import com.kp.malice.shared.proxies.AgentProxy;

/**
* Composta dal un cell browser con alberatura a 2 livelli: agenzie (livello 1 = root), agenti (livello 2 = leaf); e da due form: agente e agenzia.
* @author dariobrambilla
*
*/
public final class AmministrazioneView extends Composite implements IAmministrazioneViewDisplay {
    private IAmministrazioneViewDisplay.Listener listener;
    private static Binder uiBinder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, AmministrazioneView> {
    }

    public static interface CwConstants extends Constants {
        String cwCellBrowserDescription();

        String cwCellBrowserName();
    }

    @UiField
    FlowPanel cellBrowserHandle;
    @UiField
    AgentForm agentForm;
    @UiField
    AgencyForm agencyForm;

    CellBrowser cellBrowser;

    private AgentTreeViewModel agentTreeViewModel;
    private final SingleSelectionModel<AgentProxy> selectionModelAgent;
    private final SingleSelectionModel<AgencyProxy> selectionModelAgency;

    public static final ProvidesKey<AgencyProxy> KEY_PROVIDER = new ProvidesKey<AgencyProxy>() {
        public Object getKey(AgencyProxy item) {
            return item == null ? null : item.getId();
        }
    };

    public AmministrazioneView() {
        //CREO I SELECTIONMODEL E QUINDI LA GESTIONE EVENTI PER LE DUE LISTE (AGENZIE, AGENTE)
        selectionModelAgent = new SingleSelectionModel<AgentProxy>(new ProvidesKey<AgentProxy>() {
            public Object getKey(AgentProxy item) {
                return item == null ? null : item.getId();
            }
        });
        selectionModelAgent.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            public void onSelectionChange(SelectionChangeEvent event) {
                AgentProxy selected = selectionModelAgent.getSelectedObject();
                agentForm.setAgentProxy(selected);
            }
        });

        selectionModelAgency = new SingleSelectionModel<AgencyProxy>(new ProvidesKey<AgencyProxy>() {
            public Object getKey(AgencyProxy item) {
                return item == null ? null : item.getId();
            }
        });
        selectionModelAgency.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            public void onSelectionChange(SelectionChangeEvent event) {
                AgencyProxy selected = selectionModelAgency.getSelectedObject();
                agencyForm.setAgencyProxy(selected);
            }
        });
        agentTreeViewModel = new AgentTreeViewModel();
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void initAllComponent() {
        initViewModel();
        initCellBrowser();
        initAgentProxyForm();
    }

    @Override
    public void initViewModel() {
        agentTreeViewModel.setSelectionModelAgent(selectionModelAgent);
        agentTreeViewModel.setSelectionModelAgency(selectionModelAgency);
    }

    @Override
    public void initCellBrowser() {
        cellBrowser = new CellBrowser(agentTreeViewModel, null);
        cellBrowser.setAnimationEnabled(true);
        cellBrowser.setWidth("518px");
        cellBrowser.setHeight("400px");
        cellBrowserHandle.add(cellBrowser);
    }

    @Override
    public void initAgentProxyForm() {
        agentForm.init();
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
        agentForm.setListener(listener);
        agencyForm.setListener(listener);
        agentTreeViewModel.setListener(listener);
    }

    @Override
    public void setAgencyProxy(AgencyProxy agency) {
        agencyForm.setAgencyProxy(agency);
    }

    @Override
    public void setAgentProxy(AgentProxy agent) {
        agentForm.setAgentProxy(agent);
    }

}