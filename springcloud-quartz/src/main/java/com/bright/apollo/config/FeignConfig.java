package com.bright.apollo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月17日  
 *@Version:1.1.0  
 */
@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

 

}
