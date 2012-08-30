package com.kp.marsh.ebt.client.admin.ui;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.IUploader.UploaderConstants;
import gwtupload.client.MultiUploader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.kp.marsh.ebt.client.admin.i18n.AchievedUploadConstants;
import com.kp.marsh.ebt.client.admin.i18n.AnagraficheUploadConstants;
import com.kp.marsh.ebt.client.admin.i18n.DBParamUploadConstants;
import com.kp.marsh.ebt.client.admin.services.ProcessoreService;
import com.kp.marsh.ebt.client.admin.services.ProcessoreServiceAsync;

public class FormComando extends Composite {

	private static FormComandoUiBinder uiBinder = GWT.create(FormComandoUiBinder.class);
	interface FormComandoUiBinder extends UiBinder<Widget, FormComando> {
	}


	public static final UploaderConstants I18N_DB_CONSTANTS = GWT.create(DBParamUploadConstants.class);
	public static final UploaderConstants I18N_ANAG_CONSTANTS = GWT.create(AnagraficheUploadConstants.class);
	public static final UploaderConstants I18N_ACH_CONSTANTS = GWT.create(AchievedUploadConstants.class);
	private final ProcessoreServiceAsync processaService = (ProcessoreServiceAsync) GWT.create(ProcessoreService.class);

	@UiField
	MultiUploader uploaderConnection;
	@UiField
	MultiUploader uploaderAnagrafiche;
	@UiField
	MultiUploader uploaderAchieved;
	
	@UiField
	MultiUploader uploaderGruppiCommerciali;

	@UiField
	TextArea logDbParam;

	@UiField 
	TextArea logAnag;

	@UiField
	TextArea logAch;
	
	@UiField
	TextArea logAnagGC;



	final String baseUrl;


