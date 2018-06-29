package com.bright.apollo.request;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import com.zz.common.util.CommonUtil;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月29日  
 *@Version:1.1.0  
 */
public class RequestParam {
	private Map<String, String[]> params = null;

	public RequestParam(Map<String, String[]> params) {
		this.params = params;
	}
	public RequestParam(HttpServletRequest request) {
		//
		this.params=getParameterMap(request);
	}
	public String getValue(String name) {
		if (params != null && params.get(name) != null) {
			return params.get(name)[0];
		}
		return null;
	}

	public String[] getValueArray(String name) {
		if (params != null && params.get(name) != null) {
			return params.get(name);
		}
		return null;
	}
	public void  remove(String name ) {
		if (params != null && params.get(name) != null) {
			params.remove(name);
		}
	}
	public int getInt(String name) {
		if (params != null && params.get(name) != null) {
			if (CommonUtil.isNumber(params.get(name)[0])) {
				return Integer.parseInt(params.get(name)[0]);
			}
		}
		return 0;
	}
/*
	@Override
	public String toString() {
		if (params == null || params.size() <= 0) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer();
			for (Entry<String, String[]> entry : params.entrySet()) {
				if(entry.getKey().equals("CMD")||entry.getKey().equals("access_token"))
					continue;
				sb.append("{'"+entry.getKey() + "':");
				sb.append(CommonUtil.splitJointValue(",", entry.getValue())
						+ "}");
			}
			return sb.toString();
		}
	}*/

	public Map<String, String[]> getParams() {
		return params;
	}

	public void setParams(Map<String, String[]> params) {
		this.params = params;
	}
	public static Map getParameterMap(HttpServletRequest request) {
	    // 鍙傛暟Map
	    Map properties = request.getParameterMap();
	    // 杩斿洖鍊糓ap
	    Map returnMap = new ConcurrentHashMap();
	    Iterator entries = properties.entrySet().iterator();
	    Map.Entry entry;
	    String name = "";
	    String value = "";
	    while (entries.hasNext()) {
	        entry = (Map.Entry) entries.next();
	        name = (String) entry.getKey();
	        Object valueObj = entry.getValue();
	        if(null == valueObj){
	            value = "";
	        }else if(valueObj instanceof String[]){
	            String[] values = (String[])valueObj;
	            for(int i=0;i<values.length;i++){
	                value = values[i] + ",";
	            }
	            value = value.substring(0, value.length()-1);
	        }else{
	            value = valueObj.toString();
	        }
	        returnMap.put(name, value);
	    }
	    return returnMap;
	}
}
