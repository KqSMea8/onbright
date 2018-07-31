package com.bright.apollo.common.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
 

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月25日  
 *@Version:1.1.0  
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TCreateTableLog implements Serializable{

	/**  
	 *   
	 */
	private static final long serialVersionUID = 4774944593684720656L;

 
	private Integer id;
 
	 
 
	private java.lang.String name;
	 
	private java.util.Date lastOpTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public java.lang.String getName() {
		return name;
	}
	public void setName(java.lang.String name) {
		this.name = name;
	}
	public java.util.Date getLastOpTime() {
		return lastOpTime;
	}
	public void setLastOpTime(java.util.Date lastOpTime) {
		this.lastOpTime = lastOpTime;
	}
 


}
