package com.bright.apollo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月29日
 * @Version:1.1.0
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableHystrix
public class QuartzApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuartzApplication.class, args);

	}
}
