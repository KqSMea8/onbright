package com.bright.apollo.handler;

import com.bright.apollo.cache.AliDevCache;
import com.bright.apollo.service.AliDeviceConfigService;
import com.bright.apollo.service.TopicServer;
import com.bright.apollo.service.YaoKongYunConfig;
import com.bright.apollo.service.YaoKongYunService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AliBaseHandler {
    @Autowired
    protected AliDeviceConfigService aliDeviceConfigService;

    @Autowired
    protected YaoKongYunConfig yaoKongYunConfig;

    @Autowired
    protected AliDevCache aliDevCache;

    @Autowired
    protected YaoKongYunService yaoKongYunService;

    @Autowired
    protected TopicServer topicServer;
    /**
     * 处理消息
     * @param ctx
     * @param msg
     * @return
     */
    public abstract void process(String deviceSerialId, JSONObject object) throws Exception;
}
