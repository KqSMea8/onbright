package com.bright.apollo.service;  

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年5月4日  
 *@Version:1.1.0  
 */

@Component
public class IotClient {

    Logger logger = Logger.getLogger(IotClient.class);

    private final static String accessKeyID="LTAIBE0b86xFi9q5";
    private final static String accessKeySecret="Ym9F1CNAgwhbxt5Sk1Qki1nr6w6e3v";
    DefaultAcsClient client = null;

    public IotClient(){
        logger.info("------ IotClient ------ ");
    }

    public DefaultAcsClient getClient(String regionId){

        logger.info(" ====== IotClient.getClient method ====== ");
        try {
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyID, accessKeySecret);
            DefaultProfile.addEndpoint(regionId, regionId, "Iot", "iot."+regionId+".aliyuncs.com");
            client = new DefaultAcsClient(profile);
            return client;
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }
}
