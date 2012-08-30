package com.kp.marsh.ebt.client.webapp.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.kp.marsh.ebt.client.webapp.ui.resources.Resources;
import com.kp.marsh.ebt.client.webapp.ui.utils.ViewUtils;
import com.kp.marsh.ebt.shared.dto.PotentialInfoDTO;
import com.kp.marsh.ebt.shared.dto.ProductInfoDTO;

/**
 * @author christianachilli
 *Has Click handlers so that the activity can bind a custom event to update totals and save the inserted data
 */
public class ProductPillWidget extends Composite {

    private static ProductPillWidgetUiBinder uiBinder = GWT.create(ProductPillWidgetUiBinder.class);

    interface ProductPillWidgetUiBinder extends UiBinder<Widget, ProductPillWidget> {
    }

    @UiField
    Label potText;

    @UiField
    Label finBalText;

    @UiField
    Label achievedText;

    @UiField
    Image noteImg;

    RowElementsTable theTableIBelong;

    private ProductInfoDTO theProductValueIContain;

    String orangeButtonStyle;

    String greyButtonStyle;

    String greenPillStyle;

    String bluePillStyle;

    String azurePillStyle;

    String greyPillStyle;

    String productStyle;

    String consValueStyle;

    String greenValueStyle;

    String greyButtonConsuntivo;

    String brokerIcon;

    String compagniaIcon;

    private final InsertPopUp insertPopUp;

    private PostItPopUp productPostIt;

    public ProductPillWidget(final InsertPopUp insertPopUp) {
        this.insertPopUp = insertPopUp;
        initWidget(uiBinder.createAndBindUi(this));
        orangeButtonStyle = Resources.INSTANCE.mainStructure().orangeButton();
        greyButtonStyle = Resources.INSTANCE.mainStructure().greyButton();
        greenPillStyle = Resources.INSTANCE.mainStructure().greenButton();
        bluePillStyle = Resources.INSTANCE.mainStructure().bluButton();
        greyPillStyle = Resources.INSTANCE.mainStructure().greyButton();
        azurePillStyle = Resources.INSTANCE.mainStructure().lightbluButton();
        productStyle = Resources.INSTANCE.mainStructure().product();
        consValueStyle = Resources.INSTANCE.mainStructure().consValue();
        greenValueStyle = Resources.INSTANCE.mainStructure().greenLabel();
        greyButtonConsuntivo = Resources.INSTANCE.mainStructure().greyButtonConsuntivo();
        brokerIcon = Resources.INSTANCE.mainStructure().brokerIcon();
        compagniaIcon = Resources.INSTANCE.mainStructure().compagniaIcon();

        productPostIt = new PostItPopUp();

        productPostIt.addCloseHandler(new CloseHandler<PopupPanel>() {

            @Override
            public void onClose(CloseEvent<PopupPanel> event) {

                theProductValueIContain.getWhatInfoAmI().setPostItText(productPostIt.getPostItNote());
                checkNoteIconVisible();

                insertPopUp.show(); //THIS IS TOTAL HACK!!!!!! x salvare la nota
                insertPopUp.hide();//THIS IS TOTAL HACK!!!!!!

            }

        });

    }

    @UiHandler("noteImg")
    public void onIconNoteClick(ClickEvent e) {
        insertPopUp.callerPill = this;
        showNotesPopUp();
        e.stopPropagation(); // altrimenti compare anche il pop up di inserimento
    }

    //	@UiHandler("finBalText")
    //	public void onClickFinalBalance(ClickEvent event) {
    //		InsertPopUp.getInstance().callerPill = this;
    //		potText.setStyleName(azurePillStyle);
    //		if(BusinessInfoValueType.RESET.equals(theProductValueIContain.getWhatInfoAmI().getValueType())
    //				|| BusinessInfoValueType.FINAL_BALANCE.equals(theProductValueIContain.getWhatInfoAmI().getValueType())) {
    //			potText.setText("...");
    //		}
    //		InsertPopUp.getInstance().showInsertPopUp();
    //
    //	}

    @UiHandler("potText")
    public void onClickPotential(ClickEvent event) {

        insertPopUp.callerPill = this;
        potText.setStyleName(azurePillStyle);
        if ("+".equals(potText.getText())) {
            potText.setText("...");
        }

        insertPopUp.showInsertPopUp();
    }

    public void setPillLabelText(String value) {
        potText.setText(value);
    }

    public String getPillLabelText() {
        return potText.getText();
    }

