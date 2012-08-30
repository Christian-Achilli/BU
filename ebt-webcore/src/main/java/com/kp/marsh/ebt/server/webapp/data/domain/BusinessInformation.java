package com.kp.marsh.ebt.server.webapp.data.domain;

// Generated Feb 1, 2011 12:09:56 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * BusinessInformation generated by hbm2java
 */
public class BusinessInformation implements java.io.Serializable {

	private Integer id;
	private Products products;
	private ReferenceYears referenceYears;
	private InformationOwners informationOwners;
	private Date created;
	private Integer optlock;
	private String valueType;
	private String valueAmount;
	private String postIt;

	public BusinessInformation() {
	}

	public BusinessInformation(Integer id, Products products,
			ReferenceYears referenceYears, InformationOwners informationOwners,
			Date created, String valueType, String valueAmount) {
		this.id = id;
		this.products = products;
		this.referenceYears = referenceYears;
		this.informationOwners = informationOwners;
		this.created = created;
		this.valueType = valueType;
		this.valueAmount = valueAmount;
	}

	public BusinessInformation(int id, Products products,
			ReferenceYears referenceYears, InformationOwners informationOwners,
			Date created, Integer optlock, String valueType, String valueAmount) {
		this.id = id;
		this.products = products;
		this.referenceYears = referenceYears;
		this.informationOwners = informationOwners;
		this.created = created;
		this.optlock = optlock;
		this.valueType = valueType;
		this.valueAmount = valueAmount;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Products getProducts() {
		return this.products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	public ReferenceYears getReferenceYears() {
		return this.referenceYears;
	}

	public void setReferenceYears(ReferenceYears referenceYears) {
		this.referenceYears = referenceYears;
	}

	public InformationOwners getInformationOwners() {
		return this.informationOwners;
	}

	public void setInformationOwners(InformationOwners informationOwners) {
		this.informationOwners = informationOwners;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getOptlock() {
		return this.optlock;
	}

	public void setOptlock(Integer optlock) {
		this.optlock = optlock;
	}

	public String getValueType() {
		return this.valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getValueAmount() {
		return this.valueAmount;
	}

	public void setValueAmount(String valueAmount) {
		this.valueAmount = valueAmount;
	}

	public String getPostIt() {
		return postIt;
	}

	public void setPostIt(String postIt) {
		this.postIt = postIt;
	}

}