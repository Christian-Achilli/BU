package com.kp.marsh.ebt.client.webapp.ui.activityintf;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.kp.marsh.ebt.shared.dto.LineOfBusiness;
import com.kp.marsh.ebt.shared.dto.MarshClientDTO;
import com.kp.marsh.ebt.shared.dto.ProductInfoDTO;
import com.kp.marsh.ebt.shared.dto.ProductTotalsManager;
import com.kp.marsh.ebt.shared.dto.RowTotalsManager;

public interface IInsertPotentialViewDisplay extends IsWidget {

    public void setListener(Listener listener);

    public interface Listener {

        void onInsertPopUpClose();

        /**
         * @param clientId l'id su information owner.
         */
        void disableClient(int clientId);

        ProductTotalsManager getTotalsManager();

        /**
         * Usato quando si sfogliano le pagine
         */
        void goToPage(int page);

        /**
         * C'è un mischione tra view e activity. Per fare in fretta implemto questo metodo. Viene invocato dopo che la view ha aggiornato il total manager in modo che la activity lanci l'evento di aggiornamento dei totali di colonna sulla UI.
         */
        void inizializzaTotaliColonna();

    }

    /**
     * Reset the view state
     */
    void initView();

    void initPill();// init the pill after an update being triggered

    void configureLobHeadlines(ArrayList<ArrayList<LineOfBusiness>> lobByPageList); // prepare the tables with lob headlines

    /**
     * @param clientByCEList
     * @param lobByPageList
     */
    void configureClientTables(List<MarshClientDTO> clientByCEList, ArrayList<ArrayList<LineOfBusiness>> lobByPageList);// configures clients on the page. The given list contains a products map and the available value for the given product.

    ProductInfoDTO getClickedPillDTO();

    /**
     * Updates the totals of the row element
     * @param rowTotalsManager
     */
    void initRowTotals(RowTotalsManager rowTotalsManager);

    void rollback(); // rolls back to previous value for the clicked pill

    //	void updateTotalsAfterCommit(ProductInfoDTO prodInfo, ArrayList<ArrayList<LineOfBusiness>> lobByPageList);

    void showLoader(boolean b);

    public void refreshRowTotals(RowTotalsManager updatedClientTotManager);
}