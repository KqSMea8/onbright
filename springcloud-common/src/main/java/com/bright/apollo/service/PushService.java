package com.bright.apollo.service;

import java.util.Set;

import com.bright.apollo.common.entity.PushMessage;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月23日  
 *@Version:1.1.0  
 */
public interface PushService {
	void pushToApp(PushMessage message,Set<Integer> users );
}
