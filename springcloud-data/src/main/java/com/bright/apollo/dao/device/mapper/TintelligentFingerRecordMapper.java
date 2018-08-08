package com.bright.apollo.dao.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.bright.apollo.request.IntelligentOpenRecordDTO;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年8月6日
 * @Version:1.1.0
 */
@Mapper
@Component
public interface TintelligentFingerRecordMapper {

	/**
	 * @param serialId
	 * @param end
	 * @param start
	 * @return
	 * @Description:
	 */
	@Select("select UNIX_TIMESTAMP(a.last_op_time)*1000 as timeStamp,a.nick_name as nickName,a.`operation`  from t_intelligent_finger_record a "
			+"where a.serialId=#{serialId} and a.last_op_time<=#{end} and a.last_op_time>=#{start} order by last_op_time desc")
	@Results(value = {
			@Result(property = "timeStamp",column = "timeStamp"),
			@Result(property = "nickName",column = "nickName"),
			@Result(property = "operation",column = "operation")})
	List<IntelligentOpenRecordDTO> queryIntelligentOpenRecordByDate(@Param(value = "serialId") String serialId,
			@Param(value="end")String end, @Param(value="start")String start);

}
