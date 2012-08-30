package com.kp.marsh.ebt.client.webapp.ui.activityintf;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.IsWidget;
import com.kp.marsh.ebt.shared.dto.LightInfoOwnerDto;
import com.kp.marsh.ebt.shared.dto.LineOfBusiness;

public interface IHistogramsViewDisplay extends IsWidget {

    void setListener(IHistogramsViewDisplay.Listener listener);

    public interface Listener {

        /**
         * Usato quando si sfogliano le pagine
         * @return The list refers to clients. 
         */
        void getNextPageData();

        void getPreviousPageData();

    }

    /**
     * INserisce il relativo lighInfoOwner alla riga successiva della flex table: la ui sa qual'è. Così posso caricare un elemento alla volta e non aspettare che siano tutti calcolati
     * @param pageValues
     * @param rowNum
     */
    void appendPageData(LightInfoOwnerDto... pageValues);

    /**
     * Srive i nomi delle lob in alto sulla pagina. La pagina è quella corrente
     * @param lobsInPage
     */
    void setLobHeadline(ArrayList<LineOfBusiness> lobsInPage);

    /**
     * @param currentPage l'indice della pagina che si sta visualizzando. La prima pagina ha indice 0.
     * @param totPages il numero totale delle pagine
     */
    void setPageIndexes(int totPages, int currentPage);

    void showLoader(boolean show, String message);

    /**
     * Resetta la view
     */
    void initView();

    //<<<<<<< HEAD
    //=======
    //	void hideLoader();

    void setStatusBar(String officeName, String ceName, String gcName);

    //>>>>>>> d333579099217f0d96762d40588310d3f9423bee
}