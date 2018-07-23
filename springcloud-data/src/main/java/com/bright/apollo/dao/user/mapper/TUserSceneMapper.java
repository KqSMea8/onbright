package com.bright.apollo.dao.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TUserScene;
import com.bright.apollo.common.entity.TUserSceneExample;
import com.bright.apollo.dao.mapper.base.BaseMapper;

@Mapper
@Component
public interface TUserSceneMapper extends BaseMapper<TUserScene, TUserSceneExample, Integer> {

    @Delete("delete from t_user_scene where scene_number = #{sceneNumber}")
    void deleteUserSceneBySceneNum(@Param("sceneNumber") int sceneNumber);

    @Select("select * from t_user_scene where scene_number = #{sceneNumber}")
    List<TUserScene> getUserSceneBySceneNum(@Param("sceneNumber") int sceneNumber);

	/**  
	 * @param tUserScene
	 * @return  
	 * @Description:  
	 */
    @Insert("insert into t_user_scene (scene_number,user_id) values(#{sceneNumber},#{userId})")
	void addUserScene(TUserScene tUserScene);

	/**  
	 * @param userId
	 * @param sceneNumber
	 * @return  
	 * @Description:  
	 */
    @Select("select * from t_user_scene where scene_number = #{sceneNumber} and user_id=#{userId}")
    TUserScene getUserSceneByUserIdAndSceneNumber(@Param("userId") Integer userId,@Param("sceneNumber") Integer sceneNumber);
}