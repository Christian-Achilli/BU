package com.kp.malice.client.ui.widget;

import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.kp.malice.client.ui.resources.MaliceResources;
import com.kp.malice.shared.proxies.LinkProxy;

public class DownloadDocumentiWidget extends Composite {

    private VerticalPanel verticalPanel = new VerticalPanel();
    private boolean isDisabled;
    private String tableStyle, tdStyle;
    private String downloadCss;
    private ImageResource downloadIconHover;

    public DownloadDocumentiWidget(String tableStyle, String tdStyle, boolean isDisabled, String downloadCss,
            ImageResource downloadIconHover) {
        initWidget(verticalPanel);
        this.isDisabled = isDisabled;
        this.tableStyle = tableStyle;
        this.tdStyle = tdStyle;
        this.downloadCss = downloadCss;
        this.downloadIconHover = downloadIconHover;
    }

    public DownloadDocumentiWidget(String tableStyle, String tdStyle, String downloadCss) {
        initWidget(verticalPanel);
        this.tableStyle = tableStyle;
        this.tdStyle = tdStyle;
        this.downloadCss = downloadCss;
    }

    public DownloadDocumentiWidget() {
        initWidget(verticalPanel);
    }

    public void setLinks(List<LinkProxy> links) {
        if (tableStyle != null)
            verticalPanel.setStylePrimaryName(tableStyle);
        for (LinkProxy linkProxy : links) {
            HorizontalPanel horizontalPanel = new HorizontalPanel();
            horizontalPanel.setHeight("30px");
            horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
            horizontalPanel.add(new HTML(linkProxy.getLabel()));
            HTML div = null;
            if (!isDisabled) {
                String divHtml = "<a href='" + linkProxy.getUrl() + "' target='_blank' class='" + downloadCss + "' >"
                        + "<img src='" + MaliceResources.INSTANCE.iconTrapsarent().getURL()
                        + "' style='width:20px; height:" + 18 + "px' />" + "</a>";
                div = new HTML(divHtml);
            } else {
                div = new HTML("");
            }
            horizontalPanel.add(div);
            horizontalPanel.getWidget(0).setWidth("207px");
            if (tdStyle != null)
                horizontalPanel.getWidget(0).setStylePrimaryName(tdStyle);
            verticalPanel.add(horizontalPanel);
        }
    }

    // /**
    // * @param arrStr coppia label, link
    // */
    // public void setLinks(String[][] labelLinkMatrix) {
    // if (tableStyle != null)
    // verticalPanel.setStylePrimaryName(tableStyle);
    // for (String[] strings : labelLinkMatrix) {
    // final Image imageDownload = new Image(downloadIcon);
    // imageDownload.setVisible(false);
    // HorizontalPanel horizontalPanel = new HorizontalPanel();
    // horizontalPanel.setHeight("30px");
    // horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
    // horizontalPanel.add(new HTML(strings[0]));
    // HTML div = null;
    // if (!isDisabled) {
    // String divHtml = "<a href='" + strings[1] + "' target='_blank' class='"
    // + imageDownload.getUrl() + "' >" +
    // "<img src='"+ MaliceResources.INSTANCE.iconTrapsarent().getURL() +
    // "' style='width:20px; height:"+18+"px' />"+
    // "</a>";
    // div = new HTML(divHtml);
    // } else {
    // div = new HTML("");
    // }
    // horizontalPanel.add(div);
    // horizontalPanel.getWidget(0).setWidth("207px");
    // if (tdStyle != null)
    // horizontalPanel.getWidget(0).setStylePrimaryName(tdStyle);
    // horizontalPanel.getWidget(1).setStylePrimaryName(downloadCss);
    // verticalPanel.add(horizontalPanel);
    // }
    // }

    /**
     * @param arrStr coppia label, link
     */
    public void setLinks(String[][] labelLinkMatrix) {
        if (tableStyle != null)
            verticalPanel.setStylePrimaryName(tableStyle);
        for (String[] strings : labelLinkMatrix) {
            final Image imageDownloadHover = new Image(downloadIconHover);
            imageDownloadHover.setVisible(false);
            HorizontalPanel horizontalPanel = new HorizontalPanel();
            horizontalPanel.setHeight("30px");
            horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            horizontalPanel.add(new HTML(strings[0]));
            HTML div = null;
            if (!isDisabled) {
                String divHtml = "<a href='" + strings[1] + "' target='_blank' class='"
                        + MaliceResources.INSTANCE.main().imageHrefDownloadDoc() + "' width='30px'><img src='"
                        + MaliceResources.INSTANCE.iconTrapsarent().getURL() + "'/></a>";
                div = new HTML(divHtml);
            } else {
                div = new HTML("");
            }
            horizontalPanel.add(div);
            horizontalPanel.getWidget(0).setWidth("207px");
            if (tdStyle != null)
                horizontalPanel.getWidget(0).setStylePrimaryName(tdStyle);
            horizontalPanel.getWidget(1).setStylePrimaryName(downloadCss);
            verticalPanel.add(horizontalPanel);
        }
    }

}