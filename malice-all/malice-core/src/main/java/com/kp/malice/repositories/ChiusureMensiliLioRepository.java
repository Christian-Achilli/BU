package com.kp.malice.repositories;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.kp.malice.entities.business.AgenziaRMA;
import com.kp.malice.entities.business.ChiusuraMensileLio;
import com.kp.malice.entities.business.ChiusuraMensileLio.StatoChiusura;
import com.kp.malice.entities.business.EstrattoContoLio;
import com.kp.malice.entities.business.LioReferenceCode;
import com.kp.malice.entities.business.PuntoVenditaRMAPerLloyds;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.entities.persisted.ChrMslLio;
import com.kp.malice.entities.persisted.EstrCntTtl;
import com.kp.malice.entities.persisted.EstrContLio;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.PunVnd;
import com.kp.malice.entities.persisted.RecordIdentifier;
import com.kp.malice.entities.persisted.TtlCtb;
import com.kp.malice.factories.ChiusuraMensileLioFactory;
import com.kp.malice.factories.TitoloLloydsFactory;

public class ChiusureMensiliLioRepository {

	private final ChiusuraMensileLioFactory chrFactory;
	private final TitoloLloydsFactory titoloFactory;
	private Logger log = Logger.getLogger(ChiusureMensiliLioRepository.class);

	@Inject
	public ChiusureMensiliLioRepository(ChiusuraMensileLioFactory chrFactory,
			TitoloLloydsFactory titoloFactory) {
		this.chrFactory = chrFactory;
		this.titoloFactory = titoloFactory;
	}

	public ChiusuraMensileLio getChiusuraMeseX(AgenziaRMA age, int year,
			int month) throws Exception {
		Query q = HibernateSessionFactoryUtil.getSession().getNamedQuery(
				"chr.perMesePerAgenzia");
		q.setParameter("uoaId", age.getId());
		q.setParameter("annoRif", year);
		q.setParameter("meseRif", month);
		ChrMslLio chr = (ChrMslLio) q.uniqueResult();
		return chr != null ? chrFactory.hidrateChiusuraMensile(chr) : null;
	}

	public ChiusuraMensileLio getChiusuraById(String id) throws Exception {
		ChrMslLio chrPersisted = HibernateSessionFactoryUtil
				.getPersistedInstance(ChrMslLio.class, Long.valueOf(id));
		ChiusuraMensileLio result = chrFactory
				.hidrateChiusuraMensile(chrPersisted);
		if (null != result)
			return result;
		else
			return null;
	}

	public void saveNuovaChiusuraMensile(ChiusuraMensileLio meseX)
			throws Exception {
		ChrMslLio chr = chrFactory.getNotPersistedObject(meseX);
		HibernateSessionFactoryUtil.getSession().save(chr);
		for (PuntoVenditaRMAPerLloyds pv : meseX.getAgenzia().getPuntiVendita()) {
			saveEstrattoConto(chr, pv, pv.getBroker().getLioReferenceCode());
		}
	}

	private void saveEstrattoConto(ChrMslLio chr, PuntoVenditaRMAPerLloyds pv,
			LioReferenceCode lrc) throws Exception {
		PunVnd punVnd = (PunVnd) HibernateSessionFactoryUtil
				.getPersistedInstance(PunVnd.class, pv.getId());
		EstrContLio ecPersisted = chrFactory.getNotPersistedObject(punVnd, chr,
				lrc);
		HibernateSessionFactoryUtil.getSession().save(ecPersisted);
	}

