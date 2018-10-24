package com.bright.apollo.filter;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class PostRequestLogFilter extends ZuulFilter {
	private static final Logger logger = LoggerFactory.getLogger(PostRequestLogFilter.class);
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
		InputStream stream = RequestContext.getCurrentContext().getResponseDataStream();
		try {
			 String body = IOUtils.toString(stream);
			 logger.info("===respone:"+body);
		} catch (IOException e) {
			logger.error("errorMsg:"+e.getMessage());
		}
		 
    	return null;
	}
}