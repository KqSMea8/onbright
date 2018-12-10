package com.bright.apollo.dao.device.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TLocationScene;
import com.bright.apollo.dao.sqlProvider.LocationSceneProvider;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月27日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface LocationSceneMapper {

	/**  
	 * @param tLocationScene
	 * @return  
	 * @Description:  
	 */
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	@InsertProvider(type = LocationSceneProvider.class, method = "addSceneLocation")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int addSceneLocation(TLocationScene tLocationScene);

	/**  
	 * @param sceneNumber
	 * @param location  
	 * @Description:  
	 */
	@Delete("delete from t_location_scene where scene_number=#{sceneNumber} and location_id=#{location}")
	void deleteSceneLocation(@Param("sceneNumber")Integer sceneNumber, @Param("location")Integer location);

	 

}
