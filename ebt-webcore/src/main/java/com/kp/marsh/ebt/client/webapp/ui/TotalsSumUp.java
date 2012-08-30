/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.kp.marsh.ebt.client.webapp.ui.resources.Resources;
import com.kp.marsh.ebt.client.webapp.ui.utils.ViewUtils;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;
import com.kp.marsh.ebt.shared.dto.LineOfBusiness;
import com.kp.marsh.ebt.shared.dto.ProductDTO;
import com.kp.marsh.ebt.shared.dto.ProductTotalsManager;
import com.kp.marsh.ebt.shared.dto.TotalsManager;

/**
 * @author christianachilli
 *
 *Display the sum up by columns. May be hidden.
 *
 */
public class TotalsSumUp extends Composite {

    private static TotalsSumUpUiBinder uiBinder = GWT.create(TotalsSumUpUiBinder.class);

    interface TotalsSumUpUiBinder extends UiBinder<Widget, TotalsSumUp> {
    }

    @UiField
    DivElement headlinesTableDiv; // titoli delle lob

    @UiField
    DivElement subtotalsDiv;

    @UiField
    TableCellElement cePotTotal;

    @UiField
    TableCellElement ceAchTotal;

    @UiField
    TableRowElement potSum;

    @UiField
    TableRowElement achSum;

    @UiField
    RadioButton radioPot;

    @UiField
    RadioButton radioAch;

    private ProductTotalsManager totalsManager; // è una specie di dto che contiene i totali di potenziale e achieved divisi per prodotto

    private BusinessInfoValueType selectedTotalType = BusinessInfoValueType.POTENTIAL;

    List<TableElement> subtotalsTablesList; // contains the subtotals tables to be displayed

    Map<Integer, Element> productTdElementsMap;

    Map<String, Element> lobTdElementsMap;

    private Element currentLobLabelTable;
    private Element currentLobTitleTable;

    /**
     * List that contains the tables with line of business name. Such list is created in order that any table can be displayed on one page. By clicking left and right side arrows this list is navigated.
     * By clicking such arrows, the corresponding table replace the table in TotalsHeadlineViewImpl <code>headlinesTableDiv</code> member.
     */
    ArrayList<TableElement> headlineTablesList;

