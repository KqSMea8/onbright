package com.bright.apollo.cache;

import com.bright.apollo.redis.RedisBussines;
import com.zz.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CMDMsgCache {
    @Autowired
    private RedisBussines redisBussines;

    private static String key(String sid, String cmd) {
        return "reply_" + sid + "_" + cmd;
    }

    private static String key2(String sid, String addr) {
        return "reply_" + sid + "_" + addr;
    }

    public String getReply(String sid, String cmd) {
        String reply = redisBussines.get(key(sid, cmd));
//        String reply = RedisCache.getJedisCache().get(key(sid, cmd));
        if (!StringUtils.isEmpty(reply)) {
            redisBussines.delete(key(sid, cmd));
//            RedisCache.getJedisCache().del(key(sid, cmd));
        }
        return reply;
    }

    public void saveReply(String sid, String cmd, String data) {
        redisBussines.setValueWithExpire(key(sid, cmd),data,60);
//        RedisCache.getJedisCache().setex(key(sid, cmd), 60, data);
    }

    public void saveSearch(String sid, String addr, String data) {
        redisBussines.setValueWithExpire(key2(sid,addr),data,60);
//        RedisCache.getJedisCache().setex(key2(sid,addr), 60, data);
    }

    public String getSearch(String sid, String addr) {
        String reply = redisBussines.get(key2(sid,addr));
//        String reply = RedisCache.getJedisCache().get(key2(sid,addr));
        if (!StringUtils.isEmpty(reply)) {
            redisBussines.delete(key2(sid,addr));
//            RedisCache.getJedisCache().del(key2(sid,addr));
        }
        return reply;
    }
}
