package com.kp.malice.useCases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.log4j.Logger;

import com.kp.malice.MalicePropertyContainer;

public class ImpacchettaAllegaSpedisciFiles {

	private final Logger log = Logger
			.getLogger(ImpacchettaAllegaSpedisciFiles.class);

	public void comprimiEInviaInAllegato(String nomeAllegato, File tempDir,
			String[] receiverEmailAddress, String[] cc, String subject,
			String messageText) throws Exception {
		FilenameFilter filterXls = filter("xls");
		FilenameFilter filterZip = filter("zip");
		log.debug("Ricevuti " + tempDir.list(filterXls).length
				+ " da impacchettare e spedire per mail");
		impacchettaXlsFilesNellaTempDir(tempDir, nomeAllegato);
		spedisciFilePerMail(
				tempDir.listFiles(filterZip)[0],
				receiverEmailAddress,
				cc,
				subject,
				aggiungiElencoFileAlTestoDelMessaggio(tempDir, messageText,
						filterXls));
	}

	private String aggiungiElencoFileAlTestoDelMessaggio(File tempDir,
			String messageText, FilenameFilter filterXls) {
		StringBuffer messageTextWithFiles = new StringBuffer(messageText
				+ "\n\nL'allegato contiene "
				+ tempDir.listFiles(filterXls).length + " file:\n");
		log.debug(messageText);
		for (File file : tempDir.listFiles(filterXls)) {
			messageTextWithFiles.append("\t" + file.getName() + "\n");
			log.debug(file.getName() + "        " + file.length() + " bytes");
		}
		messageTextWithFiles.append("\n--Fine del Messaggio--");
		return messageTextWithFiles.toString();
	}

	/**
	 * Invia un mail con allegato il file zip
	 * 
	 * @param zipXlsFile
	 *            il file da allegare
	 * @param to
	 *            il destinatario
	 * @param messageText
	 *            il testo della mail
	 */
	private void spedisciFilePerMail(File zipXlsFile, String[] to, String[] cc,
			String subject, String messageText) throws Exception {
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath(zipXlsFile.getAbsolutePath());
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription("Estratti conto");
		attachment.setName(zipXlsFile.getName());
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName(MalicePropertyContainer.getMailSmtpHost());
		email.setAuthentication(MalicePropertyContainer.getUsername(),
				MalicePropertyContainer.getPassword());
		email.setSmtpPort(Integer.valueOf(MalicePropertyContainer
				.getMailSmtpPort()));
		email.setTLS(true);
		log.debug("username: " + MalicePropertyContainer.getUsername());
		for (String string : to) {
			email.addTo(string);
			log.debug("to: " + string);
		}
		for (String string : cc) {
			email.addCc(string);
			log.debug("cc: " + string);
		}
		email.setFrom(MalicePropertyContainer.getFrom(), "Portale GAR");
		email.setSubject(subject);
		email.setMsg(messageText);
		email.attach(attachment);
		email.send();
		log.debug("from: " + MalicePropertyContainer.getFrom());
	}

	/**
	 * Crea il file zip impacchetta i file presenti nella lista
	 * 
	 * @param xlsFileList
	 *            lista di file da impacchettare
	 * @param target
	 *            file di destinazione (lo zip)
	 */
	private void impacchettaXlsFilesNellaTempDir(File tempDir, String zipName)
			throws Exception {
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];
		try {
			// Create the ZIP file
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					tempDir.getCanonicalPath() + "/" + zipName + ".zip"));
			FilenameFilter filter = filter("xls");
			// Compress the files
			for (File fileXls : tempDir.listFiles(filter)) {
				FileInputStream in = new FileInputStream(fileXls);
				// Add ZIP entry to output stream.
				log.debug("aggiungo allo zip il file " + fileXls.getName());
				out.putNextEntry(new ZipEntry(fileXls.getName()));
				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				// Complete the entry
				out.closeEntry();
				in.close();
			}

			// Complete the ZIP file
			out.close();
			FilenameFilter filterZip = filter("zip");
			File[] fZip = tempDir.listFiles(filterZip);
			log.debug("Creato ZIP file " + fZip[0].getPath() + " "
					+ fZip[0].length() + " bites");
			log.debug("File zippato con successo");
		} catch (IOException e) {
			log.error("ERRORE durante la compressione dei file xls: "
					+ e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	public static FilenameFilter filter(final String regex) {
		return new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.endsWith(regex);
			}
		};
	}

}
