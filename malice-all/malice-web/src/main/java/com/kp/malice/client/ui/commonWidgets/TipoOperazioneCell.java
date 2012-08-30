package com.kp.malice.client.ui.commonWidgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.kp.malice.shared.TipoOperazione;

public class TipoOperazioneCell extends AbstractCell<TipoOperazione> {


	public interface TipoOperazioneTemplate extends SafeHtmlTemplates{
		@Template("<div class=\"labelRossa\">{0}</div>")
		SafeHtml redDiv(String qn);
		@Template("<div class=\"labelNera\">{0}</div>")
		SafeHtml blackDiv(String qn);
	}

	private static TipoOperazioneTemplate template=null;

	public TipoOperazioneCell(String... consumedEvents) {
		super(consumedEvents);
		if(template==null){
			template=GWT.create(TipoOperazioneTemplate.class);
		}

	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			TipoOperazione value, SafeHtmlBuilder sb) {
		if(value!=null){
			if(TipoOperazione.STORNO.name().equals(value.name())){
				sb.append(template.redDiv(value.name()));
			} else {
				sb.append(template.blackDiv(value.name()));
			}

		}
		
	}

}