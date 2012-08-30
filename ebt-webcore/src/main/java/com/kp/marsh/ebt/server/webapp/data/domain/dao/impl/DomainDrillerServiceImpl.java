package com.kp.marsh.ebt.server.webapp.data.domain.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.StaleStateException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kp.marsh.ebt.server.webapp.data.domain.BusinessInformation;
import com.kp.marsh.ebt.server.webapp.data.domain.InformationOwners;
import com.kp.marsh.ebt.server.webapp.data.domain.Products;
import com.kp.marsh.ebt.server.webapp.data.domain.ReferenceYears;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.DomainDrillerService;
import com.kp.marsh.ebt.server.webapp.utils.AnnotatedHibernateUtil;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;
import com.kp.marsh.ebt.shared.dto.OwnerType;

@Singleton
public class DomainDrillerServiceImpl implements DomainDrillerService {

    private static Logger log = Logger.getLogger(DomainDrillerServiceImpl.class);

    @Inject
    private AnnotatedHibernateUtil connection;

    public List<BusinessInformation> findBusinessInformationByExample(int informationOwneroID, int yearID, int productID) {
        log.debug("finding BusinessInformation instance by example");
        List<BusinessInformation> results;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("from BusinessInformation bi "
                    + "where bi.informationOwners.id =:marshClientId " + "and bi.referenceYears.id = :myYearId "
                    + "and bi.products.id =:productId ");
            query.setParameter("marshClientId", informationOwneroID);
            query.setParameter("myYearId", yearID);
            query.setParameter("productId", productID);

            results = query.list();
            log.debug("find by example successful, result size: " + results.size());
            tx.commit();
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            tx.rollback();
            throw re;
        } finally {
            session.close();
        }
        return results;
    }

    @Override
    public List<BusinessInformation> getAvailablePotentialBusinessInformationByProduct(int clientExecutiveId,
            int producId, int year) {
        List<BusinessInformation> collections = null;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            List<InformationOwners> ioList = findInformationOwnerByParentId(clientExecutiveId);
            Query query = session.createQuery("from BusinessInformation bi " + "where bi.products.id =:producId "
                    + "and bi.referenceYears.id = :myYear " + "and bi.valueType in (:allowedTypes) "
                    + "and bi.informationOwners in (:allowedClients) " + "order by bi.created desc");
            query.setParameter("producId", producId);
            query.setParameter("myYear", year);
            query.setParameterList("allowedClients", ioList);
            List<String> parList = new ArrayList<String>();
            parList.add(BusinessInfoValueType.POTENTIAL.name());
            parList.add(BusinessInfoValueType.BROKER_POT.name());
            parList.add(BusinessInfoValueType.COMPANY_POT.name());
            query.setParameterList("allowedTypes", parList);
            collections = query.list();
            tx.commit();
        } catch (HibernateException ex) {
            log.error("Exception in DomainDrillerServiceImpl.getAvailablePotentialBusinessInformationByProduct.", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }

        return collections;
    }

    public List<BusinessInformation> getAvailableBusinessInformationByType(int informationOwnerId, int yearId,
            BusinessInfoValueType valueType) {
        List<BusinessInformation> collections;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("from BusinessInformation bi "
                    + "where bi.informationOwners.id =:marshClientId " + "and bi.referenceYears.id =:myYear "
                    + "and bi.valueType =:valueType " + "order by bi.created desc");
            query.setParameter("marshClientId", informationOwnerId);
            query.setParameter("myYear", yearId);
            query.setParameter("valueType", valueType.name());
            collections = query.list();
            tx.commit();
        } catch (HibernateException ex) {
            log.error("Exception in DomainDrillerServiceImpl.getAvailableBusinessInformation.", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }
        return collections;
    }

    public List<BusinessInformation> getAvailableBusinessInformationByTypeAndLobs(int informationOwnerId, int yearId,
            BusinessInfoValueType valueType, int[] lobIdArray) {
        List<BusinessInformation> collections;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("from BusinessInformation bi "
                    + "where bi.informationOwners.id =:marshClientId " + "and bi.referenceYears.id =:myYear "
                    + "and bi.valueType =:valueType " + "and bi.valueType in (:allowedProductList) "
                    + "order by bi.created desc");
            query.setParameter("marshClientId", informationOwnerId);
            query.setParameter("myYear", yearId);
            query.setParameter("valueType", valueType.name());
            collections = query.list();
            tx.commit();
        } catch (HibernateException ex) {
            log.error("Exception in DomainDrillerServiceImpl.getAvailableBusinessInformation.", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }

        return collections;
    }

    public List<InformationOwners> getInformationOwnersChildren(int parent) {

        List<InformationOwners> collections;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {

            String SQL_QUERY = "from InformationOwners as io " + "where io.parentId = :parentId "
                    + "and io.enabled = 1" + "order by io.description asc";

            Query query = session.createQuery(SQL_QUERY);

            query.setParameter("parentId", parent);

            collections = query.list();

            for (InformationOwners informationOwners : collections) {
                Hibernate.initialize(informationOwners.getBusinessInformations());
            }

            tx.commit();
        } catch (HibernateException ex) {
            log.error("Exception in DomainDrillerServiceImpl.getInformationOwnersChildren.", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }

        return collections;

    }

    public List<ReferenceYears> getAvailableYears() {
        List<ReferenceYears> collections;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {

            collections = session.createCriteria(ReferenceYears.class).addOrder(Order.desc("created")).list();
            tx.commit();

        } catch (HibernateException ex) {
            log.error("Exception in DomainDrillerServiceImpl.getAvailabelRefYears.", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }

        return collections;
    }

    @Override
    public List<Products> getAvailableLOB() {
        List<Products> collections;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            collections = session.createQuery("from Products as prd where prd.itemType = 'LOB' and prd.enabled = true")
                    .list();
            tx.commit();
        } catch (HibernateException ex) {
            log.error("Exception in DomainDrillerService.getAvailableProducts.", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }
        return collections;
    }

    @Override
    public List<Products> getAvailableProductsByLOB(Products lob) {
        List<Products> collections;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {

            Query q = session
                    .createQuery("from Products as prd where prd.parentId = :id and prd.enabled = true and prd.itemType = 'PRODUCT'");
            q.setParameter("id", lob.getParentId());
            collections = q.list();
            tx.commit();
        } catch (HibernateException ex) {
            log.error("Exception in DomainDrillerService.getAvailableProducts.", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }

        return collections;
    }

    public List<Integer> getAvailableProductsIdByLobId(int lobId) {
        List<Integer> collections = null;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query q = session
                    .createQuery("select id from Products as prd where prd.parentId = :id and prd.enabled = true and prd.itemType = 'PRODUCT'");
            q.setParameter("id", "" + lobId);
            collections = q.list();
            log.debug("I prodotti di questa lob sono: " + collections.size());
            tx.commit();
        } catch (HibernateException ex) {
            log.error("Exception in DomainDrillerService.getAvailableProducts.", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }

        return collections;
    }

    @Override
    public void pushToDB(BusinessInformation myInfo) {
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            if (myInfo.getId() == null) {
                session.save(myInfo);
            } else {
                session.merge(myInfo);
            }
            tx.commit();
        } catch (HibernateException ex) {
            log.error("Exception in DomainDrillerService.pushToDB.", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }

    }

    @Override
    public InformationOwners findInformationOwnerById(int marshClientId) {
        log.debug("getting InformationOwners instance with id: " + marshClientId);
        InformationOwners instance;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            instance = (InformationOwners) session.get(InformationOwners.class, marshClientId);
            if (instance == null) {
                log.debug("get successful, no instance found");
            } else {
                log.debug("get successful, instance found");
            }
            tx.commit();
        } catch (RuntimeException re) {
            log.error("get failed", re);
            tx.rollback();
            throw re;
        } finally {
            session.close();
        }
        return instance;
    }

    @Override
    public List<InformationOwners> findInformationOwnerByParentId(int parentId) {
        log.debug("getting InformationOwners instance with id: " + parentId);
        List<InformationOwners> collections;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {

            Query query = session.createQuery("from InformationOwners io " + "where io.parentId =:parentId "
                    + "and io.enabled=1" + "order by io.description asc");

            query.setParameter("parentId", parentId);

            collections = query.list();
            tx.commit();
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        } finally {
            session.close();
        }
        return collections;
    }

    @Override
    public Products findProductById(int productId) {
        Products result = null;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            result = (Products) session.get(Products.class, productId);
            tx.commit();
        } catch (HibernateException ex) {
            log.error("Exception in findProductById", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public BusinessInformation findBusinessInformationById(int busInfoId) {
        BusinessInformation result = null;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            result = (BusinessInformation) session.get(BusinessInformation.class, busInfoId);
            tx.commit();
        } catch (HibernateException ex) {
            log.error("Exception in findBusinessInformationById", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public void deleteBusinessInfo(BusinessInformation persistentBInfo) {
        if (persistentBInfo.getId() != null) {
            Session session = connection.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            try {
                // persistentBInfo =
                // (BusinessInformation)session.merge(persistentBInfo);
                session.delete(persistentBInfo);
                session.getTransaction().commit();
            } catch (StaleStateException e) {
                BusinessInformation bi = findBusinessInformationById(persistentBInfo.getId());
                session.delete(bi);
                session.getTransaction().commit();
            } catch (HibernateException ex) {
                log.error("Exception in deleteBusinessInfo", ex);
                tx.rollback();
                throw ex;
            } finally {
                session.close();
            }
        }
    }

    @Override
    public int getLOBTotalByLobId(int clientExecutive, String lobId, ReferenceYears year,
            BusinessInfoValueType valueType) {
        List<BusinessInformation> collections;
        int totale = 0;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {

            List<InformationOwners> ioList = findInformationOwnerByParentId(clientExecutive);
            Query query = session.createQuery("from BusinessInformation bi " + "where bi.products.parentId =:lobId " + // all the products of the given lob
                    "and bi.informationOwners in (:allowedClientList) " + // all the info regarding the given client executive's
                                                                          // clients
                    "and bi.referenceYears =:myYear " + "and bi.valueType =:valueType " + "order by bi.created desc");

            query.setParameter("lobId", lobId);
            query.setParameter("myYear", year);
            query.setParameter("valueType", valueType.name());
            query.setParameterList("allowedClientList", ioList);

            collections = query.list();

            for (BusinessInformation businessInformation : collections) {
                if (businessInformation.getValueAmount().matches("\\d{0,}")) {
                    totale += Integer.parseInt(businessInformation.getValueAmount());
                }

            }

            tx.commit();
        } catch (HibernateException ex) {
            log.error("Exception in getLOBTotalByLobId.", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }

        return totale;
    }

    public int getReferenceYearIdFromYear(int year) {
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        int referenceYears;
        try {
            Query yearQuery = session.createQuery("select id from " + ReferenceYears.class
                    + " where referenceYear = :year");
            yearQuery.setParameter("year", year);
            referenceYears = (Integer) yearQuery.uniqueResult();
            tx.commit();
        } catch (HibernateException ex) {
            log.error("Exception in getReferenceYearIdFromYear.", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }
        return referenceYears;
    }

    public List<Integer> findGruppoCommerciale(int id) {
        log.debug("finding client from id: " + id);
        InformationOwners informationOwners = findInformationOwnerById(id);
        List<Integer> results;
        if (informationOwners == null)
            return null;
        String ownerTypeStr = informationOwners.getOwnerType();
        String queryString = "";
        switch (OwnerType.fromString(ownerTypeStr)) {
        // TODO CORPORATE
        case NATION:
            queryString = "select id from InformationOwners where owner_type = 'GRUPPO_COMMERCIALE'  and enabled=1";
            id = -234567;// dummy marker
            break;
        case AREA:
            queryString = "select id from InformationOwners where parentId in (select id from InformationOwners where parentId in (select id from InformationOwners where parentId in (select id from InformationOwners where parentId = :id and enabled=1) and enabled=1) and enabled=1) and enabled=1";
            break;
        case OFFICE:
            queryString = "select id from InformationOwners where parentId in (select id from InformationOwners where parentId = :id and enabled=1) and enabled=1";
            break;
        case CE:
            queryString = "select id from InformationOwners where parentId = :id and enabled=1";
            break;
        case GRUPPO_COMMERCIALE:
            queryString = "select id from InformationOwners where id = :id and enabled=1";
            break;
        }
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery(queryString);
            if (id != -234567)
                query.setParameter("id", id);
            results = query.list();
            tx.commit();
            log.debug("Found " + results.size() + " items.");
        } catch (RuntimeException re) {
            log.error("Fail finding clients by id: " + id, re);
            tx.rollback();
            throw re;
        } finally {
            session.close();
        }
        return results;
    }

    public List<Integer> getAllLobsId() {
        List<Integer> results;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("select id from Products where itemType = :itemType and enabled = 1");
            query.setParameter("itemType", "LOB");
            results = query.list();
            log.debug("result size: " + results.size());
            tx.commit();
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            tx.rollback();
            throw re;
        } finally {
            session.close();
        }
        return results;
    }

    public List<InformationOwners> findInformationOwnerClientInList(List<InformationOwners> list) {
        log.debug("getting InformationOwners instance with parentid in list");
        List<Integer> ids = new ArrayList<Integer>();
        for (Iterator<InformationOwners> iterator = list.iterator(); iterator.hasNext();) {
            InformationOwners informationOwners = iterator.next();
            ids.add(informationOwners.getId());
        }
        List<InformationOwners> collections;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session
                    .createQuery("from InformationOwners io where io.parentId in (:ids) and io.enabled = true");
            query.setParameterList("ids", ids);
            collections = query.list();
            tx.commit();
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        } finally {
            session.close();
        }
        if (!collections.isEmpty()) {
            return findInformationOwnerClientInList(new ArrayList<InformationOwners>(collections));
        }
        return list;
    }

    @Override
    public ReferenceYears getEnabledReferenceYear() {
        ReferenceYears result;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session
                    .createQuery("from ReferenceYears ry where :now between ry.editableFrom and ry.editableUntil");
            Date now = new Date();
            query.setParameter("now", now);
            result = (ReferenceYears) query.uniqueResult();
            tx.commit();

        } catch (HibernateException ex) {
            log.error("Exception in DomainDrillerServiceImpl.getEnabledReferenceYear", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }

        return result;
    }

    @Override
    public ReferenceYears findReferenceYearByNumber(int i) {
        ReferenceYears result;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("from ReferenceYears ry where ry.referenceYear = :year");
            query.setParameter("year", i);
            result = (ReferenceYears) query.uniqueResult();
            tx.commit();
        } catch (HibernateException ex) {
            log.error("Exception in DomainDrillerServiceImpl.getEnabledReferenceYear", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public void mergeInformationOwner(InformationOwners object) {
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.merge(object);
            session.getTransaction().commit();
        } catch (HibernateException ex) {
            log.error("Exception in DomainDrillerService.mergeInformationOwner.", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }
    }

    public List<InformationOwners> getUffici() {
        List<InformationOwners> collections;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session
                    .createQuery("from InformationOwners io where io.ownerType =:ownerType and io.enabled=1 order by io.description asc");
            query.setParameter("ownerType", OwnerType.OFFICE.name());
            collections = query.list();
            // Aggiungo la nazione ITALIA all'inizio della lista
            Query query2 = session
                    .createQuery("from InformationOwners io where io.ownerType =:ownerType and io.enabled=1");
            query2.setParameter("ownerType", OwnerType.NATION.name());
            collections.add(0, (InformationOwners) query2.list().get(0));
            tx.commit();
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;

        } finally {
            session.close();

        }
        return collections;
    }

    public List<InformationOwners> getCE(int id) {
        List<InformationOwners> collections;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session
                    .createQuery("from InformationOwners io where io.ownerType =:ownerType and io.parentId=:parentId  and io.enabled=1  order by io.description asc");
            query.setParameter("ownerType", OwnerType.CE.name());
            query.setParameter("parentId", id);
            collections = query.list();
            tx.commit();

        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;

        } finally {
            session.close();

        }
        return collections;
    }

    public List<InformationOwners> getGC(int id) {
        List<InformationOwners> collections;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session
                    .createQuery("from InformationOwners io where io.ownerType =:ownerType and io.parentId=:parentId  and io.enabled=1 order by io.description asc");
            query.setParameter("ownerType", OwnerType.GRUPPO_COMMERCIALE.name());
            query.setParameter("parentId", id);
            collections = query.list();
            tx.commit();

        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;

        } finally {
            session.close();

        }
        return collections;
    }

    public void cleanTable(String tableName) {
        Session session = (Session) this.connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            String sql = "TRUNCATE " + tableName;
            SQLQuery sqlQuery = session.createSQLQuery(sql);
            int res = sqlQuery.executeUpdate();
            log.debug("delete result: " + res);
            tx.commit();
        } catch (RuntimeException re) {
            log.error("Errore durante eliminazione dei dati dalla table " + tableName, re);
            tx.rollback();
            throw re;
        } finally {
            session.close();
        }
    }

    @Override
    public InformationOwners findInformationOwnerByEysCode(String codEys) {
        log.debug("Finding information owners by codEys: " + codEys);
        InformationOwners informationOwner = null;
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("from InformationOwners io " + "where io.codEys =:codEys "
                    + "and io.enabled=1" + "order by io.description asc");
            query.setParameter("codEys", codEys);
            informationOwner = (InformationOwners) query.uniqueResult();
            tx.commit();
        } catch (NonUniqueResultException e) {
            log.error(e.getMessage() + " with findInformationOwnerByEysCode, codEys: " + codEys);
            e.printStackTrace();
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        } finally {
            session.close();
        }
        if (informationOwner != null)
            log.trace("Found information owners with codEys: " + codEys + " and id: " + informationOwner.getId());
        else
            log.trace("NOT Found information owners with codEys: " + codEys);
        return informationOwner;
    }

}
