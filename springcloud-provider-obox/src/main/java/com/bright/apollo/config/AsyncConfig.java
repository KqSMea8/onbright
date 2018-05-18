package com.bright.apollo.config;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月10日  
 *@Version:1.1.0  
 */
@Configuration
public class AsyncConfig {
	//private Logger logger = Logger.getLogger(getClass());
	@Bean
	public AsyncTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("Anno-Executor");
		executor.setCorePoolSize(30);
		executor.setQueueCapacity(100);
		executor.setKeepAliveSeconds(120);
		executor.setMaxPoolSize(100);
	     /* if (corePoolSize < 0 ||
	              maximumPoolSize <= 0 ||
	              maximumPoolSize < corePoolSize ||
	              keepAliveTime < 0)*/
		//deny
		/*executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				
			}
		});*/
		 
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

		return executor;
	}

}
