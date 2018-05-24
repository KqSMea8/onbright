package com.bright.apollo.handler;

import org.json.JSONObject;

public abstract class AliBaseHandler {
    /**
     * 处理消息
     * @param ctx
     * @param msg
     * @return
     */
    public abstract void process(String deviceSerialId, JSONObject object) throws Exception;
}
