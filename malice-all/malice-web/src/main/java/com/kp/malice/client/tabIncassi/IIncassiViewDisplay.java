package com.kp.malice.client.tabIncassi;

import java.util.Date;

import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.kp.malice.shared.proxies.DettaglioIncassoTitoloProxy;
import com.kp.malice.shared.proxies.IncassoTitoloProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;
import com.kp.malice.shared.proxies.ScritturaContabileProxy;

public interface IIncassiViewDisplay extends IsWidget {

    public interface Listener {
        void loadScritturaContabileByDay(Date date);

        void loadIncassiAggregati(ScritturaContabileProxy scritturaContabileProxy);

		void downloadScrittureContabili(Date date);

        void caricaTitoloDettaglio(IncassoTitoloProxy incassoTitoloProxy);
    }

    void visualizzaErroreOnScritturaContabileWidget(String error);

    HasData<ScritturaContabileProxy> getTabellaScritturaContabile();

    void setListener(IIncassiViewDisplay.Listener listner);

    HasData<DettaglioIncassoTitoloProxy> getTabellaIncassiAggregati();

    void visualizeIncassiAggregati(ScritturaContabileProxy scritturaContabileProxy);

    void visualizzaMsgNoIncassi();

    void visualizzaListaScrittureContabili();

    void setComparatorsAndSortHandler(ListHandler<ScritturaContabileProxy> sortHandler);

    void visualizzaDettaglio(NewTitoloProxy updatedtitoloProxy);

}
