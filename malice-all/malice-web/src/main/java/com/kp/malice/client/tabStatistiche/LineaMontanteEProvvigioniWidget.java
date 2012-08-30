package com.kp.malice.client.tabStatistiche;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class LineaMontanteEProvvigioniWidget extends Composite implements IsWidget {

    private static LineaMontanteEProvvigioniWidgetUiBinder uiBinder = GWT
            .create(LineaMontanteEProvvigioniWidgetUiBinder.class);

    interface LineaMontanteEProvvigioniWidgetUiBinder extends UiBinder<Widget, LineaMontanteEProvvigioniWidget> {
    }

    public LineaMontanteEProvvigioniWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public native void plotLineaGraph(String stringaJSON)/*-{
		var objectDaJSON = eval('(' + stringaJSON + ')');
		var maxY = objectDaJSON.max;
		if(maxY>1)
			maxY = objectDaJSON.max;
		else
			maxY = 1000;
		$wnd
				.$(function() {
					var plot = $wnd.$.plot($wnd.$("#placeholderL"),
							objectDaJSON.grafoInputs, {
								series : {
									lines : {
										show : true,
										lineWidth : 3
									},
									points : {
										show : true,
										radius : 6.0,
										lineWidth : 3
									}
								},
								colors : [ "#99CC33", "#3880BF" ],
								grid : {
									//									axisMargin : 10,
									hoverable : true,
									clickable : true,
									show : true,
									autoHighlight : true,
									borderColor : '#ccc',
									mouseActiveRadius : 70,
									labelMargin : 60,
									borderWidth : 1
								//									minBorderMargin:10
								},
								yaxis : {
									min : 0,
									max : maxY,
									tickFormatter : function(val, axis) {
										return val + "€";
									}
								},
								xaxis : {
									mode : "time",
									timeformat : "%b %y",
									monthNames : [ "Gen", "Feb", "Mar", "Apr",
											"Mag", "Giu", "Lug", "Ago", "Set",
											"Ott", "Nov", "Dic" ],
									autoscaleMargin : 0.02,
									tickLength : 5
								},
								legend : {
									show : false
								}
							});

					function showTooltip(x, y, contents) {
						$wnd
								.$(
										'<div id="tooltip"><table width="100%" height="100%"><tr width="100%" height="100%"><td valign="middle" align="center" width="100%" >'
												+ contents
												+ '</td></tr></table></div>')
								.css({
									position : 'absolute',
									'font-size' : '0.85em',
									height : '30px',
									width : '70px',
									top : y + 15,
									left : x - 35,
									padding : '2px',
									'background-color' : '#000',
									opacity : 0.80,
									color : '#fff',
									'z-index': 999
								}).appendTo("body").fadeIn(200);
					}

					var previousPoint = null;
					$wnd
							.$("#placeholderL")
							.bind(
									"plothover",
									function(event, pos, item) {
										//										$wnd.$("#x").text(pos.x.toFixed(2));
										//										$wnd.$("#y").text(pos.y.toFixed(2));

										if (item) {
											if (previousPoint != item.dataIndex) {
												previousPoint = item.dataIndex;

												$wnd.$("#tooltip").remove();
												var x = item.datapoint[0]
														.toFixed(2), y = item.datapoint[1]
														.toFixed(0);

												showTooltip(item.pageX,
														item.pageY,
														//														item.series.label
														y + " €");
											}
										} else {
											$wnd.$("#tooltip").remove();
											previousPoint = null;
										}
									});
				});
    }-*/;
}
