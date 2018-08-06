package com.bright.apollo.dao.device.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月6日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface TIntelligentFingerAuthMapper {

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@Select("select count(*) from t_intelligent_finger_auth where serialId=#{serialId}")
	Integer countFingerAuth(@Param(value="serialId")String serialId);

}
