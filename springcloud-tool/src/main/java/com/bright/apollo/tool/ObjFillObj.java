package com.bright.apollo.tool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月12日
 * @Version:1.1.0
 */
public class ObjFillObj {
	public static <T> T FillObj(T fillObj, T filldObj)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class userCla = (Class) fillObj.getClass();
		Method[] methods = userCla.getMethods();
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (method.getName().startsWith("get") && !method.getName().equals("getClass")
					&& method.invoke(fillObj) != null) {
				if (method.invoke(filldObj) == null) {
					map.put(method.getName().toLowerCase(), method.invoke(fillObj));
				} else if (!compare(method.invoke(fillObj), method.invoke(filldObj))) {
					map.put(method.getName().toLowerCase(), method.invoke(fillObj));
				}
			}
		}
		if (map != null&&!map.isEmpty()) {
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (method.getName().startsWith("set")) {
					String methodName = method.getName().substring(3);
					if(map.containsKey("get"+methodName)){
						method.invoke(filldObj, map.get("get"+methodName));
					}
				}
			}
		}
		return filldObj;
	}

	private static boolean compare(Object t, Object m) {
		if ((t instanceof String) && (m instanceof String))
			return t.equals(m);
		else if ((t instanceof Integer) && (m instanceof Integer))
			return ((Integer) t).intValue() == ((Integer) m).intValue();
		else if ((t instanceof Long) && (m instanceof Long))
			return ((Long) t).longValue() == ((Long) m).longValue();
		else if ((t instanceof Float) && (m instanceof Float))
			return ((Float) t).floatValue() == ((Float) m).floatValue();
		else if ((t instanceof Double) && (m instanceof Double))
			return ((Double) t).doubleValue() == ((Double) m).doubleValue();
		return false;
	}

}
