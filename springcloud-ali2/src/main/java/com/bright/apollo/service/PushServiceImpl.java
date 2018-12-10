package com.bright.apollo.service;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.bright.apollo.common.entity.PushMessage;
import com.bright.apollo.mqtt.MqttGateWay;
import com.bright.apollo.redis.RedisBussines;

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
        }else{
        	logger.warn("===send To App Mesaage is null===");
        	return;
        }
        logger.info("===ToApp===type:"+message.getType()+"===serialId:"+message.getSerialId());
        logger.info("====== sendStr ======"+sendStr);
        Iterator<Integer> iterator = users.iterator();
        String tokenUserIdVal = "";
        while (iterator.hasNext()){
            Integer uId = iterator.next();
            String accessToken = (String)redisBussines.getObject("token_userId_"+uId);
            logger.info("====== accessToken ====== "+accessToken +" ====== userId ====== "+uId);
            if(!StringUtils.isEmpty(accessToken)){
                logger.info(" ====== send mqtt message ====== ");
                mqttGateWay.sendToMqtt("ob-smart."+accessToken,sendStr.toString());
            }
        }
    }

    @Override
    public void pairIrRemotecode(Map<String,Object> map, Integer userId) {
        String accessToken = (String)redisBussines.getObject("token_userId_"+userId);
        StringBuilder sendStr = new StringBuilder();
        if(map!=null){
            sendStr.append("STR"+map.toString()+"END");
        }
        logger.info("pairIrRemotecode ====== Str ======= "+sendStr);
        logger.info("====== accessToken ====== "+accessToken +" ====== userId ====== "+userId);
        if(!StringUtils.isEmpty(accessToken)){
            logger.info(" ====== send mqtt message ====== ");
            mqttGateWay.sendToMqtt("ob-smart."+accessToken,sendStr.toString());
        }
    }
}
