package com.bright.apollo.dao.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月7日  
 *@Version:1.1.0  
 */
@Mapper
@Component	
public interface TIntelligentDFingerRemoteUserMapper {

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@Select("SELECT * from t_intelligent_finger_remote_user  where serialId=#{serialId} order by last_op_time desc ")
	@Results(value = { @Result(property = "userSerialid", column = "user_serialId"),
			@Result(property = "mobile", column = "mobile"),
			@Result(property = "serialid", column = "serialId"),
			@Result(property = "nickName", column = "nick_name"),
			@Result(property = "startTime", column = "start_time"),
			@Result(property = "endTime", column = "end_time"),
			@Result(property = "times", column = "times"),
			@Result(property = "useTimes", column = "use_times"),
			@Result(property = "pwd", column = "pwd"),
			@Result(property = "isend", column = "isEnd"),
			@Result(property = "ismax", column = "isMax"),
			@Result(property = "lastOpTime", column = "last_op_time")})
	List<TIntelligentFingerRemoteUser> queryIntelligentFingerRemoteUsersBySerialId(@Param("serialId") String serialId);

}
