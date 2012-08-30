package com.kp.marsh.ebt.client.webapp.ui.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.examples.Fade;
import org.adamtacy.client.ui.effects.examples.Show;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.kp.marsh.ebt.client.webapp.ui.InsertPopUp;
import com.kp.marsh.ebt.client.webapp.ui.LoaderWidget;
import com.kp.marsh.ebt.client.webapp.ui.ProductPillWidget;
import com.kp.marsh.ebt.client.webapp.ui.ProductPopUp;
import com.kp.marsh.ebt.client.webapp.ui.RowElementsTable;
import com.kp.marsh.ebt.client.webapp.ui.activityintf.IInsertPotentialViewDisplay;
import com.kp.marsh.ebt.client.webapp.ui.resources.Resources;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;
import com.kp.marsh.ebt.shared.dto.AchievedInfoDTO;
import com.kp.marsh.ebt.shared.dto.LineOfBusiness;
import com.kp.marsh.ebt.shared.dto.MarshClientDTO;
import com.kp.marsh.ebt.shared.dto.ProductInfoDTO;
import com.kp.marsh.ebt.shared.dto.ProductTotalsManager;
import com.kp.marsh.ebt.shared.dto.RowTotalsManager;
import com.kp.marsh.ebt.shared.dto.TotalsManager;

public class InsertPotentialViewImpl extends Composite implements IInsertPotentialViewDisplay {

    private static InsertPotentialViewImplUiBinder uiBinder = GWT.create(InsertPotentialViewImplUiBinder.class);

    interface InsertPotentialViewImplUiBinder extends UiBinder<Widget, InsertPotentialViewImpl> {
    }

    @UiField
    DivElement headlinesTableDiv; // titoli delle lob

    @UiField
    DivElement leftArrow;

    @UiField
    DivElement rightArrow;

    @UiField
    DivElement appWrapper;

    @UiField
    Anchor leftPagingLabel;

    @UiField
    Anchor rightPagingLabel;

    @UiField
    Image rightPagingArrow;

    @UiField
    Image leftPagingArrow;

    @UiField
    VerticalPanel rootElement;

    //	ArrayList<VerticalPanel> tabList;

    @UiField
    LoaderWidget loaderWidget;

    private IInsertPotentialViewDisplay.Listener listener;

    private List<List<RowElementsTable>> rowElementsByPageList; // lists the row elements on each page. Used to handle the scroll function.

    /**
     * When a potential is changed, by this map I can update the row totals using the client as key
     */
    private Map<MarshClientDTO, List<RowElementsTable>> rowElementsByClientMap; // per ogni cliente ho la lista dei suoi elementi di riga, uno presente su ciascuna pagina

    private static int MAX_COL_NUMBER = 8; // NUMBER OF COLUMNS OF THE VISIBLE TABLE

    private static int SPACE_WIDTH = 75; // WIDTH IN PX OF A SINGLE COLUMN BELONGING THE TABLE

    private static int DOTS_WIDTH = 2; // width in px of the headline delimiters

    /**
     * List that contains the tables with line of business name. Such list is created in order that any table can be displayed on one page. By clicking left and right side arrows this list is navigated.
     * By clicking such arrows, the corresponding table replace the table in TotalsHeadlineViewImpl <code>headlinesTableDiv</code> member.
     */
    ArrayList<TableElement> headlineTablesList;

    private int showntableIndex = 0;

    private int showingPage = 1;// the page being visualized. Changed through the horizontal scroll.

    private InsertPopUp insertPopUp;

    private ProductPopUp productPopUp;

    private Show showEffect;

    private Fade fadeEffect;

    static {
        Resources.INSTANCE.mainStructure().ensureInjected();
        Resources.INSTANCE.popup().ensureInjected();
    }

    public InsertPotentialViewImpl() {

        initWidget(uiBinder.createAndBindUi(this));

        showEffect = new Show(appWrapper);
        fadeEffect = new Fade(appWrapper);

        showEffect.addEffectCompletedHandler(new EffectCompletedHandler() {

            @Override
            public void onEffectCompleted(EffectCompletedEvent event) {
                loaderWidget.setVisible(false);

            }
        });

        loaderWidget.setVisible(true);
        setVisible(appWrapper, false);

    }

