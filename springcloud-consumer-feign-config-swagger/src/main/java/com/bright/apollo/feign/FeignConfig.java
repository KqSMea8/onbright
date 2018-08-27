package com.bright.apollo.feign;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.NONE;
    }

//    @Bean
//    public feign.Logger.Level multipartLoggerLevel() {
//        return Logger.Level.FULL;
//    }
}
