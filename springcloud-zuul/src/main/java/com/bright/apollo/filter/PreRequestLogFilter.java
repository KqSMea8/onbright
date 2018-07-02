package com.bright.apollo.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
 
 
/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年7月2日
 * @Version:1.1.0
 */
@Component
public class PreRequestLogFilter extends ZuulFilter {
 
	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		System.out.println("=============PRE=============");
 		System.out.println("=============RECV REQUEST PARAMS START=============");
		System.out.println("URL="+request.getRequestURL());
		for (String name : request.getParameterMap().keySet()) {
			System.out.println("name=" + name + ";value=" + request.getParameter(name)); 
		}
		System.out.println("============RECV REQUEST PARAMS END=============");
		System.out.println("===Authorization:"+request.getHeader("Authorization"));
   		return null;
	}
}