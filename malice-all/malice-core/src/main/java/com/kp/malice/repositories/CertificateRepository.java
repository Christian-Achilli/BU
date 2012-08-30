package com.kp.malice.repositories;

import com.kp.malice.entities.business.CertificatoLloyds;

public interface CertificateRepository {

    public long addNewCertificateMonoRischioELavorabileDaXml(CertificatoLloyds polizza) throws Exception;

    public void removeCertificate(CertificatoLloyds polizza) throws Exception;

}