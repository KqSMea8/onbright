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
        StringBuilder appKeyUserId = new StringBuilder();
        while (iterator.hasNext()){
            appKeyUserId.append(redisBussines.get("appkey_userId"+iterator.next()));
            logger.info(" ====== appKeyUserId ====== "+appKeyUserId);
            if(!appKeyUserId.equals("")){
                mqttGateWay.sendToMqtt("ob-smart\\"+appKeyUserId,sendStr.toString());
            }
        }

    }
}
