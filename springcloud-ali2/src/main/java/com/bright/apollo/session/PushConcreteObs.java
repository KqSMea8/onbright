package com.bright.apollo.session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bright.apollo.bean.PushExceptionMsg;
import com.bright.apollo.bean.PushMessage;
import com.bright.apollo.bean.PushSystemMsg;

@Component
public class PushConcreteObs implements BasicPushObserver {
	private static final Logger logger = LoggerFactory.getLogger(PushConcreteObs.class);

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
            logger.info("===OboxStatusConcreteObs clientSession is null===");
        }

    }

    @Override
    public void update(PushExceptionMsg message, PushSystemMsg msg) {
        logger.info("===Push Exception Msg===");
        pushExceptionPool.handlerMsg(message,msg);
    }
}
