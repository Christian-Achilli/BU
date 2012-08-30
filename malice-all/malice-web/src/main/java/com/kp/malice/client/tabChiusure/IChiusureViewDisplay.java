package com.kp.malice.client.tabChiusure;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.kp.malice.shared.proxies.ChiusuraLioProxy;
import com.kp.malice.shared.proxies.EstrattoContoLioProxy;
import com.kp.malice.shared.proxies.IncassoTitoloProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;

public interface IChiusureViewDisplay extends IsWidget {

    public interface Listener {

        public void chiudiMese(String nota);

        public void caricaEstrattiConto(ChiusuraLioProxy chiusuraProxy);

        public void caricaTitoli(EstrattoContoLioProxy estrattoContoLioProxySelezionato);

        public void downloadEstrattoConto();

        public void downloadTitoli();

        public void downloadChiusure();

        public void downloadDettaglioTitolo();

        public void caricaDettaglioTitolo(NewTitoloProxy titoloProxy);

    }

    public void initChiusureChiusure(ChiusuraLioProxy chiusuraProxy);

    public void setListener(Listener listener);

    public HasData<ChiusuraLioProxy> getTabellaChiusure();

    public void visualizzaEstrattiConto(ChiusuraLioProxy chiusuraProxy);

    public HasData<EstrattoContoLioProxy> getTabellaEstrattiConto();

    public HasData<NewTitoloProxy> getTabellaTitoli();

    public void visualizzaTitoli(EstrattoContoLioProxy estrattoContoLioProxySelezionato, String string);

    public HasData<IncassoTitoloProxy> getTabellaIncassi();

    public void visualizzaDettaglio(NewTitoloProxy titoloProxy, String meseChiusura, EstrattoContoLioProxy estrattoContoLioProxySelezionato);

}
