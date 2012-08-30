package com.kp.marsh.ebt.client.webapp;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.kp.marsh.ebt.shared.dto.BusinessInfoDTO;
import com.kp.marsh.ebt.shared.dto.LightInfoOwnerDto;
import com.kp.marsh.ebt.shared.dto.LineOfBusiness;
import com.kp.marsh.ebt.shared.dto.MarshClientDTO;
import com.kp.marsh.ebt.shared.dto.NavigationDTO;
import com.kp.marsh.ebt.shared.dto.ProductInfoDTO;
import com.kp.marsh.ebt.shared.dto.ProductTotalsManager;
import com.kp.marsh.ebt.shared.dto.RowTotalsManager;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("data")
public interface DataService extends RemoteService {

	
	/**
	 * @param productsArray an orderd list of products. The same order is kept in the result.
	 * @param parentId Il parent id  dell'information owner
	 * @return la lista dei children del parent con le relative informazioni di achieved e potenziale divise per prodotto
	 */
	public LightInfoOwnerDto[] getAvailableInformationByParent(ArrayList<ProductInfoDTO> productsArray, int parentId);
	

	/**
	 * DOES NOT RETURN RESET INFO WHEREAS THAT MIGHT BE USEFUL TODO
	 *  The product list of each client is initialized with available business info.
	 * The totals list is initialized as well.
	 * 
	 * @param id the id of the client executive
	 * @param productTotalsManager the manager of the total regarding each product. Its product totals map is filled in this method
	 * @param rowTotalsManager the manager of the totals regarding each client. Its client totals map is filled in this method
	 * @return the LATEST available business information for the clients of the client executive with id <code>id</code>
	 */
	public ArrayList<MarshClientDTO> getAvailableBusinessInformationByCE(int id);
	
	
	/**
	 * @return the list of Lob split by page. Each Lob contains its products.
	 */
	public ArrayList<ArrayList<LineOfBusiness>> getLobByPageSplit();

	
	/**
	 * Might save/delete a potential info and/or a post it note
	 * Trigger the commit phase serverside. The commit might result in an insert or an update
	 * A resetInfo triggers a delete on business information table only if the corresponding post it note is empty
	 * @param toBeCommitted the info linked to an information pill. 
	 */
	public void commitBusinessInformation(BusinessInfoDTO toBeCommitted);
	
	/**
	 * Used in the insert potential phase to instantiate objects used to update ui potential values.
	 * 
	 * @param ceId client executive id
	 * @return a row total manager with a toal map that contains just ONE total manager, that relating the client executive with client id ceId
	 */
	public RowTotalsManager getUpdatedRowTotalsManager(int ceId);
	
	
	/**
	 * @param clientExecutiveId the client executive that triggered the update from the UI 
	 * @param producId the id of the product that has been updated by the user on the UI.
	 * @return a manager whose total map contains just ONE manager that relate to the product just being updated.
	 */
	public ProductTotalsManager getUpdatedProductTotalsManger(int clientExecutiveId, int producId);

	
	public int getCETotalForPotential(int clientExecutiveId);


	public int getCETotalForAchieved(int id);


	/**
	 * @param parentId
	 * @return l'elenco degli id dei figli del parent id in argomento
	 */
	int[] getChildrenIdByParentId(int parentId);


	/**
	 * @param orderdProductsArray i prodotti per i quali si vuole l'informazione
	 * @param ownerId l'ownere dei gruppi commerciali
	 * @return le informazioni sull'achieved e il potenziale dei prodotti specificati in argomento e relativamente a tutti i gruppi commerciali dell'information owner specificato in argomento
	 */
//	LightInfoOwnerDto getAvailableInformationByOwner(int[] orderdProductsArray,
//			int ownerId);

	/**
	* @param id l'id su INFORMATION_OWNER del cliente che si vuole disabilitare
	 */
	void disableClient(int id);


	/**
	 * Ritorna la lista di tutti gli uffici attivi
	 * @return
	 */
	ArrayList<NavigationDTO> getUffici();


	/**
	 * Ritorna una lista di ce per l'id dell'ufficio passato
	 * @param id dell'ufficio
	 * @return lista di ce calcolata a partire dall'id passato
	 */
	ArrayList<NavigationDTO> getCE(int id);
	
	/**
	 * Ritorna una lista di gruppi-commerciali per l'id del client-executive passato
	 * @param id dell'ufficio
	 * @return lista di gruppi-commerciali calcolata a partire dall'id passato
	 */
	ArrayList<NavigationDTO> getGC(int id);
	
	ArrayList<String> getLobNames();

}
