package com.kp.malice.useCases;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.inject.Inject;
import com.kp.malice.entities.business.IncassoTitoloLloyds.TipoOperazione;
import com.kp.malice.entities.business.MaliceUserAuthenticated;
import com.kp.malice.entities.business.PuntoVenditaRMAPerLloyds;
import com.kp.malice.entities.miscellaneous.DettaglioIncassoTitoloLloyds;
import com.kp.malice.entities.miscellaneous.ScritturaContabileRma;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.Incs;

public class RecuperaScrittureContabiliRma {
	private static final Logger log = Logger.getLogger(RecuperaScrittureContabiliRma.class);
	private final DateFormat df = new SimpleDateFormat("yyyyMMdd");

	@Inject
	public RecuperaScrittureContabiliRma() {
	}

	public List<DettaglioIncassoTitoloLloyds> getListaDettaglioIncassiByScritturaContabile(ScritturaContabileRma scrittura) throws Exception {
		List<Long> idPuntiVnd = generaArrayIdPuntiVendita();
		log.debug("idPuntiVnd size: " + idPuntiVnd.size());
		List<DettaglioIncassoTitoloLloyds> dettaglioIncassiloydsList = generaDettaglioIncassiPerPuntoVendita(scrittura, idPuntiVnd);
		return dettaglioIncassiloydsList;
	}

	private List<Long> generaArrayIdPuntiVendita() {
		SecurityContext ctx = SecurityContextHolder.getContext();
		List<PuntoVenditaRMAPerLloyds> puntiVendita = ((MaliceUserAuthenticated) ctx.getAuthentication()).getAgenzia().getPuntiVendita();
		List<Long> idPuntiVnd = new ArrayList<Long>();
		for (PuntoVenditaRMAPerLloyds pv : puntiVendita) {
			idPuntiVnd.add(pv.getId());
		}
		return idPuntiVnd;
	}

	private List<DettaglioIncassoTitoloLloyds> generaDettaglioIncassiPerPuntoVendita(ScritturaContabileRma scrittura, List<Long> pvIds) throws Exception {
		try {
			@SuppressWarnings("unchecked")
			List<DettaglioIncassoTitoloLloyds> result = HibernateSessionFactoryUtil.getSession().createSQLQuery("select i.dataIncasso, i.codMzzPagm, i.importoIncasso, i.statoIncasso, i.tipoOperazione, p.companyName, p.firstName, p.lastName, p.gender, t.impTotNet+t.impTotAcc+t.impTotIpt as importoPremioEuroCent, t.impPvgIncAcc+t.impPvgIncNet as importoProvvigioniEuroCent, t.codPol, t.codPrgMov from TTL_CTB t, DLG_INC d, INCASSI i, MOV m, VRN_POL v, RAP_CNTN r, PSN p" + " where i.ID = d.INC_ID " + "and m.ID = t.mov_ID " + "and v.ID = m.vrnPol_ID " + "and v.ID = r.VRN_POL_ID " + "and p.ID = r.PSN_ID " + "and t.ID = d.TTL_ID and d.punVndDelegatoIncasso_ID in (:pvId) " + "and date_format(d.TMST_INS_RIG, '%Y%m%d') =  :dtRegistrazione_yyyymmdd " + "and i.statoIncasso = :statoIncasso and i.tipoOperazione = :tipoOperazione " + "and i.codMzzPagm = :mzzPag order by i.TMST_INS_RIG desc").setResultTransformer(Transformers.aliasToBean(DettaglioIncassoTitoloLloyds.class)).setParameterList("pvId", pvIds).setParameter("statoIncasso", scrittura.getStatoIncasso())
					.setParameter("mzzPag", scrittura.getCodMzzPagm()).setParameter("tipoOperazione", scrittura.getTipoOperazione()).setParameter("dtRegistrazione_yyyymmdd", df.format(scrittura.getTmstInsRig())).list();

			// scorro tutta la lista e se il tipo di operazione è storno metto
			// segno negativo all'importo delle provvigioni, idem per l'abbuono
			for (DettaglioIncassoTitoloLloyds dettaglioIncassoTitoloLloyds : result) {
				if (dettaglioIncassoTitoloLloyds.getTipoOperazione() == TipoOperazione.STORNO) {
					dettaglioIncassoTitoloLloyds.setImportoProvvigioniEuroCent(dettaglioIncassoTitoloLloyds.getImportoProvvigioniEuroCent().negate());
					dettaglioIncassoTitoloLloyds.setImportoAbbuonoEuroCent(dettaglioIncassoTitoloLloyds.getImportoAbbuonoEuroCent().negate());
				}
			}
			return result;
		} catch (Exception e) {
			log.error("ERRORE IN RECUPERO INCASSI SCRITTURA CONTABILE", e);
			throw e;
		}
	}

