package com.bright.apollo.dao.user.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TException;
import com.bright.apollo.dao.sqlProvider.ExceptionProvider;
import com.bright.apollo.dao.sqlProvider.TMsgProvider;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月19日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface ExceptionMapper {

	/**  
	 * @param tException
	 * @return  
	 * @Description:  
	 */
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	@InsertProvider(type = ExceptionProvider.class, method = "addException")
	int addException(TException tException);

}
