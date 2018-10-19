package com.bright.apollo.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月19日  
 *@Version:1.1.0  
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgExceptionDTO implements Serializable{

	/**  
	 *   
	 */
	private static final long serialVersionUID = -8298305654079630137L;


 
	@JsonProperty("id")
	private  Integer id;
 	@JsonProperty("relevancyId")
	private  Integer relevancy_id;
	 
	@JsonProperty("content")
	private  String content;
	 
	@JsonProperty("type")
	private  Integer type; 
	 
	@JsonProperty("childType")
	private  Integer child_type;
	 
	@JsonProperty("state")
	private Integer state;
	 
	@JsonProperty("sendTime")
	private Long send_time;
	 
	@JsonProperty("name")
	private String name;
	 
	@JsonProperty("serialId")
	private String serialId;
	 
	@JsonProperty
	private  Long time ;
	@JsonProperty
	private  String url ;
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getChild_type() {
		return child_type;
	}

	public void setChild_type(Integer child_type) {
		this.child_type = child_type;
	}
 

 
	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRelevancy_id() {
		return relevancy_id;
	}

	public void setRelevancy_id(Integer relevancy_id) {
		this.relevancy_id = relevancy_id;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getSend_time() {
		return send_time;
	}

	public void setSend_time(Long send_time) {
		this.send_time = send_time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	

}
