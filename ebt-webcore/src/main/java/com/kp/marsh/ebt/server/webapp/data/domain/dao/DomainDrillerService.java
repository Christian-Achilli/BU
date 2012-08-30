/**
 * 
 */
package com.kp.marsh.ebt.server.webapp.data.domain.dao;

import java.util.List;

import com.kp.marsh.ebt.server.webapp.data.domain.BusinessInformation;
import com.kp.marsh.ebt.server.webapp.data.domain.InformationOwners;
import com.kp.marsh.ebt.server.webapp.data.domain.Products;
import com.kp.marsh.ebt.server.webapp.data.domain.ReferenceYears;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;

/**
 * @author christianachilli
 *
 *Fornisce i metodi per interrogare e aggiornare il DB.
 *
 */
public interface DomainDrillerService {

    /**
     * @param instance
     * @return all the available business info for a given client-product pair in certain Year
     */
    public List<BusinessInformation> findBusinessInformationByExample(int informationOwneroID, int yearID, int productID);

    /**
     * @param informationOwner the marsh client
     * @param year the relevant year
     * @param valueType the type of info we're searching for
     * @return the available business information for the client executive's client
     */
    public List<BusinessInformation> getAvailableBusinessInformationByType(int informationOwner, int year,
            BusinessInfoValueType valueType);

    /**
     * @param parent the information owner of which I want to know its children
     * @return the list of information owners object linked to <code>parent</code> argument or an empty list if it's a <code>client executive</code>
     */
    public List<InformationOwners> getInformationOwnersChildren(int parent);

    /**
     * @return the list of the available persisted years
     */
    public List<ReferenceYears> getAvailableYears();

    /**
     * @return the list of the available line of business that are enabled.
     */
    public List<Products> getAvailableLOB();

    /**
     * @param lob
     * @return the enabled products for the <code>lob</code>
     */
    public List<Products> getAvailableProductsByLOB(Products lob);

    /**
     * @param lobId
     * @return id list of product from lob specified by id 
     */
    public List<Integer> getAvailableProductsIdByLobId(int lobId);

    /**
     * @param myInfo the info to be inserted or updated
     */
    public void pushToDB(BusinessInformation myInfo);

    public InformationOwners findInformationOwnerById(int marshClientOwnerId);

    public List<InformationOwners> findInformationOwnerByParentId(int parentId);

    public Products findProductById(int productId);

    public BusinessInformation findBusinessInformationById(int busInfoId);

    public void mergeInformationOwner(InformationOwners object);

    /**
     * When the user delete the potential info already inserted and the pill gets in the reset state, the corresponding business info has to be deleted
     * @param persistentInfo the business info to be deleted
     */
    public void deleteBusinessInfo(BusinessInformation persistentInfo);

    /**
     * @param clientExecutiveId
     * @param producId
     * @param year
     * @return the list of available POTENTIAL business information for the period in <code>year</code> for the client executive with id <code>clientExecutiveId</code>
     */
    public List<BusinessInformation> getAvailablePotentialBusinessInformationByProduct(int clientExecutiveId,
            int producId, int year);

    /**
     * @param clientExecutiveId
     * @param lobId
     * @param year
     * @param valueType
     * @return the total for the line of business for the type <code>valueType</code> and the client executive <code>clientExecutiveId</code>
     */
    public int getLOBTotalByLobId(int clientExecutive, String lobId, ReferenceYears year,
            BusinessInfoValueType valueType);

    /**
     * @param year
     * @return the id of the year specified from {@link com.kp.marsh.ebt.server.data.domain.ReferenceYears}
     */
    public int getReferenceYearIdFromYear(int year);

    /**
     * @param id
     * @return return a list of id of GruppoCommerciale ({@link InflormationOwners}) from an Information Owner id
     */
    public List<Integer> findGruppoCommerciale(int id);

    /**
     * @return return a list of id of all Lob ({@link Products})
     */
    public List<Integer> getAllLobsId();

    /**
     * @param list
     * @return list of client founded starting from a list of {@link InformationOwners}
     */
    public List<InformationOwners> findInformationOwnerClientInList(List<InformationOwners> list);

    /**
     * @return the reference year whose enabled at invoke time
     */
    public ReferenceYears getEnabledReferenceYear();

    /**
     * @param i
     * @return the Reference Year whose reference year member is equal to <code>i</code>
     */
    public ReferenceYears findReferenceYearByNumber(int i);

    /**
     * Ritorna la lista degli uffici (attivi)
     * @return lista di IO (uffici)
     */
    public List<InformationOwners> getUffici();

    /**
     * Ritorna la lista dei client-executive (attivi) per quell'ufficio
     * @return lista di IO (client-executive)
     */
    public List<InformationOwners> getCE(int id);

    /**
     * Ritorna la lista dei gruppi-commerciali (attivi) per quel client-executive
     * @return lista di IO (gruppi-commerciali)
     */
    public List<InformationOwners> getGC(int id);

    /**
     * @param codEys filtro su cui eseguire la riceca degli IO
     * @return IO trovato
     */
    public InformationOwners findInformationOwnerByEysCode(String codEys);
}
