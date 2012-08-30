package com.kp.malice.shared.proxies;

import java.math.BigDecimal;
import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.kp.malice.entities.miscellaneous.DettaglioIncassoTitoloLloyds;

@ProxyFor(value = DettaglioIncassoTitoloLloyds.class)
public interface DettaglioIncassoTitoloProxy extends ValueProxy {

	Date getDataIncasso();

	Date getDataInserimento();

	public String getIdentificativoContraente();

	BigDecimal getImportoAbbuonoEuroCent();

	BigDecimal getImportoIncassoEuroCent();

	BigDecimal getImportoProvvigioniEuroCent();

	public String getNumeroTitolo();

	BigDecimal getImportoPremioEuroCent();

	String getStringMezzoPagamento();

	String getStatoIncasso();

	String getStringTipoOperazione();

	boolean isLioDelegatoIncasso();

	Boolean isStorno();
	
	void setDataIncasso(Date dataIncasso);

	public void setIdentificativoContraente(String identificativoContraente);

	void setImportoAbbuonoEuroCent(BigDecimal importo);

	void setImportoIncassoEuroCent(BigDecimal importo);

	void setImportoProvvigioniEuroCent(BigDecimal importo);

	public void setNumeroTitolo(String numeroTitolo);

	public void setImportoPremioEuroCent(BigDecimal premioNetto);
	
	void setStatoIncasso(String statoIncassoString);
	
	void setStorno(Boolean b);

	void setStringMezzoPagamento(String mezzoPagamentoString);
	
	void setStringTipoOperazione(String tipoOperazioneString);

}
