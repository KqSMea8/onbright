package com.bright.apollo.feign;

import com.bright.apollo.hrstrix.HystrixFeignAli2Fallback;
import org.springframework.cloud.netflix.feign.FeignClient;


/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月2日  
 *@Version:1.1.0  
 */
@FeignClient(name = "springcloud-ali2",fallback = HystrixFeignAli2Fallback.class, configuration = FeignConfig.class)
public interface FeignAliClient {


}
