package com.bright.apollo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月7日  
 *@Version:1.1.0  
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@MapperScan("com.bright.apollo.dao")
public class OauthApplication extends WebMvcConfigurerAdapter{
	public static void main(String[] args) {
		SpringApplication.run(OauthApplication.class, args);
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/oauth/confirm_access").setViewName("authorize");
	}

	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters(){
		//1、定义convert转换消息对象
		FastJsonHttpMessageConverter fasConverter  = new  FastJsonHttpMessageConverter();
		//2、添加fastJson的配置信息，比如：是否要格式化返回json数据
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		//3、再convert中添加配置信息
		fasConverter.setFastJsonConfig(fastJsonConfig);
		HttpMessageConverter<?> converter = fasConverter;
		return new HttpMessageConverters(converter);
	}
}
