package com.taobaoprototype.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity implements BusinessObject {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Version
	private long version;

	public long getId() {
		return id;
	}

	// public void setId(long id) {
	// this.id = id;
	// }

//	/**
//	 * Return business primary keys.
//	 * 
//	 * @return array of fields(properties) name.
//	 */
//	abstract public String[] getBK();
}
