package com.bright.apollo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zz.common.util.StringUtils;

import cn.jpush.api.JPushClient;
//import cn.jpush.api.common.APIConnectionException;
//import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class JPushService {
	private static final Logger logger = LoggerFactory.getLogger(JPushService.class);

    private static final String appKey ="LTAIBE0b86xFi9q5";
    private static final String masterSecret = "Ym9F1CNAgwhbxt5Sk1Qki1nr6w6e3v";


    public static final String TITLE = "Test from API example";
    public static final String ALERT = "Test from API Example - alert";
    public static final String MSG_CONTENT = "Test from API Example - msgContent";
    public static final String REGISTRATION_ID = "0900e8d85ef";
    public static final String TAG = "tag_api";


    public static void sendAlter(String content, String tag,String url) {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);

        // For push, all you need do is to build PushPayload object.
        PushPayload payload = buildPushObject_all_all_alert(content, tag,url);

        try {
            logger.info("jpush content==>>> " + content);
            PushResult result = jpushClient.sendPush(payload);
            logger.info("Got result - " + result);


        } catch (Exception e) {
            logger.error("Connection error. Should retry later. ", e);

        }
    }

    public static void sendAlterToAll(String content) {
        // HttpProxy proxy = new HttpProxy("localhost", 3128);
        // Can use this https proxy: https://github.com/Exa-Networks/exaproxy
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 1);

        // For push, all you need do is to build PushPayload object.
        PushPayload payload = buildPushObject_all_all_alert(content);

        try {
            logger.info("jpush content==>>> " + content);
            PushResult result = jpushClient.sendPush(payload);
            logger.info("Got result - " + result);


        } catch (Exception e) {
            logger.error("Connection error. Should retry later. ", e);

        }
    }

    public static void sendMessage(String content, String tag) {
        // HttpProxy proxy = new HttpProxy("localhost", 3128);
        // Can use this https proxy: https://github.com/Exa-Networks/exaproxy
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 1);

        // For push, all you need do is to build PushPayload object.
//        PushPayload payload = buildPushObject_all_all_alert();
        PushPayload payload = buildPushObjectMessageWithExtras(content, tag);

        try {
            logger.info("jpush content==>>> " + content);
            PushResult result = jpushClient.sendPush(payload);
            logger.info("Got result - " + result);

        } catch (Exception e) {
            logger.error("Connection error. Should retry later. ", e);

        }
    }

    public static PushPayload buildPushObject_all_all_alert(String alert, String tag,String url) {
        if(StringUtils.isEmpty(url)){
            return PushPayload.newBuilder()
                    .setPlatform(Platform.all())
                    .setAudience(Audience.newBuilder()
                            .addAudienceTarget(AudienceTarget.tag(tag))
                            .build())
                    .setNotification(Notification.newBuilder()
                            .setAlert(alert)
                            .addPlatformNotification(AndroidNotification.newBuilder()
                                    .build())
                            .addPlatformNotification(IosNotification.newBuilder()
                                    .build())
                            .build())
                    .build();
        }
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag(tag))
                        .build())
                .setNotification(Notification.newBuilder()
                        .setAlert(alert)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .addExtra("url", url)
                                .build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .addExtra("url", url)
                                .build())
                        .build())
                .build();
//	    return PushPayload.alertAll(alter);
    }

    public static PushPayload buildPushObjectMessageWithExtras(String content, String tag) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag(tag))
//                        .addAudienceTarget(AudienceTarget.alias("alias1", "alias2"))
                        .build())
                .setMessage(Message.newBuilder()
                        //.setMessageContent(content)
                        .setMsgContent(content)
                		.addExtra("from", "apollo-jpush")
                        .build())
                .build();
    }

    public static PushPayload buildPushObject_all_all_alert(String content) {
        return PushPayload.alertAll(content);
    }
}
