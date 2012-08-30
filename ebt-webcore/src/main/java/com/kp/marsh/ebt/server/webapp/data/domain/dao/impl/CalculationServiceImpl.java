/**
 * 
 */
package com.kp.marsh.ebt.server.webapp.data.domain.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kp.marsh.ebt.server.webapp.data.domain.BusinessInformation;
import com.kp.marsh.ebt.server.webapp.data.domain.InformationOwners;
import com.kp.marsh.ebt.server.webapp.data.domain.Products;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.CalculationService;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.DomainDrillerService;
import com.kp.marsh.ebt.server.webapp.utils.AnnotatedHibernateUtil;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;
import com.kp.marsh.ebt.shared.PotentialValue;
import com.kp.marsh.ebt.shared.dto.BusinessInformationLiteDTO;
import com.kp.marsh.ebt.shared.dto.SintesiDto;

/**
 * @author christianachilli
 *
 */
@Singleton
public class CalculationServiceImpl implements CalculationService {

    private static Logger log = Logger.getLogger(CalculationServiceImpl.class);

    @Inject
    private AnnotatedHibernateUtil connection;

    @Inject
    private DomainDrillerService domainService;

    /**
     * VALE sia per la situzione corrente che per l'inizio anno. Rappresenta il denominatore dell'indice
     * @param iOGruppoCommerciale
     * @param products
     * @param referenceThisYear
     * @param referenceLastYear
     * @return Per la lob e il gruppo commerciale specificato, conta il totale dei prodotti vendibili (#prodotti lob * #gruppi commerciali) e gli sottrae il # di prodotti dichiarati IAC, NA, o PI che non hanno un achieved o un consuntivo
     *	
     * @see     {@link  src/main/resources/spiegazione_insiemi.tiff} per la spiegazione degli insiemi usati nel metodo
     */
    public int calcolaNumeroVendibiliByLob(List<Integer> iOGruppoCommerciale, List<Products> products,
            int referenceThisYear, int referenceLastYear) {
        Session session = connection.getSessionFactory().openSession();
        Transaction transazione = session.beginTransaction();
        int insiemeA = products.size() * iOGruppoCommerciale.size(); // è il numero di prodotti per il numero di clienti
        List<BusinessInformation> insiemeB = new ArrayList<BusinessInformation>(); // tutte le business info che sono NA, PI o IAC
        Set<BusinessInformationLiteDTO> insiemeC = new HashSet<BusinessInformationLiteDTO>(); // tutte le business info di B che hanno un consuntivo
        Set<BusinessInformationLiteDTO> insiemeD = new HashSet<BusinessInformationLiteDTO>(); // tutte le business info di B che hanno un achieved
        Set<BusinessInformationLiteDTO> insiemeK = new HashSet<BusinessInformationLiteDTO>(); // tutte le business info di B che hanno sia un consuntivo che un achieved
        try {

            // calcolo dell'insieme B
            Criteria criteria = session.createCriteria(BusinessInformation.class);
            criteria.add(Property.forName("informationOwners.id").in(iOGruppoCommerciale));
            criteria.add(Property.forName("products").in(products));
            criteria.add(Restrictions.and(Property.forName("valueType").eq(BusinessInfoValueType.POTENTIAL.name()),
                    Restrictions.and(Property.forName("valueAmount").in(PotentialValue.getOutOfScope_NameList()),
                            Restrictions.eq("referenceYears.id", referenceThisYear))));
            insiemeB = criteria.list();
            transazione.commit();

            // lista delle coppie owner-prod delle BI di B
            MyLiteList outOfScopeList = new MyLiteList(insiemeB);

            if (!outOfScopeList.isEmpty()) {

                // calcolo l'insieme C-LARGE
                transazione = session.beginTransaction();
                criteria = session.createCriteria(BusinessInformation.class);
                criteria.add(Property.forName("informationOwners.id").in(iOGruppoCommerciale));
                criteria.add(Property.forName("products").in(products));
                criteria.add(Restrictions.and(
                        Property.forName("valueType").eq(BusinessInfoValueType.FINAL_BALANCE.name()),
                        Restrictions.eq("referenceYears.id", referenceLastYear)));
                List<BusinessInformation> insiemeCLarge = criteria.list();

                // calcolo l'insieme D-LARGE
                transazione = session.beginTransaction();
                criteria = session.createCriteria(BusinessInformation.class);
                criteria.add(Property.forName("informationOwners.id").in(iOGruppoCommerciale));
                criteria.add(Property.forName("products").in(products));
                criteria.add(Restrictions.and(Property.forName("valueType").eq(BusinessInfoValueType.ACHIEVED.name()),
                        Restrictions.eq("referenceYears.id", referenceThisYear)));
                List<BusinessInformation> insiemeDLarge = criteria.list();

                MyLiteList cAtLarge = new MyLiteList(insiemeCLarge); // la lista dei consuntivi, anche quelli che non appartengono a B
                MyLiteList dAtLarge = new MyLiteList(insiemeDLarge); // la lista degli achieved, anche quelli che non appartengono a B		

                if (!insiemeCLarge.isEmpty() || !insiemeDLarge.isEmpty()) { // devo trovare l'insieme K-Large e poi restringerlo  alle coppie owner-prod contenute in B
                    MyLiteList kAtLArge = new MyLiteList();
                    for (BusinessInformationLiteDTO finalBalance : cAtLarge) { // così trovo il K Large
                        if (dAtLarge.contains(finalBalance)) {
                            kAtLArge.add(finalBalance);

                        }
                    }

                    // faccio l'intersezione di K-Large con B: il risultato è il K che cerco
                    if (!kAtLArge.isEmpty()) {
                        for (BusinessInformationLiteDTO kLargeInfo : kAtLArge) {
                            if (outOfScopeList.contains(kLargeInfo)) {
                                insiemeK.add(kLargeInfo);

                            }
                        }
                    }

                    // faccio l'intersezione di C-Large con B: il risultato è il C che cerco
                    if (!cAtLarge.isEmpty()) {
                        for (BusinessInformationLiteDTO cLargeInfo : cAtLarge) {
                            if (outOfScopeList.contains(cLargeInfo)) {
                                insiemeC.add(cLargeInfo);

                            }
                        }
                    }

                    // faccio l'intersezione di D-Large con B: il risultato è il D che cerco
                    if (!dAtLarge.isEmpty()) {
                        for (BusinessInformationLiteDTO dLargeInfo : dAtLarge) {
                            if (outOfScopeList.contains(dLargeInfo)) {
                                insiemeD.add(dLargeInfo);

                            }
                        }
                    }
                }
            }

        } catch (RuntimeException re) {
            log.error("calcolaNumeroVendibiliByLob", re);
            throw re;

        } finally {
            session.close();

        }

        return insiemeA - insiemeB.size() - insiemeK.size() + insiemeC.size() + insiemeD.size();

    }

