package com.bright.apollo.dao.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TIntelligentFingerPush;

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

}
