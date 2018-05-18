package com.bright.apollo.cache;

import com.bright.apollo.enums.DeviceTypeEnum;
import com.bright.apollo.redis.RedisBussines;
import com.zz.common.util.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenTool {
    private static int defalut_time = 60 * 60 * 24 * 7;

    private static int short_time = 30;

    private static int long_time = 60 * 60 * 24 * 30;

    @Autowired
    private RedisBussines redisBussines;

    public String getUserId(String accessToken) {
        return redisBussines.get(accessToken);
    }

    public String getAccessToken(String uid) {
        return redisBussines.get(uid);
    }

    public String getWeight(String uid) {
        return redisBussines.get(uid+"_weight");
    }

    public String create(int userId,int weight) {
        String uuid = RandomUtils.genUUID();
//        RedisCache.getJedisCache().setex(uuid, defalut_time, String.valueOf(userId));
//        RedisCache.getJedisCache().setex(String.valueOf(userId), defalut_time, uuid);
//        RedisCache.getJedisCache().setex(String.valueOf(userId)+"_weight", defalut_time, "0" + String.valueOf(weight));
        redisBussines.setValueWithExpire(uuid,String.valueOf(userId),defalut_time);
        redisBussines.setValueWithExpire(String.valueOf(userId),uuid,defalut_time);
        redisBussines.setValueWithExpire(String.valueOf(userId)+"_weight","0" + String.valueOf(weight),defalut_time);
        return uuid;
    }

    public void createEnvironmentalSensor(String serialId,String state){
//        RedisCache.getJedisCache().setex(serialId+"_DeviceChildType_"+DeviceTypeEnum.sensor_environment.getValue(), 1, state);
        redisBussines.setValueWithExpire(serialId+"_DeviceChildType_"+DeviceTypeEnum.sensor_environment.getValue(),state,1);
    }
    public String getEnvironmentalSensor(String serialId){
//        return RedisCache.getJedisCache().get(serialId+"_DeviceChildType_"+DeviceTypeEnum.sensor_environment.getValue());
        return redisBussines.get(serialId+"_DeviceChildType_"+DeviceTypeEnum.sensor_environment.getValue());

    }
    public void createYSToken(int userId,String access_token){
//        RedisCache.getJedisCache().setex(String.valueOf(userId)+"_ys", defalut_time, access_token);
        redisBussines.setValueWithExpire(String.valueOf(userId)+"_ys",access_token,defalut_time);
    }
    //dingdon
//    @Deprecated
//    public static void createDDToken(int userId,String ddUserID){
//        RedisCache.getJedisCache().setex(String.valueOf(userId)+"_dd", defalut_time, ddUserID);
//    }
//    @Deprecated
//    public static String getDDAccessToken(int userId){
//        return RedisCache.getJedisCache().get(String.valueOf(userId)+"_dd");
//    }
    public void createTokenByDDUserID(int userId,String ddUserID){
//        RedisCache.getJedisCache().setex(String.valueOf(userId)+"_dd", defalut_time, ddUserID);
//        RedisCache.getJedisCache().setex(ddUserID, defalut_time, userId+"");
        redisBussines.setValueWithExpire(String.valueOf(userId)+"_dd",ddUserID,defalut_time);
        redisBussines.setValueWithExpire(ddUserID,userId+"",defalut_time);
    }
    public String getUserAccessTokenByDDUserID(String ddUserID){
//        return RedisCache.getJedisCache().get(ddUserID);
        return redisBussines.get(ddUserID);
    }
    public String getYSAccessToken(int userId){
//        return RedisCache.getJedisCache().get(String.valueOf(userId)+"_ys");
        return redisBussines.get(String.valueOf(userId)+"_ys");
    }

    public void createFingerprint(String serialId,String pin,int scene_number) {
//        RedisCache.getJedisCache().setex(String.valueOf(scene_number)+"_"+serialId+"_"+pin, short_time, serialId+"_"+pin);
        redisBussines.setValueWithExpire(String.valueOf(scene_number)+"_"+serialId+"_"+pin,serialId+"_"+pin,short_time);
    }

    public String getFingerprint(int scene_number,String serialId,String pin) {
//        return RedisCache.getJedisCache().get(String.valueOf(scene_number)+"_"+serialId+"_"+pin);
        return redisBussines.get(String.valueOf(scene_number)+"_"+serialId+"_"+pin);
    }
    public void createYaoKonYun(String appid,String deviceId,int useTime) {
//        RedisCache.getJedisCache().setex("createYaoKonYun",long_time,appid+":"+deviceId);
//        RedisCache.getJedisCache().setex(appid+":"+deviceId,long_time,useTime+"");
        redisBussines.setValueWithExpire("createYaoKonYun",appid+":"+deviceId,long_time);
        redisBussines.setValueWithExpire(appid+":"+deviceId,useTime+"",long_time);
    }
    public String getYaoKonYunKey(){
//        return RedisCache.getJedisCache().get("createYaoKonYun");
        return redisBussines.get("createYaoKonYun");
    }
    public String getYaoKonYunUserTime(String key){
//        return RedisCache.getJedisCache().get(key);
        return redisBussines.get(key);
    }
    public void addYaoKonYunUserTime(String key,int useTime){
//        RedisCache.getJedisCache().setex(key,long_time,useTime+"");
        redisBussines.setValueWithExpire(key,useTime+"",long_time);
    }
}
