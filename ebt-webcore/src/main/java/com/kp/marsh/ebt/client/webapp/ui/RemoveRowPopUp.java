package com.kp.marsh.ebt.client.webapp.ui;

import org.adamtacy.client.ui.effects.examples.Fade;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.kp.marsh.ebt.client.webapp.ui.resources.Resources;

// popup per la conferma dell'eliminazione cliente
public class RemoveRowPopUp extends DialogBox implements ClickHandler {
    Button confermaButton;
    Button annullaButton;
    RowElementsTable rowElementsTable;

    public RemoveRowPopUp(RowElementsTable rowElementsTable) {
        this.rowElementsTable = rowElementsTable;
        VerticalPanel dialogContents = new VerticalPanel();
        setWidget(dialogContents);
        HTML details = new HTML("<br/><br/><br/><p>CONFERMARE ELIMINAZIONE CLIENTE<p><br/><br/>");
        dialogContents.add(details);
        dialogContents.setCellHorizontalAlignment(details, HasHorizontalAlignment.ALIGN_CENTER);
        dialogContents.setCellWidth(details, "400px");
        confermaButton = new Button("CONFERMA");
        annullaButton = new Button("ANNULLA");
        confermaButton.setStyleName(Resources.INSTANCE.mainStructure().buttonDialogBox());
        annullaButton.setStyleName(Resources.INSTANCE.mainStructure().buttonDialogBox());
        annullaButton.addDomHandler(this, ClickEvent.getType());
        confermaButton.addDomHandler(this, ClickEvent.getType());
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.add(annullaButton);
        horizontalPanel.add(confermaButton);
        horizontalPanel.setCellHorizontalAlignment(annullaButton, HasHorizontalAlignment.ALIGN_LEFT);
        horizontalPanel.setCellHorizontalAlignment(confermaButton, HasHorizontalAlignment.ALIGN_RIGHT);
        horizontalPanel.setWidth("50%");
        dialogContents.add(horizontalPanel);
        dialogContents.setCellHorizontalAlignment(horizontalPanel, HasHorizontalAlignment.ALIGN_CENTER);
        setAnimationEnabled(true);
        setStyleName(Resources.INSTANCE.mainStructure().dialogBox());
        this.hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(confermaButton)) {
            hide();
            Fade eff = new Fade();
            eff.setEffectElement(rowElementsTable.getElement());
            eff.play();
            rowElementsTable.setVisible(false);
            rowElementsTable.rowContainer.remove(rowElementsTable);
        } else if (event.getSource().equals(annullaButton)) {
            hide();
        }
        rowElementsTable.setTrashVisibleInit();
    }
}
