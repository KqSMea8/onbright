package com.bright.apollo.response;

import java.io.Serializable;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月20日  
 *@Version:1.1.0  
 */
public class AliDevInfo implements Serializable{

	/**  
	 *   
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String kitCenter;
	private String deviceName;
	private String productKey;
	private String deviceSecret;
	public String getKitCenter() {
		return kitCenter;
	}
	public void setKitCenter(String kitCenter) {
		this.kitCenter = kitCenter;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getProductKey() {
		return productKey;
	}
	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}
	public String getDeviceSecret() {
		return deviceSecret;
	}
	public void setDeviceSecret(String deviceSecret) {
		this.deviceSecret = deviceSecret;
	}
	@Override
	public String toString() {
		return "AliDevInfo [kitCenter=" + kitCenter + ", deviceName=" + deviceName + ", productKey=" + productKey
				+ ", deviceSecret=" + deviceSecret + "]";
	}
	/**  
	 * @param kitCenter
	 * @param deviceName
	 * @param productKey
	 * @param deviceSecret  
	 */
	public AliDevInfo(String kitCenter, String deviceName, String productKey, String deviceSecret) {
		super();
		this.kitCenter = kitCenter;
		this.deviceName = deviceName;
		this.productKey = productKey;
		this.deviceSecret = deviceSecret;
	}
	/**  
	 *   
	 */
	public AliDevInfo() {
		super();
	}
	 
	
	
}
