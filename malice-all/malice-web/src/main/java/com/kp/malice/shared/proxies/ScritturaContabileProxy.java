package com.kp.malice.shared.proxies;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.miscellaneous.ScritturaContabileRma;

@ProxyFor(value = ScritturaContabileRma.class)
public interface ScritturaContabileProxy extends ValueProxy {

    Date getTmstInsRig();

    void setTmstInsRig(Date d);

    String getTipoOperazione();

    void setTipoOperazione(String tipoOperazione);

    String getStatoIncasso();

    void setStatoIncasso(String statoIncasso);

    String getCodMzzPagm();

    void setCodMzzPagm(String mezzoPagamento);

    BigDecimal getImportiEuroCent();

    void setImportiEuroCent(BigDecimal premi);

    BigDecimal getPremiEuroCent();

    void setPremiEuroCent(BigDecimal premiIncassati);

    BigDecimal getAbbuoniEuroCent();

    void setAbbuoniEuroCent(BigDecimal abbuoni);

    BigDecimal getProvvigioniEuroCent();

    void setProvvigioniEuroCent(BigDecimal provvigioni);

    BigInteger getCounter();

    void setCounter(BigInteger contatore);
}
