package com.bright.apollo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.listener.ContextListener;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月3日  
 *@Version:1.1.0  
 */

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@ComponentScan(basePackages = {"com.bright.apollo.*"})
@EnableAsync
public class Ali2Application {
	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Ali2Application.class);
        app.addListeners(new ContextListener());
        app.run(args);
//        SpringApplication.run(SpringcloudAli2Application.class, args);
    }
}
