package com.bright.apollo.response;

import java.io.Serializable;
import java.util.List;

import com.bright.apollo.request.IntelligentOpenRecordDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月6日  
 *@Version:1.1.0  
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IntelligentOpenRecordItemDTO implements Serializable{

 
	
	 
 	/**  
	 *   
	 */
	private static final long serialVersionUID = 1688890276235314279L;

	private String dateline;
	 
 	private List<IntelligentOpenRecordDTO> list;
	public String getDateline() {
		return dateline;
	}
	public void setDateline(String dateline) {
		this.dateline = dateline;
	}
	public List<IntelligentOpenRecordDTO> getList() {
		return list;
	}
	public void setList(List<IntelligentOpenRecordDTO> list) {
		this.list = list;
	}
	

}
