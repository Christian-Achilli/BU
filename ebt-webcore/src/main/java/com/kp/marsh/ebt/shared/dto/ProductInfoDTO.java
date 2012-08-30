/**
 * 
 */
package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author christianachilli
 * Has business info related to this product.
 */
public class ProductInfoDTO extends ProductDTO implements IsSerializable {

	
	private MarshClientDTO mClient; // it's the information owner
	
	// questo probabilmente deve diventare una lista -- TODO
	private BusinessInfoDTO whatInfoAmI; // non è un final balance! a product may have one info type at a time of type consuntivo, one potential and a potential-achieved pair as well.
	
	private BusinessInfoDTO whatInfoWasI; // non è un final balance! usata per fare il rollback e per aggiornare i totali in pagina
	
	private FinalBalanceInfoDTO myFinalBalance; // l'eventuale informazione di consuntivo. può essercene solo una all'anno!
	
	private AchievedInfoDTO achievedInfoDTO;
	
	private ProductTotalsManager totalsManager; // verificare se è usabile, quando ho provato a usarlo nell'aggiornamento dei totali di prodotto era nullo. MI sa che è giusto quello che c'è su totalsumup.
	
	@Deprecated /*Left only for RPC compatibility*/
	public ProductInfoDTO() {}
	
	
	public ProductInfoDTO(MarshClientDTO mClient, int productId,  ProductTotalsManager totalsManager) {
		
		setId(productId);
		
		this.mClient = mClient;
		
		this.totalsManager = totalsManager;
		
	}
	
	public void addBusinessInfo(BusinessInfoDTO bInfo) {
		switch (bInfo.getValueType()) {
		case FINAL_BALANCE:
			this.myFinalBalance = (FinalBalanceInfoDTO)bInfo;
			break;
			
		case ACHIEVED:
			this.achievedInfoDTO = (AchievedInfoDTO)bInfo;
			break;

		default:
			this.whatInfoAmI = bInfo;
			
			this.whatInfoWasI = bInfo;
			break;
		}
		
		if(null != totalsManager) { // it's null if it's a reset info
			totalsManager.add(bInfo);
		}
	}

	/**
	 * @return the client which is information owner of this product business info
	 */
	public MarshClientDTO getmClient() {
		return mClient;
	}


	public BusinessInfoDTO getWhatInfoAmI() {
		return whatInfoAmI;
	}

	
	/**
	 * Should the business info for this product change due to interface events, this method is used to state a new business info object.
	 * Specifically, it's used when a potential info is inserted. So, the typical use case should be from ResetInfoDTO to PotentialInfoDTO and V.v.
	 * @param bid
	 */
	public void changeBusinessInfo(BusinessInfoDTO bid) {
		whatInfoAmI = bid;
	}
	
	/**
	 * Reimposta la business information associata a questo prodotto a quella che era prima di chiamare changeBusinessInfo. Serve per ripristinare il valore sul prodotto in caso di failure del commit.
	 */
	public void rollMyInfoBack() {
		whatInfoAmI = whatInfoWasI;
		whatInfoWasI = null;
	}


	public ProductTotalsManager getTotalsManager() {
		return totalsManager;
	}
	
	public void setTotalsManager(ProductTotalsManager ptm) {
		this.totalsManager = ptm;
	}


	public BusinessInfoDTO getWhatInfoWasI() {
		return whatInfoWasI;
	}


	public void setPreviousBusinessInfo(BusinessInfoDTO bid) {
		whatInfoWasI = whatInfoAmI;
		whatInfoAmI = bid;
		
	}


	public FinalBalanceInfoDTO getMyFinalBalance() {
		return myFinalBalance;
	}


	public void setMyFinalBalance(FinalBalanceInfoDTO myFinalBalance) {
		this.myFinalBalance = myFinalBalance;
	}


	public void setWhatInfoAmI(BusinessInfoDTO whatInfoAmI) {
		this.whatInfoAmI = whatInfoAmI;
	}
	
	public AchievedInfoDTO getAchievedInfoDTO() {
		return achievedInfoDTO;
	}
	
	
	public void setAchievedInfoDTO(AchievedInfoDTO achievedInfoDTO) {
		this.achievedInfoDTO = achievedInfoDTO;
	}
}
