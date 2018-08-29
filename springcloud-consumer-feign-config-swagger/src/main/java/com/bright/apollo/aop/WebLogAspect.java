package com.bright.apollo.aop;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.feign.FeignUserClient;
import com.bright.apollo.redis.RedisBussines;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.UserService;
import com.bright.apollo.tool.NumberHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.base.Stopwatch;
import com.google.gson.Gson;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月10日
 * @Version:1.1.0
 */
@Aspect
@Configuration
public class WebLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    @Autowired
    private RedisBussines redisBussines;

    @Autowired
    private FeignUserClient feignUserClient;


    @Autowired
    private MqttPahoMessageDrivenChannelAdapter adapter;

	@Pointcut("execution(public * com.bright.apollo.controller.CommonController.*(..))")
	public void webLog() {
	}

//    @Pointcut("execution(public * com.bright.apollo.controller.CommonController.*(..))")
//    public void mqttController() {
//    }

	@Around("webLog()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        if(sra!=null){
        	HttpServletRequest request = sra.getRequest();
        	if(request!=null){
        		printRequest(request);
                printRequsetBody(request);
                try{
                    mqttFilter(request);
                }catch (Exception e){

                }
        	}
        }
        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        Gson gson = new Gson();
        logger.info("请求结束，controller的返回值是 " + gson.toJson(result));
        return result;
	}

    public void mqttFilter(HttpServletRequest request) {
        String appKey = request.getParameter("appkey");
        String accessToken = request.getParameter("access_token");
        if(!StringUtils.isEmpty(appKey)&&!StringUtils.isEmpty(accessToken)){//非第三方登录
            OAuth2Authentication defaultOAuth2AccessToken = redisBussines.getObject("auth:"+accessToken,OAuth2Authentication.class);
            User user = (User)defaultOAuth2AccessToken.getPrincipal();
            String userName = user.getUsername();
//                userDetailsService.loadUserByUsername(user.getUsername());
            TUser tUser =null;
            if(NumberHelper.isNumeric(userName)){
                ResponseObject<TUser> response = feignUserClient.getUser(userName);
                tUser = response.getData();
            }else{
                ResponseObject<TUser> response = feignUserClient.getUserById(Integer.valueOf(userName));
                tUser = response.getData();
            }
            Integer userId = tUser.getId();
            String appKeyUserId = redisBussines.get("appkey_userId"+userId);
            if(appKeyUserId==null||StringUtils.isEmpty(appKeyUserId)){
                redisBussines.setValueWithExpire("appkey_userId"+userId,appKey,60 * 60 * 24 * 7);
            }else if(appKeyUserId !=null && appKey!=null && !appKeyUserId.equals(appKey)){
                String[] appKeyUserIdArr = appKeyUserId.split(":");
                for(int i=0;i<appKeyUserIdArr.length;i++){
                    if(!appKey.equals(appKeyUserIdArr[i])){
                        redisBussines.setValueWithExpire("appkey_userId"+userId,appKeyUserId+":"+appKey,60 * 60 * 24 * 7);
                    }
                }

            }
            appKeyUserId = redisBussines.get("appkey_userId"+userId);
            String[] appkeyUserIdArr = appKeyUserId.split(":");
            String topicName = "";
            boolean isExists = false;
            if(appKeyUserId != null){
                for(int i=0;i<appkeyUserIdArr.length;i++){
                    String[] topics = adapter.getTopic();
                    topicName = "ob-smart."+appkeyUserIdArr[i];
                    for(int j=0;j<topics.length;j++){
                        if(topics[j].equals(topicName)){
                            isExists=true;
                        }
                    }
                    if(isExists==false){
                        logger.info(" ======= create topic ======= ");
                        try{
                            adapter.addTopic("ob-smart."+appkeyUserIdArr[i],1);
                        }catch (Exception e){
                            logger.info("====== create topic exception ====== "+e.getMessage());
                        }
                    }
                    isExists=false;
                }
            }
        }
    }

//	@Before("mqttController()")
	public void befortFilter() {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//		Object result = pjp.proceed();
		logger.info(" ====== befortFilter ====== " );
        if(sra!=null){
            HttpServletRequest request = sra.getRequest();

        }
//		return result;
	}

	public void printRequest(HttpServletRequest request) {
		logger.info("=============RECV REQUEST PARAMS START=============");
		logger.info("URL="+request.getRequestURL());
		for (String name : request.getParameterMap().keySet()) {
			logger.info("name=" + name + ";value=" + request.getParameter(name));
		}
		logger.info("============RECV REQUEST PARAMS END=============");
	}
	public String printRequsetBody(HttpServletRequest request) throws Exception {
		logger.info("=============RECV REQUEST BODY START=============");
		logger.info("URL="+request.getRequestURL());
		request.setCharacterEncoding("UTF-8");
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(), "utf-8"));
		StringBuffer sb = new StringBuffer("");
		String temp;
		while ((temp = br.readLine()) != null) { 
		  sb.append(temp);
		}
		br.close();
		logger.info("request param:" + sb.toString());
		logger.info("============RECV REQUEST BODY END=============");
		return sb.toString();
	}
}
