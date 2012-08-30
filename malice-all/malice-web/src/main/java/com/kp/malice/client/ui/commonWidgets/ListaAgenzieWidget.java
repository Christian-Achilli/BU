package com.kp.malice.client.ui.commonWidgets;

import java.util.Comparator;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.kp.malice.client.ui.gwtEvent.SelezioneAgenziaEvent;
import com.kp.malice.client.ui.gwtEvent.SelezioneAgenziaEvent.SelezioneAgenziaHandler;
import com.kp.malice.client.ui.resources.ListeResources;
import com.kp.malice.client.ui.resources.SimplePagerMalice;
import com.kp.malice.client.ui.resources.SimplePagerMalice.PagerResources;
import com.kp.malice.shared.proxies.AgencyProxy;

public class ListaAgenzieWidget extends Composite {

	private static ListaAgenzieWidgetUiBinder uiBinder = GWT.create(ListaAgenzieWidgetUiBinder.class);

	interface ListaAgenzieWidgetUiBinder extends UiBinder<Widget, ListaAgenzieWidget> {
	}

	private int NUM_Agenzie_X_PAG = 10;

	@UiField(provided = true)
	CellTable<AgencyProxy> cellTable;

	@UiField(provided = true)
	SimplePagerMalice pager;

	private Column<AgencyProxy, String> companyNameColumn;
	private Column<AgencyProxy, String> cityColumn;
	private Column<AgencyProxy, String> loydsCodeColumn;
	private Column<AgencyProxy, String> maliceCodeColumn;
	private Column<AgencyProxy, String> rmaCodeColumn;
	private Column<AgencyProxy, String> emailColumn;

	public ListaAgenzieWidget() {
		cellTable = new CellTable<AgencyProxy>(NUM_Agenzie_X_PAG, ListeResources.INSTANCE);
		cellTable.setWidth("100%", true);
		cellTable.addCellPreviewHandler(new Handler<AgencyProxy>() {
			long lastClick = -1000;

			@Override
			public void onCellPreview(CellPreviewEvent<AgencyProxy> event) {
				if (event.getNativeEvent().getType().contains("click")) {
					AgencyProxy agencyProxySelected = event.getValue();
					GWT.log("ListaAgenzieWidget.onCellPreview: fire SelezioneTitoloEvent");
					fireEvent(new SelezioneAgenziaEvent(agencyProxySelected));
				}
			}
		});

		pager = new SimplePagerMalice(TextLocation.CENTER, PagerResources.INSTANCE, false, 0, true);
		pager.setDisplay(cellTable);

		companyNameColumn = new Column<AgencyProxy, String>(new TextCell()) {
			@Override
			public String getValue(AgencyProxy object) {
				return object.getCompanyName();
			}
		};
		cityColumn = new Column<AgencyProxy, String>(new TextCell()) {
			@Override
			public String getValue(AgencyProxy object) {
				return object.getCity();
			}
		};
		loydsCodeColumn = new Column<AgencyProxy, String>(new TextCell()) {
			@Override
			public String getValue(AgencyProxy object) {
				return object.getLoydsCode();
			}
		};
		maliceCodeColumn = new Column<AgencyProxy, String>(new TextCell()) {
			@Override
			public String getValue(AgencyProxy object) {
				return object.getMaliceCode();
			}
		};
		rmaCodeColumn = new Column<AgencyProxy, String>(new TextCell()) {
			@Override
			public String getValue(AgencyProxy object) {
				return object.getRmaCode();
			}
		};
		emailColumn = new Column<AgencyProxy, String>(new TextCell()) {
			@Override
			public String getValue(AgencyProxy object) {
				return object.getEmail();
			}
		};

		cellTable.addColumn(companyNameColumn, "NOME COMPAGNIA");
		cellTable.addColumn(cityColumn, "CITTA'");
		cellTable.addColumn(loydsCodeColumn, "LLOYDS CODE");
		cellTable.addColumn(maliceCodeColumn, "MALICE CODE");
		cellTable.addColumn(rmaCodeColumn, "RMA CODE");
		cellTable.addColumn(emailColumn, "EMAIL");

		cellTable.setColumnWidth(companyNameColumn, 120, Unit.PX);
		cellTable.setColumnWidth(cityColumn, 85, Unit.PX);
		cellTable.setColumnWidth(loydsCodeColumn, 70, Unit.PX);
		cellTable.setColumnWidth(maliceCodeColumn, 70, Unit.PX);
		cellTable.setColumnWidth(rmaCodeColumn, 70, Unit.PX);
		cellTable.setColumnWidth(emailColumn, 165, Unit.PX);

		for (int i = 0; i < cellTable.getColumnCount(); i++) {
			cellTable.getColumn(i).setSortable(true);
		}

		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setComparatorsAndSortHandler(ListHandler<AgencyProxy> sortHandler) {

		sortHandler.setComparator(companyNameColumn, new Comparator<AgencyProxy>() {
			public int compare(AgencyProxy o1, AgencyProxy o2) {
				return o1.getCompanyName().compareTo(o2.getCompanyName());
			}
		});
		sortHandler.setComparator(cityColumn, new Comparator<AgencyProxy>() {
			public int compare(AgencyProxy o1, AgencyProxy o2) {
				return o1.getCity().compareTo(o2.getCity());
			}
		});
		sortHandler.setComparator(loydsCodeColumn, new Comparator<AgencyProxy>() {
			public int compare(AgencyProxy o1, AgencyProxy o2) {
				return o1.getLoydsCode().compareTo(o2.getLoydsCode());
			}
		});
		sortHandler.setComparator(maliceCodeColumn, new Comparator<AgencyProxy>() {
			public int compare(AgencyProxy o1, AgencyProxy o2) {
				return o1.getMaliceCode().compareTo(o2.getMaliceCode());
			}
		});
		sortHandler.setComparator(rmaCodeColumn, new Comparator<AgencyProxy>() {
			public int compare(AgencyProxy o1, AgencyProxy o2) {
				return o1.getRmaCode().compareTo(o2.getRmaCode());
			}
		});
		sortHandler.setComparator(emailColumn, new Comparator<AgencyProxy>() {
			public int compare(AgencyProxy o1, AgencyProxy o2) {
				return o1.getEmail().compareTo(o2.getEmail());
			}
		});

		cellTable.addColumnSortHandler(sortHandler);
	}

	public CellTable<AgencyProxy> getTabella() {
		return cellTable;
	}

	public HandlerRegistration addSelezioneAgenziaHandler(SelezioneAgenziaHandler handler) {
		return addHandler(handler, SelezioneAgenziaEvent.TYPE);
	}
}
