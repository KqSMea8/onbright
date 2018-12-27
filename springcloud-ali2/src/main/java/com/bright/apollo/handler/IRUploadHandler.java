package com.bright.apollo.handler;

import com.bright.apollo.bean.MatchRemoteControlResult;
import com.bright.apollo.cache.CmdCache;
import com.bright.apollo.common.entity.*;
import com.bright.apollo.service.PushService;
import com.bright.apollo.service.UserAliDevService;
import com.bright.apollo.service.YaoKongYunSend;
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
        String irName = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("irRemote_"+deviceSerialId);
        String keyType = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("keyType_"+index);
        logger.info("serialId ====== "+deviceSerialId);
        logger.info("index ====== "+index);
        logger.info("brandId ====== "+brandId);
        logger.info("deviceType ====== "+deviceType);
        logger.info("key ====== "+key);
        logger.info("irName ====== "+irName);
        logger.info("keyType ====== "+keyType);
        com.alibaba.fastjson.JSONObject resMap = new com.alibaba.fastjson.JSONObject();
        List<TUserAliDevice> userAliDevice = userAliDevService.queryAliUserId(deviceSerialId);
        Set<TUserAliDevice> userSet = new HashSet<TUserAliDevice>();
        for(TUserAliDevice user : userAliDevice){
            userSet.add(user);
        }
        if(functionId==2){//学习红外上传
            com.alibaba.fastjson.JSONObject mqttJson = new com.alibaba.fastjson.JSONObject();
            Integer kt = Integer.valueOf(keyType);
            Integer idx = Integer.valueOf(index==null?"0":index);
            resMap.put("type",20);
            resMap.put("success",true);
            resMap.put("serialId",deviceSerialId);
            TYaokonyunKeyCode yaokonyunKeyCode = yaoKongYunService.getIRDeviceByIndexAndKey(idx,key);
            if(yaokonyunKeyCode==null){
			yaokonyunKeyCode = new TYaokonyunKeyCode();
            yaokonyunKeyCode.setKeyType(Integer.valueOf(keyType));
			yaokonyunKeyCode.setIndex(idx);
			yaokonyunKeyCode.setLastOpTime(new Date());
			yaokonyunKeyCode.setBrandId(Integer.valueOf(brandId==null?"0":brandId));
			yaokonyunKeyCode.setRmodel("");
			yaokonyunKeyCode.settId(Integer.valueOf(deviceType==null?"0":deviceType));
			yaokonyunKeyCode.setName(irName);
			yaokonyunKeyCode.setVersion(0);
			yaokonyunKeyCode.setSrc(data);
			yaokonyunKeyCode.setSerialId(deviceSerialId);
			yaokonyunKeyCode.setKey(key);
			yaokonyunKeyCode.setKeyType(kt);
			yaoKongYunService.addTYaokonyunKeyCode(yaokonyunKeyCode);
            }else{
                yaoKongYunService.updateYaoKongKeyCodeNameBySerialIdAndIndexAndKey(deviceSerialId,index,key,data,kt);//保存src
            }
            List<TYaokonyunKeyCode> yaokonyunKeyCodeList = yaoKongYunService.getIRDeviceByIndex(idx);
            List<QueryRemoteBySrcDTO> dtoList = new ArrayList<QueryRemoteBySrcDTO>();
            List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
            List<Map<String,Object>> filterList = new ArrayList<Map<String,Object>>();
            com.alibaba.fastjson.JSONArray keyArray = null;
            Map<String,Object> map = null;
            for(TYaokonyunKeyCode keyCode:yaokonyunKeyCodeList){
                map = new HashMap<String, Object>();
                keyArray = new com.alibaba.fastjson.JSONArray();
                com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
                com.alibaba.fastjson.JSONArray nonArr = new com.alibaba.fastjson.JSONArray();
                String selkey = keyCode.getKey();
                jsonObject.put("key",selkey);
                keyArray.add(jsonObject);
                map.put("version",keyCode.getVersion());
                map.put("rmodel",keyCode.getRmodel());
                map.put("name",keyCode.getName());
                Integer selindex = keyCode.getIndex();
                map.put("index",selindex);
                map.put("type",keyCode.gettId());
                map.put("brandType",keyCode.getBrandId());
                if(keyCode.getKeyType()==0){
                    map.put("keys",keyArray);
                    map.put("extendsKeys",nonArr);
                }else{
                    map.put("keys",nonArr);
                    map.put("extendsKeys",keyArray);
                }
                mapList.add(map);
            }
            String idxs = "";
            if(mapList.size()>0){
                for(Map<String,Object> dtomap :mapList){
                    com.alibaba.fastjson.JSONArray dtoArray = (com.alibaba.fastjson.JSONArray)dtomap.get("keys");
                    com.alibaba.fastjson.JSONArray dtoArray2 = (com.alibaba.fastjson.JSONArray)dtomap.get("extendsKeys");
                    Integer dtoIdx = (Integer)dtomap.get("index");
                    if(filterList.size()==0){
                        filterList.add(dtomap);
                        idxs += dtoIdx+",";
                    }else{
                        for(int i=0;i<filterList.size();i++){
                            Map<String,Object> filterMap = filterList.get(i);
                            com.alibaba.fastjson.JSONArray filterArray = (com.alibaba.fastjson.JSONArray)filterMap.get("keys");
                            Integer filterIdx = (Integer) filterMap.get("index");
                            if(filterIdx.equals(dtoIdx)&&dtoArray.size()>0
                                    &&!filterArray.equals(dtoArray)){
                                com.alibaba.fastjson.JSONObject dtoJson = dtoArray.getJSONObject(0);
                                filterArray.add(dtoJson);
                            }else if(idxs.indexOf(dtoIdx.toString())<0){
                                filterList.add(dtomap);
                                idxs += dtoIdx+",";
                            }
                            com.alibaba.fastjson.JSONArray filterArray2 = (com.alibaba.fastjson.JSONArray)filterMap.get("extendsKeys");
                            if(filterIdx.equals(dtoIdx)&&dtoArray2.size()>0
                                    &&!filterArray2.equals(dtoArray2)){
                                com.alibaba.fastjson.JSONObject dtoJson = dtoArray2.getJSONObject(0);
                                filterArray2.add(dtoJson);
                            }else if(idxs.indexOf(dtoIdx.toString())<0){
                                filterList.add(dtomap);
                                idxs += dtoIdx+",";
                            }
                        }
                    }
                }
                for(Map<String,Object> dtomap :filterList){
                    dtoList.add(new QueryRemoteBySrcDTO(dtomap));
                }
            }
            resMap.put("remote",dtoList.get(0));
            pushservice.pairIrRemotecode(resMap,userSet);
        }else if(functionId==3){//一键匹配红外上传
            resMap = getRemoteControlList(brandId,"7",data);
            resMap.put("type",21);
            resMap.put("success",true);
            resMap.put("serialId",deviceSerialId);
            pushservice.pairIrRemotecode(resMap,userSet);
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
        yaoKongYunService.updateYaoKongUserTimes();
        if(remoteControlResult==null||remoteControlResult.getSm()==0){
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
