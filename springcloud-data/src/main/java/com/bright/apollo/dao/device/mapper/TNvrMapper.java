package com.bright.apollo.dao.device.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TNvr;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月28日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface TNvrMapper {

	/**  
	 * @param ip
	 * @return  
	 * @Description:  
	 */
	@Select("select * from t_nvr where ip= #{ip}")
	@Results(value = {
			@Result(property = "id",column = "id"),
			@Result(property = "ip",column = "ip"),
			@Result(property = "port",column = "port"),
			@Result(property = "user",column = "user"),
			@Result(property = "pw",column = "pw"),
			@Result(property = "license",column = "license"),
			@Result(property = "last_op_time",column = "lastOpTime"),
	})
	TNvr getNvrByIP(String ip);

	 
}
