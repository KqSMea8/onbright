package com.bright.apollo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.bright.apollo.socket.MNSHandler;
import com.bright.apollo.socket.WIFIHandler;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月26日  
 *@Version:1.1.0  
 */
@Component
public class MNSCommandLineRunner implements CommandLineRunner{
	private static Logger logger = LoggerFactory.getLogger(MNSCommandLineRunner.class);
 	@Autowired
	@Lazy
    private MNSHandler mnshandler;

	@Autowired
	private WIFIHandler irhandler;

	/* (non-Javadoc)  
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])  
	 */
	@Override
	public void run(String... args) throws Exception {
		logger.info("===MNSCommandLineRunner start===");
		Thread irThread = new Thread(new RunIRHandler());
		Thread mnsThread = new Thread(new RunMNSHandler());
		irThread.start();
		mnsThread.start();
	}

	class RunIRHandler implements Runnable{

		@Override
		public void run() {
			irhandler.handler();
		}
	}

	class RunMNSHandler implements Runnable{

		@Override
		public void run() {
			mnshandler.handler();
		}
	}
	 
}
