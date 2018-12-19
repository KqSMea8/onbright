package com.bright.apollo.handler;

import com.alibaba.fastjson.JSONArray;
import com.bright.apollo.bean.MatchRemoteControlResult;
import com.bright.apollo.cache.CmdCache;
import com.bright.apollo.common.entity.*;
import com.bright.apollo.enums.IREnum;
import com.bright.apollo.service.PushService;
import com.bright.apollo.service.TopicServer;
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
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

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

    @Autowired
    private TopicServer topicServer;


    @Override
    public void process(String deviceSerialId, JSONObject object) {
        Map<String,Object> jsonObject = null;
        try{
            ali2IR(deviceSerialId,object);
        }catch (Exception e){
            e.printStackTrace();

        }

    }

    //红外方法
    private void ali2IR(String deviceSerialId, JSONObject object) throws Exception {
        logger.info(" ======= UploadHandler ali2IR start ====== ");
        org.json.JSONArray resArray = (org.json.JSONArray) object.get("value");
        JSONObject resJson = resArray.getJSONObject(0);
        Integer functionId = (Integer) resJson.get("functionId");
        String data = (String) resJson.get("data");//红外返回码
        String index = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("index_"+deviceSerialId);
        String brandId = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("brandId_"+index)==null?
                cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("brandId_"+deviceSerialId):cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("brandId_"+index);
        String deviceType = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("deviceType_"+index)==null?cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("deviceType_"+deviceSerialId):
                cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("deviceType_"+index);
        String key = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("keyName_"+index);
        logger.info("serialId ====== "+deviceSerialId);
        logger.info("index ====== "+index);
        logger.info("brandId ====== "+brandId);
        logger.info("deviceType ====== "+deviceType);
        logger.info("key ====== "+key);
        com.alibaba.fastjson.JSONObject resMap = new com.alibaba.fastjson.JSONObject();
        TUserAliDevice userAliDevice = userAliDevService.queryAliDeviceBySerialiId(deviceSerialId);
        if(functionId==2){//学习红外上传
            com.alibaba.fastjson.JSONObject mqttJson = new com.alibaba.fastjson.JSONObject();
            JSONArray jsonArray = new JSONArray();
            mqttJson.put("keys",jsonArray);
            mqttJson.put("extendsKeys",new JSONArray());
            mqttJson.put("index",Integer.valueOf(index==null?"0":index));
            mqttJson.put("name","");
            mqttJson.put("deviceType",Integer.valueOf(deviceType==null?"0":deviceType));
            mqttJson.put("brandId",Integer.valueOf(brandId==null?"0":brandId));
            resMap.put("type",20);
            resMap.put("success",true);
            resMap.put("serialId",deviceSerialId);
            resMap.put("remote",mqttJson);
            yaoKongYunService.updateYaoKongKeyCodeNameBySerialIdAndIndexAndKey(deviceSerialId,index,key,data);//保存src
            pushservice.pairIrRemotecode(resMap,userAliDevice.getUserId());
        }else if(functionId==3){//一键匹配红外上传
            resMap = getRemoteControlList(brandId,"7",data);
            resMap.put("type",21);
            resMap.put("success",true);
            resMap.put("serialId",deviceSerialId);
            pushservice.pairIrRemotecode(resMap,userAliDevice.getUserId());
        }else if(functionId==6){//本地遥控方案——下载方案
            Integer downloadIndex = (Integer) resJson.get("index");
            if(!data.equals("0")){
                List<TYaokonyunKeyCode> yaokonyunKeyCodeList = cmdCache.getIRDeviceInfoList("ir_keCodeList_"+downloadIndex);
                for(int i=0;i<yaokonyunKeyCodeList.size();i++){
                    TYaokonyunKeyCode yaokonyunKeyCode = yaokonyunKeyCodeList.get(i);
                    Map<String, Object> requestMap = new HashMap<String, Object>();
                    requestMap.put("command","set");
                    com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
                    com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                    json.put("functionId",7);
                    json.put("data",yaokonyunKeyCode.getSrc());
                    json.put("index",yaokonyunKeyCode.getIndex());
                    json.put("count",i+1);
                    String sendkey = yaokonyunKeyCode.getKey();
                    String[] arr = sendkey.split("_");
                    if(arr.length<2){
                        arr = sendkey.split("");
                    }
                    StringBuffer sb = new StringBuffer();
                    for(int j=0;j<arr.length;j++){
                        String detail = arr[j];
                        if(j==2&&!detail.equals("")){
                            logger.info("before transfor temperature  ====== "+detail);
                            String temperature = Integer.toHexString(Integer.valueOf(detail));
                            logger.info("after transfor temperature  ====== "+temperature);
                            sb.append(temperature);

                        }else{
                            if(detail.equals("")){
                                detail = "_";
                            }
                            IREnum enums = IREnum.getRegion(detail);
                            sb.append(enums.getValue());
                        }
                    }
                    logger.info(" ======= sendKey ====== "+sb);
                    json.put("key",sb.toString());
                    jsonArray.add(json);
                    requestMap.put("value",jsonArray);
                    topicServer.pubIrRPC(requestMap,deviceSerialId);
                }
            }
        }else if(functionId==7){
            Integer localaddr = (Integer) resJson.get("localaddr");//转发器内方案存储索引
            resMap.put("type",22);
            resMap.put("success",true);
            resMap.put("serialId",deviceSerialId);
            resMap.put("index",Integer.valueOf(index));
            resMap.put("localaddr",localaddr);
            pushservice.pairIrRemotecode(resMap,userAliDevice.getUserId());
        }
    }

    private com.alibaba.fastjson.JSONObject getRemoteControlList(String brandId,String deviceType,String src) throws Exception {
        com.alibaba.fastjson.JSONObject resMap = new com.alibaba.fastjson.JSONObject();
        TYaokonyunDevice yaokonyunDevice = getYaoKongDevice();
        List<String> strings = new ArrayList<String>();
        strings.add("bid="+brandId);
        strings.add("t=7");
        strings.add("r="+src);
        strings.add("zip=1");
        String result = yaoKongYunSend
                .postMethod(strings,yaokonyunDevice,yaoKongYunConfig.getUrlPrefix()+"?c=m");
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        MatchRemoteControlResult remoteControlResult = gson.fromJson(result,MatchRemoteControlResult.class);

        if(remoteControlResult==null||remoteControlResult.getSm()==0){
//            resMap.put("sm",0);
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
                cmdCache.addIrBrandId(idx.toString(),brandId);
                cmdCache.addIrDeviceType(idx.toString(),deviceType);
                dto.setIndex(idx);
                dto.setBrandId(Integer.valueOf(brandId==null?"0":brandId));
                srcDto.setIndex(idx);
                srcDto.setBrandType(Integer.valueOf(brandId==null?"0":brandId));
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
            List<String> strings = new ArrayList<String>();
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