	public FormComando() {
		initWidget(uiBinder.createAndBindUi(this));
		uploaderConnection.setI18Constants(I18N_DB_CONSTANTS);
		uploaderAnagrafiche.setI18Constants(I18N_ANAG_CONSTANTS);
		uploaderAchieved.setI18Constants(I18N_ACH_CONSTANTS);
		baseUrl = GWT.getHostPageBaseURL();
		logDbParam.setReadOnly(true);
		logAnag.setReadOnly(true);
		logAch.setReadOnly(true);

//		processaService.getParametriDB(new AsyncCallback<String>() {
//			
//			@Override
//			public void onSuccess(String result) {
//				logDbParam.setText("\nParametri preimpostati:\n"+result);
//				
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				logDbParam.setText("Errore nel recupero dei parametri di connesione al DB:\n"+caught.getLocalizedMessage());
//				
//			}
//		});
		
		//		html.setHTML(sb.toString());


		uploaderConnection.addOnFinishUploadHandler(new OnFinishUploaderHandler() {

			@Override
			public void onFinish(IUploader uploader) {

				final StringBuffer sb = new StringBuffer("--Inizio--\n");
				String urlDbConnectionProperties;

				if (uploader.getStatus() == Status.SUCCESS) {
					urlDbConnectionProperties = baseUrl+uploader.fileUrl();
					UploadedInfo info = uploader.getServerInfo();
					GWT.log("File name " + info.name);
					GWT.log("File content-type " + info.ctype);
					GWT.log("File size " + info.size);
					GWT.log("File size " +uploader.getFileInput().getFilename());
					sb.append("file: "+uploader.getFileInput().getFilename()+"\n");

					logDbParam.setText(sb.toString());

					processaService.configuraDb(urlDbConnectionProperties, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							sb.append(" \nerrore in caricamento parametri db: "+caught.getLocalizedMessage()+"\n");
							sb.append("\n--Fine--\n");
							logDbParam.setText(sb.toString());

						}

						@Override
						public void onSuccess(String result) {
							sb.append(" Parametri di configurazione caricati: "+result);
							sb.append("\n--Fine--\n");
							logDbParam.setText(sb.toString());

						}

					});

				} else {
					logDbParam.setText("Errore nel caricamento (STATO: "+uploader.getStatus().name()+")\n--Fine--\n");

				}

			}
		});


		uploaderAnagrafiche.addOnFinishUploadHandler(new OnFinishUploaderHandler() {

			@Override
			public void onFinish(IUploader uploader) {

				final StringBuffer sb = new StringBuffer("--Inizio--\n");
				logAnag.setText(sb.toString());
				String fileCsvAnag;

				if (uploader.getStatus() == Status.SUCCESS) {
					fileCsvAnag = baseUrl+uploader.fileUrl();
					UploadedInfo info = uploader.getServerInfo();
					GWT.log("File name " + info.name);
					GWT.log("File content-type " + info.ctype);
					GWT.log("File size " + info.size);
					GWT.log("File size " +uploader.getFileInput().getFilename());
					sb.append("file: "+uploader.getFileInput().getFilename()+"\n");
					logAnag.setText(sb.toString()+"\nimport in progress\n");

					 final Timer t = new Timer() {
						 int count = 0;
						 boolean goUp = true;
						 
					      public void run() {
					    	  if(goUp) {
					    		  sb.append(".");
					    	  } else {
					    		  sb.deleteCharAt(sb.length()-1);
					    	  }
					    	  count++;
					    	  if(count%10 == 0) {
					    		  goUp = !goUp;
					    	  }
					    	  logAnag.setText(sb.toString());
					    	  
					      }
					    };
					
					t.scheduleRepeating(500);
					
					processaService.importaAnagrafica(fileCsvAnag, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							t.cancel();
							sb.append(" \nerrore in caricamento anagrafiche: "+caught.getLocalizedMessage()+"\n");
							sb.append("\n--Fine--\n");
							logAnag.setText(sb.toString());

						}

						@Override
						public void onSuccess(String result) {
							t.cancel();
							sb.append(" Anagrafiche caricate: \n"+result);
							sb.append("\n--Fine--\n");
							logAnag.setText(sb.toString());

						}

					});

				} else {
					logAnag.setText("Errore nel caricamento (STATO: "+uploader.getStatus().name()+")\n--Fine--\n");

				}

			}
		});

		uploaderGruppiCommerciali.addOnFinishUploadHandler(new OnFinishUploaderHandler() {

			@Override
			public void onFinish(IUploader uploader) {

				final StringBuffer sb = new StringBuffer("--Inizio--\n");
				logAnagGC.setText(sb.toString());
				String fileCsvAnag;

				if (uploader.getStatus() == Status.SUCCESS) {
					fileCsvAnag = baseUrl+uploader.fileUrl();
					UploadedInfo info = uploader.getServerInfo();
					GWT.log("File name " + info.name);
					GWT.log("File content-type " + info.ctype);
					GWT.log("File size " + info.size);
					GWT.log("File size " +uploader.getFileInput().getFilename());
					sb.append("file: "+uploader.getFileInput().getFilename()+"\n");
					logAnagGC.setText(sb.toString()+"\nimport in progress\n");

					 final Timer t = new Timer() {
						 int count = 0;
						 boolean goUp = true;
						 
					      public void run() {
					    	  if(goUp) {
					    		  sb.append(".");
					    	  } else {
					    		  sb.deleteCharAt(sb.length()-1);
					    	  }
					    	  count++;
					    	  if(count%10 == 0) {
					    		  goUp = !goUp;
					    	  }
					    	  logAnagGC.setText(sb.toString());
					    	  
					      }
					    };
					
					t.scheduleRepeating(500);
					
					processaService.importaGruppiCommerciali(fileCsvAnag, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							t.cancel();
							sb.append(" \nerrore in caricamento CE-Gruppi commerciali: "+caught.getLocalizedMessage()+"\n");
							sb.append("\n--Fine--\n");
							logAnagGC.setText(sb.toString());

						}

						@Override
						public void onSuccess(String result) {
							t.cancel();
							sb.append(" Relazioni Ce-Gruppi Commerciali caricate: \n"+result);
							sb.append("\n--Fine--\n");
							logAnagGC.setText(sb.toString());

						}

					});

				} else {
					logAnagGC.setText("Errore nel caricamento (STATO: "+uploader.getStatus().name()+")\n--Fine--\n");

				}

			}
		});
		

		uploaderAchieved.addOnFinishUploadHandler(new OnFinishUploaderHandler() {

			@Override
			public void onFinish(IUploader uploader) {

				final StringBuffer sb = new StringBuffer("--Inizio--\n");
				String fileCsvAch;

				if (uploader.getStatus() == Status.SUCCESS) {
					fileCsvAch = baseUrl+uploader.fileUrl();
					UploadedInfo info = uploader.getServerInfo();
					GWT.log("File name " + info.name);
					GWT.log("File content-type " + info.ctype);
					GWT.log("File size " + info.size);
					GWT.log("File size " +uploader.getFileInput().getFilename());
					sb.append("file: "+uploader.getFileInput().getFilename()+"\n");
					logAch.setText(sb.toString()+"\nimport in progress\n");

					final Timer t = new Timer() {
						 int count = 0;
						 boolean goUp = true;
						 
					      public void run() {
					    	  if(goUp) {
					    		  sb.append(".");
					    	  } else {
					    		  sb.deleteCharAt(sb.length()-1);
					    	  }
					    	  count++;
					    	  if(count%10 == 0) {
					    		  goUp = !goUp;
					    	  }
					    	  logAch.setText(sb.toString());
					      }
					    };
					
					t.scheduleRepeating(1000);
					
					processaService.importaAchieved(fileCsvAch, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							t.cancel();
							sb.append(" \nerrore in caricamento achieved: "+caught.getLocalizedMessage()+"\n");
							sb.append("\n--Fine--\n");
							logAch.setText(sb.toString());

						}

						@Override
						public void onSuccess(String result) {
							t.cancel();
							sb.append(" Achieved caricati: \n"+result);
							sb.append("\n--Fine--\n");
							logAch.setText(sb.toString());

						}

					});

				} else {
					logAch.setText("Errore nel caricamento (STATO: "+uploader.getStatus().name()+")\n--Fine--\n");

				}

			}
		});
	}

}