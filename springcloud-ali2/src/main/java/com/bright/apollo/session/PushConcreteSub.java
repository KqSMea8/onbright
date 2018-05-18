package com.bright.apollo.session;

import com.bright.apollo.bean.PushExceptionMsg;
import com.bright.apollo.bean.PushMessage;
import com.bright.apollo.bean.PushSystemMsg;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class PushConcreteSub extends BasicPushSub{
    private static Logger log = Logger.getLogger(PushConcreteSub.class);

    public void sendMessage(PushMessage message, ClientSession clientSession){
        //状态发生改变，通知各个观察者
        this.nodifyObservers(message,clientSession);
    }
    public void sendMessage(PushExceptionMsg message , PushSystemMsg msg){
        log.info("===nodifyObservers===:"+message.toString());
        //状态发生改变，通知各个观察者
        this.nodifyObservers(message,msg);
    }
}
