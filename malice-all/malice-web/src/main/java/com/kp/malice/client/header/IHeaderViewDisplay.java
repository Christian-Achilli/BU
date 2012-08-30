/**
 * 
 */
package com.kp.malice.client.header;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

public interface IHeaderViewDisplay extends IsWidget {

    /**
     * Metodi dell'activity resi accessibili al display
     */
    public interface Listener {

        public void logout();
//
//        public void filtraPolizze(String value);
//
//        public void rimuoviFiltri();
//
//        public void ricaricaListaPolizze(Date value, Date value2);
//
//        public void ricaricaStatistiche(Date value, Date value2);
//
//        public void visualizzaProvvigioni();
//
//        public void visualizzaMontanti();

    }

    /**
     * @param listener l'activity collegato al display
     */
    public void setListener(Listener listener);

    /**
     * Set the user name in the top right label next to logout
     * @param userName
     */
    public void setLabelUtenteSistema(String userName);

    // dati visualizzati sulla ricerca toolbar
    /**
     * @param value valore per la label totali elemnti in tabella
     */
    public void setLabelTotaleElementiTrovatiRicerca(String value);

//    void setListaAutocompletamentoInputRicerca(List<String> listDistinct);

    // impostazione valori sulle toolbar

//    public void setApriDettaglioTitolo(String dettaglioTitolo);

}
