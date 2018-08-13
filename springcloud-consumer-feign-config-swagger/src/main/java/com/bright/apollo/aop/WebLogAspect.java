package com.bright.apollo.aop;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月10日
 * @Version:1.1.0
 */
@Aspect
@Order(1)
@Component
public class WebLogAspect {
	private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
 
	@Pointcut("execution(public * com.bright.apollo.controller..*.*(..))")
	public void webLog() {
	}

	@Around("webLog()")
	public Object  doAround(ProceedingJoinPoint joinPoint) throws Throwable {
 		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();
		String url = request.getRequestURL().toString();
		String method = request.getMethod();
		String uri = request.getRequestURI();
		logger.info("===url:" + url);
		logger.info("===method:" + method);
		logger.info("===uri:" + uri);
		logger.info("===log request form data===");
		for (String name : request.getParameterMap().keySet()) {
			logger.info("name=" + name + ";value=" + request.getParameter(name));
		}
		logger.info("===log request body===");
		request.setCharacterEncoding("UTF-8");
		BufferedReader br = new BufferedReader(
				new InputStreamReader((ServletInputStream) request.getInputStream(), "utf-8"));
		StringBuffer sb = new StringBuffer("");
		String temp;
		while ((temp = br.readLine()) != null) {
			sb.append(temp);
		}
		br.close();
		logger.info("===request body:" + sb.toString());
		Object result = joinPoint.proceed();
		Gson gson = new Gson();
		logger.info("请求结束，controller的返回值是 " + gson.toJson(result));
		return result;
	}

}
