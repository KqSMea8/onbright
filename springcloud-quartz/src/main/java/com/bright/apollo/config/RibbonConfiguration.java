package com.bright.apollo.config;

import org.springframework.context.annotation.Configuration;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.ZoneAvoidanceRule;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月17日  
 *@Version:1.1.0  
 */
@Configuration
public class RibbonConfiguration {
	
	public IRule timeRule(){
    	return new ZoneAvoidanceRule();
    }
}
