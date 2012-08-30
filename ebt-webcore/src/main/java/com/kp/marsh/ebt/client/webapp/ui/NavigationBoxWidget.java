package com.kp.marsh.ebt.client.webapp.ui;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.kp.marsh.ebt.client.webapp.ui.resources.Resources;
import com.kp.marsh.ebt.client.webapp.ui.views.manager.HeaderBarViewImpl;
import com.kp.marsh.ebt.shared.dto.NavigationDTO;
import com.kp.marsh.ebt.shared.dto.OwnerType;

public class NavigationBoxWidget extends Composite {

    static {
        Resources.INSTANCE.gauges().ensureInjected();

    }

    HeaderBarViewImpl barViewImpl;

    private int currentlySelectedOwner;
    //tengo traccia della description delle listeBox selezionate per popolare la status-bar
    private String selectedDescriptionUfficio;
    private String selectedDescriptionCE;
    private String selectedDescriptionGC;

    //UFFICIO
    Label ufficioLabel;
    ListBox lbUfficio;
    VerticalPanel vpUfficio;

    //CE
    Label ceLabel;
    ListBox lbCE;
    VerticalPanel vpCE;

    //GC
    Label gcLabel;
    ListBox lbGC;
    VerticalPanel vpGC;

    OwnerType selectedOwnerType = OwnerType.OFFICE;
    Anchor visualizzaButton;

    private UfficioChangeHandler uffCH;

