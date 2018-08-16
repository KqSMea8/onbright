package com.bright.apollo.handler;

import com.bright.apollo.cache.AliDevCache;
import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.bright.apollo.common.entity.TYaoKongYunBrand;
import com.bright.apollo.service.AliDeviceConfigService;
import com.bright.apollo.service.TopicServer;
import com.bright.apollo.service.YaoKongYunConfig;
import com.bright.apollo.service.YaoKongYunService;
import com.zz.common.util.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class UploadHandler extends AliBaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(UploadHandler.class);

    @Override
    public void process(String deviceSerialId, JSONObject object) {
        // TODO Auto-generated method stub
        JSONObject jsonObject = null;
        try{
//            TAliDeviceConfig tAliDeviceConfig = aliDeviceConfigService.getAliDeviceConfigBySerializeId(deviceSerialId);
//            if (tAliDeviceConfig != null) {
//                if (!StringUtils.isEmpty(object.getString("value"))) {
//                    tAliDeviceConfig.setState(object.getString("value"));
//                    aliDeviceConfigService.update(tAliDeviceConfig);
//                }
//
//            }
            jsonObject = ali2IR(deviceSerialId,object);
            topicServer.pubIRTopic(null,null,deviceSerialId,jsonObject);
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

    private JSONObject ali2IR(String deviceSerialId, JSONObject object) throws Exception {

        logger.info(" ======= UploadHandler ali2IR start ====== ");

        JSONArray jsonArray = null;
        jsonArray = object.getJSONArray("value");
        logger.info("UploadHandler jsonArray ====== "+ jsonArray);
        logger.info("aliDevCache ====== "+aliDevCache);
        String bId = aliDevCache.getValue("ir_"+deviceSerialId);//品牌ID
        if(bId==null||bId.equals("")){
            bId = "478";
        }
        List<TYaoKongYunBrand> yaoKongYunBrandList = yaoKongYunService.getYaoKongYunByTId(Integer.valueOf(bId));
        TYaoKongYunBrand yaoKongYunBrand = null;
        if(yaoKongYunBrandList !=null){
            yaoKongYunBrand = yaoKongYunBrandList.get(0);
        }else{
            yaoKongYunBrand = new TYaoKongYunBrand();
        }
        JSONObject js = jsonArray.getJSONObject(0);
        Integer functionId = Integer.valueOf(js.get("functionId").toString());
        if(functionId==3){//下载测试码
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
            HttpPost httpPost =new HttpPost(yaoKongYunConfig.getUrlPrefix());
            Registry<ConnectionSocketFactory>
                    socketFactoryRegistry =RegistryBuilder.<ConnectionSocketFactory>create().
                    register("http", PlainConnectionSocketFactory.INSTANCE).build();
            PoolingHttpClientConnectionManager connectionManager =
                    new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            CloseableHttpClient httpClient = HttpClients.custom().
                    setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(requestConfig).build();
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("c", "m"));
            nvps.add(new BasicNameValuePair("appid", yaoKongYunConfig.getAppId()));
            nvps.add(new BasicNameValuePair("f", yaoKongYunConfig.getDeviceId()));
            nvps.add(new BasicNameValuePair("r", jsonArray.getString(1)));
            nvps.add(new BasicNameValuePair("zip", "1"));
            nvps.add(new BasicNameValuePair("bid", String.valueOf(yaoKongYunBrand.getbId())));
            nvps.add(new BasicNameValuePair("t", String.valueOf(yaoKongYunBrand.getDeviceType())));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost);
            logger.info("UploadHandler closeableHttpResponse ====== "+ closeableHttpResponse);

            return getStateJson("3");
        }else if(functionId==4){//遥控器学习码
            //todo 保存遥控器学习码

            return getStateJson("4");

        }
        return null;
    }

    private JSONObject getStateJson(String functionId) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONObject valueObject = new JSONObject();
        jsonObject.put("respCode","200");
        valueObject.put("functionId",functionId);
        valueObject.put("data","true");
        String[] values = new String[1];
        values[0] = valueObject.toString();
        jsonObject.put("value",values);
        logger.info(" ======= UploadHandler getStateJson ====== "+jsonObject);
        return jsonObject;
    }

}
