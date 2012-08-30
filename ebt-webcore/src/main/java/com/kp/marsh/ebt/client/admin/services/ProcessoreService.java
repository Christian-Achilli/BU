package com.kp.marsh.ebt.client.admin.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("processore")
public interface ProcessoreService extends RemoteService {

    /**
     * @return i parametri di connessione al DB attualmente in essere
     */
    String getParametriDB();

    String configuraDb(String fileDbConnectionProperties);

    /**
     * Aggiorna gli uffici di appartenenza dei CE esplorando i dati contenuti nel file csv in argomento alla ricerca di quei CE che sono presenti in INFORMATION_OWNERS
     * @param fileName
     * @return
     * @throws Exception
     */
    String importaAnagrafica(String fileName) throws Exception;

    String importaAchieved(String fileName) throws Exception;

    /**
     * Importa i gruppi commerciali presenti nel file fileName SOLO PER I CE PRESENTI IN INFORMATION_OWNERS.
     * Se un gruppo commerciale è già presente, viene controllata l'associazione con il CE ed eventualmente aggiornata.
     * 
     * @param fileName
     * @return il messaggio di log
     * @throws Exception
     */
    String importaGruppiCommerciali(String fileName) throws Exception;

}
