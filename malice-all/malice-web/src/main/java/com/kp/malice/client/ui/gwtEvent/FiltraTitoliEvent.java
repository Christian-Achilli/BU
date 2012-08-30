package com.kp.malice.client.ui.gwtEvent;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.kp.malice.shared.StatusTitolo;

public class FiltraTitoliEvent extends GwtEvent<FiltraTitoliHandler> {
	private String chiave;
	private List<StatusTitolo> listStatusTitolo;
	private Boolean sospesi;

	public FiltraTitoliEvent(String chiave,
			List<StatusTitolo> listStatusTitolo, Boolean sospesi) {
		this.setChiave(chiave);
		this.setListaStatusTitolo(listStatusTitolo);
		this.sospesi = sospesi;
	}

	public static final Type<FiltraTitoliHandler> TYPE = new Type<FiltraTitoliHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<FiltraTitoliHandler> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(FiltraTitoliHandler handler) {
		handler.filtra(this);
	}

	public String getChiave() {
		return chiave;
	}

	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	public List<StatusTitolo> getListaStatusTitolo() {
		return listStatusTitolo;
	}

	public void setListaStatusTitolo(List<StatusTitolo> listStatusTitolo) {
		this.listStatusTitolo = listStatusTitolo;
	}

	public Boolean getSospesi() {
		return sospesi;
	}

	public void setSospesi(Boolean sospesi) {
		this.sospesi = sospesi;
	}
}