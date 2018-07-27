package com.bright.apollo.common.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年7月27日
 * @Version:1.1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TCreateTableSql implements Serializable {

	/**  
	 *   
	 */
	private static final long serialVersionUID = 2032971716584122815L;

	private Integer id;

	private java.lang.String prefix;

	private java.lang.String suffix;

	private java.util.Date lastOpTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public java.lang.String getPrefix() {
		return prefix;
	}

	public void setPrefix(java.lang.String prefix) {
		this.prefix = prefix;
	}

	public java.lang.String getSuffix() {
		return suffix;
	}

	public void setSuffix(java.lang.String suffix) {
		this.suffix = suffix;
	}

	public java.util.Date getLastOpTime() {
		return lastOpTime;
	}

	public void setLastOpTime(java.util.Date lastOpTime) {
		this.lastOpTime = lastOpTime;
	}

}
