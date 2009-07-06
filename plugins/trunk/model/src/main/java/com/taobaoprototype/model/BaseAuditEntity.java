package com.taobaoprototype.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class BaseAuditEntity extends BaseEntity {

	@Basic
	private Date createDate;

	@Basic
	private String createdBy;

	@Basic
	private Date updateDate;

	@Basic
	private String updatedBy;
	
	@Basic
	private boolean active;

	public Date getCreateDate() {
		return this.createDate;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@PrePersist
	public void prePersist() {
		this.createDate = new Date();
	}

	@PreUpdate
	public void preUpdate() {
		this.updateDate = new Date();
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean getActive() {
		return active;
	}

}
