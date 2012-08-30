/**
 * 
 */
package com.kp.malice.client.tabTitoli;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.kp.malice.shared.MezzoPagamento;
import com.kp.malice.shared.StatoIncasso;
import com.kp.malice.shared.StatusTitolo;
import com.kp.malice.shared.proxies.IncassoTitoloProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public interface ITitoliViewDisplay extends IsWidget {

    public interface Listener {

        /************ LISTA *************/
        void ricaricaListaTitoli(Date startDate, Date endDate);

        void filtraTitoli(String key, List<StatusTitolo> statoTitolo, Boolean sospesi);

        void downloadListaTitoli();

        /************ DETTAGLIO *************/
        void onSelezioneTitolo(NewTitoloProxy titoloProxy);

        void incassaTitolo(BigDecimal importoIncassoEuroCent, StatoIncasso tipoIncasso, MezzoPagamento mezzoPagamento,
                Date dataIncasso);

        void annullaTitolo(String notaDiAnnullo);

        void revocaAnnulloTitolo();

        void stornaTitolo();

        void saveDettaglioTitolo(String subAgente, String codCig);

        void recuperaTitoloSospeso(Date datatIncasso, BigDecimal movePointRight, MezzoPagamento mezzoPagamento);

        /************ INSERIMENTO  *************/
        void onCreateNewTitolo();
    }

    void setListener(Listener listener);

    public HasData<NewTitoloProxy> getTabellaTitoli();

    public HasData<IncassoTitoloProxy> getTabellaIncassi();

    void setComparatorsAndSortHandler(ListHandler<NewTitoloProxy> listHandler);

    void setTitoliTrovati(int size);

    void visualizzaDettaglio(NewTitoloProxy titoloProxy);

    void populateDettaglioTitolo(NewTitoloProxy newTitoloProxy);

    void resetFiltri();

    //    void setIncassiComparatorsAndSortHandler(ListHandler<IncassoTitoloProxy> incassiSortHandler);

}
