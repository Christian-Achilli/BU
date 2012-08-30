package com.kp.marsh.ebt.server.webapp.data.domain.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kp.marsh.ebt.server.webapp.data.domain.BusinessInformation;
import com.kp.marsh.ebt.server.webapp.data.domain.InformationOwners;
import com.kp.marsh.ebt.server.webapp.data.domain.Products;
import com.kp.marsh.ebt.shared.dto.SintesiDto;

public interface CalculationService {

    /**
     * Ritorna il totale di riga del potenziale per tutti i gruppi commerciali afferenti all'information owner in argomento.
     * 
     * @param clientExecutiveId
     * @param year
     * @return
     */
    public abstract int getTotalPotentialForAnyInformationOwner(int genericInfoOwnerId, int year);

    /**
     * Ritorna il totale di riga dell'achieved  per tutti i gruppi commerciali afferenti all'information owner in argomento.
     * 
     * @param clientExecutiveId
     * @param year
     * @return
     */
    public abstract int getTotalAchievedForAnyInformationOwner(int clientExecutiveId, int year);

    /**
     * 
     * @param l'owner per i quali vengono presi in considerazione tutti i gruppi commerciali a lui afferenti
     * @param year the relevant year
     * @param l'array con gli id dei prodotti cercati
     * @return una mappa con chiave l'id del prodotto e valore la somma dei potenziali per quel prodotto relativamente a tutti i gruppi commerciali che afferiscono a informationOwner
     */
    public abstract Map<Integer, Integer> getTotalPotentialByProductList(InformationOwners informationOwner, int year,
            ArrayList<Integer> productIdList);

    /**
     * Inserendo un qualsiasi information owner viene ritornato il totale del'achieved inserito per il prodotto in ingresso su tutti i gruppi commerciali che afferiscono all'information owner in ingresso.
     * 
     * @param the  owner of the information
     * @param year the relevant year
     * @param valueType the type of info we're searching for
     * @param the id of the product
     * @return the available business information for the information owner
     */
    public abstract Map<Integer, Integer> getTotalAchievedByProductList(InformationOwners informationOwner, int year,
            ArrayList<Integer> productIdList);

    /**
     * @param clientsId
     * @param productsId
     * @param year
     * @param type
     * @param year2
     * @param type2
     * @return number of distinct {@link BusinessInformation} not of the two type specified
     */
    public abstract int countBusinessInformationByClientsIdProductsNotOfTypeSpecified(List<Integer> clientsId,
            List<Integer> productsId, int year, String type, int year2, String type2);

    /**
     * Iterativamente risalgo l'albero degli informationOwners di padre in padre fino al capostipite per l'informazionOwner di cui mi è stato passato l'id
     * @param id information owners di cui cercare l'id dell'antenato esempio: se l'id della nazione nel caso l'IO in questione abbia come ultimo padre la nazione
     * @return id dell'antenato
     */
    public abstract int getIdIOAntenatoFromIdIODiscendente(int id);

    /**
     * Setta su result l'indice di penetrazione, i prodotti lavorati e quelli da lavorare
     * @param iOGruppoCommerciale l'id dell'information owner di riferimento per tutti i gruppi commerciali di interesse, oppure l'id di un gruppo commmerciale
     * @param iOGruppoCommercialeNazione l'id dell'information owner capostifite del precedente (di fatto la NAZIONE)
     * @param referenceYearId l'id dell'anno di riferimento sulla tabella REFERENCE_YEARS
     * @param previousYearId l'id dell'anno di riferimento sulla tabella REFERENCE_YEARS per le business info dei consuntivi
     * @param products la lista degli oggetti Products di interesse
     * @param result il dto dove mettere il risultato
     */
    public abstract void calcolaIndiceDiSaturazioneDelPotenziale(List<Integer> iOGruppoCommerciale,
            List<Integer> iOGruppoCommercialeNazione, int referenceYearId, int previousYearId, List<Products> products,
            SintesiDto result);

    public abstract void calcolaIndiceDiPenetrazione(List<Integer> iOGruppoCommerciale,
            List<Integer> iOGruppoCommercialeNazione, List<Products> products, int referenceThisYearId,
            int referenceLastYearId, SintesiDto lobSintesiDto);

}