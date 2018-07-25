package com.bright.apollo.dao.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TCreateTableLog;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月25日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface TCreateTableLogMapper {

	/**  
	 * @param tUserOperationSuffix
	 * @return  
	 * @Description:  
	 */
	@Select("select name from t_create_table_log where name like  CONCAT('%',#{tUserOperationSuffix},'%') order by last_op_time desc")
	@Results(value = {
			@Result(property = "id",column = "id"),
 			@Result(property = "name",column = "name")
	})
	List<TCreateTableLog> listCreateTableLogByNameWithLike(@Param("tUserOperationSuffix")  String tUserOperationSuffix);

}
