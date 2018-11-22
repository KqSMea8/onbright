package com.bright.apollo.handler;

import com.alibaba.fastjson.JSONArray;
import com.bright.apollo.bean.MatchRemoteControlResult;
import com.bright.apollo.cache.CmdCache;
import com.bright.apollo.common.entity.*;
import com.bright.apollo.service.PushService;
import com.bright.apollo.service.UserAliDevService;
import com.bright.apollo.service.YaoKongYunSend;
import com.bright.apollo.tool.ByteHelper;
import com.bright.apollo.tool.MD5;
import com.bright.apollo.util.IndexUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class IRUploadHandler extends AliBaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(IRUploadHandler.class);

    @Autowired
    private PushService pushservice;

    @Autowired
    private UserAliDevService userAliDevService;

    @Autowired
    private CmdCache cmdCache;

    @Autowired
    private YaoKongYunSend yaoKongYunSend;


    @Override
    public void process(String deviceSerialId, JSONObject object) {
        Map<String,Object> jsonObject = null;
        try{
//            TAliDeviceConfig tAliDeviceConfig = aliDeviceConfigService.getAliDeviceConfigBySerializeId(deviceSerialId);
//            if (tAliDeviceConfig != null) {
//                if (!StringUtils.isEmpty(object.getString("value"))) {
//                    tAliDeviceConfig.setState(object.getString("value"));
//                    aliDeviceConfigService.update(tAliDeviceConfig);
//                }
//                PushMessage pushMessage;
//                pushMessage=new PushMessage();
//                pushMessage.setType(PushMessageType.WIFI_TRANS.getValue());
//                pushMessage.setOnLine(true);
//
//                net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
//                jsonObject.put("deviceId", tAliDeviceConfig.getDeviceSerialId());
//                jsonObject.put("type", tAliDeviceConfig.getType());
//                jsonObject.put("name", tAliDeviceConfig.getName());
//                jsonObject.put("action", JSONArray.parseArray(tAliDeviceConfig.getAction()));
//                jsonObject.put("state", JSONArray.parseArray(tAliDeviceConfig.getState()));
//                logger.info("upload json  ====== "+jsonObject);
//                pushMessage.setData(jsonObject.toString());
//
//                pushMsg(deviceSerialId, pushMessage);
//            }
            //tudo 红外
            ali2IR(deviceSerialId,object);
//            topicServer.pubIRTopic(null,null,deviceSerialId,jsonObject);
        }catch (Exception e){
            e.printStackTrace();
//            jsonObject = new JSONObject();
//            try {
//                jsonObject.put("respCode","10001");
//                topicServer.pubIRTopic(null,null,deviceSerialId,jsonObject);
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }

        }

    }

    //红外方法
    private void ali2IR(String deviceSerialId, JSONObject object) throws Exception {

        logger.info(" ======= UploadHandler ali2IR start ====== ");

//        org.json.JSONArray jsonArray = null;
//        jsonArray = object.getJSONArray("value");
//        logger.info("UploadHandler jsonArray ====== "+ jsonArray);
//        logger.info("aliDevCache ====== "+aliDevCache);
//        String bId = aliDevCache.getValue("ir_"+deviceSerialId);//品牌ID
//        if(bId==null||bId.equals("")){
//            bId = "478";
//        }
//        List<TYaoKongYunBrand> yaoKongYunBrandList = yaoKongYunService.getYaoKongYunByTId(Integer.valueOf(bId));
//        TYaoKongYunBrand yaoKongYunBrand = null;
//        if(yaoKongYunBrandList !=null){
//            yaoKongYunBrand = yaoKongYunBrandList.get(0);
//        }else{
//            yaoKongYunBrand = new TYaoKongYunBrand();
//        }
        Integer functionId = (Integer) object.get("functionId");
        String data = (String) object.get("data");
//        Integer functionId = Integer.valueOf(js.get("functionId").toString());
        byte[] strByte = ByteHelper.hexStringToBytes(data);
        String resStr = new String(strByte);//红外返回码
        String index = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("index_"+deviceSerialId);
        String brandId = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("brandId_"+index)==null?
                cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("brandId_"+deviceSerialId):cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("brandId_"+index);
        String deviceType = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("deviceType_"+index);
//        String remoteName = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("remoteName_"+index);
        logger.info("serialId ====== "+deviceSerialId);
        logger.info("index ====== "+index);
        logger.info("brandId ====== "+brandId);
        logger.info("deviceType ====== "+deviceType);
//        logger.info("name ====== "+remoteName);
        Map<String,Object> resMap = new HashMap<>();
        TUserAliDevice userAliDevice = userAliDevService.queryAliDeviceBySerialiId(deviceSerialId);
        if(functionId==2){//学习红外上传
            QueryRemoteBySrcDTO dto = new QueryRemoteBySrcDTO();
            dto.setType(Integer.valueOf(deviceType));
            dto.setBrandType(Integer.valueOf(brandId));
            dto.setIndex(Integer.valueOf(index));
            com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
            jsonObject.put("key",resStr);
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(jsonObject);
            dto.setKeys(jsonArray);
            dto.setExtendsKeys(new JSONArray());

            resMap.put("type",20);
            resMap.put("success",true);
            resMap.put("serialId",deviceSerialId);
            resMap.put("remote",dto);

            pushservice.pairIrRemotecode(resMap,userAliDevice.getUserId());
        }else if(functionId==3){//一键匹配红外上传
            resMap = getRemoteControlList(brandId,"7",resStr);
            resMap.put("type",21);
            resMap.put("success",true);
            resMap.put("serialId",deviceSerialId);
            pushservice.pairIrRemotecode(resMap,userAliDevice.getUserId());

        }
//        return null;
    }

    private Map<String,Object> getRemoteControlList(String brandId,String deviceType,String src) throws Exception {
        Map<String,Object> resMap = new HashMap<String,Object>();
        TYaokonyunDevice yaokonyunDevice = getYaoKongDevice();
        List<String> strings = new ArrayList<String>();
        strings.add("bid="+brandId);
        strings.add("t=7");
        strings.add("r="+src);
        strings.add("v=4");
        strings.add("zip=1");
        String result = yaoKongYunSend
                .postMethod(strings,yaokonyunDevice,yaoKongYunConfig.getUrlPrefix()+"?c=l");
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        MatchRemoteControlResult remoteControlResult = gson.fromJson(result,MatchRemoteControlResult.class);

        if(remoteControlResult==null||remoteControlResult.getSm()==0){
            resMap.put("sm",0);
            resMap.put("rs",new ArrayList());
        }else{
            List<MatchRemoteControl>  list = remoteControlResult.getRs();
            List<TYaokonyunRemoteControl> remoteControlList = new ArrayList<TYaokonyunRemoteControl>();
            List<QueryRemoteBySrcDTO> dtoList = new ArrayList<QueryRemoteBySrcDTO>();
            List<QueryRemoteBySrcDTO2> dtoSrcList = new ArrayList<QueryRemoteBySrcDTO2>();
            for(MatchRemoteControl matchRemoteControl :list){
                TYaokonyunRemoteControl tYaokonyunRemoteControl = new TYaokonyunRemoteControl(matchRemoteControl);
                remoteControlList.add(tYaokonyunRemoteControl);
                QueryRemoteBySrcDTO dto = new QueryRemoteBySrcDTO(matchRemoteControl);
                QueryRemoteBySrcDTO2 srcDto = new QueryRemoteBySrcDTO2(matchRemoteControl);
                Integer idx = IndexUtils.getIdx();
                dto.setIndex(idx);
                dto.setBrandType(Integer.valueOf(brandId));
                srcDto.setIndex(idx);
                srcDto.setBrandType(Integer.valueOf(brandId));
                dtoList.add(dto);
                dtoSrcList.add(srcDto);
            }
            cmdCache.setIRDeviceInfoList(brandId+"_"+deviceType+"_"+"_remoteControlList",dtoList);
            cmdCache.setIRDeviceInfoList(brandId+"_"+deviceType+"_"+"_remoteControlListSrc",dtoSrcList);
            resMap.put("rs",dtoList);
        }
        return resMap;
    }

    private TYaokonyunDevice getYaoKongDevice() throws Exception {
        TYaokonyunDevice yaokonyunDevice = null;
        yaokonyunDevice = yaoKongYunService.getYaoKongYunDevice();
        if (yaokonyunDevice == null) {
            yaokonyunDevice = createYaoKongYunDevice();
            List<String> strings = new ArrayList<>();
            strings.add("appid="+yaokonyunDevice.getAppId());
            strings.add("f="+yaokonyunDevice.getDeviceId());
            yaoKongYunSend.postMethod(null,yaokonyunDevice,yaoKongYunConfig.getUrlPrefix()+"?c=r");
        }

        return yaokonyunDevice;
    }

    private TYaokonyunDevice createYaoKongYunDevice() throws Exception {
        TYaokonyunDevice device = new TYaokonyunDevice();
        device.setDeviceId(MD5.getMD5Str(new Date().getTime() + ""));
        device.setAppId("15027861733449");
        yaoKongYunService.addYaoKongDevice(device);
        return yaoKongYunService.getYaoKongYunDevice();
    }



}
