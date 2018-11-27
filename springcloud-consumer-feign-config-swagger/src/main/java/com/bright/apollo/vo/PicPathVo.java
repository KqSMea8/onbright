package com.bright.apollo.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月26日  
 *@Version:1.1.0  
 */
@Component  
@RefreshScope
@ConfigurationProperties(prefix="PicPath")
public class PicPathVo {
	private String tempPath;
	private String realPath;
	private String ftpAddress;
	private String ftpName;
	private String ftpPwd;
	private String ftpPort;

	public String getTempPath() {
		return tempPath;
	}
	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}
	public String getRealPath() {
		return realPath;
	}
	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}
	public String getFtpAddress() {
		return ftpAddress;
	}
	public void setFtpAddress(String ftpAddress) {
		this.ftpAddress = ftpAddress;
	}
	public String getFtpName() {
		return ftpName;
	}
	public void setFtpName(String ftpName) {
		this.ftpName = ftpName;
	}
	public String getFtpPwd() {
		return ftpPwd;
	}
	public void setFtpPwd(String ftpPwd) {
		this.ftpPwd = ftpPwd;
	}
	public String getFtpPort() {
		return ftpPort;
	}
	public void setFtpPort(String ftpPort) {
		this.ftpPort = ftpPort;
	}
	
}
