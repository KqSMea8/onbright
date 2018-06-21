package com.bright.apollo.service.AliRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.RpcAcsRequest;
import com.bright.apollo.enums.AliRegionEnum;
import com.bright.apollo.service.IotClient;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月20日  
 *@Version:1.1.0  
 */
@Component
public class BaseRequest {
/*
	private static DefaultAcsClient client;
	private static DefaultAcsClient usclient;*/
	@Autowired
	private IotClient IotClient;
	/*static {
		client = IotClient.getClient(AliRegionEnum.SOURTHCHINA.getValue());
		usclient = IotClient.getClient(AliRegionEnum.AMERICA.getValue());
	}*/

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public  AcsResponse executeCmd(RpcAcsRequest request,AliRegionEnum region) {
		AcsResponse response = null;
		try {
			if(region.equals(AliRegionEnum.AMERICA)){
				response = IotClient.getClient(AliRegionEnum.AMERICA.getValue()).getAcsResponse(request);
			}else{
				response = IotClient.getClient(AliRegionEnum.SOURTHCHINA.getValue()).getAcsResponse(request);
			}
			
		} catch (Exception e) {
			
		}
		return response;
	}
}
