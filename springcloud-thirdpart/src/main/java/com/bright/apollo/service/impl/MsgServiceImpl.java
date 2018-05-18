package com.bright.apollo.service.impl;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.service.MsgService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月9日  
 *@Version:1.1.0  
 */
@Service
public class MsgServiceImpl implements MsgService {

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.MsgService#sendAlter(java.lang.String, java.util.List)  
	 */
	@Override
	public String sendAlter(String msg, List<TUser> list) {
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("api", appKey));
		WebResource webResource = client.resource(batch_send);
		MultivaluedMapImpl formData = new MultivaluedMapImpl();
		String pString = "";
		for (TUser tUser : list) {
			pString +=tUser.getUserName()+",";
		}
		
		pString = pString.substring(0, pString.length()-1);
		formData.add("mobile_list",pString);
		formData.add("message", msg+",请注意！【昂宝电子】");
		
		ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class,formData);
		String textEntity = response.getEntity(String.class);
		return textEntity;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.MsgService#sendWelcome(java.lang.String)  
	 */
	@Override
	public String sendWelcome(String phone) {
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("api", appKey));
		WebResource webResource = client.resource(send);
		MultivaluedMapImpl formData = new MultivaluedMapImpl();

		formData.add("mobile",phone);
		formData.add("message", "Welcome to On-Bright to visit smart building!【昂宝电子】");
		
		ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class,formData);
		String textEntity = response.getEntity(String.class);
		
		return textEntity;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.MsgService#sendGoodBye(java.lang.String)  
	 */
	@Override
	public String sendGoodBye(String phone) {
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("api", appKey));
		WebResource webResource = client.resource(send);
		MultivaluedMapImpl formData = new MultivaluedMapImpl();

		formData.add("mobile",phone);
		formData.add("message", "See you next time【昂宝电子】");
		
		ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class,formData);
		String textEntity = response.getEntity(String.class);
		
		return textEntity;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.MsgService#sendNotice(java.lang.String, java.lang.String)  
	 */
	@Override
	public String sendNotice(String phone, String msg) {
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("api", appKey));
		WebResource webResource = client.resource(send);
		MultivaluedMapImpl formData = new MultivaluedMapImpl();

		formData.add("mobile",phone);
		formData.add("message", msg);
		
		ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class,formData);
		String textEntity = response.getEntity(String.class);
		
		return textEntity;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.MsgService#sendAuthCode(java.lang.String, java.lang.String)  
	 */
	@Override
	public String sendAuthCode(int code, String phone) {
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("api", appKey));
		WebResource webResource = client.resource(send);
		MultivaluedMapImpl formData = new MultivaluedMapImpl();

		formData.add("mobile",phone);
		formData.add("message", "【昂宝电子】您的注册验证码是:"+code+",有效期是5分钟");
		
		ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class,formData);
		String textEntity = response.getEntity(String.class);
		
		return textEntity;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.MsgService#sendForgetPassCode(int, java.lang.String)  
	 */
	@Override
	public String sendCode(int code, String phone) {
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("api", appKey));
		WebResource webResource = client.resource(send);
		MultivaluedMapImpl formData = new MultivaluedMapImpl();

		formData.add("mobile",phone);
		formData.add("message", "【昂宝电子】您的验证码是:"+code+",有效期是5分钟");
		
		ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class,formData);
		String textEntity = response.getEntity(String.class);
		
		return textEntity;
	}

 

}
