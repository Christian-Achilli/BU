package com.kp.malice.useCases;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.inject.Inject;
import com.kp.malice.entities.business.AgenziaRMA;
import com.kp.malice.entities.business.ChiusuraMensileLio;
import com.kp.malice.entities.business.EstrattoContoLio;
import com.kp.malice.entities.business.GrafiData;
import com.kp.malice.entities.persisted.ChrMslLio;
import com.kp.malice.entities.persisted.EstrContLio;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.UntaOperAun;
import com.kp.malice.factories.ChiusuraMensileLioFactory;
import com.kp.malice.factories.EstrattoContoFactory;

public class FunzioniStatistiche {

	private final int RAPPORTO_CENTESIMI_MIGLIAIA = 100;

	private static Logger log = Logger.getLogger(FunzioniStatistiche.class);
	private ChiusuraMensileLioFactory chiusuraMensileLioFactory;
	private EstrattoContoFactory estrattoContoFactory;

	public class EstrattoContoLioTimeReferences {
		private EstrattoContoLio estrattoContoLio;
		private int referenceCalendarMonth;
		private int referenceYear;

		public int getReferenceCalendarMonth() {
			return referenceCalendarMonth;
		}

		public EstrattoContoLio getEstrattoContoLio() {
			return estrattoContoLio;
		}

		public void setEstrattoContoLio(EstrattoContoLio estrattoContoLio) {
			this.estrattoContoLio = estrattoContoLio;
		}

		public void setReferenceCalendarMonth(int referenceCalendarMonth) {
			this.referenceCalendarMonth = referenceCalendarMonth;
		}

		public int getReferenceYear() {
			return referenceYear;
		}

		public void setReferenceYear(int referenceYear) {
			this.referenceYear = referenceYear;
		}
	}

	@Inject
	public FunzioniStatistiche(ChiusuraMensileLioFactory chiusuraMensileLioFactory, EstrattoContoFactory estrattoContoFactory) {
		this.chiusuraMensileLioFactory = chiusuraMensileLioFactory;
		this.estrattoContoFactory = estrattoContoFactory;
	}

	public GrafiData calculateDataGrafi(AgenziaRMA age, Date start, Date end, String lioRefCode) throws Exception {
		log.debug("calculateDataGrafi da: " + start + " a: " + end + " id untaOperAun: " + age.getId() + " broker: " + lioRefCode);
		GrafiData grafiData = new GrafiData();
		String jsonMontanti = "";
		String jsonPremiProvvigioni = "";
		int months = getMonthsBetweenTwoDate(start, end);
		Session session = HibernateSessionFactoryUtil.getSession();
		Criteria criteria = session.createCriteria(ChrMslLio.class);
		criteria.add(Restrictions.eq("untaOperAun", session.load(UntaOperAun.class, age.getId())));
		criteria.add(Restrictions.between("dtInvio", start, end));
		criteria.addOrder(Order.asc("dtInvio"));
		List<ChrMslLio> chrMslLios = criteria.list();
		if (lioRefCode.equals("*")) {
			log.trace("l'utente ha selezionato tutti i broker");
			log.debug("FunzioniStatistiche.calculateDataGrafi found: " + chrMslLios.size() + " ChrMslLio");
			HibernateSessionFactoryUtil.commitTransaction();
			List<ChiusuraMensileLio> chiusuraMensileLios = new ArrayList<ChiusuraMensileLio>();
			for (ChrMslLio chrMslLio : chrMslLios) {
				chiusuraMensileLios.add(chiusuraMensileLioFactory.hidrateChiusuraMensile(chrMslLio));
			}
			log.debug("idratate " + chiusuraMensileLios.size() + " chiusure mensile lio");
			jsonMontanti = creaJsonMontantiFromListaChiusure(start, end, chiusuraMensileLios);
			jsonPremiProvvigioni = calcolaJsonPremiProvvigioni(start, end, chiusuraMensileLios);
		} else {
			log.trace("l'utente ha selezionato il broker " + lioRefCode + " quindi creo i dati per i grafi partendo dagli estratti conto del broker");
			List<EstrattoContoLioTimeReferences> estrContLiosTimeReferences = new ArrayList<EstrattoContoLioTimeReferences>();
			for (ChrMslLio chrMslLio : chrMslLios) {
				Set<EstrContLio> estrContLiosTmp = chrMslLio.getEstrattiConto();
				for (EstrContLio estrContLio : estrContLiosTmp) {
					if (estrContLio.getLioReferenceCodeString().equals(lioRefCode)) {
						EstrattoContoLioTimeReferences estrattoContoLioTimeReferences = new EstrattoContoLioTimeReferences();
						estrattoContoLioTimeReferences.setEstrattoContoLio(chiusuraMensileLioFactory.hidrateEstrattoContoLio(estrContLio));
						estrattoContoLioTimeReferences.setReferenceYear(chrMslLio.getAnnoDiRiferimento());
						estrattoContoLioTimeReferences.setReferenceCalendarMonth(chrMslLio.getMeseDiRiferimento());
						estrContLiosTimeReferences.add(estrattoContoLioTimeReferences);
					}
				}
			}
			log.debug("FunzioniStatistiche.calculateDataGrafi found: " + estrContLiosTimeReferences.size() + " EstrattoContoLio");
			HibernateSessionFactoryUtil.commitTransaction();
			jsonMontanti = creaJsonMontantiFromListaEstratiContoLioTimeReferences(start, end, estrContLiosTimeReferences);
			jsonPremiProvvigioni = calcolaJsonPremiProvvigioniFromListaEstrarttiContoLioTimeReferences(start, end, estrContLiosTimeReferences);
		}
		grafiData.setMontanti(jsonMontanti);
		grafiData.setPremiProvvigioni(jsonPremiProvvigioni);
		HibernateSessionFactoryUtil.closeSession();
		return grafiData;
	}