    public TotalsSumUp() {
        productTdElementsMap = new HashMap<Integer, Element>();
        lobTdElementsMap = new HashMap<String, Element>();
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void initView() {
    }

    @UiHandler("radioPot")
    public void onRadioPotSelection(ClickEvent e) {
        selectedTotalType = BusinessInfoValueType.POTENTIAL;
        for (TableElement t : subtotalsTablesList) {
            t.getTFoot().removeClassName(Resources.INSTANCE.mainStructure().totalsAreaGreen());
            t.getTFoot().addClassName(Resources.INSTANCE.mainStructure().totalsAreaBlu());
        }
        refreshTotals(this.totalsManager);
    }

    @UiHandler("radioAch")
    public void onRadioAchSelection(ClickEvent e) {
        selectedTotalType = BusinessInfoValueType.ACHIEVED;
        for (TableElement t : subtotalsTablesList) {
            t.getTFoot().removeClassName(Resources.INSTANCE.mainStructure().totalsAreaBlu());
            t.getTFoot().addClassName(Resources.INSTANCE.mainStructure().totalsAreaGreen());
        }
        refreshTotals(this.totalsManager);
    }

    private void setProductTotalOnUI(int productId, String total) {
        Element el = productTdElementsMap.get(productId);
        if (null != el) {
            el.setInnerHTML("0".equals(total) ? "" : ViewUtils.format(total));
        }
    }

    private void setLobTotalOnUI(String lobId, String total) {
        Element el = lobTdElementsMap.get(lobId);
        if (null != el) {
            el.setInnerHTML("0".equals(total) ? "" : "= " + ViewUtils.format(total));
        }
    }

    public void createSubtotalsTables(ArrayList<ArrayList<LineOfBusiness>> lobsByPage) {

        /**
         * Invocato alla partenza della header activity perchè la view è singleton e se non lo faccio ogni volta che si fa login si aggiunge una tabella!
         * Se le tabelle esistono già non le ricreo
         */
        if (!(null != subtotalsTablesList && !subtotalsTablesList.isEmpty())) {
            subtotalsTablesList = new ArrayList<TableElement>();
            for (ArrayList<LineOfBusiness> arrayList : lobsByPage) {
                // create TABLE
                TableElement table = TableElement.as(DOM.createTable());
                subtotalsTablesList.add(table);
                table.setAttribute("border", "0");
                table.setAttribute("cellpadding", "0");
                table.setAttribute("cellspacing", "0");
                table.setClassName(Resources.INSTANCE.mainStructure().resumeTotal());

                //create THEAD with product's names
                TableSectionElement thead = TableSectionElement.as(DOM.createTHead());
                thead.setClassName(Resources.INSTANCE.mainStructure().totLineaBus());
                table.appendChild(thead);

                Element trHead = DOM.createTR();
                thead.appendChild(trHead);

                int consumedLob = 0;
                for (LineOfBusiness lineOfBusiness : arrayList) {// set the labels
                    consumedLob++;
                    for (ProductDTO product : lineOfBusiness.getProducts()) {
                        Element th = DOM.createTH();
                        th.setInnerHTML(product.getItemName());
                        trHead.appendChild(th);

                    }
                    if (consumedLob < arrayList.size()) {// after the last one no space is needed
                        Element th = DOM.createTH();
                        th.setInnerHTML("");// space
                        trHead.appendChild(th);

                    }

                }

                //create TBODY with product's totals
                TableSectionElement tbody = TableSectionElement.as(DOM.createTBody());
                tbody.setClassName(Resources.INSTANCE.mainStructure().valuesArea());
                table.appendChild(tbody);
                Element trBody = DOM.createTR();
                tbody.appendChild(trBody);
                consumedLob = 0;
                for (LineOfBusiness lineOfBusiness : arrayList) {// set the total values
                    consumedLob++;
                    for (ProductDTO product : lineOfBusiness.getProducts()) {
                        Element td = DOM.createTD();
                        productTdElementsMap.put(product.getId(), td);
                        trBody.appendChild(td);

                    }
                    if (consumedLob < arrayList.size()) {// after the last one no space is needed
                        Element td = DOM.createTD();
                        td.setInnerHTML("");// add a space
                        trBody.appendChild(td);

                    }

                }

                //create TFOOT with lob's total
                TableSectionElement tfoot = TableSectionElement.as(DOM.createTFoot());
                tfoot.setClassName(Resources.INSTANCE.mainStructure().totalsAreaBlu());
                table.appendChild(tfoot);
                Element trFoot = DOM.createTR();
                tfoot.appendChild(trFoot);
                for (LineOfBusiness lineOfBusiness : arrayList) {// set the total sum up value
                    int consumedPrd = 0;
                    for (ProductDTO product : lineOfBusiness.getProducts()) {
                        consumedPrd++;
                        Element th = DOM.createTH();
                        if (consumedPrd == lineOfBusiness.getProducts().size()) {// TODO set the total value and the proper style
                            lobTdElementsMap.put(product.getParentId(), th);
                            //						th.setInnerHTML("000");//
                            trFoot.appendChild(th);
                            th = DOM.createTH();
                            th.setInnerHTML(""); // spaces until last com
                            trFoot.appendChild(th);

                        } else {
                            th.setInnerHTML(""); // spaces until last com
                            trFoot.appendChild(th);

                        }
                    }

                }
            }

        }

        gotToPage(0);

        //		if(null != currentLobLabelTable && subtotalsDiv.isOrHasChild(currentLobLabelTable)) {
        //			subtotalsDiv.removeChild(currentLobLabelTable); // rimuovo quella eventualmente rimasta dal login precedente
        //		}
        //		subtotalsDiv.appendChild(subtotalsTablesList.get(0)); // init con la prima pagina
        //		currentLobLabelTable = subtotalsTablesList.get(0);

    }

    /**
     * Refresh the total of the products retrieving the info from the total manager according to what type of info is selected(achieved potential or actual)
     * This method has to be called when a potential is inserted or modified or when a total type get clicked.
     */
    public void refreshTotals(ProductTotalsManager totManager) { // TODO deve essere un metodo che la UI espone in modo che la activity possa aggiornare i totali di lob e prodotto
        if (null != totManager) {
            this.totalsManager = totManager;
            refreshProducts();
            refreshLobs();

        }

    }

    private void refreshLobs() {
        Iterator<String> lobIdIterator = lobTdElementsMap.keySet().iterator();
        while (lobIdIterator.hasNext()) {
            String lobId = lobIdIterator.next();
            TotalsManager manager = totalsManager.getTotalsManagerByLobId(lobId);
            if (manager != null) {
                switch (selectedTotalType) {
                case POTENTIAL:
                case BROKER_POT: // non è previsto che si differenzino dal potenziale...ma non si sa mai!!!!
                case COMPANY_POT: // idem come sopra
                    setLobTotalOnUI(lobId, "" + manager.getTotalPotential());
                    break;

                case ACHIEVED:
                    setLobTotalOnUI(lobId, "" + manager.getTotalAchieved());
                    break;

                case FINAL_BALANCE:
                    setLobTotalOnUI(lobId, "" + manager.getTotalFinalBalance());
                    break;

                default:
                    break;
                }
            }
        }
    }

    private void refreshProducts() {
        Iterator<Integer> productidIterator = productTdElementsMap.keySet().iterator();
        while (productidIterator.hasNext()) {
            int productId = productidIterator.next();
            TotalsManager manager = totalsManager.getTotalManagerByProductid(productId);
            if (manager != null) {
                switch (selectedTotalType) {
                case POTENTIAL:
                case BROKER_POT:// non è previsto che si differenzino dal potenziale...ma non si sa mai!!!!
                case COMPANY_POT:// idem come sopra
                    setProductTotalOnUI(productId, "" + manager.getTotalPotential());
                    break;

                case ACHIEVED:
                    setProductTotalOnUI(productId, "" + manager.getTotalAchieved());
                    break;

                case FINAL_BALANCE:
                    setProductTotalOnUI(productId, "" + manager.getTotalFinalBalance());
                    break;

                default:
                    break;
                }
            }
        }
    }

    public void gotToPage(int pageIndex) {
        if (null != currentLobLabelTable)
            subtotalsDiv.removeChild(currentLobLabelTable);
        currentLobLabelTable = subtotalsTablesList.get(pageIndex);
        subtotalsDiv.appendChild(currentLobLabelTable);

        if (null != currentLobTitleTable)
            headlinesTableDiv.removeChild(currentLobTitleTable);
        currentLobTitleTable = headlineTablesList.get(pageIndex);
        headlinesTableDiv.appendChild(currentLobTitleTable);

    }

    public void setPotentialTotal(int totPot) {
        cePotTotal.setInnerHTML(totPot == 0 ? "" : ViewUtils.format(totPot));

    }

    public void setAchievedTotal(int totAch) {
        ceAchTotal.setInnerHTML(totAch == 0 ? "" : ViewUtils.format(totAch));

    }

    /**
     * Algorithm that creates headline tables as needed by the number of LOB and PRODUCTS
     * @param lobList
     */
    public void createHeadlineTables(ArrayList<ArrayList<LineOfBusiness>> lobsByPageList) {
        int MAX_COL_NUMBER = 8; // NUMBER OF COLUMNS OF THE VISIBLE TABLE
        int SPACE_WIDTH = 75; // WIDTH IN PX OF A SINGLE COLUMN BELONGING THE TABLE
        int DOTS_WIDTH = 2; // width in px of the headline delimiters

        /**
         * Invocato alla partenza della header activity perchè la view è singleton e se non lo faccio ogni volta che si fa login si aggiunge una tabella!
         * Se le tabelle esistono già non le ricreo
         */
        if (!(null != headlineTablesList && !headlineTablesList.isEmpty())) {
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
                t.setClassName(Resources.INSTANCE.mainStructure().areaBusinessTitoliDiscesa());
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
        }
    }

}
