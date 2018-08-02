package com.bright.apollo.session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bright.apollo.bean.PushExceptionMsg;
import com.bright.apollo.bean.PushSystemMsg;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.enums.ExceptionEnum;
import com.bright.apollo.enums.SystemEnum;
import com.bright.apollo.service.OboxDeviceConfigService;
import com.bright.apollo.service.SceneService;
import com.bright.apollo.service.UserService;
import com.zz.common.util.StringUtils;

@Component
public class PushExceptionPool {

    @Autowired
    private SceneService sceneService;

    @Autowired
    private UserService userService;

    @Autowired
    private OboxDeviceConfigService oboxDeviceConfigService;

    private static ExecutorService executor;

    private static final Logger logger = LoggerFactory.getLogger(PushExceptionPool.class);

    public PushExceptionPool() {

        executor = Executors
                .newFixedThreadPool(5);
    }

    public void handlerMsg(PushExceptionMsg msg, PushSystemMsg systemMsg) {

        executor.submit(new handlerMessage(msg,systemMsg));
    }

    class handlerMessage implements Runnable {

        private PushExceptionMsg msg;

        private PushSystemMsg systemMsg;
        public handlerMessage(PushExceptionMsg msg,PushSystemMsg systemMsg) {
            this.msg = msg;
            this.systemMsg=systemMsg;
        }

