package com.kp.malice.client.amministrazione;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.DefaultSelectionEventManager.CheckboxEventTranslator;
import com.google.gwt.view.client.DefaultSelectionEventManager.EventTranslator;
import com.google.gwt.view.client.DefaultSelectionEventManager.SelectAction;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.kp.malice.client.ui.resources.MaliceResources;
import com.kp.malice.shared.proxies.AgencyProxy;
import com.kp.malice.shared.proxies.AgentProxy;

/**
 * The {@link TreeViewModel} used to organize agents into a hierarchy.
 */
public class AgentTreeViewModel implements TreeViewModel {

    private IAmministrazioneViewDisplay.Listener listener;
    private final Cell<AgentProxy> agentCell;
    private final Cell<AgencyProxy> agencyCell;
    
    private final DefaultSelectionEventManager<AgentProxy> selectionManagerAgent = DefaultSelectionEventManager
            .createCustomManager(new MyboxEventTranslator());
    private final DefaultSelectionEventManager<AgencyProxy> selectionManagerAgency = DefaultSelectionEventManager
            .createCustomManager(new MyboxEventTranslator());
    
    private SelectionModel<AgentProxy> selectionModelAgent;
    private SelectionModel<AgencyProxy> selectionModelAgency;
    
    public AgentTreeViewModel(){
        //CONTACT
        List<HasCell<AgentProxy, ?>> hasCells = new ArrayList<HasCell<AgentProxy, ?>>();
        hasCells.add(new HasCell<AgentProxy, AgentProxy>() {

            private AgentCell cell = new AgentCell(MaliceResources.INSTANCE.person());

            public Cell<AgentProxy> getCell() {
                return cell;
            }

            public FieldUpdater<AgentProxy, AgentProxy> getFieldUpdater() {
                return null;
            }

            public AgentProxy getValue(AgentProxy object) {
                return object;
            }

        });
        agentCell = new CompositeCell<AgentProxy>(hasCells) {
            @Override
            public void render(Context context, AgentProxy value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<table><tbody><tr>");
                super.render(context, value, sb);
                sb.appendHtmlConstant("</tr></tbody></table>");
            }

            @Override
            protected Element getContainerElement(Element parent) {
                // Return the first TR element in the table.
                return parent.getFirstChildElement().getFirstChildElement();
            }

            @Override
            protected <X> void render(Context context, AgentProxy value, SafeHtmlBuilder sb,
                    HasCell<AgentProxy, X> hasCell) {
                Cell<X> cell = hasCell.getCell();
                sb.appendHtmlConstant("<td>");
                cell.render(context, hasCell.getValue(value), sb);
                sb.appendHtmlConstant("</td>");
            }
        };
        //AGENCY
        List<HasCell<AgencyProxy, ?>> hasCellsAgency = new ArrayList<HasCell<AgencyProxy, ?>>();
        hasCellsAgency.add(new HasCell<AgencyProxy, AgencyProxy>() {
            
            private AgencyCell agencyCell = new AgencyCell(MaliceResources.INSTANCE.agency());
            
            public Cell<AgencyProxy> getCell() {
                return agencyCell;
            }
            
            public FieldUpdater<AgencyProxy, AgencyProxy> getFieldUpdater() {
                return null;
            }
            
            public AgencyProxy getValue(AgencyProxy object) {
                return object;
            }
            
        });
        agencyCell = new CompositeCell<AgencyProxy>(hasCellsAgency) {
            @Override
            public void render(Context context, AgencyProxy value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<table><tbody><tr>");
                super.render(context, value, sb);
                sb.appendHtmlConstant("</tr></tbody></table>");
            }
            
            @Override
            protected Element getContainerElement(Element parent) {
                // Return the first TR element in the table.
                return parent.getFirstChildElement().getFirstChildElement();
            }
            
            @Override
            protected <X> void render(Context context, AgencyProxy value, SafeHtmlBuilder sb,
                    HasCell<AgencyProxy, X> hasCellsAgency) {
                Cell<X> cell = hasCellsAgency.getCell();
                sb.appendHtmlConstant("<td>");
                cell.render(context, hasCellsAgency.getValue(value), sb);
                sb.appendHtmlConstant("</td>");
            }
        };
        
    }
    
