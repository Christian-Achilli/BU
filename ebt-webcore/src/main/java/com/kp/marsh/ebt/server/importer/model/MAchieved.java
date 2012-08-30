package com.kp.marsh.ebt.server.importer.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="M_ACHIEVED")
public class MAchieved implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1731917252920797688L;
	
	@Id
	@Column(name="anno_contabile")
	private String annoContabile;
	@Id
	@Column(name="ID_AE_CGruppo")
	private String idAeCgruppo;
	@Id
	@Column(name="AE_CGruppo")
	private String aeCgruppo;
	@Id
	@Column(name="Codice_Capogruppo")
	private String codiceCapogruppo;
	@Id
	@Column(name="Capogruppo")
	private String capogruppo;
	@Id
	@Column(name="Ramo")
	private String ramo;
	@Id
	@Column(name="Ramo_Descrizione")
	private String ramoDescrizione;
	@Id
	@Column(name="Cod_Garanzia")
	private String codGaranzia;
	@Id
	@Column(name="net_revenues")
	private Double netRevenues;

	public MAchieved() {
	}

	public MAchieved(String annoContabile, String idAeCgruppo,
			String aeCgruppo, String codiceCapogruppo, String capogruppo,
			String ramo, String ramoDescrizione, String codGaranzia,
			Double netRevenues, String ebtAreaBusiness, String ebtProdotto) {
		this.annoContabile = annoContabile;
		this.idAeCgruppo = idAeCgruppo;
		this.aeCgruppo = aeCgruppo;
		this.codiceCapogruppo = codiceCapogruppo;
		this.capogruppo = capogruppo;
		this.ramo = ramo;
		this.ramoDescrizione = ramoDescrizione;
		this.codGaranzia = codGaranzia;
		this.netRevenues = netRevenues;
	}

	public String getAnnoContabile() {
		return this.annoContabile;
	}

	public void setAnnoContabile(String annoContabile) {
		this.annoContabile = annoContabile;
	}

	public String getIdAeCgruppo() {
		return this.idAeCgruppo;
	}

	public void setIdAeCgruppo(String idAeCgruppo) {
		this.idAeCgruppo = idAeCgruppo;
	}

	public String getAeCgruppo() {
		return this.aeCgruppo;
	}

	public void setAeCgruppo(String aeCgruppo) {
		this.aeCgruppo = aeCgruppo;
	}

	public String getCodiceCapogruppo() {
		return this.codiceCapogruppo;
	}

	public void setCodiceCapogruppo(String codiceCapogruppo) {
		this.codiceCapogruppo = codiceCapogruppo;
	}

	public String getCapogruppo() {
		return this.capogruppo;
	}

	public void setCapogruppo(String capogruppo) {
		this.capogruppo = capogruppo;
	}

	public String getRamo() {
		return this.ramo;
	}

	public void setRamo(String ramo) {
		this.ramo = ramo;
	}

	public String getRamoDescrizione() {
		return this.ramoDescrizione;
	}

	public void setRamoDescrizione(String ramoDescrizione) {
		this.ramoDescrizione = ramoDescrizione;
	}

	public String getCodGaranzia() {
		return this.codGaranzia;
	}

	public void setCodGaranzia(String codGaranzia) {
		this.codGaranzia = codGaranzia;
	}

	public Double getNetRevenues() {
		return this.netRevenues;
	}

	public void setNetRevenues(Double netRevenues) {
		this.netRevenues = netRevenues;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MAchieved))
			return false;
		MAchieved castOther = (MAchieved) other;

		return ((this.getAnnoContabile() == castOther.getAnnoContabile()) || (this
				.getAnnoContabile() != null
				&& castOther.getAnnoContabile() != null && this
				.getAnnoContabile().equals(castOther.getAnnoContabile())))
				&& ((this.getIdAeCgruppo() == castOther.getIdAeCgruppo()) || (this
						.getIdAeCgruppo() != null
						&& castOther.getIdAeCgruppo() != null && this
						.getIdAeCgruppo().equals(castOther.getIdAeCgruppo())))
				&& ((this.getAeCgruppo() == castOther.getAeCgruppo()) || (this
						.getAeCgruppo() != null
						&& castOther.getAeCgruppo() != null && this
						.getAeCgruppo().equals(castOther.getAeCgruppo())))
				&& ((this.getCodiceCapogruppo() == castOther
						.getCodiceCapogruppo()) || (this.getCodiceCapogruppo() != null
						&& castOther.getCodiceCapogruppo() != null && this
						.getCodiceCapogruppo().equals(
								castOther.getCodiceCapogruppo())))
				&& ((this.getCapogruppo() == castOther.getCapogruppo()) || (this
						.getCapogruppo() != null
						&& castOther.getCapogruppo() != null && this
						.getCapogruppo().equals(castOther.getCapogruppo())))
				&& ((this.getRamo() == castOther.getRamo()) || (this.getRamo() != null
						&& castOther.getRamo() != null && this.getRamo()
						.equals(castOther.getRamo())))
				&& ((this.getRamoDescrizione() == castOther
						.getRamoDescrizione()) || (this.getRamoDescrizione() != null
						&& castOther.getRamoDescrizione() != null && this
						.getRamoDescrizione().equals(
								castOther.getRamoDescrizione())))
				&& ((this.getCodGaranzia() == castOther.getCodGaranzia()) || (this
						.getCodGaranzia() != null
						&& castOther.getCodGaranzia() != null && this
						.getCodGaranzia().equals(castOther.getCodGaranzia())))
				&& ((this.getNetRevenues() == castOther.getNetRevenues()) || (this
						.getNetRevenues() != null
						&& castOther.getNetRevenues() != null && this
						.getNetRevenues().equals(castOther.getNetRevenues())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getAnnoContabile() == null ? 0 : this.getAnnoContabile()
						.hashCode());
		result = 37
				* result
				+ (getIdAeCgruppo() == null ? 0 : this.getIdAeCgruppo()
						.hashCode());
		result = 37 * result
				+ (getAeCgruppo() == null ? 0 : this.getAeCgruppo().hashCode());
		result = 37
				* result
				+ (getCodiceCapogruppo() == null ? 0 : this
						.getCodiceCapogruppo().hashCode());
		result = 37
				* result
				+ (getCapogruppo() == null ? 0 : this.getCapogruppo()
						.hashCode());
		result = 37 * result
				+ (getRamo() == null ? 0 : this.getRamo().hashCode());
		result = 37
				* result
				+ (getRamoDescrizione() == null ? 0 : this.getRamoDescrizione()
						.hashCode());
		result = 37
				* result
				+ (getCodGaranzia() == null ? 0 : this.getCodGaranzia()
						.hashCode());
		result = 37
				* result
				+ (getNetRevenues() == null ? 0 : this.getNetRevenues()
						.hashCode());
		return result;
	}

}
