package com.bright.apollo.aop;

import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
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
@Order(-5)
public class WebLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

 
	@Pointcut("execution(public * com.bright.apollo.controller.*.*(..))")
	public void webLog() {
	}
	@Around("webLog()")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		long beginTime = System.currentTimeMillis();//1、开始时间 
        //利用RequestContextHolder获取requst对象
        ServletRequestAttributes requestAttr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        StringBuffer uri = requestAttr.getRequest().getRequestURL();
        logger.info("=============RECV REQUEST BODY START=============");
		logger.info("URL="+uri);
        //访问目标方法的参数 可动态改变参数值
        Object[] args = joinPoint.getArgs();
        //方法名获取
        String methodName = joinPoint.getSignature().getName();
        logger.info("请求方法：{}, 请求参数: {}", methodName, Arrays.toString(args));
        Object result = joinPoint.proceed();
        Gson gson = new Gson();
        long endTime = System.currentTimeMillis();
        logger.info("结束计时: {},  URI: {},耗时：{}", new Date(),uri,endTime - beginTime);
        logger.info("请求结束，controller的返回值是 " + gson.toJson(result));
        logger.info("============RECV REQUEST BODY END=============");
        return result;
	}
 
}