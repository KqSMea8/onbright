package com.bright.apollo.dao.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TRemoteLed;
import com.bright.apollo.dao.sqlProvider.TIntelligentFingerPushSqlProvider;
import com.bright.apollo.dao.sqlProvider.TRemoteLedSqlProvider;
import com.bright.apollo.dao.sqlProvider.UserProvider;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月11日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface TRemoteLedMapper {

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@Select("select * from t_remote_led where serialId=#{serialId}")
	@Results(value = { @Result(column = "channel", property = "channel"),
			@Result(column = "name", property = "name"),
			@Result(column = "id", property = "id"),
			@Result(column = "serialId", property = "serialid"),
			@Result(column = "last_op_time", property = "lastOpTime")})
	List<TRemoteLed> getListBySerialId(@Param("serialId")String serialId);

	/**  
	 * @param list  
	 * @Description:  
	 */
	@InsertProvider(type = TRemoteLedSqlProvider.class, method = "batchRemoteLeds")
	void batchRemoteLeds(@Param("list")List<TRemoteLed> list);

	/**  
	 * @param updateList  
	 * @Description:  
	 */
	@Deprecated
	void batchUpdateRemotes(List<TRemoteLed> updateList);

	/**  
	 * @param remoteLed  
	 * @Description:  
	 */
	@UpdateProvider(type = TRemoteLedSqlProvider.class, method = "updateRemote")
	void updateRemote(TRemoteLed remoteLed);

	/**  
	 * @param remoteLed  
	 * @Description:  
	 */
	@InsertProvider(type = TRemoteLedSqlProvider.class, method = "addRemoteLed")
	void addRemoteLed(TRemoteLed remoteLed);

}
