package com.bright.apollo.dao.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

	/**  
	 * @param tableName  
	 * @Description:  
	 */
	@Update("DROP TABLE IF EXISTS ${tableName}")
	void dropTable(@Param("tableName") String tableName);

	/**  
	 * @param createTableSql  
	 * @Description:  
	 */
	@Update(value="${createTableSql}")
	void createTable(@Param("createTableSql")String createTableSql);

	/**  
	 * @param tCreateTableLog
	 * @return  
	 * @Description:  
	 */
	@Insert("insert into t_user_device (name) values(#{name})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
	int addCreateTableLog(TCreateTableLog tCreateTableLog);

}
