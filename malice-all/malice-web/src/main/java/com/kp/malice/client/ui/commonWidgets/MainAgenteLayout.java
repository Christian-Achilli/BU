package com.kp.malice.client.ui.commonWidgets;

import org.adamtacy.client.ui.effects.examples.Show;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.kp.malice.client.ui.resources.MaliceResources;

/**
 * Gestisce i tab non solo quelli principali (MALICE) ma anche i sottotab (tab che sono parte di uno di questi tab principali, ossia quelli che si vedono all'avvio dell'app)
 * Questa gestione a più livelli permette di mantenere la memoria di ciò che si stava facendo durante la navigazione.Es: sono in dettaglio titoli (sottotab di titoli) e clikko sul tab di chiusura, quando poi riclikko sul tab titoli voglio che mi sia presentato il dettaglio del titolo su cui stavo precedemente lavorando e non la lista titoli (che è la schermata di default del tab titoli)
 * 
 * @author dariobrambilla
 *
 */
public class MainAgenteLayout extends LayoutPanel {
    private SimplePanel toolbarsContainerPanel = new SimplePanel();
    //CHIUSURA
    private ScrollPanel dettaglioChiusuraTitoloScrollPanel = new ScrollPanel();

    //MALICEDISPLAYCONTAINER
    private TabLayoutPanel maliceDisplayContainer = new TabLayoutPanel(0, Unit.PX);
    private ScrollPanel selectAgenziaScrollPanel = new ScrollPanel();
    private ScrollPanel benvenutoScrollPanel = new ScrollPanel();
    private ScrollPanel titoliScrollPanel = new ScrollPanel();
    private ScrollPanel incassiScrollPanel = new ScrollPanel();
    private ScrollPanel chiusureScrollPanel = new ScrollPanel();
    private ScrollPanel statisticheScrollPanel = new ScrollPanel();

    private final static int SELECT_AGENZIA_TAB = 0;
    public final static int BENVENUTO_TAB = 1;
    private final static int TITOLI_TAB = 2;
    private final static int INCASSI_TAB = 3;
    private final static int CHIUSURE_TAB = 4;
    private final static int STATISTICHE_TAB = 5;

    private final class MaliceAcceptsOneWidget implements AcceptsOneWidget {

        SimplePanel panel;

        public MaliceAcceptsOneWidget(SimplePanel panel) {
            this.panel = panel;
        }

        @Override
        public void setWidget(IsWidget activityWidget) {
            Widget widget = Widget.asWidgetOrNull(activityWidget);
            panel.setVisible(widget != null);
            panel.setWidget(widget);
        }
    }

    private AcceptsOneWidget selectAgenziaDisplay = new MaliceAcceptsOneWidget(selectAgenziaScrollPanel);
    
    private AcceptsOneWidget benvenutoDisplay = new MaliceAcceptsOneWidget(benvenutoScrollPanel);

    private AcceptsOneWidget chiusureDisplay = new MaliceAcceptsOneWidget(chiusureScrollPanel);

    private AcceptsOneWidget titoliDisplay = new MaliceAcceptsOneWidget(titoliScrollPanel);

    private AcceptsOneWidget incassiDisplay = new MaliceAcceptsOneWidget(incassiScrollPanel);

    private AcceptsOneWidget statisticheDisplay = new MaliceAcceptsOneWidget(statisticheScrollPanel);

    private AcceptsOneWidget dettaglioChiusuraTitoloDisplay = new MaliceAcceptsOneWidget(
            dettaglioChiusuraTitoloScrollPanel);

    private AcceptsOneWidget toolbarsDisplay = new MaliceAcceptsOneWidget(toolbarsContainerPanel);

    public MainAgenteLayout() {
        setStylePrimaryName(MaliceResources.INSTANCE.main().agenteLayout());
        add(toolbarsContainerPanel);
        add(maliceDisplayContainer);
        //MALICEDISPLAYCONTAINER
        maliceDisplayContainer.setAnimationDuration(0); // in millisec
        maliceDisplayContainer.add(selectAgenziaScrollPanel);
        maliceDisplayContainer.add(benvenutoScrollPanel);
        maliceDisplayContainer.add(titoliScrollPanel);
        maliceDisplayContainer.add(incassiScrollPanel);
        maliceDisplayContainer.add(chiusureScrollPanel);
        maliceDisplayContainer.add(statisticheScrollPanel);
        maliceDisplayContainer.selectTab(BENVENUTO_TAB);
        setVisible(false);
    }

    public AcceptsOneWidget getSelectAgenziaDisplay() {
    	return selectAgenziaDisplay;
    }
    
    public AcceptsOneWidget getBenvenutoDisplay() {
        return benvenutoDisplay;
    }

    public AcceptsOneWidget getChiusureDisplay() {
        return chiusureDisplay;
    }

    public AcceptsOneWidget getStatisticheDisplay() {
        return statisticheDisplay;
    }

    public AcceptsOneWidget getIncassiDisplay() {
        return incassiDisplay;
    }

    public AcceptsOneWidget getTitoliDisplay() {
        return titoliDisplay;
    }

    public AcceptsOneWidget getToolbarsDisplay() {
        return toolbarsDisplay;
    }

    public AcceptsOneWidget getDettaglioChiusuraTitoloDisplay() {
        return dettaglioChiusuraTitoloDisplay;
    }

    public void visualizza() {
        Show show2 = new Show(getElement());
        show2.play();
        setVisible(true);
    }

    public boolean isBenvenutoTabSelected() {
        return maliceDisplayContainer.getSelectedIndex() == BENVENUTO_TAB;
    }

    public void selectSelectAgenzia() {
    	maliceDisplayContainer.selectTab(SELECT_AGENZIA_TAB);
    }
    
    public void selectBenvenuto() {
        maliceDisplayContainer.selectTab(BENVENUTO_TAB);
    }

    public void selectTitoli() {
        maliceDisplayContainer.selectTab(TITOLI_TAB);
    }

    public void selectIncassi() {
        maliceDisplayContainer.selectTab(INCASSI_TAB);
    }

    public void selectChiusure() {
        maliceDisplayContainer.selectTab(CHIUSURE_TAB);
    }

    public void selectStatistiche() {
        maliceDisplayContainer.selectTab(STATISTICHE_TAB);
    }

    public void setDettaglioChiusuraTitoloDisplay(AcceptsOneWidget dettaglioChiusuraTitoloDisplay) {
        this.dettaglioChiusuraTitoloDisplay = dettaglioChiusuraTitoloDisplay;
    }

}