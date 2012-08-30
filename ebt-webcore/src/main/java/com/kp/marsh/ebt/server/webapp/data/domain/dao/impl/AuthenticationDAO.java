package com.kp.marsh.ebt.server.webapp.data.domain.dao.impl;

// Generated Feb 1, 2011 12:09:56 PM by Hibernate Tools 3.4.0.CR1

import static org.hibernate.criterion.Example.create;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kp.marsh.ebt.server.webapp.data.domain.MarshPeopleCredentials;
import com.kp.marsh.ebt.server.webapp.utils.AnnotatedHibernateUtil;

/**
 * Home object for domain model class MarshPeopleCredentials.
 */
@Singleton
public class AuthenticationDAO {

    private int counter = 0; // remove if not needed.

    private Logger logger = Logger.getLogger(AuthenticationDAO.class);

    @Inject
    private AnnotatedHibernateUtil connection;

    /**
     * @param username
     * @param password
     * @return the available credential or null
     * @throws Exception 
     */
    public MarshPeopleCredentials getMarshPeopleCredentials(String username, String password) throws Exception {
        MarshPeopleCredentials example = new MarshPeopleCredentials();
        example.setUsername(username);
        //		example.setHashPassword(password); // La password è gestita da active directory
        MarshPeopleCredentials result = findCredentialsByExample(example);
        return result;
    }

    public MarshPeopleCredentials findCredentialsByExample(MarshPeopleCredentials instance) throws Exception {
        logger.debug("finding MarshPeopleCredentials instance by example");
        Session session = connection.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        MarshPeopleCredentials result = null;
        try {
            result = (MarshPeopleCredentials) session.createCriteria(MarshPeopleCredentials.class)
                    .add(create(instance)).uniqueResult();
            if (null != result) {
                Hibernate.initialize(result.getInformationOwners());
            }
            tx.commit();
        } catch (Exception ex) {
            logger.error("Exception in AuthenticationDAO.findCredentialsByExample().", ex);
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }
        return result;
    }

    public AnnotatedHibernateUtil getConnection() {
        return connection;
    }

    public void setConnection(AnnotatedHibernateUtil connection) {
        this.connection = connection;
    }

}
