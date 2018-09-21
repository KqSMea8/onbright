package com.bright.apollo.service;  

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.bright.apollo.vo.IotDevConncetion;
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年5月4日  
 *@Version:1.1.0  
 */

@Component
public class IotClient {

	private static final Logger logger = LoggerFactory.getLogger(IotClient.class);

    @Autowired
    private IotDevConncetion iotDevConncetion;
    DefaultAcsClient client = null;

    public IotClient(){
        logger.info("------ IotClient ------ ");
    }

    public DefaultAcsClient getClient(String regionId){

        logger.info(" ====== IotClient.getClient method ====== ");
        try {
            IClientProfile profile = DefaultProfile.getProfile(regionId, iotDevConncetion.getAccessKeyId(), iotDevConncetion.getAccessKeySecret());
            DefaultProfile.addEndpoint(regionId, regionId, "Iot", "iot."+regionId+".aliyuncs.com");
            client = new DefaultAcsClient(profile);
            return client;
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }
}
