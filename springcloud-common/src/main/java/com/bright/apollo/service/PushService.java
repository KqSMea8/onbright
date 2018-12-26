package com.bright.apollo.service;

import java.util.Map;
import java.util.Set;

import com.bright.apollo.common.entity.PushMessage;
import com.bright.apollo.common.entity.TUserAliDevice;
import com.bright.apollo.response.ResponseObject;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月23日  
 *@Version:1.1.0  
 */
public interface PushService {
	void pushToApp(PushMessage message,Set<Integer> users );
	void pairIrRemotecode(Map<String,Object> map, Set<TUserAliDevice> users);
}
