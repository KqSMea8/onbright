package com.bright.apollo.hrstrix;

import java.util.List;

import com.bright.apollo.feign.FeignAliClient;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.response.ResponseObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月16日
 * @Version:1.1.0
 */
@Component
public class  HystrixFeignAli2Fallback extends BasicHystrixFeignFallback implements FeignAliClient {
    private Logger logger = Logger.getLogger(getClass());
}