package com.bright.apollo.service;

import com.bright.apollo.util.Encrypt;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.GZIPInputStream;

@Component
public class YaoKongYunSend {

    @Autowired
    protected YaoKongYunConfig yaoKongYunConfig;

    private static String userAgent = "(Liunx; u; Android ; en-us;Media)";
    private static final Logger log = Logger.getLogger(YaoKongYunSend.class);
    private static String key = "demo.fortest1234";

    private Map<String,Object> sendInit(){
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
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("httpPost",httpPost);
        resultMap.put("httpClient",httpClient);
        return resultMap;
    }


    public void createDevice(){
        Map<String,Object> initMap = sendInit();

    }

    public String postMethod(String url, List<String> list) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>() ;
        String total = "" ;
        if(list!=null&&!list.isEmpty()&&list.size()>0){
            for (String str : list) {
                if(!StringUtils.isEmpty(str)){
                    String[] kv = str.split("=");
                    if(kv.length==2){
                        //避免空指针异常
                        nameValuePairs.add(new BasicNameValuePair(kv[0], kv[1]));
                        total = total + kv[1] ;
                    }
                }
            }
        }
        String deviceId = sdkManager.getDeviceId();
//        nameValuePairs.add(new BasicNameValuePair("f", deviceId));
//        String appid = sdkManager.getAppId();
//        nameValuePairs.add(new BasicNameValuePair("appid", appid));
        String time = new Date().getTime() + "" ;
        total = total + time ;
        String auth = Encrypt.encryptSpecial(total);
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost request = new HttpPost(url);
            request.setHeader("User-Agent",userAgent);
            request.addHeader("accept-encoding", "gzip,deflate");
            request.addHeader("client", time+"_" + auth);
            HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
            request.setEntity(httpEntity);
            HttpResponse response = httpClient.execute(request);
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                byte[] srcData = EntityUtils.toByteArray(entity);
                byte[] nData = null ;
                if(entity.getContentEncoding()!=null
                        && entity.getContentEncoding().getValue().contains("gzip")){
                    nData = unzip(srcData); // 解压
                }else{
                    nData = srcData ;
                }
                String iv = "testfor.demo4213";
                Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
                SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
                IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
                cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
                byte[] original = cipher.doFinal(nData);
                String originalString = new String(original, "UTF-8");
                originalString = originalString.trim(); // 源文
//                log.info( "originalString: e is " + originalString);
                return originalString;
            }
            return "";
        } catch (Exception e) {
//            log.error( "postMethod: e is " + e,e);
            e.printStackTrace();
            return "";
        } finally {
            if (httpClient != null) {
                httpClient=null;
            }
        }
    }

    public byte[] unzip(byte[] srcData) throws IOException {
        InputStream inputStream = new GZIPInputStream(new ByteArrayInputStream(
                srcData));
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(temp, 0, 1024)) != -1) {
            arrayOutputStream.write(temp, 0, len);
        }
        arrayOutputStream.close();
        inputStream.close();
        return arrayOutputStream.toByteArray();
    }
}
