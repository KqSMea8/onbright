package com.bright.apollo.dao.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TCreateTableSql;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月27日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface TCreateTableSqlMapper {

	/**  
	 * @param prefix
	 * @return  
	 * @Description:  
	 */
	@Select("select suffix from t_create_table_sql where prefix=#{prefix}")
	@Results(value = {
			@Result(property = "suffix",column = "suffix")
	})
	TCreateTableSql queryTCreateTableSqlByprefix(@Param("prefix") String prefix);

}
