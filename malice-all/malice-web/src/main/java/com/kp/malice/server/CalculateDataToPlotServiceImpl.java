package com.kp.malice.server;

import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.kp.malice.client.tabSintesi.CalculateDataToPlotService;

public class CalculateDataToPlotServiceImpl extends RemoteServiceServlet implements CalculateDataToPlotService {

	private static final long serialVersionUID = 4131211213665619004L;

	private static Logger log = Logger.getLogger(CalculateDataToPlotServiceImpl.class);

	@Override
	public String calculateData() {
		JSONObject jsonPlotter = new JSONObject();
		// array json che contiene i grafici
		JSONArray jsonArrayGraphs = new JSONArray();
		double max = 0;
		long dateTime= new Date().getTime();
		long month = 1000*60*60*24*31L;
		try {
			for (double ii = 1; ii <= 2; ii++) {
				// array che contiene i punti per il grafo
				JSONArray jsonArrayPoints = new JSONArray();
				for (int i = 1; i < 10; i++) {
					// array che rappresente un singolo punto del grafo
					JSONArray jsonArraySinglePoint = new JSONArray();
					double ordinata = Math.random() * 30;
					if (max < ordinata)
						max = ordinata;
					long thisMonth = i*month;
					long dateX = dateTime + thisMonth;
					jsonArraySinglePoint.put(dateX); // ascissa
					jsonArraySinglePoint.put(ordinata); // ordinata
					jsonArrayPoints.put(jsonArraySinglePoint);
				}
				// oggetto json che contiene i punti del grafo + la label
				JSONObject jsonObjectSingleGraph = new JSONObject();
				jsonObjectSingleGraph.put("label", "myLabel" + ii);
				jsonObjectSingleGraph.put("data", jsonArrayPoints);
				jsonArrayGraphs.put(jsonObjectSingleGraph);
			}
			max += (max/5); //h del piano cartesiamo = all'altezza massima + 20% altezza massima
			log.debug("max="+max);
			jsonPlotter.put("max",max);
			jsonPlotter.put("grafoInputs", jsonArrayGraphs);
		} catch (JSONException e) {
			log.error("ERROR during calulate data by rpc:", e);
		}
		return jsonPlotter.toString();
	}
}
