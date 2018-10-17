package com.bright.apollo.dao.sqlProvider;

import java.util.Map;

public class OboxDeviceConfigSqlProvider {

    public String getAllOboxDeviceConfig(Map<String, Object> parameters){
        String deviceType = (String)parameters.get("deviceType");
        StringBuffer sqlBuffer = new StringBuffer();
        if(deviceType.equals("ff")){
            sqlBuffer.append("SELECT * FROM t_obox_device_config");
        }else{
            sqlBuffer.append("SELECT * FROM t_obox_device_config " +
                    "where device_type = "+deviceType);
        }
        return sqlBuffer.toString();
    }
}
