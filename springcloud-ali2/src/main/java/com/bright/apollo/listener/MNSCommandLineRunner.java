package com.bright.apollo.listener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.bright.apollo.socket.MNSHandler;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月26日  
 *@Version:1.1.0  
 */
@Component
public class MNSCommandLineRunner implements CommandLineRunner{
	Logger logger = Logger.getLogger(MNSCommandLineRunner.class);
	@Autowired
	@Lazy
    private MNSHandler mnshandler;

	/* (non-Javadoc)  
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])  
	 */
	@Override
	public void run(String... args) throws Exception {
		logger.info("===MNSCommandLineRunner start===");
 		mnshandler.handler();
	
		
	}
	 
}
