package com.kp.malice.useCases;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.Logger;
import org.hibernate.util.ConfigHelper;
import org.xml.sax.SAXException;

import com.kp.malice.MalicePropertyContainer;
import com.kp.malice.entities.xml.ImportDataSet;
import com.kp.malice.entities.xml.ObjectFactory;

public final class MaliceSchemaValidator {

    private static Logger log = Logger.getLogger(MaliceSchemaValidator.class);
    private static MaliceSchemaValidator INSTANCE;
    private Unmarshaller unmarshaller;
    private Schema schema;

    private MaliceSchemaValidator() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
            unmarshaller = jaxbContext.createUnmarshaller();
            InputStream is = ConfigHelper.getResourceAsStream(MalicePropertyContainer.getPathToXsdFile());
            StreamSource ss = new StreamSource(is);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schema = schemaFactory.newSchema(ss);
        } catch (JAXBException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("EXCEPTION CREATING THE JAXB CONTEXT");
        } catch (SAXException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("EXCEPTION CREATING THE XSD SCHEMA FOR VALIDATION");
        }

    }

    private static MaliceSchemaValidator makeInstance() {
        if (null == INSTANCE)
            INSTANCE = new MaliceSchemaValidator();
        return INSTANCE;
    }

    public static ImportDataSet getJavaObjects(InputStream xmlStream) throws Exception {
        return (ImportDataSet) makeInstance().unmarshaller.unmarshal(xmlStream);
    }

    public static String getEncoding(InputStream xmlStream) throws Exception {
        String encoding = "UTF-16";
        BOMInputStream bomIn = new BOMInputStream(xmlStream, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16BE);
        if (bomIn.hasBOM() == false) {
            encoding = "UTF-8";
        }
        //        else if (bomIn.hasBOM(ByteOrderMark.UTF_16LE)) {
        //            return "UTF-8";
        //        } else if (bomIn.hasBOM(ByteOrderMark.UTF_16BE)) {
        //            return "UTF-8";
        //        }
        return encoding;
    }

    public static void checkIsValidAgainstSchema(InputStream xmlString) throws Exception {
        makeInstance().schema.newValidator().validate(new StreamSource(xmlString));
    }
}
