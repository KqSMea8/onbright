package com.bright.apollo.session;

import org.springframework.stereotype.Component;

import com.bright.apollo.bean.PushExceptionMsg;
import com.bright.apollo.bean.PushSystemMsg;
import com.bright.apollo.common.entity.PushMessage;

@Component
public class PushObserverManager {
    private  PushConcreteSub pushConcreteSub;

    private  BasicPushObserver pushObserver;

    public PushObserverManager(){
        pushConcreteSub=new PushConcreteSub();

        pushObserver=new PushConcreteObs();

        pushConcreteSub.attach(pushObserver);
    }


    public void sendMessage(PushMessage message, ClientSession clientSession){
        pushConcreteSub.sendMessage(message, clientSession);
    }
    public void sendMessage(PushExceptionMsg message, PushSystemMsg msg ){
        pushConcreteSub.sendMessage(message,msg);
    }
}
