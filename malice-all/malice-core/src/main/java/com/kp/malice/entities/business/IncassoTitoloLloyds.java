package com.kp.malice.entities.business;

import java.math.BigDecimal;
import java.util.Date;

import com.kp.malice.entities.miscellaneous.DettaglioIncassoTitoloLloyds;

public class IncassoTitoloLloyds {

    private Long ID;
    private Long version;
    private BigDecimal importoIncasso = BigDecimal.ZERO;
    private BigDecimal importoPremioEuroCent = BigDecimal.ZERO;
    private BigDecimal importoProvvigioniEuroCent = BigDecimal.ZERO;
    private StatoIncasso statoIncasso;
    private Date dataIncasso;
    private Date dataInserimento;
    private MezzoPagamento mezzoPagamento;
    private boolean lioDelegatoIncasso;
    private TipoOperazione tipoOperazione;

    public enum TipoOperazione {
        INCASSO, STORNO;

        public static TipoOperazione fromString(String s) {
            for (TipoOperazione op : values()) {
                if (op.name().equals(s))
                    return op;
            }
            return null;
        }
    }

    public Boolean isStorno() {
        return tipoOperazione == TipoOperazione.STORNO;
    }

    public void setStorno(Boolean b) {
        // solo per compatibilità con RF
    }

    public enum MezzoPagamento {
        CONTANTI, BONIFICO, ASSEGNO, BANCOMAT;

        public static MezzoPagamento fromString(String s) {
            for (MezzoPagamento enu : values()) {
                if (enu.name().equals(s)) {
                    return enu;
                }
            }
            return null;
        }

    }

    public enum StatoIncasso {
        EFFETTIVO, SOSPESO;

        public static StatoIncasso fromString(String s) {
            for (StatoIncasso enu : values()) {
                if (enu.name().equals(s)) {
                    return enu;
                }
            }
            return null;
        }

    }

    public String getStringTipoOperazione() {
        return getTipoOperazione().name();
    }
    
    public String getStringMezzoPagamento() {
        return getMezzoPagamento().name();
    }

    public void setStringMezzoPagamento(String mezzoPagamentoString) {
        setMezzoPagamento(MezzoPagamento.fromString(mezzoPagamentoString));
    }

//    public void setStringStatoIncasso(String statoIncassoString) {
//        setStatoIncasso(StatoIncasso.fromString(statoIncassoString));
//    }
    
    public void setStringTipoOperazione(String tipoOperazioneString) {
        setTipoOperazioneEnum(tipoOperazione.fromString(tipoOperazioneString));
    }

    public BigDecimal getImportoProvvigioniEuroCent() {
        return importoProvvigioniEuroCent;
    }

    public void setImportoProvvigioniEuroCent(BigDecimal importoProvvigioniEuroCent) {
        this.importoProvvigioniEuroCent = importoProvvigioniEuroCent;
    }

    public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getStatoIncasso() {
        return statoIncasso.name();
    }

    public void setStatoIncasso(String statoIncasso) {
        this.statoIncasso = StatoIncasso.fromString(statoIncasso);
    }

    public Date getDataIncasso() {
        return dataIncasso;
    }

    public void setDataIncasso(Date dataIncasso) {
        this.dataIncasso = dataIncasso;
    }

    public MezzoPagamento getMezzoPagamento() {
        return mezzoPagamento;
    }

    public void setMezzoPagamento(MezzoPagamento mezzoPagamento) {
        this.mezzoPagamento = mezzoPagamento;
    }
    
    public void setCodMzzPagm(String codMzzPag) {
    	this.mezzoPagamento = MezzoPagamento.fromString(codMzzPag);
    }

    public boolean isLioDelegatoIncasso() {
        return lioDelegatoIncasso;
    }

    public void setLioDelegatoIncasso(boolean lioDelegatoIncasso) {
        this.lioDelegatoIncasso = lioDelegatoIncasso;
    }

    public BigDecimal getImportoIncassoEuroCent() {
        return importoIncasso;
    }

    public void setImportoIncassoEuroCent(BigDecimal importoIncassoEuroCent) {
        this.importoIncasso = importoIncassoEuroCent;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ID == null) ? 0 : ID.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IncassoTitoloLloyds other = (IncassoTitoloLloyds) obj;
        if (ID == null) {
            if (other.ID != null)
                return false;
        } else if (!ID.equals(other.ID))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    public Date getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Date dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public TipoOperazione getTipoOperazione() {
        return tipoOperazione;
    }

    public void setTipoOperazioneEnum(TipoOperazione tipoOperazione) {
        this.tipoOperazione = tipoOperazione;
    }
    
    public void setTipoOperazione(String tipoOperazione) {
    	this.tipoOperazione = TipoOperazione.fromString(tipoOperazione);
    }

    public DettaglioIncassoTitoloLloyds getStorno() {
    	DettaglioIncassoTitoloLloyds storno = new DettaglioIncassoTitoloLloyds();
        storno.setDataIncasso(getDataIncasso());
        storno.setDataInserimento(new Date());
        storno.setImportoIncassoEuroCent(getImportoIncassoEuroCent().negate());
        storno.setMezzoPagamento(getMezzoPagamento());
        storno.setStatoIncasso(getStatoIncasso().toString());
        storno.setTipoOperazioneEnum(TipoOperazione.STORNO);
        return storno;
    }

    public BigDecimal getImportoAbbuonoEuroCent() {
        return calcolaAbbuonoEuroCent(getTipoOperazione(), getImportoIncassoEuroCent(), getImportoPremioEuroCent());
    }

    public void setImportoAbbuonoEuroCent(BigDecimal importoAbbuonoEuroCent) {
        // solo per compatibilità con RF
    }

    public BigDecimal getImportoPremioEuroCent() {
        return importoPremioEuroCent;
    }

    public void setImportoPremioEuroCent(BigDecimal importoPremioEuroCent) {
        this.importoPremioEuroCent = importoPremioEuroCent;
    }

    public static BigDecimal calcolaAbbuonoEuroCent(TipoOperazione op, BigDecimal impPremioIncasso,
            BigDecimal impPremioTitolo) {
        return TipoOperazione.STORNO == op ? impPremioTitolo.add(impPremioIncasso).negate() : impPremioTitolo
                .subtract(impPremioIncasso);
    }

}
