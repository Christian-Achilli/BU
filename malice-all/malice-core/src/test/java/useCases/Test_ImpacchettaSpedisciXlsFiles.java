package useCases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.io.Files;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kp.malice.MalicePropertyContainer;
import com.kp.malice.useCases.ImpacchettaAllegaSpedisciFiles;

public class Test_ImpacchettaSpedisciXlsFiles {

    private ImpacchettaAllegaSpedisciFiles impacchettaSpedisciXlsFiles;
    private Logger log = Logger.getLogger(Test_ImpacchettaSpedisciXlsFiles.class);

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void init() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(ImpacchettaAllegaSpedisciFiles.class);
            }
        });
        impacchettaSpedisciXlsFiles = injector.getInstance(ImpacchettaAllegaSpedisciFiles.class);
        pulisciInBox();
    }

    @Test
    public void invia() throws Exception {
        //creo la lista di xls  da cui verra generato lo zip
        File tempDir = Files.createTempDir();
        File estrattoContoExcel1 = File.createTempFile("EC_1", ".xls", tempDir);
        File estrattoContoExcel2 = File.createTempFile("EC_2", ".xls", tempDir);
        Files.touch(estrattoContoExcel1);
        String[] mailsCc = {"maliceinlet-test@kubepartners.it"}; 
        String[] mailsTo = {"dariobrambilla@kubepartners.com"}; 
        //crea il file zip e lo spedisce via mail
        impacchettaSpedisciXlsFiles.comprimiEInviaInAllegato("Prova", tempDir, mailsTo,
        		mailsCc, "subject", "message text");
        //recupero il file zip allegato della mail appena invia e quindi anche ricevuta
        File attachment = getAttachmentFromLastMailReceived();
        log.debug("attachment file name: " + attachment.getName());
        //eseguo le asserzioni per verificare che il tutto sia andato a buon fine
        assertNotNull(attachment);
        if (attachment != null) {
            assertTrue(attachment.length() < (estrattoContoExcel1.length() + estrattoContoExcel2.length()));
            //mi ritorna una lista dei file che compongono lo zip
            List<File> unzippedFileList = unzip((InputStream) new FileInputStream(attachment), "");
            assertEquals(unzippedFileList.size(), 2);
        }
        Files.deleteRecursively(tempDir);
    }

    private ArrayList<File> unzip(InputStream inputStream, String destinationDir) throws IOException {
        ArrayList result = null;
        final int BUFFER = 2048;
        if (inputStream != null && destinationDir != null) {
            result = new ArrayList();
            ZipInputStream zis = null;
            BufferedOutputStream bos = null;
            try {
                zis = new ZipInputStream(new BufferedInputStream(inputStream));
                ZipEntry entry = null;
                while ((entry = zis.getNextEntry()) != null) { //for each zip entry
                    if (entry.isDirectory()) { //directory
                        File dir = new File(destinationDir + File.separator + entry.getName());
                        dir.mkdirs();
                    } else { //file
                        int count;
                        byte data[] = new byte[BUFFER];
                        String fileName = destinationDir + File.separator + entry.getName();
                        FileOutputStream fos = new FileOutputStream(fileName);
                        bos = new BufferedOutputStream(fos, BUFFER);
                        while ((count = zis.read(data, 0, BUFFER)) != -1) {
                            bos.write(data, 0, count);
                        }
                        bos.flush();
                        result.add(fileName);
                    }
                }//next zip entry
                zis.close();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException ioe) { /* ignore */
                    }
                }
                if (zis != null) {
                    try {
                        zis.close();
                    } catch (IOException ioe) { /* ignore */
                    }
                }
            }
        }//else: input value unavailable

        return result;
    }//unzip()

    private File getAttachmentFromLastMailReceived() {
        File f = null;
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", MalicePropertyContainer.getMailStoreProtocol());

        try {
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore(MalicePropertyContainer.getMailStoreProtocol());
            store.connect(MalicePropertyContainer.getMailStoreHost(), MalicePropertyContainer.getFrom(),
                    MalicePropertyContainer.getPassword());
            log.debug(store);

            Folder inbox = store.getFolder("INBOX");
            if (inbox == null) {
                log.debug("No INBOX");
                System.exit(1);
            }
            inbox.open(Folder.READ_ONLY);
            Message messages[] = inbox.getMessages();
            for (int i = 0; i < messages.length; i++) {
                log.debug("Message " + (i + 1));
                Multipart multipart = (Multipart) messages[i].getContent();
                for (int ii = 0; ii < multipart.getCount(); ii++) {
                    BodyPart bodyPart = multipart.getBodyPart(ii);
                    if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                        continue; // dealing with attachments only
                    }
                    InputStream is = bodyPart.getInputStream();
                    f = new File("Attachment_" + bodyPart.getFileName());
                    FileOutputStream fos = new FileOutputStream(f);
                    byte[] buf = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buf)) != -1) {
                        fos.write(buf, 0, bytesRead);
                    }
                    fos.close();
                }
            }
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    private void pulisciInBox() {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", MalicePropertyContainer.getMailStoreProtocol());
        Properties p = System.getProperties();
        Enumeration names = p.propertyNames();
        while (names.hasMoreElements()) {
            String next = (String) names.nextElement();
            System.out.println(next + "\t\t" + p.getProperty(next));
        }

        try {
            Session session = Session.getInstance(props);
            Store store = session.getStore(MalicePropertyContainer.getMailStoreProtocol());
            store.connect(MalicePropertyContainer.getMailStoreHost(), MalicePropertyContainer.getFrom(),
                    MalicePropertyContainer.getPassword());
            log.debug(store);

            Folder inbox = store.getFolder("INBOX");
            if (inbox == null) {
                log.debug("No INBOX");
                System.exit(1);
            }
            inbox.open(Folder.READ_WRITE);
            Message messages[] = inbox.getMessages();
            for (int i = 0; i < messages.length; i++) {
                log.debug("Message " + (i + 1));
                messages[i].setFlag(Flags.Flag.DELETED, true);
            }
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
