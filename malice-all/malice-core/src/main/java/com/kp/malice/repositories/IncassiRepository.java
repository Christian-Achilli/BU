package com.kp.malice.repositories;

import java.util.List;

import com.kp.malice.entities.business.IncassoTitoloLloyds;

public interface IncassiRepository {

    List<IncassoTitoloLloyds> findMovimentiContabiliByTitoloId(long id) throws Exception;

}
