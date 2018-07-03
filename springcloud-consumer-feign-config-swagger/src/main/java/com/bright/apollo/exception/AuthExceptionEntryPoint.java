package com.bright.apollo.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年7月3日
 * @Version:1.1.0
 */
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws ServletException {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("error", "401");
		map.put("message", authException.getMessage());
		map.put("path", request.getServletPath());
		map.put("timestamp", String.valueOf(new Date().getTime()));
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getOutputStream(), map);
		} catch (Exception e) {
			throw new ServletException();
		}
	}
}