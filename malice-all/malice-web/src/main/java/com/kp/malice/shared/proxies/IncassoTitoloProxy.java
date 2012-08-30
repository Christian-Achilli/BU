package com.kp.malice.shared.proxies;

import java.math.BigDecimal;
import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.business.IncassoTitoloLloyds;

@ProxyFor(value = IncassoTitoloLloyds.class)
public interface IncassoTitoloProxy extends ValueProxy {

	Boolean isStorno();

	void setStorno(Boolean b);

	Date getDataIncasso();

	BigDecimal getImportoIncassoEuroCent();

	BigDecimal getImportoAbbuonoEuroCent();

	BigDecimal getImportoProvvigioniEuroCent();

	String getStringMezzoPagamento();

	String getStatoIncasso();

	String getStringTipoOperazione();

	Date getDataInserimento();

	boolean isLioDelegatoIncasso();

	void setImportoIncassoEuroCent(BigDecimal importo);

	void setImportoAbbuonoEuroCent(BigDecimal importo);

	void setImportoProvvigioniEuroCent(BigDecimal importo);

	void setStringMezzoPagamento(String mezzoPagamentoString);

//	void setStringStatoIncasso(String statoIncassoString);

	void setStringTipoOperazione(String tipoOperazioneString);

	void setDataIncasso(Date dataIncasso);

}