    /**
     * @param iOGruppoCommerciale
     * @param products
     * @param thisYearId
     * @param lastYearId
     * @return Per la lob e il gruppo commerciale specificato, ritorna il numero di prodotti che hanno un consuntivo assommato al numero di prodotti che hanno un achieved ma non hanno un consuntivo
     */
    public int calcolaNumeroVendutiByLob(List<Integer> iOGruppoCommerciale, List<Products> products, int thisYearId,
            int lastYearId) {

        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        int res = 0;
        try {
            ProjectionList projList = Projections.projectionList();
            projList.add(Projections.groupProperty("informationOwners.id"));
            projList.add(Projections.groupProperty("products.id"));
            Criteria criteria = session.createCriteria(BusinessInformation.class);
            criteria.add(Property.forName("informationOwners.id").in(iOGruppoCommerciale));
            criteria.add(Property.forName("products").in(products));
            criteria.add(Restrictions.or((Restrictions.and(
                    Restrictions.eq("valueType", BusinessInfoValueType.ACHIEVED.name()),
                    Restrictions.eq("referenceYears.id", thisYearId))), (Restrictions.and(
                    Restrictions.eq("valueType", BusinessInfoValueType.FINAL_BALANCE.name()),
                    Restrictions.eq("referenceYears.id", lastYearId)))));
            criteria.setProjection(projList);
            List biList = criteria.list();
            res = biList.size();
            tx.commit();
        } catch (RuntimeException re) {
            log.error("calcolaNumeroVendutiByLob", re);
            tx.rollback();
            throw re;
        } finally {
            session.close();
        }
        return res;

    }

    /**
     * @param iOGruppoCommerciale la lista di gruppi commerciali
     * @param products la lista di prodotti
     * @param referenceLastYear l'anno di riferimento dei final balance cercati
     * @return il numero di final_balance
     */
    public int calcolaNumeroVendutiInizoAnnoByLob(List<Integer> iOGruppoCommerciale, List<Products> products,
            int referenceLastYear) {
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        int res = 0;
        try {
            ProjectionList projList = Projections.projectionList();
            projList.add(Projections.groupProperty("products.id"));
            projList.add(Projections.groupProperty("informationOwners.id"));
            Criteria criteria = session.createCriteria(BusinessInformation.class);
            criteria.add(Property.forName("informationOwners.id").in(iOGruppoCommerciale));
            criteria.add(Property.forName("products").in(products));
            criteria.add(Restrictions.and(Restrictions.eq("valueType", BusinessInfoValueType.FINAL_BALANCE.name()),
                    Restrictions.eq("referenceYears.id", referenceLastYear)));
            criteria.setProjection(projList);
            List biList = criteria.list();
            res = biList.size();
            tx.commit();

        } catch (RuntimeException re) {
            log.error("calcolaNumeroVendutiInizioAnnoByLob", re);
            tx.rollback();
            throw re;

        } finally {
            session.close();

        }
        return res;
    }

