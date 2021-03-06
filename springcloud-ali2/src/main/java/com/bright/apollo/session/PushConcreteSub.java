package com.bright.apollo.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.bean.PushExceptionMsg;
import com.bright.apollo.bean.PushSystemMsg;
import com.bright.apollo.common.entity.PushMessage;

@Component
public class PushConcreteSub extends BasicPushSub{
	private static final Logger logger = LoggerFactory.getLogger(PushConcreteSub.class);

    public void sendMessage(PushMessage message, ClientSession clientSession){
        //状态发生改变，通知各个观察者
        this.nodifyObservers(message,clientSession);
    }
    public void sendMessage(PushExceptionMsg message , PushSystemMsg msg){
    	logger.info("===PushExceptionMsg===:"+message+"  ======msg:"+msg);
        //状态发生改变，通知各个观察者
        this.nodifyObservers(message,msg);
    }
}
