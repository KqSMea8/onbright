package com.bright.apollo.dao.device.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TYSCamera;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月28日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface TYSCameraMapper {

	/**  
	 * @param deviceSerialId
	 * @return  
	 * @Description:  
	 */
	@Select("select * from t_ys_camera where deviceSerial= #{deviceSerialId}")
	@Results(value = {
			@Result(property = "id",column = "id"),
			@Result(property = "channelName",column = "channelname"),
			@Result(property = "deviceSerial",column = "deviceserial"),
			@Result(property = "deviceType",column = "devicetype"),
			@Result(property = "status",column = "status"),
			@Result(property = "isShared",column = "isshared"),
			@Result(property = "deviceVersion",column = "deviceversion"),
			@Result(property = "last_op_time",column = "lastOpTime"),
			@Result(property = "license",column = "license"),
			@Result(property = "picUrl",column = "picurl"),
			@Result(property = "isEncrypt",column = " isencrypt"),
			@Result(property = "videoLevel",column = " videolevel")
	})
	TYSCamera getYSCameraBySerialId(String deviceSerialId);

}
