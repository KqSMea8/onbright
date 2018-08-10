package com.bright.apollo.dao.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TIntelligentFingerAbandonRemoteUser;
import com.bright.apollo.dao.device.sqlProvider.IntelligentFingerRemoteUserDynaSqlProvider;
import com.bright.apollo.dao.device.sqlProvider.IntelligentFingerabandonRemoteUserDynaSqlProvider;

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

	/**  
	 * @param abandonRemoteUser
	 * @return  
	 * @Description:  
	 */
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	@InsertProvider(type=IntelligentFingerabandonRemoteUserDynaSqlProvider.class,method="addIntelligentFingerAbandonRemoteUser")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	Integer addIntelligentFingerAbandonRemoteUser(TIntelligentFingerAbandonRemoteUser abandonRemoteUser);

}
