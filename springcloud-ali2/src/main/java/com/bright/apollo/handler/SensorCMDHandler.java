package com.bright.apollo.handler;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.*;
import com.bright.apollo.enums.DeviceTypeEnum;
import com.bright.apollo.enums.NodeTypeEnum;
import com.bright.apollo.enums.SceneTypeEnum;
import com.bright.apollo.service.*;
import com.bright.apollo.session.ClientSession;
import com.bright.apollo.session.SceneActionThreadPool;
import com.bright.apollo.session.SessionManager;
import com.bright.apollo.tool.ByteHelper;
import com.zz.common.util.DateTime;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class SensorCMDHandler extends BasicHandler {

    private final Logger log = Logger.getLogger(SensorCMDHandler.class);

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private SceneActionThreadPool sceneActionThreadPool;

    @Autowired
    private OboxService oboxService;

    @Autowired
    private OboxDeviceConfigService oboxDeviceConfigService;

    @Autowired
    private SceneService sceneService;

    @Autowired
    private SceneActionService sceneActionService;

    @Autowired
    private SceneConditionService sceneConditionService;

    @Autowired
    private UserSceneService userSceneService;

    @Autowired
    private UserService userService;

    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        String data = msg.getData();
        String oboxSerialId = data.substring(0, 10);
        String addr = data.substring(12, 14);
        String uploadTag = data.substring(14, 16);
        String state = data.substring(16, 32);
        int isEnvironment = 0;// 0 not Environment，1state is 0，2state
        log.info("==============msg================:" + msg.toString());
        TObox tObox = oboxService.queryOboxBySerialId(oboxSerialId);
//        TObox tObox = OboxBusiness.queryOboxsByOboxSerialId(oboxSerialId);

        byte[] body = new byte[16];
        byte[] oboxSerialBytes = ByteHelper.hexStringToBytes(oboxSerialId);
        System.arraycopy(oboxSerialBytes, 0, body, 0, oboxSerialBytes.length);
        body[5] = 0x00;
        body[6] = (byte) Integer.parseInt(addr, 16);
        body[7] = (byte) Integer.parseInt(uploadTag, 16);

        byte[] stateBytes = ByteHelper.hexStringToBytes(state);
        System.arraycopy(stateBytes, 0, body, 8, stateBytes.length);



//        if (tObox == null) {
//            TRemoter tRemoter = DeviceBusiness.queryRemoter(oboxSerialId);
//            if (tRemoter == null) {
//                log.error(String.format("not found %s device!", oboxSerialId));
//                return null;
//            }
//            List<TSceneCondition> tSceneConditions = SceneBusiness
//                    .querySceneConditionsBySerialId(oboxSerialId);
//            if (tSceneConditions != null) {
//                for (TSceneCondition tSceneCondition : tSceneConditions) {
//                    if (tSceneCondition.getCondition().equals(
//                            tRemoter.getRemoter() + "0000" + uploadTag)) {
//                        TScene tScene = SceneBusiness
//                                .querySceneBySceneNumber(tSceneCondition
//                                        .getSceneNumber());
//                        if (tScene != null) {
//                            if (tScene.getSceneRun() == 0
//                                    && tScene.getSceneStatus() != 0) {
//                                List<TSceneAction> tSceneActions = SceneBusiness
//                                        .querySceneActionsBySceneNumber(tScene
//                                                .getSceneNumber());
//                                if (tSceneActions != null
//                                        && !tSceneActions.isEmpty()) {
//                                    tScene.setSceneRun(1);
//                                    SceneBusiness.updateScene(tScene);
//                                    // take action
//                                    sceneActionThreadPool.addSceneAction(tScene
//                                            .getSceneNumber());
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } else {
            TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.queryOboxConfigByRFAddr(tObox.getOboxId(),addr);
//            TOboxDeviceConfig tOboxDeviceConfig = OboxBusiness
//                    .queryOboxConfigByAddr(tObox.getOboxId(), addr);
            if (tOboxDeviceConfig == null) {
                if (addr.equals("ff") && data.substring(10, 12).equals("00")) {
                    if (uploadTag.equals("01")) {

                        int oboxControl = Integer.parseInt(
                                data.substring(16, 18), 16) & 0x04;
                        String status = data.substring(18,
                                18 + 2 * (msg.getLength() - 9));
                        for (int i = 1; i < 5; i++) {
                            TOboxDeviceConfig config = oboxDeviceConfigService.queryOboxConfigByRFAddr(tObox.getOboxId(),ByteHelper.int2HexString(i));
//                            TOboxDeviceConfig config = OboxBusiness
//                                    .queryOboxConfigByAddr(tObox.getOboxId(),
//                                            ByteHelper.int2HexString(i));
                            if (config != null) {
                                if (status.substring((i - 1) * 2, i * 2)
                                        .equals("00")) {
                                    if (!config.getDeviceState()
                                            .substring(0, 2).equals("00")) {
                                        config.setDeviceState("00000000000000");
                                        oboxDeviceConfigService.updateTOboxDeviceConfig(config);
//                                        OboxBusiness
//                                                .updateOboxDeviceConfig(config);
                                    }
                                } else {
                                    if (!config
                                            .getDeviceState()
                                            .substring(0, 2)
                                            .equals(status.substring(
                                                    (i - 1) * 2, i * 2))) {
                                        config.setDeviceState(status.substring(
                                                (i - 1) * 2, i * 2)
                                                + "000000000000");
                                        oboxDeviceConfigService.updateTOboxDeviceConfig(config);
//                                        OboxBusiness
//                                                .updateOboxDeviceConfig(config);
                                    }
                                }
                            }
                        }
//                        if (oboxControl == 0) {
//                            tObox.setOboxControl(0);
//                        } else {
//                            tObox.setOboxControl(1);
//                        }
//
//                        OboxBusiness.updateObox(tObox);
                    }
                } else if (addr.equals("00")
                        && data.substring(10, 12).equals("00")) {
                    if (uploadTag.equals("02")) {
                        // time scene take action
                        TScene tScene = sceneService.getTSceneByOboxSerialIdAndSceneNumber(tObox.getOboxSerialId(),Integer.parseInt(data.substring(16, 18), 16));
//                        TScene tScene = OboxBusiness.querySceneBySNumber(
//                                tObox.getOboxSerialId(),
//                                Integer.parseInt(data.substring(16, 18), 16));
                        if (tScene != null) {
                            List<TSceneAction> sceneActions = sceneActionService.getSceneActionBySceneNumber(tScene.getSceneNumber());
//                            List<TSceneAction> sceneActions = SceneBusiness
//                                    .querySceneActionsBySceneNumber(tScene
//                                            .getSceneNumber());
                            for (TSceneAction sceneAction : sceneActions) {
                                if (NodeTypeEnum.single.getValue().equals(
                                        sceneAction.getNodeType())) {
                                    TOboxDeviceConfig config = oboxDeviceConfigService.getOboxDeviceConfigById(sceneAction.getId());
//                                    TOboxDeviceConfig config = DeviceBusiness
//                                            .queryDeviceConfigByID(sceneAction
//                                                    .getActionID());
                                    if (config != null) {
                                        config.setDeviceState(sceneAction
                                                .getAction());
                                        oboxDeviceConfigService.updateTOboxDeviceConfig(config);
//                                        OboxBusiness
//                                                .updateOboxDeviceConfig(config);
                                    }
                                }
                            }
                            // 处理消息
                            if (tScene.getMsgAlter() != 0
                                    && tScene.getAlterNeed() == 1) {

                                sendMsg(tScene);
                            }
                        }
                    }
                }
                return null;
            }
            log.info(String.format("%s alarm %s state",
                    tOboxDeviceConfig.getDeviceSerialId(), state));
            // ff68ff14000000
//            if (tOboxDeviceConfig.getDeviceType().equals(
//                    DeviceTypeEnum.sensor.getValue())) {
//                if (tOboxDeviceConfig.getDeviceChildType().equals("09")) {
//                    if (state.substring(0, 8).equals("ff000000")) {
//                        // power off
//                        TOboxStatus tOboxStatus = new TOboxStatus();
//                        tOboxStatus.setOboxId(tObox.getOboxId());
//                        tOboxStatus.setOboxStatus(0);
//                        tOboxStatus.setOboxPower(0);
//                        tOboxStatus.setOboxIP(tObox.getOboxIP());
//                        OboxBusiness.addOboxStatus(tOboxStatus);
//                        tObox.setOboxStatus(0);
//                        tObox.setOboxIP("0.0.0.0");
//                        OboxBusiness.updateObox(tObox);
//                        ClientSession session = sessionManager
//                                .getClientSession(tObox.getOboxSerialId());
//                        if (session != null) {
//                            session.close(Session.CloseEnum.reader_writer_idle);
//                        }
//                        tOboxDeviceConfig.setDeviceState(state);
//                        OboxBusiness.updateOboxDeviceConfig(tOboxDeviceConfig);
//                    }
//                } else if (tOboxDeviceConfig.getDeviceChildType().equals(
//                        DeviceTypeEnum.sensor_environment.getValue())) {
//                    // 6合1
//                    if (state.substring(0, 1).equals("0")) {
//                        /*
//                         * AccessTokenTool.createEnvironmentalSensor(
//                         * tOboxDeviceConfig.getDeviceId(), state.substring(0,
//                         * state.length()-2));
//                         */
//                        isEnvironment = 1;
//                        if (tOboxDeviceConfig.getDeviceState().length() > 12) {
//                            tOboxDeviceConfig.setDeviceState(state.substring(0,
//                                    state.length() - 2)
//                                    + tOboxDeviceConfig.getDeviceState()
//                                    .substring(12));
//                        } else {
//                            tOboxDeviceConfig.setDeviceState(state.substring(0,
//                                    state.length() - 2));
//                        }
//                    } else {
//                        /*
//                         * tOboxDeviceConfig.setDeviceState(AccessTokenTool
//                         * .getEnvironmentalSensor(tOboxDeviceConfig
//                         * .getDeviceId()) + state.substring(0,
//                         * state.length()-2));
//                         */
//                        if (tOboxDeviceConfig.getDeviceState().length() > 12) {
//                            tOboxDeviceConfig.setDeviceState(tOboxDeviceConfig
//                                    .getDeviceState().substring(0, 12)
//                                    + state.substring(0, state.length() - 2));
//                        } else {
//                            tOboxDeviceConfig.setDeviceState(tOboxDeviceConfig
//                                    .getDeviceState()
//                                    + state.substring(0, state.length() - 2));
//                        }
//                        isEnvironment = 2;
//                        addOrUpdateDeviceState(
//                                tOboxDeviceConfig.getDeviceState(),
//                                tOboxDeviceConfig);
//                    }
//                    OboxBusiness.updateOboxDeviceConfig(tOboxDeviceConfig);
//                } else if (tOboxDeviceConfig.getDeviceChildType().equals("02")) {// 水浸
//                    String substring = state.substring(2, 4);
//                    if (!substring.equals("00")) {
//                        PushExceptionMsg exceptionMsg = new PushExceptionMsg(
//                                ExceptionEnum.sensor.getValue(),
//                                ExceptionEnum.waterleaching.getValue(),
//                                tOboxDeviceConfig.getId(), state);
//                        PushObserverManager.getInstance().sendMessage(
//                                exceptionMsg,null);
//
//                    }else{
//                        tOboxDeviceConfig.setDeviceState(state);
//                        OboxBusiness.updateOboxDeviceConfig(tOboxDeviceConfig);
//                        addOrUpdateDeviceState(state, tOboxDeviceConfig);
//
//                    }
//                }/*
//                 * else
//                 * if(tOboxDeviceConfig.getDeviceChildType().equals("15")){//门磁
//                 *
//                 * }
//                 */
//                else {
//                    tOboxDeviceConfig.setDeviceState(state);
//                    OboxBusiness.updateOboxDeviceConfig(tOboxDeviceConfig);
//                    addOrUpdateDeviceState(state, tOboxDeviceConfig);
//                }
//            } else
                if (tOboxDeviceConfig.getDeviceType().equals(
                    DeviceTypeEnum.socket.getValue())) {

                tOboxDeviceConfig.setDeviceState(state.substring(2, 4)
                        + state.substring(0, 2)
                        + state.substring(4, state.length()));
                if (tOboxDeviceConfig.getDeviceChildType().equals(
                        DeviceTypeEnum.socket_scene.getValue())) {
                    state = tOboxDeviceConfig.getDeviceState();
                    tOboxDeviceConfig.setDeviceState(state.substring(0, 2)
                            + state.substring(6, 8) + state.substring(2, 4)
                            + state.substring(8, state.length()) + "00");
                }
                oboxDeviceConfigService.updateTOboxDeviceConfig(tOboxDeviceConfig);
//                OboxBusiness.updateOboxDeviceConfig(tOboxDeviceConfig);
                oboxDeviceConfigService.updateTOboxDeviceConfigStatus(tOboxDeviceConfig,state);
//                addOrUpdateDeviceState(state, tOboxDeviceConfig);
            } else {
                tOboxDeviceConfig.setDeviceState(state);
                oboxDeviceConfigService.updateTOboxDeviceConfig(tOboxDeviceConfig);
//                OboxBusiness.updateOboxDeviceConfig(tOboxDeviceConfig);
                oboxDeviceConfigService.updateTOboxDeviceConfigStatus(tOboxDeviceConfig,state);
//                addOrUpdateDeviceState(state, tOboxDeviceConfig);
            }

            // 门锁操作
//            boolean isDoor = false;
//
//            String doorPin = null;
//            if (tOboxDeviceConfig.getDeviceType().equals(
//                    DeviceTypeEnum.doorlock.getValue())
//                    && tOboxDeviceConfig.getDeviceChildType().equals(
//                    DeviceTypeEnum.doorlock_child.getValue())) {
//                log.info("=====state:" + state + "======");
//                String opernation = state.substring(2, 4);
//                String betty = state.substring(0, 2);// 电量
//                String type = state.substring(4, 6);
//                if (opernation.equals("c3")) {// 开锁
//                    // 开锁类型 0指纹，1密码，2卡，3钥匙，4遥控
//                    // 门禁用户ID
//                    doorPin = state.substring(8, 10);
//                    isDoor = true;
//                    addFignerLog(type, betty, doorPin,
//                            DoorOperationEnum.open.getValue(), -1,
//                            tOboxDeviceConfig);
//                } else {
//                    String weight = state.substring(6, 8);
//                    String pin = state.substring(10, 12);
//                    // c0640101000100
//                    if (opernation.equals("c0")) {
//                        // 增加用户
//                        TUSerDoorlock tUSerdoorlock = new TUSerDoorlock();
//                        tUSerdoorlock.setPin(pin);
//                        tUSerdoorlock.setSerialId(tOboxDeviceConfig
//                                .getDeviceSerialId());
//                        tUSerdoorlock.setType(type);
//                        tUSerdoorlock.setWeight(weight);
//                        tUSerdoorlock.setUserId(00);
//                        if (DoorlockBusiness.queryUserDoorlock(
//                                tOboxDeviceConfig.getDeviceSerialId(), pin,
//                                type) == null) {
//                            DoorlockBusiness.addUserDoorlock(tUSerdoorlock);
//                            addFignerLog(type, betty, pin,
//                                    DoorOperationEnum.add.getValue(), 0,
//                                    tOboxDeviceConfig);
//                        }
//                    } else if (opernation.equals("c1")) {// 删除用户
//                        Integer userId = 0;
//                        TUSerDoorlock tUSerDoorlock = DoorlockBusiness
//                                .queryUserDoorlock(
//                                        tOboxDeviceConfig.getDeviceSerialId(),
//                                        pin);
//                        if (tUSerDoorlock != null) {
//                            DoorlockBusiness.delUserDoorlock(
//                                    tOboxDeviceConfig.getDeviceSerialId(), pin);
//                            userId = tUSerDoorlock.getUserId();
//                        }
//
//                        addFignerLog(type, betty, pin,
//                                DoorOperationEnum.del.getValue(), userId,
//                                tOboxDeviceConfig);
//                    } else if (opernation.equals("c5")) {// 报警
//                        PushExceptionMsg exceptionMsg = new PushExceptionMsg();
//                        exceptionMsg.setId(tOboxDeviceConfig.getId());
//                        exceptionMsg.setType(ExceptionEnum.doorlock.getValue());
//                        exceptionMsg.setState(state);
//                        if (type.equals("07")) {
//                            exceptionMsg.setChildType(ExceptionEnum.lowbattery
//                                    .getValue());
//                        } else {
//                            exceptionMsg.setChildType(ExceptionEnum.doohickey
//                                    .getValue());
//                        }
//                        PushObserverManager.getInstance().sendMessage(
//                                exceptionMsg,null);
//                    }
//                    return null;
//                }
//
//            } else
//                if (tOboxDeviceConfig.getDeviceType().equals(
//                    DeviceTypeEnum.socket.getValue())) {
//                // 记录插座异常 //TODO
//                String substring = state.substring(0, 2);
//                byte[] src = ByteHelper.hexStringToBytes(substring);
//                int num = ByteHelper.byteIndexValid(src[0], 4, 1);
//                PushExceptionMsg exceptionMsg = new PushExceptionMsg();
//                exceptionMsg.setType(ExceptionEnum.socket.getValue());
//                exceptionMsg.setId(tOboxDeviceConfig.getId());
//                exceptionMsg.setState(state);
//                if (num == 1) {
//                    // 过压
//                    exceptionMsg.setChildType(ExceptionEnum.overcapacity
//                            .getValue());
//                    PushObserverManager.getInstance().sendMessage(exceptionMsg,null);
//                }
//                num = ByteHelper.byteIndexValid(src[0], 5, 1);
//                if (num == 1) {
//                    // 欠压
//                    exceptionMsg.setChildType(ExceptionEnum.undervoltage
//                            .getValue());
//                    PushObserverManager.getInstance().sendMessage(exceptionMsg,null);
//                }
//                num = ByteHelper.byteIndexValid(src[0], 6, 1);
//                if (num == 1) {
//                    // 过流
//                    exceptionMsg.setChildType(ExceptionEnum.overcurrent
//                            .getValue());
//                    PushObserverManager.getInstance().sendMessage(exceptionMsg,null);
//                }
//            }
        List<TSceneCondition> tSceneConditions = sceneConditionService.getSceneConditionBySerialId(tOboxDeviceConfig.getDeviceSerialId());
//            List<TSceneCondition> tSceneConditions = SceneBusiness
//                    .querySceneConditionsBySerialId(tOboxDeviceConfig
//                            .getDeviceSerialId());
            log.info("====DeviceSerialId:"
                    + tOboxDeviceConfig.getDeviceSerialId());
            if (tSceneConditions != null) {
                for (TSceneCondition tSceneCondition : tSceneConditions) {
                    // status match
                    log.info("====status match====scene_number:"
                            + tSceneCondition.getSceneNumber());
                    TScene tScene = sceneService.getSceneBySceneNumber(tSceneCondition.getSceneNumber());
//                    TScene tScene = SceneBusiness
//                            .querySceneBySceneNumber(tSceneCondition
//                                    .getSceneNumber());
                    if (tScene == null) {
                        continue;
                    }
                    boolean stateMacth = false;
                    String cond = tSceneCondition.getCond();
                    // condition match
//                    if (isDoor) {
//                        // 开门
//                        log.info("===condition match start===tSceneCondition.getCondition():"
//                                + tSceneCondition.getCondition()
//                                + "====doorPin:" + doorPin);
//                        // int pin = Integer.parseInt(doorPin, 16);
//                        if (!tSceneCondition.getCondition().equals(doorPin))
//                            continue;
//                        stateMacth = true;
//                    } else {
                        if (isEnvironment == 1)
                            continue;
                        else if (isEnvironment == 2) {
                            byte[] hexStringToBytes = ByteHelper
                                    .hexStringToBytes(tOboxDeviceConfig
                                            .getDeviceState());
                            int byte2int = ByteHelper
                                    .byte2int(hexStringToBytes);
                            byte[] int2byte = ByteHelper
                                    .int2byte(byte2int << 4);
                            state = ByteHelper.bytesToHexString(int2byte);
                        }
                        for (int i = 0; i < cond.length(); i += 4) {
                            int index = Integer.parseInt(
                                    cond.substring(i, i + 2), 16);
                            int vaildLen = (index >> 3) & 0x07;
                            if (vaildLen > 6 || index == 255) {
                                continue;
                            }
                            String condState = cond.substring(i + 2, i + 2
                                    + vaildLen * 2);
                            String invaildString = "";
                            for (int j = 0; j < vaildLen; j++) {
                                invaildString += "ff";
                            }
                            if (condState.equals(invaildString)) {
                                continue;
                            }
                            String updateState = state.substring(i + 2, i + 2
                                    + vaildLen * 2);
                            if (vaildLen > 1) {
                                i += 2 * (vaildLen - 1);
                            }
                            if ((index & 0x07) == 0x01) {
                                // <
                                if (Integer.parseInt(updateState, 16) < Integer
                                        .parseInt(condState, 16)) {
                                    stateMacth = true;

                                } else {
                                    tScene.setAlterNeed(1);
                                    break;
                                }
                            } else if ((index & 0x07) == 0x02) {
                                // ==
                                if (Integer.parseInt(updateState, 16) == Integer
                                        .parseInt(condState, 16)) {
                                    stateMacth = true;
                                } else {
                                    tScene.setAlterNeed(1);
                                    break;
                                }
                            } else if ((index & 0x07) == 0x03) {
                                // <=
                                if (Integer.parseInt(updateState, 16) <= Integer
                                        .parseInt(condState, 16)) {
                                    stateMacth = true;
                                } else {
                                    tScene.setAlterNeed(1);
                                    break;
                                }

                            } else if ((index & 0x07) == 0x04) {
                                // >
                                if (Integer.parseInt(updateState, 16) > Integer
                                        .parseInt(condState, 16)) {
                                    stateMacth = true;

                                } else {
                                    tScene.setAlterNeed(1);
                                    break;
                                }

                            } else if ((index & 0x07) == 0x06) {
                                // >=
                                if (Integer.parseInt(updateState, 16) >= Integer
                                        .parseInt(condState, 16)) {
                                    stateMacth = true;

                                } else {
                                    tScene.setAlterNeed(1);
                                    break;
                                }

                            }
                        }
//                    }

                    if (stateMacth) {
                        List<TSceneCondition> sceneConditions =
                                sceneConditionService.getSceneConditionBySceneNumberAndGroup(tSceneCondition.getSceneNumber(),tSceneCondition.getConditionGroup());
//                        List<TSceneCondition> sceneConditions = SceneBusiness
//                                .querySceneConditionBySceneNumberAndGroup(
//                                        tSceneCondition.getSceneNumber(),
//                                        tSceneCondition.getConditionGroup());
                        if (sceneConditions.size() == 1) {
                            if (tScene.getSceneType().equals(
                                    SceneTypeEnum.server.getValue())) {
                                if (tScene.getSceneRun() == 0
                                        && tScene.getSceneStatus() != 0) {
                                    log.info("===take action start===");
                                    if (tOboxDeviceConfig.getDeviceChildType()
                                            .equals("02")
                                            && tOboxDeviceConfig
                                            .getDeviceType().equals(
                                                    "0b")) {
                                        tScene.setAlterNeed(1);
                                    }
                                    tScene.setSceneRun(1);
                                    sceneService.updateScene(tScene);
//                                    SceneBusiness.updateScene(tScene);
                                    // take action
                                    sceneActionThreadPool.addSceneAction(tScene
                                            .getSceneNumber());
                                } else if (tScene.getSceneRun() == 0
                                        && tScene.getSceneStatus() != 1) {
                                    List<TSceneAction> tSceneActions = sceneActionService.getSceneActionBySceneNumber(tScene
                                            .getSceneNumber());
//                                    List<TSceneAction> tSceneActions = SceneBusiness
//                                            .querySceneActionsBySceneNumber(tScene
//                                                    .getSceneNumber());
                                    for (TSceneAction tSceneAction : tSceneActions) {
                                        if (tSceneAction.getNodeType().equals(
                                                NodeTypeEnum.status.getValue())) {
                                            TScene scene = sceneService.getSceneBySceneNumber(tSceneAction.getSceneNumber());
//                                            TScene scene = SceneBusiness
//                                                    .querySceneBySceneNumber(tSceneAction
//                                                            .getActionID());
                                            if (scene != null) {
                                                if (tSceneAction.getAction()
                                                        .substring(0, 2)
                                                        .equals("00")) {
                                                    scene.setSceneStatus(0);
                                                    sceneService.updateScene(scene);
//                                                    SceneBusiness
//                                                            .updateScene(scene);
                                                } else {
                                                    scene.setSceneStatus(1);
//                                                    SceneBusiness
//                                                            .updateScene(scene);
                                                    sceneService.updateScene(scene);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                List<TSceneAction> tSceneActions = sceneActionService.getSceneActionBySceneNumber(tScene
                                        .getSceneNumber());
//                                List<TSceneAction> tSceneActions = SceneBusiness
//                                        .querySceneActionsBySceneNumber(tScene
//                                                .getSceneNumber());
                                if (tSceneActions != null) {
                                    for (TSceneAction tSceneAction2 : tSceneActions) {
                                        if (tSceneAction2.getNodeType().equals(
                                                NodeTypeEnum.single.getValue())) {
//
                                        } else if (tSceneAction2.getNodeType()
                                                .equals(NodeTypeEnum.group
                                                        .getValue())) {
//											T
                                        }
//                                        else if (tSceneAction2.getNodeType()
//                                                .equals(NodeTypeEnum.camera
//                                                        .getValue())) {
//                                            TYSCamera tysCamera = CameraBusiness
//                                                    .queryYSCameraById(tSceneAction2
//                                                            .getActionID());
//                                            if (tysCamera != null) {
//                                                String urlString = null;
//                                                if (tScene.getLicense() != null) {
//                                                    if (tScene.getLicense() != 0) {
//                                                        TUser root = UserBusiness
//                                                                .queryUserByLicense(tScene
//                                                                        .getLicense());
//                                                        if (root != null) {
//                                                            TUserYS tUserYS = UserBusiness
//                                                                    .queryUserYs(root
//                                                                            .getUserId());
//                                                            if (tUserYS != null) {
//                                                                urlString = YSHelper
//                                                                        .setCapture(tysCamera
//                                                                                .getDeviceSerial());
//                                                                if (urlString != null) {
//                                                                    ImageLoaderPool
//                                                                            .getInstance()
//                                                                            .addImageUrl(
//                                                                                    urlString,
//                                                                                    tysCamera
//                                                                                            .getDeviceSerial(),
//                                                                                    root.getUserId());
//                                                                }
//
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                                List<TUserScene> tUserScenes = SceneBusiness
//                                                        .queryUserSceneBySceneNumber(tScene
//                                                                .getSceneNumber());
//
//                                                for (TUserScene tUserScene : tUserScenes) {
//                                                    TUser tUser = UserBusiness
//                                                            .queryUserById(tUserScene
//                                                                    .getUserId());
//                                                    TUserYS tUserYS = UserBusiness
//                                                            .queryUserYs(tUser
//                                                                    .getUserId());
//                                                    if (tUserYS != null) {
//                                                        if (urlString == null) {
//                                                            urlString = YSHelper
//                                                                    .setCapture(tysCamera
//                                                                            .getDeviceSerial());
//
//                                                        }
//
//                                                        ImageLoaderPool
//                                                                .getInstance()
//                                                                .addImageUrl(
//                                                                        urlString,
//                                                                        tysCamera
//                                                                                .getDeviceSerial(),
//                                                                        tUser.getUserId());
//                                                    }
//                                                }
//                                            }
//
//                                        }
//                                        else if (tSceneAction2.getNodeType()
//                                                .equals(NodeTypeEnum.nvr
//                                                        .getValue())) {
//                                            // nvr
//                                            TNvr tNvr = NvrBusiness
//                                                    .queryNVR(tSceneAction2
//                                                            .getActionID());
//                                            if (tNvr != null) {
//                                                String url = Constants.webHead;
//                                                // String url =
//                                                // "https://BDcloud.on-bright.com/";
//                                                // String url =
//                                                // "https://cloud.on-bright.com/";
//                                                String urlString = null;
//                                                if (tScene.getLicense() != null) {
//                                                    if (tScene.getLicense() != 0) {
//                                                        TUser root = UserBusiness
//                                                                .queryUserByLicense(tScene
//                                                                        .getLicense());
//                                                        if (root != null) {
//                                                            NvrHelper nvrHelper = NvrHelper
//                                                                    .getInstance();
//
//                                                            if (tSceneAction2
//                                                                    .getPreSet() != 0) {
//                                                                urlString = nvrHelper
//                                                                        .captureJPEGWithPreset(
//                                                                                tNvr.getIp(),
//                                                                                tNvr.getPort(),
//                                                                                tNvr.getUser(),
//                                                                                tNvr.getPw(),
//                                                                                Integer.parseInt(tSceneAction2
//                                                                                        .getAction()),
//                                                                                tSceneAction2
//                                                                                        .getPreSet());
//                                                            } else {
//                                                                urlString = nvrHelper
//                                                                        .captureJPEG(
//                                                                                tNvr.getIp(),
//                                                                                tNvr.getPort(),
//                                                                                tNvr.getUser(),
//                                                                                tNvr.getPw(),
//                                                                                Integer.parseInt(tSceneAction2
//                                                                                        .getAction()));
//                                                            }
//
//                                                            if (urlString != null) {
//                                                                TUserNvrPicture tUserNvrPicture = new TUserNvrPicture();
//                                                                tUserNvrPicture
//                                                                        .setChannel(Integer
//                                                                                .parseInt(tSceneAction2
//                                                                                        .getAction()));
//                                                                tUserNvrPicture
//                                                                        .setNvrId(tNvr
//                                                                                .getId());
//                                                                tUserNvrPicture
//                                                                        .setUserId(root
//                                                                                .getUserId());
//                                                                tUserNvrPicture
//                                                                        .setPicUrl(url
//                                                                                + urlString
//                                                                                .substring(5));
//                                                                UserBusiness
//                                                                        .addUserNvrPicture(tUserNvrPicture);
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                                List<TUserScene> tUserScenes = SceneBusiness
//                                                        .queryUserSceneBySceneNumber(tScene
//                                                                .getSceneNumber());
//
//                                                for (TUserScene tUserScene : tUserScenes) {
//                                                    TUser tUser = UserBusiness
//                                                            .queryUserById(tUserScene
//                                                                    .getUserId());
//
//                                                    if (tUser != null) {
//                                                        if (urlString == null) {
//                                                            NvrHelper nvrHelper = NvrHelper
//                                                                    .getInstance();
//                                                            if (tSceneAction2
//                                                                    .getPreSet() != 0) {
//                                                                urlString = nvrHelper
//                                                                        .captureJPEGWithPreset(
//                                                                                tNvr.getIp(),
//                                                                                tNvr.getPort(),
//                                                                                tNvr.getUser(),
//                                                                                tNvr.getPw(),
//                                                                                Integer.parseInt(tSceneAction2
//                                                                                        .getAction()),
//                                                                                tSceneAction2
//                                                                                        .getPreSet());
//                                                            } else {
//                                                                urlString = nvrHelper
//                                                                        .captureJPEG(
//                                                                                tNvr.getIp(),
//                                                                                tNvr.getPort(),
//                                                                                tNvr.getUser(),
//                                                                                tNvr.getPw(),
//                                                                                Integer.parseInt(tSceneAction2
//                                                                                        .getAction()));
//                                                            }
//
//                                                            TUserNvrPicture tUserNvrPicture = new TUserNvrPicture();
//                                                            tUserNvrPicture
//                                                                    .setChannel(Integer
//                                                                            .parseInt(tSceneAction2
//                                                                                    .getAction()));
//                                                            tUserNvrPicture
//                                                                    .setNvrId(tNvr
//                                                                            .getId());
//                                                            tUserNvrPicture
//                                                                    .setUserId(tUser
//                                                                            .getUserId());
//                                                            tUserNvrPicture
//                                                                    .setPicUrl(url
//                                                                            + urlString
//                                                                            .substring(5));
//                                                            UserBusiness
//                                                                    .addUserNvrPicture(tUserNvrPicture);
//                                                        } else {
//                                                            TUserNvrPicture tUserNvrPicture = new TUserNvrPicture();
//                                                            tUserNvrPicture
//                                                                    .setChannel(Integer
//                                                                            .parseInt(tSceneAction2
//                                                                                    .getAction()));
//                                                            tUserNvrPicture
//                                                                    .setNvrId(tNvr
//                                                                            .getId());
//                                                            tUserNvrPicture
//                                                                    .setUserId(tUser
//                                                                            .getUserId());
//                                                            tUserNvrPicture
//                                                                    .setPicUrl(url
//                                                                            + urlString
//                                                                            .substring(5));
//                                                            UserBusiness
//                                                                    .addUserNvrPicture(tUserNvrPicture);
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
                                    }
                                }

//                                if (tScene.getSceneType().equals(
//                                        SceneTypeEnum.someone.getValue())) {
//                                    tObox.setOboxPerson(1);
//                                    OboxBusiness.updateObox(tObox);
//                                } else if (tScene.getSceneType().equals(
//                                        SceneTypeEnum.onone.getValue())) {
//                                    tObox.setOboxPerson(0);
//                                    OboxBusiness.updateObox(tObox);
//                                }
                            }

                        } else if (sceneConditions.size() > 1) {
                            log.info("===mutile condition===");
                            // mutile condition
                            boolean matchCon = true;
                            for (TSceneCondition tSceneCondition2 : sceneConditions) {
                                if (tSceneCondition2.getSerialid() == null) {
                                    // time trigger
                                    String timeCond = tSceneCondition2.getCond();
                                    int cycle = Integer.parseInt(
                                            timeCond.substring(0, 2), 16);
                                    int year = Integer.parseInt(
                                            timeCond.substring(4, 6), 16);
                                    int month = Integer.parseInt(
                                            timeCond.substring(6, 8), 16);
                                    int day = Integer.parseInt(
                                            timeCond.substring(8, 10), 16);
                                    int hour = Integer.parseInt(
                                            timeCond.substring(10, 12), 16);
                                    int min = Integer.parseInt(
                                            timeCond.substring(12, 14), 16);

                                    DateTime dateTime = new DateTime();
                                    if (cycle == 0) {
                                        if (dateTime.getHours() != hour
                                                || dateTime.getMinutes() != min
                                                || (dateTime.getYear() % 2000) != year
                                                || dateTime.getMonth() != month
                                                || dateTime.getDay() != day) {
                                            matchCon = false;
                                            break;
                                        }
                                    } else {
                                        if ((cycle & 0x80) == 0x80) {
                                            // every day
                                            if (dateTime.getHours() != hour
                                                    || dateTime.getMinutes() != min) {
                                                matchCon = false;
                                                break;
                                            }
                                        } else {
                                            TimeZone tz = TimeZone
                                                    .getTimeZone("GMT+8:00");
                                            Calendar date = Calendar
                                                    .getInstance(tz);
                                            for (int i = 0; i < 7; i++) {
                                                if (((cycle >> i) & 0x01) == 0x01) {
                                                    if ((date
                                                            .get(Calendar.DAY_OF_WEEK) - 1) != i) {
                                                        matchCon = false;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (!matchCon) {
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                } else {
                                    if (tSceneCondition2.getSerialid()!= tSceneCondition
                                            .getSerialid()) {
                                        log.info("=====SerialId:"
                                                + tSceneCondition2
                                                .getSerialid());
                                        TOboxDeviceConfig oboxDeviceConfig = oboxDeviceConfigService
                                                .getTOboxDeviceConfigByDeviceSerialId(tSceneCondition2.getSerialid());
//                                        TOboxDeviceConfig oboxDeviceConfig = DeviceBusiness
//                                                .queryDeviceConfigBySerialID(tSceneCondition2
//                                                        .getSerialId());
                                        if (oboxDeviceConfig != null) {
                                            String cond2 = tSceneCondition2.getCond();
                                            // condition match
                                            if (oboxDeviceConfig
                                                    .getDeviceType()
                                                    .equals(DeviceTypeEnum.doorlock
                                                            .getValue())
                                                    && oboxDeviceConfig
                                                    .getDeviceChildType()
                                                    .equals(DeviceTypeEnum.doorlock_child
                                                            .getValue())) {
                                                log.info("===door condition match====");
                                                String updateState = oboxDeviceConfig
                                                        .getDeviceState();
                                                if (updateState != null
                                                        && updateState
                                                        .substring(0, 2)
                                                        .equals("c3")
                                                        && cond2.equals(updateState
                                                        .substring(8,
                                                                10))) {
                                                    log.info("===door condition match true====");
                                                    continue;
                                                } else {
                                                    matchCon = false;
                                                    break;
                                                }
                                            } else {
                                                for (int i = 0; i < cond2
                                                        .length(); i += 4) {
                                                    int index = Integer
                                                            .parseInt(
                                                                    cond2.substring(
                                                                            i,
                                                                            i + 2),
                                                                    16);
                                                    if (index == 255) {
                                                        continue;
                                                    }
                                                    int vaildLen = (index >> 3) & 0x07;
                                                    if (vaildLen > 6) {
                                                        continue;
                                                    }
                                                    String condState = cond2
                                                            .substring(i + 2, i
                                                                    + 2
                                                                    + vaildLen
                                                                    * 2);
                                                    String invaildString = "";
                                                    for (int j = 0; j < vaildLen; j++) {
                                                        invaildString += "ff";
                                                    }
                                                    if (condState
                                                            .equals(invaildString)) {
                                                        continue;
                                                    }
                                                    String updateState = oboxDeviceConfig
                                                            .getDeviceState()
                                                            .substring(
                                                                    i + 2,
                                                                    i
                                                                            + 2
                                                                            + vaildLen
                                                                            * 2);
                                                    if (vaildLen > 1) {
                                                        i += 2 * (vaildLen - 1);
                                                    }
                                                    if ((index & 0x07) == 0x01) {
                                                        // <
                                                        if (Integer
                                                                .parseInt(
                                                                        updateState,
                                                                        16) >= Integer
                                                                .parseInt(
                                                                        condState,
                                                                        16)) {
                                                            matchCon = false;
                                                            break;
                                                        }
                                                    } else if ((index & 0x07) == 0x02) {
                                                        // ==
                                                        if (Integer
                                                                .parseInt(
                                                                        updateState,
                                                                        16) != Integer
                                                                .parseInt(
                                                                        condState,
                                                                        16)) {
                                                            matchCon = false;
                                                            break;
                                                        }
                                                    } else if ((index & 0x07) == 0x03) {
                                                        // <=
                                                        if (Integer
                                                                .parseInt(
                                                                        updateState,
                                                                        16) > Integer
                                                                .parseInt(
                                                                        condState,
                                                                        16)) {
                                                            matchCon = false;
                                                            break;
                                                        }

                                                    } else if ((index & 0x07) == 0x04) {
                                                        // >
                                                        if (Integer
                                                                .parseInt(
                                                                        updateState,
                                                                        16) <= Integer
                                                                .parseInt(
                                                                        condState,
                                                                        16)) {
                                                            matchCon = false;
                                                            break;
                                                        }

                                                    } else if ((index & 0x07) == 0x06) {
                                                        // >=
                                                        if (Integer
                                                                .parseInt(
                                                                        updateState,
                                                                        16) < Integer
                                                                .parseInt(
                                                                        condState,
                                                                        16)) {
                                                            matchCon = false;
                                                            break;
                                                        }

                                                    }
                                                }
                                            }
                                        } else {
//                                            TFingerprint tFingerprint = FingerprintBusiness
//                                                    .queryFingerPrint(tSceneCondition2
//                                                            .getSerialId());
//                                            if (tFingerprint != null) {
//                                                String serial_pin = AccessTokenTool
//                                                        .getFingerprint(
//                                                                tSceneCondition2
//                                                                        .getSceneNumber(),
//                                                                tSceneCondition2
//                                                                        .getSerialId(),
//                                                                tSceneCondition2
//                                                                        .getCondition());
//                                                if (StringUtils
//                                                        .isEmpty(serial_pin)) {
//                                                    matchCon = false;
//                                                    break;
//                                                } else {
//                                                    matchCon = true;
//                                                }
//                                            }
                                        }
                                    }
                                }
                            }

                            if (matchCon) {
                                log.info("===start execute scene===");
                                if (tScene.getSceneType().equals(
                                        SceneTypeEnum.server.getValue())) {
                                    if (tScene.getSceneRun() == 0
                                            && tScene.getSceneStatus() != 0) {
                                        if (tOboxDeviceConfig
                                                .getDeviceChildType().equals(
                                                        "02")
                                                && tOboxDeviceConfig
                                                .getDeviceType()
                                                .equals("0b")) {
                                            tScene.setAlterNeed(1);
                                        }
                                        tScene.setSceneRun(1);
                                        sceneService.updateScene(tScene);
//                                        SceneBusiness.updateScene(tScene);
                                        // take action
                                        sceneActionThreadPool
                                                .addSceneAction(tScene
                                                        .getSceneNumber());
                                    } else if (tScene.getSceneRun() == 0
                                            && tScene.getSceneStatus() != 1) {
                                        List<TSceneAction> tSceneActions = sceneActionService.getSceneActionBySceneNumber(tScene
                                                .getSceneNumber());
//                                        List<TSceneAction> tSceneActions = SceneBusiness
//                                                .querySceneActionsBySceneNumber(tScene
//                                                        .getSceneNumber());
                                        for (TSceneAction tSceneAction : tSceneActions) {
                                            if (tSceneAction.getNodeType()
                                                    .equals(NodeTypeEnum.status
                                                            .getValue())) {
                                                TScene scene = sceneService.getSceneBySceneNumber(tSceneAction.getSceneNumber());
//                                                TScene scene = SceneBusiness
//                                                        .querySceneBySceneNumber(tSceneAction
//                                                                .getActionID());
                                                if (scene != null) {
                                                    if (tSceneAction
                                                            .getAction()
                                                            .substring(0, 2)
                                                            .equals("00")) {
                                                        scene.setSceneStatus(0);
                                                        sceneService.updateScene(scene);
//                                                        SceneBusiness
//                                                                .updateScene(scene);
                                                    } else {
                                                        scene.setSceneStatus(1);
                                                        sceneService.updateScene(scene);
//                                                        SceneBusiness
//                                                                .updateScene(scene);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else if (tScene.getSceneType().equals(
                                        SceneTypeEnum.defend.getValue())) {
                                    boolean shouldAction = defendScene(tScene,
                                            tSceneCondition.getConditionGroup());
                                    if (shouldAction) {
                                        if (tScene.getSceneRun() == 0
                                                && tScene.getSceneStatus() != 0) {
                                            log.info("===take action start===");
                                            if (tOboxDeviceConfig
                                                    .getDeviceChildType()
                                                    .equals("02")
                                                    && tOboxDeviceConfig
                                                    .getDeviceType()
                                                    .equals("0b")) {
                                                tScene.setAlterNeed(1);
                                            }
                                            tScene.setSceneRun(1);
                                            sceneService.updateScene(tScene);
//                                            SceneBusiness.updateScene(tScene);
                                            // take action
                                            sceneActionThreadPool
                                                    .addSceneAction(tScene
                                                            .getSceneNumber());
                                        }
                                    } else if (tScene.getSceneRun() == 0
                                            && tScene.getSceneStatus() != 1) {
                                        List<TSceneAction> tSceneActions = sceneActionService.getSceneActionBySceneNumber(tScene
                                                .getSceneNumber());
//                                        List<TSceneAction> tSceneActions = SceneBusiness
//                                                .querySceneActionsBySceneNumber(tScene
//                                                        .getSceneNumber());
                                        for (TSceneAction tSceneAction : tSceneActions) {
                                            if (tSceneAction.getNodeType()
                                                    .equals(NodeTypeEnum.status
                                                            .getValue())) {
                                                TScene scene = sceneService.getSceneBySceneNumber(tSceneAction.getSceneNumber());
//                                                TScene scene = SceneBusiness
//                                                        .querySceneBySceneNumber(tSceneAction
//                                                                .getActionID());
                                                if (scene != null) {
                                                    if (tSceneAction
                                                            .getAction()
                                                            .substring(0, 2)
                                                            .equals("00")) {
                                                        scene.setSceneStatus(0);
                                                        sceneService.updateScene(scene);
//                                                        SceneBusiness
//                                                                .updateScene(scene);
                                                    } else {
                                                        scene.setSceneStatus(1);
                                                        sceneService.updateScene(scene);
//                                                        SceneBusiness
//                                                                .updateScene(scene);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    List<TSceneAction> tSceneActions = sceneActionService.getSceneActionBySceneNumber(tScene
                                            .getSceneNumber());
//                                    List<TSceneAction> tSceneActions = SceneBusiness
//                                            .querySceneActionsBySceneNumber(tScene
//                                                    .getSceneNumber());
                                    if (tSceneActions != null) {
                                        for (TSceneAction tSceneAction2 : tSceneActions) {
                                            if (tSceneAction2.getNodeType()
                                                    .equals(NodeTypeEnum.single
                                                            .getValue())) {
//												TOboxDeviceConfig oboxDeviceConfig = DeviceBusiness
//														.queryDeviceConfigByID(tSceneAction2
//																.getActionID());
//												if (oboxDeviceConfig != null) {
//													oboxDeviceConfig
//															.setDeviceState(tSceneAction2
//																	.getAction());
//													OboxBusiness
//															.updateOboxDeviceConfig(oboxDeviceConfig);
//												}
                                            } else if (tSceneAction2
                                                    .getNodeType()
                                                    .equals(NodeTypeEnum.group
                                                            .getValue())) {
//												TServerGroup tServerGroup = DeviceBusiness
//														.querySererGroupById(tSceneAction2
//																.getActionID());
//												if (tServerGroup != null) {
//													tServerGroup
//															.setGroupState(tSceneAction2
//																	.getAction());
//													DeviceBusiness
//															.updateServerGroupName(tServerGroup);
//													TServerOboxGroup tServerOboxGroup = DeviceBusiness
//															.queryOBOXGroupByServerID(tServerGroup
//																	.getId());
//													if (tServerGroup != null) {
//														List<TOboxDeviceConfig> tOboxDeviceConfigs = DeviceBusiness
//																.queryOBOXGroupMemberByAddr(
//																		tServerOboxGroup
//																				.getOboxSerialId(),
//																		tServerOboxGroup
//																				.getGroupAddr());
//														for (TOboxDeviceConfig tOboxDeviceConfig2 : tOboxDeviceConfigs) {
//															tOboxDeviceConfig2
//																	.setDeviceState(tSceneAction2
//																			.getAction());
//															OboxBusiness
//																	.updateOboxDeviceConfig(tOboxDeviceConfig2);
//														}
//													}
//												}
                                            }
//                                            else if (tSceneAction2
//                                                    .getNodeType()
//                                                    .equals(NodeTypeEnum.camera
//                                                            .getValue())) {
//                                                TYSCamera tysCamera = CameraBusiness
//                                                        .queryYSCameraById(tSceneAction2
//                                                                .getActionID());
//                                                if (tysCamera != null) {
//                                                    List<TUserScene> tUserScenes = SceneBusiness
//                                                            .queryUserSceneBySceneNumber(tScene
//                                                                    .getSceneNumber());
//                                                    String urlString = null;
//                                                    for (TUserScene tUserScene : tUserScenes) {
//                                                        TUser tUser = UserBusiness
//                                                                .queryUserById(tUserScene
//                                                                        .getUserId());
//                                                        TUserYS tUserYS = UserBusiness
//                                                                .queryUserYs(tUser
//                                                                        .getUserId());
//                                                        if (tUserYS != null) {
//                                                            if (urlString == null) {
//                                                                urlString = YSHelper
//                                                                        .setCapture(tysCamera
//                                                                                .getDeviceSerial());
//                                                            } else {
//                                                                /*
//                                                                 * UserBusiness
//                                                                 * .
//                                                                 * addUserPicture
//                                                                 * (
//                                                                 * tUser.getUserId
//                                                                 * (), tysCamera
//                                                                 * .
//                                                                 * getDeviceSerial
//                                                                 * (),
//                                                                 * urlString);
//                                                                 */
//                                                                ImageLoaderPool
//                                                                        .getInstance()
//                                                                        .addImageUrl(
//                                                                                urlString,
//                                                                                tysCamera
//                                                                                        .getDeviceSerial(),
//                                                                                tUser.getUserId());
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                            else if (tSceneAction2
//                                                    .getNodeType()
//                                                    .equals(NodeTypeEnum.nvr
//                                                            .getValue())) {
//                                                // NVR
//                                                TNvr tNvr = NvrBusiness
//                                                        .queryNVR(tSceneAction2
//                                                                .getActionID());
//                                                if (tNvr != null) {
//                                                    String url = Constants.webHead;
//                                                    String urlString = null;
//                                                    if (tScene.getLicense() != null) {
//                                                        if (tScene.getLicense() != 0) {
//                                                            TUser root = UserBusiness
//                                                                    .queryUserByLicense(tScene
//                                                                            .getLicense());
//                                                            if (root != null) {
//                                                                NvrHelper nvrHelper = NvrHelper
//                                                                        .getInstance();
//                                                                if (tSceneAction2
//                                                                        .getPreSet() != 0) {
//                                                                    urlString = nvrHelper
//                                                                            .captureJPEGWithPreset(
//                                                                                    tNvr.getIp(),
//                                                                                    tNvr.getPort(),
//                                                                                    tNvr.getUser(),
//                                                                                    tNvr.getPw(),
//                                                                                    Integer.parseInt(tSceneAction2
//                                                                                            .getAction()),
//                                                                                    tSceneAction2
//                                                                                            .getPreSet());
//                                                                } else {
//                                                                    urlString = nvrHelper
//                                                                            .captureJPEG(
//                                                                                    tNvr.getIp(),
//                                                                                    tNvr.getPort(),
//                                                                                    tNvr.getUser(),
//                                                                                    tNvr.getPw(),
//                                                                                    Integer.parseInt(tSceneAction2
//                                                                                            .getAction()));
//                                                                }
//
//                                                                if (urlString != null) {
//                                                                    TUserNvrPicture tUserNvrPicture = new TUserNvrPicture();
//                                                                    tUserNvrPicture
//                                                                            .setChannel(Integer
//                                                                                    .parseInt(tSceneAction2
//                                                                                            .getAction()));
//                                                                    tUserNvrPicture
//                                                                            .setNvrId(tNvr
//                                                                                    .getId());
//                                                                    tUserNvrPicture
//                                                                            .setUserId(root
//                                                                                    .getUserId());
//                                                                    tUserNvrPicture
//                                                                            .setPicUrl(url
//                                                                                    + urlString
//                                                                                    .substring(5));
//                                                                    UserBusiness
//                                                                            .addUserNvrPicture(tUserNvrPicture);
//                                                                }
//                                                            }
//                                                        }
//                                                    }
//                                                    List<TUserScene> tUserScenes = SceneBusiness
//                                                            .queryUserSceneBySceneNumber(tScene
//                                                                    .getSceneNumber());
//
//                                                    for (TUserScene tUserScene : tUserScenes) {
//                                                        TUser tUser = UserBusiness
//                                                                .queryUserById(tUserScene
//                                                                        .getUserId());
//
//                                                        if (tUser != null) {
//                                                            if (urlString == null) {
//                                                                NvrHelper nvrHelper = NvrHelper
//                                                                        .getInstance();
//                                                                if (tSceneAction2
//                                                                        .getPreSet() != 0) {
//                                                                    urlString = nvrHelper
//                                                                            .captureJPEGWithPreset(
//                                                                                    tNvr.getIp(),
//                                                                                    tNvr.getPort(),
//                                                                                    tNvr.getUser(),
//                                                                                    tNvr.getPw(),
//                                                                                    Integer.parseInt(tSceneAction2
//                                                                                            .getAction()),
//                                                                                    tSceneAction2
//                                                                                            .getPreSet());
//                                                                } else {
//                                                                    urlString = nvrHelper
//                                                                            .captureJPEG(
//                                                                                    tNvr.getIp(),
//                                                                                    tNvr.getPort(),
//                                                                                    tNvr.getUser(),
//                                                                                    tNvr.getPw(),
//                                                                                    Integer.parseInt(tSceneAction2
//                                                                                            .getAction()));
//                                                                }
//                                                                TUserNvrPicture tUserNvrPicture = new TUserNvrPicture();
//                                                                tUserNvrPicture
//                                                                        .setChannel(Integer
//                                                                                .parseInt(tSceneAction2
//                                                                                        .getAction()));
//                                                                tUserNvrPicture
//                                                                        .setNvrId(tNvr
//                                                                                .getId());
//                                                                tUserNvrPicture
//                                                                        .setUserId(tUser
//                                                                                .getUserId());
//                                                                tUserNvrPicture
//                                                                        .setPicUrl(url
//                                                                                + urlString
//                                                                                .substring(5));
//                                                                UserBusiness
//                                                                        .addUserNvrPicture(tUserNvrPicture);
//                                                            } else {
//                                                                TUserNvrPicture tUserNvrPicture = new TUserNvrPicture();
//                                                                tUserNvrPicture
//                                                                        .setChannel(Integer
//                                                                                .parseInt(tSceneAction2
//                                                                                        .getAction()));
//                                                                tUserNvrPicture
//                                                                        .setNvrId(tNvr
//                                                                                .getId());
//                                                                tUserNvrPicture
//                                                                        .setUserId(tUser
//                                                                                .getUserId());
//                                                                tUserNvrPicture
//                                                                        .setPicUrl(url
//                                                                                + urlString
//                                                                                .substring(5));
//                                                                UserBusiness
//                                                                        .addUserNvrPicture(tUserNvrPicture);
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            }
                                        }
                                    }

//                                    if (tScene.getSceneType().equals(
//                                            SceneTypeEnum.someone.getValue())) {
//                                        tObox.setOboxPerson(1);
//                                        OboxBusiness.updateObox(tObox);
//                                    } else if (tScene.getSceneType().equals(
//                                            SceneTypeEnum.onone.getValue())) {
//                                        tObox.setOboxPerson(0);
//                                        OboxBusiness.updateObox(tObox);
//                                    }
                                }
                            }
                        }
                    } else {
                          sceneService.updateScene(tScene);
//                        SceneBusiness.updateScene(tScene);
                    }
                }
            }
            if ((tOboxDeviceConfig.getDeviceType().equals(
                    DeviceTypeEnum.doorlock.getValue()) && tOboxDeviceConfig
                    .getDeviceChildType().equals(
                            DeviceTypeEnum.doorlock_child.getValue()))
                    || (tOboxDeviceConfig.getDeviceChildType().equals("15") && tOboxDeviceConfig
                    .getDeviceType().equals(
                            DeviceTypeEnum.sensor.getValue()))) {
                List<TSceneAction> list =null;
                //todo
//                List<TSceneAction> list = SceneBusiness
//                        .querySceneActionByActionIdAndSceneType(
//                                tOboxDeviceConfig.getId(),
//                                SceneTypeEnum.defend.getValue(),
//                                NodeTypeEnum.single.getValue(),
//                                SceneStatusEnum.enable.getValue());
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        TSceneAction sceneAction = list.get(i);
                        // 00没人/没打开 01有人/开门
                        if (sceneAction.getAction().substring(0, 2)
                                .equals("00")) {
                            if (tOboxDeviceConfig.getDeviceState()
                                    .substring(2, 4).equals("00")
                                    || !tOboxDeviceConfig.getDeviceState()
                                    .substring(2, 4).equals("c3")
                                    || !tOboxDeviceConfig.getDeviceState()
                                    .substring(2, 4).equals("c5")) {
                                sceneActionThreadPool
                                        .addSceneAction(sceneAction
                                                .getSceneNumber());
                            }

                        } else {
                            if (tOboxDeviceConfig.getDeviceState()
                                    .substring(2, 4).equals("01")
                                    || tOboxDeviceConfig.getDeviceState()
                                    .substring(2, 4).equals("c3")
                                    || tOboxDeviceConfig.getDeviceState()
                                    .substring(2, 4).equals("c5")) {
                                sceneActionThreadPool
                                        .addSceneAction(sceneAction
                                                .getSceneNumber());
                            }
                        }
                    }
                }
            }

//        }
        return null;
    }

    public boolean defendScene(TScene tScene, int group) {
        int mint = 1;
        int firstGroup = 0;
        int secondGroup = 1;
        if (group == 0) {
            firstGroup = 2;
        } else if (group == 1) {
            secondGroup = 2;
        }
        try {
            boolean matchCon = true;
            List<TSceneCondition> sceneConditions
                    = sceneConditionService.getSceneConditionBySceneNumberAndGroup(tScene.getSceneNumber(),firstGroup);
//            List<TSceneCondition> sceneConditions = SceneBusiness
//                    .querySceneConditionBySceneNumberAndGroup(
//                            tScene.getSceneNumber(), firstGroup);
            if (sceneConditions != null) {
                for (TSceneCondition tSceneCondition : sceneConditions) {
                    if (tSceneCondition.getSerialid() == null) {
                        // time trigger
                        String timeCond = tSceneCondition.getCond();
                        int cycle = Integer.parseInt(timeCond.substring(0, 2),
                                16);
                        int year = Integer.parseInt(timeCond.substring(4, 6),
                                16);
                        int month = Integer.parseInt(timeCond.substring(6, 8),
                                16);
                        int day = Integer.parseInt(timeCond.substring(8, 10),
                                16);
                        int hour = Integer.parseInt(timeCond.substring(10, 12),
                                16);
                        int min = Integer.parseInt(timeCond.substring(12, 14),
                                16);

                        DateTime dateTime = new DateTime();
                        if (cycle == 0) {
                            if (dateTime.getHours() != hour
                                    || dateTime.getMinutes() != min
                                    || (dateTime.getYear() % 2000) != year
                                    || dateTime.getMonth() != month
                                    || dateTime.getDay() != day) {
                                matchCon = false;
                                break;
                            }
                        } else {
                            if ((cycle & 0x80) == 0x80) {
                                // every day
                                if (dateTime.getHours() != hour
                                        || dateTime.getMinutes() != min) {
                                    matchCon = false;
                                    break;
                                }
                            } else {
                                TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
                                Calendar date = Calendar.getInstance(tz);
                                for (int i = 0; i < 7; i++) {
                                    if (((cycle >> i) & 0x01) == 0x01) {
                                        if ((date.get(Calendar.DAY_OF_WEEK) - 1) != i) {
                                            matchCon = false;
                                            break;
                                        }
                                    }
                                }
                                if (!matchCon) {
                                    break;
                                }
                            }
                        }
                        break;
                    } else {
                        TOboxDeviceConfig oboxDeviceConfig = oboxDeviceConfigService.getTOboxDeviceConfigByDeviceSerialId(tSceneCondition.getSerialid());
//                        TOboxDeviceConfig oboxDeviceConfig = DeviceBusiness
//                                .queryDeviceConfigBySerialID(tSceneCondition
//                                        .getSerialId());
                        if (oboxDeviceConfig != null) {
                            String cond2 = tSceneCondition.getCond();
                            // condition match
                            for (int i = 0; i < cond2.length(); i += 4) {
                                int index = Integer.parseInt(
                                        cond2.substring(i, i + 2), 16);
                                if (index == 255) {
                                    continue;
                                }
                                int vaildLen = (index >> 3) & 0x07;
                                if (vaildLen > 6) {
                                    continue;
                                }
                                String condState = cond2.substring(i + 2, i + 2
                                        + vaildLen * 2);
                                String invaildString = "";
                                for (int j = 0; j < vaildLen; j++) {
                                    invaildString += "ff";
                                }
                                if (condState.equals(invaildString)) {
                                    continue;
                                }
                                String updateState = oboxDeviceConfig
                                        .getDeviceState().substring(i + 2,
                                                i + 2 + vaildLen * 2);
                                if (vaildLen > 1) {
                                    i += 2 * (vaildLen - 1);
                                }
                                if ((index & 0x07) == 0x01) {
                                    // <
//                                    if (Integer.parseInt(updateState, 16) >= Integer
//                                            .parseInt(condState, 16)) {
//                                        matchCon = false;
//                                        List<TDeviceStatus> status = DeviceBusiness
//                                                .queryDeviceStatus(
//                                                        oboxDeviceConfig
//                                                                .getDeviceSerialId(),
//                                                        mint);
//                                        if (status != null) {
//                                            for (TDeviceStatus tDeviceStatus : status) {
//                                                String state = tDeviceStatus
//                                                        .getDeviceState()
//                                                        .substring(
//                                                                i + 2,
//                                                                i
//                                                                        + 2
//                                                                        + vaildLen
//                                                                        * 2);
//                                                if (Integer.parseInt(state, 16) < Integer
//                                                        .parseInt(condState, 16)) {
//                                                    matchCon = true;
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                        if (!matchCon) {
//                                            break;
//                                        }
//                                    }
                                } else if ((index & 0x07) == 0x02) {
                                    // ==
//                                    if (Integer.parseInt(updateState, 16) != Integer
//                                            .parseInt(condState, 16)) {
//                                        matchCon = false;
//                                        List<TDeviceStatus> status = DeviceBusiness
//                                                .queryDeviceStatus(
//                                                        oboxDeviceConfig
//                                                                .getDeviceSerialId(),
//                                                        mint);
//                                        if (status != null) {
//                                            for (TDeviceStatus tDeviceStatus : status) {
//                                                String state = tDeviceStatus
//                                                        .getDeviceState()
//                                                        .substring(
//                                                                i + 2,
//                                                                i
//                                                                        + 2
//                                                                        + vaildLen
//                                                                        * 2);
//                                                if (Integer.parseInt(state, 16) == Integer
//                                                        .parseInt(condState, 16)) {
//                                                    matchCon = true;
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                        if (!matchCon) {
//                                            break;
//                                        }
//                                    }
                                } else if ((index & 0x07) == 0x03) {
                                    // <=
//                                    if (Integer.parseInt(updateState, 16) > Integer
//                                            .parseInt(condState, 16)) {
//                                        matchCon = false;
//                                        List<TDeviceStatus> status = DeviceBusiness
//                                                .queryDeviceStatus(
//                                                        oboxDeviceConfig
//                                                                .getDeviceSerialId(),
//                                                        mint);
//                                        if (status != null) {
//                                            for (TDeviceStatus tDeviceStatus : status) {
//                                                String state = tDeviceStatus
//                                                        .getDeviceState()
//                                                        .substring(
//                                                                i + 2,
//                                                                i
//                                                                        + 2
//                                                                        + vaildLen
//                                                                        * 2);
//                                                if (Integer.parseInt(state, 16) <= Integer
//                                                        .parseInt(condState, 16)) {
//                                                    matchCon = true;
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                        if (!matchCon) {
//                                            break;
//                                        }
//                                    }

                                } else if ((index & 0x07) == 0x04) {
                                    // >
//                                    if (Integer.parseInt(updateState, 16) <= Integer
//                                            .parseInt(condState, 16)) {
//                                        matchCon = false;
//                                        List<TDeviceStatus> status = DeviceBusiness
//                                                .queryDeviceStatus(
//                                                        oboxDeviceConfig
//                                                                .getDeviceSerialId(),
//                                                        mint);
//                                        if (status != null) {
//                                            for (TDeviceStatus tDeviceStatus : status) {
//                                                String state = tDeviceStatus
//                                                        .getDeviceState()
//                                                        .substring(
//                                                                i + 2,
//                                                                i
//                                                                        + 2
//                                                                        + vaildLen
//                                                                        * 2);
//                                                if (Integer.parseInt(state, 16) > Integer
//                                                        .parseInt(condState, 16)) {
//                                                    matchCon = true;
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                        if (!matchCon) {
//                                            break;
//                                        }
//                                    }

                                } else if ((index & 0x07) == 0x06) {
                                    // >=
//                                    if (Integer.parseInt(updateState, 16) < Integer
//                                            .parseInt(condState, 16)) {
//                                        matchCon = false;
//                                        List<TDeviceStatus> status = DeviceBusiness
//                                                .queryDeviceStatus(
//                                                        oboxDeviceConfig
//                                                                .getDeviceSerialId(),
//                                                        mint);
//                                        if (status != null) {
//                                            for (TDeviceStatus tDeviceStatus : status) {
//                                                String state = tDeviceStatus
//                                                        .getDeviceState()
//                                                        .substring(
//                                                                i + 2,
//                                                                i
//                                                                        + 2
//                                                                        + vaildLen
//                                                                        * 2);
//                                                if (Integer.parseInt(state, 16) >= Integer
//                                                        .parseInt(condState, 16)) {
//                                                    matchCon = true;
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                        if (!matchCon) {
//                                            break;
//                                        }
//                                    }

                                }
                            }
                        } else {
//                            TFingerprint tFingerprint = FingerprintBusiness
//                                    .queryFingerPrint(tSceneCondition
//                                            .getSerialId());
//                            if (tFingerprint != null) {
//                                String serial_pin = AccessTokenTool
//                                        .getFingerprint(tSceneCondition
//                                                        .getSceneNumber(),
//                                                tSceneCondition.getSerialId(),
//                                                tSceneCondition.getCondition());
//                                if (StringUtils.isEmpty(serial_pin)) {
//                                    matchCon = false;
//                                    break;
//                                } else {
//                                    matchCon = true;
//                                }
//                            }
                        }
                    }
                }
            }
            if (!matchCon) {
                List<TSceneCondition> conditions =
                        sceneConditionService.getSceneConditionBySceneNumberAndGroup(tScene.getSceneNumber(), secondGroup);
//                List<TSceneCondition> conditions = SceneBusiness
//                        .querySceneConditionBySceneNumberAndGroup(
//                                tScene.getSceneNumber(), secondGroup);
                if (conditions != null) {
                    for (TSceneCondition tSceneCondition : conditions) {
                        if (tSceneCondition.getSerialid() == null) {
                            // time trigger
                            String timeCond = tSceneCondition.getCond();
                            int cycle = Integer.parseInt(
                                    timeCond.substring(0, 2), 16);
                            int year = Integer.parseInt(
                                    timeCond.substring(4, 6), 16);
                            int month = Integer.parseInt(
                                    timeCond.substring(6, 8), 16);
                            int day = Integer.parseInt(
                                    timeCond.substring(8, 10), 16);
                            int hour = Integer.parseInt(
                                    timeCond.substring(10, 12), 16);
                            int min = Integer.parseInt(
                                    timeCond.substring(12, 14), 16);

                            DateTime dateTime = new DateTime();
                            if (cycle == 0) {
                                if (dateTime.getHours() != hour
                                        || dateTime.getMinutes() != min
                                        || (dateTime.getYear() % 2000) != year
                                        || dateTime.getMonth() != month
                                        || dateTime.getDay() != day) {
                                    matchCon = false;
                                    break;
                                }
                            } else {
                                if ((cycle & 0x80) == 0x80) {
                                    // every day
                                    if (dateTime.getHours() != hour
                                            || dateTime.getMinutes() != min) {
                                        matchCon = false;
                                        break;
                                    }
                                } else {
                                    TimeZone tz = TimeZone
                                            .getTimeZone("GMT+8:00");
                                    Calendar date = Calendar.getInstance(tz);
                                    for (int i = 0; i < 7; i++) {
                                        if (((cycle >> i) & 0x01) == 0x01) {
                                            if ((date.get(Calendar.DAY_OF_WEEK) - 1) != i) {
                                                matchCon = false;
                                                break;
                                            }
                                        }
                                    }
                                    if (!matchCon) {
                                        break;
                                    }
                                }
                            }
                            break;
                        } else {
                            TOboxDeviceConfig oboxDeviceConfig =
                                    oboxDeviceConfigService.getTOboxDeviceConfigByDeviceSerialId(tSceneCondition.getSerialid());
//                            TOboxDeviceConfig oboxDeviceConfig = DeviceBusiness
//                                    .queryDeviceConfigBySerialID(tSceneCondition
//                                            .getSerialId());
                            if (oboxDeviceConfig != null) {
                                String cond2 = tSceneCondition.getCond();
                                // condition match
                                for (int i = 0; i < cond2.length(); i += 4) {
                                    int index = Integer.parseInt(
                                            cond2.substring(i, i + 2), 16);
                                    if (index == 255) {
                                        continue;
                                    }
                                    int vaildLen = (index >> 3) & 0x07;
                                    if (vaildLen > 6) {
                                        continue;
                                    }
                                    String condState = cond2.substring(i + 2, i
                                            + 2 + vaildLen * 2);
                                    String invaildString = "";
                                    for (int j = 0; j < vaildLen; j++) {
                                        invaildString += "ff";
                                    }
                                    if (condState.equals(invaildString)) {
                                        continue;
                                    }
                                    String updateState = oboxDeviceConfig
                                            .getDeviceState().substring(i + 2,
                                                    i + 2 + vaildLen * 2);
                                    if (vaildLen > 1) {
                                        i += 2 * (vaildLen - 1);
                                    }
                                    if ((index & 0x07) == 0x01) {
                                        // <
//                                        if (Integer.parseInt(updateState, 16) >= Integer
//                                                .parseInt(condState, 16)) {
//                                            matchCon = false;
//                                            List<TDeviceStatus> status = DeviceBusiness
//                                                    .queryDeviceStatus(
//                                                            oboxDeviceConfig
//                                                                    .getDeviceSerialId(),
//                                                            mint);
//                                            if (status != null) {
//                                                for (TDeviceStatus tDeviceStatus : status) {
//                                                    String state = tDeviceStatus
//                                                            .getDeviceState()
//                                                            .substring(
//                                                                    i + 2,
//                                                                    i
//                                                                            + 2
//                                                                            + vaildLen
//                                                                            * 2);
//                                                    if (Integer.parseInt(state,
//                                                            16) < Integer
//                                                            .parseInt(
//                                                                    condState,
//                                                                    16)) {
//                                                        matchCon = true;
//                                                        break;
//                                                    }
//                                                }
//                                            }
//                                            if (!matchCon) {
//                                                break;
//                                            }
//                                        }
                                    } else if ((index & 0x07) == 0x02) {
                                        // ==
//                                        if (Integer.parseInt(updateState, 16) != Integer
//                                                .parseInt(condState, 16)) {
//                                            matchCon = false;
//                                            List<TDeviceStatus> status = DeviceBusiness
//                                                    .queryDeviceStatus(
//                                                            oboxDeviceConfig
//                                                                    .getDeviceSerialId(),
//                                                            mint);
//                                            if (status != null) {
//                                                for (TDeviceStatus tDeviceStatus : status) {
//                                                    String state = tDeviceStatus
//                                                            .getDeviceState()
//                                                            .substring(
//                                                                    i + 2,
//                                                                    i
//                                                                            + 2
//                                                                            + vaildLen
//                                                                            * 2);
//                                                    if (Integer.parseInt(state,
//                                                            16) == Integer
//                                                            .parseInt(
//                                                                    condState,
//                                                                    16)) {
//                                                        matchCon = true;
//                                                        break;
//                                                    }
//                                                }
//                                            }
//                                            if (!matchCon) {
//                                                break;
//                                            }
//                                        }
                                    } else if ((index & 0x07) == 0x03) {
                                        // <=
//                                        if (Integer.parseInt(updateState, 16) > Integer
//                                                .parseInt(condState, 16)) {
//                                            matchCon = false;
//                                            List<TDeviceStatus> status = DeviceBusiness
//                                                    .queryDeviceStatus(
//                                                            oboxDeviceConfig
//                                                                    .getDeviceSerialId(),
//                                                            mint);
//                                            if (status != null) {
//                                                for (TDeviceStatus tDeviceStatus : status) {
//                                                    String state = tDeviceStatus
//                                                            .getDeviceState()
//                                                            .substring(
//                                                                    i + 2,
//                                                                    i
//                                                                            + 2
//                                                                            + vaildLen
//                                                                            * 2);
//                                                    if (Integer.parseInt(state,
//                                                            16) <= Integer
//                                                            .parseInt(
//                                                                    condState,
//                                                                    16)) {
//                                                        matchCon = true;
//                                                        break;
//                                                    }
//                                                }
//                                            }
//                                            if (!matchCon) {
//                                                break;
//                                            }
//                                        }

                                    } else if ((index & 0x07) == 0x04) {
                                        // >
//                                        if (Integer.parseInt(updateState, 16) <= Integer
//                                                .parseInt(condState, 16)) {
//                                            matchCon = false;
//                                            List<TDeviceStatus> status = DeviceBusiness
//                                                    .queryDeviceStatus(
//                                                            oboxDeviceConfig
//                                                                    .getDeviceSerialId(),
//                                                            mint);
//                                            if (status != null) {
//                                                for (TDeviceStatus tDeviceStatus : status) {
//                                                    String state = tDeviceStatus
//                                                            .getDeviceState()
//                                                            .substring(
//                                                                    i + 2,
//                                                                    i
//                                                                            + 2
//                                                                            + vaildLen
//                                                                            * 2);
//                                                    if (Integer.parseInt(state,
//                                                            16) > Integer
//                                                            .parseInt(
//                                                                    condState,
//                                                                    16)) {
//                                                        matchCon = true;
//                                                        break;
//                                                    }
//                                                }
//                                            }
//                                            if (!matchCon) {
//                                                break;
//                                            }
//                                        }

                                    } else if ((index & 0x07) == 0x06) {
                                        // >=
//                                        if (Integer.parseInt(updateState, 16) < Integer
//                                                .parseInt(condState, 16)) {
//                                            matchCon = false;
//                                            List<TDeviceStatus> status = DeviceBusiness
//                                                    .queryDeviceStatus(
//                                                            oboxDeviceConfig
//                                                                    .getDeviceSerialId(),
//                                                            mint);
//                                            if (status != null) {
//                                                for (TDeviceStatus tDeviceStatus : status) {
//                                                    String state = tDeviceStatus
//                                                            .getDeviceState()
//                                                            .substring(
//                                                                    i + 2,
//                                                                    i
//                                                                            + 2
//                                                                            + vaildLen
//                                                                            * 2);
//                                                    if (Integer.parseInt(state,
//                                                            16) >= Integer
//                                                            .parseInt(
//                                                                    condState,
//                                                                    16)) {
//                                                        matchCon = true;
//                                                        break;
//                                                    }
//                                                }
//                                            }
//                                            if (!matchCon) {
//                                                break;
//                                            }
//                                        }

                                    }
                                }
                            } else {
//                                TFingerprint tFingerprint = FingerprintBusiness
//                                        .queryFingerPrint(tSceneCondition
//                                                .getSerialId());
//                                if (tFingerprint != null) {
//                                    String serial_pin = AccessTokenTool
//                                            .getFingerprint(tSceneCondition
//                                                            .getSceneNumber(),
//                                                    tSceneCondition
//                                                            .getSerialId(),
//                                                    tSceneCondition
//                                                            .getCondition());
//                                    if (StringUtils.isEmpty(serial_pin)) {
//                                        matchCon = false;
//                                        break;
//                                    } else {
//                                        matchCon = true;
//                                    }
//                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            // handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void sendMsg(TScene tScene) {
        try {
            // root
//            if (tScene.getLicense() != null) {
//                TUser root = UserBusiness.queryUserByLicense(tScene
//                        .getLicense());
//                if (root != null) {
//                    if (tScene.getMsgAlter() == 2 || tScene.getMsgAlter() == 3) {
//                        // app + msg alter
//
////                        if (root.getMsgAlter() == 1) {
////                            List<TUserPhone> tUserPhones = UserBusiness
////                                    .queryUserPhone(root.getUserId());
////                            if (!tUserPhones.isEmpty()) {
////                                MsgService.getInstance().sendAlter(
////                                        tScene.getSceneName(), tUserPhones);
////                                tScene.setAlter_need(0);
////                            }
////                        }
////                        if (tScene.getMsgAlter() == 3) {
////                            TKey tKey = OboxBusiness.queryKeyByID(tScene
////                                    .getLicense());
////                            if (tKey != null) {
////                                log.info("====before push====");
////                                JPushService.sendAlter(tScene.getSceneName(),
////                                        tKey.getLicense(), null);
////                            }
////                        }
//                    } else if (tScene.getMsgAlter() == 1) {
//                        // app alter
////                        TKey tKey = OboxBusiness.queryKeyByID(tScene
////                                .getLicense());
////                        if (tKey != null) {
////                            log.info("====before push====");
////                            JPushService.sendAlter(tScene.getSceneName(),
////                                    tKey.getLicense(), null);
////                        }
//                    }
//
//                }
//            }

            // admin/guest
            List<TUserScene> tUserScenes = userSceneService.getUserSceneBySceneNum(tScene.getSceneNumber());
//            List<TUserScene> tUserScenes = SceneBusiness
//                    .queryUserSceneBySceneNumber(tScene.getSceneNumber());
            if (!tUserScenes.isEmpty()) {
                for (TUserScene tUserScene : tUserScenes) {
                    TUser user = userService.getUserByUserId(tUserScene
                            .getUserId());
//                    TUser user = UserBusiness.queryUserById(tUserScene
//                            .getUserId());

                    if (user != null) {
                        if (tScene.getMsgAlter() == 2
                                || tScene.getMsgAlter() == 3) {
                            // msg alter
//                            if (user.getMsgAlter() == 1) {
//                                // send phone msg
//                                List<TUserPhone> tUserPhones = UserBusiness
//                                        .queryUserPhone(user.getUserId());
//                                if (!tUserPhones.isEmpty()) {
//                                    MsgService.getInstance().sendAlter(
//                                            tScene.getSceneName(), tUserPhones);
//                                    tScene.setAlter_need(0);
//                                }
//                            }
                            if (tScene.getMsgAlter() == 3) {
                                // app alter
                                log.info("====before push====");
                                JPushService.sendAlter(tScene.getSceneName(),
                                        user.getUserName().toLowerCase(), null);
                            }

                        } else if (tScene.getMsgAlter() == 1) {
                            // app alter
                            log.info("====before push====");
                            JPushService.sendAlter(tScene.getSceneName(), user
                                    .getUserName().toLowerCase(), null);
                        }

                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
