package com.kp.malice.factories;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.kp.malice.entities.business.IncassoTitoloLloyds;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.Incs;
import com.kp.malice.entities.persisted.TtlCtb;

public class IncassoLloydsFactory {

    private static final Logger log = Logger.getLogger(IncassoLloydsFactory.class);

    public ArrayList<IncassoTitoloLloyds> makeMovimentiContabili(TtlCtb titolo) throws Exception {
        ArrayList<IncassoTitoloLloyds> incassiLloyds = new ArrayList<IncassoTitoloLloyds>();
        for (int i = 0; i < titolo.getIncassi().size(); i++) {
            Incs incasso = titolo.getIncassi().get(i);
            IncassoTitoloLloyds hidratedIncasso = makeIncassoLloyds(incasso);
            incassiLloyds.add(hidratedIncasso);
        }
        return incassiLloyds;
    }

    public IncassoTitoloLloyds makeIncassoLloyds(Incs incs) throws Exception {
        IncassoTitoloLloyds incasso = new IncassoTitoloLloyds();
        incasso.setID(incs.getRecordId());
        incasso.setVersion(incs.getVersion());
        incasso.setDataInserimento(incs.getTmstInsRig());
        incasso.setDataIncasso(incs.getDataIncasso());
        incasso.setImportoIncassoEuroCent(incs.getImportoIncasso());
        incasso.setMezzoPagamento(incs.getCodMzzPagm());
        incasso.setStatoIncasso(incs.getStatoIncasso().toString());
        incasso.setTipoOperazioneEnum(incs.getTipoOperazione());
        incasso.setImportoPremioEuroCent(getImportoPremioEuroCent(incs));
        return incasso;
    }
    
//    public DettaglioIncassoTitoloLloyds makeDettaglioIncassoLloyds(IncassoTitoloLloyds incasso) {
//    	
//    }

    private BigDecimal getImportoPremioEuroCent(Incs incs) throws Exception {
        try {
            BigDecimal abbuono = (BigDecimal) HibernateSessionFactoryUtil
                    .getSession()
                    .createSQLQuery(
                            "select sum(t.impTotAcc+t.impTotIpt+t.impTotNet)  premioDaIncassare"
                                    + "       from TTL_CTB t, DLG_INC d, INCASSI i" + "       where i.ID = d.INC_ID"
                                    + "       and t.id = d.TTL_ID" + "       and i.ID = :incsId")
                    .setParameter("incsId", incs.getRecordId()).uniqueResult();
            return abbuono;
        } catch (Exception e) {
            log.error("ERRORE IN CALCOLO PREMIO DA INCASSARE PER INCASSO CON ID " + incs.getRecordId(), e);
            throw e;
        }
    }
    
}
