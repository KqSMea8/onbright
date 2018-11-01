package com.bright.apollo.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bright.apollo.redis.RedisBussines;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月31日  
 *@Version:1.1.0  
 */
@Component
public class CmdCache {
	@Autowired
	private RedisBussines redisBussines;

	/**  
	 * @param key
	 * @param value  
	 * @Description:  
	 */
	public void saveGroup(String key,String value) {
		redisBussines.setValueWithExpire(key, value, 60*10);
	}
}
