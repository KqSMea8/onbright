package com.bright.apollo.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bright.apollo.redis.RedisBussines;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月10日  
 *@Version:1.1.0  
 */
@Component
public class CacheHelper {
	private static int medium_time=5*60;
	@Autowired
	private RedisBussines redisBussines;
	/**  
	 * @param code
	 * @param openId
	 * @return  
	 * @Description:  
	 */
	public void addOpenId(String code,String openId) {
 		redisBussines.setValueWithExpire(code, openId, medium_time);	
	}
	/**  
	 * @param code
	 * @param openId
	 * @return  
	 * @Description:  
	 */
	public String getOpenId(String code) {
 		String value = redisBussines.get(code);
 		redisBussines.delete(code);
 		return value;
	}
	/**  
	 * @param mobile  
	 * @Description:  
	 */
	public String getCode(String mobile) {
		String reply = (String) redisBussines.get("Code__"+mobile);
		return reply;
		
	}
}
