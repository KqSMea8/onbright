package com.bright.apollo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    @Override
     public void addResourceHandlers(ResourceHandlerRegistry registry){
                 registry.addResourceHandler("/**")
                         .addResourceLocations("classpath:/static/");//这里将/static文件夹定为资源目录，需要根据实际更换
    }
}
