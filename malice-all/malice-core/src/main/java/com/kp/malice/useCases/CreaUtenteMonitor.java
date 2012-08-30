package com.kp.malice.useCases;

import org.mindrot.jbcrypt.BCrypt;

public class CreaUtenteMonitor {

//    private static Logger log = Logger.getLogger(ImportaAnagraficaAgenzieRMADaExcel.class);

    public static void creaAccountUtenteMonitor() {
    	System.out.println(BCrypt.hashpw("super123", BCrypt.gensalt()));
//        RecordIdentifier timestampCreazione = RecordIdentifier.getInitRecord();
//        // Viene creato Account CreaSuperUtente
//        UserMalice userMonitor = new UserMalice(timestampCreazione);
//        userMonitor.setCognome("");
//        userMonitor.setNome("SuperAgente");
//        HibernateSessionFactoryUtil.beginTransaction();
//        HibernateSessionFactoryUtil.getSession().save(userMonitor);
//        HibernateSessionFactoryUtil.commitTransaction();
//
//        AccountMalice accAdmin = new AccountMalice(timestampCreazione);
//        accAdmin.setAbilitato(true);
//        accAdmin.setPassword(BCrypt.hashpw("super123", BCrypt.gensalt()));
//        accAdmin.setUsername("superAgente");
//        accAdmin.setEmail("dariobrambilla@kubepartners.com");
//        accAdmin.setUtente(userMonitor);
//        HibernateSessionFactoryUtil.beginTransaction();
//        HibernateSessionFactoryUtil.getSession().save(accAdmin);
//        HibernateSessionFactoryUtil.commitTransaction();
//
//        prendiTuttiPuntiVendita(accAdmin, timestampCreazione);
    }

//    private static void prendiTuttiPuntiVendita(AccountMalice accMonitor, RecordIdentifier timestampCreazione) {
//
//        Criteria criteria = HibernateSessionFactoryUtil.getSession().createCriteria(RuoloApplicativo.class);
//        criteria.add(Restrictions.eq("tipoRuolo", TipoRuolo.ROLE_MONITOR));
//        RuoloApplicativo ruoloAgente = (RuoloApplicativo) criteria.list().get(0);
//        criteria = HibernateSessionFactoryUtil.getSession().createCriteria(PunVnd.class);
//        List<PunVnd> punVndList = new ArrayList<PunVnd>();
//        punVndList = criteria.list();
//        log.debug("users: " + punVndList.size());
//        //
//        //        for (PunVnd puntoVnd : punVndList) {
//        //            AccountPunVndRuolo accPvRolAgenteAdmin = new AccountPunVndRuolo(timestampCreazione);
//        //            accPvRolAgenteAdmin.setRuolo(ruoloAgente);
//        //            accPvRolAgenteAdmin.setAccount(accMonitor);
//        //            accPvRolAgenteAdmin.setPuntoVendita(puntoVnd);
//        //            HibernateSessionFactoryUtil.beginTransaction();
//        //            HibernateSessionFactoryUtil.getSession().save(accPvRolAgenteAdmin);
//        //            HibernateSessionFactoryUtil.commitTransaction();
//        //        }
//    }
}
