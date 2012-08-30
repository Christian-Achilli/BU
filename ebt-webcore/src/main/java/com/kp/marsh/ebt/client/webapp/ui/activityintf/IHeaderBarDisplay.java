/**
 * 
 */
package com.kp.marsh.ebt.client.webapp.ui.activityintf;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.IsWidget;
import com.kp.marsh.ebt.shared.dto.LineOfBusiness;
import com.kp.marsh.ebt.shared.dto.NavigationDTO;
import com.kp.marsh.ebt.shared.dto.OwnerType;
import com.kp.marsh.ebt.shared.dto.ProductTotalsManager;

/**
 * Barra delle applicazioni per i manager
 */
public interface IHeaderBarDisplay extends IsWidget {

    /**
     * @param listener l'activity collegato al display
     */
    public void setListener(Listener listener);

    /**
     * Metodi dell'activity resi accessibili al display
     */
    public interface Listener {

        /**
         * Dice al presenter che location è stata selezionata nella listBox. Il presenter prepara e invia un LocationSelectedEvent
         * @param locationId l'information owner id di un Ufficio o un CE o un GC
         * @param selectedDescriptionGC  descrizione
         * @param selectedDescriptionCE 
         * @param selectedDescriptionUfficio 
         */
        public void locationIsSelected(String locationId, OwnerType informationOwnersValueType,
                String selectedDescriptionUfficio, String selectedDescriptionCE, String selectedDescriptionGC);

        public void logout();

        public void switchToGauges();

        public void switchToHistograms();

        public void switchToPills();

        public void inizializzaComboUffici();

        public void ufficioSelected(int selected);

        public void ceSelected(int id);

    }

    public void setUserName(String displayName);

    public void showLoader(boolean b);

    /**
     * Prepare the tables with lob sub totals
     * @param lobByPageList
     */
    void configureSubTotals(ArrayList<ArrayList<LineOfBusiness>> lobByPageList);

    /**
     * Imposta il totale per colonna del potenziale accanto al radio button nella tabella a sconparsa dei totali di colonna
     * @param newTotal
     */
    void setPotentialTotal(int newTotal);

    /**
     * Imposta il totale per colonna dell'achieved accanto al radio button nella tabella a sconparsa dei totali di colonna
     * @param totAchCe
     */
    void setAchievedTotal(int totAchCe);

    /**
     * Refresh the total of the products retrieving the info from the total manager according to what type of info is selected(achieved potential or actual)
     * This method has to be called when a potential is inserted or modified or when a total type get clicked.
     */
    public void refreshTotals(ProductTotalsManager totalsManager);

    /**
     * @param pageIndex starts from 0
     */
    public void goToPage(int pageIndex);

    /**
     * Inizializza le funzionalità della barra per un manager.
     * Reset della vista dei totali di colonna
     */
    void initManagerIcons();

    /**
     * Inizializza le funzionalità della barra per un manager con clienti.
     * Reset della vista dei totali di colonna
     */
    void initManagerWithClientsIcons();

    /**
     * Inizializza le funzionalità della barra per un client executive.
     * Reset della vista dei totali di colonna
     */
    void initCEIcons();

    /**
     * Popola combo navigazione uffici con la lista di NavigationDTO passata
     * @param result
     */
    public void updateUff(ArrayList<NavigationDTO> result);

    /**
     * Popola combo navigazione CE con la lista di NavigationDTO passata
     * @param result la lista di CE
     */
    public void updateCE(ArrayList<NavigationDTO> result);

    /**
     * Popola combo navigazione GC con la lista di NavigationDTO passata
     * @param result la lista di GC
     */
    public void updateGC(ArrayList<NavigationDTO> result);

    /**
     * Inizializza le combo della navigation box con l'ufficio del manager che si è loggato
     * @param loggedUserOfficeId
     */
    public void initUserOffice(String loggedUserOfficeId);

    /**
     * ferma il bottone aggiorna
     */
    public void stopAnimation();

}
