/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.ui;

import java.util.ArrayList;
import java.util.List;

import org.adamtacy.client.ui.effects.examples.Fade;
import org.adamtacy.client.ui.effects.examples.Show;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.kp.marsh.ebt.client.webapp.ui.utils.ViewUtils;
import com.kp.marsh.ebt.client.webapp.ui.views.InsertPotentialViewImpl;
import com.kp.marsh.ebt.shared.dto.LineOfBusiness;
import com.kp.marsh.ebt.shared.dto.MarshClientDTO;
import com.kp.marsh.ebt.shared.dto.ProductInfoDTO;
import com.kp.marsh.ebt.shared.dto.ResetInfoDTO;

/**
 * @author christianachilli
 * 
 */
public class RowElementsTable extends Composite {

    private static RowElementsTableUiBinder uiBinder = GWT.create(RowElementsTableUiBinder.class);

    interface RowElementsTableUiBinder extends UiBinder<Widget, RowElementsTable> {
    }

    @UiField
    Label rowName;

    @UiField
    Label totAch;

    @UiField
    Label totPot;

    @UiField
    Label totCon;

    @UiField
    Image trashOn;

    @UiField
    Image trashOff;

    @UiField
    ProductPillWidget pill_1;

    @UiField
    ProductPillWidget pill_2;

    @UiField
    ProductPillWidget pill_3;

    @UiField
    ProductPillWidget pill_4;

    @UiField
    ProductPillWidget pill_5;

    @UiField
    ProductPillWidget pill_6;

    @UiField
    ProductPillWidget pill_7;

    @UiField
    ProductPillWidget pill_8;

    @UiField
    HTML label_1;

    @UiField
    HTML label_2;

    @UiField
    HTML label_3;

    @UiField
    HTML label_4;

    @UiField
    HTML label_5;

    @UiField
    HTML label_6;

    @UiField
    HTML label_7;

    @UiField
    HTML label_8;

    private ArrayList<ProductPillWidget> pillsList; // public per poter aggiornare i

    // totali di colonna dopo che un
    // cliente viene cancellato @see
    // InsertPotentialViewImpm.remove

    public ArrayList<ProductPillWidget> getPillsList() {
        return pillsList;
    }

    List<HTML> productLabelsList;

    final ProductPopUp productPopUp;

    final InsertPopUp insertPopUp;

    final Fade fadeEff = new Fade();

    final Show showEff = new Show();

    final InsertPotentialViewImpl rowContainer;

    public MarshClientDTO mClient;

    //	private ButtonConfirmComposite buttonConfirmComposite = new ButtonConfirmComposite();
    private RemoveRowPopUp removeRowPopUp = new RemoveRowPopUp(this);

    /**
     * Inserisce l'achieved tenendo conto che è espresso in euro mentre la
     * visualizzazione deve essere in migliaia di euro con una cifra decimale
     * 
     * @param achieved
     */
    public void setTotalAchieved(int achieved) {
        totAch.setText(achieved != 0 ? ViewUtils.format(achieved) : "");
    }

    /**
     * Inserisce il potenziale tenendo conto che è espresso in euro mentre la
     * visualizzazione deve essere in migliaia di euro con una cifra decimale
     * 
     * @param potential
     */
    public void setTotalPotential(int potential) {
        totPot.setText(potential != 0 ? ViewUtils.format(potential) : "");
    }

    /**
     * Inserisce il consuntivo tenendo conto che è espresso in euro mentre la
     * visualizzazione deve essere in migliaia di euro con una cifra decimale
     * 
     * @param actual
     */
    public void setTotalActual(int actual) {
        totCon.setText(actual != 0 ? ViewUtils.format(actual) : "");
    }

    @UiHandler("trashOn")
    public void onNameClick(ClickEvent e) {
        trashOn.setVisible(false);
        trashOff.setVisible(true);
        removeRowPopUp.center();
        removeRowPopUp.show();
    }

