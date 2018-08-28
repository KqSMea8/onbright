package com.bright.apollo.service;

import com.alibaba.fastjson.JSON;
import com.bright.apollo.common.entity.PushMessage;
import com.bright.apollo.mqtt.MqttGateWay;
import com.bright.apollo.redis.RedisBussines;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Set;

@Component
public class PushServiceImpl implements PushService {

    @Autowired
    private RedisBussines redisBussines;

    private static final Logger logger = LoggerFactory.getLogger(PushServiceImpl.class);

    @Autowired
    private MqttGateWay mqttGateWay;

    @Override
    public void pushToApp(PushMessage message, Set<Integer> users) {
        StringBuilder sendStr = new StringBuilder();
        if(message!=null){
            sendStr.append("STR"+JSON.toJSONString(message)+"END");
        }
        Iterator<Integer> iterator = users.iterator();
        String appKeyUserId = "";
        while (iterator.hasNext()){
            Integer uId = iterator.next();
            appKeyUserId = redisBussines.get("appkey_userId"+uId);
            logger.info(" ====== appKeyUserId ====== "+appKeyUserId);
            String [] appKeyUserIdArr = appKeyUserId.split(":");
            for(int i=0;i<appKeyUserIdArr.length;i++){
                if(appKeyUserIdArr[i] !=null&& !appKeyUserIdArr[i].equals("")){
                    mqttGateWay.sendToMqtt("ob-smart."+appKeyUserIdArr[i],sendStr.toString());
                }
            }

        }

    }
}
