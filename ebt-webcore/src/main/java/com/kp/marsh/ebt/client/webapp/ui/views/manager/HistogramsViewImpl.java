package com.kp.marsh.ebt.client.webapp.ui.views.manager;

import java.util.ArrayList;

import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.examples.Fade;
import org.adamtacy.client.ui.effects.examples.Show;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.kp.marsh.ebt.client.webapp.ui.LoaderWidget;
import com.kp.marsh.ebt.client.webapp.ui.RowElementsTableIsto;
import com.kp.marsh.ebt.client.webapp.ui.activityintf.IHistogramsViewDisplay;
import com.kp.marsh.ebt.client.webapp.ui.resources.Resources;
import com.kp.marsh.ebt.shared.dto.LightInfoOwnerDto;
import com.kp.marsh.ebt.shared.dto.LineOfBusiness;

public class HistogramsViewImpl extends Composite implements IHistogramsViewDisplay {

    private static HistogramsViewImplUiBinder uiBinder = GWT.create(HistogramsViewImplUiBinder.class);

    interface HistogramsViewImplUiBinder extends UiBinder<Widget, HistogramsViewImpl> {
    }

    /**
     * Nella FlexTable metto gli istogrammi
     * 
     */

    @UiField
    FlexTable rootElement;

    @UiField
    DivElement headlinesTableDiv; // titoli delle lob

    Element currentLobLabelTable;

    @UiField
    DivElement appWrapper;

    @UiField
    DivElement leftArrow;

    @UiField
    DivElement rightArrow;

    @UiField
    Anchor leftPagingLabel;

    @UiField
    Anchor rightPagingLabel;

    @UiField
    Image rightPagingArrow;

    @UiField
    Image leftPagingArrow;

    @UiField
    LoaderWidget loaderWidget;

    private Fade fadeEffect;

    private Show showEffect;

    @UiField
    SpanElement ufficio;

    @UiField
    SpanElement ce;

    @UiField
    SpanElement gc;

    @UiField
    SpanElement descrUff;

    @UiField
    SpanElement descrCe;

    @UiField
    SpanElement descrGc;

    private ArrayList<RowElementsTableIsto> elementInPageList; // elenco degli
    // owner e degli istogrammi mostrati in pagina

    IHistogramsViewDisplay.Listener listener;