    public void initView() {

        //		tabList = new ArrayList<VerticalPanel>();
        //		rootElement.setAnimationEnabled(true);

        productPopUp = new ProductPopUp();

        hideLeftArrow();

        insertPopUp = new InsertPopUp();

        insertPopUp.addCloseHandler(new CloseHandler<PopupPanel>() {

            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                try {
                    listener.onInsertPopUpClose();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        showingPage = 1;

    }

    private void hideRightArrow() {
        rightArrow.setAttribute("class", "display:none");
        rightPagingArrow.setVisible(false);
        rightPagingLabel.setVisible(false);
    }

    private void hideLeftArrow() {
        leftArrow.setAttribute("class", "display:none");
        leftPagingArrow.setVisible(false);
        leftPagingLabel.setVisible(false);
    }

    private void showRightArrow(String pageLabel) {
        rightArrow.setAttribute("class", Resources.INSTANCE.mainStructure().navPagesDX());
        rightPagingArrow.setVisible(true);
        rightPagingLabel.setVisible(true);
        rightPagingLabel.setHTML(pageLabel);
    }

    private void showLeftArrow(String pageLabel) {
        leftArrow.setAttribute("class", Resources.INSTANCE.mainStructure().navPagesSX());
        leftPagingArrow.setVisible(true);
        leftPagingLabel.setVisible(true);
        leftPagingLabel.setHTML(pageLabel);
    }

    @UiHandler("rightPagingLabel")
    void nextPageLabel(ClickEvent event) {
        goRight();
    }

    @UiHandler("leftPagingLabel")
    void previousPageLabel(ClickEvent event) {
        goLeft();
    }

    @UiHandler("rightPagingArrow")
    void nextPage(ClickEvent event) {
        goRight();
    }

    @UiHandler("leftPagingArrow")
    void previousPage(ClickEvent event) {
        goLeft();
    }

    private void goRight() {
        nextPage();
        showingPage++;
        if (showingPage == headlineTablesList.size()) {
            hideRightArrow();

        } else {
            showRightArrow(showingPage + "/" + headlineTablesList.size());

        }
        showLeftArrow(showingPage + "/" + headlineTablesList.size());
        List<RowElementsTable> newPageTables = rowElementsByPageList.get(showingPage - 1);
        browseToNextPage(newPageTables);
        listener.goToPage(showingPage - 1);

    }

    private void goLeft() {
        previousPage();
        showingPage--;
        if (showingPage == 1) {
            hideLeftArrow();

        } else {
            showLeftArrow(showingPage + "/" + headlineTablesList.size());

        }
        showRightArrow(showingPage + "/" + headlineTablesList.size());
        List<RowElementsTable> newPageTables = rowElementsByPageList.get(showingPage - 1);
        browseToNextPage(newPageTables);
        listener.goToPage(showingPage - 1);

    }

    /**
     * Browse horizontally from old to new page
     * @param newPageTables
     * @param oldPageTables
     */
    private void browseToNextPage(List<RowElementsTable> newPageTables) {
        //		rootElement.selectTab(showingPage-1);
        rootElement.clear();
        for (int row = 0; row < rowElementsByClientMap.size(); row++) {
            rootElement.add(newPageTables.get(row));
        }
    }

    @Override
    public void configureLobHeadlines(ArrayList<ArrayList<LineOfBusiness>> lobByPageList) {
        createHeadlineTables(lobByPageList);
        if (headlineTablesList.size() > 1) {
            showRightArrow(showingPage + "/" + headlineTablesList.size());
        }
    }

    @Override
    public void initRowTotals(RowTotalsManager rowTotalsManager) { // usato solo in inizializzaizone pagina
        refreshRowTotals(rowTotalsManager);
    }

    /**
     * Aggiorna solo i totali dei potenziali non nulli e lascia inalterati gli altri
     * @param rowTotalsManager
     */
    @Override
    public void refreshRowTotals(RowTotalsManager rowTotalsManager) {
        Iterator<MarshClientDTO> rowTotalsWidgetsMap = rowElementsByClientMap.keySet().iterator();
        while (rowTotalsWidgetsMap.hasNext()) { // itero su tutti i clienti
            MarshClientDTO mClient = rowTotalsWidgetsMap.next();
            TotalsManager manager = rowTotalsManager.getTotalManagerByRowElementOwnerid(mClient.getClientId());
            // itero su tutti gli elementi grafici del cliente per aggiornarne i totali
            List<RowElementsTable> uiElementsList = rowElementsByClientMap.get(mClient);
            if (manager != null) {
                for (RowElementsTable rowElementsTable : uiElementsList) {
                    rowElementsTable.setTotalAchieved(manager.getTotalAchieved());
                    rowElementsTable.setTotalActual(manager.getTotalFinalBalance());
                    rowElementsTable.setTotalPotential(manager.getTotalPotential());
                }
            }
        }
    }

    @Override
    public void configureClientTables(List<MarshClientDTO> clientByCEList,
            ArrayList<ArrayList<LineOfBusiness>> lobByPageList) {

        int maxRowNumPerPage = clientByCEList.size();

        rowElementsByPageList = new ArrayList<List<RowElementsTable>>(maxRowNumPerPage); // browsed with scroll

        rowElementsByClientMap = new HashMap<MarshClientDTO, List<RowElementsTable>>(maxRowNumPerPage);// used to update totals

        for (int page = 0; page < lobByPageList.size(); page++) { //Browsing LOB by page: initialize row table elements on each page for the current browsed client

            ArrayList<RowElementsTable> tablesInPage = new ArrayList<RowElementsTable>(maxRowNumPerPage);

            rowElementsByPageList.add(page, tablesInPage);

            ArrayList<LineOfBusiness> lobInThePage = lobByPageList.get(page);

            //			VerticalPanel currentPanel = new VerticalPanel();
            //			tabList.add(currentPanel);

            for (int row = 0; row < maxRowNumPerPage; row++) { //Iterazione sulla pagina.

                MarshClientDTO client = clientByCEList.get(row); // cliente per cliente

                RowElementsTable buf2 = new RowElementsTable(this, lobInThePage, client, productPopUp, insertPopUp);// create the current row table for this client in the current page
                tablesInPage.add(buf2);

                List<RowElementsTable> uiElementsList = rowElementsByClientMap.get(client);
                if (null == uiElementsList) {
                    uiElementsList = new ArrayList<RowElementsTable>(lobByPageList.size());
                    rowElementsByClientMap.put(client, uiElementsList);

                }
                uiElementsList.add(buf2);

            }

        }

        List<RowElementsTable> newPageTables = rowElementsByPageList.get(showingPage - 1);
        browseToNextPage(newPageTables);

    }

    /**
     * Algorithm that creates headline tables as needed by the number of LOB and PRODUCTS
     * @param lobList
     */
    void createHeadlineTables(ArrayList<ArrayList<LineOfBusiness>> lobsByPageList) {

        headlineTablesList = new ArrayList<TableElement>(lobsByPageList.size());

        // Each table may contain up to 8 column 75 px width each.
        for (ArrayList<LineOfBusiness> arrayList : lobsByPageList) { // for each page create a table

            int lobConsumed = 0;
            int numbOfPrd = 0;
            int consumedColumn = 0; // used to check if some spacer padding is needed in the last page
            Element el = DOM.createTable();// table creation
            TableElement t = TableElement.as(el);
            headlineTablesList.add(t);
            // border="0" cellpadding="0" cellspacing="0" class="{resources.mainStructure.areaBusiness}"
            t.setAttribute("border", "0");
            t.setAttribute("cellpadding", "0");
            t.setClassName(Resources.INSTANCE.mainStructure().areaBusiness());
            TableSectionElement tbody = TableSectionElement.as(DOM.createTBody());
            t.appendChild(tbody);
            Element tr = DOM.createTR(); // row creation
            tbody.appendChild(tr);
            // fill the row with headlines
            for (LineOfBusiness lob : arrayList) {

                lobConsumed++;
                numbOfPrd += lob.getProducts().size();
                consumedColumn += lob.getProducts().size();
                if (numbOfPrd + (lobConsumed - 1) <= MAX_COL_NUMBER) {// (lobConsumed - 1)is the number of needed spacer: if the number of max columns has not been reached
                    if (lobConsumed > 1) { // if it's not the first one lob, a spacer must be added before the next lob
                        Element spacer = DOM.createTD();
                        spacer.setClassName(Resources.INSTANCE.mainStructure().spacer());
                        tr.appendChild(spacer);
                        consumedColumn++;

                    }
                }

                // add the lob
                Element td = DOM.createTD();
                td.setInnerText(lob.getItemName());
                td.setClassName(Resources.INSTANCE.mainStructure().headLines());
                int width = SPACE_WIDTH * lob.getProducts().size() - DOTS_WIDTH; // space engaged by a line of business label
                String widthS = "" + width;
                td.setAttribute("width", widthS);
                tr.appendChild(td);

            }

            // pad right consumed column
            while (++consumedColumn <= MAX_COL_NUMBER) {
                Element spacer = DOM.createTD();
                spacer.setClassName(Resources.INSTANCE.mainStructure().spacer());
                tr.appendChild(spacer);
            }

        }

        headlinesTableDiv.appendChild(headlineTablesList.get(showntableIndex));

    }

    public void nextPage() {
        //		totalsSumUp.getSubtotalsDiv().removeChild(totalsSumUp.getSubtotalsTablesList().get(showntableIndex));
        headlinesTableDiv.removeChild(headlineTablesList.get(showntableIndex));

        headlinesTableDiv.appendChild(headlineTablesList.get(++showntableIndex));
        //		totalsSumUp.getSubtotalsDiv().appendChild(totalsSumUp.getSubtotalsTablesList().get(showntableIndex));
    }

    public void previousPage() {
        headlinesTableDiv.removeChild(headlineTablesList.get(showntableIndex));
        //		totalsSumUp.getSubtotalsDiv().removeChild(totalsSumUp.getSubtotalsTablesList().get(showntableIndex));

        headlinesTableDiv.appendChild(headlineTablesList.get(--showntableIndex));
        //		totalsSumUp.getSubtotalsDiv().appendChild(totalsSumUp.getSubtotalsTablesList().get(showntableIndex));
    }

    @Override
    public ProductInfoDTO getClickedPillDTO() {
        return insertPopUp.getCallerPill().getTheProductValueIContain();
    }

    @Override
    public void rollback() {
        GWT.log("rollback!!");
        insertPopUp.getCallerPill().getTheProductValueIContain().rollMyInfoBack();
        //		insertPopUp.getCallerPill().init();
    }

    @Override
    public void showLoader(boolean b) {
        if (b) {
            loaderWidget.setVisible(true);
            if (isVisible(appWrapper)) {
                fadeEffect.play();
            }
        } else {
            if (!isVisible(appWrapper)) {
                setVisible(appWrapper, true);
            }
            showEffect.play();
        }
    }

    @Override
    public void setListener(IInsertPotentialViewDisplay.Listener listener) {
        this.listener = listener;
    }

    @Override
    public void initPill() {
        insertPopUp.getCallerPill().init();

    }

    public void remove(RowElementsTable rowElementsTable) { // TODO spostarlo sull'activity:è un casino, lo lascio qua e implemento sull'activity getTotalsManager e inizializzaTotaliColonna

        listener.disableClient(rowElementsTable.mClient.getClientId());

        int debugcont = 0;

        rowElementsByClientMap.remove(rowElementsTable.mClient);

        ProductTotalsManager updatedProductTotalManager = listener.getTotalsManager();

        // ciclo su tutte le pagine
        for (List<RowElementsTable> pageRowList : rowElementsByPageList) {
            RowElementsTable toBeRemoved = null;

            // ciclo su tutti i clienti della pagina....
            for (RowElementsTable rowTableInPage : pageRowList) {

                GWT.log(rowTableInPage.mClient.getClientName());

                // ...finchè trovo il cliente che voglio cancellare
                if (rowTableInPage.mClient.getClientId() == rowElementsTable.mClient.getClientId()) {
                    toBeRemoved = rowTableInPage;

                    //TODO spostare questa parte nell activity
                    // aggiornare i totali di colonna
                    for (ProductPillWidget pill : rowTableInPage.getPillsList()) {
                        if (null != pill.getTheProductValueIContain()) {

                            String valuePotStr = pill.getTheProductValueIContain().getWhatInfoAmI().getValue();
                            int valuePotInt = 0;
                            if (valuePotStr != null) {
                                try {
                                    valuePotInt = Integer.parseInt(valuePotStr) * -1;
                                    updatedProductTotalManager.updateValue(BusinessInfoValueType.POTENTIAL, pill
                                            .getTheProductValueIContain().getId(), pill.getTheProductValueIContain()
                                            .getParentId(), valuePotInt);
                                } catch (NumberFormatException nfe) {
                                    //allora questa stringa non è un numero
                                }
                            }
                            int valueAchivedInt = 0;
                            AchievedInfoDTO achievedInfoDTO = pill.getTheProductValueIContain().getAchievedInfoDTO();
                            if (achievedInfoDTO != null) {
                                String valueAchivedStr = achievedInfoDTO.getValue();
                                try {
                                    valueAchivedInt = Integer.parseInt(valueAchivedStr) * -1;
                                    updatedProductTotalManager.updateValue(BusinessInfoValueType.ACHIEVED, pill
                                            .getTheProductValueIContain().getId(), pill.getTheProductValueIContain()
                                            .getParentId(), valueAchivedInt);
                                } catch (NumberFormatException nfe) {
                                    //allora questa stringa non è un numero
                                }
                            }

                        }
                    }
                    break;// passo alla pagina successiva
                }
            }
            pageRowList.remove(toBeRemoved);
        }

        listener.inizializzaTotaliColonna();

    }

}
