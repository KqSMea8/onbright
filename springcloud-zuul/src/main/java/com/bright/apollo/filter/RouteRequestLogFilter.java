/*package com.bright.apollo.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
 
 
*//**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年7月2日
 * @Version:1.1.0
 *//*
@Component
public class RouteRequestLogFilter extends ZuulFilter {
 
	@Override
	public String filterType() {
		return "route";
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
 		String responseBody = ctx.getResponseBody();
		System.out.println("====responseBody:"+responseBody);
		return null;
	}
}*/