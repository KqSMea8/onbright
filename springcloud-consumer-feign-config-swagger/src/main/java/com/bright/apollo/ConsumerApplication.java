package com.bright.apollo;


import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import com.bright.apollo.vo.PicPathVo;



@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableHystrix
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableAsync
@PropertySource("classpath:properties/custom.properties")
public class ConsumerApplication {
	@Autowired
	private PicPathVo picPathVo;
	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}
	@Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(picPathVo.getTempPath());
        return factory.createMultipartConfig();
	}
//	@Bean
//	public HttpMessageConverters fastJsonHttpMessageConverters(){
//		//1、定义convert转换消息对象
//		FastJsonHttpMessageConverter fasConverter  = new  FastJsonHttpMessageConverter();
//		//2、添加fastJson的配置信息，比如：是否要格式化返回json数据
//		FastJsonConfig fastJsonConfig = new FastJsonConfig();
//		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
//		//3、再convert中添加配置信息
//		List<MediaType> fastMediaTypes = new ArrayList<MediaType>();
//		fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//		fasConverter.setSupportedMediaTypes(fastMediaTypes);
//		fasConverter.setFastJsonConfig(fastJsonConfig);
//		HttpMessageConverter<?> converter = fasConverter;
//		return new HttpMessageConverters(converter);
//	}
}
