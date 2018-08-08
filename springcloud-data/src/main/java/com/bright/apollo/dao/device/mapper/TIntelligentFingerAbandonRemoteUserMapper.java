package com.bright.apollo.dao.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TIntelligentFingerAbandonRemoteUser;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年8月8日
 * @Version:1.1.0
 */
@Mapper
@Component
public interface TIntelligentFingerAbandonRemoteUserMapper {

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@Select("select * from t_intelligent_finger_abandon_remote_user a where a.serialId=#{serialId}")
	List<TIntelligentFingerAbandonRemoteUser> queryTIntelligentFingerAbandonRemoteUsersBySerialId(
			@Param("serialId") String serialId);

	/**  
	 * @param id  
	 * @Description:  
	 */
	@Delete("delete from t_intelligent_finger_abandon_remote_user where id=#{id}")
	void delIntelligentFingerAbandonRemoteUserById(@Param("id")Integer id);

}
