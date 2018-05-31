package com.bright.apollo.socket;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.Message;
import com.bright.apollo.common.entity.TAliDevice;
import com.bright.apollo.common.entity.TAliDeviceUS;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.enums.ALIDevTypeEnum;
import com.bright.apollo.enums.AliRegionEnum;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.handler.CMDHandlerManager;
import com.bright.apollo.handler.CommandHandler;
import com.bright.apollo.service.AliDeviceService;
import com.bright.apollo.service.MsgReceiver;
import com.bright.apollo.service.OboxService;
import com.bright.apollo.service.TopicServer;
import com.bright.apollo.service.impl.AliDeviceServiceImpl;
import com.bright.apollo.service.impl.OboxServiceImpl;
import com.bright.apollo.tool.ByteHelper;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Component
public class MNService {

    Logger logger = Logger.getLogger(MNService.class);
    public static MNService instance;

    private static final String accessKeyId ="LTAImm6aizjagsfp";
    private static final String accessKeySecret ="zNdZ9RuwSU7RG2Lkoon9i2hbVx3gsm";
    private static final String endPoint ="http://1558412029548413.mns.cn-shanghai.aliyuncs.com/";
    private static final String queueStr = "aliyun-iot-"+ ALIDevTypeEnum.OBOX.getSouthChinaName();

    private static final String AmericaEndPoint ="http://1558412029548413.mns.us-west-1.aliyuncs.com/";
    private static final String AmericaqueueStr = "aliyun-iot-queue";

    private static MNSClient client;
    //private static MNSClient usclient;
    private static Gson gson ;

    @Autowired
    private AliDeviceService aliDeviceService;
    @Autowired
    private OboxService oboxService;
    @Autowired
    private TopicServer topicService;
    @Autowired
    private CMDHandlerManager cmdHandlerManager;
    @Autowired
    private CommandHandler commandHandler;


    public MNService() {
        // TODO Auto-generated constructor stub
            logger.info("------ MNService init ------");
            CloudAccount account = new CloudAccount(accessKeyId, accessKeySecret, endPoint);
            client = account.getMNSClient();

//        if (eAliRegionEnum.equals(AliRegionEnum.SOURTHCHINA)) {
//            CloudAccount account = new CloudAccount(accessKeyId, accessKeySecret, endPoint);
//            client = account.getMNSClient();
//        }else {
//            CloudAccount account = new CloudAccount(accessKeyId, accessKeySecret, AmericaEndPoint);
//            usclient = account.getMNSClient();
//        }

        gson = new Gson();
    }

    public static MNService getInstance(AliRegionEnum eAliRegionEnum){
        if (instance == null){
//            instance = new MNService(eAliRegionEnum);
        }
        return instance;
    }

    class workAction implements Runnable{

        private int workerId;
        private AliRegionEnum eAliRegionEnum;
        private String qStr;

        public workAction(int workerId,AliRegionEnum eAliRegionEnum,String qStr){
            logger.info("------ workAction init ------");
            this.workerId = workerId;
            this.eAliRegionEnum =eAliRegionEnum;
            this.qStr = qStr;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            logger.info("------ workAction thread run  ------");
            WorkerFunc(workerId, eAliRegionEnum, qStr);
        }
    }

