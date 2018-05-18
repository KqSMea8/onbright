package com.bright.apollo.session;

import com.bright.apollo.bean.PushExceptionMsg;
import com.bright.apollo.bean.PushMessage;
import com.bright.apollo.bean.PushSystemMsg;

import java.util.List;
import java.util.Vector;

public abstract class BasicPushSub {
    /**
     * 用来保存注册的观察者对象
     */
    private List<BasicPushObserver> list = new Vector<BasicPushObserver>();

    /**
     * 注册观察者对象
     *
     * @param OboxStatusObserver
     *            观察者对象
     */
    public void attach(BasicPushObserver pushObserver) {

        list.add(pushObserver);

    }

    /**
     * 删除观察者对象
     *
     * @param OboxStatusObserver
     *            观察者对象
     */
    public void detach(BasicPushObserver OboxStatusObserver) {

        list.remove(OboxStatusObserver);
    }

    /**
     * 通知所有注册的观察者对象
     */
    public void nodifyObservers(PushMessage message, ClientSession clientSession) {

        for (BasicPushObserver pushObserver : list) {
            pushObserver.update(message,clientSession);
        }
    }
    /**
     * 通知所有注册的观察者对象
     */
    public void nodifyObservers(PushExceptionMsg message, PushSystemMsg msg) {

        for (BasicPushObserver pushObserver : list) {
            pushObserver.update(message,msg);
        }
    }
}
