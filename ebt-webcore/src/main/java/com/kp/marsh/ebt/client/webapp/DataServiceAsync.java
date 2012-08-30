package com.kp.marsh.ebt.client.webapp;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.kp.marsh.ebt.shared.dto.BusinessInfoDTO;
import com.kp.marsh.ebt.shared.dto.LightInfoOwnerDto;
import com.kp.marsh.ebt.shared.dto.LineOfBusiness;
import com.kp.marsh.ebt.shared.dto.MarshClientDTO;
import com.kp.marsh.ebt.shared.dto.NavigationDTO;
import com.kp.marsh.ebt.shared.dto.ProductInfoDTO;
import com.kp.marsh.ebt.shared.dto.ProductTotalsManager;
import com.kp.marsh.ebt.shared.dto.RowTotalsManager;

/**
 * The async counterpart of <code>DataService</code>.
 */
public interface DataServiceAsync {
	

	void getLobByPageSplit(
			AsyncCallback<ArrayList<ArrayList<LineOfBusiness>>> asyncCallback);

	void commitBusinessInformation(BusinessInfoDTO infoDTO,
			AsyncCallback<Void> callback);

	void getAvailableBusinessInformationByCE(int id,
			AsyncCallback<ArrayList<MarshClientDTO>> callback);

	void getUpdatedRowTotalsManager(int ceId,
			AsyncCallback<RowTotalsManager> asyncCallback);

	void getUpdatedProductTotalsManger(int clientExecutiveId, int producId,
			AsyncCallback<ProductTotalsManager> callback);

	void getCETotalForPotential(int clientExecutiveId, AsyncCallback<Integer> callback);

	void getCETotalForAchieved(int id, AsyncCallback<Integer> callback);
	
	// metodo che serve per disabilitare il cliente
	/**
	 * @param id l'id su INFORMATION_OWNER del cliente che si vuole disabilitare
	 * @param callback
	 */
	void disableClient(int id, AsyncCallback<Void> callback);
	

	void getAvailableInformationByParent(ArrayList<ProductInfoDTO> productsArray, int parentId,
			AsyncCallback<LightInfoOwnerDto[]> callback);

	void getChildrenIdByParentId(int parentId, AsyncCallback<int[]> callback);

	void getUffici(AsyncCallback<ArrayList<NavigationDTO>> callback);

	void getCE(int id, AsyncCallback<ArrayList<NavigationDTO>> asyncCallback);
	
	void getGC(int id, AsyncCallback<ArrayList<NavigationDTO>> asyncCallback);

	void getLobNames(AsyncCallback<ArrayList<String>> callback);

		
}
