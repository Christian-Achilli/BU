package com.kp.malice.entities.persisted;

import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.kp.malice.MalicePropertyContainer;

/**
 * Provide a session factory
 */
public class HibernateSessionFactoryUtil {

    private static Logger log = Logger.getLogger(HibernateSessionFactoryUtil.class);
    private static Configuration configuration;
    private static SessionFactory sessionFactory;
    private static final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();
    private static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();

    static {
        String hibernateConfigurationFile = MalicePropertyContainer.getHibernateConfigurationFileName();
        configuration = new Configuration().configure(hibernateConfigurationFile);
        addAnnotatedClassesToConfiguration();
        String connString = System.getProperty("JDBC_CONNECTION_STRING");
        if (StringUtils.isNotEmpty(connString))
            configuration.setProperty("hibernate.connection.url", connString);
        sessionFactory = configuration.buildSessionFactory();
    }

    public HibernateSessionFactoryUtil() {
    }

    private static void addAnnotatedClassesToConfiguration() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        String[] MAPPING_PACKAGES = MalicePropertyContainer.getPackagesWithEntities();
        if (MAPPING_PACKAGES != null) {
            for (int j = 0; j < MAPPING_PACKAGES.length; j++) {
                for (BeanDefinition bd : scanner.findCandidateComponents(MAPPING_PACKAGES[j])) {
                    String name = bd.getBeanClassName();
                    try {
                        configuration.addAnnotatedClass(Class.forName(name));
                    } catch (Exception e) {
                        log.error("Exception loading Entity " + name);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static Session getSession() throws RuntimeException {
        Session s = (Session) threadSession.get();
        try {
            if (s == null) {
                log.trace("Opening new Session for this thread.");
                s = sessionFactory.openSession();
                threadSession.set(s);
            }
        } catch (Exception ex) {
            throw new RuntimeException("ECCEZIONE IN APERTURA SESSIONE DATABASE", ex);
        }
        return s;
    }

    public static void closeSessionFactory() throws RuntimeException {
        synchronized (sessionFactory) {
            try {
                log.trace("HibernateUtil.closeSessionFactory() - Destroy the current SessionFactory.");
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

    public static void closeSession() throws RuntimeException {
        try {
            Session s = (Session) threadSession.get();
            threadSession.set(null);
            if (s != null && s.isOpen()) {
                log.trace("Closing Session of this thread.");
                s.close();
            }
        } catch (Exception ex) {
            throw new RuntimeException("ECCEZIONE IN CHIUSURA SESSIONE", ex);
        }
    }

    public static void beginTransaction() throws RuntimeException {
        Transaction tx = (Transaction) threadTransaction.get();
        try {
            if (tx == null) {
                log.trace("Starting new database transaction in this thread.");
                tx = getSession().beginTransaction();
                threadTransaction.set(tx);
            }
        } catch (Exception ex) {
            throw new RuntimeException("ECCEZIONE IN APERTURA TRANSAZIONE", ex);
        }
    }

    public static void commitTransaction() throws RuntimeException {
        Transaction tx = (Transaction) threadTransaction.get();
        try {
            if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
                log.trace("Committing database transaction of this thread.");
                tx.commit();
            }
            threadTransaction.set(null);
        } catch (Exception ex) {
            log.error("Errore: "+ex.getMessage());
            rollbackTransaction();
            throw new RuntimeException("ECCEZIONE IN SALVATAGGIO", ex);
        }
    }

    public static void rollbackTransaction() {
        Transaction tx = (Transaction) threadTransaction.get();
        try {
            threadTransaction.set(null);
            if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
                tx.rollback();
            }
        } catch (Exception ex) {
            throw new RuntimeException("ECCEZIONE IN ROLLBACK", ex);
        } finally {
            closeSession();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getPersistedInstance(Class<T> type, long id) {
        return (T) getSession().get(type, id);
    }

}
