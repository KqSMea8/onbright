package com.bright.apollo.service;  

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
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

    private final static String accessKeyID="LTAImm6aizjagsfp";
    private final static String accessKeySecret="zNdZ9RuwSU7RG2Lkoon9i2hbVx3gsm";

    public IotClient(){
        System.out.println("------ IotClient ------ ");
    }

    public DefaultAcsClient getClient(String regionId){
        DefaultAcsClient client = null;
        System.out.println("--------=============");
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
