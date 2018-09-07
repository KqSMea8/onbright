package com.bright.apollo.request;

import java.io.Serializable;

import com.bright.apollo.common.entity.TIntelligentFingerPush;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月9日  
 *@Version:1.1.0  
 */
public class TIntelligentFingerPushDTO implements Serializable{

	
	/**  
	 *   
	 */
	private static final long serialVersionUID = -5839536195060309333L;
	@Expose
	@SerializedName("value")
	private Integer value;
	@Expose
	@SerializedName("enable")
	private Integer enable;
	@Expose
	@SerializedName("serialId")
	private String serialId;
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public String getSerialId() {
		return serialId;
	}
	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}
	/**  
	 *   
	 */
	public TIntelligentFingerPushDTO(TIntelligentFingerPush push) {
		super();
		if(push==null)
			return;
		setValue(push.getValue());
		setEnable(push.getEnable());
		setSerialId(push.getSerialid());
	}
	/**  
	 *   
	 */
	public TIntelligentFingerPushDTO() {
		super();
	}
 	
}
