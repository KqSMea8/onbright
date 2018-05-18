package com.bright.apollo.session;

import com.zz.common.util.CommonUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    public void setValueNull(String name ) {
        if(params==null)
            params=new HashMap<String, String[]>();
        params.put(name, null);
    }
    public void setValue(String name,String value ) {
        if(params==null)
            params=new HashMap<String, String[]>();
        String[]array={value};
        params.put(name, array);
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


    public Map<String, String[]> getParams() {
        return params;
    }

    public void setParams(Map<String, String[]> params) {
        this.params = params;
    }
    public static Map getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        Map returnMap = new HashMap();
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
