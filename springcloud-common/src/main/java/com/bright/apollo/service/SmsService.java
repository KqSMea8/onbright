package com.bright.apollo.service;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年10月26日
 * @Version:1.1.0
 */
public interface SmsService {
	static final String product = "Dysmsapi";
	static final String domain = "dysmsapi.aliyuncs.com";
	static final String accessKeyId = "LTAIluedWG2d8XLx";
	static final String accessKeySecret = "SqxupBzigQzCQqqI9KobNCmEq3NXU4";

	static final String sceneSend = "SMS_149102310";//SMS_149150028
	static final String identitySend = "SMS_149150028";//SMS_149150028
	static final String remoteCodeSend = "SMS_149097588";//门锁临时授权码
	static final String authSend = "SMS_149150025";//用户注册验证码
	<T> T sendScene(String sceneName,String phone, byte sign);
	/**  
	 * @param mobile
	 * @param validateCode  
	 * @param sign 
	 * @Description:  
	 */
	<T> T sendCode(String mobile, String validateCode, String sign);
	/**  
	 * @param mobile
	 * @param pwd  
	 * @param sign 
	 * @Description:  
	 */
	<T> T sendNotice(String mobile, String pwd, String sign);
	/**  
	 * @param code
	 * @param mobile  
	 * @param sign 
	 * @Description:  
	 */
	<T> T sendAuthCode(int code, String mobile, String sign);
}
