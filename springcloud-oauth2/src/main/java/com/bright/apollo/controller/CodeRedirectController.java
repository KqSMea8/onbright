package com.bright.apollo.controller;

import com.alibaba.fastjson.JSONReader;
import com.bright.apollo.redis.RedisBussines;
import com.bright.apollo.service.OltuService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

@RestController
@RequestMapping("/aouth2")
public class CodeRedirectController {

    Logger logger = Logger.getLogger(CodeRedirectController.class);

    @Autowired
    private RedisBussines redisBussines;

    @Autowired
    private OltuService oltuService;


    @RequestMapping(value="/takeToken",method = RequestMethod.GET,produces = "application/json ;charset=UTF-8")
    public Object getAuthorizationOauth(HttpServletRequest request,HttpServletResponse response) throws Exception {
       logger.info(" ======= /aouth2/takeToken ====== ");
       logger.info(" code ======= "+request.getParameter("code"));
       System.out.println(" code ======= "+request.getParameter("code"));
       List<NameValuePair> nvps = new ArrayList<NameValuePair>();
       nvps.add(new BasicNameValuePair("code", request.getParameter("code")));
       nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));
       nvps.add(new BasicNameValuePair("client_id", "webApp"));
       nvps.add(new BasicNameValuePair("client_secret", "webApp"));
       nvps.add(new BasicNameValuePair("redirect_uri", "http://localhost:8815/aouth2/takeToken"));
        Map<String,Object> map = tokenPost(nvps);
       System.out.println(map.toString());
       return map.toString();
    }

    private Map<String,Object> tokenPost(List<NameValuePair> nvps) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        enableSSl();
        HttpPost httpPost =new HttpPost("https://localhost:8815/oauth/token");
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        Registry<ConnectionSocketFactory>
                socketFactoryRegistry =RegistryBuilder.<ConnectionSocketFactory>create().
                register("http", PlainConnectionSocketFactory.INSTANCE).
                register("https", socketFactory).build();
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        CloseableHttpClient httpClient = HttpClients.custom().
                setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig).build();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();

        InputStream is = entity.getContent();
        Reader reder = new InputStreamReader(is);
        JSONReader jsonReader =new JSONReader(reder);
        Map<String,Object> map = (Map<String, Object>) jsonReader.readObject();
        return map;
    }

    private static TrustManager manager = new X509TrustManager() {


        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    private static void enableSSl() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("SSL");
        context.init(null,new TrustManager[]{manager},null);
        socketFactory = new SSLConnectionSocketFactory(context,NoopHostnameVerifier.INSTANCE);
    }

    private static SSLConnectionSocketFactory socketFactory;
    
    @RequestMapping(value="/sendRedirect",method = RequestMethod.POST)
    public String sendRedirectURI(HttpServletRequest request,HttpServletResponse response) throws Exception {
       logger.info(" ======= sendRedirectURI ====== ");
       logger.info(" code ======= "+request.getParameter("code"));
       String redirect_uri = request.getParameter("redirect_uri");
       String state = request.getParameter("state");
       System.out.println(request.getRequestURI());
       if(redirect_uri!=null){
       	redirect_uri = URLDecoder.decode(redirect_uri, "UTF-8");
       	redirect_uri += "&"+state;
       	System.out.println(redirect_uri);
       	request.setAttribute("redirect_uri", redirect_uri);
       }
       Enumeration<String> m = request.getParameterNames();
       while(m.hasMoreElements()){
       	String element = m.nextElement();
       	System.out.println("params ------ "+element+" ------ "+request.getParameter(element));
       }
       
       return "";
    }



}
