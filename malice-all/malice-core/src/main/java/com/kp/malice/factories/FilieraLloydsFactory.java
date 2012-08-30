package com.kp.malice.factories;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import com.kp.malice.MalicePropertyContainer;
import com.kp.malice.entities.business.AgenziaRMA;
import com.kp.malice.entities.business.BindingAuthority;
import com.kp.malice.entities.business.FilieraLloyds;
import com.kp.malice.entities.business.LioReferenceCode;
import com.kp.malice.entities.business.LloydsBroker;
import com.kp.malice.entities.business.LloydsCoverHolder;
import com.kp.malice.entities.business.LloydsItalianOffice;
import com.kp.malice.entities.business.PuntoVenditaRMAPerLloyds;
import com.kp.malice.entities.persisted.AliasName;
import com.kp.malice.entities.persisted.CnlVnd;
import com.kp.malice.entities.persisted.CompPtf;
import com.kp.malice.entities.persisted.EncodingName;
import com.kp.malice.entities.persisted.EntityAlias;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.Pol;
import com.kp.malice.entities.persisted.PunVnd;
import com.kp.malice.entities.persisted.UntaOperAun;
import com.kp.malice.entities.persisted.VrnPol;
import com.kp.malice.entities.xml.BindingAuthorityDetails;
import com.kp.malice.entities.xml.Document;
import com.kp.malice.entities.xml.ImportDataSet;

public class FilieraLloydsFactory extends EntityAliasFactory {

    private final Logger log = Logger.getLogger(FilieraLloydsFactory.class);

    public LioReferenceCode getLioReferenceCode(String lioCodeString) {
        AliasName an = findAliasNameForLloydsBroker();
        EncodingName en = findEncodingNameForLio();
        EntityAlias alias = findEntityAliasFromAliasCode(en, an, lioCodeString);
        return findLioReferenceCodeFromUoa(alias.getUntaOperAun());
    }
    
    public List<LioReferenceCode> getListLioReferenceCode(){
    	AliasName an = findAliasNameForLloydsBroker();
        EncodingName en = findEncodingNameForLio();
        List<EntityAlias> aliasList = findEntityAliasList(en, an);
        log.trace("aliasList size: "+aliasList.size());
        List<LioReferenceCode> lioReferenceCodeList = new ArrayList<LioReferenceCode>();
        lioReferenceCodeList.add(new LioReferenceCode("*", "TUTTI"));
        for (EntityAlias entityAlias : aliasList) {
        	lioReferenceCodeList.add(findLioReferenceCodeFromUoa(entityAlias.getUntaOperAun()));
		}
        log.trace("lioReferenceCodeList size: "+lioReferenceCodeList.size());
    	return lioReferenceCodeList;
    }

    public LloydsItalianOffice getLio() {
        UntaOperAun lioUoa = HibernateSessionFactoryUtil.getPersistedInstance(UntaOperAun.class,
                MalicePropertyContainer.getLioRecordId());
        LloydsItalianOffice lio = new LloydsItalianOffice();
        lio.setEmail(lioUoa.getIndMail());
        return lio;
    }

    public PuntoVenditaRMAPerLloyds getPuntoVendita(PunVnd punVnd) throws Exception {
        PuntoVenditaRMAPerLloyds pv = new PuntoVenditaRMAPerLloyds();
        pv.setId(punVnd.getRecordId());
        pv.setCodiceCompagnia(punVnd.getCodCompPtf());
        pv.setCodicePuntoVendita(punVnd.getCodPunVnd());
        pv.setBroker(findLloydsBrokerFromCnlVnd(punVnd.getCnlVnd()));
        return pv;
    }

