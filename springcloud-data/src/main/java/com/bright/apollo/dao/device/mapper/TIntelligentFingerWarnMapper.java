package com.bright.apollo.dao.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TIntelligentFingerWarn;
import com.bright.apollo.dao.sqlProvider.IntelligentFingerRemoteUserDynaSqlProvider;
import com.bright.apollo.dao.sqlProvider.IntelligentFingerWarnDynaSqlProvider;
import com.bright.apollo.request.IntelligentFingerWarnDTO;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月7日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface TIntelligentFingerWarnMapper {

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@Select("select count(*) from t_intelligent_finger_warn where serialId=#{serialId}")
	Integer queryCountIntelligentWarnBySerialId(@Param("serialId")String serialId);

	/**  
	 * @param serialId
	 * @param end
	 * @param start
	 * @return  
	 * @Description:  
	 */
	@Select("select UNIX_TIMESTAMP(a.last_op_time)*1000 as timeStamp,a.`operation`  from t_intelligent_finger_warn a "
			+"where a.serialId=#{serialId} and a.last_op_time<=#{end} and a.last_op_time>=#{start} order by last_op_time desc")
	@Results(value = {
			@Result(property = "timeStamp",column = "timeStamp"),
			@Result(property = "operation",column = "operation")})
	List<IntelligentFingerWarnDTO> queryIntelligentWarnByDate(String serialId, String end, String start);

	/**  
	 * @param fingerWarn
	 * @return  
	 * @Description:  
	 */
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	@InsertProvider(type=IntelligentFingerWarnDynaSqlProvider.class,method="addTIntelligentFingerWarn")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int addTIntelligentFingerWarn(TIntelligentFingerWarn fingerWarn);

}
