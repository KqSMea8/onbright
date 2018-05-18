package com.bright.apollo.session;

import com.bright.apollo.bean.PushExceptionMsg;
import com.bright.apollo.bean.PushMessage;
import com.bright.apollo.bean.PushSystemMsg;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PushConcreteObs implements BasicPushObserver {
    private static Logger log = Logger.getLogger(PushConcreteObs.class);

    @Autowired
    private PushExceptionPool pushExceptionPool;

    @Override
    public void update(PushMessage message, ClientSession clientSession) {
        /**
         * 更新观察者的状态，使其与目标的状态保持一致
         */
        if (clientSession != null ) {
            clientSession.process(message);
        }else {
            log.info("===OboxStatusConcreteObs clientSession is null===");
        }

    }

    @Override
    public void update(PushExceptionMsg message, PushSystemMsg msg) {
        log.info("===Push Exception Msg===");
        pushExceptionPool.handlerMsg(message,msg);
    }
}
