package com.bright.apollo.vo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年9月21日  
 *@Version:1.1.0  
 */
@Component
public class IotDevConncetion {

	@Value("${IotDevConncetion.accessKeyId}")
	private String accessKeyId;
	@Value("${IotDevConncetion.accessKeySecret}")
	private String accessKeySecret;
	@Value("${IotDevConncetion.endPoint}")
	private String endPoint;
	@Value("${IotDevConncetion.oboxSouthChinaUrl}")
	private String oboxSouthChinaUrl;
	@Value("${IotDevConncetion.oboxSouthChinaName}")
	private String oboxSouthChinaName;
	@Value("${IotDevConncetion.oboxAmericaName}")
	private String oboxAmericaName;
	@Value("${IotDevConncetion.wifiSouthChinaUrl}")
	private String wifiSouthChinaUrl;
	@Value("${IotDevConncetion.wifiSouthChinaName}")
	private String wifiSouthChinaName;
	@Value("${IotDevConncetion.wifiAmericaName}")
	private String wifiAmericaName;
	@Value("${IotDevConncetion.deviceAmericaName}")
	private String deviceAmericaName;
	@Value("${IotDevConncetion.deviceSouthChinaName}")
	private String deviceSouthChinaName;
	public String getAccessKeyId() {
		return accessKeyId;
	}
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	public String getAccessKeySecret() {
		return accessKeySecret;
	}
	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
	public String getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	public String getOboxSouthChinaUrl() {
		return oboxSouthChinaUrl;
	}
	public void setOboxSouthChinaUrl(String oboxSouthChinaUrl) {
		this.oboxSouthChinaUrl = oboxSouthChinaUrl;
	}
	public String getOboxSouthChinaName() {
		return oboxSouthChinaName;
	}
	public void setOboxSouthChinaName(String oboxSouthChinaName) {
		this.oboxSouthChinaName = oboxSouthChinaName;
	}
	public String getOboxAmericaName() {
		return oboxAmericaName;
	}
	public void setOboxAmericaName(String oboxAmericaName) {
		this.oboxAmericaName = oboxAmericaName;
	}
	public String getWifiSouthChinaUrl() {
		return wifiSouthChinaUrl;
	}
	public void setWifiSouthChinaUrl(String wifiSouthChinaUrl) {
		this.wifiSouthChinaUrl = wifiSouthChinaUrl;
	}
	public String getWifiSouthChinaName() {
		return wifiSouthChinaName;
	}
	public void setWifiSouthChinaName(String wifiSouthChinaName) {
		this.wifiSouthChinaName = wifiSouthChinaName;
	}
	public String getWifiAmericaName() {
		return wifiAmericaName;
	}
	public void setWifiAmericaName(String wifiAmericaName) {
		this.wifiAmericaName = wifiAmericaName;
	}
	public String getDeviceAmericaName() {
		return deviceAmericaName;
	}
	public void setDeviceAmericaName(String deviceAmericaName) {
		this.deviceAmericaName = deviceAmericaName;
	}
	public String getDeviceSouthChinaName() {
		return deviceSouthChinaName;
	}
	public void setDeviceSouthChinaName(String deviceSouthChinaName) {
		this.deviceSouthChinaName = deviceSouthChinaName;
	}
	 

 
	
 
	 

 
	
	
}
