/*package com.bright.apollo.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class PostRequestLogFilter extends ZuulFilter {
 
	@Override
	public String filterType() {
		return "post";
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
		HttpServletResponse response = ctx.getResponse();
		try {
			PrintWriter writer = response.getWriter();
			 
			OutputStream outputStream = response.getOutputStream();
			 
		} catch (IOException e) {
			System.out.println("errorMsg:"+e.getMessage());
		}
		System.out.println("=============POST=============");
    	return null;
	}
}*/