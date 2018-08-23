package com.bright.apollo.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.bright.apollo.bean.Message;
import com.bright.apollo.cache.AliDevCache;
import com.bright.apollo.cache.CmdCache;
import com.bright.apollo.common.entity.TAliDevice;
import com.bright.apollo.common.entity.TAliDeviceUS;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.enums.AliRegionEnum;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.enums.Command;
import com.bright.apollo.service.AliDeviceService;
import com.bright.apollo.service.CMDMessageService;
import com.bright.apollo.service.DeviceChannelService;
import com.bright.apollo.service.DeviceService;
import com.bright.apollo.service.IntelligentFingerService;
import com.bright.apollo.service.MsgService;
import com.bright.apollo.service.OboxDeviceConfigService;
import com.bright.apollo.service.OboxService;
import com.bright.apollo.service.SceneActionService;
import com.bright.apollo.service.SceneConditionService;
import com.bright.apollo.service.SceneService;
import com.bright.apollo.service.TopicServer;
import com.bright.apollo.service.UserDeviceService;
import com.bright.apollo.service.UserOboxService;
import com.bright.apollo.service.UserOperationService;
import com.bright.apollo.service.UserSceneService;
import com.bright.apollo.service.UserService;
import com.bright.apollo.session.ClientSession;
import com.bright.apollo.session.PushThreadPool;
import com.bright.apollo.session.SceneActionThreadPool;
import com.bright.apollo.session.SessionManager;
import com.bright.apollo.tool.ByteHelper;
import com.bright.apollo.tool.EncDecHelper;
import com.bright.apollo.util.FingerUtil;
import com.zz.common.util.StringUtils;

@Component
public class CMDHandlerManager {
	private static final Logger logger = LoggerFactory.getLogger(CMDHandlerManager.class);

    @Autowired
    @Lazy
    private TopicServer topServer;

    @Autowired
    private UserOperationService userOperationService;
    
    @Autowired
    private EncDecHelper helper;
    
    @Autowired
    private AliDevCache aliDevCache;

    @Autowired
    private UserService userService;
    
    @Autowired
    private AliDeviceService aliDeviceService;

    @Autowired
    private OboxService oboxService;

    @Autowired
    private PushThreadPool pushThreadPool;
    @Autowired
    private SceneService sceneService;
    
    @Autowired
    private SceneActionThreadPool sceneActionThreadPool;
    
    @Autowired
    private SceneConditionService sceneConditionService;

    @Autowired
    private UserSceneService userSceneService;

    @Autowired
    private SceneActionService sceneActionService;

    @Autowired
    private OboxDeviceConfigService oboxDeviceConfigService;
    @Autowired
    private UserDeviceService userDeviceService;
    @Autowired
    private DeviceChannelService deviceChannelService;
    @Autowired
    private UserOboxService userOboxService;
    @Autowired
    private CMDMessageService cmdMessageService;
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private CmdCache cmdCache;
    @Autowired
    private IntelligentFingerService intelligentFingerService;
    @Autowired
    private FingerUtil fingerUtil;
    
    @Autowired
    private MsgService msgService;
    private static Map<String, BasicHandler> cmdHandlers = new HashMap<String, BasicHandler>();

