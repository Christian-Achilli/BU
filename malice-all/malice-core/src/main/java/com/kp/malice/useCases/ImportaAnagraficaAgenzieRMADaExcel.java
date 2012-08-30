package com.kp.malice.useCases;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.mindrot.jbcrypt.BCrypt;

import com.google.inject.Inject;
import com.kp.malice.MalicePropertyContainer;
import com.kp.malice.entities.business.AgenziaRMA;
import com.kp.malice.entities.persisted.AliasName;
import com.kp.malice.entities.persisted.AnagAgenzieRMA;
import com.kp.malice.entities.persisted.CnlVnd;
import com.kp.malice.entities.persisted.CompPtf;
import com.kp.malice.entities.persisted.EncodingName;
import com.kp.malice.entities.persisted.EntityAlias;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.PunVnd;
import com.kp.malice.entities.persisted.RecordIdentifier;
import com.kp.malice.entities.persisted.UntaOperAun;
import com.kp.malice.entities.persisted.authentication.AccountMalice;
import com.kp.malice.entities.persisted.authentication.AccountPunVndRuolo;
import com.kp.malice.entities.persisted.authentication.RuoloApplicativo;
import com.kp.malice.entities.persisted.authentication.RuoloApplicativo.TipoRuolo;
import com.kp.malice.entities.persisted.authentication.UserMalice;
import com.kp.malice.factories.FilieraLloydsFactory;
import com.kp.malice.repositories.ChiusureMensiliLioRepository;

public class ImportaAnagraficaAgenzieRMADaExcel {
    private Logger log = Logger.getLogger(ImportaAnagraficaAgenzieRMADaExcel.class);
    private final FilieraLloydsFactory filieraLloyds;
    private final ChiusureMensiliLioRepository chRepo;

    @Inject
    public ImportaAnagraficaAgenzieRMADaExcel(ChiusureMensiliLioRepository chRepo, FilieraLloydsFactory filieraLloyds) {
        this.chRepo = chRepo;
        this.filieraLloyds = filieraLloyds;
    }

    public void importaExcelSulDb(String path) throws IOException, BiffException {
        Workbook wb = Workbook.getWorkbook(new File(path));
        Sheet sh = wb.getSheet(0);
        Session session = HibernateSessionFactoryUtil.getSession();
        Query query = session.createQuery("DELETE FROM " + AnagAgenzieRMA.class.getSimpleName());
        query.executeUpdate();

        for (int i = 1; i < sh.getRows(); i++) {
            AnagAgenzieRMA temp = new AnagAgenzieRMA();
            Cell[] myRow = sh.getRow(i);
            temp.setCodice(myRow[1].getContents());
            temp.setAgenzia(myRow[2].getContents());
            temp.setCompanyName(myRow[3].getContents());
            temp.setZipCode(myRow[4].getContents());
            temp.setEmail(myRow[5].getContents());
            temp.setTelephoneNumber(myRow[6].getContents());
            temp.setPin(myRow[7].getContents());
            temp.setPassword(myRow[8].getContents());
            HibernateSessionFactoryUtil.getSession().save(temp);
        }
        wb.close();

    }

    public void creaUOA_PunVnd_EntityAlias() throws Exception {

        RecordIdentifier timestampCreazione = RecordIdentifier.getInitRecord();

        Criteria criteria = HibernateSessionFactoryUtil.getSession().createCriteria(CnlVnd.class);
        criteria.add(Restrictions.not(Restrictions.eq("denCnlVnd", "MONITOR"))); // monitor
                                                                                 // va
                                                                                 // escluso
        List<CnlVnd> canaliVndList = criteria.list();

        criteria = HibernateSessionFactoryUtil.getSession().createCriteria(AnagAgenzieRMA.class);
        List<AnagAgenzieRMA> anagAgenziaList = criteria.list();

        CompPtf lloyds = HibernateSessionFactoryUtil.getPersistedInstance(CompPtf.class,
                MalicePropertyContainer.getLloydsRecordId());

        NumberFormat denominazionePvFormat = new DecimalFormat("0000");

        for (AnagAgenzieRMA anagAgenzia : anagAgenziaList) {

            // Vado a Popolare la tabella UOA
            UntaOperAun uoaCompanyName = salvaUoaSeNonEsiste(timestampCreazione, anagAgenzia);

            if (null == uoaCompanyName) {
                throw new Exception("uoaCompanyName NULL id agenzia: " + anagAgenzia.getId());
            }

            // Vado a Popolare la tabella Entity Alias
            salvaEntityAliasSeNonEsiste(timestampCreazione, anagAgenzia, uoaCompanyName);

            // Vado a Popolare la tabella Punto Vendita
            for (CnlVnd canaleVnd : canaliVndList) {
                criteria = HibernateSessionFactoryUtil.getSession().createCriteria(PunVnd.class);
                criteria.add(Restrictions.eq("untaOperAun", uoaCompanyName));
                criteria.add(Restrictions.eq("codCompPtf", lloyds.getCodCompPtf()));
                criteria.add(Restrictions.eq("codCnlVnd", canaleVnd.getCodCnlVnd()));
                PunVnd codUOA_pntVnd = (PunVnd) criteria.uniqueResult();
                if (null == codUOA_pntVnd) {
                    salvaPuntoVendita(lloyds, canaleVnd, uoaCompanyName,
                            denominazionePvFormat.format(uoaCompanyName.getRecordId()) + "-" + canaleVnd.getRecordId());
                }
            }

            // Vado a Popolare la Tabella Utente_Malice, Account Utente e
            // Account Punto Vendita Ruolo
            creaUtente(timestampCreazione, anagAgenzia, canaliVndList, uoaCompanyName, lloyds);

            log.debug("get agenzia rma from id uoa: " + uoaCompanyName.getRecordId());
            AgenziaRMA age = filieraLloyds.getAgenziaRma(uoaCompanyName);
            chRepo.creaNuovaChiusuraAperta(age, new DateTime());

        }

    }

