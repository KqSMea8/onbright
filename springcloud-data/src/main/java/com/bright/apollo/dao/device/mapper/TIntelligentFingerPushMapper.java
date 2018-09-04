package com.bright.apollo.dao.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TIntelligentFingerPush;
import com.bright.apollo.dao.device.sqlProvider.OboxDeviceConfigSqlProvider;
import com.bright.apollo.dao.device.sqlProvider.TIntelligentFingerPushSqlProvider;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年8月9日
 * @Version:1.1.0
 */
@Mapper
@Component
public interface TIntelligentFingerPushMapper {

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@Select("select * from t_intelligent_finger_push a where a.serialId=#{serialId}")
	List<TIntelligentFingerPush> queryTIntelligentFingerPushsBySerialId(@Param("serialId") String serialId);

	/**
	 * @param mobile
	 * @param serialId
	 * @Description:
	 */
	@Update("update t_intelligent_finger_push set mobile=#{mobile} where serialId =#{serialId} ")
	void updateTIntelligentFingerPushMobileBySerialId(@Param("mobile") String mobile,
			@Param("serialId") String serialId);

	/**
	 * @param enable
	 * @param serialId
	 * @param value
	 * @Description:
	 */
	@Update("update t_intelligent_finger_push set `enable`=#{enable}  where serialId =#{serialId} and `value`=#{value}")
	void updateTIntelligentFingerPushEnableBySerialIdAndValue(@Param("enable") Integer enable,
			@Param("serialId") String serialId, @Param("value") Integer value);

	/**  
	 * @param serialId
	 * @param cmd
	 * @return  
	 * @Description:  
	 */
	@Select("select * from t_intelligent_finger_push a where a.serialId=#{serialId} and a.cmd=#{cmd}")
	@Results(value = { 
			@Result(property = "mobile", column = "mobile"),
			@Result(property = "id", column = "id"),
			@Result(property = "serialid", column = "serialId"),
			@Result(property = "cmd", column = "cmd"),
			@Result(property = "value", column = "value"),
			@Result(property = "enable", column = "enable"),
			@Result(property = "lastOpTime", column = "last_op_time")})
	TIntelligentFingerPush queryTIntelligentFingerPushBySerialIdAndCmd(@Param("serialId")String serialId, @Param("cmd")String cmd);

	/**  
	 * @param pushList
	 * @param serialId  
	 * @Description:  
	 */
	@InsertProvider(type = TIntelligentFingerPushSqlProvider.class, method = "batchTIntelligentFingerPush")
	void batchTIntelligentFingerPush(List<TIntelligentFingerPush> pushList, @Param("serialId")String serialId);

}
