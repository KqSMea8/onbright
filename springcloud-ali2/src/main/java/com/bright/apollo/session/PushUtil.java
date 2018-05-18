package com.bright.apollo.session;

import com.bright.apollo.bean.PushMessage;
import com.bright.apollo.bean.SceneDTO;
import com.bright.apollo.cache.AccessTokenTool;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.enums.PushMessageType;
import com.bright.apollo.enums.SceneTypeEnum;
import com.bright.apollo.service.SceneService;
import com.bright.apollo.tool.NumberHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zz.common.util.ObjectUtils;
import com.zz.common.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PushUtil {
    private static final Logger log = Logger.getLogger(PushUtil.class);
    private static Set<CMDEnum> cmdSet = new HashSet<CMDEnum>();

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private SceneService sceneService;

    @Autowired
    private AccessTokenTool accessTokenTool;

    static {
        // 放行接口cmd

        cmdSet.add(CMDEnum.modify_user);
        cmdSet.add(CMDEnum.add_obox);
        cmdSet.add(CMDEnum.delete_obox);
        cmdSet.add(CMDEnum.set_group);
        cmdSet.add(CMDEnum.setting_sc_info);
        cmdSet.add(CMDEnum.execute_sc);
        cmdSet.add(CMDEnum.create_location);
        cmdSet.add(CMDEnum.set_device_location);
        cmdSet.add(CMDEnum.set_scene_location);
        cmdSet.add(CMDEnum.add_fingerprint);

    }

    // 拦截不放送命令 以及发送数据变化的设备 给相关设备
    public void filterForwordSeverChange(RequestParam requestParam,
                                                CMDEnum cmdEnum, String accessToken, JsonObject respJsonObject)
            throws Exception {
        if (cmdSet.contains(cmdEnum) && respJsonObject != null) {
            JsonElement jsonElement = respJsonObject.get("success");
            if (jsonElement == null)
                return;
            boolean success = jsonElement.getAsBoolean();
            if (!success)
                return;
            // g2.toJson(jsonElement) is error
            Gson g2 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                    .create();
            JsonObject jsonObject = new JsonObject();
            Map<String, String[]> params = requestParam.getParams();
            Set<String> keySet = params.keySet();
            Iterator<String> iterator = keySet.iterator();
            String appkey = null;
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (!key.equals("access_token") && !key.equals("CMD")
                        && !key.equals("appkey"))
                    jsonObject.add(key, g2.toJsonTree(params.get(key)));
                if (key.equals("appkey"))
                    appkey = params.get(key)[0];
            }
            String request = jsonObject.toString();
            if (cmdEnum.equals(CMDEnum.set_group)) {
                if (!StringUtils.isEmpty(requestParam.getValue("group_style"))
                        && requestParam.getValue("group_style").equals("00")) {
                    return;
                }
            } else if (cmdEnum.equals(CMDEnum.execute_sc)) {
                String sceneNumber = requestParam.getValue("scene_number");
                try {
                    if (respJsonObject.has("obox_scene_number")) {
                        JsonElement jsonElement2 = respJsonObject
                                .get("obox_scene_number");
                        if (jsonElement2 != null
                                && !jsonElement2.isJsonNull()
                                && NumberHelper.isNumeric(jsonElement2
                                .getAsString()))
                            return;
                    }
                    TScene tScene = sceneService.getSceneBySceneNumber(Integer
                            .parseInt(sceneNumber));
//                    TScene tScene = SceneBusiness
//                            .querySceneBySceneNumber(Integer
//                                    .parseInt(sceneNumber));

                    if (tScene != null
                            && !tScene.getSceneType().equals(
                            SceneTypeEnum.server.getValue())) {
                        return;
                    }
                } catch (Exception e) {
//                    Log.error("===error param sceneNumber " + cmdEnum);
                    return;
                }

            } else if (cmdEnum.equals(CMDEnum.setting_sc_info)) {
                String sceneString = requestParam.getValue("scene");
                if (StringUtils.isEmpty(sceneString)) {
                    return;
                }
                SceneDTO sceneDTO = (SceneDTO) ObjectUtils.fromJsonToObject(
                        sceneString, SceneDTO.class);
                if (sceneDTO == null) {
                    return;
                }
                String sceneType = sceneDTO.getSceneType();
                if (!StringUtils.isEmpty(sceneType)
                        && !sceneType.equals(SceneTypeEnum.server.getValue())) {
                    return;
                }
            }
            log.info("===after requestParam remove :" + request);
            ForwordSeverChange(accessToken, respJsonObject, request, cmdEnum,
                    appkey);
        }
    }

    public void ForwordSeverChange(String accessToken,
                                          JsonObject respJsonObject, String request, CMDEnum cmdEnum,
                                          String appkey) throws Exception {
        String userId = accessTokenTool.getUserId(accessToken);
        String sessionKey = sessionManager.sessionKey(userId, appkey);
        ClientSession clientSessionBySessionKey = sessionManager
                .getClientSessionBySessionKey(sessionKey);
//        List<TUser> userIds = UserBusiness
//                .queryListByAdminGroupAndRootGroup(userId);
//        if (userIds != null && userIds.size() > 0) {
//            PushMessage message = new PushMessage();
//            message.setType(PushMessageType.SERVER_CHANGE.getValue());
//            message.setData(respJsonObject.toString());
//            message.setRequest(request);
//            message.setCmd(cmdEnum.toString());
//
//            PushObserverManager pushObserverManager = PushObserverManager
//                    .getInstance();
//            for (int i = 0; i < userIds.size(); i++) {
//                Set<ClientSession> setClientSession = sessionManager
//                        .getSetClientSessionByUid(userIds.get(i).getId()
//                                + "");
//                if (setClientSession != null && !setClientSession.isEmpty()
//                        && setClientSession.size() > 0) {
//                    Iterator<ClientSession> iterator = setClientSession
//                            .iterator();
//                    while (iterator.hasNext()) {
//                        ClientSession next = iterator.next();
//                        if (clientSessionBySessionKey != null
//                                && next != null
//                                && clientSessionBySessionKey.getChannel() != null
//                                && next.getChannel() != null
//                                && clientSessionBySessionKey.getChannel() == next
//                                .getChannel())
//                            continue;
//                        if (next != null
//                                && next.getStatus() == Session.STATUS_CONNECTED)
//                            pushObserverManager.sendMessage(message, next);
//                    }
//                }
//            }
//        }

    }
}