    /**
     * Invocato ogni volta che la pillola deve essere refreshata: all'onload della pagina e dopo la chiusura del popup di inserimento o dl pop up di inserimento nota
     */
    public void init() {

        // già inizializzato nel costruttore di ProductInfoDTO
        //		getTheProductValueIContain().setPreviousBusinessInfo(getTheProductValueIContain().getWhatInfoAmI());// per inizializzare lo stato precedente

        if (theProductValueIContain != null && theProductValueIContain.getWhatInfoAmI() != null)
            productPostIt.setPostItNote(theProductValueIContain.getWhatInfoAmI().getPostItText());

        checkNoteIconVisible();

        if (null != theProductValueIContain && null != theProductValueIContain.getMyFinalBalance()) {
            finBalText.setStylePrimaryName(consValueStyle);
            finBalText.setText(ViewUtils.format(theProductValueIContain.getMyFinalBalance().getValue()));
        }

        if (null != theProductValueIContain && null != theProductValueIContain.getAchievedInfoDTO()) {
            achievedText.setStylePrimaryName(greenValueStyle);
            achievedText.setText(ViewUtils.format(theProductValueIContain.getAchievedInfoDTO().getValue()));
        }

        if (null != theProductValueIContain && null != theProductValueIContain.getWhatInfoAmI()) { // non deve mai essere null

            switch (theProductValueIContain.getWhatInfoAmI().getValueType()) {

            case RESET:
                if (null != theProductValueIContain.getAchievedInfoDTO()) {
                    potText.setStylePrimaryName(greenPillStyle);
                } else if (null != theProductValueIContain.getMyFinalBalance()) {
                    potText.setStylePrimaryName(greyButtonConsuntivo);
                } else {
                    potText.setStylePrimaryName(orangeButtonStyle);
                }
                potText.setText("+");
                break;

            case BROKER_POT:
                potText.setStylePrimaryName(brokerIcon);
                potText.setText(ViewUtils.format(theProductValueIContain.getWhatInfoAmI().getValue()));
                break;

            case COMPANY_POT:
                potText.setStylePrimaryName(compagniaIcon);
                potText.setText(ViewUtils.format(theProductValueIContain.getWhatInfoAmI().getValue()));
                break;

            case POTENTIAL:
                // discriminate potential values
                PotentialInfoDTO potInfo = ((PotentialInfoDTO) theProductValueIContain.getWhatInfoAmI());

                switch (potInfo.getPotentialValueType()) {
                case INTERNATIONAL_PROGRAM:
                    potText.setStylePrimaryName(greyPillStyle);
                    potText.setText("PI");
                    break;
                case NOT_APPLICABLE:
                    potText.setStylePrimaryName(greyPillStyle);
                    potText.setText("NA");
                    break;
                case IN_ALTRA_COPERTURA:
                    potText.setStylePrimaryName(greyPillStyle);
                    potText.setText("IAC");
                    break;
                case VALUE:
                    potText.setStylePrimaryName(bluePillStyle);
                    potText.setText(ViewUtils.format(potInfo.getValue()));
                    break;
                default:
                    break;
                }

                break;

            }

        } else {
            GWT.log("Errore, product info DTO senza business information che non sia un consuntivo. Dovrebbe almeno avere un Reset");
        }

    }

    public ProductInfoDTO getTheProductValueIContain() {
        return theProductValueIContain;
    }

    public void setTheProductValueIContain(ProductInfoDTO theProductValueIContain) {
        this.theProductValueIContain = theProductValueIContain;
    }

    public void showNotesPopUp() {
        productPostIt.setPopupPosition(this.getAbsoluteLeft() + this.getOffsetWidth() - 10, this.getAbsoluteTop());
        productPostIt.getPostIt().setFocus(true);
        productPostIt.show();
        getTheProductValueIContain().setPreviousBusinessInfo(getTheProductValueIContain().getWhatInfoAmI());//è un hack che uso perchè il salvataggio della nota avviene sull'evento di chiusura del pop up di inserimento. Se non faccio così mi si sballano i totali

    }

    private void checkNoteIconVisible() {
        if ("".equals(productPostIt.getPostItNote())) {
            hideNoteIcon();
        } else {
            showNoteIcon();
        }
    }

    private void showNoteIcon() {
        noteImg.setStylePrimaryName(Resources.INSTANCE.mainStructure().noteIcon_visible());
    }

    private void hideNoteIcon() {
        noteImg.setStylePrimaryName(Resources.INSTANCE.mainStructure().noteIcon_hidden());
    }

    public PostItPopUp getProductPostIt() {
        return productPostIt;
    }

}
