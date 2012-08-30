package com.kp.malice.factories;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;

import com.kp.malice.entities.business.ContraentePolizzaLloyds;
import com.kp.malice.entities.business.ContraentePolizzaLloyds.Gender;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.Pol;
import com.kp.malice.entities.persisted.Psn;
import com.kp.malice.entities.persisted.RapCntn;
import com.kp.malice.entities.persisted.VrnPol;
import com.kp.malice.entities.xml.PolicyholderDetails;

public class ContraenteLloydsFactory {

    public ContraentePolizzaLloyds transcode(PolicyholderDetails policyholderDetails) {
        ContraentePolizzaLloyds contraente = new ContraentePolizzaLloyds();
        contraente.setAddressLine1(policyholderDetails.getAddressLine1());
        contraente.setAddressLine2(policyholderDetails.getAddressLine2());
        contraente.setCity(policyholderDetails.getCity());
        contraente.setCompanyName(policyholderDetails.getCompanyName());
        contraente.setFirstName(policyholderDetails.getFirstName());
        contraente.setLastName(policyholderDetails.getLastName());
        contraente.setJobTitle(policyholderDetails.getJobTitle());
        contraente.setForeigner(StringUtils.isNotEmpty(policyholderDetails.getNonItalianPolicyholderCode()));
        contraente.setRegion(policyholderDetails.getRegion());
        contraente.setVatNumber(policyholderDetails.getVATNumber());
        contraente.setCountry(policyholderDetails.getCountry());
        contraente.setFiscalCode(contraente.isForeigner() ? policyholderDetails.getNonItalianPolicyholderCode()
                : policyholderDetails.getFiscalCode());
        contraente.setGender(Gender.fromString(policyholderDetails.getGenderType().value()));
        contraente.setPostCode(policyholderDetails.getPostCode());
        return contraente;
    }

    public ContraentePolizzaLloyds hidrateContraente(VrnPol vrnPol) {
    	RapCntn rapCntn = vrnPol.getRapportiContraenza().iterator().next();
        Psn contraente = rapCntn.getPersona();
        ContraentePolizzaLloyds contr = new ContraentePolizzaLloyds();
        contr.setAddressLine1(contraente.getAddressLine1());
        contr.setAddressLine2(contraente.getAddressLine2());
        contr.setGender(contraente.getGender());
        contr.setFirstName(contraente.getFirstName());
        contr.setLastName(contraente.getLastName());
        contr.setCity(contraente.getCity());
        contr.setPostCode(contraente.getPostCode());
        contr.setFiscalCode(contraente.getFiscalCode());
        contr.setVatNumber(contraente.getVatNumber());
        contr.setCompanyName(contraente.getCompanyName());
        contr.setCountry(contraente.getCountry());
        contr.setForeigner(contraente.isForeigner());
        contr.setId(contraente.getRecordId());
        contr.setJobTitle(contraente.getJobTitle());
        contr.setRegion(contraente.getRegion());
        return contr;
    }
}
