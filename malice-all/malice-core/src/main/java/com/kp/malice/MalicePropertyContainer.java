package com.kp.malice;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public final class MalicePropertyContainer {

    public static String getDroolsConfigurationFileName() {
        return getInstance().droolsConfigurationFileName;
    }

    public static long getLloydsRecordId() {
        return getInstance().lloydsRecordId;
    }

    public static String getHibernateConfigurationFileName() {
        return getInstance().hibernateConfigurationFileName;
    }

    public static String[] getPackagesWithEntities() {
        return getInstance().hibernateMappingPackages;
    }

    public static String getPathToXsdFile() {
        return getInstance().interceptorXSDFileNamePath;
    }

    public static String getPathToXlsFiles() {
        return getInstance().interceptorXLSFileNamePath;
    }

    public static long getLioRecordId() {
        return getInstance().lioRecordId;
    }

    public static String getUsername() {
        return getInstance().username;
    }

    public static String getPassword() {
        return getInstance().password;
    }

    public static String getFrom() {
        return getInstance().from;
    }

    public static String getTo() {
        return getInstance().to;
    }

    public static String getMailStoreHost() {
        return getInstance().mailStoreHost;
    }

    public static String getMailStoreProtocol() {
        return getInstance().mailStoreProtocol;
    }

    public static String getMailSmtpHost() {
        return getInstance().mailSmtpHost;
    }

    public static String getMailSmtpPort() {
        return getInstance().mailSmtpPort;
    }

    public static String getMailSmtpStarttlsEnable() {
        return getInstance().mailSmtpStarttlsEnable;
    }

    public static String getMailSmtpAuth() {
        return getInstance().mailSmtpAuth;
    }

    private static Logger log = Logger.getLogger(MalicePropertyContainer.class);
    private static MalicePropertyContainer INSTANCE;
    private String hibernateConfigurationFileName;
    private String[] hibernateMappingPackages;
    private String interceptorXSDFileNamePath;
    private String interceptorXLSFileNamePath;
    private long lloydsRecordId;
    private String droolsConfigurationFileName;
    private long lioRecordId;

    //MAIL
    private final String username;
    private final String password;
    private final String from;
    private final String to;
    private final String mailStoreHost;
    private final String mailStoreProtocol;
    private final String mailSmtpPort;
    private final String mailSmtpHost;
    private final String mailSmtpStarttlsEnable;
    private final String mailSmtpAuth;

    private MalicePropertyContainer() {
        try {
            Properties prop = getPropertiesFromClasspath("/interceptor.properties");
            @SuppressWarnings("unchecked")
            Enumeration<String> propertyEnumenrator = (Enumeration<String>) prop.propertyNames();
            while (propertyEnumenrator.hasMoreElements()) {
                String propertyName = propertyEnumenrator.nextElement();
                String propertyValue = prop.getProperty(propertyName);
                log.info(propertyName + ":" + propertyValue);
            }
            hibernateConfigurationFileName = prop.getProperty("hibernate.cfg.file");
            if (StringUtils.isEmpty(hibernateConfigurationFileName))
                hibernateConfigurationFileName = "/hibernate.cfg.xml";
            String hibernateMappingPackagesTemp = prop.getProperty("hibernate.mapping.packages");
            if (null != hibernateMappingPackagesTemp)
                hibernateMappingPackages = hibernateMappingPackagesTemp.split(",");
            interceptorXSDFileNamePath = prop.getProperty("xsd.complete.pathName");
            interceptorXLSFileNamePath = prop.getProperty("xls.complete.pathName");
            lloydsRecordId = Long.valueOf(prop.getProperty("lloyds.db.id"));
            droolsConfigurationFileName = prop.getProperty("drools.cfg.file");
            lioRecordId = Long.valueOf(prop.getProperty("lio.db.id"));
            //MAIL
            username = prop.getProperty("username");
            password = prop.getProperty("password");
            from = prop.getProperty("from");
            to = prop.getProperty("to");
            mailStoreHost = prop.getProperty("mail.store.host");
            mailStoreProtocol = prop.getProperty("mail.store.protocol");
            mailSmtpPort = prop.getProperty("mail.smtp.port");
            mailSmtpHost = prop.getProperty("mail.smtp.host");
            mailSmtpStarttlsEnable = prop.getProperty("mail.smtp.starttls.enable");
            mailSmtpAuth = prop.getProperty("mail.smtp.auth");

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("ERRORE IN CARICAMENTO FILE DELLE PROPRIETÀ", e);
        }
    }

    private Properties getPropertiesFromClasspath(String propFileName) throws IOException {
        Properties props = new Properties();
        InputStream inputStream = this.getClass().getResourceAsStream(propFileName);
        if (inputStream == null) {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }
        props.load(inputStream);
        inputStream.close();
        return props;
    }

    private static MalicePropertyContainer getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new MalicePropertyContainer();
        }
        return INSTANCE;
    }
}
