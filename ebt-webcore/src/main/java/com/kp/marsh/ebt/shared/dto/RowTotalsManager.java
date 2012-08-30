/**
 * 
 */
package com.kp.marsh.ebt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;



/**
 * @author christianachilli
 * Handle the total for each element represented on a row.
 */
public class RowTotalsManager extends TotalsManager implements IsSerializable {
	
	/**
	 * @param productId
	 * @return the totals manager for the product with id <code>id</code>
	 */
	public TotalsManager getTotalManagerByRowElementOwnerid(int rowOwnerId) {
		return totalsMap.get(rowOwnerId);
	}
	
	
	public RowTotalsManager() {
		super();
	}
	
	/**
	 * 
	 * Called when iterating business info from DB
	 * @param whatInfoAmI
	 */
	public void add(BusinessInfoDTO whatInfoAmI) {
		
		TotalsManager prodTotMan = getTotalManagerByRowElementOwnerid(whatInfoAmI.getClientId());
		
		if(null == prodTotMan) {
			prodTotMan = new RowTotalsManager();
			totalsMap.put(whatInfoAmI.getClientId(), prodTotMan);
		}
		

		try {
			switch (whatInfoAmI.getValueType()) {

			case ACHIEVED:
				prodTotMan.addAchieved(Integer.parseInt(whatInfoAmI.getValue()));
				break;

			case POTENTIAL:
			case BROKER_POT:
			case COMPANY_POT:
				prodTotMan.addPotential(Integer.parseInt(whatInfoAmI.getValue()));
				break;

			case FINAL_BALANCE:
				prodTotMan.addFinalBalance(Integer.parseInt(whatInfoAmI.getValue()));
				break;
				
			default:
				break;
			}
		} catch (NumberFormatException e) {
			// no need to trace this exception the potential may not be a number. Don't use cast because it's expensive
		}

	}
	
	/**
	 * 
	 * Aggiunge il valore per il tipo specificato
	 * @param infoType di fatto lo si usa solo per i potenziali e i reset, che comunque sono una differenza sempre sul potenziale giˆ inserito
	 * @param newValue la differenza tra il nuovo valore inserito e il valore precedente TOTALE + (NUOVO VALORE - VALORE PRECEDENTE)
	 */
	public void updateValue(BusinessInfoValueType infoType, int clientId, int newValue) {
		TotalsManager prodTotMan = getTotalManagerByRowElementOwnerid(clientId);
		if(null == prodTotMan) {
			prodTotMan = new RowTotalsManager();
			totalsMap.put(clientId, prodTotMan);
		}
		prodTotMan.addPotential(newValue);
	}
	
	
}
