package com.bright.apollo.session;

import com.alibaba.fastjson.JSONArray;
import com.bright.apollo.cache.AliDevCache;
import com.bright.apollo.common.entity.*;
import com.bright.apollo.enums.AliRegionEnum;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.enums.NodeTypeEnum;
import com.bright.apollo.service.*;
import com.bright.apollo.tool.ByteHelper;
import com.zz.common.util.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Component
public class SceneActionThreadPool {
    private static ExecutorService executor;

    @Autowired
    private AliDeviceService aliDeviceService;

    @Autowired
    private AliDevCache aliDevCache;

    @Autowired
    @Lazy
    private TopicServer topicServer;

    @Autowired
    private SceneActionService sceneActionService;

    @Autowired
    private OboxDeviceConfigService oboxDeviceConfigService;

   
 

	private final Logger log = Logger
            .getLogger(SceneActionThreadPool.class);

    public SceneActionThreadPool() {
        executor = Executors
                .newFixedThreadPool(5);
    }

    public void addTimerAction(Integer timerId){
        log.info("===SceneActionThreadPool timerAction:" + timerId);
        executor.submit(new timerAction(timerId));
    }

    class timerAction implements Runnable{


        private Integer timerId;

        public timerAction(Integer timerId){
            this.timerId = timerId;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {

                TAliDevTimer tAliDevTimer = aliDeviceService.getAliDevTimerByTimerId(timerId);
                if (tAliDevTimer == null) {
                    return;
                }
                String productKey = aliDevCache.getProductKey(tAliDevTimer.getDeviceSerialId());
                String deviceName = aliDevCache.getProductKey(tAliDevTimer.getDeviceSerialId());
                String region = aliDevCache.getProductRegion(tAliDevTimer.getDeviceSerialId());
                AliRegionEnum eAliRegionEnum = AliRegionEnum.SOURTHCHINA;
                if (StringUtils.isEmpty(productKey)) {
                    TAliDevice tAliDevice = aliDeviceService.getAliDeviceBySerializeId(tAliDevTimer.getDeviceSerialId());
                    if (tAliDevice != null) {
                        productKey = tAliDevice.getProductKey();
                        deviceName = tAliDevice.getDeviceName();
                        region = AliRegionEnum.SOURTHCHINA.name();
                        aliDevCache.saveDevInfo(productKey, tAliDevTimer.getDeviceSerialId(), deviceName,AliRegionEnum.SOURTHCHINA);
                    }else {
                        TAliDeviceUS tAliDeviceUS = aliDeviceService.getAliUSDeviceBySerializeId(tAliDevTimer.getDeviceSerialId());
                        if (tAliDeviceUS == null) {
                            return;
                        }
                        productKey = tAliDeviceUS.getProductKey();
                        deviceName = tAliDeviceUS.getDeviceName();
                        region = AliRegionEnum.AMERICA.name();
                        eAliRegionEnum = AliRegionEnum.AMERICA;
                        aliDevCache.saveDevInfo(productKey, tAliDevTimer.getDeviceSerialId(), deviceName,AliRegionEnum.AMERICA);
                    }
                }else {
                    eAliRegionEnum = AliRegionEnum.getRegion(region);
                }


                if (tAliDevTimer != null) {
                    if (!StringUtils.isEmpty(tAliDevTimer.getTimerValue())) {
                        JSONObject sendObject = new JSONObject();
                        sendObject.put("command", "set");
                        sendObject.put("value", JSONArray.parseArray(tAliDevTimer.getTimerValue()));
                        topicServer.pubTopicDev(sendObject, productKey, deviceName, eAliRegionEnum);

                        if (tAliDevTimer.getIsCountdown() !=0) {
                            aliDeviceService.deleteAliDevTimerBySerializeIdAndType(tAliDevTimer.getDeviceSerialId(), tAliDevTimer.getIsCountdown());
//                            AliDevBusiness.deleteAliDevTimerByType(tAliDevTimer.getDeviceSerialId(), tAliDevTimer.getIsCountdown());
                        }
                    }
                }

            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    public void addSceneAction(int sceneNumber) {
        //log.info("========SceneActionThreadPool  addSceneAction==========");
        log.info("===SceneActionThreadPool sceneNumber:" + sceneNumber);
        executor.submit(new setAction(sceneNumber));
    }

    class setAction implements Runnable {

        private int sceneNumber;

        private long startTime;

        private String urlString = null;

        private SessionManager sessionManager = SessionManager
                .getInstance();

        //private static MsgService msgService = MsgService.getInstance();

        public setAction(int sceneNumber) {

            this.sceneNumber = sceneNumber;
            this.startTime = new Date().getTime();
        }

        @Override
        public void run() {

            try {
                List<TSceneAction> tSceneActions = sceneActionService.getSceneActionBySceneNumber(sceneNumber);
//                List<TSceneAction> tSceneActions = SceneBusiness
//                        .querySceneActionsBySceneNumber(sceneNumber);
                for (TSceneAction tSceneAction : tSceneActions) {
                    if (tSceneAction.getNodeType().equals(
                            NodeTypeEnum.single.getValue())) {
                        TOboxDeviceConfig oboxDeviceConfig = oboxDeviceConfigService.getOboxDeviceConfigById(tSceneAction.getId());
//                        TOboxDeviceConfig oboxDeviceConfig = DeviceBusiness
//                                .queryDeviceConfigByID(tSceneAction
//                                        .getActionID());
                        if (oboxDeviceConfig != null) {
                            byte[] bodyBytes = new byte[16];
                            byte[] oboxSerildIdBytes = ByteHelper
                                    .hexStringToBytes(oboxDeviceConfig
                                            .getOboxSerialId());
                            System.arraycopy(oboxSerildIdBytes,
                                    0, bodyBytes, 0,
                                    oboxSerildIdBytes.length);
                            bodyBytes[5] = 0x00;
                            bodyBytes[6] = (byte) Integer
                                    .parseInt(oboxDeviceConfig
                                                    .getDeviceRfAddr(),
                                            16);
                            byte[] sBytes = ByteHelper
                                    .hexStringToBytes(tSceneAction
                                            .getAction());
                            System.arraycopy(sBytes, 0,
                                    bodyBytes, 7, sBytes.length);
//                            System.out.println("SceneActionThreadPool.setAction.run():"+tSceneAction
//                                    .getActionID());
//                            TopicService topicService = TopicService.getInstance();
                            if (topicServer != null) {
                                topicServer.request(CMDEnum.setting_node_status, bodyBytes, oboxDeviceConfig
                                        .getOboxSerialId());
                            }
                        }
                    }
//                    else if (tSceneAction.getNodeType().equals(
//                            NodeTypeEnum.group.getValue())) {
//                        TServerGroup tServerGroup = DeviceBusiness
//                                .querySererGroupById(tSceneAction
//                                        .getActionID());
//                        if (tServerGroup != null) {
//
//                            if (tServerGroup.getGroupStyle()
//                                    .equals("00")) {
//
//                                TServerOboxGroup tServerOboxGroup = DeviceBusiness
//                                        .queryOBOXGroupByServerID(tServerGroup
//                                                .getId());
//                                if (tServerOboxGroup != null) {
//                                    TObox tObox = OboxBusiness
//                                            .queryOboxsByOboxSerialId(tServerOboxGroup
//                                                    .getOboxSerialId());
//                                    if (tObox != null) {
//                                        byte[] stateBytes = ByteHelper
//                                                .hexStringToBytes(tSceneAction
//                                                        .getAction());
//                                        byte[] bodyBytes = new byte[7+stateBytes.length];
//                                        byte[] oboxSerialIdBytes = ByteHelper
//                                                .hexStringToBytes(tServerOboxGroup
//                                                        .getOboxSerialId());
//                                        System.arraycopy(
//                                                oboxSerialIdBytes,
//                                                0,
//                                                bodyBytes,
//                                                0,
//                                                oboxSerialIdBytes.length);
//                                        bodyBytes[5] = (byte) Integer
//                                                .parseInt(
//                                                        tServerOboxGroup
//                                                                .getGroupAddr(),
//                                                        16);
//                                        bodyBytes[6] = (byte) 0xff;
//
//
//                                        System.arraycopy(
//                                                stateBytes,
//                                                0,
//                                                bodyBytes,
//                                                7,
//                                                stateBytes.length);
//                                        TopicService topicService = TopicService.getInstance();
//                                        System.out.println("SceneActionThreadPool.setAction.run():"+tSceneAction
//                                                .getActionID());
//                                        topicService.request(CMDEnum.setting_node_status, bodyBytes, tObox
//                                                .getOboxSerialId());
//                                    }
//                                }
//                            } else {
//
//                                TKey tKey = OboxBusiness
//                                        .queryKeyByID(tServerGroup
//                                                .getLicense());
//                                if (tKey != null) {
//                                    List<TObox> tOboxs = OboxBusiness
//                                            .queryOboxByGroupId(tServerGroup
//                                                    .getId());
//                                    int count = 0;
//                                    for (TObox tObox : tOboxs) {
//                                        byte[] stateBytes = ByteHelper
//                                                .hexStringToBytes(tSceneAction
//                                                        .getAction());
//                                        byte[] bodyBytes = new byte[7+stateBytes.length];
//                                        byte[] oboxSerialIdBytes = ByteHelper
//                                                .hexStringToBytes(tKey
//                                                        .getServerAddr());
//                                        System.arraycopy(
//                                                oboxSerialIdBytes,
//                                                0,
//                                                bodyBytes,
//                                                0,
//                                                oboxSerialIdBytes.length);
//                                        byte[] addrBytes = ByteHelper
//                                                .hexStringToBytes(tServerGroup
//                                                        .getGroup_addr());
//                                        System.arraycopy(
//                                                addrBytes,
//                                                0,
//                                                bodyBytes,
//                                                5,
//                                                addrBytes.length);
//
//
//                                        System.arraycopy(
//                                                stateBytes,
//                                                0,
//                                                bodyBytes,
//                                                7,
//                                                stateBytes.length);
//                                        TopicService topicService = TopicService.getInstance();
//                                        System.out.println("SceneActionThreadPool.setAction.run():"+tSceneAction
//                                                .getActionID());
//                                        topicService.request(CMDEnum.setting_node_status, bodyBytes, tObox
//                                                .getOboxSerialId());
//
//                                    }
//                                }
//                            }
//                        }
//                    }
                }

//                TScene fScene = SceneBusiness.querySceneBySceneNumber(sceneNumber);//父场景
//                fScene.setSceneRun(0);
//                SceneBusiness.updateScene(fScene);


            } catch (Exception e) {

                e.printStackTrace();
//                try {
//                    TScene tScene = SceneBusiness
//                            .querySceneBySceneNumber(sceneNumber);
//                    if (tScene != null) {
//                        tScene.setSceneRun(0);
//                        SceneBusiness.updateScene(tScene);
//                    }
//                } catch (Exception e2) {
//
//                    e2.printStackTrace();
//                }

            }
        }
    }
}