    public PuntoVenditaRMAPerLloyds makePuntoVenditaRma(ImportDataSet xmlInstance) throws Exception {
        try {
            AgenziaRMA age = retrieveAgenziaRMA(xmlInstance);
            PuntoVenditaRMAPerLloyds pv = findPuntoVenditaLloyds(age, findLloydsBroker(xmlInstance));
            pv.setAgenzia(age);
            return pv;
        } catch (Exception e) {
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    private LloydsCoverHolder getCoverHolder(ImportDataSet xmlInstance) throws Exception {
        try {
            LloydsCoverHolder ch = findLloydsCoverHolderFromLloydsPin(xmlInstance.getDocument()
                    .getBindingAuthorityDetails().getBAcorrPin1()
                    + "-" + xmlInstance.getDocument().getBindingAuthorityDetails().getBAcorrPin2());
            return ch;
        } catch (Exception e) {
            log.error("ERRORE IN RECUPERO COVER HOLDER DA DB", e);
            throw e;
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    private LloydsBroker findLloydsBrokerFromCnlVnd(CnlVnd cnlVnd) {
        EncodingName encodingName = findEncodingNameForMalice();
        AliasName aliasName = findAliasNameForCanaleBroker();
        EntityAlias aliasBroker = findEntityAliasFromAliasCode(encodingName, aliasName, "" + cnlVnd.getRecordId());
        LloydsBroker broker = generaLloydsBroker(aliasBroker);
        broker.setCodiceCanaleVendita(new Integer(cnlVnd.getCodCnlVnd()));
        broker.setDescription(cnlVnd.getDenCnlVnd());
        return broker;
    }

    private LloydsBroker generaLloydsBroker(EntityAlias aliasBroker) {
        LloydsBroker broker = new LloydsBroker();
        broker.setIdUoa(aliasBroker.getUntaOperAun().getRecordId());
        broker.setDescription(aliasBroker.getUntaOperAun().getShortDescription());
        EntityAlias aliasBrokerCanale = findAliasBrokerCanaleFromUoa(aliasBroker.getUntaOperAun());
        broker.setIdCanaleVendita(Long.parseLong(aliasBrokerCanale.getAliasCode()));
        broker.setLioReferenceCode(findLioReferenceCodeFromUoa(aliasBroker.getUntaOperAun()));
        return broker;
    }

    public AgenziaRMA getAgenziaRma(UntaOperAun uoa) throws Exception {
        AgenziaRMA age = new AgenziaRMA();
        age.setShortDescription(uoa.getShortDescription());
        age.setDescription(uoa.getDescription());
        age.setId(uoa.getRecordId());
        age.setEmail(uoa.getIndMail());
        EntityAlias ea = findAliasIntermediarioFromUoa(uoa);
        if (ea == null) {
            throw new Exception("impossibile trovare entityAlias per uoa: " + uoa.getRecordId());
        }
        age.setOmcCode(ea.getAliasCode());
        for (PunVnd punVnd : uoa.getPunVnds()) {
            PuntoVenditaRMAPerLloyds pv = getPuntoVendita(punVnd);
            age.getPuntiVendita().add(pv);
            pv.setAgenzia(age);
        }
        return age;
    }

    private BindingAuthority getBindingAuthority(ImportDataSet xmlInstance) {
        BindingAuthority bindingAuthority = new BindingAuthority();
        BindingAuthorityDetails baDetails = xmlInstance.getDocument().getBindingAuthorityDetails();
        final XMLGregorianCalendar referenceDate = baDetails.getBARefDate();
        DateTime refDate = new DateTime(referenceDate.getYear(), referenceDate.getMonth(), referenceDate.getDay(), 0, 0);
        bindingAuthority.setRegistrationDate(refDate.toDate());
        bindingAuthority.setRegistrationNumber("" + baDetails.getBARefNo());
        bindingAuthority.setBrokerRef(baDetails.getBABrokersRef());
        return bindingAuthority;
    }

    private BindingAuthority getBindingAuthority(VrnPol vrnPol) {
        BindingAuthority bindAuth = new BindingAuthority();
        bindAuth.setBrokerRef(vrnPol.getBrokerRef());
        bindAuth.setRegistrationDate(vrnPol.getRegistrationDate());
        bindAuth.setRegistrationNumber(vrnPol.getRegistrationNumber());
        bindAuth.setDescription(vrnPol.getBrokerRef());
        return bindAuth;
    }

    private LloydsBroker findLloydsBroker(ImportDataSet xmlInstance) throws Exception {
        try {
            LloydsBroker brk = findLloydsBrokerFromLloydsPin(xmlInstance.getDocument().getBindingAuthorityDetails()
                    .getBAPseudonym().trim()
                    + "-" + xmlInstance.getDocument().getBindingAuthorityDetails().getBANumber().trim());
            return brk;
        } catch (Exception e) {
            log.error("ERRORE IN RECUPERO BROKER DA DB", e);
            throw e;
        }
    }

    private LloydsBroker findLloydsBrokerFromLloydsPin(String brokerId) {
        EntityAlias aliasBroker = findAliasBrokerFromLloydsPin(brokerId);
        return generaLloydsBroker(aliasBroker);
    }

    private AgenziaRMA retrieveAgenziaRMA(ImportDataSet xmlInstance) throws Exception {
        return findAgenziaRMAFromLloydsPin(xmlInstance.getDocument().getBindingAuthorityDetails().getIntermediaryPin(),
                xmlInstance.getDocument().getBindingAuthorityDetails().getIntermediaryPinType());
    }

    private AgenziaRMA findAgenziaRMAFromLloydsPin(String intermediaryPin, String intermediaryPinType) throws Exception {
        if (!"EN-LC".equals(intermediaryPinType))
            throw new RuntimeException("IL TIPO DI CODIFICA DELL'INTERMEDIARIO NON E' GESTITA:<" + intermediaryPinType
                    + ">");
        String aliasCode = intermediaryPin;
        EntityAlias entityAlias = findAliasIntermediarioFromLloydsPin(aliasCode);
        AgenziaRMA agenziaRMA = getAgenziaRma(entityAlias.getUntaOperAun());
        if (agenziaRMA == null)
            throw new RuntimeException("IMPOSSIBILE TROVARE AGENZIA RMA PER UOA CON ID: "
                    + entityAlias.getUntaOperAun().getRecordId());
        return agenziaRMA;
    }

    private PuntoVenditaRMAPerLloyds findPuntoVenditaLloyds(AgenziaRMA agenzia, LloydsBroker broker) throws Exception {
        UntaOperAun uoa = HibernateSessionFactoryUtil.getPersistedInstance(UntaOperAun.class, agenzia.getId());
        CompPtf lloyds = HibernateSessionFactoryUtil.getPersistedInstance(CompPtf.class,
                MalicePropertyContainer.getLloydsRecordId());
        CnlVnd cnlVnd = HibernateSessionFactoryUtil.getPersistedInstance(CnlVnd.class, broker.getIdCanaleVendita());
        Criteria c = HibernateSessionFactoryUtil.getSession().createCriteria(PunVnd.class);
        c.add(Restrictions.eq("codCompPtf", lloyds.getCodCompPtf()));
        c.add(Restrictions.eq("codCnlVnd", cnlVnd.getCodCnlVnd()));
        c.add(Restrictions.eq("untaOperAun", uoa));
        PunVnd puntoVenditaAgenziale = (PunVnd) c.uniqueResult();
        PuntoVenditaRMAPerLloyds pv = getPuntoVendita(puntoVenditaAgenziale);
        return pv;
    }

    private LioReferenceCode findLioReferenceCodeFromLloydsCoverHolderCode(String codiceLloyds) {
        EntityAlias aliasCoverHolder = findAliasCoverHolderFromLloydsPin(codiceLloyds);
        UntaOperAun uoaCoverHoder = aliasCoverHolder.getUntaOperAun();
        return findLioReferenceCodeFromUoa(uoaCoverHoder);
    }

    private LioReferenceCode findLioReferenceCodeFromUoa(UntaOperAun uoaCoverHoder) {
        EntityAlias aliasBroker = findAliasBrokerLioFromUoa(uoaCoverHoder);
        return new LioReferenceCode(aliasBroker.getAliasCode(), uoaCoverHoder.getShortDescription());
    }
    
    private LloydsCoverHolder findLloydsCoverHolderFromLloydsPin(String covHoldId) {
        LloydsCoverHolder ch = null;
        if (StringUtils.isNotEmpty(covHoldId)) {
            ch = new LloydsCoverHolder();
            EntityAlias aliasCoverHolder = findAliasCoverHolderFromLloydsPin(covHoldId);
            ch.setId(aliasCoverHolder.getRecordId());
            ch.setIdUoa(aliasCoverHolder.getUntaOperAun().getRecordId());
            ch.setDescription(aliasCoverHolder.getUntaOperAun().getShortDescription());
            ch.setCodiceLloyds(covHoldId);
            ch.setLioReferenceCode(findLioReferenceCodeFromLloydsCoverHolderCode(covHoldId));
        }
        return ch;
    }

    private AgenziaRMA getAgenzia(Document doc) throws Exception {
        return findAgenziaRMAFromLloydsPin(doc.getBindingAuthorityDetails().getIntermediaryPin(), doc
                .getBindingAuthorityDetails().getIntermediaryPinType());
    }

    private LloydsBroker getBroker(Document doc) throws Exception {
        return findLloydsBrokerFromLloydsPin(doc.getBindingAuthorityDetails().getBAPseudonym() + "-"
                + doc.getBindingAuthorityDetails().getBANumber());
    }

    public FilieraLloyds getFiliera(ImportDataSet xmlDataSet) throws Exception {
        FilieraLloyds filiera = new FilieraLloyds();
        filiera.setAgenziaRma(getAgenzia(xmlDataSet.getDocument()));
        filiera.setBroker(getBroker(xmlDataSet.getDocument()));
        filiera.setBindingAuthority(getBindingAuthority(xmlDataSet));
        filiera.setCoverHolder(getCoverHolder(xmlDataSet));
        return filiera;
    }

    public FilieraLloyds getFiliera(Pol pol) throws Exception {
        FilieraLloyds filiera = new FilieraLloyds();
        filiera.setAgenziaRma(getAgenziaRma(pol.getPunVnd().getUntaOperAun()));
        filiera.setBroker(findLloydsBrokerFromCnlVnd(pol.getPunVnd().getCnlVnd()));
        filiera.setBindingAuthority(getBindingAuthority(pol.getVrnPols().iterator().next()));
        filiera.setCoverHolder(findLloydsCoverHolderFromLloydsPin(pol.getCodiceCoverHolder()));
        return filiera;
    }

}