	private int getMonthsBetweenTwoDate(Date start, Date end) {
		int numberOfMonthBetween = 0;
		DateTime variableDate = new DateTime(start.getTime());
		while (variableDate.isBefore(end.getTime())) {
			variableDate = variableDate.plusMonths(1);
			numberOfMonthBetween++;
		}
		return numberOfMonthBetween;
	}

	private String creaJsonMontantiFromListaChiusure(Date startingDate, Date endingDate, List<ChiusuraMensileLio> chiusuraMensileLios) {
		Map<Long, BigDecimal> montantiGrezzi = calcolaMontanteProvvigioniFromChiusureMensiliLio(chiusuraMensileLios);
		Map<Long, BigDecimal> montantiRettificati = rettificaMapPerRappresentazioneGrafoMontanti(startingDate, endingDate, montantiGrezzi);
		List<Map<Long, BigDecimal>> listMapMontanti = new ArrayList<Map<Long, BigDecimal>>();
		listMapMontanti.add(montantiRettificati);
		String jsonMontanti = creaJson(listMapMontanti);
		return jsonMontanti;
	}

	private String creaJsonMontantiFromListaEstratiContoLioTimeReferences(Date startingDate, Date endingDate, List<EstrattoContoLioTimeReferences> estrattoContoLios) {
		Map<Long, BigDecimal> montantiGrezzi = calcolaMontanteProvvigioniFromEstrattiContoLioTimeReferences(estrattoContoLios);
		Map<Long, BigDecimal> montantiRettificati = rettificaMapPerRappresentazioneGrafoMontanti(startingDate, endingDate, montantiGrezzi);
		List<Map<Long, BigDecimal>> listMapMontanti = new ArrayList<Map<Long, BigDecimal>>();
		listMapMontanti.add(montantiRettificati);
		String jsonMontanti = creaJson(listMapMontanti);
		return jsonMontanti;
	}

