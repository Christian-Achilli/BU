package com.kp.malice.shared.proxies;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.businessRules.FunzioniAbilitate;

@ProxyFor(FunzioniAbilitate.class)
public interface FunzioniAbilitateProxy extends ValueProxy {

    boolean isAnnulloTitolo();

    boolean isCreaRettificaProvvigioni();

    boolean isDelegaIncasso();

    boolean isIncassoTitolo();

    boolean isModificaTitolo();

    boolean isRecuperoSospeso();

    boolean isRevocaAnnulloTitolo();

    boolean isStornoIncasso();

    void setAnnulloTitolo(boolean b);

    void setCreaRettificaProvvigioni(boolean b);

    void setDelegaIncasso(boolean b);

    void setIncassoTitolo(boolean b);

    void setModificaTitolo(boolean b);

    void setRecuperoSospeso(boolean b);

    void setRevocaAnnulloTitolo(boolean b);

    void setStornoIncasso(boolean b);

}
