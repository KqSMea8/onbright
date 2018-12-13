
package com.bright.apollo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.bright.apollo.enums.SignatureEnum;
import com.bright.apollo.service.SmsService;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年10月26日
 * @Version:1.1.0
 */
@Service
public class SmsServiceImpl implements SmsService {
	private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);
	private static IAcsClient acsClient;

	public SmsServiceImpl() throws ClientException {
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		acsClient = new DefaultAcsClient(profile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.service.SmsService#sendScene(java.lang.String)
	 */
	/*@SuppressWarnings("unchecked")
	@Override
	public SendSmsResponse sendScene(String sceneName, String phone) {
		SendSmsRequest request = null;
		try {
			request = buildSmsRequest(phone, sceneSend);
			// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
			request.setTemplateParam("{\"sceneName\":\""+sceneName+"\"}");
			// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
			// request.setSmsUpExtendCode("90997");
			// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
			request.setOutId("yourOutId");

			// hint 此处可能会抛出异常，注意catch
			return acsClient.getAcsResponse(request);
		} catch (ClientException e) {
			logger.error("===error msg:" + e.getErrMsg() + "====error code:" + e.getErrCode());
		}
		return null;
	}*/

	private SendSmsRequest buildSmsRequest(String phone, String tempCode) throws ClientException {

		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(phone);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName("昂宝电子");
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(tempCode);
		return request;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SmsService#sendCode(java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SendSmsResponse sendCode(String mobile, String validateCode,String sign) {
		SendSmsRequest request = null;
		try {
			request = buildSmsRequest(mobile, identitySend);
			// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
			request.setTemplateParam("{\"code\":\""+validateCode+"\"}");
			// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
			// request.setSmsUpExtendCode("90997");
			// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
			request.setOutId("yourOutId");
			request.setSignName(sign);
			// hint 此处可能会抛出异常，注意catch
			return acsClient.getAcsResponse(request);
		} catch (ClientException e) {
			logger.error("===error msg:" + e.getErrMsg() + "====error code:" + e.getErrCode());
		}
		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SmsService#sendNotice(java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SendSmsResponse sendNotice(String mobile, String pwd,String sign) {
		SendSmsRequest request = null;
		try {
			request = buildSmsRequest(mobile, remoteCodeSend);
			// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
			request.setTemplateParam("{\"code\":\""+pwd+"\"}");
			// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
			// request.setSmsUpExtendCode("90997");
			// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
			request.setOutId("yourOutId");
			request.setSignName(sign);
			// hint 此处可能会抛出异常，注意catch
			return acsClient.getAcsResponse(request);
		} catch (ClientException e) {
			logger.error("===error msg:" + e.getErrMsg() + "====error code:" + e.getErrCode());
		}
		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SmsService#sendAuthCode(int, java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SendSmsResponse sendAuthCode(int code, String mobile,String sign) {
		SendSmsRequest request = null;
		try {
			request = buildSmsRequest(mobile, authSend);
			// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
			request.setTemplateParam("{\"code\":\""+code+"\"}");
			// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
			// request.setSmsUpExtendCode("90997");
			// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
			request.setOutId("yourOutId");
			request.setSignName(sign);
			// hint 此处可能会抛出异常，注意catch
			return acsClient.getAcsResponse(request);
		} catch (ClientException e) {
			logger.error("===error msg:" + e.getErrMsg() + "====error code:" + e.getErrCode());
		}
		return null;
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SmsService#sendScene(java.lang.String, java.lang.String, byte)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SendSmsResponse sendScene(String sceneName, String phone, byte sign) {
		SendSmsRequest request = null;
		try {
			request = buildSmsRequest(phone, sceneSend);
			// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
			request.setTemplateParam("{\"sceneName\":\""+sceneName+"\"}");
			// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
			// request.setSmsUpExtendCode("90997");
			// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
			request.setOutId("yourOutId");
			request.setSignName(sign==SignatureEnum.MIL.getValue()?SignatureEnum.OB.getSign():SignatureEnum.MIL.getSign());
			// hint 此处可能会抛出异常，注意catch
			return acsClient.getAcsResponse(request);
		} catch (ClientException e) {
			logger.error("===error msg:" + e.getErrMsg() + "====error code:" + e.getErrCode());
		}
		return null;
	}
	public static void main(String[] args) {
		SmsServiceImpl serviceImpl;
		try {
			serviceImpl = new SmsServiceImpl();
			//serviceImpl.sendAuthCode(123456, "15879618946");
		} catch (ClientException e) {
			// TODO Auto-generated catch block  
			e.printStackTrace();
		}
		
	}

	 

	
}
