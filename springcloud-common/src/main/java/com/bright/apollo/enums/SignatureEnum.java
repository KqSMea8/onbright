package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月11日  
 *@Version:1.1.0  
 */
public enum SignatureEnum {

    OB((byte)0,"昂宝电子","OB"),
    MIL((byte)1,"艾罗艺帝灯饰有限公司","Mi Lamp");
    
    private byte value;

    private String sign;
    
    private String appId;

    private SignatureEnum(byte v,String sign,String appId) {
        this.value = v;
        this.sign=sign;
        this.appId=appId;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;

    }

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

    

}
