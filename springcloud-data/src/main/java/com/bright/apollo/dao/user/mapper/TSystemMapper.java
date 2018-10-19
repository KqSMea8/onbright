package com.bright.apollo.dao.user.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TSystem;
import com.bright.apollo.dao.sqlProvider.SystemProvider;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月18日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface TSystemMapper {

	/**  
	 * @param tSystem
	 * @return  
	 * @Description:  
	 */
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	@InsertProvider(type = SystemProvider.class, method = "addSystem")
	int addSystem(TSystem tSystem);

}
