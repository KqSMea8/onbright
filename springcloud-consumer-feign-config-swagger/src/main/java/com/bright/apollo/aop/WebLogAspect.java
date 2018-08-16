package com.bright.apollo.aop;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
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

	@Pointcut("execution(public * com.bright.apollo.controller.CommonController.*(..))")
	public void webLog() {
	}

	@Around("webLog()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        if(sra!=null){
        	HttpServletRequest request = sra.getRequest();
        	if(request!=null){
        		printRequest(request);
                printRequsetBody(request);
        	}
        }
        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        Gson gson = new Gson();
        logger.info("请求结束，controller的返回值是 " + gson.toJson(result));
        return result;
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
