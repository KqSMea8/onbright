package com.bright.apollo.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bright.apollo.bean.PushExceptionMsg;
import com.bright.apollo.bean.PushSystemMsg;
import com.bright.apollo.common.entity.PushMessage;

@Component
public class PushObserverManager {
	@Autowired
    private  PushConcreteSub pushConcreteSub;
    @Autowired
    private  BasicPushObserver pushObserver;

 

    public void sendMessage(PushMessage message, ClientSession clientSession){
    	if(pushConcreteSub.getList()==null||pushConcreteSub.getList().isEmpty())
    		pushConcreteSub.attach(pushObserver);
        pushConcreteSub.sendMessage(message, clientSession);
    }
    public void sendMessage(PushExceptionMsg message, PushSystemMsg msg ){
    	if(pushConcreteSub.getList()==null||pushConcreteSub.getList().isEmpty())
    		pushConcreteSub.attach(pushObserver);
        pushConcreteSub.sendMessage(message,msg);
    }
}
