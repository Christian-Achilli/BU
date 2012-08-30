package com.kp.marsh.ebt.server.webapp.data.domain;

// Generated Mar 23, 2011 9:29:35 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * InformationOwners generated by hbm2java
 */
public class InformationOwners implements java.io.Serializable {

	private int id;
	private Date created;
	private Integer optlock;
	private String ownerType;
	private Integer parentId;
	private String description;
	private boolean enabled;
	private String codEys;
	private Set<MarshPeopleCredentials> marshPeopleCredentialses = new HashSet<MarshPeopleCredentials>(
			0);
	private Set<BusinessInformation> businessInformations = new HashSet<BusinessInformation>(
			0);

	public InformationOwners() {
	}

	public InformationOwners(int id, String ownerType, String description,
			boolean enabled, String codEys) {
		this.id = id;
		this.ownerType = ownerType;
		this.description = description;
		this.enabled = enabled;
		this.codEys = codEys;
	}

	public InformationOwners(int id, Date created, Integer optlock,
			String ownerType, Integer parentId, String description,
			boolean enabled, String codEys,
			Set<MarshPeopleCredentials> marshPeopleCredentialses,
			Set<BusinessInformation> businessInformations) {
		this.id = id;
		this.created = created;
		this.optlock = optlock;
		this.ownerType = ownerType;
		this.parentId = parentId;
		this.description = description;
		this.enabled = enabled;
		this.codEys = codEys;
		this.marshPeopleCredentialses = marshPeopleCredentialses;
		this.businessInformations = businessInformations;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getOwnerType() {
		return this.ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getCodEys() {
		return this.codEys;
	}

	public void setCodEys(String codEys) {
		this.codEys = codEys;
	}

	public Set<MarshPeopleCredentials> getMarshPeopleCredentialses() {
		return this.marshPeopleCredentialses;
	}

	public void setMarshPeopleCredentialses(
			Set<MarshPeopleCredentials> marshPeopleCredentialses) {
		this.marshPeopleCredentialses = marshPeopleCredentialses;
	}

	public Set<BusinessInformation> getBusinessInformations() {
		return this.businessInformations;
	}

	public void setBusinessInformations(
			Set<BusinessInformation> businessInformations) {
		this.businessInformations = businessInformations;
	}

}