    public NavigationBoxWidget() {

        //UFFICIO
        ufficioLabel = new Label("UFFICIO");
        lbUfficio = new ListBox();

        vpUfficio = new VerticalPanel();

        ufficioLabel.setStyleName(Resources.INSTANCE.mainStructure().labelListBoxUff());

        uffCH = new UfficioChangeHandler();

        lbUfficio.addChangeHandler(uffCH);

        lbUfficio.addItem("");
        lbUfficio.setStyleName(Resources.INSTANCE.mainStructure().listBoxNavigationUff());
        vpUfficio.add(ufficioLabel);
        vpUfficio.add(lbUfficio);
        vpUfficio.setStyleName(Resources.INSTANCE.mainStructure().vpNavigationUfficio());
        //	    vpUfficio.setCellHorizontalAlignment(ufficioLabel, HasHorizontalAlignment.ALIGN_CENTER);
        //	    vpUfficio.setCellHorizontalAlignment(lbUfficio, HasHorizontalAlignment.ALIGN_CENTER);

        //CE
        ceLabel = new Label("CLIENT EXECUTIVE");
        ceLabel.setStyleName(Resources.INSTANCE.mainStructure().labelListBoxCE());
        lbCE = new ListBox();
        lbCE.setStyleName(Resources.INSTANCE.mainStructure().listBoxNavigationCE());
        lbCE.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                int selected = lbCE.getSelectedIndex();
                String selectedValue = lbCE.getValue(selected);
                GWT.log("clikkato ce id: " + selectedValue);
                currentlySelectedOwner = Integer.parseInt(selectedValue);
                selectedDescriptionCE = lbCE.getItemText(selected);
                selectedDescriptionGC = "";
                barViewImpl.getListener().ceSelected(currentlySelectedOwner);
                selectedOwnerType = OwnerType.CE;
            }
        });
        vpCE = new VerticalPanel();
        vpCE.add(ceLabel);
        vpCE.add(lbCE);
        vpCE.setStyleName(Resources.INSTANCE.mainStructure().vpNavigationCE());

        //GC
        gcLabel = new Label("GRUPPO COMMERCIALE");
        gcLabel.setStyleName(Resources.INSTANCE.mainStructure().labelListBoxGC());
        lbGC = new ListBox();
        lbGC.setStyleName(Resources.INSTANCE.mainStructure().listBoxNavigationGC());

        lbGC.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                int selected = lbGC.getSelectedIndex();
                String selectedValue = lbGC.getValue(selected);
                GWT.log("clikkato ce id: " + selectedValue);
                currentlySelectedOwner = Integer.parseInt(selectedValue);
                selectedDescriptionGC = lbGC.getItemText(selected);
                selectedOwnerType = OwnerType.GRUPPO_COMMERCIALE;
            }
        });
        vpGC = new VerticalPanel();
        vpGC.add(gcLabel);
        vpGC.add(lbGC);
        vpGC.setStyleName(Resources.INSTANCE.mainStructure().vpNavigationGC());

        //ADD VERTICAL PANEL TO HORIZONTAL PANEL
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(vpUfficio);
        hp.add(vpCE);
        hp.add(vpGC);
        //	    hp.setCellWidth(vpUfficio, "200px");
        hp.setCellWidth(vpCE, "226px");
        //	    hp.setCellWidth(vpGC, "200px");
        hp.setStyleName(Resources.INSTANCE.mainStructure().panelNavigation());

        visualizzaButton = new Anchor("Aggiorna");
        visualizzaButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                visualizzaButton.setStylePrimaryName(Resources.INSTANCE.mainStructure().animaAggiorna());
                GWT.log("clikkato visualizza con id: " + currentlySelectedOwner + " del tipo "
                        + selectedOwnerType.name());
                barViewImpl.getListener().locationIsSelected("" + currentlySelectedOwner, selectedOwnerType,
                        selectedDescriptionUfficio, selectedDescriptionCE, selectedDescriptionGC);
            }
        });
        visualizzaButton.setStylePrimaryName(Resources.INSTANCE.mainStructure().buttonVisualizzaNavigation());
        hp.add(visualizzaButton);

        initWidget(hp);
    }

    public void setHeaderBarVIew(HeaderBarViewImpl barViewImpl) {
        this.barViewImpl = barViewImpl;

    }

    public void popolaListBoxUffici(ArrayList<NavigationDTO> result) {
        lbUfficio.clear();
        lbCE.clear();
        lbGC.clear();
        if (null != result) {
            for (NavigationDTO navDTO : result) {
                lbUfficio.addItem(navDTO.getDescription(), navDTO.getId());

            }
        }
    }

    public void popolaListBoxCE(ArrayList<NavigationDTO> result) {
        lbCE.clear();
        lbGC.clear();
        if (null != result) {
            for (NavigationDTO navDTO : result) {
                lbCE.addItem(navDTO.getDescription(), navDTO.getId());

            }
        }

    }

    public void popolaListBoxGC(ArrayList<NavigationDTO> result) {
        lbGC.clear();
        if (null != result) {
            for (NavigationDTO navDTO : result) {
                lbGC.addItem(navDTO.getDescription(), navDTO.getId());

            }
        }
    }

    /**
     * 
     * Invocato al log in per selezionare sulle combo l'ufficio dell'utente 
     * @param officeId
     */
    public void selectUserOffice(String officeId) {
        if (null != officeId && !"".equals(officeId)) {
            currentlySelectedOwner = Integer.valueOf(officeId);
            selectedOwnerType = OwnerType.OFFICE;
            for (int i = 0; i < lbUfficio.getItemCount(); i++) { // seleziono l'elemento nella combo uffici
                String val = lbUfficio.getValue(i);
                if (val.equals(officeId)) {
                    lbUfficio.setSelectedIndex(i); // seleziono l'ufficio
                    uffCH.onChange(null);
                    break;

                }
            }
        }
    }

    /**
     * @author christianachilli
     * Mi serve per poter selezionare l'ufficio al login, perch i metodi della listbox non vanno
     */
    private class UfficioChangeHandler implements ChangeHandler {

        @Override
        public void onChange(ChangeEvent event) {
            selectedOwnerType = OwnerType.OFFICE;
            int selected = lbUfficio.getSelectedIndex();// seleziono l'indice della lista che ho clikkato
            String selectedValue = lbUfficio.getValue(selected); // l'id dell'ufficio su information owners
            GWT.log("clikkato ufficio id: " + selectedValue);
            currentlySelectedOwner = Integer.parseInt(selectedValue);
            selectedDescriptionUfficio = lbUfficio.getItemText(selected);
            selectedDescriptionCE = "";
            selectedDescriptionGC = "";
            barViewImpl.getListener().ufficioSelected(currentlySelectedOwner);

        }

    }

    public void stopAnimation() {
        visualizzaButton.setStylePrimaryName(Resources.INSTANCE.mainStructure().buttonVisualizzaNavigation());

    }

}
