package com.bright.apollo.http;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.OauthClientDetails;
import com.bright.apollo.constant.Constant;

  

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年9月10日  
 *@Version:1.1.0  
 */
public class HttpWithBasicAuth {
	    public static CloseableHttpResponse http(Map<String, Object> map,OauthClientDetails client) throws Exception {
    		if(client==null||StringUtils.isEmpty(client.getClientId())||StringUtils.isEmpty(client.getClientSecret())){
    			return null;
    		}
	    	SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
		    HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
			
    		CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(client.getClientId(), client.getClientSecret()));
            CloseableHttpClient createDefault = HttpClients.custom()
                    .setDefaultCredentialsProvider(credsProvider).setSSLSocketFactory(sslsf)
                    .build();
            StringBuilder sb=new StringBuilder(Constant.OAUTH);
            if(map!=null&&!map.isEmpty()){
            	sb.append("?");
            	for(Map.Entry<String, Object> entry:map.entrySet()){
            		sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            	}
             }
    		HttpPost post = new HttpPost(sb.toString());
    		return createDefault.execute(post);
    		/*CloseableHttpResponse result = createDefault.execute(post);
    		int statusCode = result.getStatusLine().getStatusCode();
    		System.out.println(statusCode);
    		System.out.println("result：" + EntityUtils.toString(result.getEntity()));*/
    	}
}
