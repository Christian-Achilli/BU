package com.kp.marsh.ebt.shared.dto;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class TotalsManager implements IsSerializable {

	public TotalsManager() {
		super();
		totalsMap = new HashMap<Integer, TotalsManager>();
	}

	
	public abstract void add(BusinessInfoDTO businessInfo);
	
	public void setTotalsManager(Integer id, TotalsManager tm) {
		totalsMap.put(id, tm);// the previous value is replaced
	}
	
	protected Map<Integer, TotalsManager> totalsMap;
	
	private int final_balance; // may be called 'actual' as well
	
	private int potential;
	
	private int achieved;
	
	protected void addFinalBalance(int finalBalance) {
		this.final_balance += finalBalance;
	}
	
	protected void addPotential(int potential) {
		this.potential = this.potential + potential;
	}
	
	protected void addAchieved(int achieved) {
		this.achieved += achieved;
	}
	protected void setFinalBalance(int finalBalance) {
		this.final_balance = finalBalance;
	}
	
	protected void setPotential(int potential) {
		this.potential = potential;
	}
	
	protected void setAchieved(int achieved) {
		this.achieved = achieved;
	}
	
	
	public int getTotalFinalBalance() {
		return final_balance;
	}
	
	public int getTotalPotential() {
		return potential;
	}
	
	public int getTotalAchieved() {
		return achieved;
	}

}