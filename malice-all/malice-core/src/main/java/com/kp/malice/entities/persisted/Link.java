package com.kp.malice.entities.persisted;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "LINK")
public class Link implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private long id;
	@Column(name = "LABEL")
	private String label;
	@Column(name = "URL")
	private String url;
	@Column(name = "TYPE", columnDefinition = "varchar(255)", nullable = false)
	private TipoLink tipo;

	public enum TipoLink {
		DOCUMENT, TUTORIAL_VIDEO, TUTORIAL_DOCUMENT;
		
		// the valueOfMethod
		public static TipoLink fromString(String s) {
			for (TipoLink enu : values()) {
				if (enu.name().equals(s)) {
					return enu;
				}
			}
			return null;
		}

		// the identifierMethod
		public String toString(TipoLink tipoLink) {
			return tipoLink.name();
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TipoLink getTipo() {
		return tipo;
	}

	public void setTipo(TipoLink tipo) {
		this.tipo = tipo;
	}
	
	public String getStringTipo() {
		return tipo.name();
	}
	
	public void setStringTipo(String s){
		//only for request factory proxy
	}
	
}
