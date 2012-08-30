package com.kp.malice.client.ui.widget;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.kp.malice.client.MaliceDebugIds;

public class EnsuredDbgIdTextCell extends TextCell {

    private static EnsuredDbgIdTextCellTemplate template = null;

    public EnsuredDbgIdTextCell() {
        super();
        if (template == null) {
            template = GWT.create(EnsuredDbgIdTextCellTemplate.class);
        }
    }

    public interface EnsuredDbgIdTextCellTemplate extends SafeHtmlTemplates {
        @Template("<div id=\"" + MaliceDebugIds.GWT_COMMON_DBG_PREFIX
                + "{0}\" style=\"outline:none;\" tabindex=\"0\">{0}</div>")
        SafeHtml withValueAsDebugId(String value);
    }

    @Override
    public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
        if (value != null) {
            sb.append(template.withValueAsDebugId(value.asString()));
        }
    }

}
