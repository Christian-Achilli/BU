package database;

import java.io.IOException;

import jxl.read.biff.BiffException;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kp.malice.entities.persisted.AliasName;
import com.kp.malice.entities.persisted.CnlVnd;
import com.kp.malice.entities.persisted.CompPtf;
import com.kp.malice.entities.persisted.EncodingName;
import com.kp.malice.entities.persisted.EntityAlias;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.RecordIdentifier;
import com.kp.malice.entities.persisted.UntaOperAun;
import com.kp.malice.entities.persisted.authentication.RuoloApplicativo;
import com.kp.malice.entities.persisted.authentication.RuoloApplicativo.TipoRuolo;
import com.kp.malice.factories.ChiusuraMensileLioFactory;
import com.kp.malice.factories.FilieraLloydsFactory;
import com.kp.malice.repositories.ChiusureMensiliLioRepository;
import com.kp.malice.useCases.FunzioniChiusura;
import com.kp.malice.useCases.ImportaAnagraficaAgenzieRMADaExcel;

public class Test_Configura_Malice_e_Agenzie {

    private final String pathToExcelConAgenzie = "src/test/resources/DA_caricare_toltePrime20.xls";

    ImportaAnagraficaAgenzieRMADaExcel importer;

    @Before
    public void avvio() {
        Injector inj = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                bind(ImportaAnagraficaAgenzieRMADaExcel.class);
                bind(FilieraLloydsFactory.class);
                bind(FunzioniChiusura.class);
                bind(ChiusuraMensileLioFactory.class);
                bind(ChiusureMensiliLioRepository.class);
            }
        });
        importer = inj.getInstance(ImportaAnagraficaAgenzieRMADaExcel.class);
    }

    @Test
    public void configura() throws Exception {
        try {
            HibernateSessionFactoryUtil.beginTransaction();
//            configuraMalice();
            configuraAgenzie();
            HibernateSessionFactoryUtil.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            HibernateSessionFactoryUtil.rollbackTransaction();
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    private void configuraAgenzie() throws IOException, BiffException, Exception {
        importer.importaExcelSulDb(pathToExcelConAgenzie);
        importer.creaUOA_PunVnd_EntityAlias();
    }

    private void configuraMalice() {
        RecordIdentifier timestampCreazione = RecordIdentifier.getInitRecord();
        AliasName aliasIntermediario = new AliasName(timestampCreazione, "1-Intermediary", "AN-IM");
        AliasName aliasLloydsBroker = new AliasName(timestampCreazione, "2-Lloyd's Broker", "AN-LB");
        AliasName aliasCoverHoilder = new AliasName(timestampCreazione, "3-Cover Holder", "AN-CH");
        AliasName aliasChannel = new AliasName(timestampCreazione, "4-Channel", "AN-CN");
        HibernateSessionFactoryUtil.getSession().save(aliasIntermediario);
        HibernateSessionFactoryUtil.getSession().save(aliasLloydsBroker);
        HibernateSessionFactoryUtil.getSession().save(aliasCoverHoilder);
        HibernateSessionFactoryUtil.getSession().save(aliasChannel);
        EncodingName encodingMalice = new EncodingName(timestampCreazione, "1-Malice", "EN-MA");
        EncodingName encodingLloydsCorp = new EncodingName(timestampCreazione, "2-Lloyd's Corp", "EN-LC");
        EncodingName encodingLio = new EncodingName(timestampCreazione, "3-LIO", "EN-LIO");
        EncodingName encodingMithras = new EncodingName(timestampCreazione, "4-Mithras-XML", "EN-MI");
        EncodingName encodingFurness = new EncodingName(timestampCreazione, "5-Furness-XML", "EN-FU");
        EncodingName encodingRma = new EncodingName(timestampCreazione, "6-Reale Mutua", "EN-RM");
        HibernateSessionFactoryUtil.getSession().save(encodingMalice);
        HibernateSessionFactoryUtil.getSession().save(encodingLloydsCorp);
        HibernateSessionFactoryUtil.getSession().save(encodingLio);
        HibernateSessionFactoryUtil.getSession().save(encodingMithras);
        HibernateSessionFactoryUtil.getSession().save(encodingFurness);
        HibernateSessionFactoryUtil.getSession().save(encodingRma);
        UntaOperAun uoaLio = new UntaOperAun(timestampCreazione, 1, "LIO", "LIO");
        uoaLio.setIndMail("acn_italy.sm@kubepartners.com");
        UntaOperAun uoaBdb = new UntaOperAun(timestampCreazione, 2, "BDB Ltd", "BDB");
        uoaBdb.setIndMail("ammin-lloyds@kubepartners.it");
        UntaOperAun uoaIpr = new UntaOperAun(timestampCreazione, 3, "IPR", "IPR");
        UntaOperAun uoaMithras = new UntaOperAun(timestampCreazione, 4, "Mithras Underwriting Limited", "Mithras");
        UntaOperAun uoaFurness = new UntaOperAun(timestampCreazione, 5, "Furness Underwriting Limited", "Furness");

        HibernateSessionFactoryUtil.getSession().save(uoaLio);
        HibernateSessionFactoryUtil.getSession().save(uoaBdb);
        HibernateSessionFactoryUtil.getSession().save(uoaIpr);
        HibernateSessionFactoryUtil.getSession().save(uoaMithras);
        HibernateSessionFactoryUtil.getSession().save(uoaFurness);
        CompPtf lloyds = salvaLloyds();
        CnlVnd canaleVenditaBDB = new CnlVnd(timestampCreazione, 1, "BDB", lloyds);
        CnlVnd canaleVenditaIPR = new CnlVnd(timestampCreazione, 2, "IPR", lloyds);
        HibernateSessionFactoryUtil.getSession().save(canaleVenditaBDB);
        HibernateSessionFactoryUtil.getSession().save(canaleVenditaIPR);
        //BDB
        EntityAlias entity1 = new EntityAlias(timestampCreazione, uoaBdb, aliasLloydsBroker, encodingLloydsCorp,
                "BDB-312");
        EntityAlias entity2 = new EntityAlias(timestampCreazione, uoaBdb, aliasCoverHoilder, encodingLloydsCorp,
                "104512-PXB");
        EntityAlias entity3 = new EntityAlias(timestampCreazione, uoaBdb, aliasCoverHoilder, encodingLloydsCorp, null);
        EntityAlias entity4 = new EntityAlias(timestampCreazione, uoaBdb, aliasLloydsBroker, encodingLio, "312");
        EntityAlias entity5 = new EntityAlias(timestampCreazione, uoaBdb, aliasChannel, encodingMalice, ""
                + canaleVenditaBDB.getRecordId());
        //IPR
        EntityAlias entity6 = new EntityAlias(timestampCreazione, uoaIpr, aliasLloydsBroker, encodingLloydsCorp,
                "IPR-1124");
        EntityAlias entity7 = new EntityAlias(timestampCreazione, uoaIpr, aliasCoverHoilder, encodingLloydsCorp, null);
        EntityAlias entity8 = new EntityAlias(timestampCreazione, uoaIpr, aliasLloydsBroker, encodingLio, "1124");
        EntityAlias entity9 = new EntityAlias(timestampCreazione, uoaIpr, aliasChannel, encodingMalice, ""
                + canaleVenditaIPR.getRecordId());
        //Mithras Underwriting Limited
        EntityAlias entity10 = new EntityAlias(timestampCreazione, uoaMithras, aliasCoverHoilder, encodingLloydsCorp,
                "110081-JAS");
        EntityAlias entity11 = new EntityAlias(timestampCreazione, uoaMithras, aliasLloydsBroker, encodingLio, "9005");
        //Furness Underwriting Limited
        EntityAlias entity12 = new EntityAlias(timestampCreazione, uoaFurness, aliasCoverHoilder, encodingLloydsCorp,
                "108935-WWA");
        EntityAlias entity13 = new EntityAlias(timestampCreazione, uoaFurness, aliasLloydsBroker, encodingLio, "9000");
        HibernateSessionFactoryUtil.getSession().save(entity1);
        HibernateSessionFactoryUtil.getSession().save(entity2);
        HibernateSessionFactoryUtil.getSession().save(entity3);
        HibernateSessionFactoryUtil.getSession().save(entity4);
        HibernateSessionFactoryUtil.getSession().save(entity5);
        HibernateSessionFactoryUtil.getSession().save(entity6);
        HibernateSessionFactoryUtil.getSession().save(entity7);
        HibernateSessionFactoryUtil.getSession().save(entity8);
        HibernateSessionFactoryUtil.getSession().save(entity9);
        HibernateSessionFactoryUtil.getSession().save(entity10);
        HibernateSessionFactoryUtil.getSession().save(entity11);
        HibernateSessionFactoryUtil.getSession().save(entity12);
        HibernateSessionFactoryUtil.getSession().save(entity13);
        RuoloApplicativo ruolo = new RuoloApplicativo(timestampCreazione);
        ruolo.setTipoRuolo(TipoRuolo.ROLE_AGENTE);
        HibernateSessionFactoryUtil.getSession().save(ruolo);
    }

    private CompPtf salvaLloyds() {
        RecordIdentifier rid = RecordIdentifier.getInitRecord();
        CompPtf compPtf = new CompPtf(rid, 1, "Lloyd's");
        compPtf.setDenCompPtf("Lloyd's");
        HibernateSessionFactoryUtil.getSession().save(compPtf);
        return compPtf;
    }

}
