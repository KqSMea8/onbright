package com.bright.apollo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

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
@MapperScan("com.bright.apollo.dao")
public class DeviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(DeviceApplication.class, args);
	}
}
