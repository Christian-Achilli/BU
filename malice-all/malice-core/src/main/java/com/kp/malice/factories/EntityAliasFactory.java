package com.kp.malice.factories;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.kp.malice.entities.business.WelcomeInfo;
import com.kp.malice.entities.persisted.AliasName;
import com.kp.malice.entities.persisted.EncodingName;
import com.kp.malice.entities.persisted.EntityAlias;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.UntaOperAun;

public abstract class EntityAliasFactory {

    private static final Logger log = Logger.getLogger(EntityAliasFactory.class);

    protected EncodingName findEncodingNameForLloydsCorp() {
        return findEncodingNameFromShortDescription("EN-LC");
    }

    protected EncodingName findEncodingNameForMalice() {
        return findEncodingNameFromShortDescription("EN-MA");
    }

    protected EncodingName findEncodingNameForLio() {
        return findEncodingNameFromShortDescription("EN-LIO");
    }

    protected EncodingName findEncodingNameFromShortDescription(String intermediaryPinType) {
        EncodingName encodingName = (EncodingName) HibernateSessionFactoryUtil.getSession()
                .createCriteria(EncodingName.class).add(Restrictions.eq("shortDesc", intermediaryPinType))
                .uniqueResult();
        return encodingName;
    }

    protected AliasName findAliasNameForLloydsCoverHolder() {
        return findAliasNameFromShortDescription("AN-CH");
    }

    protected AliasName findAliasNameForLloydsBroker() {
        return findAliasNameFromShortDescription("AN-LB");
    }

    protected AliasName findAliasNameForIntermediario() {
        return findAliasNameFromShortDescription("AN-IM");
    }

    protected AliasName findAliasNameForCanaleBroker() {
        return findAliasNameFromShortDescription("AN-CN");
    }

    protected AliasName findAliasNameFromShortDescription(String intermediaryAliasIdentifier) {
        AliasName aliasName = (AliasName) HibernateSessionFactoryUtil.getSession().createCriteria(AliasName.class)
                .add(Restrictions.eq("shortDesc", intermediaryAliasIdentifier)).uniqueResult();
        return aliasName;
    }

    protected EntityAlias findAliasBrokerLioFromUoa(UntaOperAun uoaCoverHoder) {
        AliasName aliasName = findAliasNameForLloydsBroker();
        EncodingName lioEncoding = findEncodingNameForLio();
        EntityAlias aliasLio = (EntityAlias) HibernateSessionFactoryUtil.getSession().createCriteria(EntityAlias.class)
                .add(Restrictions.eq("untaOperAun", uoaCoverHoder)).add(Restrictions.eq("encodingName", lioEncoding))
                .add(Restrictions.eq("aliasName", aliasName)).uniqueResult();
        return aliasLio;
    }

    protected EntityAlias findAliasBrokerCanaleFromUoa(UntaOperAun untaOperAun) {
        AliasName aliasName = findAliasNameForCanaleBroker();
        EncodingName maliceEncoding = findEncodingNameForMalice();
        EntityAlias aliasBrokerCanale = (EntityAlias) HibernateSessionFactoryUtil.getSession()
                .createCriteria(EntityAlias.class).add(Restrictions.eq("untaOperAun", untaOperAun))
                .add(Restrictions.eq("encodingName", maliceEncoding)).add(Restrictions.eq("aliasName", aliasName))
                .uniqueResult();
        return aliasBrokerCanale;
    }

    protected EntityAlias findAliasBrokerFromLloydsPin(String brokerId) {
        EncodingName encodingName = findEncodingNameForLloydsCorp();
        AliasName aliasName = findAliasNameForLloydsBroker();
        return findEntityAliasFromAliasCode(encodingName, aliasName, brokerId);
    }

    protected EntityAlias findAliasIntermediarioFromLloydsPin(String aliasCode) {
        EncodingName encodingName = findEncodingNameForLloydsCorp();
        AliasName aliasName = findAliasNameForIntermediario();
        return findEntityAliasFromAliasCode(encodingName, aliasName, aliasCode);
    }

    protected EntityAlias findAliasIntermediarioFromUoa(UntaOperAun uoa) {
        EncodingName encodingName = findEncodingNameForLloydsCorp();
        AliasName aliasName = findAliasNameForIntermediario();
        return findEntityAliasFromUoa(encodingName, aliasName, uoa);
    }

    protected EntityAlias findAliasCoverHolderFromLloydsPin(String coverHolderCode) {
        EncodingName encodingName = findEncodingNameForLloydsCorp();
        AliasName aliasName = findAliasNameForLloydsCoverHolder();
        return findEntityAliasFromAliasCode(encodingName, aliasName, coverHolderCode);
    }

    protected EntityAlias findEntityAliasFromAliasCode(EncodingName encodingName, AliasName aliasName, String aliasCode) {
        return (EntityAlias) HibernateSessionFactoryUtil.getSession().createCriteria(EntityAlias.class)
                .add(Restrictions.eq("aliasCode", aliasCode)).add(Restrictions.eq("encodingName", encodingName))
                .add(Restrictions.eq("aliasName", aliasName)).uniqueResult();
    }
    
    protected List<EntityAlias> findEntityAliasList(EncodingName encodingName, AliasName aliasName) {
    	return HibernateSessionFactoryUtil.getSession().createCriteria(EntityAlias.class)
    			.add(Restrictions.eq("encodingName", encodingName))
    			.add(Restrictions.eq("aliasName", aliasName)).list();
    }

    protected EntityAlias findEntityAliasFromUoa(EncodingName encodingName, AliasName aliasName, UntaOperAun uoa) {
        String query = "select * from ENTITY_ALIAS  where ID_UOA = :idUOA and ALIAS_NAME = :idAN and ENCODING_CODE = :idEN";
        return (EntityAlias) HibernateSessionFactoryUtil.getSession().createSQLQuery(query)
                .addEntity(EntityAlias.class).setParameter("idUOA", uoa.getRecordId())
                .setParameter("idAN", aliasName.getRecordId()).setParameter("idEN", encodingName.getRecordId())
                .uniqueResult();
    }
}
