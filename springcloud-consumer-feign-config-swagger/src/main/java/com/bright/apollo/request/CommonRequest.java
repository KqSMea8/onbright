package com.bright.apollo.request;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zz.common.exception.AppException;
import com.zz.common.util.CommonUtil;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月29日  
 *@Version:1.1.0  
 */
public class CommonRequest {

	public String CMD;
	public String cip;
	public Phead phead;
	public GsonObject requestjson;
	public RequestParam requestParam;

	public CommonRequest(HttpServletRequest request, String CMD,
			String requestdata, RequestParam requestParam) throws AppException {
		this.CMD = CMD;
		this.requestParam = requestParam;
		if (!CommonUtil.isNull(requestdata)) {
			JsonElement jsonElement = new JsonParser().parse(requestdata);
			this.requestjson = new GsonObject(jsonElement.getAsJsonObject());
			// 解析头信息
			JsonObject pheadjson = requestjson.getJsonObject("phead");
			// 用户ip的处理
			if (!pheadjson.has("cip") || pheadjson.get("cip").isJsonNull()) {
				this.cip = getIpAddr(request);
				pheadjson.addProperty("cip", cip);
			} else {
				this.cip = pheadjson.get("cip").getAsString();
			}
			this.phead = new Gson().fromJson(pheadjson, Phead.class);
			// 校验头信息数据完整性
//			phead.validate();
		} else {
			this.cip = getIpAddr(request);
		}
	}

	/**
	 * 获取客户端的ip
	 */
	public static String getIpAddr(HttpServletRequest request) {
		// 针对　nginx获取ip
		String ip = request.getHeader("X-Real-IP");
		// 针对　一般tomcat获取ip
		if ((ip == null) || (ip.length() == 0)
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if ((ip == null) || (ip.length() == 0)
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteHost();
		}
		return ip;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("phead:");
		builder.append(phead);
		// builder.append("\nrequestData:");
		// builder.append(requestjson);
		builder.append("\nrequestParam:");
		builder.append(requestParam);
		return builder.toString();
	}

}
