package com.bright.apollo.session;

import com.bright.apollo.bean.PushExceptionMsg;
import com.bright.apollo.bean.PushSystemMsg;
import com.bright.apollo.common.entity.PushMessage;

public interface BasicPushObserver {
    /**
     * 推送接口
     *
     */
    public void update(PushMessage message, ClientSession clientSession);

    /**
     * 推送接口
     *
     */
    public void update(PushExceptionMsg message, PushSystemMsg msg);
}