    public void WorkerFunc(int workerId,AliRegionEnum eAliRegionEnum,String qStr)
    {
        MsgReceiver receiver = new MsgReceiver(workerId, client, "aliyun-iot-"+qStr);
        logger.info("MNService.WorkerFunc() + " + "aliyun-iot-"+qStr);
//        if (eAliRegionEnum.equals(AliRegionEnum.SOURTHCHINA)) {
//            receiver = new MsgReceiver(workerId, client, "aliyun-iot-"+qStr);
//        }else{
//            receiver = new MsgReceiver(workerId, usclient, "aliyun-iot-"+qStr);
//        }
//        AliDeviceService aliDeviceService = new AliDeviceServiceImpl();
//        OboxService oboxService = new OboxServiceImpl();
//        TopicServer topicService = new TopicServer();
//        CMDHandlerManager cmdHandlerManager = new CMDHandlerManager();
//        CommandHandler commandHandler = new CommandHandler();
        while (true) {
            Message message = receiver.receiveMessage();
            logger.info("Thread" + workerId + " GOT ONE MESSAGE! " + message.getMessageId());
            if (message != null) {
                try {
                    String body = message.getMessageBody();
                    LinkedTreeMap map = gson.fromJson(body, LinkedTreeMap.class);
                    String messageType = (String)map.get("messagetype");
                    String payload = (String)map.get("payload");
                    byte[] contentBytes = Base64.decodeBase64(payload);
                    String aString = new String(contentBytes, "utf-8");

                    if (messageType.equals("status")) {
                        //update device status

                        JSONObject object = new JSONObject(aString);
                        System.out.println(" productKey ------ "+object.get("productKey"));
                        System.out.println(" deviceName ------ "+object.get("deviceName"));
//        	    		String obox_serial_id = object.getString("deviceName");
                        if (object.getString("status").equals("offline")) {
                            if(ALIDevTypeEnum.getTypebyValue(object.getString("productKey")).equals(ALIDevTypeEnum.OBOX)){
                                TAliDevice tAliDevice =  aliDeviceService.getAliDeviceByProductKeyAndDeviceName(object.getString("productKey"),object.getString("deviceName"));
//                                TAliDevice tAliDevice = AliDevBusiness.queryAliDevByName(object.getString("productKey"),object.getString("deviceName"));
                                if (tAliDevice != null) {
                                    tAliDevice.setOffline(0);
                                    aliDeviceService.updateAliDevice(tAliDevice);
//                                    AliDevBusiness.updateAliDev(tAliDevice);
                                    TObox dbObox = oboxService.queryOboxsByOboxSerialId(tAliDevice.getOboxSerialId());
//                                    TObox dbObox = OboxBusiness.queryOboxsByOboxSerialId(tAliDevice.getOboxSerialId());
                                    if (dbObox != null) {
//            	    					AliDevCache.DelDevInfo(tAliDevice.getOboxSerialId());

                                        dbObox.setOboxStatus(0);
//                                        dbObox.setOboxIP("0.0.0.0");
                                        oboxService.update(dbObox);
                                    }
                                }else {
                                    TAliDeviceUS tAliDeviceUS = aliDeviceService.getAliUSDeviceByProductKeyAndDeviceName(object.getString("productKey"),object.getString("deviceName"));
//                                    TAliDeviceUS tAliDeviceUS = AliDevBusiness.queryAliDevUSByName(object.getString("productKey"),object.getString("deviceName"));
                                    if(tAliDeviceUS != null){
                                        tAliDeviceUS.setOffline(0);
                                        aliDeviceService.updateAliUSDevice(tAliDeviceUS);
                                        TObox dbObox = oboxService.queryOboxsByOboxSerialId(tAliDeviceUS.getDeviceSerialId());
//                                        TObox dbObox = OboxBusiness.queryOboxsByOboxSerialId(tAliDeviceUS.getDeviceSerialId());
                                        if (dbObox != null) {
//                	    					AliDevCache.DelDevInfo(tAliDeviceUS.getDeviceSerialId());

                                            dbObox.setOboxStatus(0);
//                                            dbObox.setOboxIP("0.0.0.0");
                                            oboxService.update(dbObox);
                                        }
                                    }
                                }
//            	    			else {
//    								tAliDevice = new TAliDevice();
//    								tAliDevice.setDeviceName(object.getString("deviceName"));
//    								tAliDevice.setProductKey(object.getString("productKey"));
//    								AliDevBusiness.addAliDev(tAliDevice);
//    							}
                            }else{

                                TAliDeviceUS tAliDeviceUS = aliDeviceService.getAliUSDeviceByProductKeyAndDeviceName(object.getString("productKey"),object.getString("deviceName"));
//                                TAliDeviceUS tAliDeviceUS = AliDevBusiness.queryAliDevUSByName(object.getString("productKey"),object.getString("deviceName"));
                                if(tAliDeviceUS != null){
                                    tAliDeviceUS.setOffline(1);
                                    aliDeviceService.updateAliUSDevice(tAliDeviceUS);
                                }else {
                                    TAliDevice tAliDevice =  aliDeviceService.getAliDeviceByProductKeyAndDeviceName(object.getString("productKey"),object.getString("deviceName"));
//                                    TAliDevice tAliDevice = AliDevBusiness.queryAliDevByName(object.getString("productKey"),object.getString("deviceName"));
                                    if (tAliDevice != null) {

                                        tAliDevice.setOffline(1);
                                        aliDeviceService.updateAliDevice(tAliDevice);
                                    }
                                }
                            }

                        }

                        if (object.getString("status").equals("online")){
                            TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
                            Calendar date = Calendar.getInstance(tz);

                            byte [] body1 = new byte [9];
                            body1 [0] = 2;
                            body1 [1] = 8; //时区
                            body1 [2] = (byte)(date.get(Calendar.YEAR) % 2000); // 年
                            body1 [3] = (byte)(date.get(Calendar.MONTH)+1); // 月
                            body1 [4] = (byte)date.get(Calendar.DATE); // 日
                            body1 [5] = (byte)(date.get(Calendar.DAY_OF_WEEK)); // 星期几
                            body1 [6] = (byte)date.get(Calendar.HOUR_OF_DAY); //小时
                            body1 [7] = (byte)date.get(Calendar.MINUTE); //分钟
                            body1 [8] = (byte)date.get(Calendar.SECOND); //秒

                            if(ALIDevTypeEnum.getTypebyValue(object.getString("productKey")).equals(ALIDevTypeEnum.OBOX)){
                                topicService.pubTopic(CMDEnum.time, body1, object.getString("productKey"),object.getString("deviceName"),eAliRegionEnum);
                            }else{
                                String time = ByteHelper.bytesToHexString(body1);
                                JSONObject object2 = new JSONObject();
                                object2.put("time", time.substring(2));
                                object2.put("command", "time_sync");
                                topicService.pubTopicDev(object2, object.getString("productKey"), object.getString("deviceName"), eAliRegionEnum);

                                TAliDeviceUS tAliDeviceUS = aliDeviceService.getAliUSDeviceByProductKeyAndDeviceName(object.getString("productKey"),object.getString("deviceName"));
//                                TAliDeviceUS tAliDeviceUS = AliDevBusiness.queryAliDevUSByName(object.getString("productKey"),object.getString("deviceName"));
                                if(tAliDeviceUS != null){
                                    tAliDeviceUS.setOffline(0);
                                    aliDeviceService.updateAliUSDevice(tAliDeviceUS);
//                                    AliDevBusiness.updateAliDevUS(tAliDeviceUS);
                                }else {
                                    TAliDevice tAliDevice =  aliDeviceService.getAliDeviceByProductKeyAndDeviceName(object.getString("productKey"),object.getString("deviceName"));
//                                    TAliDevice tAliDevice = AliDevBusiness.queryAliDevByName(object.getString("productKey"),object.getString("deviceName"));
                                    if (tAliDevice != null) {
                                        tAliDevice.setOffline(0);
                                        aliDeviceService.updateAliDevice(tAliDevice);
                                    }
                                }
                            }

                        }

                        logger.info("status PopMessage Body: "+ aString); //获取原始消息
                    }else if (messageType.equals("upload")) {
                        //upload topic
                        String topic = (String)map.get("topic");
                        String [] topicArray = topic.split("/");
                        logger.info("topic:"+topic + " PopMessage Body: " + aString); //获取原始消息
                        if(ALIDevTypeEnum.getTypebyValue(topicArray[1]).equals(ALIDevTypeEnum.OBOX)){
                            cmdHandlerManager.processTopic(topicArray[1],topicArray[2],aString);
                        }else {
                            commandHandler.process(topicArray[1],topicArray[2],aString);
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    logger.info("------ MNService WorkerFunc Exception ------"+e.getMessage());
                }

            }
            client.getQueueRef("aliyun-iot-"+qStr).deleteMessage(message.getReceiptHandle());
//            if (eAliRegionEnum.equals(AliRegionEnum.SOURTHCHINA)) {
//                client.getQueueRef("aliyun-iot-"+qStr).deleteMessage(message.getReceiptHandle());
//            }else{
//                usclient.getQueueRef("aliyun-iot-"+qStr).deleteMessage(message.getReceiptHandle());
//            }

        }
    }


    public void getMNS(){
        //CloudQueue queue = client.getQueueRef(queueStr);
//        int theradId = 1;
        logger.info("------ MNService getMNS method  ------");
        List<Thread> list = new ArrayList<Thread>();
        for (int i = 1; i < 4; i++) {
            Thread thread = new Thread(new workAction(i, AliRegionEnum.SOURTHCHINA, ALIDevTypeEnum.OBOX.getSouthChinaName()));
            thread.start();
            list.add(thread);
        }
//        theradId ++;

//        for (ALIDevTypeEnum typeEnum : ALIDevTypeEnum.values()) {
//            for (int i = 1; i < 4; i++) {
//                Thread thread;
//                if (eAliRegionEnum.equals(AliRegionEnum.AMERICA)) {
//                    thread = new Thread(new workAction(theradId, eAliRegionEnum, typeEnum.getAmericaName()));
//                }else {
//                    thread = new Thread(new workAction(theradId, eAliRegionEnum, typeEnum.getSouthChinaName()));
//                }
//                thread.start();
//                list.add(thread);
//            }
//            theradId ++;
//        }

        for (Thread thread : list) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
