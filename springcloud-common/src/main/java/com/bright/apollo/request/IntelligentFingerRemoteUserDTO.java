package com.bright.apollo.request;

import java.io.Serializable;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;
import com.bright.apollo.tool.DateHelper;
 
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月7日  
 *@Version:1.1.0  
 */
public class IntelligentFingerRemoteUserDTO implements Serializable{

	/**  
	 *   
	 */
	private static final long serialVersionUID = -3808326597944281477L;

 
	private java.lang.Integer id;
 
	private String serialId;
	 
	private String nickName;
 
	private Integer pin;
 
	private String start;
 
	private String end;
 
	private Integer isEnd;
 
	private Integer times;
	 
	private java.lang.Integer useTimes;
 
	private java.lang.String mobile;
 
	private java.lang.String pwd;
	 
	private java.lang.Integer isMax;
 
	private java.lang.String timeLeft;
	public IntelligentFingerRemoteUserDTO(TIntelligentFingerRemoteUser user) {
		setIsMax(user.getIsmax());
		setId(user.getId());
		setSerialId(user.getSerialid());
		setEnd(DateHelper.formatDate(Long.parseLong(user.getEndTime()), DateHelper.FORMATALL));
		setStart(DateHelper.formatDate(Long.parseLong(user.getStartTime()), DateHelper.FORMATALL));
		setPin(user.getUserSerialid());
		setTimes(user.getTimes());
		setUseTimes(user.getUseTimes());
		setMobile(user.getMobile());
		if(user.getUserSerialid()<10){
			setPwd("620"+user.getUserSerialid()+user.getPwd());
		}else{
			setPwd("62"+user.getUserSerialid()+user.getPwd());
		}
		if(StringUtils.isEmpty(user.getNickName())){
			setNickName( user.getUserSerialid()+"");
		}else{
			setNickName(user.getNickName());
		}
		if(Long.parseLong(user.getEndTime())-new Date().getTime()>0){
			setTimeLeft((Long.parseLong(user.getEndTime())-new Date().getTime())/(1000*60)+"");
		}else{
			setTimeLeft("0");
		}
		if(user.getIsend()!=1){
			if(Long.parseLong(user.getStartTime())>new Date().getTime()){
				setIsEnd(-1);
			}else if(Long.parseLong(user.getEndTime())<new Date().getTime()){
				setIsEnd(1);
			}else{
				setIsEnd(0);
			}
		}else{
			setIsEnd(user.getIsend());
		}
	}

	public java.lang.String getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(java.lang.String timeLeft) {
		this.timeLeft = timeLeft;
	}

	public java.lang.String getPwd() {
		return pwd;
	}

	public void setPwd(java.lang.String pwd) {
		this.pwd = pwd;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getPin() {
		return pin;
	}

	public void setPin(Integer pin) {
		this.pin = pin;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public Integer getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(Integer isEnd) {
		this.isEnd = isEnd;
	}

	public java.lang.Integer getUseTimes() {
		return useTimes;
	}

	public void setUseTimes(java.lang.Integer useTimes) {
		this.useTimes = useTimes;
	}

	public java.lang.String getMobile() {
		return mobile;
	}

	public void setMobile(java.lang.String mobile) {
		this.mobile = mobile;
	}

	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getIsMax() {
		return isMax;
	}

	public void setIsMax(java.lang.Integer isMax) {
		this.isMax = isMax;
	}
	

}