	private String calcolaJsonPremiProvvigioni(Date startingDate, Date endingDate, List<ChiusuraMensileLio> chiusureMaliceDb) {
		List<Map<Long, BigDecimal>> listMapPremiProvvigioni = new ArrayList<Map<Long, BigDecimal>>();
		Map<Long, BigDecimal> totProvvigioniEcGrezzi = new LinkedHashMap();
		Map<Long, BigDecimal> totPremiEcGrezzi = new LinkedHashMap();
		for (ChiusuraMensileLio chiusuraMensileLio : chiusureMaliceDb) {
			int meseRiferimento = chiusuraMensileLio.getReferenceCalendarMonth();
			int annoRiferimento = chiusuraMensileLio.getReferenceYear();
			DateTime dt = new DateTime(annoRiferimento, meseRiferimento, 1, 0, 0, 0, 0);
			totProvvigioniEcGrezzi.put(dt.getMillis(), chiusuraMensileLio.getTotCommissioniEuroCent()); // RAPPORTO_CENTESIMI_MIGLIAIA);
			totPremiEcGrezzi.put(dt.getMillis(), chiusuraMensileLio.getTotPremiEuroCent()); // RAPPORTO_CENTESIMI_MIGLIAIA);
		}
		Map<Long, BigDecimal> totPremiEcRettificati = rettificaMapPerRappresentazioneGrafoPremiEProvvigioni(startingDate, endingDate, totPremiEcGrezzi);
		Map<Long, BigDecimal> totProvvigioniEcRettificati = rettificaMapPerRappresentazioneGrafoPremiEProvvigioni(startingDate, endingDate, totProvvigioniEcGrezzi);
		listMapPremiProvvigioni.add(totPremiEcRettificati);
		listMapPremiProvvigioni.add(totProvvigioniEcRettificati);
		String jsonPremiProvvigioni = creaJson(listMapPremiProvvigioni);
		return jsonPremiProvvigioni;
	}

	private String calcolaJsonPremiProvvigioniFromListaEstrarttiContoLioTimeReferences(Date startingDate, Date endingDate, List<EstrattoContoLioTimeReferences> estrContLiosTimeReferences) {
		List<Map<Long, BigDecimal>> listMapPremiProvvigioni = new ArrayList<Map<Long, BigDecimal>>();
		Map<Long, BigDecimal> totProvvigioniEcGrezzi = new LinkedHashMap();
		Map<Long, BigDecimal> totPremiEcGrezzi = new LinkedHashMap();
		for (EstrattoContoLioTimeReferences estrattoContoLioTimeReferences : estrContLiosTimeReferences) {
			int meseRiferimento = estrattoContoLioTimeReferences.getReferenceCalendarMonth();
			int annoRiferimento = estrattoContoLioTimeReferences.getReferenceYear();
			DateTime dt = new DateTime(annoRiferimento, meseRiferimento, 1, 0, 0, 0, 0);
			totProvvigioniEcGrezzi.put(dt.getMillis(), estrattoContoLioTimeReferences.getEstrattoContoLio().getTotCommissioniEuroCent()); // RAPPORTO_CENTESIMI_MIGLIAIA);
			totPremiEcGrezzi.put(dt.getMillis(), estrattoContoLioTimeReferences.getEstrattoContoLio().getTotPremiEuroCent()); // RAPPORTO_CENTESIMI_MIGLIAIA);
		}
		Map<Long, BigDecimal> totPremiEcRettificati = rettificaMapPerRappresentazioneGrafoPremiEProvvigioni(startingDate, endingDate, totPremiEcGrezzi);
		Map<Long, BigDecimal> totProvvigioniEcRettificati = rettificaMapPerRappresentazioneGrafoPremiEProvvigioni(startingDate, endingDate, totProvvigioniEcGrezzi);
		listMapPremiProvvigioni.add(totPremiEcRettificati);
		listMapPremiProvvigioni.add(totProvvigioniEcRettificati);
		String jsonPremiProvvigioni = creaJson(listMapPremiProvvigioni);
		return jsonPremiProvvigioni;
	}