        @Override
        public void run() {
            try {
                Set<String> set = new HashSet<String>();
                if(systemMsg!=null){
                    logger.info("===systemMsg===:" + JSON.toJSON(systemMsg));
                    if(systemMsg.getType().intValue()==SystemEnum.system.getValue()&&
                            systemMsg.getChildType().intValue()==SystemEnum.scene.getValue()
                            ){
                        TScene tScene = sceneService.getSceneBySceneNumber(systemMsg.getId());
//                        TScene tScene = SceneBusiness.querySceneBySceneNumber(systemMsg
//                                .getId());
                        if (tScene == null) {
                            logger.warn("===PushException tScene not exist===");
                            return;
                        }
                        if (!addRoot(set, tScene.getLicense())) {
                            logger.warn("===PushException root not exist===");
                        }
                        List<TUser> tUserList = userService.getUsersBySceneNumber(tScene.getSceneNumber());
//                        List<TUser> tUserList = UserBusiness
//                                .queryUserBySceneNumber(tScene.getSceneNumber());
                        sendSysMsgByUserList(set, tScene.getSceneNumber(),
                                tScene.getSceneName(), null, tUserList,null);
                    }
                    return;
                }
                logger.info("===msg===:" + JSON.toJSON(msg));
                if (msg == null || msg.getId() == null) {
                    logger.warn("===PushException error data===");
                    return;
                }
                if (msg.getType().intValue() != ExceptionEnum.securityscene
                        .getValue()) {
                    TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.getOboxDeviceConfigById(msg.getId());
//                    TOboxDeviceConfig tOboxDeviceConfig = DeviceBusiness
//                            .queryDeviceConfigByID(msg.getId());
//                    TYSCamera tYSCamera = CameraBusiness.queryYSCameraById(msg
//                            .getId());
//                    if ((tOboxDeviceConfig == null || tOboxDeviceConfig
//                            .getLicense() == null)
//                            && (tYSCamera == null || tYSCamera.getLicense() == null)) {
//                        logger.warn("===PushException device not exist===");
//                        return;
//                    }
//                    if (tOboxDeviceConfig!=null&&!addRoot(set, tOboxDeviceConfig.getLicense())
//                            && StringUtils.isEmpty(msg.getUrl())) {
//                        logger.warn("===PushException root not exist===");
//                    } else if (tYSCamera!=null&&!addRoot(set, tYSCamera.getLicense())) {
//                        logger.warn("===PushException root not exist===");
//                    }

                    if (StringUtils.isEmpty(msg.getUrl())) {
                        // 更新状态
                        tOboxDeviceConfig.setDeviceState(msg.getState());
                        addOrUpdateDeviceState(
                                tOboxDeviceConfig.getDeviceState(),
                                tOboxDeviceConfig);
                        if (msg.getType().intValue() == ExceptionEnum.alldevice
                                .getValue()
                                && msg.getChildType().intValue() == ExceptionEnum.unconnection
                                .getValue()) {
                            //
                        } else {
                            oboxDeviceConfigService.updateTOboxDeviceConfig(tOboxDeviceConfig);
//                            OboxBusiness
//                                    .updateOboxDeviceConfig(tOboxDeviceConfig);
                        }
                        List<TUser> tUserList = userService.getUsersByDeviceId(tOboxDeviceConfig.getId());
//                        List<TUser> tUserList = UserBusiness
//                                .queryUserByDeviceId(tOboxDeviceConfig.getId());
                        sendMsgByUserList(set, tOboxDeviceConfig.getId(),
                                tOboxDeviceConfig.getDeviceId(),
                                tOboxDeviceConfig.getDeviceSerialId(),
                                tUserList,null);
                    }else{
//                        set.add(msg.getUserId().intValue()+"");
//                        sendMsgByUserList(set, tYSCamera.getId(),
//                                tYSCamera.getChannelName(),
//                                tYSCamera.getDeviceSerial(),
//                                null,msg.getUrl());
                    }
                } else {
                    // 安防
                    TScene tScene = sceneService.getSceneBySceneNumber(msg.getId());
//                    TScene tScene = SceneBusiness.querySceneBySceneNumber(msg
//                            .getId());
                    if (tScene == null) {
                        logger.warn("===PushException tScene not exist===");
                        return;
                    }
                    if (!addRoot(set, tScene.getLicense())) {
                        logger.warn("===PushException root not exist===");
                    }
                    List<TUser> tUserList = userService.getUsersBySceneNumber(tScene.getSceneNumber());
//                    List<TUser> tUserList = UserBusiness
//                            .queryUserBySceneNumber(tScene.getSceneNumber());
                    sendMsgByUserList(set, tScene.getSceneNumber(),
                            tScene.getSceneName(), null, tUserList,null);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        }

        private void addOrUpdateDeviceState(String deviceState,
                                            TOboxDeviceConfig tOboxDeviceConfig) throws Exception {
//            logger.info("===add device status===:"
//                    + tOboxDeviceConfig.getDeviceSerialId() + " deviceState:"
//                    + deviceState);
//            if(StringUtils.isEmpty(deviceState))
//                return;
//            TDeviceStatus tDeviceStatus = new TDeviceStatus();
//            tDeviceStatus.setDeviceSerialId(tOboxDeviceConfig
//                    .getDeviceSerialId());
//            tDeviceStatus.setDeviceState(deviceState);
//            DeviceBusiness.addDeviceStatus(tDeviceStatus);
        }
        private void sendSysMsgByUserList(Set<String> set, Integer relevancyId,
                                          String name, String serialId, List<TUser> tUserList,String url)
                throws Exception {
            if (tUserList == null || tUserList.size() == 0) {
                sendSysMessage(set, relevancyId, name, serialId,url);
                return;
            }
            for (TUser user : tUserList) {
                set.add(user.getId() + "");
            }
            sendSysMessage(set, relevancyId, name, serialId,url);
        }


        // 批量发送消息给用户
        private void sendMsgByUserList(Set<String> set, Integer relevancyId,
                                       String name, String serialId, List<TUser> tUserList,String url)
                throws Exception {
            if (tUserList == null || tUserList.size() == 0) {
                sendMessage(set, relevancyId, name, serialId,url);
                return;
            }
            for (TUser user : tUserList) {
                set.add(user.getId() + "");
            }
            sendMessage(set, relevancyId, name, serialId,url);
        }

        // 添加root 到set
        private boolean addRoot(Set<String> set, Integer license)
                throws Exception {
//            TKey tKey = OboxBusiness.queryKeyByID(license);
//            if (tKey == null || StringUtils.isEmpty(tKey.getClient())) {
//                return false;
//            }
//            TUser tUser = UserBusiness.queryUserByUserName(tKey.getClient());
//            if (tUser == null) {
//                return false;
//            }
//            set.add(tUser.getUserId().intValue() + "");
            return true;
        }
        private void sendSysMessage(Set<String> set, Integer relevancyId,
                                    String name, String serialId, String url) throws Exception {
//            Iterator<String> iterator = set.iterator();
//            TSystem tSystem=new TSystem(systemMsg.getType(),
//                    systemMsg.getChildType(),  relevancyId,name);
//            logger.info("===tSystem===:" + JSON.toJSONString(tSystem));
//            int systemId = MsgBussiness.addSystem(tSystem);
//            long sendTime = new Date().getTime();
//            //	String time = DateHelper.formatDate(sendTime, DateHelper.FORMATALL);
//            while (iterator.hasNext()) {
//                String userId = iterator.next();
//                TUser tUser = UserBusiness.queryUserById(Integer
//                        .parseInt(userId));
//                if (StringUtils.isEmpty(systemMsg.getContent())) {
//                    logger.warn("===PushException couldn't build content===");
//                    return;
//                }
//                TMsg tMsg = new TMsg(MsgEnum.system.getValue(),
//                        Integer.parseInt(userId), systemMsg.getContent(), systemId,
//                        sendTime,url);
//                MsgBussiness.addMsg(tMsg);
//                JPushService.sendAlter(systemMsg.getContent(), tUser.getUserName(), null);
//            }
        }
        // 发送消息
        private void sendMessage(Set<String> set, Integer relevancyId,
                                 String name, String serialId,String url) throws Exception {
//            Iterator<String> iterator = set.iterator();
//            TException tException = new TException(msg.getType(),
//                    msg.getChildType(), msg.getState(), relevancyId, serialId,
//                    name);
//
//            int exceptionId = ExceptionBussiness.addException(tException);
//            long sendTime = new Date().getTime();
//            logger.info("===tException===:" +sendTime+"====:" +JSON.toJSONString(tException));
//            String time = DateHelper.formatDate(sendTime, DateHelper.FORMATALL);
//            while (iterator.hasNext()) {
//                String userId = iterator.next();
//                TUser tUser = UserBusiness.queryUserById(Integer
//                        .parseInt(userId));
//                String content = buildContent(msg, tUser.getUserName(), time,
//                        name);
//                if (StringUtils.isEmpty(content)) {
//                    logger.warn("===PushException couldn't build content===");
//                    return;
//                }
//                TMsg tMsg = new TMsg(MsgEnum.exception.getValue(),
//                        Integer.parseInt(userId), content, exceptionId,
//                        sendTime,url);
//                MsgBussiness.addMsg(tMsg);
//                JPushService.sendAlter(content, tUser.getUserName(), null);
//            }
        }

        // 生成发送内容
        private String buildContent(PushExceptionMsg msg, String userName,
                                    String time, String name) {
            StringBuilder sb = new StringBuilder("尊敬的").append(userName)
                    .append("用户，您的").append(name);
            if (msg.getType().intValue() != ExceptionEnum.securityscene
                    .getValue()) {
                sb.append("设备，在").append(time);
            } else {
                sb.append("安守场景，在").append(time);
            }
            switch (msg.getType().intValue()) {
                case 0:// ExceptionEnum.socket.getValue():
                    if (msg.getChildType().intValue() == ExceptionEnum.overcurrent
                            .getValue()) {
                        sb.append(ExceptionEnum.overcurrent.getContent());
                    } else if (msg.getChildType().intValue() == ExceptionEnum.overcapacity
                            .getValue()) {
                        sb.append(ExceptionEnum.overcapacity.getContent());
                    } else if (msg.getChildType().intValue() == ExceptionEnum.undervoltage
                            .getValue()) {
                        sb.append(ExceptionEnum.undervoltage.getContent());
                    } else {
                        return null;
                    }
                    break;
                case 1:// ExceptionEnum.environmentalsensor.getValue():
                    // TODO
                    return null;
                case 2:// ExceptionEnum.doorlock.getValue():
                    if (msg.getChildType().intValue() == ExceptionEnum.lowbattery
                            .getValue()) {
                        sb.append(ExceptionEnum.lowbattery.getContent());
                    } else if (msg.getChildType().intValue() == ExceptionEnum.doohickey
                            .getValue()) {
                        sb.append(ExceptionEnum.doohickey.getContent());
                    } else {
                        return null;
                    }
                    break;
                case 3:// ExceptionEnum.alldevice.getValue():
                    if (msg.getChildType().intValue() == ExceptionEnum.unconnection
                            .getValue()) {
                        sb.append(ExceptionEnum.unconnection.getContent());
                    } else {
                        sb.append(ExceptionEnum.pic.getContent());
                    }
                    break;
                case 4:// ExceptionEnum.securityscene.getValue():
                    if (msg.getChildType().intValue() == ExceptionEnum.trigger
                            .getValue()) {
                        sb.append(ExceptionEnum.trigger.getContent());
                    } else if (msg.getChildType().intValue() == ExceptionEnum.detectionopen
                            .getValue()) {
                        sb.append(ExceptionEnum.detectionopen.getContent());
                    } else {
                        return null;
                    }
                    break;
                default:
                    break;
            }
            return sb.toString();
        }
    }
}