	public List<ScritturaContabileRma> getListaScrittureByDataRegistrazione(Date dataRegistrazione) throws Exception {
		List<Long> idPuntiVnd = generaArrayIdPuntiVendita();
		List<ScritturaContabileRma> scrittureConImportoIncassi = getScrittureConImportoIncassi(dataRegistrazione, idPuntiVnd);
		List<ScritturaContabileRma> scrittureConPremiEAbbuono = getScrittureConPremiEAbbuono(dataRegistrazione, idPuntiVnd);
		return mergeIncassiPremiEAbbuono(scrittureConImportoIncassi, scrittureConPremiEAbbuono);
	}

	private List<ScritturaContabileRma> mergeIncassiPremiEAbbuono(List<ScritturaContabileRma> scrittureConImportoIncassi, List<ScritturaContabileRma> scrittureConPremiEAbbuono) {
		for (ScritturaContabileRma tempDTO : scrittureConPremiEAbbuono) {
			int index = scrittureConImportoIncassi.indexOf(tempDTO);
			ScritturaContabileRma resultDto = scrittureConImportoIncassi.get(index);
			resultDto.setPremiEuroCent(resultDto.getPremiEuroCent().add(tempDTO.getPremiEuroCent()));
			resultDto.setProvvigioniEuroCent(TipoOperazione.STORNO.name().equals(tempDTO.getTipoOperazione()) ? resultDto.getProvvigioniEuroCent().add(tempDTO.getProvvigioniEuroCent().negate()) : resultDto.getProvvigioniEuroCent().add(tempDTO.getProvvigioniEuroCent()));
		}
		return scrittureConImportoIncassi;
	}

	private List<ScritturaContabileRma> getScrittureConPremiEAbbuono(Date dataRegistrazione, List<Long> pvIds) throws Exception {
		try {
			@SuppressWarnings("unchecked")
			List<ScritturaContabileRma> listaScrittureContabiliTitoli = HibernateSessionFactoryUtil.getSession().createSQLQuery("select sum(t.impTotAcc+t.impTotIpt+t.impTotNet) premiEuroCent," + "    sum(t.impPvgIncNet+t.impPvgIncAcc) provvigioniEuroCent,    " + "    i.statoIncasso, i.tipoOperazione, i.codMzzPagm " + "from TTL_CTB t, DLG_INC d, INCASSI i " + "where i.ID = d.INC_ID " + "and t.id = d.TTL_ID and d.punVndDelegatoIncasso_ID in (:pvId) " + "and date_format(d.TMST_INS_RIG, '%Y%m%d') =  :dtRegistrazione_yyyymmdd " + "group by i.statoIncasso, i.tipoOperazione, i.codMzzPagm").setResultTransformer(Transformers.aliasToBean(ScritturaContabileRma.class)).setParameterList("pvId", pvIds).setParameter("dtRegistrazione_yyyymmdd", df.format(dataRegistrazione)).list();
			return listaScrittureContabiliTitoli;
		} catch (Exception e) {
			log.error("ERRORE IN RECUPERO SCRITTURE CONTABILI DA TITOLI", e);
			throw e;
		}
	}

	private List<ScritturaContabileRma> getScrittureConImportoIncassi(Date dataRegistrazione, List<Long> pvIds) throws Exception {
		try {
			@SuppressWarnings("unchecked")
			List<ScritturaContabileRma> listaScrittureContabili = HibernateSessionFactoryUtil.getSession().createSQLQuery("select count(*) counter, i.statoIncasso statoIncasso, i.tipoOperazione tipoOperazione, " + "i.codMzzPagm codMzzPagm, sum(importoIncasso) importiEuroCent" + " from DLG_INC d , INCASSI i where i.ID = d.INC_ID " + "and d.punVndDelegatoIncasso_ID in (:pvId) " + "and date_format(d.TMST_INS_RIG, '%Y%m%d') = :dtRegistrazione_yyyymmdd " + "group by statoIncasso, tipoOperazione, codMzzPagm").setResultTransformer(Transformers.aliasToBean(ScritturaContabileRma.class)).setParameterList("pvId", pvIds).setParameter("dtRegistrazione_yyyymmdd", df.format(dataRegistrazione)).list();
			for (ScritturaContabileRma tempDTO : listaScrittureContabili) {
				tempDTO.setTmstInsRig(dataRegistrazione);
			}
			return listaScrittureContabili;
		} catch (Exception e) {
			log.error("ERRORE IN RECUPERO SCRITTURE CONTABILI DA INCASSI", e);
			throw e;
		}
	}
}
