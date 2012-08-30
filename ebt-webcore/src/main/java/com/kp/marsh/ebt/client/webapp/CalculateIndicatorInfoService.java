package com.kp.marsh.ebt.client.webapp;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.kp.marsh.ebt.shared.dto.SintesiDto;

@RemoteServiceRelativePath("calculate")
public interface CalculateIndicatorInfoService extends RemoteService {


	/**
	 * Crea una lista di sintesiDto, uno per ogni gruppo commerciale figlio del padre con id passato come parametro in entrata.
	 * Esempio: passare l'id dell'ufficio per calcore gli indici per tutti i gruppi commerciali dei client executive di quell'ufficio
	 * @param id  InformationOwner id from witch calculate data (sintesiDto) for each line of business
	 * @return List of {@link com.kp.marsh.ebt.ebt.client.shared.dto.SintesiDto}
	 */
	public  ArrayList<SintesiDto> calcolaIndiciByOwnerId(int id);

}