    private void creaUtente(RecordIdentifier timestampCreazione, AnagAgenzieRMA anagAgenzia,
            List<CnlVnd> canaliVndList, UntaOperAun uoaCompanyName, CompPtf lloyds) {

        UserMalice user = new UserMalice(timestampCreazione);
        String email = anagAgenzia.getEmail();
        String nomeCompagnia = anagAgenzia.getCompanyName();
        String[] nome = email.split("\\.");
        log.debug("Dimensione: " + nome.length);
        String[] cognome = nomeCompagnia.split(" ");

        Criteria criteria = HibernateSessionFactoryUtil.getSession().createCriteria(UserMalice.class);
        criteria.add(Restrictions.eq("nome", nome[0].toUpperCase()));
        UserMalice users = (UserMalice) criteria.uniqueResult();
        if (null == users) {

            // Viene creato uno User
            user.setCognome(cognome[0]);
            user.setNome(nome[0].toUpperCase());

            HibernateSessionFactoryUtil.getSession().save(user);

            // Viene creato Account Malice
            AccountMalice acc = new AccountMalice(timestampCreazione);
            acc.setAbilitato(true);
            acc.setPassword(BCrypt.hashpw(anagAgenzia.getPassword(), BCrypt.gensalt()));
            acc.setUsername(email);
            acc.setEmail(email);
            acc.setUtente(user);
            HibernateSessionFactoryUtil.getSession().save(acc);

            criteria = HibernateSessionFactoryUtil.getSession().createCriteria(RuoloApplicativo.class);
            criteria.add(Restrictions.eq("tipoRuolo", TipoRuolo.ROLE_AGENTE));
            RuoloApplicativo ruoloAgente = (RuoloApplicativo) criteria.list().get(0);

            for (CnlVnd canaleVnd : canaliVndList) {
                AccountPunVndRuolo accPvRolAgente = new AccountPunVndRuolo(timestampCreazione);
                accPvRolAgente.setRuolo(ruoloAgente);
                accPvRolAgente.setAccount(acc);

                criteria = HibernateSessionFactoryUtil.getSession().createCriteria(PunVnd.class);
                criteria.add(Restrictions.eq("untaOperAun", uoaCompanyName));
                criteria.add(Restrictions.eq("codCompPtf", lloyds.getCodCompPtf()));
                criteria.add(Restrictions.eq("codCnlVnd", canaleVnd.getCodCnlVnd()));
                PunVnd codUOA_pntVnd = (PunVnd) criteria.uniqueResult();
                accPvRolAgente.setPuntoVendita(codUOA_pntVnd);

                HibernateSessionFactoryUtil.getSession().save(accPvRolAgente);
            }
        }
    }

    private UntaOperAun salvaUoaSeNonEsiste(RecordIdentifier timestampCreazione, AnagAgenzieRMA anagAgenzia) {
        String uoaDescription = anagAgenzia.getCompanyName();
        String shortDescription = anagAgenzia.getAgenzia() + "-" + anagAgenzia.getCodice();
        Criteria criteria = HibernateSessionFactoryUtil.getSession().createCriteria(UntaOperAun.class);
        criteria.setProjection(Projections.max("codUntaOperAun")); // trova max
                                                                   // codUntaOperAun
        Long codUOA = (Long) criteria.uniqueResult();
        criteria = HibernateSessionFactoryUtil.getSession().createCriteria(UntaOperAun.class);
        criteria.add(Restrictions.eq("description", uoaDescription)).add(
                Restrictions.eq("shortDescription", shortDescription));
        UntaOperAun uoaCompanyName = (UntaOperAun) criteria.uniqueResult();
        if (null == uoaCompanyName) {
            codUOA++;
            log.debug("creo uoa con codUOA: " + codUOA);
            uoaCompanyName = new UntaOperAun(timestampCreazione, codUOA, uoaDescription, shortDescription);
            String indMail = anagAgenzia.getEmail();
            String codDstNzle = anagAgenzia.getZipCode();
            String codNumTel = anagAgenzia.getTelephoneNumber();
            uoaCompanyName.setCodDstNzle(codDstNzle);
            uoaCompanyName.setCodNumTel(codNumTel);
            uoaCompanyName.setIndMail(indMail);
            HibernateSessionFactoryUtil.getSession().save(uoaCompanyName);
        } else {
            log.debug("gia' esiste uoa con codUOA: " + uoaCompanyName.getCodUntaOperAun());
        }
        log.debug("ritorno uoa con id: " + uoaCompanyName.getRecordId());
        return uoaCompanyName;
    }