    public static class MyboxEventTranslator<T> implements EventTranslator<T> {

        /**
         * The column index. Other columns are ignored.
         */
        private final int column;

        /**
         * Construct a new {@link CheckboxEventTranslator} 
         */
        public MyboxEventTranslator() {
            this(-1);
        }
        public MyboxEventTranslator(int column) {
            this.column = column;
        }

        public boolean clearCurrentSelection(CellPreviewEvent<T> event) {
            return false;
        }

        public SelectAction translateSelectionEvent(CellPreviewEvent<T> event) {
            // Handle the event.
            NativeEvent nativeEvent = event.getNativeEvent();
            if ("click".equals(nativeEvent.getType())) {
                // Ignore if the event didn't occur in the correct column.
                if (column > -1 && column != event.getColumn()) {
                    return SelectAction.IGNORE;
                }
                return SelectAction.TOGGLE;
            }
            // For keyboard events, do the default action.
            return SelectAction.DEFAULT;
        }
    }

    /**
     * The cell used to render categories.
     */
    private static class AgencyCell extends AbstractCell<AgencyProxy> {
    
        /**
         * The html of the image used for agents.
         */
        private final String imageHtml;
    
        public AgencyCell(ImageResource image) {
            this.imageHtml = AbstractImagePrototype.create(image).getHTML();
        }
    
        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context, AgencyProxy value, SafeHtmlBuilder sb) {
            if (value != null) {
                sb.appendHtmlConstant(imageHtml).appendEscaped(" ");
                sb.appendEscaped(value.getAge());
            }
        }
    }

    private static class AgentCell extends AbstractCell<AgentProxy> {
        public AgentCell(ImageResource image) {
            super("click", "keydown");
            this.imageHtml = AbstractImagePrototype.create(image).getHTML();
        }

        /**
         * The html of the image used for agents.
         */
        private final String imageHtml;

        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context, AgentProxy value, SafeHtmlBuilder sb) {
            // Value can be null, so do a null check..
            if (value == null) {
                return;
            }

            sb.appendHtmlConstant("<table>");

            // Add the agent image.
            sb.appendHtmlConstant("<tr><td rowspan='3'>");
            sb.appendHtmlConstant(imageHtml);
            sb.appendHtmlConstant("</td>");

            // Add the name and address.
            sb.appendHtmlConstant("<td style='font-size:95%;'>");
            sb.appendEscaped(value.getSurname()+" "+value.getName());
            sb.appendHtmlConstant("</td></tr><tr><td>");
            sb.appendEscaped(value.getRole());
            sb.appendHtmlConstant("</td></tr></table>");
        }
    }

    @Override
    public <T> NodeInfo<?> getNodeInfo(T value) {
        if (value == null) {
            ListDataProvider<AgencyProxy> listDataProvider = listener.getAgenciesDataProvider();
            return new DefaultNodeInfo<AgencyProxy>(listDataProvider, agencyCell, selectionModelAgency, selectionManagerAgency, null);
        } else if (value instanceof AgencyProxy) {
            AgencyProxy agencyInfo = (AgencyProxy) value;
            listener.cleanAndPopulateAgentDataProvider(agencyInfo);
            return new DefaultNodeInfo<AgentProxy>(listener.getAgentsDataProvider(), agentCell, selectionModelAgent, selectionManagerAgent, null);
        }
    
        // Unhandled type.
        String type = value.getClass().getName();
        throw new IllegalArgumentException("Unsupported object type: " + type);
    }

    public void setSelectionModelAgent(SingleSelectionModel<AgentProxy> selectionModelAgent) {
        this.selectionModelAgent = selectionModelAgent;        
    }

    public void setSelectionModelAgency(SingleSelectionModel<AgencyProxy> selectionModelAgency) {
        this.selectionModelAgency = selectionModelAgency;
    }

    @Override
    public boolean isLeaf(Object value) {
        return value instanceof AgentProxy;
    }

    public void setListener(IAmministrazioneViewDisplay.Listener listener) {
        this.listener = listener;
    }
}