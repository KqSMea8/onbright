package com.bright.apollo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月2日  
 *@Version:1.1.0  
 */
@Configuration
public class DefaultView extends WebMvcConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(DefaultView.class);
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    	logger.info(" ====== addViewControllers ====== ");
        registry.addViewController("/").setViewName("forward:/login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }
}
