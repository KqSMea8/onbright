package com.bright.apollo.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.WeightedResponseTimeRule;
import com.netflix.loadbalancer.ZoneAffinityPredicate;
import com.netflix.loadbalancer.ZoneAvoidanceRule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonConfiguration {

    /**
     * 轮询
     * @return
     */
//    @Bean
//    public IPing ribbonPing() {
//        return new PingUrl();
//    }

    /**
     * 随机
     * @return
     */
   /* @Bean
    public IRule ribbonRule() {
        return new RandomRule();
    }*/
    /**
     * 
     * @return
     */
    public IRule timeRule(){
    	return new ZoneAvoidanceRule();
    }
    /**
     * 随机
     * @return
     */
//    @Bean
//    public ILoadBalancer ribbonLoadBalancer() {
//        return new ILoadBalancer();
//    }
}
