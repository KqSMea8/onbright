package com.bright.apollo.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月7日  
 *@Version:1.1.0  
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IntelligentFingerWarnItemDTO implements Serializable{

	/**  
	 *   
	 */
	private static final long serialVersionUID = -2358650887622582652L;
	 
 	private String dateline;
 	private List<IntelligentFingerWarnDTO> list;
	public String getDateline() {
		return dateline;
	}
	public void setDateline(String dateline) {
		this.dateline = dateline;
	}
	public List<IntelligentFingerWarnDTO> getList() {
		return list;
	}
	public void setList(List<IntelligentFingerWarnDTO> list) {
		this.list = list;
	}
 	
}
