package com.bright.apollo.service;

import com.alibaba.fastjson.JSON;
import com.bright.apollo.common.entity.PushMessage;
import com.bright.apollo.mqtt.MqttGateWay;
import com.bright.apollo.redis.RedisBussines;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
//            mqttGateWay.sendToMqtt("ob-smart.67A5AA91-880A-48F8-93C2-D91A2D32EEF3","STR===test67A5AA91-880A-48F8-93C2-D91A2D32EEF3===END");
//            mqttGateWay.sendToMqtt("ob-smart.22DDBCF6-E304-4AD9-B9A2-13C4ED915A30","STR===22DDBCF6-E304-4AD9-B9A2-13C4ED915A30===END");
        }
        Iterator<Integer> iterator = users.iterator();
        String appKeyUserId = "";
        while (iterator.hasNext()){
            Integer uId = iterator.next();
            appKeyUserId = redisBussines.get("appkey_userId"+uId);
            if(!StringUtils.isEmpty(appKeyUserId)){
                logger.info(" ====== appKeyUserIds ====== "+appKeyUserId);
                String [] appKeyUserIdArr = appKeyUserId.split(":");
                for(int i=0;i<appKeyUserIdArr.length;i++){
                    if(appKeyUserIdArr[i] !=null&& !appKeyUserIdArr[i].equals("")){
                        logger.info(" ====== appKeyUserId ====== "+appKeyUserIdArr[i]);
                        mqttGateWay.sendToMqtt("ob-smart."+appKeyUserIdArr[i],sendStr.toString());
                    }
                }
            }
        }

    }
}
