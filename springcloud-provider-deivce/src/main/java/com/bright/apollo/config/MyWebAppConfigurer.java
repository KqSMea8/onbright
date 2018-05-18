package com.bright.apollo.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.bright.apollo.Interceptor.SecurityInterceptor;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月29日
 * @Version:1.1.0
 */
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SecurityInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);

	}
}
