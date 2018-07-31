/*package com.bright.apollo.listener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.bright.apollo.socket.MNSHandler;
import com.bright.apollo.socket.MNServer;

*//**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月26日  
 *@Version:1.1.0  
 *//*
@Component
public class MNSListener implements ApplicationListener<ContextRefreshedEvent> {
	Logger  logger = Logger.getLogger(MNSListener.class);
	 (non-Javadoc)  
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)  
	 
	@Autowired
	@Lazy
    private MNSHandler mnshandler;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
	//	logger.info("===msn listener start===");
	//	mnshandler.handler();
	}

}
*/