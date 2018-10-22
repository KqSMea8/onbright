package com.bright.apollo.handler;

import com.alibaba.fastjson.JSONArray;
import com.bright.apollo.common.entity.PushMessage;
import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.bright.apollo.common.entity.TUserAliDevice;
import com.bright.apollo.enums.PushMessageType;
import com.bright.apollo.service.PushService;
import com.bright.apollo.service.UserAliDevService;
import com.zz.common.util.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class UploadHandler extends AliBaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(UploadHandler.class);

    @Autowired
    private PushService pushservice;

    @Autowired
    private UserAliDevService userAliDevService;


    @Override
    public void process(String deviceSerialId, JSONObject object) {
//        Map<String,Object> jsonObject = null;
        try{
            TAliDeviceConfig tAliDeviceConfig = aliDeviceConfigService.getAliDeviceConfigBySerializeId(deviceSerialId);
            if (tAliDeviceConfig != null) {
                if (!StringUtils.isEmpty(object.getString("value"))) {
                    tAliDeviceConfig.setState(object.getString("value"));
                    aliDeviceConfigService.update(tAliDeviceConfig);
                }
                PushMessage pushMessage;
                pushMessage=new PushMessage();
                pushMessage.setType(PushMessageType.WIFI_TRANS.getValue());
                pushMessage.setOnLine(true);

                net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
                jsonObject.put("deviceId", tAliDeviceConfig.getDeviceSerialId());
                jsonObject.put("type", tAliDeviceConfig.getType());
                jsonObject.put("name", tAliDeviceConfig.getName());
                jsonObject.put("action", JSONArray.parseArray(tAliDeviceConfig.getAction()));
                jsonObject.put("state", JSONArray.parseArray(tAliDeviceConfig.getState()));
                logger.info("upload json  ====== "+jsonObject);
                pushMessage.setData(jsonObject.toString());

                pushMsg(deviceSerialId, pushMessage);
            }
            //tudo 红外
//            jsonObject = ali2IR(deviceSerialId,object);
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
//    private Map<String,Object> ali2IR(String deviceSerialId, JSONObject object) throws Exception {
//
//        logger.info(" ======= UploadHandler ali2IR start ====== ");
//
//        JSONArray jsonArray = null;
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
//        JSONObject js = jsonArray.getJSONObject(0);
//        Integer functionId = Integer.valueOf(js.get("functionId").toString());
//        if(functionId==3){//下载测试码
//            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
//            HttpPost httpPost =new HttpPost(yaoKongYunConfig.getUrlPrefix());
//            Registry<ConnectionSocketFactory>
//                    socketFactoryRegistry =RegistryBuilder.<ConnectionSocketFactory>create().
//                    register("http", PlainConnectionSocketFactory.INSTANCE).build();
//            PoolingHttpClientConnectionManager connectionManager =
//                    new PoolingHttpClientConnectionManager(socketFactoryRegistry);
//            CloseableHttpClient httpClient = HttpClients.custom().
//                    setConnectionManager(connectionManager)
//                    .setDefaultRequestConfig(requestConfig).build();
//            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//            nvps.add(new BasicNameValuePair("c", "m"));
//            nvps.add(new BasicNameValuePair("appid", yaoKongYunConfig.getAppId()));
//            nvps.add(new BasicNameValuePair("f", yaoKongYunConfig.getDeviceId()));
//            String data = (String)js.get("data");
//            nvps.add(new BasicNameValuePair("r", data));
//            nvps.add(new BasicNameValuePair("zip", "1"));
//            nvps.add(new BasicNameValuePair("bid", String.valueOf(yaoKongYunBrand.getbId())));
//            nvps.add(new BasicNameValuePair("t", String.valueOf(yaoKongYunBrand.getDeviceType())));
//            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
//            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost);
//            logger.info("UploadHandler closeableHttpResponse ====== "+ closeableHttpResponse);
//
//            return getStateJson("3");
//        }else if(functionId==4){//遥控器学习码
//            //todo 保存遥控器学习码
//
//            return getStateJson("4");
//
//        }
//        return null;
//    }

    private Map<String,Object> getStateJson(String functionId) throws JSONException {
        Map<String,Object> jsonObject = new HashMap<String, Object>();
        Map<String,Object> valueObject = new HashMap<String, Object>();
        jsonObject.put("respCode","200");
        valueObject.put("functionId",functionId);
        valueObject.put("data","true");
        jsonObject.put("value",valueObject);
        logger.info(" ======= UploadHandler getStateJson ====== "+jsonObject);
        return jsonObject;
    }

    private void pushMsg(String deviceSerialId,PushMessage pushMessage) throws Exception {
        logger.info("upload pushMessage ====== "+pushMessage.toString());
        List<TUserAliDevice> list=userAliDevService.queryAliUserId(deviceSerialId);
        Set<Integer> setuser=new ConcurrentSkipListSet<Integer>();
        for (TUserAliDevice tUserAliDevice : list) {
            setuser.add(tUserAliDevice.getUserId());
        }
        pushservice.pushToApp(pushMessage, setuser);
    }

}