    /**
     * 
     * Create a widget that represents a row displayed on a page
     * 
     * @param insertPotentialViewImpl
     * @param lineOfBusinessInPage
     *            the line of business displayed on one page
     */
    public RowElementsTable(InsertPotentialViewImpl insertPotentialViewImpl,
            ArrayList<LineOfBusiness> lineOfBusinessInPage, final MarshClientDTO mClient, ProductPopUp productPopUp,
            InsertPopUp insertPopUp) {

        this.productPopUp = productPopUp;

        this.insertPopUp = insertPopUp;

        this.rowContainer = insertPotentialViewImpl;

        attachUiElements();

        initProducts(lineOfBusinessInPage, mClient);

        rowName.setText(mClient.getClientName());

        this.mClient = mClient;

        setTrashVisibleInit();
    }

    private void attachUiElements() {
        initWidget(uiBinder.createAndBindUi(this));

        pillsList = new ArrayList<ProductPillWidget>();
        pillsList.add(pill_1);
        pillsList.add(pill_2);
        pillsList.add(pill_3);
        pillsList.add(pill_4);
        pillsList.add(pill_5);
        pillsList.add(pill_6);
        pillsList.add(pill_7);
        pillsList.add(pill_8);

        productLabelsList = new ArrayList<HTML>(8);
        productLabelsList.add(label_1);
        productLabelsList.add(label_2);
        productLabelsList.add(label_3);
        productLabelsList.add(label_4);
        productLabelsList.add(label_5);
        productLabelsList.add(label_6);
        productLabelsList.add(label_7);
        productLabelsList.add(label_8);

    }

    @UiFactory
    ProductPillWidget makePills() {
        return new ProductPillWidget(insertPopUp);
    }

    // most cpu consuming routine
    private void initProducts(ArrayList<LineOfBusiness> lineOfBusinessInPage, MarshClientDTO mClient) {
        int columnCount = 0;

        for (LineOfBusiness lob : lineOfBusinessInPage) {

            for (final ProductInfoDTO product : lob.getProducts()) {

                ProductInfoDTO prdInfo = mClient.getProductInfoByProductId(product.getId());

                if (null == prdInfo) { // nel caso ci sia solo il final balance
                                       // il prd info non è null ma lo è il
                                       // whatInfoAmI; se non lo inizialzzo col
                                       // reset poi i consuntivi non avranno il
                                       // pulsante
                    prdInfo = new ProductInfoDTO(mClient, product.getId(), null);
                }
                if (null == prdInfo.getWhatInfoAmI()) {
                    prdInfo.addBusinessInfo(new ResetInfoDTO(mClient.getClientId(), product.getId(), null, "", ""
                            + lob.getId()));
                }

                final HTML lab = productLabelsList.get(columnCount);
                lab.setHTML(product.getItemName());
                //				lab.setText(product.getItemName());
                lab.addMouseOverHandler(new MouseOverHandler() {

                    @Override
                    public void onMouseOver(MouseOverEvent event) {
                        fadeEff.setEffectElement(productPopUp.getElement());
                        fadeEff.setDuration(0.2);
                        showEff.setEffectElement(productPopUp.getElement());
                        showEff.setDuration(0.4);
                        productPopUp.show();
                        productPopUp.setVisible(false);
                        // ProductPopUp pd = ProductPopUp.getInstance();
                        productPopUp.setProductDescription(product.getNotes());
                        productPopUp.setPopupPosition(lab.getAbsoluteLeft() + 15, lab.getAbsoluteTop() + 15);

                        if (!productPopUp.isVisible())
                            productPopUp.setVisible(true);
                        showEff.play();

                    }
                });

                lab.addMouseOutHandler(new MouseOutHandler() {

                    @Override
                    public void onMouseOut(MouseOutEvent event) {
                        productPopUp.hide();

                        fadeEff.play();

                    }
                });

                ProductPillWidget ob = pillsList.get(columnCount);
                lab.setStylePrimaryName(ob.productStyle);

                ob.setTheProductValueIContain(prdInfo);
                ob.theTableIBelong = this;
                ob.init();

                columnCount++;
            }

            if (columnCount < 7) {// insert he space before the next line of
                                  // business
                columnCount++;
            }
        }
    }

    public Label getTotPot() {
        return totPot;
    }

    public void setTotPot(Label totPot) {
        this.totPot = totPot;
    }

    public Label getTotCon() {
        return totCon;
    }

    public void setTotCon(Label totCon) {
        this.totCon = totCon;
    }

    public void setTrashVisibleInit() {
        trashOn.setVisible(true);
        trashOff.setVisible(false);
    }

}
