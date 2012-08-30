package com.kp.marsh.ebt.client.admin.i18n;

import gwtupload.client.IUploader.UploaderConstants;

public interface DBParamUploadConstants extends UploaderConstants {

	@DefaultStringValue("There is already an active upload, try later.")
    String uploaderActiveUpload();

    @DefaultStringValue("This file was already uploaded.")
    String uploaderAlreadyDone();

    @DefaultStringValue("It seems the application is configured to use GAE blobstore.\nThe server has raised an error while creating an Upload-Url\nBe sure thar you have enabled billing for this application in order to use blobstore.")
    String uploaderBlobstoreError();

    @DefaultStringValue("Imposta da File i parametri di connessione al DB")
    String uploaderBrowse();

    @DefaultStringValue("Invalid file.\nOnly these types are allowed:\n")
    String uploaderInvalidExtension();

    @DefaultStringValue("Send")
    String uploaderSend();

    @DefaultStringValue("Invalid server response. Have you configured correctly your application in the server side?")
    String uploaderServerError();
    
    @DefaultStringValue("Unable to contact with the server: ")
    String uploaderServerUnavailable();

    @DefaultStringValue("Timeout sending the file:\n perhaps your browser does not send files correctly,\n your session has expired,\n or there was a server error.\nPlease try again.")
    String uploaderTimeout();
	
}