    public HistogramsViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));

        fadeEffect = new Fade(appWrapper);
        fadeEffect.setDuration(0.5);
        showEffect = new Show(appWrapper);
        showEffect.setDuration(0.5);

        showEffect.addEffectCompletedHandler(new EffectCompletedHandler() {

            @Override
            public void onEffectCompleted(EffectCompletedEvent event) {
                loaderWidget.setVisible(false);

            }
        });

        showLoaderWithHight();
        setVisible(appWrapper, false);

        setVisible(ufficio, false);
        setVisible(ce, false);
        setVisible(gc, false);

        elementInPageList = new ArrayList<RowElementsTableIsto>();

    }

    /**
     * 
     */
    private void showLoaderWithHight() {
        loaderWidget.getHtmlPanel().setHeight("" + this.getOffsetHeight());
        loaderWidget.setVisible(true);
    }

    @UiHandler("leftPagingLabel")
    void previousPageLabel(ClickEvent event) {
        goLeft();
    }

    @UiHandler("rightPagingLabel")
    void nextPageLabel(ClickEvent event) {
        goRight();
    }

    @UiHandler("rightPagingArrow")
    void nextPage(ClickEvent event) {
        goRight();
    }

    @UiHandler("leftPagingArrow")
    void previousPage(ClickEvent event) {
        goLeft();
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

    private void goRight() {
        listener.getNextPageData();

    }

    private void goLeft() {
        listener.getPreviousPageData();

    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;

    }

    @Override
    public void appendPageData(LightInfoOwnerDto... pageValue) {
        int lastAppendedRow = 0;
        rootElement.clear();
        for (int i = 0; i < pageValue.length; i++) {
            RowElementsTableIsto rowWidget = null;
            if (elementInPageList.size() < (i + 1)) { // se manca l'elemento di riga lo devo creare
                rowWidget = new RowElementsTableIsto(pageValue[i]);
                elementInPageList.add(rowWidget);

            } else { // se l'elemento di riga c' lo aggiorno coi nuovi valori
                rowWidget = elementInPageList.get(lastAppendedRow);
                rowWidget.updateHistograms(pageValue[i]);

            }
            rootElement.setWidget(lastAppendedRow, 0, rowWidget);
            lastAppendedRow++;

        }
    }

    @Override
    public void setLobHeadline(ArrayList<LineOfBusiness> lobsInPage) {

        if (headlinesTableDiv.hasChildNodes())
            headlinesTableDiv.removeChild(currentLobLabelTable);// rimuove la
                                                                // tabella con
                                                                // le label
                                                                // delle linee
                                                                // di business

        // Each table may contain up to 8 column 75 px width each.
        int MAX_COL_NUMBER = 8; // NUMBER OF COLUMNS OF THE VISIBLE TABLE
        int SPACE_WIDTH = 75; // WIDTH IN PX OF A SINGLE COLUMN BELONGING THE
                              // TABLE
        int DOTS_WIDTH = 2; // width in px of the headline delimiters

        int lobConsumed = 0;
        int numbOfPrd = 0;
        int consumedColumn = 0; // used to check if some spacer padding is
                                // needed in the last page
        Element el = DOM.createTable();// table creation
        TableElement t = TableElement.as(el);
        // border="0" cellpadding="0" cellspacing="0"
        // class="{resources.mainStructure.areaBusiness}"
        t.setAttribute("border", "0");
        t.setAttribute("cellpadding", "0");
        t.setClassName(Resources.INSTANCE.mainStructure().areaBusiness());
        TableSectionElement tbody = TableSectionElement.as(DOM.createTBody());
        t.appendChild(tbody);
        Element tr = DOM.createTR(); // row creation
        tbody.appendChild(tr);
        // fill the row with headlines
        for (LineOfBusiness lob : lobsInPage) {

            lobConsumed++;
            numbOfPrd += lob.getProducts().size();
            consumedColumn += lob.getProducts().size();
            if (numbOfPrd + (lobConsumed - 1) <= MAX_COL_NUMBER) {// (lobConsumed
                                                                  // - 1)is
                                                                  // the
                                                                  // number of
                                                                  // needed
                                                                  // spacer:
                                                                  // if the
                                                                  // number of
                                                                  // max
                                                                  // columns
                                                                  // has not
                                                                  // been
                                                                  // reached
                if (lobConsumed > 1) { // if it's not the first one lob, a
                                       // spacer must be added before the next
                                       // lob
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
            int width = SPACE_WIDTH * lob.getProducts().size() - DOTS_WIDTH; // space
                                                                             // engaged
                                                                             // by
                                                                             // a
                                                                             // line
                                                                             // of
                                                                             // business
                                                                             // label
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

        headlinesTableDiv.appendChild(el);
        currentLobLabelTable = el;

    }

    @Override
    public void setPageIndexes(int totPage, int currentPage) {
        if (currentPage == 0) { // prima pagina
            hideLeftArrow();
            showRightArrow("" + (currentPage + 1) + "/" + totPage);
        } else if (totPage == (currentPage + 1)) { // ultima pagina
            hideRightArrow();
            showLeftArrow("" + (currentPage + 1) + "/" + totPage);
        } else {
            showRightArrow("" + (currentPage + 1) + "/" + totPage);
            showLeftArrow("" + (currentPage + 1) + "/" + totPage);
        }

    }

    @Override
    public void showLoader(boolean show, String message) {
        if (show) {
            showLoaderWithHight();
            if (isVisible(appWrapper)) {
                fadeEffect.play();
            }
        } else { // l'implemetazione del'else  un hack fatto per evitare lo sfarfllio tra logout e login alla selezione degli istogrammi
            showEffect.play();
            if (!isVisible(appWrapper)) {
                setVisible(appWrapper, true);
            }
        }
    }

    @Override
    public void initView() {
        showLoaderWithHight();
        setVisible(appWrapper, false); // per evitareil flickering quando consulto gli istogrammi dopo login logout
        fadeEffect.play();

    }

    @Override
    public void setStatusBar(String ufficioSelectedDescription, String ceSelectedDescription,
            String gcSelectedDescription) {
        //UFFICIO
        if (null != ufficioSelectedDescription && !"".equals(ufficioSelectedDescription)) {
            setVisible(ufficio, true);
            descrUff.setInnerHTML(ufficioSelectedDescription);
        } else
            setVisible(ufficio, false);
        //CE
        if (null != ceSelectedDescription && !"".equals(ceSelectedDescription)) {
            setVisible(ce, true);
            descrCe.setInnerHTML(ceSelectedDescription);
        } else
            setVisible(ce, false);
        //GC
        if (null != gcSelectedDescription && !"".equals(gcSelectedDescription)) {
            setVisible(gc, true);
            descrGc.setInnerHTML(gcSelectedDescription);
        } else
            setVisible(gc, false);
    }
}