    static {
        /**
         * version update
         */
        cmdHandlers.put(Command.VERSION.getValue(), new VersionCMDHandler());

        /**
         * obox info set
         */
        cmdHandlers.put(Command.OBOXINFO.getValue(), new OBOXInfoCMDHanlder());

        /**
         * heart beat
         */
//		cmdHandlers.put(Command.HEARTBEAT.getValue(), new HeartBeatCMDHandler());

        /**
         * signal intensity for node channel
         */
        cmdHandlers.put(Command.CHANNEL.getValue(), new NodeChannelHandler());

        /**
         * control PW set
         */
        cmdHandlers.put(Command.CONTROLPW.getValue(), new ControlPWCMDHandler());

        /**
         * control PW reset not transfer
         */
//        cmdHandlers.put(Command.CONTROLPWRESET.getValue(), new ControlPWResetCMDHandler());

        /**
         * 处理obox搜索返回结果逻辑
         */
        cmdHandlers.put(Command.SEARCH_RESULT.getValue(), new SearchResultHandler());
        /**
         * 处理传感器报警业务逻辑
         */
        cmdHandlers.put(Command.SENSOR.getValue(), new SensorCMDHandler());

        cmdHandlers.put(Command.DRONEHEARTBEAT.getValue(), new DroneHeartBeatHandler());

        /**
         * release all device
         */
        cmdHandlers.put(Command.RELEASE.getValue(), new ReleaseCMDHandler());

        cmdHandlers.put(Command.NODERELEASE.getValue(), new NodeReleaseCMDHandler());

        /**
         * node change
         */
        cmdHandlers.put(Command.NODECHANGE.getValue(), new UpdateNodeNameCMDHandler());

        /**
         * group change
         */
//        cmdHandlers.put(Command.GROUPCHANGE.getValue(), new GroupCMDHandler());

        /**
         * node status
         */
        cmdHandlers.put(Command.NODESTATUS.getValue(), new NodeStatusCMDHandler());
        /**
         * scene set
         */
        cmdHandlers.put(Command.SCENE.getValue(), new SceneCMDHandler());

        cmdHandlers.put(Command.SETSTATUS.getValue(), new SetStatusHandler());

        cmdHandlers.put(Command.SETRELEASE.getValue(), new SetReleaseHandler());

        cmdHandlers.put(Command.SEARCH.getValue(), new SetSearchHandler());

        cmdHandlers.put(Command.NODESET.getValue(), new SetNodeHandler());

        cmdHandlers.put(Command.REMOVEOBOX.getValue(), new RemoveOBOXHandler());

        cmdHandlers.put(Command.REMOVEOBOXRESP.getValue(), new DeleteOBOXHandler());

        cmdHandlers.put(Command.SETCHANNEL.getValue(), new SetChannelHandler());

//        cmdHandlers.put(Command.REMOTERCHANNEL.getValue(), new RemoterChannelHandler());
//
//        cmdHandlers.put(Command.REMOTERBUTTON.getValue(), new RemoterButtonHandler());

        cmdHandlers.put(Command.GETSTATUS.getValue(), new GetRealStatusHandler());

//        cmdHandlers.put(Command.ERROR.getValue(), new ErrorHandler());

        cmdHandlers.put(Command.FILTER.getValue(), new FilterCMDHandler());

//        cmdHandlers.put(Command.IRUP.getValue(), new IRUploadHandler());//红外上传学习码
        
        cmdHandlers.put(Command.TIMEREPORT.getValue(), new TimeReportHandler());
		cmdHandlers.put(Command.FINGEREMOTE.getValue(), new FingerRemoteHandler());
		//远程开锁
		//cmdHandlers.put(Command.REMOTEOPEN.getValue(), new RemoteOpenHandler());

    }