    @Override
    public int getIdIOAntenatoFromIdIODiscendente(int id) {
        InformationOwners informationOwners = domainService.findInformationOwnerById(id);
        int idFiglio = id;
        if (informationOwners == null)
            return -1;
        // risalgo al padre iterativamente fino al padre supremo: colui che non
        // ha padri
        while (informationOwners.getParentId() != null) {
            int idPadre = informationOwners.getParentId();
            informationOwners = domainService.findInformationOwnerById(idPadre);
            idFiglio = idPadre;
            if (informationOwners == null)
                return -1;
        }
        return idFiglio;
    }

    @Override
    public Map<Integer, Integer> getTotalAchievedByProductList(InformationOwners informationOwner, int year,
            ArrayList<Integer> products) {
        Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();

        // la query vuole in ingresso la lisa di Integer. Da quella in input
        // deduco gli is negativi, relativi a colonne della pagina vuote.
        //		ArrayList<Integer> integerList = new ArrayList<Integer>();
        //		for (ProductInfoDTO product : products) {
        //			if (product != null)
        //				integerList.add(new Integer(product.getId()));
        //		}

        // preparo la lista con le BI cercate
        List<String> allowedVals = new ArrayList<String>();
        allowedVals.add(BusinessInfoValueType.ACHIEVED.name());

        // cerco tutti gruppi commerciali che afferiscono all'information owner
        // in ingresso
        List<Integer> gruppiCommercialiList = domainService.findGruppoCommerciale(informationOwner.getId());
        log.debug("Information owner: " + informationOwner.getDescription() + " type: "
                + informationOwner.getOwnerType() + " has " + gruppiCommercialiList.size() + " gruppi commerciali");

        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {

            if (!gruppiCommercialiList.isEmpty()) {
                Query q = session.createQuery("select  sum(valueAmount), products.id from BusinessInformation where "
                        + "informationOwners.id in (:allowedList) " + "and referenceYears.id =:year "
                        + "and valueType in (:allowedValueTypes) " + "and products.id in (:prodId) "
                        + "group by products.id");
                q.setParameter("year", year);
                q.setParameterList("allowedValueTypes", allowedVals);
                q.setParameterList("allowedList", gruppiCommercialiList);
                q.setParameterList("prodId", products);

                List<Object[]> queryResult = (List<Object[]>) q.list();

                if (!queryResult.isEmpty()) {
                    for (Object[] objects : queryResult) {
                        resultMap.put((Integer) objects[1], Integer.valueOf((String) objects[0]));

                    }
                }

            }

            tx.commit();

        } catch (HibernateException e) {
            log.error("Exception in getTotalAchievedByProductList per owner con id <" + informationOwner.getId() + ">",
                    e);
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
        return resultMap;
    }

    @Override
    public int getTotalAchievedForAnyInformationOwner(int anyInfoOwnerTypeId, int year) {
        String sum = null;
        List<Integer> gruppiCommercialiIdList = domainService.findGruppoCommerciale(anyInfoOwnerTypeId);

        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {

            if (!gruppiCommercialiIdList.isEmpty()) {
                Query q = session.createQuery("select  sum(valueAmount) from BusinessInformation where "
                        + "informationOwners.id in (:allowedList) " + "and referenceYears.id =:year "
                        + "and valueType in (:allowedValueTypes)" + "and valueAmount not in ('NA','PI')");
                q.setParameter("year", year);
                List<String> allowedVals = new ArrayList<String>();
                allowedVals.add(BusinessInfoValueType.ACHIEVED.name());
                q.setParameterList("allowedValueTypes", allowedVals);
                q.setParameterList("allowedList", gruppiCommercialiIdList);

                sum = (String) q.uniqueResult();

            }

            tx.commit();

        } catch (HibernateException e) {
            log.error("Exception in calcolo totale achieved per CE con id <" + anyInfoOwnerTypeId + ">", e);
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
        return sum != null ? Integer.valueOf(sum) : 0;
    }

    @Override
    public Map<Integer, Integer> getTotalPotentialByProductList(InformationOwners informationOwner, int year,
            ArrayList<Integer> products) {
        Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();

        // la query vuole in ingresso la lisa di Integer. Da quella in input
        // deduco gli id negativi, relativi a colonne della pagina vuote.
        //		ArrayList<Integer> integerList = new ArrayList<Integer>();
        //		for (ProductInfoDTO product : products) {
        //			if (product != null)
        //				integerList.add(new Integer(product.getId()));
        //		}

        // preparo la lista con le BI cercate
        List<String> allowedVals = new ArrayList<String>();
        allowedVals.add(BusinessInfoValueType.POTENTIAL.name());
        allowedVals.add(BusinessInfoValueType.BROKER_POT.name());
        allowedVals.add(BusinessInfoValueType.COMPANY_POT.name());

        // cerco tutti gruppi commerciali che afferiscono all'information owner
        // in ingresso
        List<Integer> gruppiCommercialiList = domainService.findGruppoCommerciale(informationOwner.getId());
        log.debug("Information owner: " + informationOwner.getDescription() + " type: "
                + informationOwner.getOwnerType() + " has " + gruppiCommercialiList.size() + " gruppi commerciali");

        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {

            if (!gruppiCommercialiList.isEmpty()) {
                Query q = session.createQuery("select  sum(valueAmount), products.id from BusinessInformation where "
                        + "informationOwners.id in (:allowedList) " + "and referenceYears.id =:year "
                        + "and valueType in (:allowedValueTypes) " + "and products.id in (:prodId) "
                        + "and valueAmount not in ('NA','PI') " + "group by products.id");
                q.setParameter("year", year);
                q.setParameterList("allowedValueTypes", allowedVals);
                q.setParameterList("allowedList", gruppiCommercialiList);
                q.setParameterList("prodId", products);

                List<Object[]> queryResult = (List<Object[]>) q.list();

                if (!queryResult.isEmpty()) {
                    for (Object[] objects : queryResult) {
                        resultMap.put((Integer) objects[1], Integer.valueOf((String) objects[0]));

                    }
                }

            }

            tx.commit();

        } catch (HibernateException e) {
            log.error(
                    "Exception in getTotalPotentialByProductList per owner con id <" + informationOwner.getId() + ">",
                    e);
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
        return resultMap;
    }

    @Override
    public int getTotalPotentialForAnyInformationOwner(int anyInfoOwnerTypeId, int year) {
        String sum = null;

        List<Integer> gruppiCommercialiIdList = domainService.findGruppoCommerciale(anyInfoOwnerTypeId);

        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {

            if (!gruppiCommercialiIdList.isEmpty()) {
                Query q = session.createQuery("select  sum(valueAmount) from BusinessInformation where "
                        + "informationOwners.id in (:allowedList) " + "and referenceYears.id =:year "
                        + "and valueType in (:allowedValueTypes) " + "and valueAmount not in ('NA','PI')");
                q.setParameter("year", year);
                List<String> allowedVals = new ArrayList<String>();
                allowedVals.add(BusinessInfoValueType.POTENTIAL.name());
                allowedVals.add(BusinessInfoValueType.BROKER_POT.name());
                allowedVals.add(BusinessInfoValueType.COMPANY_POT.name());
                q.setParameterList("allowedValueTypes", allowedVals);
                q.setParameterList("allowedList", gruppiCommercialiIdList);

                sum = (String) q.uniqueResult();
            }

            tx.commit();

        } catch (HibernateException e) {

            log.error("Exception in calcolo totale per InformationOwner con id <" + anyInfoOwnerTypeId + ">", e);
            tx.rollback();
            throw e;

        } finally {
            session.close();
        }

        return sum != null ? Integer.valueOf(sum) : 0;

    }

    /**
     * Somma il valore per i tipi specificati e per i prodotti e i gruppi
     * commerciali specificati
     * 
     * @param clientsId
     *            lista di id dei gruppi commerciali
     * @param productsId
     *            lista di id dei prodotti
     * @param year
     *            anno di validità
     * @param allowedVals
     *            lista di valori di cui eseguire la sommatoria
     * @return la sommatoria
     */
    private int sumValueOfTypeFromBusinessInformationInLobInClient(List<Integer> clientsId, List<Integer> productsId,
            int year, List<String> allowedVals) {
        log.debug("sumValueOfTypeFromBusinessInformationInLobInClient");
        List<BusinessInformation> results = null;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        int sum = 0;
        try {
            Query q = session.createQuery("select sum(valueAmount) from BusinessInformation where "
                    + "informationOwners.id in (:clientsId) " + "and referenceYears.id =:year "
                    + "and valueType in (:allowedValueTypes) " + "and products.id in (:productsId) "
                    + "and valueAmount not in (:notInList)");
            q.setParameter("year", year);
            q.setParameterList("allowedValueTypes", allowedVals);
            q.setParameterList("clientsId", clientsId);
            q.setParameterList("productsId", productsId);
            q.setParameterList("notInList", PotentialValue.getOutOfScope_NameList());
            String sumStr = (String) q.uniqueResult();
            if (sumStr != null)
                sum = Integer.parseInt(sumStr);
            log.debug("sum " + sum);
            tx.commit();
        } catch (RuntimeException re) {
            log.error("Failed calculating sum value from BusinessInformation", re);
            tx.rollback();
            throw re;
        } finally {
            session.close();
        }
        return sum;
    }

    public int countBusinessInformationByClientsIdProductsNotOfTypeSpecified(List<Integer> clientsId,
            List<Integer> productsId, int year, String type, int year2, String type2) {
        log.debug("countInformationByClientsIdProducts");
        List<BusinessInformation> results = null;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        int res = 0;
        try {
            ProjectionList projList = Projections.projectionList();
            projList.add(Projections.countDistinct("products.id"));
            projList.add(Projections.groupProperty("informationOwners.id"));
            Criteria criteria = session.createCriteria(BusinessInformation.class);
            criteria.add(Property.forName("informationOwners.id").in(clientsId));
            criteria.add(Property.forName("products.id").in(productsId));
            criteria.add(Restrictions.not(Property.forName("valueAmount").in(PotentialValue.getOutOfScope_NameList())));
            criteria.add(Restrictions.and(
                    Restrictions.and(Restrictions.ne("valueType", type), Restrictions.eq("referenceYears.id", year)),
                    Restrictions.and(Restrictions.ne("valueType", type2), Restrictions.eq("referenceYears.id", year2))));
            criteria.setProjection(projList);
            List biList = criteria.list();
            for (Iterator iterator = biList.iterator(); iterator.hasNext();) {
                res += (Integer) (((Object[]) iterator.next())[0]);
            }
            log.debug("found " + res + " products");
            tx.commit();
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            tx.rollback();
            throw re;
        } finally {
            session.close();
        }
        return res;
    }

    @Override
    public void calcolaIndiceDiSaturazioneDelPotenziale(List<Integer> gruppiCommerciali,
            List<Integer> gruppiCommercialiNazione, int referenceYearId, int lastYearId, List<Products> products,
            SintesiDto result) {

        Integer[] ispLiteDto = elaboraISP(gruppiCommerciali, referenceYearId, lastYearId, products);
        result.setIndiceSaturazionePotenziale(ispLiteDto[0]);
        result.setProdottiDaLavorare(ispLiteDto[2]);
        result.setProdottiLavorati(ispLiteDto[1]);

        if (gruppiCommerciali.containsAll(gruppiCommercialiNazione)) {
            result.setIndiceSaturazionePotenzialeTotale(ispLiteDto[0]);

        } else {
            ispLiteDto = elaboraISP(gruppiCommercialiNazione, referenceYearId, lastYearId, products);
            result.setIndiceSaturazionePotenzialeTotale(ispLiteDto[0]);
        }

    }

    //public per test
    public Integer[] elaboraISP(List<Integer> gruppiCommercialiList, int referenceYearId, int lastYearId,
            List<Products> products) {
        //		List<Integer> gruppiCommercialiList = domainService.findGruppoCommerciale(id);

        int totaleProdotti = products.size() * gruppiCommercialiList.size();

        MyLiteList ownProdOutOfScopeList = trovaOutOfScope(referenceYearId, lastYearId, products, gruppiCommercialiList);

        //		// C: elenco le coppie prodotto-owner out of scope, cioè che hanno un consuntivo e/o sono NA, PI o IAC
        //		MyLiteList outOfScopeIdList = new MyLiteList(outOfScopeList);

        MyLiteList workedList[] = trovaPotenzialiInScope(referenceYearId, ownProdOutOfScopeList, products,
                gruppiCommercialiList);

        // F: tra i lavorati cerco quelli in saturazione: insieme K
        //		MyLiteList workedLiteList = new MyLiteList(workedList);

        int amountAchievedConPotenzialeDichiarato = 0; // un achieved che ha anche un potenziale: numeratore dell'indice ISP
        int amountPotenzialeDichiaratoInSaturazione = 0; // il potenziale che ha anche un achieved:denominatore dell'indice ISP

        //		List<BusinessInformation> inSaturazioneList = new ArrayList<BusinessInformation>();
        //		if(!workedList.isEmpty()) {
        MyLiteList[] inSaturazioneList = trovaAchievedConPotenziale(referenceYearId, workedList[0], products,
                gruppiCommercialiList);

        //		if(!inSaturazioneList.isEmpty()) { // vado a cercare i potenziali relativi agli achieved. Ci devono essere per forza perchè gli achieved sono scelti tra quelli lavorati

        // G: cerco i potenziali delle coppie prodotto-owner in saturazione: insieme K del disegno esplicativo /ebt-services/src/main/resources/definizione_ISP.tiff
        //			MyLiteList inSaturazioneLiteList = new MyLiteList(inSaturazioneList);

        amountAchievedConPotenzialeDichiarato = inSaturazioneList[0].getThisListTotalAmount();

        //			for (BusinessInformation busInfo : inSaturazioneList) {
        //				amountAchievedConPotenzialeDichiarato += Integer.valueOf(busInfo.getValueAmount());
        //
        //			}

        //		MyLiteList potenzialiInSaturazioneList = trovaPotenzialiConAchieved(referenceYearId, inSaturazioneList, products, gruppiCommercialiList); 

        //		amountPotenzialeDichiaratoInSaturazione =  inSaturazioneList[1].getThisListTotalAmount();
        amountPotenzialeDichiaratoInSaturazione = workedList[0].getThisListTotalAmount();

        //			for (BusinessInformation busInfo : potenzialiInSaturazioneList) {
        //				amountPotenzialeDichiaratoInSaturazione += Integer.valueOf(busInfo.getValueAmount());
        //
        //			}
        //		}

        //		}

        //normalizzazione
        if (amountPotenzialeDichiaratoInSaturazione < amountAchievedConPotenzialeDichiarato) {
            // TODO
        } else if (amountPotenzialeDichiaratoInSaturazione == 0) { // default a 0 evitando divisioni indeterminate
            amountAchievedConPotenzialeDichiarato = 0;
            amountPotenzialeDichiaratoInSaturazione = 1;
        }

        Integer[] result = new Integer[3];
        result[0] = Math.round(new Float(amountAchievedConPotenzialeDichiarato * 100)
                / new Float(amountPotenzialeDichiaratoInSaturazione)); //isp
        result[1] = workedList[0].size();//lavorati
        int daLavorare = totaleProdotti - workedList[1].size() - ownProdOutOfScopeList.size();
        result[2] = daLavorare < 0 ? 0 : daLavorare; //daLavorare

        return result;

    }

    /**
     * Il size della lista che viene ritornata è il numero totale di coppie owner-prodotto out of scope
     * @param referenceYearId
     * @param lastYearId
     * @param products
     * @param gruppiCommercialiList
     * @return il numero NON ridondato degli out of scope. Sarebbe ridondato perchè alcune coppie owner prodotto possono avere sia un consuntivo che NA, PI, IAC, il distinct serve per eliminare la ridondanza.
     */
    public MyLiteList trovaOutOfScope(int referenceYearId, int lastYearId, List<Products> products,
            List<Integer> gruppiCommercialiList) {
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(BusinessInformation.class);
        List<BusinessInformation> outOfScopeList = new ArrayList<BusinessInformation>();
        try {
            // A: trovo tutte le business information la cui coppia owner-prodotto è un CONSUNTIVO o ha un POTENZIALE con valore NA, PI o IAC: 
            // insieme distinct delle coppie owner-prodotto OUT OF SCOPE
            criteria = session.createCriteria(BusinessInformation.class);
            ProjectionList projList1 = Projections.projectionList();
            ProjectionList projList2 = Projections.projectionList();
            projList1.add(Projections.property("informationOwners.id"));
            projList1.add(Projections.property("products.id"));
            projList2.add(Projections.distinct(projList1)); // questo elimina la ridondanza tra consuntivi e NA, PI, IAC
            criteria.setProjection(projList2);
            criteria.add(Property.forName("informationOwners.id").in(gruppiCommercialiList));
            criteria.add(Property.forName("products").in(products));
            // consuntivo or potenziale NA, PI, IAC
            criteria.add(Restrictions.or(Restrictions.and(Property.forName("referenceYears.id").eq(referenceYearId),
                    Property.forName("valueAmount").in(PotentialValue.getOutOfScope_NameList())), Restrictions.and(
                    Property.forName("referenceYears.id").eq(lastYearId),
                    Property.forName("valueType").eq(BusinessInfoValueType.FINAL_BALANCE.name()))));
            outOfScopeList = criteria.list();

        } catch (RuntimeException re) {
            log.error("calcolaIndiceDiPenetrazione", re);
            throw re;

        } finally {
            tx.commit();
            session.close();

        }
        return new MyLiteList(outOfScopeList);

    }

    /**
     * 
     * @param referenceYearId
     * @param session
     * @param outOfScopeList
     * @return Tra tutti i prodotti-owner, quelli che hanno un potenziale dichiarato e che non hanno un consuntivo o una dichiarazione NA, PI o IAC
     */
    public MyLiteList[] trovaPotenzialiInScope(int referenceYearId, MyLiteList outOfScopeList, List<Products> products,
            List<Integer> gruppiCommercialiList) {
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(BusinessInformation.class);
        MyLiteList potentialLiteList = new MyLiteList();
        MyLiteList potentialNoConsuntiviLiteList = new MyLiteList();
        MyLiteList[] result = new MyLiteList[2];
        result[0] = potentialLiteList;
        result[1] = potentialNoConsuntiviLiteList;
        try {
            // E: cerco tutti i potenziali che non sono out of scope, cioè la cui coppia owner-prodotto non è nella lista determinata in C e D (lista degli out of scope): lista di quelli lavorati 
            List<String> allowedVals = new ArrayList<String>();
            allowedVals.add(BusinessInfoValueType.POTENTIAL.name());
            allowedVals.add(BusinessInfoValueType.BROKER_POT.name());
            allowedVals.add(BusinessInfoValueType.COMPANY_POT.name());
            criteria = session.createCriteria(BusinessInformation.class);
            ProjectionList projList1 = Projections.projectionList();
            ProjectionList projList2 = Projections.projectionList();
            projList1.add(Projections.groupProperty("informationOwners.id"));
            projList1.add(Projections.groupProperty("products.id"));
            projList1.add(Projections.sum("valueAmount"));
            projList2.add(Projections.distinct(projList1));// c'è bisogno di questo distinct?? per come è fatto il DB direi di no
            criteria.setProjection(projList2);
            criteria.add(Property.forName("products").in(products));
            criteria.add(Property.forName("informationOwners.id").in(gruppiCommercialiList));
            criteria.add(Property.forName("referenceYears.id").eq(referenceYearId));
            criteria.add(Property.forName("valueType").in(allowedVals));
            criteria.add( // tolgo gli out of scope
            Restrictions.not(Restrictions.and(Property.forName("referenceYears.id").eq(referenceYearId), Property
                    .forName("valueAmount").in(PotentialValue.getOutOfScope_NameList()))));
            List<Object[]> tuttiPotenziali = criteria.list(); // trovo TUTTI i potenziali, tranne gli NA, PI, IAC 
            MyLiteList bufferList = new MyLiteList(tuttiPotenziali);
            for (BusinessInformationLiteDTO pot : bufferList) {
                potentialLiteList.add(pot);
                potentialNoConsuntiviLiteList.add(pot);
            }

            // devo togliere quelle coppie che hanno un consuntivo
            for (BusinessInformationLiteDTO finalBalance : outOfScopeList) {
                if (potentialNoConsuntiviLiteList.contains(finalBalance)) {
                    potentialNoConsuntiviLiteList.remove(finalBalance);
                }
            }

        } catch (RuntimeException re) {
            log.error("calcolaIndiceDiPenetrazione", re);
            throw re;

        } finally {
            tx.commit();
            session.close();
        }
        return result;
    }

    /**
     * @param referenceYearId
     * @param session
     * @param workedLiteList
     * @return in posizione 0 le coppie owner-prodotto di achieved che hanno un potenziale e sono in scope, in posizione 1 le coppie owner-prodotto con il potenziale relativo alle coppie in 0
     */
    public MyLiteList[] trovaAchievedConPotenziale(int referenceYearId, MyLiteList workedLiteList,
            List<Products> products, List<Integer> gruppiCommercialiList) {
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(BusinessInformation.class);
        List<Object[]> achievedList = new ArrayList<Object[]>();
        MyLiteList achievedLiteList = new MyLiteList();
        MyLiteList potentialLiteList = new MyLiteList();
        MyLiteList[] result = new MyLiteList[2];
        result[0] = achievedLiteList;
        result[1] = potentialLiteList;
        try {
            criteria = session.createCriteria(BusinessInformation.class);
            ProjectionList projList1 = Projections.projectionList();
            ProjectionList projList2 = Projections.projectionList();
            projList1.add(Projections.groupProperty("informationOwners.id"));
            projList1.add(Projections.groupProperty("products.id"));
            projList1.add(Projections.sum("valueAmount"));
            projList2.add(Projections.distinct(projList1));// c'è bisogno di questo distinct?? per come è fatto il DB direi di no
            criteria.setProjection(projList2);
            criteria.add(Property.forName("valueType").eq(BusinessInfoValueType.ACHIEVED.name()));
            criteria.add(Property.forName("products").in(products));
            criteria.add(Property.forName("informationOwners.id").in(gruppiCommercialiList));
            criteria.add(Property.forName("referenceYears.id").eq(referenceYearId));
            achievedList = (List<Object[]>) criteria.list();

            MyLiteList bufferList = new MyLiteList(achievedList);
            for (BusinessInformationLiteDTO achieved : bufferList) {
                if (workedLiteList.contains(achieved)) { // se l'achieved è tra i potenziali lavorati e in scope
                    achievedLiteList.add(achieved);
                    potentialLiteList.add(workedLiteList.get(workedLiteList.indexOf(achieved)));

                }

            }

        } catch (RuntimeException re) {
            log.error("calcolaIndiceDiPenetrazione", re);
            throw re;

        } finally {
            tx.commit();
            session.close();

        }
        return result;
    }

    /**
     * @author christianachilli
     * La uso per gestire le query del calcolo dei vendibili
     */
    public class MyLiteList extends ArrayList<BusinessInformationLiteDTO> {

        private int totalAmount = 0;

        public MyLiteList() {
            super();
        }

        /**
         * @param ownerProdList
         * @param computeTotalAmount true se il total amount va calcolato. Lo uso per risparmiare tempo di calcolo quando il total amount non serve
         */
        public MyLiteList(List<?> ownerProdList) {
            if (!ownerProdList.isEmpty()) {

                if (ownerProdList.get(0) instanceof BusinessInformation) {
                    for (BusinessInformation busInfo : (List<BusinessInformation>) ownerProdList) {
                        add(new BusinessInformationLiteDTO(busInfo.getProducts().getId(), busInfo
                                .getInformationOwners().getId(), 0));

                    }

                } else if (ownerProdList.get(0) instanceof Object[]) {
                    for (Object[] idArray : (List<Object[]>) ownerProdList) {
                        if (idArray.length < 3) {
                            add(new BusinessInformationLiteDTO((Integer) idArray[1], (Integer) idArray[0], 0));
                        } else {
                            add(new BusinessInformationLiteDTO((Integer) idArray[1], (Integer) idArray[0],
                                    Integer.valueOf((String) idArray[2])));
                        }
                    }
                }
            }
        }

        /**
         * Funziona solo se la lista è stata creata a degli Object[]
         * @return la somma dei value_amount delle business info che formano formato questa lista
         */
        public int getThisListTotalAmount() {
            return totalAmount;

        }

        @Override
        public boolean add(BusinessInformationLiteDTO e) {
            totalAmount += e.getAmount();
            return super.add(e);

        }

    }

    @Override
    public void calcolaIndiceDiPenetrazione(List<Integer> iOGruppoCommerciale,
            List<Integer> iOGruppoCommercialeNazione, List<Products> products, int referenceThisYearId,
            int referenceLastYearId, SintesiDto lobSintesiDto) {
        int venduti = calcolaNumeroVendutiByLob(iOGruppoCommerciale, products, referenceThisYearId, referenceLastYearId);
        log.debug("\tvenduti: " + venduti);
        int vendibili = calcolaNumeroVendibiliByLob(iOGruppoCommerciale, products, referenceThisYearId,
                referenceLastYearId);
        log.debug("\tvendibili: " + vendibili);
        int vendutiNazione = calcolaNumeroVendutiByLob(iOGruppoCommercialeNazione, products, referenceThisYearId,
                referenceLastYearId);
        log.debug("\tvendutiNazione: " + vendutiNazione);
        int vendibiliNazione = calcolaNumeroVendibiliByLob(iOGruppoCommercialeNazione, products, referenceThisYearId,
                referenceLastYearId);
        log.debug("\tvendibiliNazione: " + vendibiliNazione);
        int vendutiInizioAnno = calcolaNumeroVendutiInizoAnnoByLob(iOGruppoCommerciale, products, referenceLastYearId);
        log.debug("\tvendutiInizioAnno: " + vendutiInizioAnno);
        lobSintesiDto.setIndicePenetrazione(Math.round(new Float(venduti * 100) / new Float(vendibili)));
        lobSintesiDto.setIndicePenetrazioneIniziale(Math.round(new Float(vendutiInizioAnno * 100)
                / new Float(vendibili)));
        lobSintesiDto.setIndicePenetrazioneTotale(Math.round(new Float(vendutiNazione * 100)
                / new Float(vendibiliNazione)));

    }
}