	private Map<Long, BigDecimal> rettificaMapPerRappresentazioneGrafo(Date startingDate, Date endignDate, Map<Long, BigDecimal> montanti, BigDecimal lastValueToAdd) {
		Map<Long, BigDecimal> montantiPlus = new LinkedHashMap<Long, BigDecimal>();
		if (montanti.size()>0) {
			List<Long> times = new ArrayList<Long>(montanti.keySet());
			Calendar calBeforeFrom = Calendar.getInstance();
			calBeforeFrom.setTimeInMillis(times.get(0));
			Calendar calBeforeTo = Calendar.getInstance();
			calBeforeTo.setTime(startingDate);
			int monthsBefore = monthsBetween(calBeforeFrom, calBeforeTo);
			Calendar calAfterFrom = Calendar.getInstance();
			long lastTime = times.get(times.size() - 1);
			calAfterFrom.setTimeInMillis(lastTime);
			Calendar calAfterTo = Calendar.getInstance();
			calAfterTo.setTime(endignDate);
			int monthsAfter = monthsBetween(calAfterTo, calAfterFrom);
			DateTime jodaIncrementingDate = new DateTime(startingDate);
			// aggiungo tanti zero quanti sono i mesi precedenti senza valore
			aggiungiValorePerMesi(montantiPlus, monthsBefore, jodaIncrementingDate, BigDecimal.ZERO);
			// aggiungo i mesi trovati
			montantiPlus.putAll(montanti);
			jodaIncrementingDate = new DateTime(lastTime);
			// aggiungo tanti zero quanti sono i mesi successivi senza valore
			aggiungiValorePerMesi(montantiPlus, monthsAfter, jodaIncrementingDate.plusMonths(1), lastValueToAdd);
		}else{
			//non esistono dati metto tutto a 0
			Calendar calFrom = Calendar.getInstance();
			calFrom.setTime(startingDate);
			Calendar calTo = Calendar.getInstance();
			calTo.setTime(endignDate);
			int monthsBefore = monthsBetween(calTo, calFrom);
			DateTime jodaIncrementingDate = new DateTime(startingDate);
			aggiungiValorePerMesi(montantiPlus, monthsBefore, jodaIncrementingDate, BigDecimal.ZERO);
		}
		return montantiPlus;
	}

	private Map<Long, BigDecimal> rettificaMapPerRappresentazioneGrafoMontanti(Date startingDate, Date endignDate, Map<Long, BigDecimal> montanti) {
		List<BigDecimal> valuesMontanti = new ArrayList<BigDecimal>(montanti.values());
		BigDecimal valueToAdd = BigDecimal.ZERO;
		if (valuesMontanti.size() > 0)
			valueToAdd = valuesMontanti.get(valuesMontanti.size() - 1);
		return rettificaMapPerRappresentazioneGrafo(startingDate, endignDate, montanti, valueToAdd);
	}

	private Map<Long, BigDecimal> rettificaMapPerRappresentazioneGrafoPremiEProvvigioni(Date startingDate, Date endignDate, Map<Long, BigDecimal> montanti) {
		return rettificaMapPerRappresentazioneGrafo(startingDate, endignDate, montanti, BigDecimal.ZERO);
	}

	private void aggiungiValorePerMesi(Map<Long, BigDecimal> mapToBeCompleted, int months, DateTime jodaIncrementingDate, BigDecimal valueToAdd) {
		if (months <= 0)
			return;
		for (int i = 0; i < months; i++) {
			long millisecStartingJodaDate = jodaIncrementingDate.getMillis();
			mapToBeCompleted.put(millisecStartingJodaDate, valueToAdd);
			jodaIncrementingDate = jodaIncrementingDate.plusMonths(1);
		}
	}

	private int monthsBetween(Calendar date1, Calendar date2) {
		int monthsBetween = 0;
		// difference in month for years
		monthsBetween = (date1.get(Calendar.YEAR) - date2.get(Calendar.YEAR)) * 12;
		// difference in month for months
		monthsBetween += date1.get(Calendar.MONTH) - date2.get(Calendar.MONTH);
		// difference in month for days
		if (date1.get(Calendar.DAY_OF_MONTH) != date1.getActualMaximum(Calendar.DAY_OF_MONTH) && date1.get(Calendar.DAY_OF_MONTH) != date1.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			monthsBetween += ((date1.get(Calendar.DAY_OF_MONTH) - date2.get(Calendar.DAY_OF_MONTH)) / 31d);
		}
		return monthsBetween;
	}

