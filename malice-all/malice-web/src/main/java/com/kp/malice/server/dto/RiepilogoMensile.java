package com.kp.malice.server.dto;

import java.util.Date;

public class RiepilogoMensile {

	private int titoliIncassati;
	
	private int titoliArretrati;
	
	private int titoliEmessi;
	
	private Date mese;
	
	private long totPremiIncassati; // eurocent
	
	public RiepilogoMensile() {
		mese = new Date();
		titoliIncassati = 123;
		titoliEmessi = 130;
		titoliArretrati = 5;
		totPremiIncassati = 234567687L;
	}

	public int getTitoliIncassati() {
		return titoliIncassati;
	}

	public int getTitoliArretrati() {
		return titoliArretrati;
	}

	public int getTitoliEmessi() {
		return titoliEmessi;
	}

	public Date getMese() {
		return mese;
	}

	public long getTotPremiIncassati() {
		return totPremiIncassati;
	}

}