    public static BasicHandler getCMDHandler(Command cmd) {
        return cmdHandlers.get(cmd);
    }
    @Async
    public void processTopic(String ProductKey,String DeviceName,String inMsg){
        try {
            logger.info("======topic msg=====:key:"+ProductKey+" device:"+DeviceName+" payload:"+inMsg);
            if (inMsg.length() != 136) {
                return;
            }

            ClientSession client = new ClientSession();
// 			client.setUid(DeviceName);
//            String obox_serial_id = aliDevCache.getOboxSerialId(ProductKey, DeviceName);
            String obox_serial_id = aliDevCache.getOboxSerialId(ProductKey, DeviceName);
            if (StringUtils.isEmpty(obox_serial_id)) {
                //cashe time out or first time login
                //different table query by region
                TAliDevice tAliDevice = aliDeviceService.getAliDeviceByProductKeyAndDeviceName(ProductKey,DeviceName);
//                TAliDevice tAliDevice = AliDevBusiness.queryAliDevByName(ProductKey, DeviceName);
                if (tAliDevice != null) {
                    if (!tAliDevice.getOboxSerialId().equals("available")) {
                        client.setUid(tAliDevice.getOboxSerialId());
                        aliDevCache.saveDevInfo(tAliDevice.getProductKey(), tAliDevice.getOboxSerialId(), tAliDevice.getDeviceName(),AliRegionEnum.SOURTHCHINA);
                    }
                }else{
                    TAliDeviceUS tAliDeviceUS = aliDeviceService.getAliUSDeviceByProductKeyAndDeviceName(ProductKey, DeviceName);
//                    TAliDeviceUS tAliDeviceUS = AliDevBusiness.queryAliDevUSByName(ProductKey, DeviceName);
                    if (!tAliDeviceUS.getDeviceSerialId().equals("available")) {
                        client.setUid(tAliDeviceUS.getDeviceSerialId());
                        aliDevCache.saveDevInfo(tAliDeviceUS.getProductKey(), tAliDeviceUS.getDeviceSerialId(), tAliDeviceUS.getDeviceName(),AliRegionEnum.AMERICA);
                    }else{
                        return;
                    }

                }
            }else {
                client.setUid(obox_serial_id);
            }
            Message<String> msg = new Message<String>();
            msg.setHeader(inMsg.substring(0, 8));
            msg.setDecodeData(inMsg.substring(8));
            if (msg.getDecodeData().length() != 128 && msg.getDecodeData().length() != 32) {
                return;
            }
            msg.setCmd(msg.getDecodeData().substring(8, 12)); // 命令
            msg.setLength(Integer.parseInt(msg.getDecodeData().substring(12, 14), 16));
            msg.setData(msg.getDecodeData().substring(14, 14 + 54 * 2));

            String cmd = msg.getCmd();
            logger.info("===msg data:"+msg.getData());
            if ("a1".equals(cmd) || "b1".equals(cmd) || "b4".equals(cmd)) {
//				if (clientSession.getStatus() == Session.STATUS_AUTHENTICATED) {
//					CMDMsgCache.saveReply(clientSession.getUid(), cmd,
//							msg.getData());
//				}
//
//				clientSession.setAvaibleByte(0);
            } else if ("a010".equals(cmd)
                    || Command.REMOVEOBOXRESP.getValue().equals(cmd)) {
//				clientSession.setAvaibleByte(~0);
//				if (clientSession.getStatus() == Session.STATUS_AUTHENTICATED) {
//					CMDMsgCache.saveReply(clientSession.getUid(), cmd,
//							msg.getData());
//				}
            } else {

                if (Command.HEARTBEAT.getValue().equals(cmd)) {
                	logger.info("==="+Command.HEARTBEAT.getValue()+"===");
                    if (msg.getData().substring(0, 2).equals("01")){
                        String random_number = msg.getData().substring(2, 34);
                        String serialId = msg.getData().substring(34, 44);
                        String oboxVersion = msg.getData().substring(44, 60);
                        String oboxName = ByteHelper.fromHexAscii(msg.getData().substring(62, 62+2*Integer.parseInt(msg.getData().substring(60, 62), 16)));
                        client.setUid(serialId);

                        AliRegionEnum enum1= AliRegionEnum.SOURTHCHINA;
                        TAliDevice tAliDevice = aliDeviceService.getAliDeviceByProductKeyAndDeviceName(ProductKey, DeviceName);
//                        TAliDevice tAliDevice = AliDevBusiness.queryAliDevByName(ProductKey, DeviceName);
                        if (tAliDevice != null) {
                        	logger.info("===tAliDevice device name:"+tAliDevice.getDeviceName());
                            if (!tAliDevice.getOboxSerialId().equals(serialId)) {
                                TAliDevice tAliDevice2 = aliDeviceService.getAliDeviceBySerializeId(serialId);
//                                TAliDevice tAliDevice2 = AliDevBusiness.queryAliDevBySerial(serialId);
                                if (tAliDevice2 != null) {
                                    tAliDevice2.setOboxSerialId("available");
                                    aliDeviceService.updateAliDevice(tAliDevice2);
//                                    AliDevBusiness.updateAliDev(tAliDevice2);
                                }

                                tAliDevice.setOboxSerialId(serialId);
                                aliDeviceService.updateAliDevice(tAliDevice);
//                                AliDevBusiness.updateAliDev(tAliDevice);
                            }
                            aliDevCache.saveDevInfo(ProductKey, serialId, DeviceName,AliRegionEnum.SOURTHCHINA);
                        }else{
                            TAliDeviceUS TAliDeviceUS = aliDeviceService.getAliUSDeviceByProductKeyAndDeviceName(ProductKey, DeviceName);
//                            TAliDeviceUS TAliDeviceUS = AliDevBusiness.queryAliDevUSByName(ProductKey, DeviceName);
                            if (TAliDeviceUS != null) {
                                if (!TAliDeviceUS.getDeviceSerialId().equals(serialId)) {
                                    TAliDeviceUS tAliDeviceUS2 = aliDeviceService.getAliUSDeviceBySerializeId(serialId);
//                                    TAliDeviceUS tAliDeviceUS2 = AliDevBusiness.queryAliDevUSBySerial(serialId);
                                    if (tAliDeviceUS2 != null) {
                                        tAliDeviceUS2.setDeviceSerialId("available");
                                        aliDeviceService.updateAliUSDevice(tAliDeviceUS2);
//                                        AliDevBusiness.updateAliDevUS(tAliDeviceUS2);
                                    }

                                    TAliDeviceUS.setDeviceSerialId(serialId);
                                    aliDeviceService.updateAliUSDevice(TAliDeviceUS);
//                                    AliDevBusiness.updateAliDevUS(TAliDeviceUS);
                                }
                                enum1 = AliRegionEnum.AMERICA;
                                aliDevCache.saveDevInfo(ProductKey, serialId, DeviceName,AliRegionEnum.AMERICA);
                            }
                        }
                        TObox dbObox = oboxService.queryOboxsByOboxSerialId(serialId);
//                        TObox dbObox = OboxBusiness.queryOboxsByOboxSerialId(serialId);
//						TKey tKey = OboxBusiness.queryKeyByProductKey(ProductKey);
                        if (dbObox == null) {
                        	logger.info("===add obox===");
                            dbObox = new TObox();
//							if (tKey != null) {
//								dbObox.setLicense(tKey.getId());
//							}
                            dbObox.setOboxVersion(oboxVersion);
                            dbObox.setOboxName(oboxName);
                            dbObox.setOboxSerialId(serialId);
                            dbObox.setOboxStatus((byte)1);
//                            dbObox.setOboxPwd("88888888");
//                            dbObox.setOboxActivate(0);
//							dbObox.setOboxIP(ip[0].substring(1));
                            dbObox.setOboxIp("0.0.0.0");
                            oboxService.addObox(dbObox);
//                            OboxBusiness.addObox(dbObox);
                            dbObox = oboxService.queryOboxsByOboxSerialId(serialId);
                        }else {
//							if (tKey != null) {
//								dbObox.setLicense(tKey.getId());
//							}
                            dbObox.setOboxStatus((byte)1);
                            dbObox.setOboxName(oboxName);
//                            dbObox.setOboxActivate(0);
                            dbObox.setOboxVersion(oboxVersion);
//							dbObox.setOboxIP(ip[0].substring(1));
                            oboxService.update(dbObox);
//                            OboxBusiness.updateObox(dbObox);
                        }

                        //send server addr to obox
                        byte [] body = new byte [22];
                        body [0] = 1;
//						if (tKey == null) {
//							tKey = OboxBusiness.queryKeyByCompany("OBHOME");
//						}
//                        TKey tKey = OboxBusiness.queryKeyByCompany("OB");
//                        byte [] serverSerialByte = ByteHelper.hexStringToBytes(tKey.getServerAddr());
//                        byte [] randomByte = ByteHelper.hexStringToBytes(random_number);
//                        System.arraycopy(randomByte, 0, body, 1, randomByte.length);
//                        System.arraycopy(serverSerialByte, 0, body, 17, serverSerialByte.length);

                        topServer.pubTopic(CMDEnum.time, body, ProductKey,DeviceName,enum1);
                    }
                }

                BasicHandler handler = cmdHandlers.get(cmd);
                if (handler == null) {
                    logger.error("not exist this cmd : " + cmd);
                    return;
                }

                // TODO only save the reply except the upload data
                if (Command.NODECHANGE.getValue().equals(cmd)
                        || Command.GROUPCHANGE.getValue().equals(cmd)
                        || Command.CONTROLPW.getValue().equals(cmd)
                        || Command.SCENE.getValue().equals(cmd)
                        || Command.RELEASE.getValue().equals(cmd)
                        || Command.NODESTATUS.getValue().equals(cmd)
                        || Command.VERSION.getValue().equals(cmd)
                        || Command.REMOVEOBOXRESP.getValue().equals(cmd)
                        || Command.HEARTBEAT.getValue().equals(cmd)
                        || Command.GETSTATUS.getValue().equals(cmd)
                        || Command.ERROR.getValue().equals(cmd)
                        ) {
                    //CMDMsgCache.saveReply(clientSession.getUid(), cmd,
                    //		msg.getData());
                } else {
                    if (Command.CHANNEL.getValue().equals(cmd)) {
                        if (msg.getData().substring(0, 2).equals("01")
                                || msg.getData().substring(0, 2)
                                .equals("02")) {
                            //CMDMsgCache.saveReply(clientSession.getUid(),
                            //		cmd, msg.getData());
                        }
                    }
                    if (Command.SEARCH_RESULT.getValue().equals(cmd)) {
                        if (msg.getData().substring(60, 62).equals("00")) {
                            //CMDMsgCache.saveReply(clientSession.getUid(),
                            //		cmd, msg.getData());
                        } else {
                            int addr = Integer.parseInt(msg.getData()
                                    .substring(60, 62), 16);
                            //CMDMsgCache.saveSearch(clientSession.getUid(),
                            //		String.valueOf(addr), msg.getData());
                        }
                    }
                }

                //inject the obj to the handler
                if(handler.getOboxService()==null){
                	handler.setMsgService(msgService);
                	handler.setTopicServer(topServer);
                	handler.setFingerUtil(fingerUtil);
                	handler.setIntelligentFingerService(intelligentFingerService);
                	handler.setUserOperationService(userOperationService);
                	handler.setUserService(userService);
                	handler.setSceneActionThreadPool(sceneActionThreadPool);
                	handler.setDeviceChannelService(deviceChannelService);
                	handler.setOboxDeviceConfigService(oboxDeviceConfigService);
                	handler.setOboxService(oboxService);
                	handler.setSceneActionService(sceneActionService);
                	handler.setSceneConditionService(sceneConditionService);
                	handler.setSceneService(sceneService);
                	handler.setUserDeviceService(userDeviceService);
                	handler.setUserOboxService(userOboxService);
                	handler.setUserSceneService(userSceneService);
                	handler.setCmdMessageService(cmdMessageService);
                	handler.setSessionManager(sessionManager);
                	handler.setCmdCache(cmdCache);
                }
                handler.process(client, msg);
                
//				Message<String> replyMsg =
//				if (replyMsg != null) {
//
//					clientSession.process(replyMsg);
//				}

                //TODO
                //send the message from obox to app
                if(msg!=null){
                    BasicHandler basicHandler = cmdHandlers.get(Command.FILTER.getValue());
                    if(basicHandler!=null)

                        pushThreadPool.pushCmd(basicHandler, client, msg);
                    else
                        logger.info("====FilterCMDHandler error===");
                }
                else
                    logger.info("====FilterCMDHandler error===");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
