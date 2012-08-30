package com.kp.malice.repositories;

import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTimeZone;

import com.google.inject.Inject;
import com.kp.malice.entities.business.FilieraLloyds;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.RecordIdentifier;
import com.kp.malice.entities.persisted.RegisteredDocument;
import com.kp.malice.entities.xml.Document;
import com.kp.malice.entities.xml.ImportDataSet;
import com.kp.malice.factories.FilieraLloydsFactory;

public class DatabaseRegistroXml implements FlussoXmlRepository {

    public static final Logger log = Logger.getLogger(DatabaseRegistroXml.class);

    private final FilieraLloydsFactory lloydsObjFact;

    @Inject
    public DatabaseRegistroXml(FilieraLloydsFactory lloydsObjFact) {
        this.lloydsObjFact = lloydsObjFact;
    }

    @Override
    public Long add(ImportDataSet xmlDataSet, String xmlString, String encoding, boolean valid,
            String validationErrorMessage) throws Exception {
        Document doc = xmlDataSet.getDocument();
        FilieraLloyds filiera = lloydsObjFact.getFiliera(xmlDataSet);
        String numInstalments = getNumeroRata(doc);
        String numCertificate = getNumCertificato(doc);
        String numAppendice = getNumAppendice(doc);
        log.info("numInstalments: " + numInstalments + "      numCertificate: " + numCertificate + " numAppendice: "
                + numAppendice);
        RegisteredDocument regDoc = null;
        try {
            regDoc = new RegisteredDocument(RecordIdentifier.getInitRecord());
            regDoc.setFromBroker(filiera.getBroker().getDescription());
            regDoc.setForAgenzia(filiera.getAgenziaRma().getShortDescription());
            regDoc.setNumCertificato(numCertificate);
            regDoc.setNumAppendice(numAppendice);
            regDoc.setDocumentIsValid(valid);
            regDoc.setValidationErrorMessage(validationErrorMessage);
            regDoc.setXmlStr(xmlString);
            regDoc.setEncoding(encoding);
            HibernateSessionFactoryUtil.beginTransaction();
            HibernateSessionFactoryUtil.getSession().save(regDoc);
            HibernateSessionFactoryUtil.commitTransaction();
        } catch (Exception e) {
            log.error("EXCEPTION ADDING AN XML DOCUMENT TO DATABASE");
            e.printStackTrace();
            throw new RuntimeException("EXCEPTION ADDING AN XML DOCUMENT TO DATABASE");
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
        return regDoc != null ? regDoc.getRecordId() : null;
    }

    private String getNumeroRata(Document doc) {
        return "" + doc.getTotalPremiumDetails().getNumberOfInstalments();
    }

    private String getNumAppendice(Document doc) {
        return doc.getDocumentReferences().getCertificateICNumber();
    }

    private String getNumCertificato(Document doc) {
        return StringUtils.isEmpty(doc.getDocumentReferences().getICNumber()) ? getNumAppendice(doc) : doc
                .getDocumentReferences().getICNumber();
    }

    @Override
    public RegisteredDocument find(Long id) throws Exception {
        RegisteredDocument doc = null;
        try {
            doc = (RegisteredDocument) HibernateSessionFactoryUtil.getSession().get(RegisteredDocument.class, id);
        } catch (Exception e) {
            log.error("EXCEPTION FINDING AN XML DOCUMENT IN DATABASE");
            e.printStackTrace();
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
        return doc;
    }

    public List<RegisteredDocument> findAll() throws Exception {
        List<RegisteredDocument> docs = null;
        try {
            docs = HibernateSessionFactoryUtil.getSession().createCriteria(RegisteredDocument.class).list();
        } catch (Exception e) {
            log.error("EXCEPTION FINDING ALL XML DOCUMENT IN DATABASE");
            e.printStackTrace();
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
        return docs;
    }

    @Override
    public List<RegisteredDocument> findAllNotWorked() throws Exception {
        List<RegisteredDocument> docsToWork = null;
        try {
            Criteria criteria = HibernateSessionFactoryUtil.getSession().createCriteria(RegisteredDocument.class);
            criteria.add(Restrictions.eq("worked", false));
            criteria.add(Restrictions.eq("isSchemaConsistent", true));
            docsToWork = criteria.list();
            log.debug("found " + docsToWork.size() + "xml not worked");
        } catch (Exception e) {
            log.error("EXCEPTION FINDING ALL NOT WORKED XML DOCUMENT IN DATABASE");
            e.printStackTrace();
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
        return docsToWork;
    }

    @Override
    public void delete(Long id) throws Exception {
        HibernateSessionFactoryUtil.beginTransaction();
        try {
            HibernateSessionFactoryUtil.getSession().delete(
                    HibernateSessionFactoryUtil.getSession().get(RegisteredDocument.class, id));
            HibernateSessionFactoryUtil.commitTransaction();
        } catch (Exception e) {
            log.error("EXCEPTION DELETING XML DOCUMENT FROM DATABASE");
            e.printStackTrace();
            HibernateSessionFactoryUtil.rollbackTransaction();
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    public int deleteAll() {
        HibernateSessionFactoryUtil.beginTransaction();
        int rowsAffected = 0;
        try {
            Session session = HibernateSessionFactoryUtil.getSession();
            Query query = session.createQuery("delete from " + RegisteredDocument.class.getSimpleName());
            rowsAffected = query.executeUpdate();
            HibernateSessionFactoryUtil.commitTransaction();
        } catch (Exception e) {
            log.error("EXCEPTION DELETING ALL XML DOCUMENT FROM DATABASE");
            e.printStackTrace();
            HibernateSessionFactoryUtil.rollbackTransaction();
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
        log.info("Eliminati " + rowsAffected + " record da RegisteredDocument");
        return rowsAffected;
    }

    public int countRecords() {
        int rows = 0;
        try {
            Session session = HibernateSessionFactoryUtil.getSession();
            Object rowsObj = session.createCriteria(RegisteredDocument.class).setProjection(Projections.rowCount())
                    .uniqueResult();
            rows = Integer.parseInt(rowsObj.toString());
        } catch (Exception e) {
            log.error("EXCEPTION COUNTING XML DOCUMENTS FROM THE DB");
            e.printStackTrace();
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
        return rows;
    }
}