	public ChiusuraMensileLio getNextToBeSent(AgenziaRMA age) throws Exception {
		List<ChiusuraMensileLio> chiusure = getListaChiusure(age);
		for (ChiusuraMensileLio chiusura : chiusure) {
			if (chiusura.isNextToBeSent())
				return chiusura;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<ChiusuraMensileLio> getListaChiusure(AgenziaRMA age)
			throws Exception {
		Query q = HibernateSessionFactoryUtil.getSession().getNamedQuery(
				"chr.tuttePerAgenzia");
		q.setParameter("uoaId", age.getId());
		List<ChrMslLio> chrList = q.list();
		List<ChiusuraMensileLio> result = new ArrayList<ChiusuraMensileLio>(0);
		for (ChrMslLio chrMslLio : chrList) {
			result.add(chrFactory.hidrateChiusuraMensile(chrMslLio));
		}
		impostaNextToBeSent(result);
		return result;
	}

	private void impostaNextToBeSent(List<ChiusuraMensileLio> result) {
		ChiusuraMensileLio nextToBeSent = null;
		DateTime oldestPeriod = null;
		for (ChiusuraMensileLio chiusura : result) {
			if (StatoChiusura.APERTA == chiusura.getStatoChiusura())
				if (null == oldestPeriod
						|| oldestPeriod.isAfter(chiusura.getPeriodo())) {
					if (nextToBeSent != null)
						nextToBeSent.setNextToBeSent(false);
					nextToBeSent = chiusura;
					nextToBeSent.setNextToBeSent(true);
					oldestPeriod = chiusura.getPeriodo();
				}
		}
	}

	public List<EstrattoContoLio> getListaEstrattiConto(ChiusuraMensileLio c)
			throws Exception {
		ChrMslLio chr = HibernateSessionFactoryUtil.getPersistedInstance(
				ChrMslLio.class, c.getId());
		List<EstrattoContoLio> result = new ArrayList<EstrattoContoLio>(0);
		for (EstrContLio ec : chr.getEstrattiConto()) {
			result.add(chrFactory.hidrateEstrattoContoLio(ec));
		}
		return result;
	}

	public List<TitoloLloyds> getListaTitoliPerEstrattoConto(EstrattoContoLio ec)
			throws Exception {
		EstrContLio ecl = HibernateSessionFactoryUtil.getPersistedInstance(
				EstrContLio.class, ec.getId());
		List<TitoloLloyds> result = new ArrayList<TitoloLloyds>(0);
		for (EstrCntTtl ttlEC : ecl.getTitoliEstratto()) {
			TtlCtb ttl = ttlEC.getTitolo();
			result.add(titoloFactory.makeTitoloLloyds(ttl));
		}
		return result;
	}

	public void addToEstrattoConto(TitoloLloyds titolo,
			ChiusuraMensileLio chiusura) throws Exception {
		log.debug("addToEstrattoConto start");
		Query q = HibernateSessionFactoryUtil
				.getSession()
				.getNamedQuery("ec.perLioCode")
				.setParameter("chId", chiusura.getId())
				.setParameter("lioCode",
						titolo.getLioReferenceCode().getCodice());
		EstrContLio ec = (EstrContLio) q.uniqueResult();
		ChrMslLio chr = HibernateSessionFactoryUtil.getPersistedInstance(
				ChrMslLio.class, chiusura.getId());
		if (null == ec) {
			saveEstrattoConto(chr, titolo.getCertificatoLloyds()
					.getPuntoVendita(), titolo.getLioReferenceCode());
			ec = (EstrContLio) q.uniqueResult();
		}
		TtlCtb ttl = HibernateSessionFactoryUtil.getPersistedInstance(
				TtlCtb.class, titolo.getId());
		EstrCntTtl joinTtlEc = new EstrCntTtl(RecordIdentifier.getInitRecord(),
				ttl, ec);
		ec.add(ttl);
		chr.add(ttl);
		HibernateSessionFactoryUtil.getSession().saveOrUpdate(joinTtlEc);
		HibernateSessionFactoryUtil.getSession().saveOrUpdate(chr);
		HibernateSessionFactoryUtil.getSession().saveOrUpdate(ec);
	}

	public void removeFromEstrattoContoIfAttached(TitoloLloyds titolo) {
		Query q = HibernateSessionFactoryUtil.getSession().getNamedQuery(
				"ec.trovaEcPerTitolo");
		q.setParameter("ttlId", titolo.getId());
		EstrContLio ec = (EstrContLio) q.uniqueResult();
		TtlCtb ttl = HibernateSessionFactoryUtil.getPersistedInstance(
				TtlCtb.class, titolo.getId());
		ec.remove(ttl);
		ChrMslLio chr = ec.getChiusura();
		chr.remove(ttl);
		Criteria c = HibernateSessionFactoryUtil.getSession()
				.createCriteria(EstrCntTtl.class)
				.add(Restrictions.eq("titolo", ttl));
		EstrCntTtl joinRecord = (EstrCntTtl) c.uniqueResult();
		HibernateSessionFactoryUtil.getSession().delete(joinRecord);
		if (ec.mustBeDeletedIfEmpty()) {
			HibernateSessionFactoryUtil.getSession().delete(ec);
		} else {
			HibernateSessionFactoryUtil.getSession().saveOrUpdate(ec);
		}
		HibernateSessionFactoryUtil.getSession().saveOrUpdate(chr);
	}

	public ChiusuraMensileLio creaNuovaChiusuraAperta(AgenziaRMA age,
			DateTime mdt) throws Exception {
		ChiusuraMensileLio nuovaNextToGo;
		nuovaNextToGo = chrFactory.create(age, mdt.getYear(),
				mdt.getMonthOfYear());
		nuovaNextToGo.setStatoChiusura(StatoChiusura.APERTA);
		nuovaNextToGo.setNextToBeSent(false);
		saveNuovaChiusuraMensile(nuovaNextToGo);
		return nuovaNextToGo;
	}
}
