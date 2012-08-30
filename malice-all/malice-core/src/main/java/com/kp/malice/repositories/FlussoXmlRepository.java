package com.kp.malice.repositories;

import java.util.List;

import com.kp.malice.entities.persisted.RegisteredDocument;
import com.kp.malice.entities.xml.ImportDataSet;

public interface FlussoXmlRepository {

    public Long add(ImportDataSet xmlDataSet, String xmlString, String encoding, boolean valid,
            String validationErrorMessage) throws Exception;

    public RegisteredDocument find(Long id) throws Exception;

    public void delete(Long id) throws Exception;

    public List<RegisteredDocument> findAllNotWorked() throws Exception;

}