    private void salvaEntityAliasSeNonEsiste(RecordIdentifier timestampCreazione, AnagAgenzieRMA anagAgenzia,
            UntaOperAun uoaCompanyName) {
        String codiceAgenziaLloyd = anagAgenzia.getPin();

        String codiceAgenziaRma = "RMA" + anagAgenzia.getCodice();
        String codiceAgenziaMithras = anagAgenzia.getCodice();

        Criteria criteria = HibernateSessionFactoryUtil.getSession().createCriteria(AliasName.class);
        criteria.add(Restrictions.eq("description", "1-Intermediary")).add(Restrictions.eq("shortDesc", "AN-IM"));
        AliasName aliasIntermediario = (AliasName) criteria.uniqueResult();

        criteria = HibernateSessionFactoryUtil.getSession().createCriteria(EncodingName.class);
        criteria.add(Restrictions.eq("description", "2-Lloyd's Corp")).add(Restrictions.eq("shortDesc", "EN-LC"));
        EncodingName encodingLloydsCorp = (EncodingName) criteria.uniqueResult();

        criteria = HibernateSessionFactoryUtil.getSession().createCriteria(EncodingName.class);
        criteria.add(Restrictions.eq("description", "4-Mithras-XML")).add(Restrictions.eq("shortDesc", "EN-MI"));
        EncodingName encodingMithras = (EncodingName) criteria.uniqueResult();

        criteria = HibernateSessionFactoryUtil.getSession().createCriteria(EncodingName.class);
        criteria.add(Restrictions.eq("description", "6-Reale Mutua")).add(Restrictions.eq("shortDesc", "EN-RM"));
        EncodingName encodingRma = (EncodingName) criteria.uniqueResult();

        criteria = HibernateSessionFactoryUtil.getSession().createCriteria(EntityAlias.class);
        criteria.add(Restrictions.eq("aliasCode", codiceAgenziaLloyd));
        EntityAlias entity_x = (EntityAlias) criteria.uniqueResult();

        if (null == entity_x) {
            log.debug("Non trovato entityAlias per aliascode " + codiceAgenziaLloyd + ", procedo nella creazione.");
            //			criteria = HibernateSessionFactoryUtil.getSession().createCriteria(
            //					EntityAlias.class);
            EntityAlias entity_1 = new EntityAlias(timestampCreazione, uoaCompanyName, aliasIntermediario,
                    encodingLloydsCorp, codiceAgenziaLloyd);
            EntityAlias entity_2 = new EntityAlias(timestampCreazione, uoaCompanyName, aliasIntermediario,
                    encodingMithras, codiceAgenziaRma);
            EntityAlias entity_3 = new EntityAlias(timestampCreazione, uoaCompanyName, aliasIntermediario, encodingRma,
                    codiceAgenziaMithras);

            HibernateSessionFactoryUtil.getSession().save(entity_1);
            HibernateSessionFactoryUtil.getSession().save(entity_2);
            HibernateSessionFactoryUtil.getSession().save(entity_3);
            //verifica per debug
            String query = "select * from ENTITY_ALIAS  where ID_UOA = :idUOA";
            List<EntityAlias> entityAlias = HibernateSessionFactoryUtil.getSession().createSQLQuery(query)
                    .addEntity(EntityAlias.class).setParameter("idUOA", entity_1.getUntaOperAun().getRecordId()).list();
            log.debug("trovati " + entityAlias.size() + " entity alias con uoa id: "
                    + entity_1.getUntaOperAun().getRecordId());
            //	                .setParameter("idAN", aliasName.getRecordId()).setParameter("idEN", encodingName.getRecordId())
            //	                .uniqueResult();
        }
    }

    private PunVnd salvaPuntoVendita(CompPtf compPtf, CnlVnd cnlVnd, UntaOperAun uoa, String codicePuntoVendita) {
        RecordIdentifier timestamp = RecordIdentifier.getInitRecord();
        HibernateSessionFactoryUtil.getSession().saveOrUpdate(cnlVnd);
        PunVnd punVnd = new PunVnd(timestamp, cnlVnd, codicePuntoVendita, compPtf, uoa);
        punVnd.setUntaOperAun(uoa);
        uoa.getPunVnds().add(punVnd);
        HibernateSessionFactoryUtil.getSession().save(punVnd);
        return punVnd;
    }

    //cripta la password
    // public static void main(String a[]){
    // System.out.println(BCrypt.hashpw("", BCrypt.gensalt()));
    // }
}
