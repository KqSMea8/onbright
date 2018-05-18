package com.bright.apollo.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bright.apollo.redis.RedisBussines;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年5月4日  
 *@Version:1.1.0  
 */
@Component
public class UserCacheService {

	@Autowired
	private RedisBussines redisBussines;
	public String getCode(String mobile) {
		String reply = redisBussines.get("Code__"+mobile);
		return reply;
	}
	
	public void saveOpenId(String openId) {
		redisBussines.setValueWithExpire("openId__"+openId, openId,60*5);
		
	}
	public void saveCode(String mobile, int code) {
		redisBussines.setValueWithExpire("Code__"+mobile, code,60*5);
		
	}
	public void saveForgetPass(String mobile, int code) {
		redisBussines.setValueWithExpire("ForgetPass__"+mobile, code,60*5);
		
	}
	public String getForgetPass(String mobile, int code) { 
		String reply = redisBussines.get("ForgetPass__"+mobile);
		return reply;
	}

	/**  
	 * @param mobile
	 * @param code  
	 * @Description:  
	 */
	public void saveBindCode(String mobile, int code) {
		redisBussines.setValueWithExpire("BindCode__"+mobile, code,60*5);
	}
	public String getBindCode(String mobile) {
		String reply = redisBussines.get("BindCode__"+mobile);
		return reply;
	}

	public <T> void setList(String listName, List<T> list){
		redisBussines.setList(listName,list);
	}

	public <T> List<T> getList(String listName){
		 return redisBussines.getList(listName);
	}

}
