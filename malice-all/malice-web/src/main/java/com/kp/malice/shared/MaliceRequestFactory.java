package com.kp.malice.shared;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.kp.malice.boundaries.services.PortaleServiceBoundary;
import com.kp.malice.server.inject.InjectingServiceLocator;
import com.kp.malice.shared.proxies.AgencyProxy;
import com.kp.malice.shared.proxies.ChiusuraLioProxy;
import com.kp.malice.shared.proxies.DettaglioIncassoTitoloProxy;
import com.kp.malice.shared.proxies.EstrattoContoLioProxy;
import com.kp.malice.shared.proxies.GrafiDataProxy;
import com.kp.malice.shared.proxies.LioReferenceCodeProxy;
import com.kp.malice.shared.proxies.NewTitoloProxy;
import com.kp.malice.shared.proxies.ScritturaContabileProxy;
import com.kp.malice.shared.proxies.WelcomeInfoProxy;

public interface MaliceRequestFactory extends RequestFactory {
    @Service(value = PortaleServiceBoundary.class, locator = InjectingServiceLocator.class)
    public interface ServiziPortale extends RequestContext {

        Request<List<NewTitoloProxy>> findAllTitoliInPeriodoInizioCopertura(Date startDate, Date endDate);

        Request<NewTitoloProxy> findTitolo(long id);

        Request<Void> annulloTitolo(NewTitoloProxy titolo, Date dataDiAnnullo, String notaDiAnnullo);

        Request<Void> revocaAnnulloTitolo(NewTitoloProxy titolo);

        Request<Void> incassaTitolo(NewTitoloProxy titolo, DettaglioIncassoTitoloProxy incasso);

        Request<Void> stornaIncassoTitolo(NewTitoloProxy titolo);

        Request<Void> aggiornaDatiTitolo(NewTitoloProxy titolo);

        Request<Void> recuperaTitoloSospeso(NewTitoloProxy titolo, BigDecimal sommaRecuperataEuroCent,
                Date datatIncasso, String mezzoPagamentoString);

        Request<List<ScritturaContabileProxy>> findListByDataRegistrazioneIncasso(Date d);

        Request<List<DettaglioIncassoTitoloProxy>> findListIncassiAggregati(ScritturaContabileProxy scritturaContabileProxy);

        Request<List<ChiusuraLioProxy>> getListaChiusure();

        Request<List<EstrattoContoLioProxy>> getListaEstrattiConto(ChiusuraLioProxy c);

        Request<List<NewTitoloProxy>> getListaTitoliInEstrattoConto(EstrattoContoLioProxy e);

        Request<Void> inviaChiusura(String nota);

        Request<WelcomeInfoProxy> getWelcomeInfo();

		Request<GrafiDataProxy> calculateDataGrafi(Date start, Date end, String broker);

		Request<List<LioReferenceCodeProxy>> getLioReferenceCodeProxyList();

		Request<List<AgencyProxy>> findAllAgenzie();

    }

    ServiziPortale serviziPortale();

}