	/**
	 * @param chiusureMaliceDb
	 *            lista delle chiusure di cui calcolare il montate provvigioni
	 * @return una mappa con chiave il mese espresso in millisecondi e in valore
	 *         il montante delle provvigioni fino a quel mese
	 */
	private Map<Long, BigDecimal> calcolaMontanteProvvigioniFromChiusureMensiliLio(List<ChiusuraMensileLio> chiusureMaliceDb) {
		Map<Long, BigDecimal> montanti = new LinkedHashMap();
		BigDecimal sommatoria = BigDecimal.ZERO;
		for (ChiusuraMensileLio chiusuraMensileLio : chiusureMaliceDb) {
			sommatoria = sommatoria.add(chiusuraMensileLio.getTotCommissioniEuroCent()); // RAPPORTO_CENTESIMI_MIGLIAIA;
			int meseRiferimento = chiusuraMensileLio.getReferenceCalendarMonth();
			int annoRiferimento = chiusuraMensileLio.getReferenceYear();
			DateTime dt = new DateTime(annoRiferimento, meseRiferimento, 1, 0, 0, 0, 0);
			montanti.put(dt.getMillis(), sommatoria);
		}
		return montanti;
	}

	/**
	 * @param EstrattoContoLio
	 *            lista degli estratti conto di cui calcolare il montate
	 *            provvigioni
	 * @return una mappa con chiave il mese espresso in millisecondi e in valore
	 *         il montante delle provvigioni fino a quel mese
	 */
	private Map<Long, BigDecimal> calcolaMontanteProvvigioniFromEstrattiContoLioTimeReferences(List<EstrattoContoLioTimeReferences> estrattoContoLiosTimeReferences) {
		Map<Long, BigDecimal> montanti = new LinkedHashMap();
		BigDecimal sommatoria = BigDecimal.ZERO;
		for (EstrattoContoLioTimeReferences estrattoContoLioTimeReferences : estrattoContoLiosTimeReferences) {
			sommatoria = sommatoria.add(estrattoContoLioTimeReferences.getEstrattoContoLio().getTotCommissioniEuroCent()); // RAPPORTO_CENTESIMI_MIGLIAIA;
			int meseRiferimento = estrattoContoLioTimeReferences.getReferenceCalendarMonth();
			int annoRiferimento = estrattoContoLioTimeReferences.getReferenceYear();
			DateTime dt = new DateTime(annoRiferimento, meseRiferimento, 1, 0, 0, 0, 0);
			montanti.put(dt.getMillis(), sommatoria);
		}
		return montanti;
	}

	/**
	 * @param mappa
	 *            contenente i valori per la creazione della striga json
	 * @return la stringa json correttamente formattata contenente i valori
	 *         presenti nella mappa
	 */
	private String creaJson(List<Map<Long, BigDecimal>> listMap) {
		JSONObject jsonPlotter = new JSONObject();
		// array json
		JSONArray jsonArrayGraphs = new JSONArray();
		double max = 0;
		try {
			for (Map<Long, BigDecimal> map : listMap) {
				// array che contiene i punti per il grafo
				JSONArray jsonArrayPoints = new JSONArray();
				Iterator it = map.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry) it.next();
					System.out.println(pairs.getKey() + " = " + pairs.getValue());
					// array che rappresente un singolo punto del grafo
					JSONArray jsonArraySinglePoint = new JSONArray();
					BigDecimal ordinataEuroBD = ((BigDecimal) pairs.getValue()).divide(new BigDecimal(100));
					BigDecimal ordinataEuroBDNoDecimale = ordinataEuroBD.setScale(0, BigDecimal.ROUND_HALF_UP);
					double ordinataEuro = ordinataEuroBDNoDecimale.doubleValue();
					if (max < ordinataEuro)
						max = ordinataEuro;
					jsonArraySinglePoint.put(pairs.getKey()); // ascissa
					jsonArraySinglePoint.put(ordinataEuro); // ordinata
					jsonArrayPoints.put(jsonArraySinglePoint);
				}
				// oggetto json che contiene i punti del grafo + la label
				JSONObject jsonObjectSingleGraph = new JSONObject();
				jsonObjectSingleGraph.put("label", "myLabel");
				jsonObjectSingleGraph.put("data", jsonArrayPoints);
				jsonArrayGraphs.put(jsonObjectSingleGraph);
			}
			max += (max / 5); // h del piano cartesiamo = all'altezza massima +
								// 20% altezza massima
			log.debug("max=" + max);
			jsonPlotter.put("max", max);
			jsonPlotter.put("grafoInputs", jsonArrayGraphs);
		} catch (JSONException e) {
			log.error("ERROR building json:", e);
		}
		return jsonPlotter.toString();
	}
}
