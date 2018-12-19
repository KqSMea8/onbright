package com.bright.apollo;


import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.scheduling.annotation.EnableAsync;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年2月28日  
 *@Version:1.1.0  
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableHystrix
@EnableAsync
@MapperScan("com.bright.apollo.dao")
public class DeviceApplication {
	//private static final Log logger =LogFactory.getLog(DeviceApplication.class);
//	private static final Logger logger = LoggerFactory.getLogger(DeviceApplication.class);
	public static void main(String[] args) {
//		logger.info("=====device start====");
		//System.setProperty("org.apache.commons.logging.LogFactory","org.apache.commons.logging.impl.SLF4JLogFactory");
		SpringApplication.run(DeviceApplication.class, args);
	}
}
