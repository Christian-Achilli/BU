package com.kp.malice.useCases;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.mindrot.jbcrypt.BCrypt;

import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.PunVnd;
import com.kp.malice.entities.persisted.RecordIdentifier;
import com.kp.malice.entities.persisted.authentication.AccountMalice;
import com.kp.malice.entities.persisted.authentication.RuoloApplicativo;
import com.kp.malice.entities.persisted.authentication.RuoloApplicativo.TipoRuolo;
import com.kp.malice.entities.persisted.authentication.UserMalice;

public class CreaUtenteMonitor {

    private static Logger log = Logger.getLogger(ImportaAnagraficaAgenzieRMADaExcel.class);

    public static void creaAccountUtenteMonitor() {

        RecordIdentifier timestampCreazione = RecordIdentifier.getInitRecord();
        // Viene creato Account CreaSuperUtente
        UserMalice userMonitor = new UserMalice(timestampCreazione);
        userMonitor.setCognome("");
        userMonitor.setNome("Monitor");
        HibernateSessionFactoryUtil.beginTransaction();
        HibernateSessionFactoryUtil.getSession().save(userMonitor);
        HibernateSessionFactoryUtil.commitTransaction();

        AccountMalice accAdmin = new AccountMalice(timestampCreazione);
        accAdmin.setAbilitato(true);
        accAdmin.setPassword(BCrypt.hashpw("monit123", BCrypt.gensalt()));
        accAdmin.setUsername("kpmonitoring");
        accAdmin.setEmail("portale-gar@kubepartners.it");
        accAdmin.setUtente(userMonitor);
        HibernateSessionFactoryUtil.beginTransaction();
        HibernateSessionFactoryUtil.getSession().save(accAdmin);
        HibernateSessionFactoryUtil.commitTransaction();

        prendiTuttiPuntiVendita(accAdmin, timestampCreazione);
    }

    private static void prendiTuttiPuntiVendita(AccountMalice accMonitor, RecordIdentifier timestampCreazione) {

        Criteria criteria = HibernateSessionFactoryUtil.getSession().createCriteria(RuoloApplicativo.class);
        criteria.add(Restrictions.eq("tipoRuolo", TipoRuolo.ROLE_MONITOR));
        RuoloApplicativo ruoloAgente = (RuoloApplicativo) criteria.list().get(0);
        criteria = HibernateSessionFactoryUtil.getSession().createCriteria(PunVnd.class);
        List<PunVnd> punVndList = new ArrayList<PunVnd>();
        punVndList = criteria.list();
        log.debug("users: " + punVndList.size());
        //
        //        for (PunVnd puntoVnd : punVndList) {
        //            AccountPunVndRuolo accPvRolAgenteAdmin = new AccountPunVndRuolo(timestampCreazione);
        //            accPvRolAgenteAdmin.setRuolo(ruoloAgente);
        //            accPvRolAgenteAdmin.setAccount(accMonitor);
        //            accPvRolAgenteAdmin.setPuntoVendita(puntoVnd);
        //            HibernateSessionFactoryUtil.beginTransaction();
        //            HibernateSessionFactoryUtil.getSession().save(accPvRolAgenteAdmin);
        //            HibernateSessionFactoryUtil.commitTransaction();
        //        }
    }
}
