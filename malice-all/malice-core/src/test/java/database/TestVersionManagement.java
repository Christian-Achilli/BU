package database;
import static org.junit.Assert.*;

import org.junit.Test;

import com.kp.malice.entities.persisted.AliasName;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.RecordIdentifier;

public class TestVersionManagement {

    @Test
    public void test_version_increment() throws Exception {
        RecordIdentifier timestampCreazione = RecordIdentifier.getInitRecord();
        AliasName alias1 = new AliasName(timestampCreazione, "1-Intermediary", "AN-IM");
        HibernateSessionFactoryUtil.beginTransaction();
        HibernateSessionFactoryUtil.getSession().save(alias1);
        HibernateSessionFactoryUtil.commitTransaction();
        HibernateSessionFactoryUtil.closeSession();

        final Long id = alias1.getRecordId();
        final Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                AliasName alias1;
                HibernateSessionFactoryUtil.beginTransaction();
                alias1 = (AliasName) HibernateSessionFactoryUtil.getSession().get(AliasName.class, id);
                alias1.setCodUteAgr("Thread 1 " + Math.random());
                System.out.println("Thread 1:From version " + alias1.getVersion());
                HibernateSessionFactoryUtil.getSession().saveOrUpdate(alias1);
                HibernateSessionFactoryUtil.commitTransaction();
                System.out.println("Thread 1:To version " + alias1.getVersion());
                HibernateSessionFactoryUtil.closeSession();
                incrementVersion(id);
                incrementVersion(id);
            }
        });
        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                AliasName alias1;
                HibernateSessionFactoryUtil.beginTransaction();
                alias1 = (AliasName) HibernateSessionFactoryUtil.getSession().get(AliasName.class, id);
                alias1.setCodUteAgr("Thread 2 " + Math.random());
                System.out.println("Thread 2 :From version " + alias1.getVersion());
                t.run();
                HibernateSessionFactoryUtil.getSession().saveOrUpdate(alias1);
                HibernateSessionFactoryUtil.commitTransaction();
                System.out.println("Thread 2 :To version " + alias1.getVersion());
                HibernateSessionFactoryUtil.closeSession();

            }
        });

        incrementVersion(id);
        alias1 = incrementVersion(id);
        t2.run();
        assertEquals(Long.valueOf("5"), alias1.getVersion());

    }

    private AliasName incrementVersion(Long id) {
        AliasName alias1;
        HibernateSessionFactoryUtil.beginTransaction();
        alias1 = (AliasName) HibernateSessionFactoryUtil.getSession().get(AliasName.class, id);
        alias1.setCodUteAgr("cambio 1 " + Math.random());
        System.out.println("From version " + alias1.getVersion());
        HibernateSessionFactoryUtil.getSession().saveOrUpdate(alias1);
        HibernateSessionFactoryUtil.commitTransaction();
        System.out.println("\tTo version " + alias1.getVersion());
        HibernateSessionFactoryUtil.closeSession();
        return alias1;
    }
}
