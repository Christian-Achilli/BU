package com.kp.marsh.ebt.server.webapp.utils;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.google.inject.Inject;

/**
 * Provide a session factory
 */
public class AnnotatedHibernateUtil {

    /** Logger. */
    private Log log = LogFactory.getLog(AnnotatedHibernateUtil.class);

    /** configuration. */
    private Configuration configuration;

    /** Session Factory */
    private SessionFactory sessionFactory;

    private Properties customDBprops = null;

    @Inject
    public AnnotatedHibernateUtil() {
    }

    /**
     * Returns the SessionFactory used for this  class. If offlineMode has
     * been set then we use hibernate.cfg.xml to create sessionfactory, if not
     * then we use sessionfactory bound to JNDI.
     *
     * @return SessionFactory
     */
    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            log.debug("HibernateUtil.getSessionFactory() - Using hibernate.cfg.xml to create a SessionFactory");
            try {
                configuration = new Configuration().configure();
                if (null != customDBprops) {
                    log.info("Ho trovato delle properties da inserire nella configurazione");
                    configuration.setProperties(customDBprops);
                    log.info("Ho inserito le properties nella configurazione");
                }
                sessionFactory = configuration.buildSessionFactory();
            } catch (HibernateException x) {
                log.error("Errore in getSessionFactory", x);
                throw new RuntimeException(
                        "HibernateUtil.getSessionFactory() - Error creating SessionFactory with hibernate.cfg.xml .", x);
            }
        }
        if (sessionFactory == null) {
            throw new IllegalStateException("HibernateUtil.getSessionFactory() - SessionFactory not available.");
        }
        log.trace("!!!!!!!!!! url connection: " + configuration.getProperty("hibernate.connection.url")
                + " !!!!!!!!!!!!");
        return sessionFactory;
    }

    /**
     * Sets the given SessionFactory.
     *
     * @param sessionFactory
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

    }

    /**
     * Returns the original Hibernate configuration.
     *
     * @return Configuration
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Destroy this SessionFactory and release all resources (caches, connection
     * pools, etc).
     *
     * @param cfg
     */
    public void closeSessionFactory() {
        synchronized (sessionFactory) {
            try {
                log.debug("HibernateUtil.closeSessionFactory() - Destroy the current SessionFactory.");
                sessionFactory.close();
                // Clear  variables
                configuration = null;
                sessionFactory = null;
            } catch (Exception x) {
                throw new RuntimeException(
                        "HibernateUtil.closeSessionFactory() - Error destroying the current SessionFactory", x);
            }
        }
    }

    public void setDBConnectionProperties(Properties p) {
        this.customDBprops = p;
        if (null != sessionFactory)
            sessionFactory.close();
        sessionFactory = null;
    }

}
